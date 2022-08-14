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
<jsp:useBean
  id="mode"
  scope="request"
  class="java.lang.String">
</jsp:useBean>
<%--
--%>
<body class="wssBody">
<form name=Form1 action="subscriberadd" method="post">
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
	            <td class="label">&nbsp;Host ID</td>
	            <td class="gridTd">
					<jsp:getProperty name="alPlan" property="hostAllocationId" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;Host Allocation Type</td>
	            <td class="gridTd">
					<!-- jsp:getProperty name="alPlan" property="hostAllocationType" / --></font>&nbsp;</td>
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
	            <td class="label">&nbsp;Plan ID</td>
	            <td class="gridTd">
					<jsp:getProperty name="alPlan" property="subAllocationPlanId" />&nbsp;</td>
	          </tr>
	          <tr class="gridAlt">
	            <td class="label">&nbsp;Allocation Plan Type</td>
	            <td class="gridTd">
					<jsp:getProperty name="alPlan" property="planType" />&nbsp;</td>
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
    <tr>
      <td width=95% colspan=8>
        <div id="Content2" class="wssDivSubAllocation">
<% 
      List types = alPlan.getTypeList(); %>
                         Allocation Plan Type&nbsp;<select name="planType">
<% 

			for (Iterator it = types.iterator(); it.hasNext();) {
			    AllocationPlanTypeBean pt = (AllocationPlanTypeBean) it.next();
%>
<option value="<%= pt.getSubAllocTypeCd() %>"><%= pt.getSubAllocTypeDesc() %></option>
<% } %>

</select>
          <input type="hidden" name="allocId" value="<%= alPlan.getHostAllocationId() %>" >

          <input type="submit" value="Create" onclick="add();">

        </div>
      </td>
    </tr>
        </table>
      </td>
    </tr>
  </table>
</div>
<%@ include file="CMIncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Value Stack <%= mode %> Satellite Plan</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayVDERMenu("", out, response); %>
<%@ include file="IncBottomAccount.jsp" %>
</form>
</body>
</html>
