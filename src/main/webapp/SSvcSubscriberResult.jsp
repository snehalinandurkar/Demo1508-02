<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>Subscriber Plan Result</title>
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
  id="results"
  scope="request"
  class="com.splwg.selfservice.SubscriberResultBean">
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
  		<jsp:getProperty name="results" property="cssAcctId" /></font></td>
            </tr>
            <tr  class="headerRow">
              <td class="label">Customer Name</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="entityName" /></font></td>
            </tr>
		</table>
<% if (!results.getCreditPeriod().equals("On-Demand")) { %>
		<tr><td>Value Stack Summary</td></tr>
	       <table class="wssTable650">
	          <tr class="grid">
	            <td class="label">&nbsp;Current Period</td>
	            <td class="gridTd">
					<jsp:getProperty name="results" property="creditPeriod" />&nbsp;</td>
	          </tr>
	          <tr class="grid">
	            <td class="label">&nbsp;Credits Earned</td>
	            <td class="gridTd">
					<jsp:getProperty name="results" property="creditsEarned" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;Credits Allocated</td>
	            <td class="gridTd">
					<jsp:getProperty name="results" property="creditsAllocated" /></font>&nbsp;</td>
	          </tr>
<% if (!results.getAllocationType().equals("PBSA")) { %>
	          <tr class="grid">
	            <td class="label">&nbsp;Host Bank Contribution</td>
	            <td class="gridTd">
					<jsp:getProperty name="results" property="hostBankContribution" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;Credits Banked</td>
	            <td class="gridTd">
					<jsp:getProperty name="results" property="creditsBanked" />&nbsp;</td>
	          </tr>
<% } %>
	        </table>
	      </td>
	     </tr>
<% if (!results.getAllocationType().equals("LBSA")) { %>
                <tr><td>Host Bank</td></tr>
               <table class="wssTable650">
                  <tr class="grid">
                    <td class="label">&nbsp;Previous Balance</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="results" property="previousBalance" />&nbsp;</td>
                  </tr>
                  <tr class="grid">
                    <td class="label">&nbsp;Contributions</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="results" property="contributions" />&nbsp;</td>
                  </tr>
                  <tr class="gridAlt">
                    <td class="label">&nbsp;Withdrawals</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="results" property="withdrawals" /></font>&nbsp;</td>
                  </tr>
                  <tr class="grid">
                    <td class="label">&nbsp;Ending Balance</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="results" property="endingBalance" />&nbsp;</td>
                  </tr>
                </table>
<% } %>
              </td>
             </tr>

	    </table>
      </td>
    </tr>
<% } %>
    <tr><td><a href="subscriberstatement?resultId=<jsp:getProperty name="results" property="subAllocationResultId" />" target="_blank"> View Statement </a> </td></tr>
    <tr>
      <td class="wssTD100">
        <table >
          <%
          List monthlyList = results.getMonthlyList();
          if (monthlyList.size() > 0) {
          %>
    <tr>
      <td width=95% colspan=8>
        <div id="Content2" class="wssDivSubResult" >
          <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1" >
            <%
			String gridValue="gridAlt";
	    %>
          <tr class='gridLabel'>
            <td class="gridTd"  align=left>Account ID</td>
            <td class="gridTd"  align=left>Subscriber Name</td>
            <td class="gridTd"  align=left>Status</td>
            <td class="gridTd"  align=left>Allocation %</td>
            <td class="gridTd"  align=left>Svc Class</td>
            <td class="gridTd"  align=left>Value Stack Credit</td>
            <td class="gridTd"  align=left>Energy Credit</td>
            <td class="gridTd"  align=left>Capacity Credit</td>
            <td class="gridTd"  align=left>Environmental Credit</td>
            <td class="gridTd"  align=left>DSRV Credit</td>
            <td class="gridTd"  align=left>LSRV Credit</td>
            <td class="gridTd"  align=left>MTC Credit</td>
            <td class="gridTd"  align=left>Community Credit</td>
            <td class="gridTd"  align=left>kWh</td>
            <td class="gridTd"  align=left>&nbsp;&nbsp;From&nbsp;Date&nbsp;&nbsp;</td>
            <td class="gridTd"  align=left>&nbsp;&nbsp;&nbsp;To&nbsp;Date&nbsp;&nbsp;&nbsp;</td>
            <td class="gridTd"  align=left>ConEd Bill</td>
            <td class="gridTd"  align=left>ESCO Charges</td>
            <td class="gridTd"  align=left>Actual Billed Dollars</td>
            <td class="gridTd"  align=left>Actual Billed kWh</td>
            <td class="gridTd"  align=left>Credit Applied to Account</td>
            <td class="gridTd"  align=left>Credit Applied&nbsp;Date</td>
            <td class="gridTd"  align=left>Previous Month's Bank</td>
            <td class="gridTd"  align=left>Monthly Bank Contribution</td>
            <td class="gridTd"  align=left>Bank Withdrawal</td>
            <td class="gridTd"  align=left>Banked Balance</td>
          </tr>

            <%

			for (Iterator it = monthlyList.iterator(); it.hasNext();) {
			    MonthlyAllocationBean ma = (MonthlyAllocationBean) it.next();
		if (gridValue == "grid")
			gridValue = "gridAlt";
		else
			gridValue = "grid";
			%>
	          <tr class='<%= gridValue %>'>
	            <td class="gridTd"><%= Util.ShowField(ma.getAccountId()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getSubscriberName()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getStatus()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getPercent()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getServiceClass()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getValueStackCredit()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getEnergyCredit()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getCapacityCredit()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getEnvironmentalCredit()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getDsrvCredit()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getLsrvCredit()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getMtcCredit()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getCommunityCredit()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getKwh()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getFromDate()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getToDate()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getConedBill()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getEscoCharges()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getBilledAmount()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getBilledKwh()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getCreditAppliedAmount()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getCreditAppliedDate()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getPreviousBalance()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getMonthlyBankContribution()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getBankWithdrawal()) %>&nbsp;</td>
	            <td class="gridTd"><%= Util.ShowField(ma.getBankedBalance()) %>&nbsp;</td>
	          </tr>
              <% } %>
          </table>
        </div>
      </td>
    </tr>
          <% } else { %>
              <table width="100%">
              <tr>
                <td width="100%" align=center><font color=red><strong>No Subscriber Monthly Allocations Exist </strong></font></td>
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
          List demandList = results.getDemandList();
          if (demandList.size() > 0) {
          %>
    <tr>
      <td width=95% colspan=8>
        <div id="Content2" class="wssDivSubAccount" >
          <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1" >
            <%
                        String gridValue="gridAlt";
            %>
          <tr class='gridLabel'>
            <td class="gridTd" width=30% align=left>Account ID</td>
            <td class="gridTd" width=30% align=left>Subscriber Name</td>
            <td class="gridTd" width=20% align=left>Allocation %</td>
            <td class="gridTd" width=20% align=left>Value Stack Credit</td>
          </tr>

            <%

                        for (Iterator it = demandList.iterator(); it.hasNext();) {
                            OnDemandAllocationBean od = (OnDemandAllocationBean) it.next();
                if (gridValue == "grid")
                        gridValue = "gridAlt";
                else
                        gridValue = "grid";
                        %>
                  <tr class='<%= gridValue %>'>
                    <td width=30% class="gridTd"><%= Util.ShowField(od.getAccountId()) %>&nbsp;</td>
                    <td width=30% class="gridTd"><%= Util.ShowField(od.getSubscriberName()) %>&nbsp;</td>
                    <td width=20% class="gridTd"><%= Util.ShowField(od.getPercent()) %>&nbsp;</td>
                    <td width=20% class="gridTd"><%= Util.ShowField(od.getCredit()) %>&nbsp;</td>
                  </tr>
              <% } %>
          </table>
        </div>
      </td>
    </tr>
          <% } else { %>
              <table width="100%">
              <tr>
                <td width="100%" align=center><font color=red><strong>No On Demand Allocations Exist </strong></font></td>
              </tr>
              </table>
          <% } %>
        </table>
      </td>
    </tr>



  </table>
</div>
<%@ include file="CMIncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Value Stack Satellite Plan Result</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayVDERMenu("", out, response); %>
<%@ include file="IncBottomAccount.jsp" %>
</form>
</body>
</html>
