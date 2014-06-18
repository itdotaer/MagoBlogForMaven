package com.mago.base;

import java.io.Serializable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.mago.bean.Article;
import com.mago.bean.ArticleList;
import com.mago.bean.Classe;

public class SessionOper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7248245340872965033L;

	public static boolean IsContain(HttpSession session, String arg)
	{
		Object sessionObject = session.getAttribute(arg);
		
		if(sessionObject == null)
		{
			return false;
		}
		
		return true;
	}
	
	public static boolean IsContainArticle(HttpSession session, String arg, int articleId){
		Article article = (Article) session.getAttribute(arg);
		
		if(article == null){
			return false;
		}else{
			
			if(articleId == article.getArticleId()){
				return true;
			}
			
			return false;
		}		
		
	}
	
	public static boolean IsContainClassList(HttpSession session, String arg, int classId){
		@SuppressWarnings("unchecked")
		Vector<ArticleList> vec = (Vector<ArticleList>)session.getAttribute(arg);
		
		if(vec == null || vec.size() == 0){
			return false;
		}else{
			
			if(classId == vec.elementAt(0).getClassId()){
				return true;
			}
			
			return false;
		}		
		
	}
	
	public static Vector<Vector<Classe>> SortClassById(Vector<Classe> arg)
	{
		Vector<Vector<Classe>> vec = new Vector<Vector<Classe>>();
		Classe bean = null;
		Classe subBean = null;
		
		for(int i = 0; i< arg.size(); i++){
			bean = arg.elementAt(i);
			if(bean.getParentId() == 0){
				Vector<Classe> subVec = new Vector<Classe>();
				subVec.add(bean);
				int id = bean.getClassId();
				
				for(int j = 0; j < arg.size(); j++){
					
					subBean = arg.elementAt(j);
					
					if(subBean.getParentId() == id){
						
						subVec.add(subBean);
						
					}
				}
				vec.add(subVec);
			}
		}
		
		return vec;
	}
	
	public static String queryClassNameByClassId(Vector<Classe> arg, int classId){
		String rs = "";
		Classe bean = null;
		for(int i = 0; i < arg.size(); i++){
			bean = arg.elementAt(i);
			if(bean.getClassId() == classId){
				rs = bean.getClassName();
			}
		}
		
		return rs;
	}
	
}
