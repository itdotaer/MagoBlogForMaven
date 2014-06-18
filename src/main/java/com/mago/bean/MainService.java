package com.mago.bean;

import java.io.Serializable;

public class MainService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8554152297874968691L;	
	private int artitleId;
	private String artitlceName;
	private String artitleDescription;
	
	public MainService(int artitleId, String artitleName, String atitleDescription){
		this.setArtitleId(artitleId);
		this.setArtitlceName(artitleName);
		this.setArtitleDescription(atitleDescription);
	}
	
	/**
	 * @return the artitleId
	 */
	public int getArtitleId() {
		return artitleId;
	}
	/**
	 * @param artitleId the artitleId to set
	 */
	public void setArtitleId(int artitleId) {
		this.artitleId = artitleId;
	}
	/**
	 * @return the artitlceName
	 */
	public String getArtitlceName() {
		return artitlceName;
	}
	/**
	 * @param artitlceName the artitlceName to set
	 */
	public void setArtitlceName(String artitlceName) {
		this.artitlceName = artitlceName;
	}
	/**
	 * @return the artitleDescription
	 */
	public String getArtitleDescription() {
		return artitleDescription;
	}
	/**
	 * @param artitleDescription the artitleDescription to set
	 */
	public void setArtitleDescription(String artitleDescription) {
		this.artitleDescription = artitleDescription;
	}
	
}
