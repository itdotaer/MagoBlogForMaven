package com.mago.bean;

import java.io.Serializable;
import java.util.Date;

public class Classe implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4497453120244387274L;	
	private int classId;
	private String className;
	private int productId;
	private int parentId;
	private int createdBy;
	private Date createDate;
	private int lastUpdatedBy;
	private Date lastUpdateDate;
	
	public Classe(int classId, String className, int productId, int parentId, 
			int createdBy, Date createDate, int lastUpdatedBy, Date lastUpdateDate){
		this.setClassId(classId);
		this.setClassName(className);
		this.setProductId(productId);
		this.setParentId(parentId);
		this.setCreatedBy(createdBy);
		this.setCreateDate(createDate);
		this.setLastUpdatedBy(lastUpdatedBy);
		this.setLastUpdateDate(lastUpdateDate);
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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the productId
	 */
	public int getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
	/**
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * @return the createdBy
	 */
	public int getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the lastUpdateBy
	 */
	public int getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * @param lastUpdateBy the lastUpdateBy to set
	 */
	public void setLastUpdatedBy(int lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}
