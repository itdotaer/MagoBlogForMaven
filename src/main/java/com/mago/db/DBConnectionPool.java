package com.mago.db;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DBConnectionPool implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static List<DBConnection> connectionPool = Collections.synchronizedList(new LinkedList<DBConnection>());
	
	public void push(DBConnection connection){
		connection.returned();
		connectionPool.add(connection);
	}
	
	public synchronized DBConnection pop(){
		if (connectionPool.size() > 0){
			DBConnection connection = connectionPool.remove(0);
			connection.occupied();
			return connection;
		} else {
			return null;
		}
	}
	
	public void clear(){
		connectionPool.clear();
	}
	
	public Iterator<DBConnection> iterator(){
		return connectionPool.iterator();
	}

}
