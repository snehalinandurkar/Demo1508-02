<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.splwg.selfservice.*, java.util.*" %>

<%
	CMSSvcPaymentOptionsBean paymentOption = (CMSSvcPaymentOptionsBean) session.getAttribute("CMSSvcPaymentOptionsBean");
	session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);
%>

<html>
<head>
<title>Update Direct Payment Plan</title>
<%@include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>

<script type="text/javascript" language="javascript">
function validRequired(field) {
	if (field == "") {
    	return false;
    }
    return true;
}

function submitAutoPayUpdateForm() {
   	var reasonPB = "";
	reasonPB += routingNumber();
		
	if (reasonPB != "") { 	
    	document.getElementById('errorAutoPayUpdate').innerHTML = reasonPB;	
    	return false;
  	} else { 		
    	document.getElementById('errorAutoPayUpdate').innerHTML = '';	
    	document.initiateAutoPayUpdateForm.actionCode.value = "verifyAutoPayUpdate";	
		document.initiateAutoPayUpdateForm.submit();	
	}
}

function routingNumber() {
	var error = "";
	var routing = document.initiateAutoPayUpdateForm.bankRouteNumber.value;
	var accountNumber = document.initiateAutoPayUpdateForm.bankAccountNumber.value;
	var accountType = document.initiateAutoPayUpdateForm.bankAccountType.value;
	
	if (!validRequired(routing)) {	
		document.getElementById('routingNumber').className = 'errorAutoPayUpdate';		
		error = error + 'Bank Routing Number is required to update Direct Pay Plan.<br>' ;
	} else if (validRequired(routing) && routing.length !=9 ) {	
		document.getElementById('routingNumber').className = 'errorAutoPayUpdate';		
		error = error + 'The Bank Routing Number must contain 9 digits.<br>' ;
	} else {
		document.getElementById('routingNumber').className = 'textFont';			
	}
	
	if (!validRequired(accountNumber)) {	
		document.getElementById('accountNumber').className = 'errorAutoPayUpdate';		
		error = error + 'Bank Account Number is required to update Direct Pay Plan.<br>' ;
	} else if (validRequired(accountNumber) && accountNumber.length > 19) {	
		document.getElementById('accountNumber').className = 'errorAutoPayUpdate';		
		error = error + 'The Bank Account Number must contain 19 digits or less.<br>' ;
	} else {
		document.getElementById('accountNumber').className = 'textFont';			
	}
	
	if (!validRequired(accountType)) {	
		document.getElementById('accountType').className = 'errorAutoPayUpdate';		
		error = error + 'Bank Account Type is required to update Direct Pay Plan.<br>' ;
	} else {
		document.getElementById('accountType').className = 'textFont';			
	}
	
	return error; 
} 

function numericControl (inputNum) {
    var msg = inputNum.value;
    var chr;

    for (i = 0; i < msg.length; i++) {
		chr = msg.substring(i, i + 1);
		if (chr < "0" || chr > "9") {
			msg = msg.substring(0, i);
			inputNum.value = msg;
      	}
    }
}
</script>

</head>
<jsp:useBean id="SelfService_validUser" scope="session"
	class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<jsp:useBean id="CMSSvcPaymentOptionsBean" scope="session"
	 class="com.splwg.selfservice.CMSSvcPaymentOptionsBean">
</jsp:useBean> 

