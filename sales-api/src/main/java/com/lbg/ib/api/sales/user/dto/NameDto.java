package com.lbg.ib.api.sales.user.dto;

import java.util.List;

public class NameDto {

	private List<String> initials;
	private String firstName;
	private List<String> foreNames;
	private List<String> middleName;
	private String lastName;
	private List<String> title;
    private List<String> suffixTitle;
    private List<String> salutation;
    private String generationSuffix;
    
	public List<String> getInitials() {
		return initials;
	}
	public void setInitials(List<String> initials) {
		this.initials = initials;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public List<String> getForeNames() {
		return foreNames;
	}
	public void setForeNames(List<String> foreNames) {
		this.foreNames = foreNames;
	}
	public List<String> getMiddleName() {
		return middleName;
	}
	public void setMiddleName(List<String> middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public List<String> getTitle() {
		return title;
	}
	public void setTitle(List<String> title) {
		this.title = title;
	}
	public List<String> getSuffixTitle() {
		return suffixTitle;
	}
	public void setSuffixTitle(List<String> suffixTitle) {
		this.suffixTitle = suffixTitle;
	}
	public List<String> getSalutation() {
		return salutation;
	}
	public void setSalutation(List<String> salutation) {
		this.salutation = salutation;
	}
	public String getGenerationSuffix() {
		return generationSuffix;
	}
	public void setGenerationSuffix(String generationSuffix) {
		this.generationSuffix = generationSuffix;
	}
    
    
}
