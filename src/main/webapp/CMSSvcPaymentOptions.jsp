<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>

<%
	CMSSvcPaymentOptionsBean paymentOption = (CMSSvcPaymentOptionsBean) session.getAttribute("CMSSvcPaymentOptionsBean");
	session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);

	String autoPayEnrolled = null;
	if (paymentOption.getIsAutoPayEnrolled()) {
		autoPayEnrolled = "true";
	} else {
		autoPayEnrolled = "false";
	}
	session.setAttribute("autoPayEnrolled", autoPayEnrolled);
%>

<html>
<head>
<title>Payment Options</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>
</head>

<jsp:useBean id="SelfService_validUser" scope="session"
	class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>

<script type="text/javascript" language="javascript">
function initiateAutoPayEnroll(){
	document.paymentOptionsForm.actionCode.value = "initiateAutoPayEnroll";
	document.paymentOptionsForm.submit();
}

function initiateAutoPayUpdate(){
	document.paymentOptionsForm.actionCode.value = "initiateAutoPayUpdate";
	document.paymentOptionsForm.submit();
}

function initiateOneTimePayment(){
	if (<%=autoPayEnrolled%>) {
		var r=confirm("Your account is currently enrolled in Direct Payment.  Would you like to continue to make a one-time payment?");
		if (!r) {
			return;
		}
	}
	document.paymentOptionsForm.actionCode.value = "initiateOneTimePayment";
	document.paymentOptionsForm.submit();
}
</script>

<body class="wssBody">
<form name="paymentOptionsForm">
	<input type="hidden" name="_charset_" value="">
	<input type="hidden" name="actionCode" id="actionCode" />
	<%@include file="ErrorHandler.jsp" %>
	<div id="Content" class="wssDivMainPersonalInfo">
		<div class="wssDivLeft">
   			<table class="wssTableMajor">
   				<tr>
       				<td class="wssTD100">
       					<table class="wssTableMinor">
           					<tr  class="headerRow">
           						<td class="label">Account Number</td>
           						<td class="wssTDWhiteMidLeft">
           							<font class="wssFontArial2"><jsp:getProperty name="SelfService_validUser" property="accountId" /></font>
           						</td>
           					</tr>
           					<tr  class="headerRow">
           						<td class="label">Customer Name</td>
           						<td class="wssTDWhiteMidLeft">
           							<font class="wssFontArial2"><jsp:getProperty name="SelfService_validUser" property="entityName" /></font>
           						</td>
           					</tr>
       					</table>
       				</td>
   				</tr>
   				<%
					if (!paymentOption.getIsAutoPayEnrolled()) {
				%>
   				<tr>
       				<td width="100%">
   						<table class="wssTableMinor2">     
   							<tr>
								<td class="wssTDMidLeft">
									<font class="wssFontArial2">
										<b>
										<br><br><u>Direct Payment Plan</u> - With nothing to mail, no check to write, and no stamps to buy, <br> 
										Con Edison's Direct Payment Plan saves you time and money. It's easy, simple, and convenient.
										</b>										
										<br><br>
										Imagine, your Con Edison bill is paid every month and you never have to lift a finger. Eliminate the need <br> 
										to write checks and mail bills by enrolling in the Direct Payment Plan (DPP), the fast, convenient, and<br> 
										secure way for customers with checking or savings accounts to pay their Con Edison bills. Your payment<br>
										will be automatically deducted from your account 10 days after you receive your bill. More than 400,000<br>
										customers are now enrolled in DPP, and enjoy the ease and convenience it offers. You can enroll online<br>
										by providing your bank account information.
										<br><br>
										To enroll in Direct Payment you will need your routing and bank account numbers from checking account<br>
										or savings account. 
										<br> 
									</font>
								</td>
							</tr>
						</table>
					<td>
				</tr>
				<tr>
					<td colspan=4 align=right>	
						<input type="button" value="Enroll in Direct Payment Plan" class="button" onclick="initiateAutoPayEnroll();">
					</td>
				</tr>
				<%
					} else {
				%>
				<tr>
       				<td width="100%">
    					<table class="wssTableMinor2">     
    						<tr>
								<td class="wssTDMidLeft">
									<font class="wssFontArial2">
										<b>
										<br><br><u>Direct Payment Plan</u> - With nothing to mail, no check to write, and no stamps to buy, <br> 
										Con Edison's Direct Payment Plan saves you time and money. It's easy, simple, and convenient.
										</b>
										<br><br>
										Our Direct Payment Plan is a carefree easy way to pay your Con Edison bill for customers with checking<br>
										or savings accounts. Direct Payment eliminates the need for check writing and mailing. Your payment <br>
										will be automatically deducted from your bank account 10 days after you receive your bill. You can <br>
										update your plan by providing a new bank checking or savings account information.
										<br><br>
										To update your Direct Payment Plan information choose the link below. You will need your routing and <br>
										bank account numbers from checking account or savings account. If you have a bill current balance due, <br>
										call us at 212-780-8855.
										<br>
									</font>
								</td>
							</tr>
						</table>
					<td>
				</tr>
				<tr>
					<td colspan=4 align=right>	
						<input type="button" value="Update Direct Payment Plan" class="button" onclick="initiateAutoPayUpdate();">
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
									<font class="wssFontArial2">
										<b>
										<br><br><br>
										<u>One Time Payment</u> - Pay your bill online using your checking or savings account.
										</b>
										<br>
									</font>
								</td>
							</tr>
						</table>
					<td>
				</tr>
				<tr>
					<td colspan=4 align=right>	
						<input type="button" value="Make a One Time Payment" class="button" onclick="initiateOneTimePayment();">
					</td>
				</tr>		
			</table>
		</div>
    </div>
</form>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Payment Options</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("PaymentOptions", out, response); %>
<%@ include file="IncBottomPersonalInfo.jsp" %>
</body>
</html>
