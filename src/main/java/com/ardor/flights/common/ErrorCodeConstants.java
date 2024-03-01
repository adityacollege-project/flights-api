/**
 * 
 */
package com.ardor.flights.common;

/**
 * 
 */
public class ErrorCodeConstants {
	//Flight Search Exceptions
	public static final String FSE_0024 = "FSE0024:Maximum number of flight searches allowed is 5";
	public static final String FSE_0025 = "FSE0025:Invalid location code";

	//Reprice error codes
	public static final String RPE_0001 = "RPE0001:JID cannot be blank.";
	public static final String RPE_0002 = "RPE0002:Passenger details cannot be blank.";
	public static final String RPE_0003 = "RPE0003:Invalid pax details";
	public static final String RPE_0004 = "RPE0004:The adult count must be at least 1";
	public static final String RPE_0005 = "RPE0005:The adult count can be a maximum of 9";
	public static final String RPE_0006 = "RPE0006:The child count should not be less than 0";
	public static final String RPE_0007 = "RPE0007:The child count can be a maximum of 8";
	public static final String RPE_0008 = "RPE0008:The infant count should not be less than 0";
	public static final String RPE_0009 = "RPE0009:The infant count can be a maximum of 8";
	public static final String RPE_00010 = "RPE0010:Invalid JID";
	public static final String RPE_00011 = "RPE011:Failed to prepare supplier request.";
	public static final String RPE_00012 = "RPE012:Failed to parse the reprice response from API.";
	public static final String RPE_00013 = "RPE013:Itinerary Expired at supplier.";
	public static final String RPE_00014 = "RPE014:Failed to map the response.";
	public static final String RPE_00015 = "RPE015:Failed to get response from supplier.";
	public static final String RPE_00016 = "RPE0010:Invalid Fare Ids";

	//Seatmap error codes
	public static final String SME_0001 = "SMW0001:JID cannot be blank.";
	public static final String SME_0002 = "SMW0002:Invalid JID";
	public static final String SME_0003 = "SME0003:Seat map not available from supplier";
	public static final String SME_0004 = "SME0004:Failed to get response from supplier.";
	public static final String SME_0005 = "SMW0002:Invalid Fare Ids";

