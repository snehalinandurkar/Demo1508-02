<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>Satellite Plan</title>
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
  id="alPlan"
  scope="request"
  class="com.splwg.selfservice.SubscriberPlansAndResultsBean">
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
  		<jsp:getProperty name="alPlan" property="accountNumber" /></font></td>
            </tr>
              <td class="label">Account Number</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
  		<jsp:getProperty name="alPlan" property="cssAcctId" /></font></td>
            </tr>
            <tr  class="headerRow">
              <td class="label">Customer Name</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="alPlan" property="customerName" /></font></td>
            </tr>
		</table>
		<tr></tr>
	       <table class="wssTable650">
	          <tr class="grid">
	            <td class="label">&nbsp;Plan Type</td>
	            <td class="gridTd">
					<jsp:getProperty name="alPlan" property="planType" />&nbsp;</td>
	          </tr>
	          <tr class="grid">
	            <td class="label">&nbsp;Status</td>
	            <td class="gridTd">
					<jsp:getProperty name="alPlan" property="status" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;Create Date/Time</td>
	            <td class="gridTd">
					<jsp:getProperty name="alPlan" property="createDateTime" /></font>&nbsp;</td>
	          </tr>
	          <tr class="grid">
	            <td class="label">&nbsp;Effective Date</td>
	            <td class="gridTd">
					<jsp:getProperty name="alPlan" property="effectiveDate" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;Submission Date</td>
	            <td class="gridTd">
					<jsp:getProperty name="alPlan" property="submissionDate" />&nbsp;</td>
		    <td class="gridTd"> <%
			if ("ActiveComplete".contains(alPlan.getStatus())) {%>
				<a href="<%= response.encodeUrl("subscriberedit") %>?planId=<%= alPlan.getSubAllocationPlanId() %>&d=1">Duplicate Plan</a>
					<% }
			else if ("Pending".contains(alPlan.getStatus())) {%>
				<a href="<%= response.encodeUrl("subscriberedit") %>?planId=<%= alPlan.getSubAllocationPlanId() %>">Edit Plan</a>
					<% }
			%>&nbsp;</td>	
	          </tr>
	        </table>
	      </td>
	     </tr>
	    </table>
      </td>
    </tr>
    <tr>
      <td class="wssTD100">
        <table >
          <%
          List list = alPlan.getPlanList();
          int index;
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
            <td class="gridTd" width=20% align=left>Account&nbsp;ID</td>
            <td class="gridTd" width=20% align=left>Name</td>
            <td class="gridTd" width=30% align=left>Address</td>
            <td class="gridTd" width=10% align=left>Allocation&nbsp;%</td>
            <td class="gridTd" width=10% align=left>Status</td>
            <td class="gridTd" width=10% align=left>End&nbsp;Date</td>
          </tr>

            <%

			for (Iterator it = list.iterator(); it.hasNext();) {
			    SubscriberAllocationBean sa = (SubscriberAllocationBean) it.next();
		if (gridValue == "grid")
			gridValue = "gridAlt";
		else
			gridValue = "grid";
			%>
	          <tr class='<%= gridValue %>'>
	            <td width=20% class="gridTd"><%= Util.ShowField(sa.getAccountId()) %>&nbsp;</td>
	            <td width=20% class="gridTd"><%= Util.ShowField(sa.getName()) %>&nbsp;</td>
	            <td width=30% class="gridTd"><%= Util.ShowField(sa.getAddress()) %>&nbsp;</td>
	            <td width=10% class="gridTd"><%= Util.ShowField(sa.getPercent()) %>&nbsp;</td>
	            <td width=10% class="gridTd"><%= Util.ShowField(sa.getStatus()) %>&nbsp;</td>
	            <td width=10% class="gridTd"><%= Util.ShowField(sa.getEndDate()) %>&nbsp;</td>
	          </tr>
              <% } %>
          </table>
        </div>
      </td>
    </tr>
          <% } else { %>
              <table width="100%">
              <tr>
                <td width="100%" align=center><font color=red><strong>No Satellite Plans Exist </strong></font></td>
              </tr>
              </table>
          <% } %>
        </table>
      </td>
    </tr>
    <tr>
      <td class="wssTD100">
        <table >
          <%
          List list2 = alPlan.getResultList();
          String resurl="subscriberresult";
          if (list2.size() > 0) {
          %>
    <tr>
      <td width=95% colspan=8>
        <div id="Content2" class="wssDivSubAccount" >
          <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1" >
            <%
			String gridValue="gridAlt";
	    %>
          <tr class='gridLabel'>
            <td class="gridTd" width=30% align=left>Credit Period</td>
            <td class="gridTd" width=30% align=left>Statement Date</td>
            <td class="gridTd" width=20% align=left>Credit Amount</td>
            <td class="gridTd" width=20% align=left>Balance</td>
          </tr>

            <%

			for (Iterator it = list2.iterator(); it.hasNext();) {
			    AllocationResultBean ar = (AllocationResultBean) it.next();
		if (gridValue == "grid")
			gridValue = "gridAlt";
		else
			gridValue = "grid";
			%>
	          <tr class='<%= gridValue %>'>
	            <td width=30% class="gridTd"><a href="<%= response.encodeURL(resurl) %>?resultId=<%= ar.getResultId() %>"><%= ar.getPeriod() %></a>&nbsp;</td>
	            <td width=30% class="gridTd"><%= Util.ShowField(ar.getStatementDate()) %>&nbsp;</td>
	            <td width=20% class="gridTd"><%= Util.ShowField(ar.getCreditAmount()) %>&nbsp;</td>
	            <td width=20% class="gridTd"><%= Util.ShowField(ar.getBalance()) %>&nbsp;</td>
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
<td class="wssTDMain"><b><font class="wssFontArial5">Value Stack Satellite Plan</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayVDERMenu("", out, response); %>
<%@ include file="IncBottomAccount.jsp" %>
</form>
</body>
</html>
