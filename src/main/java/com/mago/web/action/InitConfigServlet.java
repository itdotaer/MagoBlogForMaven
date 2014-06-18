package com.mago.web.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mago.db.DBConnectionManager;
import com.mago.util.session.SessionManagerUtil;

public class InitConfigServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8434860465911515788L;
	
	private Logger logger = Logger.getLogger(InitConfigServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");
		dispatcher.forward(req, resp);
		
	}
	
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		//init DB connection
		logger.info("Starting initiate DB connection......");
		String dbURL = config.getInitParameter("DB_URL");
		String dbName = config.getInitParameter("DB_NAME");
		String user = config.getInitParameter("Username");
		String pass = config.getInitParameter("Password");
		String connectionAmount = config.getInitParameter("Connection_Amount");
		String expireTime = config.getInitParameter("Session_Expire_Time");
		String keepaliveInterval = config.getInitParameter("DB_KEEP_ALIVE");
		
		int cAmount = 10;
		try{
			cAmount = Integer.parseInt(connectionAmount);
		} catch (Exception e){
			logger.info("Connection amount is invalid, user default[10].");
		}
		
		long keepalive = 28000;
		try{
			keepalive = Long.parseLong(keepaliveInterval);
			logger.info("DB connection keepalive is set on : " + keepalive + " minutes.");
		} catch (NumberFormatException nfe){
			logger.warn("Invalid DB connection keepalive(minutes) is assigned: " + keepaliveInterval + ", ignore and use DB connection keepalive[60 minutes]!");
		}		
		
		DBConnectionManager.getInstance().initDB(dbURL, dbName, user, pass, cAmount, keepalive * 60L);
		logger.info("Initiate DB connection finished. Number of connections to DB: " + cAmount);
		
		long expire = 60;
		try{
			expire = Long.parseLong(expireTime);
			logger.info("Session Expire Period is set on : " + expire + " minutes.");
		} catch (NumberFormatException nfe){
			logger.warn("Invalid session expire time(minutes) is assigned: " + expireTime + ", ignore and use default expire time[60 minutes]!");
		}		

		SessionManagerUtil.setExpireTime(expire * 60L * 1000L);

	}
}
