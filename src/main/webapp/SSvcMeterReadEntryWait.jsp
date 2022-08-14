<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Pay</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>

</head>
<jsp:useBean
  id="SelfService_validUser"
  scope="session"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<jsp:useBean
  id="read"
  scope="request"
  class="com.splwg.selfservice.MeterReadEntryBean">
</jsp:useBean>
<%--
--%>
<body topmargin="0" leftmargin="0" link="#002B5D" vlink="#002B5D" alink="#FF9900">
<div id="Content" style="position:absolute; left:210px; top:100px; width:540px; height:350px; z-index:1; visibility: visible; overflow: auto; background-color: #D9DFE7; layer-background-color: #D9DFE7; border: 1px none #000000">
  <div align="left">
    <table border="1" width="515" align="left" cellspacing="0" cellpadding="0">
      <tr>
        <td width="100%">
          <table border="0" width="517" cellpadding="0" cellspacing="0">
            <tr class="headerRow">
		<td width="206" valign="middle" align="left" class="label">Account Number</td>
		<td valign="middle" align="left"  width="289"><font class="wssFontArial2" size="2">
			<jsp:getProperty name="SelfService_validUser" property="accountId" /></font></td>
            </tr>
            <tr class="headerRow">
		<td width="206" valign="middle" align="left" class="label">Customer Name</td>
		<td valign="middle" align="left" width="289"><font class="wssFontArial2">
			<jsp:getProperty name="SelfService_validUser" property="entityName" /></font></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td width="100%">
          <table border="0" width="515" cellpadding="0" cellspacing="5">
            <tr>
              <td width="493" valign="middle" align="left">&nbsp;</td>
            </tr>
            <tr>
              <td width="493" valign="middle" align="left">&nbsp;</td>
            </tr>
            <tr>
		<td width="501" valign="middle" align="center">
			<b><font class="wssFontArial2">Please Wait...&nbsp; Your meter read entry is being processed.</font></b>
		</td>
            </tr>
            <tr>
              <td width="493" valign="middle" align="left">&nbsp;</td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>
  &nbsp;</div>
<%@ include file="IncTop.jsp" %>

<td class="wssTDMain"><b><font class="wssFontArial5">Meter Read Entry</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu(" ", out, response); %>
<%@ include file="IncBottom.jsp" %>
</body>
</html>
