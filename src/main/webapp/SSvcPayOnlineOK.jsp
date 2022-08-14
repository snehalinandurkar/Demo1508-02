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
              <td class=wssTDMidCenter>&nbsp;</td>
              <td class=wssTDMidCenter>&nbsp;</td>
            </tr>
            <tr>
            <td width="501" class=wssTDMidCenter colspan="2"><b><font class=wssFontArial3
              >Thank You!&nbsp;&nbsp; Your payment has been processed.</font></b></td>
            </tr>
	  </table>
          <table class=wssTableMinor border="1" cellpaddin="0" cellspacing="0"> 
            
            <tr>
              <td width=40% class="label" align="left">Transaction Reference Number</td>
              <td class=wssTDMidLeftF><font class=wssFontArial2>
                <jsp:getProperty name="newPayment" property="paymentEventId" /></font></td>
            </tr>
            <tr>
              <td width=40%  class="label" align="left">Payment Date</td>
              <td class=wssTDMidLeftF><font class=wssFontArial2>
                <jsp:getProperty name="newPayment" property="paymentDateFormatted" /></font></td>
            </tr>
            <tr>
              <td width=40% class="label" align="left">New Balance</td>
              <td class=wssTDMidLeftF><font class=wssFontArial2>
                <jsp:getProperty name="newPayment" property="accountBalanceAfterPayment" /></font></td>
            </tr>
            
            <tr>
              <td width="501" class=wssTDMidLeft colspan="2"><p align="center"><font class="wssFontArial2">
               Please print this page &mdash; this is your receipt.</p></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>
  &nbsp;</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Payment Confirmation</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("Pay", out, response); %>
<%@ include file="IncBottom.jsp" %>
</body>
</html>
