<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.splwg.selfservice.*, java.util.*" %>

<html>
<head>
<title>Update Direct Payment Plan</title>
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
<form name="confirmAutoPayUpdateForm">
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
            				<tr class="headerRow">
             					<td class="label">Service Address:</td>
              					<td class="wssTDWhiteMidLeft">
              						<font class="wssFontArial2"><jsp:getProperty name="accountInfo" property="premiseInfo" /></font>
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
        									<br><br>Thank you for updating your Direct Payment Plan information. The changes will be <br>
        									effective upon your next billing cycle. If you have a current amount due, please pay  <br>
        									using one of our other payment options or call us at 1-212-780-8855.<br><br>
        									To continue to enjoy the convenience of the Direct Payment Plan, please tell us if your<br> 
        									bank account number changes.<br><br>
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
<td class="wssTDMain"><b><font class="wssFontArial5">Update Direct Payment Plan</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("PaymentOptions", out, response); %>
<%@ include file="IncBottomPersonalInfo.jsp" %>
</body>
</html>