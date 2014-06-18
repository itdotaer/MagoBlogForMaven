package com.mago.db;

public class DBName {
	
	public static final int CLASS = 0;
	public static final int ARTICLE_BASE = 1;
	public static final int ARTICLE_EXT = 2;
	public static final int USER_BASE = 3;
	public static final int USER_EXT = 4;
	public static final int NOTICE_INFO = 5;
	public static final int DETAIL_PATROL_REPORT = 6;
	public static final int PARTOL_RECORD = 7;
	public static final int RESCUE_INFO = 8;
	public static final int ROAD_CLEAR_RECORD = 9;
	public static final int SAFETY_RISK_INFO = 10;
	public static final int MONTHLY_ROADCLEAR = 11;
	
	private static String[] tableArray = new String[]{
		"classe","article_base", "article_ext", "user_base", "user_ext", "notice_info", 
		"detail_daily_patrol_report", "daily_patrol_record", "rescue_info", "road_clear_record", "safety_risk_info","monthly_road_clear_record"};
	
	public static String getTableName(int tableIndex){
		if (tableIndex < 0 || tableIndex >= tableArray.length){
			return null;
		}
		return tableArray[tableIndex];
	}
	
}
