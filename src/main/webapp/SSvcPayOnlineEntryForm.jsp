<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Pay</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>

</head>
<jsp:useBean
  id="accountInfo"
  scope="request"
  class="com.splwg.selfservice.ControlCentralBean">
</jsp:useBean>
<jsp:useBean
  id="SelfService_validUser"
  scope="session"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<%--
--%>
<body class="wssBody">
<form name="Form1"
action="<%= response.encodeURL("Pay") %>"
method="post" accept-charset="utf-8">
<input type="hidden" name="_charset_" value="">
<%@include file="ErrorHandler.jsp" %>
<div id="Content" class="wssDivMain" >
  <div class="wssDivLeft">
    <table class="wssTableMajor">
      <tr>
        <td class="wssTD100">
          <table class="wssTableMinor">
            <tr class="headerRow">
              <td class="label">Account Number</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
  		<jsp:getProperty name="accountInfo" property="accountId" /></font></td>
            </tr>
            <tr  class="headerRow">
              <td class="label">Customer Name</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="entityName" /></font></td>
            </tr>
            <tr  class="headerRow">
              <td class="label">Current Balance</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="currentBalance" /></font></td>
            </tr>
          </table>
        </td>
      </tr>
  
      <tr>
        <td class="wssTD100">
          <table >
            <tr>
		<td width="35%" class="label">Amount You Are Paying</td>
		<%
		  String paymentAmount = request.getParameter("paymentAmount");
 		 if (paymentAmount == null)  //&& Float.parseFloat(accountInfo.getCurrentBalanceUnformatted()) > 0) {
			paymentAmount = accountInfo.getCurrentBalanceWithoutCurrency();
//         }  else if (paymentAmount == null) {
//            paymentAmount = "0";
//         }


		%>
		<td ><input type="text"
			name="paymentAmount" size="20" value="<%= paymentAmount %>"></td>
            </tr>
		            <tr>
		<td class="label">Type of Card</td>
		<%
          String cardType = request.getParameter("cardType");
		  if (cardType == null) cardType = "";
		%>
		<td class="wssTD319MidLeft42">
		<div class"wssDivSec">
		<table class="wssTableMinor100">
		<tr>
		<td width="7%"><input type="radio" name="cardType" value="VISA"
		<%= cardType.equals("VISA") ? "checked" : "" %>></td>
		<td width="43%"><b><font class="wssFontArial2">Visa</font></b></td>
		<td width="7%"><input type="radio" name="cardType" value="MC"
		<%= cardType.equals("MC") ? "checked" : "" %>></td>
		<td width="43%"><b><font class="wssFontArial2">MasterCard</font></b></td>
		</tr>
		<tr>
		<td width="7%"><input type="radio" name="cardType" value="Discover"
		<%= cardType.equals("Discover") ? "checked" : "" %>></td>
		<td width="43%"><b><font class="wssFontArial2">Discover</font></b></td>
		<td width="7%"><input type="radio" name="cardType" value="AmEx"
		<%= cardType.equals("AmEx") ? "checked" : "" %>></td>
		<td width="43%"><b><font class="wssFontArial2">American Express</font></b></td>
		</tr>
		</table>
		</div>
		</td>
            </tr>
            <tr>
		<td class="label">Credit Card Number</td>
		<%
		  String creditCardNumber = request.getParameter("creditCardNumber");
		  if (creditCardNumber == null) creditCardNumber = "";
		%>
		<td ><input type="text" 
			name="creditCardNumber" size="50" value="<%= creditCardNumber %>"></td>
            </tr>
            <tr>
		<td class="label">Expiration Date</td>
		<%
		  String expMonth = request.getParameter("expMonth");
		  if (expMonth == null) expMonth = "";
		  String expYear = request.getParameter("expYear");
		  if (expYear == null) expYear = "";
		%>
		        <td with="125"><b><font class="wssFontArial2">Month:&nbsp; 
                  <select name="expMonth">
                    <option value="XX"> 
                    <option value="01" <%= expMonth.equals("01") ? "selected" : "" %>>January 
                    <option value="02" <%= expMonth.equals("02") ? "selected" : "" %>>February 
                    <option value="03" <%= expMonth.equals("03") ? "selected" : "" %>>March 
                    <option value="04" <%= expMonth.equals("04") ? "selected" : "" %>>April 
                    <option value="05" <%= expMonth.equals("05") ? "selected" : "" %>>May 
                    <option value="06" <%= expMonth.equals("06") ? "selected" : "" %>>June 
                    <option value="07" <%= expMonth.equals("07") ? "selected" : "" %>>July 
                    <option value="08" <%= expMonth.equals("08") ? "selected" : "" %>>August 
                    <option value="09" <%= expMonth.equals("09") ? "selected" : "" %>>September 
                    <option value="10" <%= expMonth.equals("10") ? "selected" : "" %>>October 
                    <option value="11" <%= expMonth.equals("11") ? "selected" : "" %>>November 
                    <option value="12" <%= expMonth.equals("12") ? "selected" : "" %>>December 
                  </select>
                  Year:&nbsp; 
                  <select name="expYear">
                    <option value="XXXX"> 
                    <option value="2002" <%= expYear.equals("2002") ? "selected" : "" %>><b><font class="wssFontArial2">2002 
                    </font></b>
                    <option value="2003" <%= expYear.equals("2003") ? "selected" : "" %>><b><font class="wssFontArial2">2003 
                    </font></b>
                    <option value="2004" <%= expYear.equals("2004") ? "selected" : "" %>><b><font class="wssFontArial2">2004 
                    </font></b>
                    <option value="2005" <%= expYear.equals("2005") ? "selected" : "" %>><b><font class="wssFontArial2">2005 
                    </font></b>
                    <option value="2006" <%= expYear.equals("2006") ? "selected" : "" %>><b><font class="wssFontArial2">2006 
                    </font></b>
                    <option value="2007" <%= expYear.equals("2007") ? "selected" : "" %>><b><font class="wssFontArial2">2007 
                    </font></b>
                    <option value="2008" <%= expYear.equals("2008") ? "selected" : "" %>><b><font class="wssFontArial2">2008 
                    </font></b>
                    <option value="2009" <%= expYear.equals("2009") ? "selected" : "" %>><b><font class="wssFontArial2">2009 
                    </font></b>
                    <option value="2010" <%= expYear.equals("2010") ? "selected" : "" %>><b><font class="wssFontArial2">2010 
                    </font></b>
                  </select>
                  </font></b></td>    
        </tr>
        <tr>
		<td class="label">Name As Shown On Card</td>
		<%
		  String creditCardName = request.getParameter("creditCardName");
		  if (creditCardName == null) creditCardName = "";
		%>
		<td ><input type="text"
			name="creditCardName" size="50" value="<%= creditCardName %>"></td>
		            </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
		<td colspan=3>
		<p align="center"><b><font class="wssFontArial2"><input type="submit"
		value="Make Payment" class="button" ></font></b></td>
		            </tr>
		          </table>
		<input type="hidden" name="step" value="process">
        </td>
      </tr>
    </table>
   </form>
  </div></div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Payment Entry</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("Pay", out, response); %>
<%@ include file="IncBottom.jsp" %>
</body>
</html>
