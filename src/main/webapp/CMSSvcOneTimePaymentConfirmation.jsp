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
</script>

</head>
<jsp:useBean id="accountInfo" scope="request"
  class="com.splwg.selfservice.ControlCentralBean">
</jsp:useBean>
<jsp:useBean id="SelfService_validUser" scope="session"
	class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<jsp:useBean id="CMSSvcPaymentOptionsBean" scope="session"
	 class="com.splwg.selfservice.CMSSvcPaymentOptionsBean">
</jsp:useBean> 

<body class="wssBody">
<form name="confirmAutoPayEnrollForm">
	<input type="hidden" name="_charset_" value="">
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
        								<b><font class="wssFontArial2">
        									<br><br>Thank you for using One-Time Payment. Your payment will be processed  <br>
        									and posted to your account after clearing.   <br><br>
        									Please call us at 1-212-780-8855 for issues and concerns regarding your payment.<br><br>
        								</font></b>
        							</td>
            					</tr>              				
            				</table>
					</td>
      			</tr> 
				<% System.out.println("ENROL[" + request.getParameter("enrolAutoPay") + "]");
				if (paymentOption.getCreateAutoPay()) { %>
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2">   				          				
            					<tr>
								<td class="wssTDMidLeft">
									<b><font class="wssFontArial2">
										Enrolled Account Number: <jsp:getProperty name="SelfService_validUser" property="accountId" />
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
        						<td class="wssTDMidLeft">
        							<b><font class="wssFontArial2">
        								<br>To continue to enjoy the convenience of the Direct Payment Plan, please tell us if your <br> 
        								bank account number changes.<br><br>
        							</font></b>
        						</td>
            				</tr>
						</table>
					</td>
      			</tr> 
				<% } %>
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2"> 							
							<tr>
        						<td class="wssTDMidLeft">
        							<b><font class="wssFontArial2">
        								<br><br>Customer Operations<br>Consolidated Edison Company of New York<br><br>
        							</font></b>
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