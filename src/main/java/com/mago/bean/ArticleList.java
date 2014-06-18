package com.mago.bean;

import java.io.Serializable;
import java.util.Date;

public class ArticleList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5106120706100652220L;
	private int classId;
	private int articleId;
	private String articleName;
	private String publishedBy;
	private Date publishDate;
	private int viewNum;
	private String articleDescription;
	
	public ArticleList(int classId, int articleId, String articleName, 
			String publishedBy, Date publishDate, int viewNum, String articleDescription){
		this.setClassId(classId);
		this.setArticleId(articleId);
		this.setArticleName(articleName);
		this.setPublishedBy(publishedBy);
		this.setPublishDate(publishDate);
		this.setViewNum(viewNum);
		this.setArticleDescription(articleDescription);
	}
	
	/**
	 * @return the classId
	 */
	public int getClassId() {
		return classId;
	}
	/**
	 * @param classId the classId to set
	 */
	public void setClassId(int classId) {
		this.classId = classId;
	}
	/**
	 * @return the articleId
	 */
	public int getArticleId() {
		return articleId;
	}
	/**
	 * @param articleId the articleId to set
	 */
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	/**
	 * @return the publishedBy
	 */
	public String getPublishedBy() {
		return publishedBy;
	}
	/**
	 * @param publishedBy the publishedBy to set
	 */
	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}
	/**
	 * @return the viewNum
	 */
	public int getViewNum() {
		return viewNum;
	}
	/**
	 * @param viewNum the viewNum to set
	 */
	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}
	/**
	 * @return the articleDescrption
	 */
	public String getArticleDescription() {
		return articleDescription;
	}
	/**
	 * @param articleDescrption the articleDescrption to set
	 */
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}

	/**
	 * @return the articleName
	 */
	public String getArticleName() {
		return articleName;
	}

	/**
	 * @param articleName the articleName to set
	 */
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	/**
	 * @return the publishDate
	 */
	public Date getPublishDate() {
		return publishDate;
	}

	/**
	 * @param publishDate the publishDate to set
	 */
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	
}
