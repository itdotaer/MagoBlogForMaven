package com.mago.bean;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3871316785824255606L;	
	private int articleId;
	private String articleName;
	private Date publishDate;
	private int viewNum;
	private String articleDescription;
	private String articleContent;
	private String publishedBy;
	private int isPublish;
	private int isMainService;
	private int isAd;
	
	public Article(int articleId, String articleName, Date publishDate, 
			int viewNum, String articleDescription, String articleContent, 
			String publishedBy, int isPublish, int isMainService, int isAd){
		this.setArticleId(articleId);
		this.setArticleName(articleName);
		this.setPublishDate(publishDate);
		this.setViewNum(viewNum);
		this.setArticleDescription(articleDescription);
		this.setArticleContent(articleContent);
		this.setPublishedBy(publishedBy);
		this.setIsPublish(isPublish);
		this.setIsMainService(isMainService);
		this.setIsAd(isAd);
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
	 * @return the articleContent
	 */
	public String getArticleContent() {
		return articleContent;
	}
	/**
	 * @param articleContent the articleContent to set
	 */
	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
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
	 * @return the isPublish
	 */
	public int getIsPublish() {
		return isPublish;
	}
	/**
	 * @param isPublish the isPublish to set
	 */
	public void setIsPublish(int isPublish) {
		this.isPublish = isPublish;
	}
	/**
	 * @return the isMainService
	 */
	public int getIsMainService() {
		return isMainService;
	}
	/**
	 * @param isMainService the isMainService to set
	 */
	public void setIsMainService(int isMainService) {
		this.isMainService = isMainService;
	}
	/**
	 * @return the isAd
	 */
	public int getIsAd() {
		return isAd;
	}
	/**
	 * @param isAd the isAd to set
	 */
	public void setIsAd(int isAd) {
		this.isAd = isAd;
	}
}
