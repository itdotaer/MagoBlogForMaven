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
import com.mago.bean.Classe;
import com.mago.db.DBConnection;
import com.mago.db.DBConnectionManager;

public class GetNavAction extends Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8335318656080758317L;
	private static Logger logger = Logger.getLogger(DBConnection.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		ActionForward forward = null;
		
		if(!SessionOper.IsContain(session, "navVec"))
		{
			DBConnection conn = DBConnectionManager.getInstance().getConnection();
			
			if(conn == null)
			{
				logger.error("Cann't create the connection to DataBase!");
				
				session.setAttribute("errorMessage", ExceptionMessage.CON_ERROR);
				
				forward = new ActionForward();
				forward.setPath("/error.jsp");
				
			}else{
				logger.debug("Get all the classes from DataBase!");				
				Vector<Classe> navVector = conn.queryClass();
				logger.debug("Successed to get all the classes from DataBase!");
				
				session.setAttribute("navVec", navVector);				
			}			
			
		}
		
		return forward;
	}

}
