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
<body topmargin="0" leftmargin="0" link="#002B5D" vlink="#002B5D" alink="#FF9900">
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
              >Please Wait while your payment is being Processed.</font></b></td>
            </tr>
	  </table>
        </td>
      </tr>
    </table>
  </div>
  &nbsp;</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Payment</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("Pay", out, response); %>
<%@ include file="IncBottom.jsp" %>

</body>
</html>
