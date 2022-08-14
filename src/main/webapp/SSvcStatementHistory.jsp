<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>Statement History</title>
<%@include file="IncPreamble.jsp" %>
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
  id="history"
  scope="request"
  class="com.splwg.selfservice.StatementHistoryBean">
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
<div id="Content" class="wssDivMainLong">
  <table class="wssTableLarge">
    <tr>
      <td class="wssTD100">
        <table class="wssTableMinor">
	     <tr>
	      <td class="wssTD100">
	        <table class="wssTableMinor" >
            <tr class="headerRow">
              <td class="label">Web Self Service ID</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
  		<jsp:getProperty name="accountInfo" property="accountId" /></font></td>
            </tr>
            <tr class="headerRow">
              <td class="label">Account Number</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
  		<jsp:getProperty name="history" property="cssAcctId" /></font></td>
            </tr>
            <tr  class="headerRow">
              <td class="label">Customer Name</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="entityName" /></font></td>
            </tr>
		</table>

		<tr></tr>

	      </td>
	     </tr>
	    </table>
      </td>
    </tr>
    <tr>
      <td class="wssTD100">
        <table >
          <%
          List list = history.getResultList();
          String planurl="subscriberplan";
          String hosturl="allocationinformation";
          String resurl="subscriberresult";
          if (list.size() > 0) {
          %>
    <tr>
      <td width=95% colspan=8>
        <div id="Content2" class="wssDivSubAllocation" >
          <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1" >
            <%
			String gridValue="gridAlt";
	    %>
          <tr class='gridLabel'>
            <td class="gridTd" width=30% align=left>Credit Period</td>
            <td class="gridTd" width=20% align=left>Statement Date</td>
            <td class="gridTd" width=20% align=left>Credit Amount</td>
            <td class="gridTd" width=10% align=left>Balance</td>
            <td class="gridTd" width=10% align=left>Subscriber Plan</td>
            <td class="gridTd" width=10% align=left>Host Allocation</td>
          </tr>

            <%

			for (Iterator it = list.iterator(); it.hasNext();) {
			    AllocationResultBean ar = (AllocationResultBean) it.next();
		if (gridValue == "grid")
			gridValue = "gridAlt";
		else
			gridValue = "grid";
			%>
	          <tr class='<%= gridValue %>'>
	            <td width=30% class="gridTd"><a href="<%= response.encodeURL(resurl) %>?resultId=<%= ar.getResultId() %>"><%= ar.getPeriod() %></a>&nbsp;</td>
	            <td width=20% class="gridTd"><%= Util.ShowField(ar.getStatementDate()) %>&nbsp;</td>
	            <td width=20% class="gridTd"><%= Util.ShowField(ar.getCreditAmount()) %>&nbsp;</td>
	            <td width=10% class="gridTd"><%= Util.ShowField(ar.getBalance()) %>&nbsp;</td>
	            <td width=10% class="gridTd"><a href="<%= response.encodeURL(planurl) %>?planId=<%= ar.getSubscriberPlanId() %>">View Plan</a>&nbsp;</td>
	            <td width=10% class="gridTd"><a href="<%= response.encodeURL(hosturl) %>?allocationId=<%= ar.getHostAllocationId() %>">View Allocation</a>&nbsp;</td>
	          </tr>
              <% } %>
          </table>
        </div>
      </td>
    </tr>
          <% } else { %>
              <table width="100%">
              <tr>
                <td width="100%" align=center><font color=red><strong>No Allocation Results Exist </strong></font></td>
              </tr>
              </table>
          <% } %>
        </table>
      </td>
    </tr>
  </table>
</div>
<%@ include file="CMIncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Value Stack Statement History</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayVDERMenu("StatementHistory", out, response); %>
<%@ include file="IncBottomAccount.jsp" %>
</form>
</body>
</html>
