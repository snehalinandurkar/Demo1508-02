<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>View Meter Read</title>
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
  <div class="wssDivLeft">
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
              <td class=wssTDMidCenter>&nbsp;</td>
              <td class=wssTDMidCenter>&nbsp;</td>
              <td class=wssTDMidCenter><p>&nbsp;</p></td>
            </tr>
            <tr>
            <td width="501" class=wssTDMidCenter colspan="2"><b><font class=wssFontArial3
              ><p>&nbsp;</p>No meter exists!</font></b></td>
            </tr>
            <tr>
              <td class=wssTDMidLeft>&nbsp;</td>
              <td class=wssTDMidLeft>&nbsp;</td>
              <td class=wssTDMidLeft><p>&nbsp;</p></td>
            </tr>
            <tr>
              <td width="501" class=wssTDMidLeft colspan="2"><font class=wssFontArial2>
              <p align="center">(No meter currently exists for this service.)</p></font></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>
  &nbsp;</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">No Meter Reads Exist</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("", out, response); %>
<%@ include file="IncBottom.jsp" %>
</body>
</html>
