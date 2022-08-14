<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>My Accounts</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>
</head>
<jsp:useBean
  id="accountPerson"
  scope="request"
  class="com.splwg.selfservice.AccountsForPersonBean">
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
          <tr>
            <td class="label" align=center>Please Select An Account</td>
          </tr>
        </table>
      </td>
    </tr>
    
    <tr>
      <td align=center>
        <table class="table#dataTable">
          <tr class="gridLabel">
            <td class="gridTd">Account Number</td>
            <td class="gridTd">Account Info</td>
            <td class"gridTd">Balance</td>
          </tr>
<%
List list = accountPerson.getAccountList();
String gridValue="gridAlt";
String urlValue="account";
for (Iterator it = list.iterator(); it.hasNext();) {
    AccountPersonBean acc = (AccountPersonBean) it.next();
    if (gridValue == "grid")
	gridValue = "gridAlt";
    else
	gridValue = "grid";

    if (acc.isVDER())
        urlValue = "allocationhistory";
    else
        urlValue = "account";

%>
          <tr class="<%= gridValue %>">
            <td width=25% class=gridTd><a href="<%= response.encodeURL(urlValue) %>?accountId=<%= acc.getAccountId() %>"><%= acc.getAccountId() %></a>&nbsp;</td>
            <td width=50% class=gridTd><%= acc.getAccountInfo() %>&nbsp;</td>
            <td width=25% class=gridTd align=right><%= acc.getCurrentBalance() %>&nbsp;</td>
          </tr>
<% } %>
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  &nbsp;</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">My Accounts</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayReducedMenu("MyAccounts", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
