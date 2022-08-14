<!--*
	* CMSSvcCustomerStatement.jsp
	*
	* Created on November 27, 2014, by Sherry Medina
	* This page will present the Customer Statement Page where the customer
	* can input the Billing History Start and End Month details.
	*
	* CHANGE HISTORY:
	*
	* Date:       by:    Reason:
	*
	* YYYY-MM-DD  IN     Reason text.
	* 2015-01-06  SMedin If a customer wants more than 2 years worth of
	*                    information they should not be able to obtain 
	*                    it themselves, but instead they need to ConEd.
	* 2015-01-21  SMedin If month value is equals to 08 or 09, parsed
	*                    integer value becomes 0. Updated code to avoid
	*                    this issue.
    * 2015-06-18  MCerip Security Fix.
	*-->
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head>
<title>Customer Statement</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>

</head>
<jsp:useBean
  id="customerStatement"
  scope="request"
  class="com.splwg.selfservice.CMSSvcCustomerStatementBean">
</jsp:useBean>
<%@ page import="com.splwg.selfservice.WebStringUtilities" %>
<script type="text/javascript" src="/SelfService/jcap/md5.js"></script>
<script type="text/javascript" src="/SelfService/jcap/jcap.js"></script>
<%--
--%>
<body class="wssBody">
<form name="Form1" name="Form1" action="<%= response.encodeURL("customerStatement?step=process") %>" target="_blank" method="post" accept-charset="utf-8">
<input type="hidden" name="_charset_" value="">
<script type="text/javascript">

function CheckInput() {

	var validFormat = /^\d{2}\/\d{4}$/;
	var showReport = "Y";
	var startDate = "";
	var startMonth = "";
	var startYear = "";
	var endDate = "";
	var endMonth = "";
	var endYear = "";	
 
	if (document.Form1.billingHistoryStart.value == "") {
		window.alert("Please enter the Billing History Start Month in MM/YYYY Format.");
		showReport = "N";
	} else {
		if (!validFormat.test(document.Form1.billingHistoryStart.value)) {
			window.alert("Please enter the Billing History Start Month in MM/YYYY Format.");
			showReport = "N";
		}
		
		startDate = document.Form1.billingHistoryStart.value.split('/');
		// 2015-01-21 - SMedin - Start Change
		if (startDate[0] == "08" || startDate[0] == "09") {
			startMonth = parseInt(startDate[0].replace("0",""));
		} else {
			startMonth = parseInt(startDate[0]);
		}
		//startMonth = parseInt(startDate[0]);
		// 2015-01-21 - SMedin - End Change
		startYear = parseInt(startDate[1]);
		
		if (startMonth > 12) { 
			window.alert("Invalid value for Billing History Start Month.");
			showReport = "N";
		} 
		
		// 2015-01-06 - SMedin - Start Change
		//if (startYear < 1902 || startYear > (new Date()).getFullYear()) { 
		if (startYear > (new Date()).getFullYear()) {
		// 2015-01-06 - SMedin - End Change		
			window.alert("Invalid value for Billing History Start Date. Customer Information for the given date is not yet available.");
			showReport = "N";
		} 
		
		// 2015-01-06 - SMedin - Start Add
		if (startYear == (new Date()).getFullYear() && startMonth > ((new Date()).getMonth()) + 1) {		
			window.alert("Invalid value for Billing History Start Date. Customer Information for the given date is not yet available.");
			showReport = "N";
		} 
		
		if (startYear < ((new Date()).getFullYear() - 2)) {
			window.alert("Invalid value for Billing History Start Date.\n\nTo obtain a Customer Statement that is older than 2 years, please contact us during normal business hours at (212) 780-8855.");
			showReport = "N";
		}
		
		if (startYear == ((new Date()).getFullYear() - 2) && startMonth < ((new Date()).getMonth()) + 1) { 
			window.alert("Invalid value for Billing History Start Date.\n\nTo obtain a Customer Statement that is older than 2 years, please contact us during normal business hours at (212) 780-8855.");
			showReport = "N";
		}
		// 2015-01-06 - SMedin - End Add
	}
	
	if (document.Form1.billingHistoryEnd.value == "") {
		window.alert("Please enter the Billing History End Month in MM/YYYY Format.");
		showReport = "N";
	} else {
		if (!validFormat.test(document.Form1.billingHistoryEnd.value)) {
			window.alert("Please enter the Billing History End Month in MM/YYYY Format.");
			showReport = "N";
		}
		
		endDate = document.Form1.billingHistoryEnd.value.split('/');
		// 2015-01-21 - SMedin - Start Change
		if (endDate[0] == "08" || endDate[0] == "09") {
			endMonth= parseInt(endDate[0].replace("0",""));
		} else {
			endMonth = parseInt(endDate[0]);
		}
		//endMonth = parseInt(endDate[0]);
		// 2015-01-21 - SMedin - End Change
		endYear = parseInt(endDate[1]);
		
		if (endMonth > 12) { 
			window.alert("Invalid value for Billing History End Month.");
			showReport = "N";
		} 
		
		// 2015-01-06 - SMedin - Start Change
		//if (endYear < 1902 || endYear > (new Date()).getFullYear()) {
		if (endYear > (new Date()).getFullYear()) {
		// 2015-01-06 - SMedin - End Change		
			window.alert("Invalid value for Billing History End Date. Customer Information for the given date is not yet available.");
			showReport = "N";
		} 
		
		// 2015-01-06 - SMedin - Start Add
		if (endYear == (new Date()).getFullYear() && endMonth > ((new Date()).getMonth()) + 1) {		
			window.alert("Invalid value for Billing History End Date. Customer Information for the given date is not yet available.");
			showReport = "N";
		} 
		
		if (endYear < ((new Date()).getFullYear() - 2)) {
			window.alert("Invalid value for Billing History End Date.\n\nTo obtain a Customer Statement that is older than 2 years, please contact us during normal business hours at (212) 780-8855.");
			showReport = "N";
		}
		
		if (endYear == ((new Date()).getFullYear() - 2) && endMonth < ((new Date()).getMonth()) + 1) { 
			window.alert("Invalid value for Billing History End Date.\n\nTo obtain a Customer Statement that is older than 2 years, please contact us during normal business hours at (212) 780-8855.");
			showReport = "N";
		}
		// 2015-01-06 - SMedin - End Add
	} 
	
	if (showReport == "Y" && document.Form1.billingHistoryStart.value != "" && document.Form1.billingHistoryEnd.value != "") {
		if (startYear > endYear) {
			window.alert("Billing History End Month " + document.Form1.billingHistoryEnd.value + " must be greater than Billing History Start Month " + document.Form1.billingHistoryStart.value + ".");
			showReport = "N";
		} else if (startYear == endYear && startMonth >= endMonth) {
			window.alert("Billing History End Month " + document.Form1.billingHistoryEnd.value + " must be greater than Billing History Start Month " + document.Form1.billingHistoryStart.value + ".");
			showReport = "N";
		}
	}
	
	if (showReport == "Y") {
		document.Form1.title = "Customer Statement";
		document.Form1.submit();
	}
}

