<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Registration Confirmation</title>
<%@ include file="IncPreamble.jsp" %>
</head>
<jsp:useBean
  id="validUser"
  scope="request"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<jsp:useBean
  id="personsForAccount"
  scope="session"
  class="com.splwg.selfservice.PersonsForAccountBean">
</jsp:useBean>
<%--
--%>
<body class="wssBody">
<form name="Form1"action="<%= response.encodeURL("myaccounts") %>"
method="post">
<%@include file="ErrorHandler.jsp" %>
<div id="Content" class=wssDivMainSmall>
  <div align="left">
    <table class="wssTableMajor" border="1">
      <tr>
        <td class="wssTD100">
          <table class="wssTableMinor">
            <tr>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td width="100%">
          <table width="510" class=wssTableSimple>
            <tr>
              <td width="510" >&nbsp;</td>
            </tr>
            <tr>
              <td width="510" class="label" align="center" colspan="2"><p>Thank You!&nbsp;&nbsp; Your Registration has been processed successfully.
                  </p>
                  <p align="center"><font class=wssFontArial2>Your User Id is: </font>
                  <font class=wssFontArial2><strong><jsp:getProperty name="validUser" property="userId" /></strong></font></p>
                  <p align="center"><font class=wssFontArial2>Your Password is: </font>
                  <font class=wssFontArial2><strong><jsp:getProperty name="validUser" property="password" /></strong></font></p>
                </td>
            </tr>
            <tr>
              <td colspan=2 align="center"> <p align="center">
                  &nbsp;
                </p></td>
            </tr>
            <% if (personsForAccount.getWebAccess()) {  %>
            <tr>
              <td colspan=2 align="center"> <p align="center">
                  <input type="submit" name="submit" value="View Account Information" class="button">
                </p></td>
            </tr>
            <% } %>
          </table>
        </td>
      </tr>
    </table>
  </div>
  &nbsp;</div>
<%@ include file="IncTopLogin.jsp" %>
<td class=wssTDMidCenter height="40" width="540"><b><font
 class="wssFontArial5">Registration Confirmation</font></b></td>
<%@ include file="IncLeftLogin.jsp" %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
