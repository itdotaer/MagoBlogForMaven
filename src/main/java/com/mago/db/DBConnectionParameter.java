package com.mago.db;

import java.io.Serializable;

public class DBConnectionParameter implements Serializable{

	private static final long serialVersionUID = 1L;
	private String m_url = "jdbc:mysql://localhost:3306/";
	private String m_username = "ets_sl";
	private String m_password = "ets_sl";
	private String m_db = "ets_sl";
	private int m_connectionAmount = 10;
	
	public DBConnectionParameter(String url, String dbName, String user, String pass, int connectionSize){
		m_url = url;
		m_username = user;
		m_password = pass;
		m_db = dbName;
		m_connectionAmount = connectionSize;
	}
	
	public String getURL(){
		return m_url;
	}
	
	public String getUsername(){
		return m_username;
	}
	
	public String getPassword(){
		return m_password;
	}
	
	public int getConnectionAmount(){
		return m_connectionAmount;
	}
	
	public String getDBName(){
		return m_db;
	}
}