<body class="wssBody">
<form name="initiateAutoPayUpdateForm">
	<input type="hidden" name="_charset_" value="">
	<input type="hidden" name="actionCode" id="actionCode" />
	<%@include file="ErrorHandler.jsp" %>
	<div id="Content" class="wssDivMainPersonalInfo">
  		<div class="wssDivLeft">
    		<table class="wssTableMajor">
      			<tr>
        			<td class="wssTD100">
         					<table class="wssTableMinor">
           					<tr class="headerRow">
             					<td class="label">Account Number</td>
            					<td class="wssTDWhiteMidLeft">
             						<font class="wssFontArial2"><jsp:getProperty name="SelfService_validUser" property="accountId" /></font>
								</td>
            				</tr>
            				<tr class="headerRow">
             					<td class="label">Customer Name</td>
              					<td class="wssTDWhiteMidLeft">
              						<font class="wssFontArial2"><jsp:getProperty name="SelfService_validUser" property="entityName" /></font>
              					</td>
            				</tr>
          				</table>
        			</td>
      			</tr>
      			<%
					if (paymentOption.getIsInvalidBankRouteNumber()) {
				%>
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2"> 
            				<tr>
        						<td class="wssTDMidLeft">
									<b><font class="wssFontArial3" color="red">	
        								 The bank routing number entered was not found in our system. Please try <br>
        								 again or call us at 1-212-780-8855 and a representative will assist you.<br>
        							</font></b>
        						</td>
            				</tr>            							
	          			</table>
	        		</td>
	      		</tr>
	      		<%
					}
				%>
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2">
      						<tr>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial3" color="red">										
										<div id="errorAutoPayUpdate" class="mainErrMsg"></div>										
									</font></b>
								</td>
							</tr>    					
      					</table>
        			</td>
      			</tr>
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2"> 
      						<%
								String backRouteNum = paymentOption.getBankRoutingNumber();
								if (backRouteNum == null) {
									backRouteNum = "";
								}
								
								// Start Add - 2016-09-29 - SMedina - CEd15
								else {
									StringBuffer masked_bankRouteNum = new StringBuffer(backRouteNum);
									for (int i = 0; i < (backRouteNum.length()); i++) {
										masked_bankRouteNum.replace(i,(backRouteNum.length()),"X");
									}
									
									backRouteNum = masked_bankRouteNum.toString();
								}
								// End Add - 2016-09-29 - SMedina - CEd15
							%>							           				             				
            				<tr>            						
								<td class="wssTDMidLeft" id="routingNumber">
									<b><font class="wssFontArial2">Bank Routing Number</font></b>
								</td>
								<td>
									<input type="text" name="bankRouteNumber" id="bankRouteNumber" class="inputTextNew" size="40"
											onkeyup="numericControl(this);" maxlength="50" value="<%= backRouteNum %>" /> 
								</td>
							</tr>
							<%
								String bankAcctNum = paymentOption.getBankAccountNumber();
								if (bankAcctNum == null) {
									bankAcctNum = "";
								} 
								
								// Start Add - 2016-09-29 - SMedina - CEd15
								else {
									StringBuffer masked_bankAcctNum = new StringBuffer(bankAcctNum);
									if(bankAcctNum.length() > 7) {
										for (int i = 0; i < (bankAcctNum.length()-4); i++) {
											masked_bankAcctNum.replace(i,(bankAcctNum.length()-4),"X");
										}														
									
										masked_bankAcctNum.append(bankAcctNum.substring(bankAcctNum.length()-4, bankAcctNum.length()));
									} 
									else if(bankAcctNum.length() == 7) {
										for (int i = 0; i < (bankAcctNum.length()-3); i++) {
											masked_bankAcctNum.replace(i,(bankAcctNum.length()-3),"X");
										}													
									
										masked_bankAcctNum.append(bankAcctNum.substring(bankAcctNum.length()-3, bankAcctNum.length()));
									} 
									else {
										masked_bankAcctNum.replace(0,masked_bankAcctNum.length(), "XXXX");
									}
									
									bankAcctNum = masked_bankAcctNum.toString();
								}
								// End Add - 2016-09-29 - SMedina - CEd15
							%>
            				<tr id="bankAccountNumberRow" style="display:;">
								<td class="wssTDMidLeft" id="accountNumber">
									<b><font class="wssFontArial2">Bank Account Number</font></b>
								</td>
								<td class="wssTDMidJustify">
									<input type="text" name="bankAccountNumber"	id="bankAccountNumber" class="inputTextNew" size="40" 
											onkeyup="numericControl(this);"	maxlength="20" value="<%= bankAcctNum %>" /> 
								</td>
							</tr>
							<%
								String bankAcctType = paymentOption.getBankAccountTypeCode();
								if (bankAcctType == null) {
									bankAcctType = "";
								}
							%>					           				
            				<tr>
								<td class="wssTDMidLeft" id="accountType">
									<b><font class="wssFontArial2">Bank Account Type</font></b>
								</td>								           					
             					<td class="wssTDMidJustify">
             						<select class="inputTextNew" id="bankAccountType" name="bankAccountType">
									<% 
										if (bankAcctType != null && !bankAcctType.equals("")) {
											if (bankAcctType.equals("APCK")) { 
									%>
										<option value="APCK">Checking</option>
										<option value="APSV">Savings</option>
									<%
											} else {
									%>
										<option value="APSV">Savings</option>
										<option value="APCK">Checking</option>
									<%
											}
										}
									%>									
									</select>
								</td>
            				</tr>
            				<tr>         				
              					<td colspan=4 align=right>
            						<input type="button" value="Continue" onclick="submitAutoPayUpdateForm(); return false" class="button">
            					</td>
            				</tr>
	          			</table>
	        		</td>
	      		</tr>
	    	</table>
	  	</div>
	</div>
</form>	  	
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Update Direct Payment Plan</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("PaymentOptions", out, response); %>
<%@ include file="IncBottomPersonalInfo.jsp" %>
</body>
</html>