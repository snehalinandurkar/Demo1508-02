<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>Account</title>
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
  id="lastBill"
  scope="request"
  class="com.splwg.selfservice.BillBean">
</jsp:useBean>
<jsp:useBean
  id="saList"
  scope="request"
  class="com.splwg.selfservice.SAforAccountBean">
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
              <td class="label">Account Number</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
  		<jsp:getProperty name="accountInfo" property="accountId" /></font></td>
            </tr>
            <tr  class="headerRow">
              <td class="label">Customer Name</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="entityName" /></font></td>
            </tr>
		</table>
		<tr></tr>
	       <table class="wssTable650">
	          <tr class="grid">
	            <td class="label">&nbsp;Service Address</td>
	            <td class="gridTd">
					<jsp:getProperty name="accountInfo" property="premiseInfo" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt" >
	            <td class="label">&nbsp;Last Payment</td>
	            <td class="gridTd">
					<jsp:getProperty name="accountInfo" property="lastPayment" />&nbsp;</td>
	          </tr>
	          <tr class="grid">
	            <td class="label">&nbsp;Last Bill</td>
	            <td class="gridTd">
                <% if (!accountInfo.getLastBill().equals("")) { %>
					<jsp:getProperty name="accountInfo" property="lastBill" />, due
					<jsp:getProperty name="lastBill" property="dueDate" />
					<% String allowed = ((Properties) application.getAttribute("properties")).getProperty("com.splwg.selfservice.ShowBill");
                      if (allowed.equals("true")) {
                    %>
                    <a href="<%= response.encodeURL("ViewBill") %>?billId=<%= lastBill.getBillId() %>"
					target="_blank"><font size="1">View Bill</font></a>
                    <% } %></font>
                  <% } %>&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;Next Bill Date</td>
	            <td class="gridTd">
					<jsp:getProperty name="accountInfo" property="nextBillDate" />&nbsp;</td>
	          </tr>
	          <tr class="grid">
	            <td class="label">&nbsp;Current Balance</td>
	            <td class="gridTd">
					<jsp:getProperty name="accountInfo" property="currentBalance" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;<%= SelfService_validUser.getHomePhoneLabel() %></td>
	            <td class="gridTd">
					<jsp:getProperty name="accountInfo" property="homePhone" /></font>&nbsp;</td>
	          </tr>
	          <tr class="grid">
	            <td class="label">&nbsp;<%= SelfService_validUser.getBusinessPhoneLabel() %></td>
	            <td class="gridTd">
					<jsp:getProperty name="accountInfo" property="businessPhone" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;E-Mail</td>
	            <td class="gridTd">
					<jsp:getProperty name="accountInfo" property="email" />&nbsp;</td>
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
          List list = saList.getSAList();
          int index;
          String serviceDesc;
          String address;
          if (list.size() > 0) {
          %>
    <tr>
      <td width=95% colspan=8>
        <div id="Content2" class="wssDivSubAccount" >
          <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1" >
            <%
			String gridValue="gridAlt";
	    %>
          <tr class='gridLabel'>
            <td class="gridTd" width=19% align=left>Service</td>
            <td class="gridTd" width=8% align=left>Tariff</td>
            <td class="gridTd" width=6% align=left>Status</td>
            <td class="gridTd" width=10% align=left>Start Date</td>
            <td class="gridTd" width=10% align=left>End Date  </td>
            <td class="gridTd" width=10% align=left>Balance</td>
            <td class="gridTd" width=20% align=left>Address</td>
            <td class="gridTd" width=10% align=left>&nbsp;</td>
          </tr>

            <%

			for (Iterator it = list.iterator(); it.hasNext();) {
			    ServiceAgreementBean sa = (ServiceAgreementBean) it.next();
                  serviceDesc = sa.getServiceDesc();
                  index = serviceDesc.indexOf("/");
                  if (index > -1) {
                     serviceDesc = serviceDesc.substring(index + 2);
                  }
                  address = sa.getAddress();
                  index = address.indexOf("-");
                  if (index > -1) {
                    address = address.substring(0, index - 1);
                  }
		if (gridValue == "grid")
			gridValue = "gridAlt";
		else
			gridValue = "grid";
			%>
	          <tr class='<%= gridValue %>'>
	            <td width=19% class="gridTd"><%= Util.ShowField(serviceDesc) %>&nbsp;</td>
	            <td width=8% class="gridTd"><%= Util.ShowField(sa.getSAType()) %>&nbsp;</td>
	            <td width=6% class="gridTd"><%= Util.ShowField(sa.getStatus()) %>&nbsp;</td>
	            <td width=10% class="gridTd"><%= Util.ShowField(sa.getStartDate()) %>&nbsp;</td>
	            <td width=8% class="gridTd"><%= Util.ShowField(sa.getEndDate()) %>&nbsp;</td>
	            <td width=10% class="gridTd" align=right><%= sa.getSABalance() %>&nbsp;</td>
                <% if (!sa.getAddress().equals("")) { %>
	            <td width=20% class="gridTd"><%= Util.ShowField(address) %>&nbsp;</td>
	            <% if (sa.getActiveStatus().equals("true")) { %>
                <td  width=10% class="gridTd">
	            </td>
				</td>
    			<% } else { %>
                <td class="gridTd">&nbsp;</td>
                <%  }
                  } else {  %>
	            <td class="gridTd">&nbsp;&nbsp;</td>
	            <td class="gridTd">&nbsp;&nbsp;</td>
				</td>
    			<% } %>
	          </tr>
              <% } %>
          </table>
        </div>
      </td>
    </tr>
          <% } else { %>
              <table width="100%">
              <tr>
                <td width="100%" align=center><font color=red><strong>No Service Agreements Exist </strong></font></td>
              </tr>
              </table>
          <% } %>
        </table>
      </td>
    </tr>
  </table>
</div>
<%@ include file="CMIncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Account Information</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("Account", out, response); %>
<%@ include file="IncBottomAccount.jsp" %>
</form>
</body>
</html>
