<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>Allocation History</title>
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
  id="alList"
  scope="request"
  class="com.splwg.selfservice.AccountHostAllocationsBean">
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
              <td class="label">Web Self Service Id</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
  		<jsp:getProperty name="accountInfo" property="accountId" /></font></td>
            </tr>
            <tr class="headerRow">
              <td class="label">Account Number</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
  		<jsp:getProperty name="alList" property="cssAcctId" /></font></td>
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
	          <tr class="grid">
	            <td class="label">&nbsp;Last Statement Date</td>
	            <td class="gridTd">
					<jsp:getProperty name="alList" property="lastStatementDate" />&nbsp;</td>
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
          List list = alList.getTransactionList();
          int index;
          String serviceDesc;
          String url="AllocationInformation";
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
            <td class="gridTd" width=50% align=left>Project Plan</td>
            <td class="gridTd" width=20% align=left>Create Date</td>
            <td class="gridTd" width=20% align=left>Effective Date  </td>
            <td class="gridTd" width=10% align=left>Status</td>
          </tr>

            <%

			for (Iterator it = list.iterator(); it.hasNext();) {
			    HostAllocationBean ha = (HostAllocationBean) it.next();
                  serviceDesc = ha.getAllocationTypeDescription();
                  index = serviceDesc.indexOf("/");
                  if (index > -1) {
                     serviceDesc = serviceDesc.substring(index + 2);
                  }
		if (gridValue == "grid")
			gridValue = "gridAlt";
		else
			gridValue = "grid";
			%>
	          <tr class='<%= gridValue %>'>
	            <td width=50% class="gridTd"><a href="<%= response.encodeURL(url) %>?allocationId=<%= ha.getAllocationId() %>"><%= Util.ShowField(serviceDesc) %></a>&nbsp;</td>
	            <td width=20% class="gridTd"><%= Util.ShowField(ha.getCreateDate()) %>&nbsp;</td>
	            <td width=20% class="gridTd"><%= Util.ShowField(ha.getEffectiveDate()) %>&nbsp;</td>
	            <td width=10% class="gridTd"><%= Util.ShowField(ha.getStatus()) %>&nbsp;</td>
	          </tr>
              <% } %>
          </table>
        </div>
      </td>
    </tr>
          <% } else { %>
              <table width="100%">
              <tr>
                <td width="100%" align=center><font color=red><strong>No Allocation Plans Exist </strong></font></td>
              </tr>
              </table>
          <% } %>
        </table>
      </td>
    </tr>
  </table>
</div>
<%@ include file="CMIncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Value Stack Project History</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayVDERMenu("AllocationHistory", out, response); %>
<%@ include file="IncBottomAccount.jsp" %>
</form>
</body>
</html>
