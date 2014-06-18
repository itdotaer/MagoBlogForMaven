package com.mago.bean;

public class UserExt extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7396603137928243638L;	
	private String fullName;
	private String sex;
	private String description;
	private String createdBy;
	private String createDate;
	private String lastUpdatedBy;
	private String lastUpdateDate;
	
	public UserExt(String id, String userName, String mail, String password, String fullName, 
			String sex, String description, String createdBy, String createDate, String lastUpdatedBy, 
			String lasteUpdateDate) {
		super(id, userName, mail, password);
		this.setFullName(fullName);
		this.setSex(sex);
		this.setDescription(description);
		this.setCreatedBy(createdBy);
		this.setCreateDate(createDate);
		this.setLastUpdatedBy(lastUpdatedBy);
		this.setLastUpdateDate(lastUpdateDate);
	}
	

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}


	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}


	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	/**
	 * @return the lastUpdateDate
	 */
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}


	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}


	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}


	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

}
