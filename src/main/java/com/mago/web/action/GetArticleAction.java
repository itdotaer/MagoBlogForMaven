package com.mago.web.action;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mago.base.ExceptionMessage;
import com.mago.base.SessionOper;
import com.mago.bean.Article;
import com.mago.db.DBConnection;
import com.mago.db.DBConnectionManager;

public class GetArticleAction extends Action implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2514599908438689303L;
	private Logger logger = Logger.getLogger(GetArticleAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String articleIdStr = request.getParameter("articleId");
		int articleId = Integer.parseInt(articleIdStr);
		
		HttpSession session = request.getSession();
		ActionForward forward = new ActionForward();;
		
		if(!SessionOper.IsContainArticle(session, "article", articleId)){
			DBConnection conn = DBConnectionManager.getInstance().getConnection();
			
			if(conn == null)
			{
				logger.error("Cann't create the connection to DataBase!");
				
				session.setAttribute("errorMessage", ExceptionMessage.CON_ERROR);
				
				forward.setPath("/error.jsp");
				
			}else{
				logger.debug("Get the article according the articleId from DataBase!");
				Article article = conn.queryArticle(articleId);
				logger.debug("Successed to get the article from DataBase!");
				
				if(SessionOper.IsContain(session, "article")){
					session.removeAttribute("article");
				}
				
				session.setAttribute("article", article);
				forward.setPath("/showDetail.jsp");
			}			
			
		}else{
			forward.setPath("/showDetail.jsp");
		}
		
		return forward;
	}

}
