package com.mago.web.action;

import java.io.Serializable;
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

public class GetListAction extends Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5722072226627877243L;
	private Logger logger = Logger.getLogger(GetListAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String classPidStr = request.getParameter("pId");
		String classSubIdStr = request.getParameter("subId");
		int classPid = Integer.parseInt(classPidStr);
		int classSubId = 0;
		
		int classId = classPid;
		
		if(classSubIdStr != "" && classSubIdStr != null){
			classSubId = Integer.parseInt(classSubIdStr);
			classId = classSubId;
		}
		
		HttpSession session = request.getSession();
		ActionForward forward = new ActionForward();
		
		if(!SessionOper.IsContainClassList(session, "classArticleList", classId)){
			DBConnection conn = DBConnectionManager.getInstance().getConnection();
			
			if(conn == null)
			{
				logger.error("Cann't create the connection to DataBase!");			
				session.setAttribute("errorMessage", ExceptionMessage.CON_ERROR);				
				forward.setPath("/error.jsp");
				
			}else{
				logger.debug("Get articles list from DataBase!");				
				Vector<ArticleList> classArticleVector = conn.queryArticleListByClassId(classId);
				logger.debug("Successed to get articles list from DataBase!");
				
				if(SessionOper.IsContain(session, "classArticleList")){
					session.removeAttribute("classArticleList");
				}
				
				session.setAttribute("classArticleList", classArticleVector);
				forward.setPath("/showList.jsp");
			}			
			
		}else{
			forward.setPath("/showList.jsp");
		}
		
		return forward;
	}
}
