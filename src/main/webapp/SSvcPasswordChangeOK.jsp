<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Password Change</title>
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
<form name="Form1">
<%@include file="ErrorHandler.jsp" %>
<div id="Content" class="wssDivMain">
  <div align="left">
    <table class="wssTableMajor">
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
          <table width="520" class=wssTable0>
            <tr>
              <td width="520" >&nbsp;</td>
            </tr>
            <tr>
              <td width="520" >&nbsp;</td>
            </tr>
            <tr>
              <td width="520" class=wssTDMidCenter colspan="2"><p align="center"><b><font class="wssFontArial2">
              Thank You!&nbsp;&nbsp; Your Password Has Been Changed Successfully.
                  </font></b></p>
                <p align="center"><strong><font class="wssFontArial2">An Email With
                  Your New Password Has Been Sent To You.</font></strong></p>
                </td>
            </tr>
            <tr> 
              <td width="501" >&nbsp;</td>
              <td width="501" >&nbsp;</td>
            </tr>
            <tr> 
              <td >&nbsp;</td>
              <td >&nbsp;</td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </div>
  &nbsp;</div>
<%@ include file="IncTop.jsp" %>
<td class=wssTDMidCenter height="40" width="540"><b><font
 class=wssFontArial5>Password Change</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("ChangePassword", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
