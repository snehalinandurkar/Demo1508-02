<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>Billing History</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>
</head>
<jsp:useBean
  id="billHist"
  scope="request"
  class="com.splwg.selfservice.BillingHistoryBean">
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
            <td class="label">Account Number</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="SelfService_validUser" property="accountId" /></font></td>
          </tr>
          <tr class="headerRow">
            <td class="label">Customer Name</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="SelfService_validUser" property="entityName" /></font></td>
          </tr>
        </table>
      </td>
    </tr>
    <% List list = billHist.getBillingHistoryList();
      if (list.size() > 0) { %>
    <tr>
      <td width=95%>
        <div id="Content2" class="wssDivSub">
          <table class="table#dataTable" width=98% border="1" cellpadding=0 cellspacing=0>
          <tr class='gridLabel'>
            <td width=25% class="gridTd">Bill Date</td>
            <td width=25% class="gridTd">Bill Charge</td>
            <td width=25% class="gridTd">Balance</td>
          <% String allowed = ((Properties) application.getAttribute("properties")).getProperty("com.splwg.selfservice.ShowBill");
                      if (allowed.equals("true")) { %>
            <td width=25% class="gridTd">Bill Image</td>
             <% } %>
          </tr>
<%
String gridValue="gridAlt";
for (Iterator it = list.iterator(); it.hasNext();) {
    BillBean bill = (BillBean) it.next();

    if (gridValue == "grid")
	gridValue = "gridAlt";
    else
	gridValue = "grid";
%>
          <tr class='<%= gridValue %>'>
            <td class="gridTd" align=left width=25%><%= bill.getBillDate() %>&nbsp;</td>
            <td class="gridTd" align=right width=25%><%= bill.getTotalCurrentCharges() %>&nbsp;</td>
            <td class="gridTd" align=right width=25%><%= bill.getEndingBalance() %>&nbsp;</td>
            <% if (allowed.equals("true")) { %>
            <td class="gridTd" align=left width=25%><a
		href="<%= response.encodeURL("ViewBill") %>?billId=<%= bill.getBillId() %>" target="_blank">View Bill</a></td>
            <% } %>
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
      <td width="100%" align=center><font color=red><strong>No Billing History Exists </strong></font></td>
    </tr>
    </table>
<% } %>
  </table>
</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Billing History</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("BillingHistory", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