	//Booking Error Codes
	public static final String FBE_0001 = "FBE0001:JId cannot be blank";
	public static final String FBE_0002 = "FBE0002:Invalid fareId";
	public static final String FBE_0003 = "FBE0003:Failed to prepare supplier booking request.";
	public static final String FBE_0004 = "FBE0004:Failed to parse the Supplier Response";
	public static final String FBE_0005 = "FBE0005:Failed to get response from supplier";
	public static final String FBE_0006 = "FBE0006:Fair Id should contain both Jid and Supplier Id";
	public static final String FBE_0007 = "FBE0007:PaxType cannot be null";
	public static final String FBE_0008 = "FBE0008:PaxType cannot be blank";
	public static final String FBE_0009 = "FBE0009:Gender cannot be null";
	public static final String FBE_0010 = "FBE00010:Gender cannot be blank";
	public static final String FBE_0011 = "FBE00011:userTitle cannot be null";
	public static final String FBE_0012 = "FBE00012:user Title cannot be blank";
	public static final String FBE_0013 = "FBE00013:firstName cannot be Null";
	public static final String FBE_0014 = "FBE00014:firstName cannot be blank";
	public static final String FBE_0015 = "FBE00015:lastName cannot be null";
	public static final String FBE_0016 = "FBE00016:lastName cannot be blank";
	public static final String FBE_0017 = "FBE00017:Date of Birth cannot be Null";
	public static final String FBE_0018 = "FBE00018:Date of Birth cannot be blank";
	public static final String FBE_0019 = "FBE00019:phoneNumber cannot be null";
	public static final String FBE_0020 = "FBE00020:phoneNumber cannot be blank";
	public static final String FBE_0021 = "FBE00021:Only Numerics are Allowed for Phone Number with a maximum of eleven digits.";
	public static final String FBE_0022 = "FBE00022:Email cannot be Null";
	public static final String FBE_0023 = "FBE00023:Email cannot be blank";
	public static final String FBE_0024 = "FBE00024:paymentType cannot be Null";
	public static final String FBE_0025 = "FBE00025:paymentType cannot be Blank";
	public static final String FBE_0026 = "FBE00026:cardType cannot be Null";
	public static final String FBE_0027 = "FBE00027:cardType cannot be Null";
	public static final String FBE_0028 = "FBE00028:cardNumber cannot be Null";
	public static final String FBE_0029 = "FBE00029:cardNumber cannot be blank";
	public static final String FBE_0030 = "FBE00030:cvv cannot be blank";
	public static final String FBE_0031 = "FBE00031:cvv cannot be blank";
	public static final String FBE_0032 = "FBE00032:expiryDate cannot be Null";
	public static final String FBE_0033 = "FBE00033:expiryDate cannot be blank";
	public static final String FBE_0034 = "FBE00034:Billing Phone Number cannot be Null";
	public static final String FBE_0035 = "FBE00035:Billing Phone Number cannot be blank";
	public static final String FBE_0036 = "FBE00036:Billing Name cannot be null";
	public static final String FBE_0037 = "FBE00037:Billing Name cannot be blank";
	public static final String FBE_0038 = "FBE00038:Billing Address cannot be Null";
	public static final String FBE_0039 = "FBE00039:Billing Address cannot be Blank";
	public static final String FBE_0040 = "FBE00040:City cannot be null";
	public static final String FBE_0041 = "FBE00041:City cannot be blank";
	public static final String FBE_0042 = "FBE00042:State cannot be Null";
	public static final String FBE_0043 = "FBE00043:State cannot be blank";
	public static final String FBE_0044 = "FBE00044:Country cannot be Null";
	public static final String FBE_0045 = "FBE00045:Country cannot be blank";
	public static final String FBE_0046 = "FBE00046:Zip Code cannot be Null";
	public static final String FBE_0047 = "FBE00047:Country cannot be blank";
	public static final String FBE_0048 = "FBE0048:Passenger details cannot be empty";
	public static final String FBE_0049 = "FBE0049:Invalid pax type. Allowed values are ADT, CHD, INF";
	public static final String FBE_0050 = "FBE0050:Invalid gender. Allowed values are M, F, O";
	public static final String FBE_0051 = "FBE0051:Invalid  user title. Allowed values are Mr, Mrs";
	public static final String FBE_0052 = "FBE0052:First name should contain only alphabetic characters.";
	public static final String FBE_0053 = "FBE0053:Middle name should contain only alphabetic characters.";
	public static final String FBE_0054 = "FBE0054:Last name should contain only alphabetic characters.";
	public static final String FBE_0055 = "FBE0055:Date of Birth should be in MM/DD/YYYY format.";
	public static final String FBE_0056 = "FBE0056:Date of Birth should be in the past.";
	public static final String FBE_0057 = "FBE0057:Contact Info cannot be null";
	public static final String FBE_0058 = "FBE0058:Invalid Email ID";
	public static final String FBE_0059 = "FBE0059:Payment details cannot be null.";
	public static final String FBE_0060 = "FBE0060:Invalid payment type. Allowed values are CC, CK, HOLD";
	public static final String FBE_0061 = "FBE0061:Credit card details are mandatory when the payment type is CC.";
	public static final String FBE_0062 = "FBE0062:Invalid credit card number.";
	public static final String FBE_0063 = "FBE0063:Invalid card type. Allowed values are VI, CA.";
	//public static final String FBE_0064 = "FBE0064:Credit card number must contain exactly sixteen digits";
	public static final String FBE_0065 = "FBE0065:cvv must contain exactly three digits.";
	public static final String FBE_0066 = "FBE0066:Expiry date should be in MM/YYYY format";
	public static final String FBE_0067 = "FBE0067:Expiry date should be in the future.";
	public static final String FBE_0068 = "FBE0068:City should contain only alphabetic characters.";
	public static final String FBE_0069 = "FBE0069:State should contain only alphabetic characters.";
	public static final String FBE_0070 = "FBE0070:Country should contain only alphabetic characters.";
	public static final String FBE_0071 = "FBE0071:ZipCode should contain only alphanumeric characters.";
	public static final String FBE_0072 = "FBE0072:Billing name should contain only alphabetic characters";
	public static final String FBE_0073 = "FBE0073:Billing address should contain only alphanumeric characters";
	public static final String FBE_0074 = "FBE0074:Duplicate Booking";
	public static final String FBE_0075 = "FBE0075:Booking Failed at Supplier";
	public static final String FBE_0076 = "FBE0076:Itinerary not available.";

	//Cancel PNR Error Codes
	public static final String FCE_0001 = "FCE_0001:PNR cannot be blank";
	public static final String FCE_0002 = "FCE_0002:Failed to parse the Supplier Response";
	public static final String FCE_0003 = "FCE_0003:Failed to get response from supplier.";
	public static final String FCE_0004 = "FCE_0004:Invalid referenceNumber.";
	public static final String FCE_0005 = "FCE_0005:ReferenceNumber cannot be blank";

	//Read PNR Error Codes
	public static final String FRE_0001 = "FRE_0001:PNR cannot be blank";
	public static final String FRE_0002 = "FRE_0002:Failed to parse the Supplier Response";
	public static final String FRE_0003 = "FRE_0003:Failed to get response from supplier.";
	public static final String FRE_0004 = "FRE_0004:";
	public static final String FRE_0005 = "FRE_0005:Invalid referenceNumber.";
	public static final String FRE_0006 = "FRE_0006:ReferenceNumber cannot be blank";

	//Ticket Order Error Codes
	public static final String OTE_0001 = "TOE_0001:PNR cannot be blank";
	public static final String OTE_0002 = "TOE_0002:Failed to parse the Supplier Response";
	public static final String OTE_0003 = "TOE_0003:Failed to get response from supplier.";
	public static final String OTE_0004 = "TOE_0004:";
	public static final String OTE_0005 = "TOE_0005:Invalid referenceNumber.";
	public static final String OTE_0006 = "TOE_0006:ReferenceNumber cannot be blank";

	//UserInfo Error Codes
	public static final String UIE_0001 = "UIE_0001:API key cannot be empty";
	public static final String UIE_0002 = "UIE_0002:Invalid User";
	public static final String UIE_0003 = "UIE_0003:Failed to parse the Supplier Response";
	public static final String UIE_0004 = "UIE_0004:User Info Not Found";

}
