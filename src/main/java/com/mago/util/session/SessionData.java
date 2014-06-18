package com.mago.util.session;

public class SessionData {
	
	private String sessionId;
	private String httpSessionId;
	private int operatorId = -1;
	private String username;
	private int level = 0;
	private String password;
	private String postfix;
	private long ts_last;
	private int sessionCriteriaValue = -1;
	private String sessionCriteriaKey = "none";
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	public long getTs_last() {
		return ts_last;
	}
	public void setTs_last(long ts_last) {
		this.ts_last = ts_last;
	}
	
	public String toString(){
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SessionData: [ ")
		      .append("sessionId=").append(sessionId).append(" | ")
		      .append("operationId=").append(operatorId).append(" | ")
		      .append("username=").append(username).append(" | ")
		      .append("ts_last=").append(ts_last).append(" | ")
		      .append("criteria={").append(sessionCriteriaKey).append(":").append(sessionCriteriaValue).append("}")
		      .append(" ]");
		return buffer.toString();
		
	}
	public String getSessionCriteriaKey() {
		return sessionCriteriaKey;
	}
	public void setSessionCriteriaKey(String sessionCriteriaKey) {
		this.sessionCriteriaKey = sessionCriteriaKey;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	
	public int getSessionCriteriaValue() {
		return sessionCriteriaValue;
	}
	public void setSessionCriteriaValue(int sessionCriteriaValue) {
		this.sessionCriteriaValue = sessionCriteriaValue;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	public String getPostfix() {
		return postfix;
	}
	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}
	public String getHttpSessionId() {
		return httpSessionId;
	}

}
