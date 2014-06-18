package com.mago.util.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SessionManagerUtil {
	
	private static HashMap<String, SessionData> sessionDataMap = new HashMap<String, SessionData>();
	@SuppressWarnings("unused")
	private static long expireTime = 3600000L;

	public static void setExpireTime(long eTime){
		expireTime = eTime;
	}
	
	public synchronized static SessionData getSession(String sessionId){
		return sessionDataMap.get(sessionId);
	}
	
	public synchronized static int getOperatorId(String sessionId){
		int operatorId = -1;
		SessionData sessionData = sessionDataMap.get(sessionId);
		if (sessionData != null){
			operatorId = sessionData.getOperatorId();
		}
		return operatorId;
	}
	
	public synchronized static String getUsername(String sessionId){
		String username = null;
		SessionData sessionData = sessionDataMap.get(sessionId);
		if (sessionData != null){
			username = sessionData.getUsername();
		}
		return username;
	}
	
	public synchronized static int getUserLevel(String sessionId){
		int level = 0;
		SessionData sessionData = sessionDataMap.get(sessionId);
		if (sessionData != null){
			level = sessionData.getLevel();
		}
		return level;
	}
	
	public synchronized static String getCriteriaKey(String sessionId){
		String criteriaKey = null;
		SessionData sessionData = sessionDataMap.get(sessionId);
		if (sessionData != null){
			criteriaKey = sessionData.getSessionCriteriaKey();
		}
		return criteriaKey;
	}
	
	public synchronized static int getCriteriaValue(String sessionId){
		int criteria = -1;
		SessionData sessionData = sessionDataMap.get(sessionId);
		if (sessionData != null){
			criteria = sessionData.getSessionCriteriaValue();
		}
		return criteria;
	}
	
	public synchronized static void removeSession(String sessionId){
		sessionDataMap.remove(sessionId);
	}
	
	public synchronized static void removeSession(int operatorId){
		
		String sessionId = null;
		Collection<SessionData> sessionDatas = sessionDataMap.values();
		Iterator<SessionData> sessionDataIts = sessionDatas.iterator();
		while (sessionDataIts.hasNext()){
			SessionData data = sessionDataIts.next();
			if (data.getOperatorId() == operatorId){
				sessionId = data.getSessionId();
				break;
			}
		}
		if (sessionId != null) {
			sessionDataMap.remove(sessionId);
		}
	}
	
//	public synchronized static String getSession(int operatorId, User user){
//		
//		String sessionId = null;
//		Collection<SessionData> sessionDatas = sessionDataMap.values();
//		Iterator<SessionData> sessionDataIts = sessionDatas.iterator();
//		boolean hasIt = false;
//		while (sessionDataIts.hasNext()){
//			SessionData data = sessionDataIts.next();
//			if (data.getOperatorId() == operatorId){
//				hasIt = true;
//				data.setTs_last(System.currentTimeMillis());
//				sessionId = data.getSessionId();
//				break;
//			}
//		}
//		
//		if (!hasIt){
//			sessionId = System.currentTimeMillis() + "-" + user.getLogin();
//			SessionData data = new SessionData();
//			data.setSessionId(sessionId);
//			data.setOperatorId(operatorId);
//			data.setPassword(user.getUserPass());
//			data.setLevel(user.getLevel());
//			data.setUsername(user.getLogin());
//			data.setTs_last(System.currentTimeMillis());
//			sessionDataMap.put(sessionId, data);
//		}
//		return sessionId;
//		
//	}
	
//	public synchronized static int validateSession(String sessionId){
//		
//		int result = Constants.ResultCodes.RESULT_SUCCESS;
//		SessionData sessionData = sessionDataMap.get(sessionId);
//		if (sessionData == null){
//			result = Constants.CommonResultCodes.ERROR_SESSION_EXPIRED;
//		} else {
////			long ts_diff = System.currentTimeMillis() - sessionData.getTs_last();
////			if (ts_diff > expireTime){
////				sessionDataMap.remove(sessionId);
////				result = Constants.CommonResultCodes.ERROR_SESSION_EXPIRED;
////			} else {
////				sessionData.setTs_last(System.currentTimeMillis());
////			}
//			sessionData.setTs_last(System.currentTimeMillis());
//		}
//		
//		return result;
//	}
	
	public static String listSessions(){
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("Sessions:\n");
		Set<String> keySet = sessionDataMap.keySet();
		Iterator<String> keyIt = keySet.iterator();
		while (keyIt.hasNext()){
			String key = keyIt.next();
			SessionData data = sessionDataMap.get(key);
			buffer.append("key=").append(key).append(" ").append(data.toString()).append("\n");
		}
		
		return buffer.toString();
		
	}
	
}
