package com.mago.db;

import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Driver;

public class DBConnectionManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5401569561900503006L;
	private static DBConnectionManager connectionManager = null;
	private static DBConnectionParameter parameters = null;
	private static DBConnectionPool connectionPool = new DBConnectionPool();
	private static Driver jdbcDriver = null;
	private Logger logger = Logger.getLogger(DBConnectionManager.class);
	
	private DBConnectionManager(){
	}
	
	public static DBConnectionManager getInstance(){
		if (connectionManager == null){
			connectionManager = new DBConnectionManager();
		}
		return connectionManager;
	}
	
	public void initDB(String dbURL, String dbName, String user, String pass, int connectionAmount, long keepAlive){
		if (parameters == null){
			try {
				jdbcDriver = new Driver();
				DriverManager.registerDriver(jdbcDriver);
			} catch (SQLException e) {
				logger.error("Register JDBC driver failed: " + e);
			}
			parameters = new DBConnectionParameter(dbURL, dbName, user, pass, connectionAmount);
			int poolSize = parameters.getConnectionAmount();
			for (int i = 0; i<poolSize; i++){
				try{
					DBConnection connection = new DBConnection(parameters, i);
					connectionPool.push(connection);
				} catch (DBException dbe){
					logger.error("Create connection [" + i + "] failed: " + dbe);
				}
			}
		}
	}
	
	public synchronized DBConnection getConnection(){
		
		DBConnection connection = connectionPool.pop();
		if (connection != null){
			logger.debug("Connection [" + connection.getConnectionId() + "] is fetched from connection pool and will be used for the following operation.");
		} else {
			logger.warn("Connection pool is fully occupied, there must be some operations which does not release the DB connections, report to system operator!");
			try{
				DBConnection tmpConnection = new DBConnection(parameters, 100);
				logger.debug("Connection [100] is used for the following operation.");
				return tmpConnection;
			} catch (DBException dbe){
				logger.error("Create temp connection failed: " + dbe);
			}
		}

		return connection;
	}
	
	public void returnConnection(DBConnection connection){
		logger.debug("Connection [" + connection.getConnectionId() + "] is returned to connection pool.");
		connectionPool.push(connection);
	}
	
	private void closeDBConnection(){
		Iterator<DBConnection> its = connectionPool.iterator();
		while (its.hasNext()){
			DBConnection conn = its.next();
			int connectionId = conn.getConnectionId();
			try{
				boolean conStatus = conn.getStatus();
				conn.close();
				logger.debug("Connection [" + connectionId + ":" + conStatus + "] is closed!");
			} catch (SQLException sqle){
				logger.error("Close DB connection [" + connectionId + "] with exception: " + sqle);
			}
			
		}
	}
	
	public void close(){
		
		logger.debug("Stopping DB connection pool......");
		closeDBConnection();
		connectionPool.clear();
		connectionPool = null;
		if (jdbcDriver != null) {
			try {
				DriverManager.deregisterDriver(jdbcDriver);
			} catch (SQLException e) {
				logger.error("Deregister JDBC driver failed: " + e);
			}
		}
		logger.debug("DB connection pool is successfully stopped.");

	}
}
