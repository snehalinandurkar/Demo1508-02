<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List,
                 java.util.Iterator,
                 com.splwg.selfservice.ServiceAgreementBean"%>
<html>
<head>
<title>Disconnect Service</title>
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
  id="saList"
  scope="request"
  class="com.splwg.selfservice.SAforAccountBean">
</jsp:useBean>

<%--
--%>
<body class="wssBody">
<form name="Form1" method="post" action="<%= response.encodeURL("disconnectService") %>">
<%@include file="ErrorHandler.jsp" %>
<div id="Content" class="wssDivMain">
  <table class="wssTableMajor">
    <tr>
      <td class="wssTD100">
        <table class="wssTableMinor">
          <tr class="headerRow">
            <td class="label">Account Number</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="accountId" /></font></td>
          </tr>
          <tr class="headerRow">
            <td class="label">Customer Name</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="entityName" /></font></td>
          </tr>
        </table>
      </td>
    </tr>

 
    <%
      List list = saList.getDisconnectSAList();
      int index;
      if (list.size() > 0) {
    %>
     <tr>
      <td width=95%>
        <div id="Content2" class="wssDivSub">
          <table class="table#dataTable" width=98% border="1" cellpadding=0 cellspacing=0>
        <table width="100%">

    <tr class="gridLabel">
        <td width="15%" class="gridTd">Status</td>
        <td  width="33%" class="gridTd">Address</td>
        <td width="16%" class="gridTd">Service</td>
        <td width="18%" class="gridTd">Start</td>
        <td width="18%" class="gridTd">End</td>
      </tr>    <%  
	String gridValue="gridAlt";
	for (Iterator it = list.iterator(); it.hasNext();) {
          ServiceAgreementBean sa = (ServiceAgreementBean) it.next();
          if (gridValue == "grid")
		gridValue = "gridAlt";
    	  else
		gridValue = "grid";
    %>
    <tr class="<%= gridValue %>">
      <td width=10% class="gridTd"><% if (sa.getDisconnectMessage().equals("Could Not Stop")) { %><font color=red><b> <% } %><%= sa.getDisconnectMessage() %></b></font></td>
      <td width=36% class="gridTd"><%= sa.getAddress() %>&nbsp;</td>
      <td width=18% class="gridTd"><%= sa.getServiceDesc() %>&nbsp;</td>
      <td width=18% class="gridTd"><%= sa.getStartDate()%>&nbsp;</td>
      <td width=18% class="gridTd"><%= sa.getEndDate()%>&nbsp;</td>
    </tr>
  <%
      }
   %>
          </table>
        </div>
      </td>
    </tr>
  </table>
  <% } else { %>
      <table width="100%">
      <tr>
        <td width="100%" align=center><font color=red><strong>No Service Agreements Have Been Selected</strong></font></td>
      </tr>
      </table>
  <% } %>
</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Stop Service Status</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("StopService", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
