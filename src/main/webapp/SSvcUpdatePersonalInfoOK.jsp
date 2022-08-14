<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Update Personal Information</title>
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
<%--
--%>
<body class="wssBody">
<div id="Content" class="wssDivMain">
  <table class="wssTableMajor">
    <tr>
      <td class="wssTD100">
        <table class="wssTableMinor">
          <tr class="headerRow">
            <td class="label">Account Number</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="SelfService_validUser" property="accountId" /></font></td>
          </tr>
          <tr class="headerRow">
            <td class="label">Customer Name</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="SelfService_validUser" property="entityName" /></font></td>
          </tr>
        </table>
      </td>
    </tr>
      <tr>
        <td width="100%">
          <table class=wssTableMinor>
            <tr>
              <td class=wssTDMidLeft>&nbsp;</td>
              <td width="287" valign="middle" align="left">&nbsp;</td>
            </tr>
            <tr>
              <td width="501" class="wssTDMidCenter" colspan="2"><b><font face="Arial"
              size="3">Thank You!&nbsp;&nbsp; Your Personal Information has been updated.</font></b></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>
</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Update Personal Information</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("", out, response); %>
<%@ include file="IncBottom.jsp" %>
</body>
</html>
