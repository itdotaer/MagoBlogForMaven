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
import com.mago.bean.PicList;
import com.mago.db.DBConnection;
import com.mago.db.DBConnectionManager;

public class GetProjectCarouselAction extends Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7370432265817126294L;
	private Logger logger = Logger.getLogger(GetProjectCarouselAction.class);
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		ActionForward forward = new ActionForward();
		
		if(!SessionOper.IsContain(session, "newestPicVec")){
			DBConnection conn = DBConnectionManager.getInstance().getConnection();
			
			if(conn == null)
			{
				logger.error("Cann't create the connection to DataBase!");			
				session.setAttribute("errorMessage", ExceptionMessage.CON_ERROR);
				
				forward.setPath("/error.jsp");
				return forward;
				
			}else{
				logger.debug("Get the newest pic articles viewed articles from DataBase!");				
				Vector<PicList> newestPicVector = conn.queryNewestPic(10);
				logger.debug("Successed to get newest pic articles from DataBase!");
				
				session.setAttribute("newestPicVec", newestPicVector);
			}		
		}
		
		return null;
	}

}
