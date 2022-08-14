<!--*
	* CMSSvcPasswordChange.jsp
	*
	* Created on December 7, 2011, by Jordan Fernandez
	* Part of CorDaptix Web Self Service (10321)
	* Cloned SSvcPasswordChange.jsp
	* Implements the Action for updating personal information
	* Added processing for super user access
	* Added processing for super user access. All input fields 
	* (textbox, buttons, and drow-down menus) are disabled.
	*
	* CHANGE HISTORY:
	*
	* Date:       by:    Reason:
	*
	* YYYY-MM-DD  IN     Reason text.
    * 2015-05-12  MCerip Security Fix.    
    * 2015-06-19  MCerip Security Fix Release 7.
    * 2015-06-22  MCerip Security Fix Release 8.
    *
	*-->
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.splwg.selfservice.WebStringUtilities" %>
<html>
<head>
<title>Change Password</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>

</head>
<jsp:useBean
  id="user"
  scope="request"
  class="com.splwg.selfservice.PasswordReminderBean">
</jsp:useBean>
<jsp:useBean
  id="passwordHint"
  scope="session"
  class="com.splwg.selfservice.PasswordHintBean">
</jsp:useBean>
<script type="text/javascript" src="/SelfService/jcap/md5.js"></script>
<script type="text/javascript" src="/SelfService/jcap/jcap.js"></script>
<%--
--%>
<body class="wssBody">
<form name="Form1" name="Form1" action="<%= response.encodeURL("passwordChange?step=process") %>" method="post" accept-charset="utf-8">
<input type="hidden" name="_charset_" value="">
<script type="text/javascript">

function CheckInput() {
  if (document.Form1.priorPassword.value == "") {
    window.alert("Please enter the old password");
  } else if (document.Form1.newPassword.value == "") {
    window.alert("Please enter the new password");
  } else if (document.Form1.newPassword.value.length < 4) {
    window.alert("The password must be at least 4 characters long");
  } else if (document.Form1.newPasswordConfirm.value == "") {
    window.alert("Please confirm the new password");
  } else if (document.Form1.passwordAnswer.value == "") {
    window.alert("Please enter an answer to the password hint");
  } else if (document.Form1.newPassword.value != document.Form1.newPasswordConfirm.value) {
    window.alert("The password Confirmation value must be identical to the password value");
  } else if (document.Form1.priorPassword.value == document.Form1.newPassword.value ) {
    window.alert("The new password must be different from the old one");
  } else {
    document.Form1.submit();
  }
}

</script>
 <%@include file="ErrorHandler.jsp" %>
<div id="Content" class="wssDivMain">
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
      <td width="500" class=wssTDTopLeft >
        <div class="wssDivReg" align="left">
          <table width="500" class=wssTable0>
            <tr>
              <td width="4" class=wssTDTopLeft><img border="0" src="/SelfService/graphics/Button_03.gif" width="4" height="5"></td>
              <td width="496" class=wssTDTopLeft>
                 <table width="100%" class=wssTable0 >
                    <tr>
                      <td class=wssTDMidCenter4><font face="Arial" size="3" ></font></td>
                    </tr>
                    <tr>
                      <td >&nbsp;</td>
                      <td >&nbsp;</td>
                      <td >&nbsp;</td>
                      <td >&nbsp;</td>
                    </tr>
                    <tr>
                      <td >&nbsp;</td>
                    <% String userId = user.getUserId();
                       if (userId == null)
                         request.getParameter("userId");
                      if (userId == null) userId = "";
                    %>
                      <td class="label" align="right">User
                        ID:</td>
                      <td align="left"><font class=wssFontArial2 >
                        <input name="userId" type="text" id="userId" size="12" disabled value="<%= WebStringUtilities.asHTML(userId) %>">
                        </font></td>
                      <td width="18%" align="center">&nbsp;</td>
                    </tr>
                    <% String priorPassword = request.getParameter("priorPassword");
                       if (priorPassword == null) priorPassword = ""; %>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right">Old Password</td>
                      <td align="left"><font class=wssFontArial2 >
                        <input name="priorPassword" type="password" id="priorPassword" maxLength="12" size="12" disabled = "true" value="<%= WebStringUtilities.asHTML(priorPassword) %>">
                        </font></td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <% String newPassword = request.getParameter("newPassword");
                       if (newPassword == null) newPassword = "";
                    %>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right">New Password</td>
                      <td align="left"><font class=wssFontArial2 >
                        <input name="newPassword" type="password" id="newPassword" size="12" maxLength="12" disabled = "true" value="<%= WebStringUtilities.asHTML(newPassword) %>">
                        </font></td>
                      <td >&nbsp;</td>
                    </tr>
                    <% String newPasswordConfirm = request.getParameter("newPasswordConfirm");
                       if (newPasswordConfirm == null) newPasswordConfirm = "";
                    %>
                    <tr>
                      <td >&nbsp;</td>
                      <td class="label" align="right">Confirm
                        New Password</td>
                      <td align="left"><font class=wssFontArial2 >
                        <input name="newPasswordConfirm" type="password" id="newPasswordConfirm" maxLength="12" size="12" disabled = "true" value="<%= WebStringUtilities.asHTML(newPasswordConfirm) %>">
                        </font></td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right">Password
                        Reminder Question</td>
                      <td align="left"><font class=wssFontArial2 >
                          <select name="passwordQuestion" disabled = "true">
                      <%
                        String question = user.getPasswordQuestion();
                        if (question == null)
                          question = "";
                        HashMap map = passwordHint.getQuestions();
                        int index;
                        String serviceDesc;
                        String address;
                        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
                          Map.Entry me = (Map.Entry) it.next();


                      %>
                            <option value="<%= me.getKey() %>" <%= question.equals(me.getValue()) ? "selected" : "" %>><%= me.getValue() %>

                      <% } %>
                        </select>
                        </font></td>
                      <td align="center">&nbsp;</td>
                    </tr>
                     <% String passwordAnswer = request.getParameter("passwordAnswer");
                       if (passwordAnswer == null) passwordAnswer = "";
                     %>
                               <tr>
                      <td width="15%" align="center">&nbsp;</td>
                      <td width="31%" align="right"><b><font class=wssFontArial2 >
                        Answer to Reminder:</font></b></td>
                      <td width="34%" align="left"><font class=wssFontArial2 >
                        <input name="passwordAnswer" type="text" id="passwordAnswer" size="32" maxLength="60" disabled = "true" value="<%= WebStringUtilities.asHTML(passwordAnswer) %>">
                        </font></td>
                      <td width="20%" align="center">&nbsp;</td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr>
                        <td class="Captchalabel" colspan="2" align="right" valign="top">Enter word shown below&nbsp;</td>
                        <td width="25%" align="left"><font class=wssFontArial2 >
                        <script type="text/javascript">sjcap();</script>
                        </td>
                        <td width="25%" align="center">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="100%" colspan="4" align="center">&nbsp;</td>
                    </tr>
                    <tr>
                      <td colspan=4 class=wssTDMidCenter4> <p align="center">
                            <input class="button" type="button" name="Button" value="Change Password" disabled = "true" onClick="if(jcap()){ CheckInput(); }else{return false;};">
                        </p></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                    <tr>
                  </table>
              </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
    <tr>
      <td width="750" class=wssTDTopLeft>
        <p align="right"><span style="layout-flow: vertical"></span></td>
    </tr>
  </table>
</div>
<%@ include file="CMIncTop.jsp" %>
<td class=wssTDMidCenter height="40" width="540"><b><font
 class=wssFontArial5>Password Change</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("ChangePassword", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
