<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>Allocation Information</title>
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
  id="details"
  scope="request"
  class="com.splwg.selfservice.AllocationDetailsBean">
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
  		<jsp:getProperty name="details" property="cssAcctId" /></font></td>
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
					<jsp:getProperty name="accountInfo" property="lastBill" />&nbsp;</td>
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


                <tr></tr>
               <table class="wssTable650">
                  <tr class="grid" style="display: none;">
                    <td class="label">&nbsp;Host Allocation ID</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="details" property="allocationId" />&nbsp;</td>
                  </tr>
                  <tr class="grid">
                    <td class="label">&nbsp;Project Type</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="details" property="projectTypeDesc" />&nbsp;</td>
                  </tr>
                  <tr class="gridAlt">
                    <td class="label">&nbsp;Project Start Date</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="details" property="projectStartDate" /></font>&nbsp;</td>
                  </tr>
                  <tr class="grid">
                    <td class="label">&nbsp;MC #</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="details" property="masterCaseNumber" />&nbsp;</td>
                  </tr>
                  <tr class="gridAlt">
                    <td class="label">&nbsp;Project Status</td>
                    <td class="gridTd">
                                        <jsp:getProperty name="details" property="projectStatus" />&nbsp;</td>
                  </tr>
                  	<tr class="grid">
                    	<td class="label">&nbsp;Bank Balance</td>
                    	<td class="gridTd">
<% if (details.getProjectType().startsWith("CDG")) { %>
                                       <jsp:getProperty name="details" property="bankBalance" />&nbsp;</td>
<% } else { %>
                                       <jsp:getProperty name="details" property="monthlyBankBalance" />&nbsp;</td>
<% } %>

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
          String url="subscriberedit";
          List creditList = details.getCreditList();
          if (creditList.size() > 0) {
          %>
    <tr>
      <td width=95% colspan=8>
        <div id="Content2" class="wssDivSubAccount" >
          <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1" >
            <%
			String gridValue="gridAlt";
	    %>
          <tr class='gridLabel'>
            <td class="gridTd" width=50% align=left>Credit Option</td>
            <td class="gridTd" width=30% align=left>Value</td>
            <td class="gridTd" width=20% align=left>Effective Date</td>
          </tr>

            <%

			for (Iterator it = creditList.iterator(); it.hasNext();) {
			    CreditOptionBean co = (CreditOptionBean) it.next();
		if (gridValue == "grid")
			gridValue = "gridAlt";
		else
			gridValue = "grid";
			%>
	          <tr class='<%= gridValue %>'>
	            <td width=50% class="gridTd"><%= Util.ShowField(co.getDescription()) %>&nbsp;</td>
	            <td width=30% class="gridTd"><%= Util.ShowField(co.getValue()) %>&nbsp;</td>
	            <td width=20% class="gridTd"><%= Util.ShowField(co.getEffectiveDate()) %>&nbsp;</td>
	          </tr>
              <% } %>
          </table>
        </div>
      </td>
    </tr>
          <% } else { %>
              <table width="100%">
              <tr>
                <td width="100%" align=center><font color=red><strong>No Credit Options Exist </strong></font></td>
              </tr>
              </table>
          <% } %>
        </table>
      </td>
    </tr>
    <tr>
       <% 
	if ((! "ONSITE".contains(details.getProjectType())) && (! "ONSITE2".contains(details.getProjectType()))) {
              url = "subscriberadd";
	%>
       	   <td>Active Satellite Plans</td>
           <td><a href="<%= response.encodeURL(url) %>?allocId=<%= details.getAllocationId() %>&pt=<%= details.getProjectType() %>">Add new Satellite Plan</a></td>
	<% } %>
    </tr>
    <tr>
        <% 
	if ((! "ONSITE".contains(details.getProjectType())) && (! "ONSITE2".contains(details.getProjectType()))) {
	%>
      <td class="wssTD100">
        <table >
          <%
          String planUrl = "subscriberplan";
          List planList = details.getPlanList();
          if (planList.size() > 0) {
          %>
    <tr>
      <td width=95% colspan=8>
        <div id="Content2" class="wssDivSubAccount" >
          <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1" >
            <%
                        String gridValue="gridAlt";
            %>
          <tr class='gridLabel'>
            <td class="gridTd" width=45% align=left>Plan Type</td>
            <td class="gridTd" width=20% align=left>Create Date</td>
            <td class="gridTd" width=20% align=left>Effective Date</td>
            <td class="gridTd" width=15% align=left>Status</td>
            <!-- td class="gridTd" width=15% align=left>&nbsp;</td  -->
          </tr>

            <%

                        for (Iterator it = planList.iterator(); it.hasNext();) {
                            SubscriberPlanBean sp = (SubscriberPlanBean) it.next();
                if (gridValue == "grid")
                        gridValue = "gridAlt";
                else
                        gridValue = "grid";
                        %>
                  <tr class='<%= gridValue %>'>
                    <td width=45% class="gridTd"><a href="<%= response.encodeURL(planUrl) %>?planId=<%= sp.getPlanId() %>"><%= sp.getPlanType() %></a>&nbsp;</td>
                    <td width=20% class="gridTd"><%= Util.ShowField(sp.getCreateDate()) %>&nbsp;</td>
                    <td width=20% class="gridTd"><%= Util.ShowField(sp.getEffectiveDate()) %>&nbsp;</td>
                    <td width=15% class="gridTd"><%= Util.ShowField(sp.getStatus()) %>&nbsp;</td>
                    <!-- td width=15% class="gridTd"><%
                                                    if ("ActiveComplete".contains(sp.getStatus())) {%>
                           <a href="<%= response.encodeURL(url) %>?planId=<%= sp.getPlanId() %>&d=1">Duplicate Plan</a>
                                                                                                   <% }
                                                    else if ("Pending".contains(sp.getStatus())) {%>
                           <a href="<%= response.encodeURL(url) %>?planId=<%= sp.getPlanId() %>">Edit Plan</a>
                                                                                                  <% }
                                                    %>&nbsp;</td -->
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
    <% } %> 
   </tr>



  </table>
</div>
<%@ include file="CMIncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Value Stack Project Information</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayVDERMenu("", out, response); %>
<%@ include file="IncBottomAccount.jsp" %>
</form>
</body>
</html>
