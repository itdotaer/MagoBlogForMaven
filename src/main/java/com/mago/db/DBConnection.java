package com.mago.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.mago.bean.Article;
import com.mago.bean.ArticleList;
import com.mago.bean.Classe;
import com.mago.bean.MainService;
import com.mago.bean.PicList;

public class DBConnection implements Serializable{
	
	/**
	 * Connect to the DataBase and do the operators
	 */
	private static final long serialVersionUID = -6522749603801701515L;
	
	private boolean m_status = true;
	private Connection conn = null;
	private Statement stat = null;
	private static Logger logger = Logger.getLogger(DBConnection.class);
	private int connectionId = -1;

	public DBConnection(DBConnectionParameter parameters, int id) throws DBException{
		try{
			connectionId = id;
			conn = DriverManager.getConnection(parameters.getURL() + parameters.getDBName() + "?autoreconnect=true", 
					parameters.getUsername(), parameters.getPassword());
			stat = conn.createStatement();

		} catch (Exception e){
			logger.error("Error when create statement: " + e);
			throw new DBException("Error when create statement!");
		}
		if (stat == null){
			throw new DBException("Statement is null!");
		}
	}
	
	public int getConnectionId(){
		return connectionId;
	}
	
	public void occupied(){
		m_status = false;
	}
	
	public boolean getStatus(){
		return m_status;
	}
	
	public void returned(){
		m_status = true;
	}
	
	public void close() throws SQLException{
		conn.close();
	}
	
