/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.dto;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.EMAIL;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

/**
 * Class contain personal details of users.
 * 
 * @author tkhann
 */
@Validate
public class PersonalDetails {
	/**
	 * Updated email details
	 */
	@RequiredFieldValidation
	@StringFieldValidation(pattern = EMAIL, maxLength = 80)
	private String emailAddress;

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
