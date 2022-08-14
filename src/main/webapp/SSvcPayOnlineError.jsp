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
  id="SelfService_validUser"
  scope="session"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<jsp:useBean
  id="newPayment"
  scope="request"
  class="com.splwg.selfservice.NewPaymentBean">
</jsp:useBean>
<%--
--%>
<body class="wssBody">
<form name="Form1">
<%@include file="ErrorHandler.jsp" %>
<div id="Content" class="wssDivMain">
  <div class="wssDivLeft">
    <table class="wssTableMajor">
      <tr>
        <td class="wssTD100">
          <table class="wssTableMinor">
            <tr class="headerRow">
              <td class="label">Account Number</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
  		<jsp:getProperty name="SelfService_validUser" property="accountId" /></font></td>
            </tr>
            <tr class="headerRow">
              <td class="label">Customer Name</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="SelfService_validUser" property="entityName" /></font></td>
            </tr>
            <tr class="headerRow">
              <td class="label">Payment Amount</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="newPayment" property="paymentAmountFormatted" /></font></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td width="100%">
          <table class=wssTableMinor>
            <tr>
              <td class=wssTDMidLeft>&nbsp;</td>
              <td class=wssTDMidLeft>&nbsp;</td>
            </tr>
            <tr>
              <td class=wssTDMidLeft>&nbsp;</td>
              <td class=wssTDMidLeft>&nbsp;</td>
            </tr>
            <tr>
              <td width="501" class=wssTDMidCenter colspan="2"><b><font class=wssFontArial3
                >The server had difficulties processing your request.</font></b></td>
            </tr>
            <tr>
              <td class=wssTDMidLeft>&nbsp;</td>
              <td class=wssTDMidLeft>&nbsp;</td>
            </tr>
            <tr>
              <td class=wssTDMidLeft>&nbsp;</td>
              <td class=wssTDMidLeft>&nbsp;</td>
            </tr>
            <tr>
              <td width="501" class=wssTDMidLeft colspan="2"><p align="center"><font class=wssFontArial2
                >Please go to the
              <a href="<%= response.encodeURL("FinancialHistory") %>"><b>Financial History</b></a>
              page to check whether your payment was processed. If it was not, please try again.</font></p></td>
            </tr>
            <tr>
              <td class=wssTDMidLeft>&nbsp;</td>
              <td class=wssTDMidLeft>&nbsp;</td>
            </tr>
            <tr>
              <td width="501" class=wssTDMidCenter colspan="2"><p align="center"><font class=wssFontArial2
                >If you continue having problems, please call Customer Service at 1-800-555-1212.</font></p></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>
  &nbsp;</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Payment Error</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("Pay", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
