
package com.lbg.ib.api.sales.communication.domain;

import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.EMAIL;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.POSTCODE;
import static com.lbg.ib.api.sales.common.validation.regex.RegexPatterns.REQUIRED_ALPHA_SPACE;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.StringFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class InvolvedPartyDetails {
    @RequiredFieldValidation
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = 16)
    private String title;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = 24)
    private String firstName;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = REQUIRED_ALPHA_SPACE, maxLength = 25)
    private String lastName;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = EMAIL, maxLength = 80)
    private String email;

    @RequiredFieldValidation
    @StringFieldValidation(pattern = POSTCODE)
    private String postCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((postCode == null) ? 0 : postCode.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InvolvedPartyDetails other = (InvolvedPartyDetails) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (firstName == null) {
            if (other.firstName != null) {
                return false;
            }
        } else if (!firstName.equals(other.firstName)) {
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(other.lastName)) {
            return false;
        }
        if (postCode == null) {
            if (other.postCode != null) {
                return false;
            }
        } else if (!postCode.equals(other.postCode)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InvolvedPartyDetails [title=" + title + ", firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + ", postCode=" + postCode + "]";
    }

}
