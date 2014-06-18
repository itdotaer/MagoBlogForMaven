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

public class GetAdAction extends Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5313727691046463923L;
	private Logger logger = Logger.getLogger(GetAdAction.class);
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		ActionForward forward = new ActionForward();
		
		if(!SessionOper.IsContain(session, "adVec")){
			DBConnection conn = DBConnectionManager.getInstance().getConnection();
			
			if(conn == null)
			{
				logger.error("Cann't create the connection to DataBase!");			
				session.setAttribute("errorMessage", ExceptionMessage.CON_ERROR);
				
				forward.setPath("/error.jsp");
				return forward;
				
			}else{
				logger.debug("Get the newest ad articles from DataBase!");				
				Vector<PicList> adVector = conn.queryAd(5);
				logger.debug("Successed to get newest ad articles from DataBase!");
				
				session.setAttribute("adVec", adVector);
			}		
		}
		
		return null;
	}
}
