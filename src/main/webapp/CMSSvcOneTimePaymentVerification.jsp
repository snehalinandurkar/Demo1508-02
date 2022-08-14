<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.splwg.selfservice.*, java.util.*" %>

<%
	CMSSvcPaymentOptionsBean paymentOption = (CMSSvcPaymentOptionsBean) session.getAttribute("CMSSvcPaymentOptionsBean");
	session.setAttribute("CMSSvcPaymentOptionsBean", paymentOption);
%>

<html>
<head>
<title>One-Time Payment</title>
<%@include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>

<script type="text/javascript" language="javascript">
function confirmAutoPayEnroll(){
	document.verifyAutoPayEnrollForm.actionCode.value = "confirmOneTimePayment";
	document.verifyAutoPayEnrollForm.submit();
}
 
function ignoreAutoPayEnroll(){
    window.history.back() 
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
<form name="verifyAutoPayEnrollForm">
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
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2"> 
            				<tr>
        						<td class="wssTDMidLeft">
        							<b><br><font class="wssFontArial2">Confirm the information below is correct.</font><br></b>
        						</td>
            				</tr>              				
            				
            				<%
								String bankAcctNum = paymentOption.getBankAccountNumber();
								StringBuffer masked_bankAcctNum = new StringBuffer(bankAcctNum);
								if(bankAcctNum.length() > 7) {
									for (int i = 0; i < (bankAcctNum.length()-4); i++) {
										masked_bankAcctNum.replace(i,(bankAcctNum.length()-4),"X");
									}														
									
									masked_bankAcctNum.append(bankAcctNum.substring(bankAcctNum.length()-4, bankAcctNum.length()));
								} else if(bankAcctNum.length() == 7) {
									for (int i = 0; i < (bankAcctNum.length()-3); i++) {
										masked_bankAcctNum.replace(i,(bankAcctNum.length()-3),"X");
									}													
									
									masked_bankAcctNum.append(bankAcctNum.substring(bankAcctNum.length()-3, bankAcctNum.length()));
								} else {
									masked_bankAcctNum.replace(0,masked_bankAcctNum.length(), "XXXX");
								}
								
								// Start Add - 2016-09-28 - SMedina - CEd15
								String bankRoutingNum = paymentOption.getBankRoutingNumber();
								StringBuffer masked_bankRouteNum = new StringBuffer(bankRoutingNum);
								for (int i = 0; i < (bankRoutingNum.length()); i++) {
									masked_bankRouteNum.replace(i,(bankRoutingNum.length()),"X");
								}
								// End Add - 2016-09-28 - SMedina - CEd15
							%>            				           				
            				
            				<tr>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">Bank Name:</font></b>
								</td>
								
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">
									<%
										if (paymentOption.getAutoPaySourceName() != null) {
									%> 
										<jsp:getProperty name="CMSSvcPaymentOptionsBean" property="autoPaySourceName" /> 
									<%
 										}
 									%> 
 									</font></b>
 								</td>
							</tr>
            				
            				<tr>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">Bank Routing Number:</font></b>
								</td>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">
									    <!-- Start Modify - 2016-09-28 - SMedina - CEd15 -->
										<!-- <jsp:getProperty name="CMSSvcPaymentOptionsBean" property="bankRoutingNumber" /> -->
										<%=masked_bankRouteNum.toString()%>
										<!-- End Modify - 2016-09-28 - SMedina - CEd15 -->
									</font></b>
								</td>
							</tr>
            				
            				<tr>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">Bank Account Number:</font></b>
								</td>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">
										<%=masked_bankAcctNum.toString()%>
									</font></b>
								</td>
							</tr>
							
							<tr>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">Bank Account Type:</font></b>
								</td>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">
										<jsp:getProperty name="CMSSvcPaymentOptionsBean" property="bankAccountTypeDesc" /> 
									</font></b>
								</td>
							</tr>
            					<tr>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">Payment Amount:</font></b>
								</td>
								
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">
									<jsp:getProperty name="CMSSvcPaymentOptionsBean" property="paymentAmount"/>
 									</font></b>
 								</td>

							</tr>
							<tr>
    								<td colspan=2 align=left><b>
									<font class="wssFontArial2"></font></b>
								</td>
							</tr>
							<% System.out.println("ENROL[" + request.getParameter("enrolAutoPay") + "]");
							if (paymentOption.getCreateAutoPay()) { 
System.out.println("ENROL[" + request.getParameter("enrolAutoPay") + "]Check1[" + request.getParameter("Check1") + "]");

							%>
							<tr>
    								<td colspan=2 align=left><b>
									<font class="wssFontArial2">Enroll Bank Details in Direct Payment Plan</font></b>
								</td>
							</tr>
							<% } %>
						</table>
        				</td>
      			</tr>
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2"> 		            				
            				<tr>
            					<td colspan=4 align=right>
            						<input type="button" value="Previous" onclick="ignoreAutoPayEnroll();" class="button">
            						<input type="button" value="Continue" onclick="confirmAutoPayEnroll();" class="button">
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
<td class="wssTDMain"><b><font class="wssFontArial5">One-Time Payment</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("PaymentOptions", out, response); %>
<%@ include file="IncBottomPersonalInfo.jsp" %>
</body>
</html>