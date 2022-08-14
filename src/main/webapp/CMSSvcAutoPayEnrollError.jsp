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
</script>

</head>
<jsp:useBean id="SelfService_validUser" scope="session"
	class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<jsp:useBean id="CMSSvcPaymentOptionsBean" scope="session"
	class="com.splwg.selfservice.CMSSvcPaymentOptionsBean">
</jsp:useBean> 

<body class="wssBody">
<form name="errorAutoPayEnrollForm">
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
	      		<%
					if (CMSSvcPaymentOptionsBean.getIsAutoPayDateConflict()) {
				%>
      			<tr>
					<td width="100%">
      					<table class="wssTableMinor2"> 
            				<tr>
        						<td class="wssTDMidLeft">
        							<b><font class="wssFontArial3" color="red">
        								<br><br>
        								An error was encountered when processing your enrollment in the <br>
        								Direct Payment Plan. The error occured because you just recently <br>
        								requested to set up your Direct Payment Plan. If you wish to update <br> 
        								your Direct Payment Plan information, please call us at <br>
        								1-212-780-8855 and a representative will assist you.<br>
        							</font></b>
        						</td>
            				</tr>            							
	          			</table>
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
        							<b><font class="wssFontArial3" color="red">
        								<br><br>
        								An error was encountered when processing your enrollment in the <br>
        								Direct Payment Plan. To complete enrollment, please call us at <br>
        								1-212-780-8855 and a representative will assist you.<br>
        							</font></b>
        						</td>
            				</tr>            							
	          			</table>
	        		</td>
	      		</tr>
	      		<%
					}
				%>				
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