package com.lbg.ib.api.sales.overdraft.constant;

import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;

@Component
public class OverdraftConstants {

	public static final String APPLICATION_SOURCE_CODE = "004";
	public static final String BANK_ORGANIZATION_CODE = "001";
	public static final String DEFAULT_CURRENCY_CODE = "GBP";
	public static final String FILE_NAME_CREDIT_SCORE_SOURCE_SYSTEM = "index-overdraft.json";
	public static final Double ZERO = new Double(0);
	public static final String EXCEEDING_OVERDRAFT_LIMIT = "Demanded overdraft limit cannot exceeds the maximum amount that can be offered";
	public static final String SAME_OVERDRAFT_LIMIT = "New overdraft limit cannot be same as existing overdraft limit";
	public static final String NEGATIVE_AVAILABLE_BALANCE = "Available Balance cannot be negative";

	public enum OverdraftResponseError {

		EXCEEDING_OVERDRAFT_LIMIT_ERROR(ResponseErrorConstants.INVALID_OVERDRAFT_AMOUNT, EXCEEDING_OVERDRAFT_LIMIT),
		SAME_OVERDRAFT_LIMIT_ERROR(ResponseErrorConstants.INVALID_OVERDRAFT_AMOUNT, SAME_OVERDRAFT_LIMIT),
		NEGATIVE_AVAILABLE_BALANCE_ERROR(ResponseErrorConstants.INVALID_OVERDRAFT_AMOUNT, NEGATIVE_AVAILABLE_BALANCE);

		private String errorCode;
		private String errorMessage;

		private OverdraftResponseError(String code, String message) {
			this.errorCode = code;
			this.errorMessage = message;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
	}

	public enum OverdraftLimit {
		PERMANENT("001"), DECREASING_REGULARLY("002"), DECREASIING_ONCE("003");

		private String value;

		private OverdraftLimit(String value) {
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}
	}

	public enum OverdraftPurposeCode {

		NOT_KNOWN("000");

		private String value;

		private OverdraftPurposeCode(String value) {
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}
	}

	public enum PartyBusnessRelationship {

		FRANCHISED("1");

		private String value;

		private PartyBusnessRelationship(String value) {
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}
	}

	public enum IncomeFrequency {

		MONTHLY("4");

		private String value;

		private IncomeFrequency(String value) {
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}
	}

	public enum PostalAddressCodes {

		UK_PAF("001"), BFPO("002"), FOREIGN("003"), OTHER("004");

		private String value;

		private PostalAddressCodes(String value) {
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}
	}

	public enum EmploymentStatusCode {

		UNEMPLOYED("000"), SELF_EMPLOYED("001"), PART_TIME("002"), EMPLOYED("003"), FULL_TIME_STUDENT("004"),
		HOME_WORKER("005"), RETIRED("006");

		private String value;

		private EmploymentStatusCode(String value) {
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}
	}

	public enum OverdraftFulfillmentOperation {
		E160("E160"), E169("E169"), E170("E170");

		private String operation;
		private OverdraftFulfillmentSuccessResponse sucessResponse;

		OverdraftFulfillmentOperation(String ops) {
			this.operation = ops;
		}

		public String toString() {
			return operation;
		}

		public String successResponse() {
			switch (this) {
			case E160:
				sucessResponse = OverdraftFulfillmentSuccessResponse.OD_REMOVED;
				break;
			case E170:
				sucessResponse = OverdraftFulfillmentSuccessResponse.OD_AMENDED;
				break;
			case E169:
				sucessResponse = OverdraftFulfillmentSuccessResponse.OD_CREATED;
				break;
			default:
				sucessResponse = OverdraftFulfillmentSuccessResponse.UNSUPPORTED_OPERATION;
			}
			return sucessResponse.stringValue();
		}
	}

	public enum OverdraftFulfillmentSuccessResponse {

		OD_REMOVED("Removed Overdraft"), OD_AMENDED("Amended Overdraft"), OD_CREATED("Issued Overdraft"),
		UNSUPPORTED_OPERATION("UNSUPPORTED_OPERATION");

		private String value;

		OverdraftFulfillmentSuccessResponse(String value) {
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}
	}

	public enum OverdraftAsmDecisionCode {
		ACCEPT(1, "Accept"), REFER(2, "Refer"), DECLINE(3, "Decline"), DOWNSELL(4, "Downsell"), UNKNOWN(5, null);
		private Integer code;
		private String value;

		private OverdraftAsmDecisionCode(Integer code, String value) {
			this.code = code;
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}

		public static OverdraftAsmDecisionCode deriveFromValue(Integer code) {
			for (OverdraftAsmDecisionCode constant : values()) {
				if (constant.code == code) {
					return constant;
				}
			}
			return UNKNOWN;
		}

	}
	
	public enum ProductFamilyIdentifier{
		BOS("1301"), HALIFAX("2301"),LLOYDS("301");
		
		private String value;
		
		ProductFamilyIdentifier(String value) {
			this.value = value;
		}

		public String stringValue() {
			return this.value;
		}
	}

}
