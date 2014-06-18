package com.mago.web.action;

import java.util.Vector;

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
import com.mago.bean.ArticleList;
import com.mago.db.DBConnection;
import com.mago.db.DBConnectionManager;

public class GetBlogCarouselAction extends Action {
	
	private Logger logger = Logger.getLogger(GetBlogCarouselAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		ActionForward forward = new ActionForward();
		
		if(!SessionOper.IsContain(session, "mostViewedArticleVec")){
			DBConnection conn = DBConnectionManager.getInstance().getConnection();
			
			if(conn == null)
			{
				logger.error("Cann't create the connection to DataBase!");			
				session.setAttribute("errorMessage", ExceptionMessage.CON_ERROR);
				
				forward.setPath("/error.jsp");
				return forward;
				
			}else{
				logger.debug("Get the most viewed articles from DataBase!");				
				Vector<ArticleList> mostViewedArticleVector = conn.queryArticleListByViewNum(100, 10);
				logger.debug("Successed to get most viewed articles from DataBase!");
				
				session.setAttribute("mostViewedArticleVec", mostViewedArticleVector);
			}		
		}
		
		return null;
	}

}
