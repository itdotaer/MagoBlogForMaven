package com.mago.db;

public class SQLStatementQuery {
	
	public final static String SPACE = " ";

	public static String getQueryAllPrefix(){
		return "select * from ";
	}
	
	public static String getQuerySpecialField(String[] fieldArray){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select ");
		for (int i = 0; i<fieldArray.length; i++){
			if (i + 1 < fieldArray.length){
				buffer.append(fieldArray[i]).append(",");
			} else {
				buffer.append(fieldArray[i]);
			}
		}
		buffer.append(" from ");
		return buffer.toString();
	}
	
	public static String insert(){
		return "insert into ";
	}
	
	public static String update(){
		return "update ";
	}
	
	public static String delete(){
		return "delete from ";
	}
	
	public static String getColumnSpecifiyQueryPrefix(String column){
		return "select " + column +  " from ";
	}
	
	public synchronized static String getQueryCount(String asString){
		
		String result = "";
		if (asString == null || asString.trim().equals("")) {
			result = "select count(*) from ";
		} else {
			result = "select count(*) as " + asString + " from ";
		}
		return result;
	}
	
}