	public boolean executeStartupBatch(String sql) {
		boolean result = false;

		try{
			logger.debug("Start executing start-up batch.");
			result = stat.execute(sql);
			logger.debug("End executing start-up batch.");
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public Vector<Classe> queryClass(){
		Vector<Classe> classes = new Vector<Classe>();
		Classe bean = null;
		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.CLASS) + ";";
		
		try{
			ResultSet results = stat.executeQuery(queryState);
			logger.debug("Start get all classes from " +  DBName.getTableName(DBName.CLASS) + " table!");
			
			while(results.next()){			
				bean = new Classe(results.getInt("classId"), results.getString("className"),results.getInt("productId"), results.getInt("parentId"), 
						results.getInt("createdBy"),results.getDate("createDate"), results.getInt("lastUpdatedBy"), results.getDate("lastUpdateDate"));
				
				classes.add(bean);
			}
			
			logger.debug("End get all classes from " + DBName.getTableName(DBName.CLASS) + " table!");
			results.close();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return classes;
	}
	
	public Vector<MainService> queryMainService(int count){
		
		Vector<MainService> vec = new Vector<MainService>();
		
		MainService bean = null;
		String[] fields = {DBName.getTableName(DBName.ARTICLE_EXT) + ".articleId", DBName.getTableName(DBName.ARTICLE_BASE) 
				+ ".articleName", DBName.getTableName(DBName.ARTICLE_EXT) + ".articleDescription"};
		
		String queryState = SQLStatementQuery.getQuerySpecialField(fields) + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
				+ DBName.getTableName(DBName.ARTICLE_EXT);
		
		String condition = " where " + DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId=" + DBName.getTableName(DBName.ARTICLE_EXT) + ".articleId" 
				+ " and " +DBName.getTableName(DBName.ARTICLE_BASE) + ".isMainService = 1 and " + DBName.getTableName(DBName.ARTICLE_BASE) 
				+ ".isPublish=" + 1 +" limit " + count + ";";
		
		try{
			ResultSet results = stat.executeQuery(queryState + condition);
			logger.debug("Start get all main service from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + " tables!");
			
			while(results.next()){			
				bean = new MainService(results.getInt(DBName.getTableName(DBName.ARTICLE_EXT) + ".articleId"), 
						results.getString(DBName.getTableName(DBName.ARTICLE_BASE) + ".articleName"), 
						results.getString(DBName.getTableName(DBName.ARTICLE_EXT) + ".articleDescription"));
				
				vec.add(bean);
			}
			
			logger.debug("End get all classes from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + " tables!");
			results.close();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return vec;
	}
	
	public Article queryArticle(int articleId){
		
		Article article = null;
		
		String[] fields = {DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId", DBName.getTableName(DBName.ARTICLE_BASE) + ".articleName",  
				DBName.getTableName(DBName.ARTICLE_BASE) + ".publishDate",  DBName.getTableName(DBName.ARTICLE_BASE) + ".viewNum", 
				DBName.getTableName(DBName.ARTICLE_EXT) + ".articleDescription",  DBName.getTableName(DBName.ARTICLE_EXT) + ".articleContent",  
				DBName.getTableName(DBName.USER_EXT) + ".fullName",DBName.getTableName(DBName.ARTICLE_BASE) + ".isPublish",
				DBName.getTableName(DBName.ARTICLE_BASE) + ".isMainService", DBName.getTableName(DBName.ARTICLE_BASE) + ".isAds"};
		
		String queryState = SQLStatementQuery.getQuerySpecialField(fields) + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
				+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT);
		
		String condition = " where " + DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId=" + DBName.getTableName(DBName.ARTICLE_EXT) 
				+ ".articleId and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".publishedBy=" + DBName.getTableName(DBName.USER_EXT) + ".userId" 
				+ " and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId=" + articleId + " and " + DBName.getTableName(DBName.ARTICLE_BASE) 
				+ ".isPublish=" + 1 +";";
		
		
		try{
			ResultSet results = stat.executeQuery(queryState + condition);
			logger.debug("Start get the article from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT) + " tables!");
			
			while(results.next()){			
				article = new Article(results.getInt(fields[0]), results.getString(fields[1]), results.getDate(fields[2]), 
						results.getInt(fields[3]), results.getString(fields[4]), results.getString(fields[5]), 
						results.getString(fields[6]), results.getInt(fields[7]), results.getInt(fields[8]), 
						results.getInt(fields[9]));
			}
			
			logger.debug("End get the article from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT) + " tables!");
			results.close();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return article;
	}
	
	public Vector<ArticleList> queryArticleListByClassId(int classId){
		
		Vector<ArticleList> articleVector = new Vector<ArticleList>();
		ArticleList bean = null;
		
		String[] fields = {DBName.getTableName(DBName.ARTICLE_BASE) + ".classId", DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".articleName", DBName.getTableName(DBName.USER_EXT) + ".fullName", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".publishDate", DBName.getTableName(DBName.ARTICLE_BASE) + ".viewNum", 
				DBName.getTableName(DBName.ARTICLE_EXT) + ".articleDescription"};
		
		String queryState = SQLStatementQuery.getQuerySpecialField(fields) + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
				+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT);
		
		String condition = " where " + DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId=" + DBName.getTableName(DBName.ARTICLE_EXT) 
				+ ".articleId and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".publishedBy=" + DBName.getTableName(DBName.USER_EXT) + ".userId" 
				+ " and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".classId=" + classId + " and " + DBName.getTableName(DBName.ARTICLE_BASE) 
				+ ".isPublish=" + 1 +" ;";
		
		
		try{
			ResultSet results = stat.executeQuery(queryState + condition);
			logger.debug("Start get article list from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT) + " tables!");
			
			while(results.next()){			
				bean = new ArticleList(results.getInt(fields[0]), results.getInt(fields[1]), results.getString(fields[2]), 
						results.getString(fields[3]), results.getDate(fields[4]), results.getInt(fields[5]), 
						results.getString(fields[6]));
				
				articleVector.add(bean);
			}
			
			logger.debug("End get article list from " + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT) + " tables!");
			results.close();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return articleVector;
		
	}
	
	public Vector<ArticleList> queryArticleListByViewNum(int viewNum, int articleNum){
		Vector<ArticleList> articleVector = new Vector<ArticleList>();
		
		ArticleList bean = null;
		
		String[] fields = {DBName.getTableName(DBName.ARTICLE_BASE) + ".classId", DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".articleName", DBName.getTableName(DBName.USER_EXT) + ".fullName", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".publishDate", DBName.getTableName(DBName.ARTICLE_BASE) + ".viewNum", 
				DBName.getTableName(DBName.ARTICLE_EXT) + ".articleDescription"};
		
		String queryState = SQLStatementQuery.getQuerySpecialField(fields) + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
				+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT);
		
		String condition = " where " + DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId=" + DBName.getTableName(DBName.ARTICLE_EXT) 
				+ ".articleId and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".publishedBy=" + DBName.getTableName(DBName.USER_EXT) + ".userId" 
				+ " and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".viewNum>=" + viewNum + " and " +DBName.getTableName(DBName.ARTICLE_BASE) 
				+ ".isPublish=" + 1 + " order by viewNum desc" + " limit " + articleNum;
		
		
		try{
			ResultSet results = stat.executeQuery(queryState + condition);
			logger.debug("Start get article list from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + DBName.getTableName(DBName.USER_EXT) + " tables!");
			
			while(results.next()){			
				bean = new ArticleList(results.getInt(fields[0]), results.getInt(fields[1]), results.getString(fields[2]), 
						results.getString(fields[3]), results.getDate(fields[4]), results.getInt(fields[5]), 
						results.getString(fields[6]));
				
				articleVector.add(bean);
			}
			
			logger.debug("End get article list from " + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT) + " tables!");
			results.close();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return articleVector;
	}
	
	public Vector<PicList> queryAd(int adNum){
		Vector<PicList> picVector = new Vector<PicList>();
		PicList bean = null;
		
		String[] fields = {DBName.getTableName(DBName.ARTICLE_BASE) + ".classId", DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".articleName", DBName.getTableName(DBName.USER_EXT) + ".fullName", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".publishDate", DBName.getTableName(DBName.ARTICLE_BASE) + ".viewNum", 
				DBName.getTableName(DBName.ARTICLE_EXT) + ".articleDescription",DBName.getTableName(DBName.ARTICLE_BASE) + ".filePath"};
		
		String queryState = SQLStatementQuery.getQuerySpecialField(fields) + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
				+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT) + "," + DBName.getTableName(DBName.CLASS);
		
		String condition = " where " + DBName.getTableName(DBName.ARTICLE_BASE) + ".classId=" + DBName.getTableName(DBName.CLASS) + ".classId and " 
				+ DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId=" + DBName.getTableName(DBName.ARTICLE_EXT) 
				+ ".articleId and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".publishedBy=" + DBName.getTableName(DBName.USER_EXT) 
				+ ".userId and " + DBName.getTableName(DBName.CLASS) + ".productId=" + 1 + " and " + DBName.getTableName(DBName.ARTICLE_BASE) 
				+ ".isAds=" + 1 + " and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".isPublish=" + 1 + " order by " 
				+ DBName.getTableName(DBName.ARTICLE_BASE) + ".publishDate desc limit " + adNum + ";";
		
		
		try{
			ResultSet results = stat.executeQuery(queryState + condition);
			logger.debug("Start get ad list from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," + DBName.getTableName(DBName.ARTICLE_EXT) 
					+ DBName.getTableName(DBName.USER_EXT) + " tables!");
			
			while(results.next()){			
				bean = new PicList(results.getInt(fields[0]), results.getInt(fields[1]), results.getString(fields[2]), 
						results.getString(fields[3]), results.getDate(fields[4]), results.getInt(fields[5]), 
						results.getString(fields[6]),results.getString(fields[7]));
				
				picVector.add(bean);
			}
			
			logger.debug("End get ad list from " + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + DBName.getTableName(DBName.USER_EXT) + " tables!");
			results.close();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return picVector;
	}
	
	public Vector<PicList> queryNewestPic(int picNum){
		Vector<PicList> picVector = new Vector<PicList>();
		PicList bean = null;
		
		String[] fields = {DBName.getTableName(DBName.ARTICLE_BASE) + ".classId", DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".articleName", DBName.getTableName(DBName.USER_EXT) + ".fullName", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".publishDate", DBName.getTableName(DBName.ARTICLE_BASE) + ".viewNum", 
				DBName.getTableName(DBName.ARTICLE_EXT) + ".articleDescription",DBName.getTableName(DBName.ARTICLE_BASE) + ".filePath"};
		
		String queryState = SQLStatementQuery.getQuerySpecialField(fields) + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
				+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT) + "," + DBName.getTableName(DBName.CLASS);
		
		String condition = " where " + DBName.getTableName(DBName.ARTICLE_BASE) + ".classId=" + DBName.getTableName(DBName.CLASS) + ".classId and " 
				+ DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId=" + DBName.getTableName(DBName.ARTICLE_EXT) 
				+ ".articleId and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".publishedBy=" + DBName.getTableName(DBName.USER_EXT) 
				+ ".userId and " + DBName.getTableName(DBName.CLASS) + ".productId=" + 1 + " and " + DBName.getTableName(DBName.ARTICLE_BASE) 
				+ ".isPublish=" + 1 + " order by " + DBName.getTableName(DBName.ARTICLE_BASE) + ".publishDate desc limit " + picNum + ";";
		
		
		try{
			ResultSet results = stat.executeQuery(queryState + condition);
			logger.debug("Start get newest picture list from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," + DBName.getTableName(DBName.ARTICLE_EXT) 
					+ DBName.getTableName(DBName.USER_EXT) + " tables!");
			
			while(results.next()){			
				bean = new PicList(results.getInt(fields[0]), results.getInt(fields[1]), results.getString(fields[2]), 
						results.getString(fields[3]), results.getDate(fields[4]), results.getInt(fields[5]), 
						results.getString(fields[6]),results.getString(fields[7]));
				
				picVector.add(bean);
			}
			
			logger.debug("End get newest picture list from " + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + DBName.getTableName(DBName.USER_EXT) + " tables!");
			results.close();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return picVector;
	}
	
	public Vector<ArticleList> queryNewestArticle(int articleNum){
		Vector<ArticleList> articleVector = new Vector<ArticleList>();
		
		ArticleList bean = null;
		
		String[] fields = {DBName.getTableName(DBName.ARTICLE_BASE) + ".classId", DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".articleName", DBName.getTableName(DBName.USER_EXT) + ".fullName", 
				DBName.getTableName(DBName.ARTICLE_BASE) + ".publishDate", DBName.getTableName(DBName.ARTICLE_BASE) + ".viewNum", 
				DBName.getTableName(DBName.ARTICLE_EXT) + ".articleDescription"};
		
		String queryState = SQLStatementQuery.getQuerySpecialField(fields) + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
				+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT);
		
		String condition = " where " + DBName.getTableName(DBName.ARTICLE_BASE) + ".articleId=" + DBName.getTableName(DBName.ARTICLE_EXT) 
				+ ".articleId and " + DBName.getTableName(DBName.ARTICLE_BASE) + ".publishedBy=" + DBName.getTableName(DBName.USER_EXT) + ".userId" 
				+ " and "  +DBName.getTableName(DBName.ARTICLE_BASE) 
				+ ".isPublish=" + 1 +" order by publishDate desc" + " limit " + articleNum;
		
		
		try{
			ResultSet results = stat.executeQuery(queryState + condition);
			logger.debug("Start get newest article list from " +  DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + DBName.getTableName(DBName.USER_EXT) + " tables!");
			
			while(results.next()){			
				bean = new ArticleList(results.getInt(fields[0]), results.getInt(fields[1]), results.getString(fields[2]), 
						results.getString(fields[3]), results.getDate(fields[4]), results.getInt(fields[5]), 
						results.getString(fields[6]));
				
				articleVector.add(bean);
			}
			
			logger.debug("End get newest article list from " + DBName.getTableName(DBName.ARTICLE_BASE) + "," 
					+ DBName.getTableName(DBName.ARTICLE_EXT) + "," + DBName.getTableName(DBName.USER_EXT) + " tables!");
			results.close();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return articleVector;
	}
	
//	public DeptInfoBean queryDept(int id){
//		
//		DeptInfoBean bean = null;
//		String condition = " where id=" + id + ";";
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DEPT_INFO) + condition;
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start get dept info by uId:[" + id + "] from [base_dept_info] table!");
//			while (results.next()){
//				bean = new DeptInfoBean(results.getInt("id")
//						, results.getInt("type")
//						, results.getInt("parent")
//						, results.getString("name"));
//			}
//			logger.debug("End get dept info by uId:[" + id + "] from [base_dept_info] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//		
//	}
//	
//	public Vector<DeptInfoBean> querySubDept(int parentId){
//		
//		Vector<DeptInfoBean> vec = new Vector<DeptInfoBean>();
//		String condition = " where parent=" + parentId + ";";
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DEPT_INFO) + condition;
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start get dept list by parent Id:[" + parentId + "] from [base_dept_info] table!");
//			while (results.next()){
//				DeptInfoBean bean = new DeptInfoBean(results.getInt("id")
//						, results.getInt("type")
//						, results.getInt("parent")
//						, results.getString("name"));
//				vec.add(bean);
//			}
//			logger.debug("End get dept list by parent Id:[" + parentId + "] from [base_dept_info] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return vec;
//	}	
//	
//	public Vector<DeptInfoBean> queryDadui(){
//		
//		Vector<DeptInfoBean> vec = new Vector<DeptInfoBean>();
//		String condition = " where type=" + BaseConstants.DeptLevel.LEVEL_DADUI + ";";
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DEPT_INFO) + condition;
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start get all Dadui from [base_dept_info] table!");
//			while (results.next()){
//				DeptInfoBean bean = new DeptInfoBean(results.getInt("id")
//						, results.getInt("type")
//						, results.getInt("parent")
//						, results.getString("name"));
//				vec.add(bean);
//			}
//			logger.debug("End get all Dadui from [base_dept_info] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return vec;
//	}		
//	
//	public int queryZhiduiDeptId(){
//		
//		int bean = -1;
//		String condition = " where type=" + BaseConstants.DeptLevel.LEVEL_ZHIDUI + ";";
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DEPT_INFO) + condition;
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start get Zhidui info from [DEPT_INFO] table!");
//			while (results.next()){
//				bean =results.getInt("id");
//				break;
//			}
//			logger.debug("End get Zhidui info from [DEPT_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public DeptInfoBean queryDept(String name){
//		
//		DeptInfoBean bean = null;
//		String condition = " where name='" + name + "';";
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DEPT_INFO) + condition;
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start get dept info by name:[" + name + "] from [DEPT_INFO] table!");
//			while (results.next()){
//				bean = new DeptInfoBean(results.getInt("id")
//						, results.getInt("type")
//						, results.getInt("parent")
//						, results.getString("name"));
//			}
//			logger.debug("End get operator info by name:[" + name + "] from [DEPT_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//		
//	}
//	
//	public boolean addDept(int type, int parent, String name){
//		
//		boolean result = false;
//		
//		name = this.replayHTMLTags(name);
//		
//		StringBuffer condition = new StringBuffer(" (type,parent,name) values ");
//		condition.append("(").append(type).append(",")
//							 .append(parent).append(",")
//							 .append("'").append(name).append("'").append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.DEPT_INFO) + condition.toString();
//		try{
//			logger.debug("Start add dept with name:[" + name + "] into [base_dept_info] table!");
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [base_dept_info]");
//			logger.debug("End add dept with name:[" + name + "] into [base_dept_info] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//		
//	}
//	
//	public boolean modifyDept(int id, int type, int parent, String name){
//		
//		boolean result = false;
//		name = this.replayHTMLTags(name);
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("type=").append(type).append(",")
//							 .append("parent=").append(parent).append(",")
//							 .append("name=").append("'").append(name).append("'")
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.DEPT_INFO) + condition.toString();
//		try{
//			logger.debug("Start update department info with dept_id:[" + id + "] into [base_dept_info] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [base_dept_info]");
//			logger.debug("End update department info with dept_id:[" + id + "] into [base_dept_info] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//		
//	}
//	
//	public boolean removeDept(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.DEPT_INFO) + condition.toString();
//		try{
//			logger.debug("Start delete department with dept_id:[" + id + "] from [base_dept_info] table!");
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug(rows + " rows are deleted from table [base_dept_info]");
//			logger.debug("End delete department with dept_id:[" + id + "] from [base_dept_info] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//		
//	}	
//	
//	public Vector<DeptInfoBean> getDeptList(){
//		
//		Vector<DeptInfoBean> vec = new Vector<DeptInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DEPT_INFO) + " ORDER BY type DESC;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start list all dept info from [base_dept_info] table!");
//			while (results.next()){
//				DeptInfoBean bean = new DeptInfoBean(results.getInt("id")
//						, results.getInt("type")
//						, results.getInt("parent")
//						, results.getString("name"));
//				vec.add(bean);
//			}
//			logger.debug("End list all dept info from [base_dept_info] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return vec;
//	}
//	
//	public Vector<DeptInfoBean> getParentDeptList(){
//		
//		Vector<DeptInfoBean> vec = new Vector<DeptInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DEPT_INFO) 
//						+ " where type=" + BaseConstants.DeptLevel.LEVEL_ZHIDUI
//						+ " OR type=" + BaseConstants.DeptLevel.LEVEL_FENGONGSI
//						+ " ORDER BY type DESC;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start list all dept info from [base_dept_info] table!");
//			while (results.next()){
//				DeptInfoBean bean = new DeptInfoBean(results.getInt("id")
//						, results.getInt("type")
//						, results.getInt("parent")
//						, results.getString("name"));
//				vec.add(bean);
//			}
//			logger.debug("End list all dept info from [base_dept_info] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return vec;
//	}	
//	
//	public OperatorInfoBean queryOperator(int id){
//		
//		OperatorInfoBean bean = null;
//		String condition = " where id=" + id + ";";
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.OPERATOR_INFO) + condition;
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start get operator info by uId:[" + id + "] from [base_operator_info] table!");
//			while (results.next()){
//				bean = new OperatorInfoBean(results.getInt("id"),
//						results.getInt("type"),
//						results.getInt("dept"),
//						results.getString("name"),
//						EncryptionUtil.decrypt3DES(results.getString("password")),
//						results.getString("disp_name"),
//						results.getString("mobile"),
//						results.getString("email"),
//						results.getString("lastlogin"));
//			}
//			logger.debug("End get operator info by uId:[" + id + "] from [base_operator_info] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//		
//	}
//	
//	public boolean addOperator(String name, String pass, String fullname, String mobile, String email, int level, int parentId){
//		
//		boolean result = false;
//		
//		fullname = this.replayHTMLTags(fullname);
//		String encryptedPass = EncryptionUtil.encrypt3DES(pass);
//		
//		StringBuffer condition = new StringBuffer(" (name,password,dept,type,disp_name,mobile,email) values ");
//		condition.append("(").append("'").append(name).append("'").append(",")
//							 .append("'").append(encryptedPass).append("'").append(",")
//							 .append(parentId).append(",")
//							 .append(level).append(",")
//							 .append("'").append(fullname).append("'").append(",")
//							 .append("'").append(mobile).append("'").append(",")
//							 .append("'").append(email).append("'").append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.OPERATOR_INFO) + condition.toString();
//		try{
//			logger.debug("Start add operator with operator_name:[" + name + "] into [base_operator_info] table!");
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [base_operator_info]");
//			logger.debug("End add operator with operator_name:[" + name + "] into [base_operator_info] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//		
//	}
//	
//	public boolean removeOperator(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.OPERATOR_INFO) + condition.toString();
//		try{
//			logger.debug("Start delete operator with op_id:[" + id + "] from [base_operator_info] table!");
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug(rows + " rows are deleted from table [base_operator_info]");
//			logger.debug("End delete operator with op_id:[" + id + "] from [base_operator_info] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//		
//	}
//	
//	public boolean modifyOperator(int id, String name, String pass, String fullname, String mobile, String email, int level, int parentId){
//		
//		boolean result = false;
//		fullname = this.replayHTMLTags(fullname);
//		String encryptedPass = EncryptionUtil.encrypt3DES(pass);
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("type=").append(level).append(",")
//							 .append("dept=").append(parentId).append(",")
//							 .append("name=").append("'").append(name).append("'").append(",")
//							 .append("password=").append("'").append(encryptedPass).append("'").append(",")
//							 .append("disp_name=").append("'").append(fullname).append("'").append(",")
//							 .append("mobile=").append("'").append(mobile).append("'").append(",")
//							 .append("email=").append("'").append(email).append("'")
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.OPERATOR_INFO) + condition.toString();
//		try{
//			logger.debug("Start update operator info with op_id:[" + id + "] into [base_operator_info] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [base_operator_info]");
//			logger.debug("End update operator info with op_id:[" + id + "] into [base_operator_info] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//		
//	}	
//	
//	public int queryUserDeptId(int userId){
//		
//		int deptId = -1;
//		String condition = " where id=" + userId + ";";
//		String queryState = "select dept from " + DBName.getTableName(DBName.OPERATOR_INFO) + condition;
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start get dept id by uId:[" + userId + "] from [base_operator_info] table!");
//			while (results.next()){
//				deptId = results.getInt("dept");
//			}
//			logger.debug("End get dept id by uId:[" + userId + "] from [base_operator_info] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return deptId;
//		
//	}
//	
//	public void updateLoginTime(int id){
//		
//		String condition = " set lastlogin='" + getTimestampString() + "' where id=" + id + ";";
//		String queryState = SQLStatementQuery.update() + DBName.getTableName(DBName.OPERATOR_INFO) + condition;
//		try{
//			logger.debug("Start update logintime by id:[" + id + "] from [base_operator_info] table!");
//			stat.executeUpdate(queryState);
//			logger.debug("End update logintime by id:[" + id + "] from [base_operator_info] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		
//	}
//	
//	public OperatorInfoBean queryOperator(String name){
//		
//		OperatorInfoBean bean = null;
//		String condition = " where name='" + name + "';";
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.OPERATOR_INFO) + condition;
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start get operator info by name:[" + name + "] from [base_operator_info] table!");
//			while (results.next()){
//				bean = new OperatorInfoBean(results.getInt("id"),
//						results.getInt("type"),
//						results.getInt("dept"),
//						results.getString("name"),
//						EncryptionUtil.decrypt3DES(results.getString("password")),
//						results.getString("disp_name"),
//						results.getString("mobile"),
//						results.getString("email"),
//						results.getString("lastlogin"));
//			}
//			logger.debug("End get operator info by name:[" + name + "] from [base_operator_info] table!");
//			results.close();
//		} catch (Exception e){
//			e.printStackTrace();
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public boolean modifyMyInfo(int id, String pass, String fullname,
//			String mobile, String email) {
//
//		boolean result = false;
//		String encryptedPass = EncryptionUtil.encrypt3DES(pass);
//		fullname = this.replayHTMLTags(fullname);
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("password=").append("'").append(encryptedPass).append("'").append(",")
//				.append("disp_name=").append("'").append(fullname).append("'").append(",")
//				.append("mobile=").append("'").append(mobile).append("'").append(",")
//				.append("email=").append("'").append(email).append("'")
//				.append(" ").append("where")
//				.append(" id=").append(id).append(";");
//
//		String updateState = SQLStatementQuery.update()
//				+ DBName.getTableName(DBName.OPERATOR_INFO)
//				+ condition.toString();
//		try {
//			logger.debug("Start update operator info with operator_id:[" + id
//					+ "] into [base_operator_info] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows
//					+ " rows are inserted into table [base_operator_info]");
//			logger.debug("End update operator info with operator_id:[" + id
//					+ "] into [base_operator_info] table!");
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public Vector<OperatorInfoBean> getOperatorList(){
//		
//		Vector<OperatorInfoBean> vec = new Vector<OperatorInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.OPERATOR_INFO) 
//							+ " where type<" + BaseConstants.OperatorLevel.LEVEL_SYS_ADMIN + " ORDER BY dept;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start list all operators info from [base_operator_info] table!");
//			while (results.next()){
//				OperatorInfoBean bean = new OperatorInfoBean(results.getInt("id"),
//						results.getInt("type"),
//						results.getInt("dept"),
//						results.getString("name"),
//						EncryptionUtil.decrypt3DES(results.getString("password")),
//						results.getString("disp_name"),
//						results.getString("mobile"),
//						results.getString("email"),
//						results.getString("lastlogin"));
//				vec.add(bean);
//			}
//			logger.debug("End list all operators info from [base_operator_info] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return vec;
//	}	
//	
//	public boolean addDailyPatrolRecord( int userId, int deptId,int date, String weather, int mileage,
//			int barrierNum, int netNum, int dangerNum, int theftNum,
//			int landNum, int serviceNum, int securityNum, int slopNum,
//			int tankNum, int fireNum, int emergencyNum, int accidentNum,
//			int trafficNum, int chargeNum, BigDecimal chargeMoney, int loseNum,
//			BigDecimal loseMoney){
//		
//		boolean result = false;
//		
//		weather = this.replayHTMLTags(weather);
//		
//		StringBuffer condition = new StringBuffer(" (user_id,dept_id,date,weather" +
//				",mileage,barrier_num,net_num,danger_num,theft_num,land_num,service_num" +
//				",security_num,slop_num,tank_num,fire_num,emergency_num,accident_num" +
//				",traffic_num,charge_num,charge_money,lose_num,lose_money) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(deptId).append(",")
//							 .append(date).append(",")
//							 .append("'").append(weather).append("'").append(",")
//							 .append(mileage).append(",")
//							 .append(barrierNum).append(",")
//							 .append(netNum).append(",")
//							 .append(dangerNum).append(",")		
//							 .append(theftNum).append(",")
//							 .append(landNum).append(",")
//							 .append(serviceNum).append(",")
//							 .append(securityNum).append(",")		
//							 .append(slopNum).append(",")
//							 .append(tankNum).append(",")
//							 .append(fireNum).append(",")
//							 .append(emergencyNum).append(",")		
//							 .append(accidentNum).append(",")
//							 .append(trafficNum).append(",")
//							 .append(chargeNum).append(",")
//							 .append(chargeMoney).append(",")
//							 .append(loseNum).append(",")
//							 .append(loseMoney).append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.PARTOL_RECORD) + condition.toString();
//		try{
//			logger.debug("Start add record into [PARTOL_RECORD] table!");
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [PARTOL_RECORD]");
//			logger.debug("End add record into [PARTOL_RECORD] table!");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//		
//	}
//	
//	public boolean modifyDailyPatrolRecord(int id, String weather, int mileage,
//			int barrierNum, int netNum, int dangerNum, int theftNum,
//			int landNum, int serviceNum, int securityNum, int slopNum,
//			int tankNum, int fireNum, int emergencyNum, int accidentNum,
//			int trafficNum, int chargeNum, BigDecimal chargeMoney, int loseNum,
//			BigDecimal loseMoney){
//		
//		boolean result = false;
//		weather = this.replayHTMLTags(weather);
//
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("weather='").append(weather).append("'").append(",")
//							 .append("mileage=").append(mileage).append(",")
//							 .append("barrier_num=").append(barrierNum).append(",")
//							 .append("net_num=").append(netNum).append(",")
//							 .append("danger_num=").append(dangerNum).append(",")
//							 .append("theft_num=").append(theftNum).append(",")
//							 .append("land_num=").append(landNum).append(",")
//							 .append("service_num=").append(serviceNum).append(",")
//							 .append("security_num=").append(securityNum).append(",")
//							 .append("slop_num=").append(slopNum).append(",")
//							 .append("tank_num=").append(tankNum).append(",")
//							 .append("fire_num=").append(fireNum).append(",")
//							 .append("emergency_num=").append(emergencyNum).append(",")
//							 .append("accident_num=").append(accidentNum).append(",")
//							 .append("traffic_num=").append(trafficNum).append(",")
//							 .append("charge_num=").append(chargeNum).append(",")
//							 .append("charge_money=").append(chargeMoney).append(",")
//							 .append("lose_num=").append(loseNum).append(",")
//							 .append("lose_money=").append(loseMoney)						 
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.PARTOL_RECORD) + condition.toString();
//		try{
//			logger.debug("Start modify record into [PARTOL_RECORD] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [PARTOL_RECORD]");
//			logger.debug("End modify record into [PARTOL_RECORD] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public PatrolRecordBean getPatrolRecordByDay(int deptId, String date){
//		PatrolRecordBean patrol = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.PARTOL_RECORD) 
//							+ " where date=" + date +" and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query patrol record info from [PARTOL_RECORD] table!");
//			while (results.next()){
//				patrol = new PatrolRecordBean(results.getInt("id"), results.getInt("dept_id"), results.getInt("user_id")
//						,results.getInt("date"), results.getString("weather"), results.getInt("mileage")
//						,results.getInt("barrier_num"), results.getInt("net_num"), results.getInt("danger_num"), results.getInt("theft_num")
//						,results.getInt("land_num"), results.getInt("service_num"), results.getInt("security_num"), results.getInt("slop_num")
//						,results.getInt("tank_num"), results.getInt("fire_num"), results.getInt("emergency_num"), results.getInt("accident_num")
//						,results.getInt("traffic_num"), results.getInt("charge_num"), results.getBigDecimal("charge_money"), results.getInt("lose_num")
//						,results.getBigDecimal("lose_money"));
//				break;
//			}
//			logger.debug("End query patrol record info from [PARTOL_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return patrol;
//	}
//	
//	public int getPatrolMileageByMonth(int deptId, String date){
//		int sum= 0;
//		String queryState = "select mileage from " + DBName.getTableName(DBName.PARTOL_RECORD) 
//							+ " where date=" + date
//							+" and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query patrol record info from [PARTOL_RECORD] table!");
//			while (results.next()){
//				sum = results.getInt(1);
//			}
//			logger.debug("End query patrol record info from [PARTOL_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return sum;
//	}	
//
//	public int getPatrolAccidentNumByMonth(int deptId, String date){
//		int sum= 0;
//		String queryState = "select sum(accident_num+lose_num) from " + DBName.getTableName(DBName.PARTOL_RECORD) 
//							+ " where date=" + date
//							+" and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query patrol record info from [PARTOL_RECORD] table!");
//			while (results.next()){
//				sum = results.getInt(1);
//			}
//			logger.debug("End query patrol record info from [PARTOL_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return sum;
//	}		
//	
//	public PatrolRecordBean getPatrolRecordById(int id){
//		PatrolRecordBean patrol = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.PARTOL_RECORD) 
//							+ " where id=" + id + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query patrol record info from [PARTOL_RECORD] table!");
//			while (results.next()){
//				patrol = new PatrolRecordBean(results.getInt("id"), results.getInt("dept_id"), results.getInt("user_id")
//						,results.getInt("date"), results.getString("weather"), results.getInt("mileage")
//						,results.getInt("barrier_num"), results.getInt("net_num"), results.getInt("danger_num"), results.getInt("theft_num")
//						,results.getInt("land_num"), results.getInt("service_num"), results.getInt("security_num"), results.getInt("slop_num")
//						,results.getInt("tank_num"), results.getInt("fire_num"), results.getInt("emergency_num"), results.getInt("accident_num")
//						,results.getInt("traffic_num"), results.getInt("charge_num"), results.getBigDecimal("charge_money"), results.getInt("lose_num")
//						,results.getBigDecimal("lose_money"));
//				break;
//			}
//			logger.debug("End query patrol record info from [PARTOL_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return patrol;
//	}
//	
//	public boolean checkPatrolRecordByUserAndId(int userId,int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.PARTOL_RECORD) 
//							+ " where id=" + recordId
//							+ " and user_id=" + userId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query patrol record info from [PARTOL_RECORD] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query patrol record info from [PARTOL_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean checkPatrolRecordByUserAndId(int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.PARTOL_RECORD) 
//							+ " where id=" + recordId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query patrol record info from [PARTOL_RECORD] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query patrol record info from [PARTOL_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public Vector<PatrolRecordBean> getPatrolRecordListByDay(int deptId, int sDate, int eDate){
//		Vector<PatrolRecordBean> pVec = new Vector<PatrolRecordBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.PARTOL_RECORD) 
//							+ " where date<=" + eDate +" and date>=" + sDate +" and dept_id=" + deptId
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query patrol record list info from [PARTOL_RECORD] table!");
//			while (results.next()){
//				PatrolRecordBean patrol = new PatrolRecordBean(results.getInt("id"), results.getInt("dept_id"), results.getInt("user_id")
//						,results.getInt("date"), results.getString("weather"), results.getInt("mileage")
//						,results.getInt("barrier_num"), results.getInt("net_num"), results.getInt("danger_num"), results.getInt("theft_num")
//						,results.getInt("land_num"), results.getInt("service_num"), results.getInt("security_num"), results.getInt("slop_num")
//						,results.getInt("tank_num"), results.getInt("fire_num"), results.getInt("emergency_num"), results.getInt("accident_num")
//						,results.getInt("traffic_num"), results.getInt("charge_num"), results.getBigDecimal("charge_money"), results.getInt("lose_num")
//						,results.getBigDecimal("lose_money"));
//				pVec.add(patrol);
//			}
//			logger.debug("End query patrol record list info from [PARTOL_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}
//	
//	
//	public boolean deletePatrolRecord(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.PARTOL_RECORD) + condition.toString();
//		try{
//			logger.debug("Start delete daily patrol record with id:[" + id + "] from [PARTOL_RECORD] table!");
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug(rows + " rows are deleted from table [PARTOL_RECORD]");
//			logger.debug("End delete daily patrol record with id:[" + id + "] from [PARTOL_RECORD] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean addHrRecord( int userId, int deptId,int date, int total
//			,int mgr,int worker,int support,int security,int fired,int resigned
//			,int dismissed,int incoming,int outcoming,String remark){
//		
//		boolean result = false;
//		
//		remark = this.replayHTMLTags(remark);
//		
//		StringBuffer condition = new StringBuffer(" (user_id,dept_id,date,total" +
//				",mgr,worker,support,security,fired,resigned,dismissed" +
//				",incoming,outcoming,remark,status) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(deptId).append(",")
//							 .append(date).append(",")
//							 .append(total).append(",")
//							 .append(mgr).append(",")
//							 .append(worker).append(",")
//							 .append(support).append(",")		
//							 .append(security).append(",")
//							 .append(fired).append(",")
//							 .append(resigned).append(",")
//							 .append(dismissed).append(",")		
//							 .append(incoming).append(",")
//							 .append(outcoming).append(",'")
//							 .append(remark).append("',")
//							 .append(BaseConstants.ReportStatus.DRAFT).append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.HUMAN_RES_INFO) + condition.toString();
//		try{
//			logger.debug("Start add record into [HUMAN_RES_INFO] table!");
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [HUMAN_RES_INFO]");
//			logger.debug("End add record into [HUMAN_RES_INFO] table!");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//		
//	}
//	
//	public boolean modifyHrRecord(int id, int total
//			,int mgr,int worker,int support,int security,int fired,int resigned
//			,int dismissed,int incoming,int outcoming,String remark){
//		
//		boolean result = false;
//		
//		remark = this.replayHTMLTags(remark);
//
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("total=").append(total).append(",")
//							 .append("mgr=").append(mgr).append(",")
//							 .append("worker=").append(worker).append(",")
//							 .append("support=").append(support).append(",")
//							 .append("security=").append(security).append(",")
//							 .append("fired=").append(fired).append(",")
//							 .append("resigned=").append(resigned).append(",")
//							 .append("dismissed=").append(dismissed).append(",")
//							 .append("incoming=").append(incoming).append(",")
//							 .append("outcoming=").append(outcoming).append(",")
//							 .append("remark='").append(remark).append("'")
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.HUMAN_RES_INFO) + condition.toString();
//		try{
//			logger.debug("Start modify record into [HUMAN_RES_INFO] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [HUMAN_RES_INFO]");
//			logger.debug("End modify record into [HUMAN_RES_INFO] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean checkHrRecordByUserAndId(int userId,int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where id=" + recordId
//							+ " and user_id=" + userId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean checkHrRecordByUserAndId(int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where id=" + recordId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}		
//	
//	public boolean approveHrRecord(int id){
//		
//		boolean result = false;
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("status=").append(BaseConstants.ReportStatus.APPROVED)
//							 .append(" ").append("where id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.HUMAN_RES_INFO) + condition.toString();
//		try{
//			logger.debug("Start modify record into [HUMAN_RES_INFO] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [HUMAN_RES_INFO]");
//			logger.debug("End modify record into [HUMAN_RES_INFO] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean rejectHrRecord(int id){
//		
//		boolean result = false;
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("status=").append(BaseConstants.ReportStatus.DRAFT)
//							 .append(" ").append("where id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.HUMAN_RES_INFO) + condition.toString();
//		try{
//			logger.debug("Start modify record into [HUMAN_RES_INFO] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [HUMAN_RES_INFO]");
//			logger.debug("End modify record into [HUMAN_RES_INFO] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}		
//	
//	public boolean submitHrRecord(int deptId, String date){
//		
//		boolean result = false;
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("status=").append(BaseConstants.ReportStatus.SUBMITTED)
//							 .append(" ").append("where dept_id=").append(deptId)
//							 .append(" and ").append(" date=").append(date)
//							 .append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.HUMAN_RES_INFO) + condition.toString();
//		try{
//			logger.debug("Start modify record into [HUMAN_RES_INFO] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [HUMAN_RES_INFO]");
//			logger.debug("End modify record into [HUMAN_RES_INFO] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}		
//	
//	public HumanResourceInfoBean getHrRecordByDate(int deptId, String date){
//		HumanResourceInfoBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where date=" + date +" and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				bean = new HumanResourceInfoBean(results.getInt("id"), results.getInt("user_id"),results.getInt("dept_id")
//						, results.getInt("date"), results.getInt("total"),results.getInt("mgr"), results.getInt("worker"), results.getInt("support")
//						,results.getInt("security"), results.getInt("fired"), results.getInt("resigned"), results.getInt("dismissed")
//						,results.getInt("incoming"), results.getInt("outcoming"), results.getString("remark"), results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public HumanResourceInfoBean getApprovedHrRecordByDate(int deptId, String date){
//		HumanResourceInfoBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where date=" + date +" and dept_id=" + deptId 
//							+ " and status>=" + BaseConstants.ReportStatus.APPROVED + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				bean = new HumanResourceInfoBean(results.getInt("id"), results.getInt("user_id"),results.getInt("dept_id")
//						, results.getInt("date"), results.getInt("total"),results.getInt("mgr"), results.getInt("worker"), results.getInt("support")
//						,results.getInt("security"), results.getInt("fired"), results.getInt("resigned"), results.getInt("dismissed")
//						,results.getInt("incoming"), results.getInt("outcoming"), results.getString("remark"), results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}	
//
//	public HumanResourceInfoBean getHrRecordById(int id){
//		HumanResourceInfoBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where id=" + id + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				bean = new HumanResourceInfoBean(results.getInt("id"), results.getInt("user_id"),results.getInt("dept_id")
//						, results.getInt("date"), results.getInt("total"),results.getInt("mgr"), results.getInt("worker"), results.getInt("support")
//						,results.getInt("security"), results.getInt("fired"), results.getInt("resigned"), results.getInt("dismissed")
//						,results.getInt("incoming"), results.getInt("outcoming"), results.getString("remark"),results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}	
//	
//	public int getHrRecordStatusById(int id){
//		int bean = -1;
//		String queryState = "select status from " + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where id=" + id + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query status info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				bean = results.getInt("status");
//				break;
//			}
//			logger.debug("End query status info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}		
//	
//	public Vector<HumanResourceInfoBean> getHrRecordListByDate(int deptId, int sDate, int eDate){
//		Vector<HumanResourceInfoBean> hrVec = new Vector<HumanResourceInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where date<=" + eDate +" and date>=" + sDate +" and dept_id=" + deptId
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				HumanResourceInfoBean bean = new HumanResourceInfoBean(results.getInt("id"), results.getInt("user_id"),results.getInt("dept_id")
//						, results.getInt("date"), results.getInt("total"),results.getInt("mgr"), results.getInt("worker"), results.getInt("support")
//						,results.getInt("security"), results.getInt("fired"), results.getInt("resigned"), results.getInt("dismissed")
//						,results.getInt("incoming"), results.getInt("outcoming"), results.getString("remark"),results.getInt("status"));
//				hrVec.add(bean);
//			}
//			logger.debug("End query record list info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return hrVec;
//	}
//	
//	public Vector<HumanResourceInfoBean> getApprovedHrRecordListByDate(int deptId, int sDate, int eDate){
//		Vector<HumanResourceInfoBean> hrVec = new Vector<HumanResourceInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where date<=" + eDate +" and date>=" + sDate +" and dept_id=" + deptId
//							+ " and status=" + BaseConstants.ReportStatus.APPROVED
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				HumanResourceInfoBean bean = new HumanResourceInfoBean(results.getInt("id"), results.getInt("user_id"),results.getInt("dept_id")
//						, results.getInt("date"), results.getInt("total"),results.getInt("mgr"), results.getInt("worker"), results.getInt("support")
//						,results.getInt("security"), results.getInt("fired"), results.getInt("resigned"), results.getInt("dismissed")
//						,results.getInt("incoming"), results.getInt("outcoming"), results.getString("remark"),results.getInt("status"));
//				hrVec.add(bean);
//			}
//			logger.debug("End query record list info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return hrVec;
//	}	
//	
//	public Vector<HumanResourceInfoBean> getSubmittedHrRecordListByDate(int deptId, int sDate, int eDate){
//		Vector<HumanResourceInfoBean> hrVec = new Vector<HumanResourceInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.HUMAN_RES_INFO) 
//							+ " where date<=" + eDate +" and date>=" + sDate +" and dept_id=" + deptId
//							+ " and status=" + BaseConstants.ReportStatus.SUBMITTED
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [HUMAN_RES_INFO] table!");
//			while (results.next()){
//				HumanResourceInfoBean bean = new HumanResourceInfoBean(results.getInt("id"), results.getInt("user_id"),results.getInt("dept_id")
//						, results.getInt("date"), results.getInt("total"),results.getInt("mgr"), results.getInt("worker"), results.getInt("support")
//						,results.getInt("security"), results.getInt("fired"), results.getInt("resigned"), results.getInt("dismissed")
//						,results.getInt("incoming"), results.getInt("outcoming"), results.getString("remark"),results.getInt("status"));
//				hrVec.add(bean);
//			}
//			logger.debug("End query record list info from [HUMAN_RES_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return hrVec;
//	}		
//	
//	public boolean deleteHrRecord(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.HUMAN_RES_INFO) + condition.toString();
//		try{
//			logger.debug("Start delete hr record with id:[" + id + "] from [HUMAN_RES_INFO] table!");
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug(rows + " rows are deleted from table [HUMAN_RES_INFO]");
//			logger.debug("End delete hr record with id:[" + id + "] from [HUMAN_RES_INFO] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean addNoticeInfo (int userId,int approver, int deptId,long create_ts, long approve_ts,String title, String content, int status){
//		
//		boolean result = false;
//		content = this.replayHTMLTags(content);
//		
//		StringBuffer condition = new StringBuffer(" (auther,approver,dept_id,create_ts" +
//				",approve_ts,title, content,status) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(approver).append(",")
//							 .append(deptId).append(",")
//							 .append(create_ts).append(",")
//							 .append(approve_ts).append(",'")
//							 .append(title).append("','")
//							 .append(content).append("',")
//							 .append(status).append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.NOTICE_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug("End add record into [NOTICE_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean addNoticeInfo (int userId,int approver, int deptId,long create_ts, String title, String content, int status){
//		
//		boolean result = false;
//		content = this.replayHTMLTags(content);
//		
//		StringBuffer condition = new StringBuffer(" (auther,approver,dept_id,create_ts" +
//				", title,content,status) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(approver).append(",")
//							 .append(deptId).append(",")
//							 .append(create_ts).append(",'")
//							 .append(title).append("','")
//							 .append(content).append("',")
//							 .append(status).append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.NOTICE_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug("End add record into [NOTICE_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean modifyNoticeInfoByMgr(int id, String title, String content){
//		
//		boolean result = false;
//		content = this.replayHTMLTags(content);
//
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("content='").append(content).append("',")
//							.append("title='").append(title).append("',")
//							.append("approve_ts=").append(System.currentTimeMillis()).append(",")
//							.append("status=").append(BaseConstants.ReportStatus.APPROVED)
//							.append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.NOTICE_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End modify record into [NOTICE_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean modifyNoticeInfo(int id, String title, String content){
//		
//		boolean result = false;
//		content = this.replayHTMLTags(content);
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("content='").append(content).append("',")
//							.append("title='").append(title).append("'")
//							.append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.NOTICE_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End modify record into [NOTICE_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean approveNoticeInfo(int id, int approver){
//		
//		boolean result = false;
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("status=").append(BaseConstants.ReportStatus.APPROVED).append(",")
//								.append("approver=").append(approver).append(",")
//								 .append("approve_ts=").append(System.currentTimeMillis())
//								 .append(" ").append("where id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.NOTICE_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End modify record into [NOTICE_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean rejectNoticeInfo(int id, int approver){
//		
//		boolean result = false;
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("status=").append(BaseConstants.ReportStatus.DRAFT).append(",")
//								.append("approver=").append(approver).append(",")
//								 .append("approve_ts=").append(System.currentTimeMillis())
//								 .append(" ").append("where id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.NOTICE_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End modify record into [NOTICE_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}		
//
//	public NoticeInfoBean getNoticeInfoById(int id){
//		NoticeInfoBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.NOTICE_INFO) 
//							+ " where id=" + id + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [NOTICE_INFO] table!");
//			while (results.next()){
//				bean = new NoticeInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getString("title"),results.getString("content"),results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [NOTICE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public boolean checkNoticeByDeptAndId(int deptId, int userId, int id){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.NOTICE_INFO) 
//							+ " where id=" + id 
//							+ " and auther=" + userId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [NOTICE_INFO] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [NOTICE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean checkNoticeByDeptAndId(int deptId, int id){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.NOTICE_INFO) 
//							+ " where id=" + id 
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [NOTICE_INFO] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [NOTICE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}		
//	
//	public Vector<NoticeInfoBean> getNoticeInfoListByDate(int deptId, long sDate, long eDate){
//		Vector<NoticeInfoBean> hrVec = new Vector<NoticeInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.NOTICE_INFO) 
//							+ " where create_ts<=" + eDate +" and create_ts>=" + sDate +" and dept_id=" + deptId
//							+ " order by create_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [NOTICE_INFO] table!");
//			while (results.next()){
//				NoticeInfoBean bean = new NoticeInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getString("title"),results.getString("content"),results.getInt("status"));
//				hrVec.add(bean);
//			}
//			logger.debug("End query record list info from [NOTICE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return hrVec;
//	}
//	
//	public Vector<NoticeInfoBean> getApprovedNoticeInfoListByDept(int deptId, long sDate, long eDate){
//		Vector<NoticeInfoBean> hrVec = new Vector<NoticeInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.NOTICE_INFO) 
//							+ " where approve_ts<=" + eDate +" and approve_ts>=" + sDate +" and dept_id=" + deptId
//							+ " and status=" + BaseConstants.ReportStatus.APPROVED
//							+ " order by approve_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [NOTICE_INFO] table!");
//			while (results.next()){
//				NoticeInfoBean bean = new NoticeInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getString("title"),results.getString("content"),results.getInt("status"));
//				hrVec.add(bean);
//			}
//			logger.debug("End query record list info from [NOTICE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return hrVec;
//	}	
//	
//	public Vector<NoticeInfoBean> getApprovedNoticeInfoListByDept(int deptId, int startNumber, int limit){
//		Vector<NoticeInfoBean> hrVec = new Vector<NoticeInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.NOTICE_INFO) 
//							+ " where dept_id=" + deptId
//							+ " and status=" + BaseConstants.ReportStatus.APPROVED
//							+ " order by approve_ts desc";
//		
//		if (startNumber != -1) {
//			queryState += " limit " + startNumber + ",";
//		} else {
//			queryState += " limit 0,";
//		}
//		if (limit != -1) {
//			queryState += limit;
//		} else {
//			queryState += "30";
//		}
//		queryState += ";";
//		
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [NOTICE_INFO] table!");
//			while (results.next()){
//				NoticeInfoBean bean = new NoticeInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getString("title"),results.getString("content"),results.getInt("status"));
//				hrVec.add(bean);
//			}
//			logger.debug("End query record list info from [NOTICE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return hrVec;
//	}	
//	
//	public boolean deleteNoticeInfo(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.NOTICE_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug("End delete record with id:[" + id + "] from [NOTICE_INFO] table!"  + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean addBriefInfo (int userId,int approver, int deptId,long create_ts, long approve_ts, int type, String content, String reply, int status){
//		
//		boolean result = false;
//		content = this.replayHTMLTags(content);
//
//		StringBuffer condition = new StringBuffer(" (auther,approver,dept_id,create_ts" +
//				",approve_ts,type,content,reply,status) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(approver).append(",")
//							 .append(deptId).append(",")
//							 .append(create_ts).append(",")
//							 .append(approve_ts).append(",")
//							 .append(type).append(",'")
//							 .append(content).append("','")
//							 .append(reply).append("',")
//							 .append(status).append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.BRIEF_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug("End add record into [BRIEF_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean AddReplyToBriefInfo(int id, String reply){
//		
//		boolean result = false;
//		
//		reply = this.replayHTMLTags(reply);
//
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("reply='").append(reply).append("'")
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.BRIEF_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End add reply info into [BRIEF_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean modifyBriefInfo(int id, String content){
//		
//		boolean result = false;
//		
//		content = this.replayHTMLTags(content);
//
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("content='").append(content).append("',")
//							.append("status=").append(BaseConstants.BriefStatus.DRAFT)
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.BRIEF_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End modify record into [BRIEF_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean updateBriefStatus(int id, int approver, int status){
//		
//		boolean result = false;
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("status=").append(status).append(",")
//								.append("approver=").append(approver).append(",")
//								 .append("approve_ts=").append(System.currentTimeMillis())
//								 .append(" ").append("where id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.BRIEF_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End modify record into [BRIEF_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//
//	public BriefInfoBean getBriefInfoById(int id){
//		BriefInfoBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.BRIEF_INFO) 
//							+ " where id=" + id + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [BRIEF_INFO] table!");
//			while (results.next()){
//				bean = new BriefInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getInt("type"),results.getString("content"),results.getString("reply"),results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [BRIEF_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public boolean checkBriefByDeptAndId(int deptId, int userId, int id){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.BRIEF_INFO) 
//							+ " where id=" + id 
//							+ " and auther=" + userId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [BRIEF_INFO] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [BRIEF_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean checkBriefByDeptAndId(int deptId, int id){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.BRIEF_INFO) 
//							+ " where id=" + id 
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [BRIEF_INFO] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [BRIEF_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}		
//	
//	public Vector<BriefInfoBean> getBriefInfoListByDate(int deptId, long sDate, long eDate){
//		Vector<BriefInfoBean> bVec = new Vector<BriefInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.BRIEF_INFO) 
//							+ " where create_ts<=" + eDate +" and create_ts>=" + sDate +" and dept_id=" + deptId
//							+ " order by create_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [BRIEF_INFO] table!");
//			while (results.next()){
//				BriefInfoBean bean = new BriefInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getInt("type"),results.getString("content"),results.getString("reply"),results.getInt("status"));
//				bVec.add(bean);
//			}
//			logger.debug("End query record list info from [BRIEF_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bVec;
//	}
//	
//	public Vector<BriefInfoBean> getAllBriefInfoListByDate(long sDate, long eDate){
//		Vector<BriefInfoBean> bVec = new Vector<BriefInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.BRIEF_INFO) 
//							+ " where create_ts<=" + eDate +" and create_ts>=" + sDate
//							+ " order by create_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [BRIEF_INFO] table!");
//			while (results.next()){
//				BriefInfoBean bean = new BriefInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getInt("type"),results.getString("content"),results.getString("reply"),results.getInt("status"));
//				bVec.add(bean);
//			}
//			logger.debug("End query record list info from [BRIEF_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bVec;
//	}
//	
//	public Vector<BriefInfoBean> getAllBriefInfoListByDateAndStatus(long sDate, long eDate, int status){
//		Vector<BriefInfoBean> bVec = new Vector<BriefInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.BRIEF_INFO) 
//							+ " where create_ts<=" + eDate +" and create_ts>=" + sDate
//							+ " and status=" + status
//							+ " order by create_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [BRIEF_INFO] table!");
//			while (results.next()){
//				BriefInfoBean bean = new BriefInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getInt("type"),results.getString("content"),results.getString("reply"),results.getInt("status"));
//				bVec.add(bean);
//			}
//			logger.debug("End query record list info from [BRIEF_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bVec;
//	}	
//	
//	public Vector<BriefInfoBean> getBriefInfoListByDateAndStatus(int deptId, long sDate, long eDate, int status){
//		Vector<BriefInfoBean> bVec = new Vector<BriefInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.BRIEF_INFO) 
//							+ " where create_ts<=" + eDate +" and create_ts>=" + sDate +" and dept_id=" + deptId
//							+ " and status=" + status
//							+ " order by create_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [BRIEF_INFO] table!");
//			while (results.next()){
//				BriefInfoBean bean = new BriefInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getInt("type"),results.getString("content"),results.getString("reply"),results.getInt("status"));
//				bVec.add(bean);
//			}
//			logger.debug("End query record list info from [BRIEF_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bVec;
//	}	
//	
//	public Vector<BriefInfoBean> getBriefInfoListByDateTypeAndStatus(long sDate, long eDate, int type,int status){
//		Vector<BriefInfoBean> bVec = new Vector<BriefInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.BRIEF_INFO)
//							+ " where create_ts<=" + eDate +" and create_ts>=" + sDate
//							+ " AND status=" + status
//							+ " AND type=" + type
//							+ " order by create_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record list info from [BRIEF_INFO] table!");
//			while (results.next()){
//				BriefInfoBean bean = new BriefInfoBean(results.getInt("id"), results.getInt("auther"),results.getInt("approver")
//						,results.getInt("dept_id"), results.getLong("create_ts"),results.getLong("approve_ts")
//						,results.getInt("type"),results.getString("content"),results.getString("reply"),results.getInt("status"));
//				bVec.add(bean);
//			}
//			logger.debug("End query record list info from [BRIEF_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bVec;
//	}		
//	
//	public boolean deleteBriefInfo(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.BRIEF_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug("End delete record with id:[" + id + "] from [NOTICBRIEF_INFOE_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean addRoadClearRecord( int userId, int deptId,int date, String weather, String informer,
//			String plate, String driver, String phone, String car_type,
//			int start_mileage, int end_mileage, String cause, int mileage,
//			BigDecimal fee, String rescue_driver, String remark){
//		
//		boolean result = false;
//		
//		weather = this.replayHTMLTags(weather);
//		informer = this.replayHTMLTags(informer);
//		plate = this.replayHTMLTags(plate);
//		driver = this.replayHTMLTags(driver);
//		phone = this.replayHTMLTags(phone);
//		car_type = this.replayHTMLTags(car_type);
//		remark = this.replayHTMLTags(remark);
//		rescue_driver = this.replayHTMLTags(rescue_driver);
//		cause = this.replayHTMLTags(cause);
//		
//		StringBuffer condition = new StringBuffer(" (user_id,dept_id,date,weather" +
//				",informer,plate,driver,phone,car_type,start_mileage,end_mileage" +
//				",cause,mileage,fee,rescue_driver,remark) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(deptId).append(",")
//							 .append(date).append(",")
//							 .append("'").append(weather).append("'").append(",")
//							 .append("'").append(informer).append("'").append(",")
//							 .append("'").append(plate).append("'").append(",")
//							 .append("'").append(driver).append("'").append(",")
//							 .append("'").append(phone).append("'").append(",")	
//							 .append("'").append(car_type).append("'").append(",")	
//							 .append(start_mileage).append(",")
//							 .append(end_mileage).append(",")
//							 .append("'").append(cause).append("'").append(",")	
//							 .append(mileage).append(",")
//							 .append(fee).append(",")
//							 .append("'").append(rescue_driver).append("'").append(",")	
//							 .append("'").append(remark).append("'")
//							 .append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.ROAD_CLEAR_RECORD) + condition.toString();
//		try{
//			logger.debug("Start add record into [ROAD_CLEAR_RECORD] table!");
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [ROAD_CLEAR_RECORD]");
//			logger.debug("End add record into [ROAD_CLEAR_RECORD] table!");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean modifyRoadClearRecord(int id, String weather, String informer,
//			String plate, String driver, String phone, String car_type,
//			int start_mileage, int end_mileage, String cause, int mileage,
//			BigDecimal fee, String rescue_driver, String remark){
//		
//		boolean result = false;
//		
//		weather = this.replayHTMLTags(weather);
//		informer = this.replayHTMLTags(informer);
//		plate = this.replayHTMLTags(plate);
//		driver = this.replayHTMLTags(driver);
//		phone = this.replayHTMLTags(phone);
//		car_type = this.replayHTMLTags(car_type);
//		remark = this.replayHTMLTags(remark);
//		rescue_driver = this.replayHTMLTags(rescue_driver);
//		cause = this.replayHTMLTags(cause);
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("weather='").append(weather).append("'").append(",")
//							 .append("informer='").append(informer).append("'").append(",")
//							 .append("plate='").append(plate).append("'").append(",")
//							 .append("driver='").append(driver).append("'").append(",")
//							 .append("phone='").append(phone).append("'").append(",")	
//							 .append("car_type='").append(car_type).append("'").append(",")	
//							 .append("start_mileage=").append(start_mileage).append(",")
//							 .append("end_mileage=").append(end_mileage).append(",")
//							 .append("cause='").append(cause).append("'").append(",")	
//							 .append("mileage=").append(mileage).append(",")
//							 .append("fee=").append(fee).append(",")
//							 .append("rescue_driver='").append(rescue_driver).append("'").append(",")	
//							 .append("remark='").append(remark).append("'")
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.ROAD_CLEAR_RECORD) + condition.toString();
//		try{
//			logger.debug("Start modify record into [ROAD_CLEAR_RECORD] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [ROAD_CLEAR_RECORD]");
//			logger.debug("End modify record into [ROAD_CLEAR_RECORD] table!");
//		} catch (Exception e){
//			logger.debug(updateState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public RoadClearRecordBean getRoadClearRecordByDate(int deptId, String date){
//		RoadClearRecordBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.ROAD_CLEAR_RECORD) 
//							+ " where date=" + date +" and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [ROAD_CLEAR_RECORD] table!");
//			while (results.next()){
//				bean = new RoadClearRecordBean(results.getInt("id"), results.getInt("dept_id"), results.getInt("user_id")
//						,results.getInt("date"), results.getString("weather"), results.getString("informer")
//						,results.getString("plate"), results.getString("driver"), results.getString("phone"), results.getString("car_type")
//						,results.getInt("start_mileage"), results.getInt("end_mileage"), results.getString("cause"), results.getInt("mileage")
//						,results.getBigDecimal("fee"), results.getString("rescue_driver"), results.getString("remark"));
//				break;
//			}
//			logger.debug("End query record info from [ROAD_CLEAR_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}	
//
//	public boolean checkRoadClearRecordByUserAndId(int userId,int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.ROAD_CLEAR_RECORD) 
//							+ " where id=" + recordId
//							+ " and user_id=" + userId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [ROAD_CLEAR_RECORD] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [ROAD_CLEAR_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean checkRoadClearRecordByUserAndId(int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.ROAD_CLEAR_RECORD) 
//							+ " where id=" + recordId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [ROAD_CLEAR_RECORD] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [ROAD_CLEAR_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public RoadClearRecordBean getRoadClearRecordById(int id){
//		RoadClearRecordBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.ROAD_CLEAR_RECORD) 
//							+ " where id=" + id + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [ROAD_CLEAR_RECORD] table!");
//			while (results.next()){
//				bean = new RoadClearRecordBean(results.getInt("id"), results.getInt("dept_id"), results.getInt("user_id")
//						,results.getInt("date"), results.getString("weather"), results.getString("informer")
//						,results.getString("plate"), results.getString("driver"), results.getString("phone"), results.getString("car_type")
//						,results.getInt("start_mileage"), results.getInt("end_mileage"), results.getString("cause"), results.getInt("mileage")
//						,results.getBigDecimal("fee"), results.getString("rescue_driver"), results.getString("remark"));
//				break;
//			}
//			logger.debug("End query record info from [ROAD_CLEAR_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public Vector<RoadClearRecordBean> getRoadClearRecordListByDay(int deptId, int sDate, int eDate){
//		Vector<RoadClearRecordBean> pVec = new Vector<RoadClearRecordBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.ROAD_CLEAR_RECORD) 
//							+ " where date<=" + eDate +" and date>=" + sDate +" and dept_id=" + deptId
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query road clear record list info from [ROAD_CLEAR_RECORD] table!");
//			while (results.next()){
//				RoadClearRecordBean bean = new RoadClearRecordBean(results.getInt("id"), results.getInt("dept_id"), results.getInt("user_id")
//						,results.getInt("date"), results.getString("weather"), results.getString("informer")
//						,results.getString("plate"), results.getString("driver"), results.getString("phone"), results.getString("car_type")
//						,results.getInt("start_mileage"), results.getInt("end_mileage"), results.getString("cause"), results.getInt("mileage")
//						,results.getBigDecimal("fee"), results.getString("rescue_driver"), results.getString("remark"));
//				pVec.add(bean);
//			}
//			logger.debug("End query road clear record list info from [ROAD_CLEAR_RECORD] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}
//	
//	
//	public boolean deleteRoadClearRecord(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.ROAD_CLEAR_RECORD) + condition.toString();
//		try{
//			logger.debug("Start delete daily road clear record with id:[" + id + "] from [ROAD_CLEAR_RECORD] table!");
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug(rows + " rows are deleted from table [ROAD_CLEAR_RECORD]");
//			logger.debug("End delete daily road clear record with id:[" + id + "] from [ROAD_CLEAR_RECORD] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean addMonthlyRoadClearRecord(int userId, int deptId, int date,
//			String plate, String maintain, String driver, int mileage,
//			int run_num, int clear_mileage, int gas, int meter, int clear_num,
//			BigDecimal fee, String remark){
//		
//		boolean result = false;
//		
//		maintain = this.replayHTMLTags(maintain);
//		plate = this.replayHTMLTags(plate);
//		driver = this.replayHTMLTags(driver);
//		remark = this.replayHTMLTags(remark);
//		
//		StringBuffer condition = new StringBuffer(" (user_id,dept_id,date" +
//				",plate,run_num,clear_num,fee,mileage,clear_mileage" +
//				",gas,maintain,meter,driver,remark) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(deptId).append(",")
//							 .append(date).append(",")
//							 .append("'").append(plate).append("'").append(",")
//							 .append(run_num).append(",")
//							 .append(clear_num).append(",")
// 							 .append(fee).append(",")
// 							 .append(mileage).append(",")
// 							 .append(clear_mileage).append(",")
// 							 .append(gas).append(",")
//							 .append("'").append(maintain).append("'").append(",")
//							 .append(meter).append(",")
//							 .append("'").append(driver).append("'").append(",")	
//							 .append("'").append(remark).append("'")
//							 .append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) + condition.toString();
//		try{
//			logger.debug("Start add record into [MONTHLY_ROADCLEAR] table!");
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug(rows + " rows are inserted into table [MONTHLY_ROADCLEAR]");
//			logger.debug("End add record into [MONTHLY_ROADCLEAR] table!");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean modifyMonthlyRoadClearRecord(int id, String maintain,
//			 String driver, int mileage,int run_num, int clear_mileage, int gas, 
//			 int meter, int clear_num, BigDecimal fee, String remark){
//		
//		boolean result = false;
//		
//		maintain = this.replayHTMLTags(maintain);
//		driver = this.replayHTMLTags(driver);
//		remark = this.replayHTMLTags(remark);
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("maintain='").append(maintain).append("'").append(",")
//							 .append("driver='").append(driver).append("'").append(",")
//							 .append("mileage=").append(mileage).append(",")
//							 .append("fee=").append(fee).append(",")
//							 .append("run_num=").append(run_num).append(",")
//							 .append("clear_mileage=").append(clear_mileage).append(",")
//							 .append("gas=").append(gas).append(",")
//							 .append("meter=").append(meter).append(",")
//							 .append("clear_num=").append(clear_num).append(",")
//							 .append("remark='").append(remark).append("'")
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) + condition.toString();
//		try{
//			logger.debug("Start modify record into [MONTHLY_ROADCLEAR] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are updated into table [MONTHLY_ROADCLEAR]");
//			logger.debug("End modify record into [MONTHLY_ROADCLEAR] table!");
//		} catch (Exception e){
//			logger.debug(updateState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean modifyMonthlyRoadClearRecordStatus(int month, int deptId, int status){
//		
//		boolean result = false;
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("status=").append(status)
//							 .append(" ").append("where").append(" date=").append(month)
//							 .append(" and dept_id=").append(deptId).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) + condition.toString();
//		try{
//			logger.debug("Start modify record into [MONTHLY_ROADCLEAR] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug(rows + " rows are updated into table [MONTHLY_ROADCLEAR]");
//			logger.debug("End modify record into [MONTHLY_ROADCLEAR] table!");
//		} catch (Exception e){
//			logger.debug(updateState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}		
//	
//	public MonthlyRoadClearRecordBean getMonthlyRoadClearRecordByDateAndPlate(int deptId, String date, String plate){
//		MonthlyRoadClearRecordBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where date=" + date 
//							+" and plate='" + plate + "'"
//							+" and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				bean = new MonthlyRoadClearRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("plate"), results.getString("maintain")
//						,results.getString("driver"), results.getInt("mileage"), results.getInt("run_num"), results.getInt("clear_mileage")
//						,results.getInt("gas"), results.getInt("meter"), results.getInt("clear_num")
//						,results.getBigDecimal("fee"), results.getString("remark"), results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//
//	public boolean checkMonthlyRoadClearRecordByUserAndId(int userId,int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where id=" + recordId
//							+ " and user_id=" + userId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean checkMonthlyRoadClearRecordByUserAndId(int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where id=" + recordId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public MonthlyRoadClearRecordBean getMonthlyRoadClearRecordById(int id){
//		MonthlyRoadClearRecordBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where id=" + id + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				bean = new MonthlyRoadClearRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("plate"), results.getString("maintain")
//						,results.getString("driver"), results.getInt("mileage"), results.getInt("run_num"), results.getInt("clear_mileage")
//						,results.getInt("gas"), results.getInt("meter"), results.getInt("clear_num")
//						,results.getBigDecimal("fee"), results.getString("remark"), results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public Vector<MonthlyRoadClearRecordBean> getMonthlyRoadClearRecordListByDay(int deptId, int sDate, int eDate){
//		Vector<MonthlyRoadClearRecordBean> pVec = new Vector<MonthlyRoadClearRecordBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where date<=" + eDate +" and date>=" + sDate +" and dept_id=" + deptId
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query road clear record list info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				MonthlyRoadClearRecordBean bean = new MonthlyRoadClearRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("plate"), results.getString("maintain")
//						,results.getString("driver"), results.getInt("mileage"), results.getInt("run_num"), results.getInt("clear_mileage")
//						,results.getInt("gas"), results.getInt("meter"), results.getInt("clear_num")
//						,results.getBigDecimal("fee"), results.getString("remark"), results.getInt("status"));
//				pVec.add(bean);
//			}
//			logger.debug("End query road clear record list info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}
//	
//	public Vector<MonthlyRoadClearRecordBean> getMonthlyRoadClearRecordListByDay(int deptId, int month){
//		Vector<MonthlyRoadClearRecordBean> pVec = new Vector<MonthlyRoadClearRecordBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where date=" + month +" and dept_id=" + deptId
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query road clear record list info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				MonthlyRoadClearRecordBean bean = new MonthlyRoadClearRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("plate"), results.getString("maintain")
//						,results.getString("driver"), results.getInt("mileage"), results.getInt("run_num"), results.getInt("clear_mileage")
//						,results.getInt("gas"), results.getInt("meter"), results.getInt("clear_num")
//						,results.getBigDecimal("fee"), results.getString("remark"), results.getInt("status"));
//				pVec.add(bean);
//			}
//			logger.debug("End query road clear record list info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}
//	
//	public Vector<String> getAnnualRoadClearRecordPlateList(int deptId, int smonth, int emonth, int status){
//		Vector<String> plateVec = new Vector<String>();
//		String queryState = "select distinct plate from " + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where date>" + smonth
//							+ " and date<" + emonth
//							+ " and dept_id=" + deptId
//							+ " and status=" + status + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query plate list info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				plateVec.add(results.getString("plate"));
//			}
//			logger.debug("End query plate list info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return plateVec;
//	}
//		
//	public Vector<MonthlyRoadClearRecordBean> getMonthlyRoadClearRecordListByDateAndPlate(int deptId, int sdate, int edate, String plate){
//		Vector<MonthlyRoadClearRecordBean> vec = new Vector<MonthlyRoadClearRecordBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where date>" + sdate 
//							+ " and date<" + edate
//							+ " and plate='" + plate + "'"
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				MonthlyRoadClearRecordBean bean = new MonthlyRoadClearRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("plate"), results.getString("maintain")
//						,results.getString("driver"), results.getInt("mileage"), results.getInt("run_num"), results.getInt("clear_mileage")
//						,results.getInt("gas"), results.getInt("meter"), results.getInt("clear_num")
//						,results.getBigDecimal("fee"), results.getString("remark"), results.getInt("status"));
//				vec.add(bean);
//			}
//			logger.debug("End query record info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return vec;
//	}	
//	
//	public Vector<MonthlyRoadClearRecordBean> getMonthlyRoadClearRecordListByDayAndStatus(int deptId, int month, int status){
//		Vector<MonthlyRoadClearRecordBean> pVec = new Vector<MonthlyRoadClearRecordBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) 
//							+ " where date=" + month +" and dept_id=" + deptId
//							+ " and status=" + status
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query road clear record list info from [MONTHLY_ROADCLEAR] table!");
//			while (results.next()){
//				MonthlyRoadClearRecordBean bean = new MonthlyRoadClearRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("plate"), results.getString("maintain")
//						,results.getString("driver"), results.getInt("mileage"), results.getInt("run_num"), results.getInt("clear_mileage")
//						,results.getInt("gas"), results.getInt("meter"), results.getInt("clear_num")
//						,results.getBigDecimal("fee"), results.getString("remark"), results.getInt("status"));
//				pVec.add(bean);
//			}
//			logger.debug("End query road clear record list info from [MONTHLY_ROADCLEAR] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}
//	
//	public boolean deleteMonthlyRoadClearRecord(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.MONTHLY_ROADCLEAR) + condition.toString();
//		try{
//			logger.debug("Start delete daily road clear record with id:[" + id + "] from [MONTHLY_ROADCLEAR] table!");
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug(rows + " rows are deleted from table [ROAD_CLEAR_RECORD]");
//			logger.debug("End delete daily road clear record with id:[" + id + "] from [MONTHLY_ROADCLEAR] table!");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//
//	public boolean addSaftyRiskRecord( int userId, int deptId, int status,int date,
//			long spot_ts, long report_ts, String latitude, String longitude,
//			String span,String spot, String description, String reportDept, String reply,
//			String pic, String remark){
//		
//		boolean result = false;
//		
//		latitude = this.replayHTMLTags(latitude);
//		longitude = this.replayHTMLTags(longitude);
//		spot = this.replayHTMLTags(spot);
//		span = this.replayHTMLTags(span);
//		description = this.replayHTMLTags(description);
//		reportDept = this.replayHTMLTags(reportDept);
//		reply = this.replayHTMLTags(reply);
//		remark = this.replayHTMLTags(remark);
//		pic = this.replayHTMLTags(pic);
//		
//		StringBuffer condition = new StringBuffer(" (user_id,dept_id,status,date,spot_ts" +
//				",report_ts,latitude,longitude,span,spot,description,report_dept,reply" +
//				",pic,remark) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(deptId).append(",")
//							 .append(status).append(",")
//							 .append(date).append(",")
//							 .append(spot_ts).append(",")
//							 .append(report_ts).append(",")
//							 .append("'").append(latitude).append("'").append(",")
//							 .append("'").append(longitude).append("'").append(",")
//							 .append("'").append(span).append("'").append(",")
//							 .append("'").append(spot).append("'").append(",")							 
//							 .append("'").append(description).append("'").append(",")
//							 .append("'").append(reportDept).append("'").append(",")	
//							 .append("'").append(reply).append("'").append(",")	
//							 .append("'").append(pic).append("'").append(",")	
//							 .append("'").append(remark).append("'")
//							 .append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.SAFETY_RISK_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug("End add record into [SAFETY_RISK_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean checkSafetyRiskByUserAndId(int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.SAFETY_RISK_INFO) 
//							+ " where id=" + recordId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [SAFETY_RISK_INFO] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [SAFETY_RISK_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public Vector<SafetyRiskInfoBean> getSafetyRiskListByDeptAndDate(int deptId, int s_ts, int e_ts){
//		Vector<SafetyRiskInfoBean> pVec = new Vector<SafetyRiskInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.SAFETY_RISK_INFO) 
//							+ " where date>=" + s_ts
//							+ " and date<=" + e_ts
//							+ " and dept_id=" + deptId
//							+ " order by spot_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [SAFETY_RISK_INFO] table!");
//			while (results.next()){
//				SafetyRiskInfoBean bean = new SafetyRiskInfoBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"),results.getLong("spot_ts"), results.getLong("report_ts"), results.getString("latitude")
//						,results.getString("longitude"), results.getString("span"), results.getString("spot"), results.getString("description"), results.getString("report_dept")
//						,results.getString("reply"), BaseConstants.FieldReportLocation.SAFETYRISK + results.getString("pic"), results.getString("remark"), results.getInt("status"));
//				pVec.add(bean);
//			}
//			logger.debug("End query road clear record list info from [SAFETY_RISK_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}	
//	
//	public Vector<SafetyRiskInfoBean> getSafetyRiskListByDeptAndDate(int deptId, int date){
//		Vector<SafetyRiskInfoBean> pVec = new Vector<SafetyRiskInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.SAFETY_RISK_INFO) 
//							+ " where date=" + date
//							+ " and dept_id=" + deptId
//							+ " order by spot_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [SAFETY_RISK_INFO] table!");
//			while (results.next()){
//				SafetyRiskInfoBean bean = new SafetyRiskInfoBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"),results.getLong("spot_ts"), results.getLong("report_ts"), results.getString("latitude")
//						,results.getString("longitude"), results.getString("span"), results.getString("spot"), results.getString("description"), results.getString("report_dept")
//						,results.getString("reply"), BaseConstants.FieldReportLocation.SAFETYRISK + results.getString("pic"), results.getString("remark"), results.getInt("status"));
//				pVec.add(bean);
//			}
//			logger.debug("End query road clear record list info from [SAFETY_RISK_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}
//	
//	public int getSafetyRiskNumByDate(int deptId, String date){
//		int sum = 0;
//		String queryState = "select count(*) from " + DBName.getTableName(DBName.SAFETY_RISK_INFO) 
//							+ " where date=" + date
//							+ " and dept_id=" + deptId
//							+ ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [SAFETY_RISK_INFO] table!");
//			while (results.next()){
//				sum = results.getInt(1);
//			}
//			logger.debug("End query record list info from [SAFETY_RISK_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return sum;
//	}	
//	
//	public boolean addAdBoardRecord( int userId, int deptId, int status,int type,
//			int date, long spot_ts, long report_ts, String latitude, String longitude,
//			String span, String spot, String description, String reportDept, String reply,
//			String pic, String remark){
//		
//		boolean result = false;
//		latitude = this.replayHTMLTags(latitude);
//		longitude = this.replayHTMLTags(longitude);
//		spot = this.replayHTMLTags(spot);
//		description = this.replayHTMLTags(description);
//		reportDept = this.replayHTMLTags(reportDept);
//		reply = this.replayHTMLTags(reply);
//		remark = this.replayHTMLTags(remark);
//		pic = this.replayHTMLTags(pic);		
//		
//		StringBuffer condition = new StringBuffer(" (user_id,dept_id,status,type,date, spot_ts" +
//				",report_ts,latitude,longitude,span, spot,description,report_dept,reply" +
//				",pic,remark) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(deptId).append(",")
//							 .append(status).append(",")
//							 .append(type).append(",")
//							 .append(date).append(",")
//							 .append(spot_ts).append(",")
//							 .append(report_ts).append(",")
//							 .append("'").append(latitude).append("'").append(",")
//							 .append("'").append(longitude).append("'").append(",")
//							 .append("'").append(spot).append("'").append(",")
//							 .append("'").append(spot).append("'").append(",")
//							 .append("'").append(description).append("'").append(",")
//							 .append("'").append(reportDept).append("'").append(",")	
//							 .append("'").append(reply).append("'").append(",")	
//							 .append("'").append(pic).append("'").append(",")	
//							 .append("'").append(remark).append("'")
//							 .append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.AD_BOARD_RISK) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug("End add record into [AD_BOARD_RISK] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public Vector<AdBoardInfoBean> getAdBoardListByDeptAndDate(int deptId, int date){
//		Vector<AdBoardInfoBean> pVec = new Vector<AdBoardInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.AD_BOARD_RISK) 
//							+ " where date=" + date
//							+ " and dept_id=" + deptId
//							+ " order by spot_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [AD_BOARD_RISK] table!");
//			while (results.next()){
//				AdBoardInfoBean bean = new AdBoardInfoBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("status"),results.getInt("type"),results.getInt("date"),results.getLong("spot_ts")
//						, results.getLong("report_ts"), results.getString("latitude"),results.getString("longitude")
//						, results.getString("span"), results.getString("spot"), results.getString("description"), results.getString("report_dept")
//						,results.getString("reply"), BaseConstants.FieldReportLocation.ADBOARD + results.getString("pic")
//						, results.getString("remark"));
//				pVec.add(bean);
//			}
//			logger.debug("End query record list info from [AD_BOARD_RISK] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}	
//	
//	public Vector<AdBoardInfoBean> getAdBoardListByDeptAndDate(int deptId, int sdate, int edate){
//		Vector<AdBoardInfoBean> pVec = new Vector<AdBoardInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.AD_BOARD_RISK) 
//							+ " where date>=" + sdate
//							+ " and date<=" + edate
//							+ " and dept_id=" + deptId
//							+ " order by spot_ts desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [AD_BOARD_RISK] table!");
//			while (results.next()){
//				AdBoardInfoBean bean = new AdBoardInfoBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("status"),results.getInt("type"),results.getInt("date"),results.getLong("spot_ts")
//						, results.getLong("report_ts"), results.getString("latitude"),results.getString("longitude")
//						, results.getString("span"), results.getString("spot"), results.getString("description"), results.getString("report_dept")
//						,results.getString("reply"), BaseConstants.FieldReportLocation.ADBOARD + results.getString("pic")
//						, results.getString("remark"));
//				pVec.add(bean);
//			}
//			logger.debug("End query record list info from [AD_BOARD_RISK] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}	
//	
//	public boolean addRescueRecord( int userId, int deptId,int date,
//			long report_ts, long arrieve_ts, String latitude, String longitude,
//			String span, String spot, String description, String reportDept, String reply,
//			String lost, String pic, String remark){
//		
//		boolean result = false;
//		latitude = this.replayHTMLTags(latitude);
//		longitude = this.replayHTMLTags(longitude);
//		spot = this.replayHTMLTags(spot);
//		description = this.replayHTMLTags(description);
//		reportDept = this.replayHTMLTags(reportDept);
//		reply = this.replayHTMLTags(reply);
//		remark = this.replayHTMLTags(remark);
//		
//		StringBuffer condition = new StringBuffer(" (user_id,dept_id,date" +
//				",report_ts,arrieve_ts,latitude,longitude,span,spot,description,report_dept,reply" +
//				",lost, pic,remark) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(deptId).append(",")
//							 .append(date).append(",")
//							 .append(report_ts).append(",")
//							 .append(arrieve_ts).append(",")							 
//							 .append("'").append(latitude).append("'").append(",")
//							 .append("'").append(longitude).append("'").append(",")
//							 .append("'").append(span).append("'").append(",")
//							 .append("'").append(spot).append("'").append(",")
//							 .append("'").append(description).append("'").append(",")
//							 .append("'").append(reportDept).append("'").append(",")	
//							 .append("'").append(reply).append("'").append(",")	
//							 .append("'").append(lost).append("'").append(",")	
//							 .append("'").append(pic).append("'").append(",")	
//							 .append("'").append(remark).append("'")
//							 .append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.RESCUE_INFO) + condition.toString();
//		try{
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug("End add record into [RESCUE_INFO] table!" + rows + " rows.");
//		} catch (Exception e){
//			logger.debug(insertState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public Vector<RescueInfoBean> getRescueListByDeptAndDate(int deptId, int date){
//		Vector<RescueInfoBean> pVec = new Vector<RescueInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.RESCUE_INFO) 
//							+ " where date=" + date
//							+ " and dept_id=" + deptId
//							+ " order by id desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [RESCUE_INFO] table!");
//			while (results.next()){
//				RescueInfoBean bean = new RescueInfoBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						, results.getInt("date"),results.getLong("report_ts"), results.getLong("arrieve_ts"),results.getString("latitude")
//						, results.getString("longitude"), results.getString("span"), results.getString("spot"), results.getString("description")
//						, results.getString("report_dept"),results.getString("reply"),results.getString("lost")
//						, BaseConstants.FieldReportLocation.RESCUE + results.getString("pic"), results.getString("remark"));
//				
//				pVec.add(bean);
//			}
//			logger.debug("End query road clear record list info from [RESCUE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}
//	
//	public Vector<RescueInfoBean> getRescueListByDeptAndDate(int deptId, int sdate, int edate){
//		Vector<RescueInfoBean> pVec = new Vector<RescueInfoBean>();
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.RESCUE_INFO) 
//							+ " where date>=" + sdate
//							+ " and date<=" + edate
//							+ " and dept_id=" + deptId
//							+ " order by id desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [RESCUE_INFO] table!");
//			while (results.next()){
//				RescueInfoBean bean = new RescueInfoBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"),results.getLong("report_ts"), results.getLong("arrieve_ts"),results.getString("latitude")
//						,results.getString("longitude"), results.getString("span"), results.getString("spot"), results.getString("description")
//						, results.getString("report_dept"),results.getString("reply"),results.getString("lost")
//						, BaseConstants.FieldReportLocation.RESCUE + results.getString("pic"), results.getString("remark"));
//				
//				pVec.add(bean);
//			}
//			logger.debug("End query road clear record list info from [RESCUE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return pVec;
//	}		
//	
//	public int getRescueNumByDate(int deptId, String date){
//		int sum = 0;
//		String queryState = "select count(*) from " + DBName.getTableName(DBName.RESCUE_INFO) 
//							+ " where date=" + date
//							+ " and dept_id=" + deptId
//							+ ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [RESCUE_INFO] table!");
//			while (results.next()){
//				sum = results.getInt(1);
//			}
//			logger.debug("End query road clear record list info from [RESCUE_INFO] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return sum;
//	}		
//	
//	public boolean addDetailDailyPatrolRecord(int userId, int deptId, 
//			int month, String gpsStatus, String videoStatus,
//			String casualties, int mileage, int rescue, int accident,
//			int compensate, int people, int risk, int land, int landOccupied,
//			int status){
//		
//		boolean result = false;
//		
//		casualties = this.replayHTMLTags(casualties);
//		videoStatus = this.replayHTMLTags(videoStatus);
//		gpsStatus = this.replayHTMLTags(gpsStatus);
//		
//		StringBuffer condition = new StringBuffer(" (user_id,dept_id,date" +
//				",gps_status,video_status,casualties,mileage,rescue_num,accident_num" +
//				",compensate_num,people_num,risk_num,land_num,land_occupied,status) values ");
//		condition.append("(").append(userId).append(",")
//						     .append(deptId).append(",")
//							 .append(month).append(",")
//							 .append("'").append(gpsStatus).append("'").append(",")
//							 .append("'").append(videoStatus).append("'").append(",")
//							 .append("'").append(casualties).append("'").append(",")
//							 .append(mileage).append(",")
//							 .append(rescue).append(",")
// 							 .append(accident).append(",")
// 							 .append(compensate).append(",")
// 							 .append(people).append(",")
//							 .append(risk).append(",")
// 							 .append(land).append(",")
// 							 .append(landOccupied).append(",")
//							 .append(status).append(")").append(";");
//							 
//		String insertState = SQLStatementQuery.insert() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) + condition.toString();
//		try{
//			logger.debug("Start add record into [DETAIL_PATROL_REPORT] table!");
//			int rows = stat.executeUpdate(insertState);
//			result = true;
//			logger.debug("End add record into [DETAIL_PATROL_REPORT] table." + rows + " rows are inserted into table [DETAIL_PATROL_REPORT]");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean modifyDetailDailyPatrolRecord(int id, String gpsStatus, String videoStatus,
//			String casualties, int mileage, int rescue, int accident,
//			int compensate, int people, int risk, int land, int landOccupied){
//		
//		boolean result = false;
//		
//		casualties = this.replayHTMLTags(casualties);
//		videoStatus = this.replayHTMLTags(videoStatus);
//		gpsStatus = this.replayHTMLTags(gpsStatus);
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("gps_status='").append(gpsStatus).append("'").append(",")
//							 .append("video_status='").append(videoStatus).append("'").append(",")
//							 .append("casualties='").append(casualties).append("'").append(",")
//							 .append("mileage=").append(mileage).append(",")
//							 .append("rescue_num=").append(rescue).append(",")
//							 .append("accident_num=").append(accident).append(",")
//							 .append("people_num=").append(people).append(",")
//							 .append("compensate_num=").append(compensate).append(",")
//							 .append("risk_num=").append(risk).append(",")
//							 .append("land_num=").append(land).append(",")
//							 .append("land_occupied=").append(landOccupied)
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) + condition.toString();
//		try{
//			logger.debug("Start modify record into [DETAIL_PATROL_REPORT] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End modify record into [DETAIL_PATROL_REPORT] table!" + rows + " rows are updated into table [DETAIL_PATROL_REPORT]");
//		} catch (Exception e){
//			logger.debug(updateState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public boolean modifyDetailDailyPatrolRecordStatus(int id, int status){
//		
//		boolean result = false;
//		
//		StringBuffer condition = new StringBuffer(" set ");
//		condition.append("status=").append(status)
//							 .append(" ").append("where").append(" id=").append(id).append(";");
//							 
//		String updateState = SQLStatementQuery.update() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) + condition.toString();
//		try{
//			logger.debug("Start modify record into [DETAIL_PATROL_REPORT] table!");
//			int rows = stat.executeUpdate(updateState);
//			result = true;
//			logger.debug("End modify record into [DETAIL_PATROL_REPORT] table!" + rows + " rows are updated");
//		} catch (Exception e){
//			logger.debug(updateState);
//			logger.error(e.getMessage());
//		}
//		return result;
//	}		
//
//	public boolean checkDetailDailyPatrolRecordByUserAndId(int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) 
//							+ " where id=" + recordId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [DETAIL_PATROL_REPORT] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [DETAIL_PATROL_REPORT] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean checkDetailDailyPatrolByDeptAndId(int deptId, int recordId){
//		boolean result = false;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) 
//							+ " where id=" + recordId
//							+ " and dept_id=" + deptId + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [DETAIL_PATROL_REPORT] table!");
//			while (results.next()){
//				result = true;
//				break;
//			}
//			logger.debug("End query record info from [DETAIL_PATROL_REPORT] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
//	
//	public DetailDailyPatrolRecordBean getDetailDailyPatrolById(int id){
//		DetailDailyPatrolRecordBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) 
//							+ " where id=" + id + ";";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query record info from [DETAIL_PATROL_REPORT] table!");
//			while (results.next()){
//				bean = new DetailDailyPatrolRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("gps_status"), results.getString("video_status"), results.getString("casualties")
//						,results.getInt("mileage"), results.getInt("rescue_num"), results.getInt("accident_num"), results.getInt("compensate_num")
//						,results.getInt("people_num"), results.getInt("risk_num"), results.getInt("land_num")
//						,results.getInt("land_occupied"), results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [DETAIL_PATROL_REPORT] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public DetailDailyPatrolRecordBean getDetailDailyPatrolReportByDate(int deptId, int date){
//		DetailDailyPatrolRecordBean bean  = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) 
//							+ " where date=" + date +" and dept_id=" + deptId
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query road clear record list info from [DETAIL_PATROL_REPORT] table!");
//			while (results.next()){
//				bean = new DetailDailyPatrolRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("gps_status"), results.getString("video_status"), results.getString("casualties")
//						,results.getInt("mileage"), results.getInt("rescue_num"), results.getInt("accident_num"), results.getInt("compensate_num")
//						,results.getInt("people_num"), results.getInt("risk_num"), results.getInt("land_num")
//						,results.getInt("land_occupied"), results.getInt("status"));
//				break;
//			}
//			logger.debug("End query record info from [DETAIL_PATROL_REPORT] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//
//	public DetailDailyPatrolRecordBean getDetailDailyPatrolReportByDateAndStatus(int deptId, int date, int status){
//		DetailDailyPatrolRecordBean bean = null;
//		String queryState = SQLStatementQuery.getQueryAllPrefix() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) 
//							+ " where date=" + date +" and dept_id=" + deptId
//							+ " and status=" + status
//							+ " order by date desc;";
//		try{
//			ResultSet results = stat.executeQuery(queryState);
//			logger.debug("Start query road clear record list info from [DETAIL_PATROL_REPORT] table!");
//			while (results.next()){
//				bean = new DetailDailyPatrolRecordBean(results.getInt("id"), results.getInt("user_id"), results.getInt("dept_id")
//						,results.getInt("date"), results.getString("gps_status"), results.getString("video_status"), results.getString("casualties")
//						,results.getInt("mileage"), results.getInt("rescue_num"), results.getInt("accident_num"), results.getInt("compensate_num")
//						,results.getInt("people_num"), results.getInt("risk_num"), results.getInt("land_num")
//						,results.getInt("land_occupied"), results.getInt("status"));
//			}
//			logger.debug("End query road clear record list info from [DETAIL_PATROL_REPORT] table!");
//			results.close();
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return bean;
//	}
//	
//	public boolean deleteDetailDailyPatrol(int id){
//		
//		boolean result = false;
//		String condition = " where id=" + id + ";";
//							 
//		String deleteState = SQLStatementQuery.delete() + DBName.getTableName(DBName.DETAIL_PATROL_REPORT) + condition.toString();
//		try{
//			logger.debug("Start delete daily road clear record with id:[" + id + "] from [DETAIL_PATROL_REPORT] table!");
//			int rows = stat.executeUpdate(deleteState);
//			result = true;
//			logger.debug("End delete daily road clear record with id:[" + id + "] from [DETAIL_PATROL_REPORT] table!" + rows + " rows are deleted.");
//		} catch (Exception e){
//			logger.error(e.getMessage());
//		}
//		return result;
//	}	
	
	public String getTimestampString(){
		return new Timestamp(System.currentTimeMillis()).toString();
	}
	
//	private String replayHTMLTags(String tags){
//		if (tags != null) {
//			tags = tags.replaceAll("'", "\"");
//			tags = tags.replaceAll("<", "< ");
//			tags = tags.replaceAll(">", " >");
//			tags = tags.replaceAll("\\\\", "\"");
//			tags = tags.trim();
////			try{
////				tags = java.net.URLEncoder.encode(tags,"utf-8");
////			}catch (Exception e){
////				logger.error(e.toString());
////			}
//		}
//		return tags;
//	}
}
