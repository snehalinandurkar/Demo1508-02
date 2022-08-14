<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.splwg.selfservice.*, java.util.*" %>

<html>
<head>
<title>Enroll in Direct Payment Plan</title>
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

function submitAutoPayEnrollForm() {
   	var reasonPB = "";
	reasonPB += routingNumber();
		
	if (reasonPB != "") { 	
    	document.getElementById('errorAutoPayEnroll').innerHTML = reasonPB;	
    	return false;
  	} else { 		
    	document.getElementById('errorAutoPayEnroll').innerHTML = '';	
    	document.initiateAutoPayEnrollForm.actionCode.value = "verifyAutoPayEnroll";	
		document.initiateAutoPayEnrollForm.submit();	
	}
}

function routingNumber() {
	var error = "";
	var routing = document.initiateAutoPayEnrollForm.bankRouteNumber.value;
	var accountNumber = document.initiateAutoPayEnrollForm.bankAccountNumber.value;
	var accountType = document.initiateAutoPayEnrollForm.bankAccountType.value;
	
	if (!validRequired(routing)) {	
		document.getElementById('routingNumber').className = 'errorAutoPayEnroll';		
		error = error + 'Bank Routing Number is required to start Direct Pay Plan.<br>' ;
	} else if (validRequired(routing) && routing.length !=9 ) {	
		document.getElementById('routingNumber').className = 'errorAutoPayEnroll';		
		error = error + 'The Bank Routing Number must contain 9 digits.<br>' ;
	} else {
		document.getElementById('routingNumber').className = 'textFont';			
	}
	
	if (!validRequired(accountNumber)) {	
		document.getElementById('accountNumber').className = 'errorAutoPayEnroll';		
		error = error + 'Bank Account Number is required to start Direct Pay Plan.<br>' ;
	} else if (validRequired(accountNumber) && accountNumber.length > 19) {	
		document.getElementById('accountNumber').className = 'errorAutoPayEnroll';		
		error = error + 'The Bank Account Number must contain 19 digits or less.<br>' ;
	} else {
		document.getElementById('accountNumber').className = 'textFont';			
	}
	
	if (!validRequired(accountType)) {	
		document.getElementById('accountType').className = 'errorAutoPayEnroll';		
		error = error + 'Bank Account Type is required to start Direct Pay Plan.<br>' ;
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
<%
	CMSSvcPaymentOptionsBean paymentOption = (CMSSvcPaymentOptionsBean) session.getAttribute("CMSSvcPaymentOptionsBean");
	session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);
%>
</head>
<jsp:useBean id="SelfService_validUser" scope="session"
	class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<jsp:useBean id="CMSSvcPaymentOptionsBean" scope="session"
	 class="com.splwg.selfservice.CMSSvcPaymentOptionsBean">
</jsp:useBean> 

<body class="wssBody">
<form name="initiateAutoPayEnrollForm">
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
										<div id="errorAutoPayEnroll" class="mainErrMsg"></div>										
									</font></b>
								</td>
							</tr>    					
      					</table>
        			</td>
      			</tr>
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2">             				             				
            				<tr>
								<td class="wssTDMidLeft" id="routingNumber">
									<b><font class="wssFontArial2">Bank Routing Number</font></b>
								</td>
								<% 
               						String BankRouteNumber = null;
									BankRouteNumber = request.getParameter("bankRouteNumber");
									if (BankRouteNumber == null) {
										BankRouteNumber = "";
									}
             					%>								
								<td class="wssTDMidJustify">
									<input type="text" name="bankRouteNumber" id="bankRouteNumber" class="inputTextNew" 
											size="40" maxlength="50" onkeyup="numericControl(this);"  value="<%= BankRouteNumber %>">
								</td>
            				</tr>
            				<tr>            			
								<td class="wssTDMidLeft" id="accountNumber">
									<b><font class="wssFontArial2">Bank Account Number</font></b>
								</td>
								<% 
              						String BankAcctNumber = null;
									BankAcctNumber = request.getParameter("bankAccountNumber");
									if (BankAcctNumber == null) {
										BankAcctNumber = "";
									}
            					%>
								<td class="wssTDMidJustify">
									<input type="text" name="bankAccountNumber" id="bankAccountNumber" class="inputTextNew" 
											size="40" maxlength="50" onkeyup="numericControl(this);" value="<%= BankAcctNumber %>">
								</td>
            				</tr>            				
            				<tr>
								<td class="wssTDMidLeft" id="accountType">
									<b><font class="wssFontArial2">Bank Account Type</font></b>
								</td>
								<% 
                					String BankAcctType = null;
									BankAcctType = request.getParameter("bankAccountType");
									if (BankAcctType == null) {
										BankAcctType = "";
									}
             					%>
             					
             					<td class="wssTDMidJustify">
             						<select class="inputTextNew" id="bankAccountType" name="bankAccountType">
									<% 
										if(!BankAcctType.equals("")) { 
									%>
										<option value="<%= BankAcctType %>">
											<% 
												if(BankAcctType.equals("APSV")) {
											%>	
													Savings 
											<% 	
												} else { 
											%>  
													Checking 
											<% 
												} 
											%>
										</option>
										<% 
											if(BankAcctType.equals("APSV")) {
										%>
											<option value="APCK">Savings</option>
										<% 
											} else if(BankAcctType.equals("APCK")) { 
										%>
											<option value="APSV">Checking</option>
									<% 	
											} 
										} else { 
									%>
										<option value=""></option>
										<option value="APSV">Savings</option>
										<option value="APCK">Checking</option>
									<% 
										} 
									%>
									</select>
								</td>
            				</tr>
            				<tr>         				
              					<td colspan=4 align=right>
            						<input type="button" value="Continue" onclick="submitAutoPayEnrollForm(); return false" class="button">
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
<td class="wssTDMain"><b><font class="wssFontArial5">Enroll in Direct Payment Plan</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("PaymentOptions", out, response); %>
<%@ include file="IncBottomPersonalInfo.jsp" %>
</body>
</html>