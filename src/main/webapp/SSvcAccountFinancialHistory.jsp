<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>Financial History</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>
</head>
<jsp:useBean
  id="accountFinHist"
  scope="request"
  class="com.splwg.selfservice.AccountFinancialHistoryBean">
</jsp:useBean>
<jsp:useBean
  id="SelfService_validUser"
  scope="session"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<%--
--%>
<body class="wssBody">
<form name=Form1>
<%@include file="ErrorHandler.jsp" %>
<div id="Content" class="wssDivMain">
  <table class="wssTableMajor">
    <tr>
      <td class="wssTD100">
        <table class="wssTableMinor">
          <tr class="headerRow">
            <td class="label">Account Number:</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountFinHist" property="accountId" /></font></td>
          </tr>
          <tr class="headerRow">
            <td class="label">Customer Name:</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="SelfService_validUser" property="entityName" /></font></td>
          </tr>
        </table>
      </td>
    </tr>
    <% List list = accountFinHist.getTransactionList();
      if (list.size() > 0) { %>
    <tr>
      <td width=95%>
        <div id="Content2" class="wssDivSub">
          <table class="table#dataTable" width=98% border="1" cellpadding=0 cellspacing=0>
          <tr class='gridLabel'>
            <td class="gridTd" width=20%>Date</td>
            <td class="gridTd" width=44%>Transaction Type</td>
            <td class="gridTd" width=18%>Amount</td>
            <td class="gridTd" width=18%>Balance</td>
          </tr>
<%
String gridValue="gridAlt";
for (Iterator it = list.iterator(); it.hasNext();) {
    FinancialTransactionBean transaction = (FinancialTransactionBean) it.next();
    if (gridValue == "grid")
	gridValue = "gridAlt";
    else
	gridValue = "grid";

%>
          <tr class='<%= gridValue %>'>
            <td class="gridTd" align=left width=20%><%= transaction.getArrearsDate() %>&nbsp;</td>
            <td class="gridTd" align=left width=44%><%= transaction.getTransactionTypeDescription() %>&nbsp;</td>
            <td class="gridTd" align=right width=18%><%= transaction.getCurrentAmount() %>&nbsp;</td>
            <td class="gridTd" align=right width=18%><%= transaction.getCurrentBalance() %>&nbsp;</td>
          </tr>
<% } %>
          </table>
        </div>
      </td>
    </tr>
<% } else { %>
    <table width="100%">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td width="100%" align=center><font color=red><strong>No Financial History Exists</strong></font></td>
    </tr>
    </table>
<% } %>  </table>
</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Account Financial History</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("FinancialHistory", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