</script>
 <%@include file="ErrorHandler.jsp" %>
<div id="Content" class="wssDivMain">
    <table class="wssTableMajor">
      <tr>
        <td class="wssTD100">
          <table class="wssTableMinor">
            <tr>
            </tr>
          </table>
        </td>
      </tr>
    <tr>
      <td width="500" class=wssTDTopLeft >
        <div class="wssDivReg" align="left">
          <table width="500" class=wssTable0>
            <tr>
              <td width="4" class=wssTDTopLeft><img border="0" src="/SelfService/graphics/Button_03.gif" width="4" height="5"></td>
              <td width="496" class=wssTDTopLeft>
                 <table width="100%" class=wssTable0>
                    <tr>
                      <td class=wssTDMidCenter4><font face="Arial" size="3" ></font></td>
                    </tr>
                    <tr colspan="4" align="center">
                      <td >&nbsp;</td>
                      <td >&nbsp;</td>
                      <td >&nbsp;</td>
                      <td >&nbsp;</td>
                    </tr>
                    <tr align="center">                      
                    <% String BillingHistoryStart = null;
					   BillingHistoryStart = request.getParameter("billingHistoryStart");
                       if (BillingHistoryStart == null) {
					       BillingHistoryStart = ""; 
					   }%>					  
                      <td class="label" align="right"> Billing History Start Month (MM/YYYY) </td>
                      <td align="left" colspan="3"><font class=wssFontArial2 >
                        <input name="billingHistoryStart" type="text" id="billingHistoryStart" size="10" maxlength="8" value="<%= WebStringUtilities.asHTML(BillingHistoryStart) %>">
                        </font></td>
                    </tr>                    
                    <tr align="center">
					<% String BillingHistoryEnd = null;
					   BillingHistoryEnd = request.getParameter("billingHistoryEnd");
                       if (BillingHistoryEnd == null) { 
					       BillingHistoryEnd = ""; 
					   } %>                      
                      <td class="label" align="right"> Billing History End Month (MM/YYYY) </td>
                      <td align="left" colspan="3"><font class=wssFontArial2 >
                        <input name="billingHistoryEnd" type="text" id="billingHistoryEnd" size="10" maxlength="8" value="<%= WebStringUtilities.asHTML(BillingHistoryEnd) %>">
                        </font></td>
                    </tr>                   
                    <tr colspan="4" align="center">
					    <td>&nbsp;</td>
					</tr>
                    <tr>
                        <td class="Captchalabel" align="right" valign="top">Enter word shown below&nbsp;</td>
                        <td width="25%" align="left" colspan="2" ><font class=wssFontArial2 >
                        <script type="text/javascript">sjcap();</script>
                        </td>
                        <td width="25%" align="center">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="100%" colspan="4" align="center">&nbsp;</td>
                    </tr>
                    <tr>
                      <td colspan=4 class=wssTDMidCenter4> <p align="center">
                          <input class="button" type="button" name="Button" value="Submit" onClick="if(jcap()){ CheckInput(); }else{return false;};">
                        </p></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                    <tr>
                  </table>
              </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
    <tr>
      <td width="750" class=wssTDTopLeft>
        <p align="right"><span style="layout-flow: vertical"></span></td>
    </tr>
  </table>
</div>
<%@ include file="CMIncTop.jsp" %>
<td class=wssTDMidCenter height="40" width="540"><b><font
 class=wssFontArial5>Customer Statement</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("CustomerStatement", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
