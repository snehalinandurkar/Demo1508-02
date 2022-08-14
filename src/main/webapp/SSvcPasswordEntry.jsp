<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List,
                 java.util.Iterator,
                 java.util.HashMap,
                 java.util.Map.Entry,
                 java.util.*"%>
<html>
<head>
<title>Customer Registration</title>
<%@ include file="IncPreamble.jsp" %>
</head>
<jsp:useBean
  id="personsForAccount"
  scope="session"
  class="com.splwg.selfservice.PersonsForAccountBean">
</jsp:useBean>
<jsp:useBean
  id="passwordHint"
  scope="session"
  class="com.splwg.selfservice.PasswordHintBean">
</jsp:useBean>
<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Add -->
<%@ page import="com.splwg.selfservice.WebStringUtilities" %>
<!-- SMedina - 05-11-2015 - Security Vulnerability - End Add -->
 <%--
--%>
<script type="text/javascript" src="/SelfService/jcap/md5.js"></script>
<script type="text/javascript" src="/SelfService/jcap/jcap.js"></script>
<body class="wssBody">
<form name="Form1" name="Form1" action="<%= response.encodeURL("registration?step=passwordProcess") %>" method="post" accept-charset="utf-8">
<input type="hidden" name="_charset_" value="">
<%@include file="ErrorHandler.jsp" %>
<%
	String warningMsg = request.getParameter("warningMsg");
          if (warningMsg != null && warningMsg.equals("") == false && warningMsg.equals("null") == false) {
          out.println("<input id=WarningMessage name=WarningMessage type=hidden value=" + '"' + warningMsg + '"' + ">");
        %>
          <script>
              window.alert(document.Form1.WarningMessage.value);
          </script>
        <%
	}
%>
<script type="text/javascript">

function CheckInput() {
  if (document.Form1.password.value == "") {
    window.alert("Please enter a password");
  } else if (document.Form1.user.value == "") {
    window.alert("Please enter a user id");
  } else if (document.Form1.user.value.length < 6) {
    window.alert("The user id must be at least 6 characters long");
  } else if (document.Form1.passwordConfirm.value == "") {
    window.alert("Please enter a password confirmation value");
  } else if (document.Form1.password.value.length < 4) {
    window.alert("The password must be at least 4 characters long");
  } else if (document.Form1.passwordAnswer.value == "") {
    window.alert("Please enter an answer to the password hint");
  } else if (document.Form1.password.value != document.Form1.passwordConfirm.value) {
    window.alert("The passward Confirmation value must be identical to the password value");
  } else {
    document.Form1.submit();
  }
}

function Cancel() {
    location.href="/SelfService";
}


</script>
<div align="left">
  <table width="750" class=wssTable0>
    <tr>
      <td width="750" class=wssTable0 height="100">
        <div align="left">
          <table class=wssTable0 width="750" height="100" >
            <tr>
              <td width="750" class=wssTDTopLeft height="50">
                <div align="left">
                  <table class=wssTable0 width="750" height="50" >
                    <tr>
                      <td width="210" class="wssTDMidCenterH50"><font class=wssFontArial2><img border="0" src="/SelfService/graphics/Self_Service_Logo.gif" width="210" height="50"></font></td>
                      <td width="90" class="wssTDMidCenterH50"></td>
                      <td width="360" class="wssTDMidCenterH50"><font class=wssFontArial2></font></td>
                      <td width="90" class="wssTDMidCenterH50"><font class=wssFontArial2><a href="http://www.coned.com"><img border="0" src="/SelfService/graphics/Icons.gif" width="200" height="50"></a></font></td>
                    </tr>
                  </table>
                </div>
              </td>
            </tr>
            <tr>
              <td width="750" class=wssTDTopLeft height="50">
                <div align="left">
                  <table width="750" class=wssTable0 height="50">
                    <tr>
                      <td width="750" class=wssTDMidCenter height="5"><font class=wssFontArial2><img border="0" src="/SelfService/graphics/Line_01.gif" width="750" height="5"></font></td>
                    </tr>
                    <tr>
                      <td width="750" class=wssTDMidCenter height="40">
                        <div align="left">
                          <table class=wssTable0 width="750" height="40" >
                            <tr>
                              <td class=wssTDMidCenter height="40" width="210"></td>
                              <td class=wssTDMidCenter height="40" width="270"><b><font
                                 class="wssFontArial5">Password Entry</font></b></td>
                                <td class=wssTDMidCenter height="40" width="210"></td>
                            </tr>
                          </table>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td width="750" class=wssTDMidCenter height="5"><font class=wssFontArial2><img border="0" src="/SelfService/graphics/Line_02.gif" width="750" height="5"></font></td>
                    </tr>
                  </table>
                </div>
              </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
    <tr>
      <td width="750" class=wssTDTopLeft>
        <div align="left">
          <table width="750" class=wssTableSimple>
            <tr>
              <td width="746" class=wssTDTopLeft >
                 <table width="100%" class=wssTable0>
                    <tr>
                      <td colspan="4" align="center"><font class=wssFontArial3 ></font></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                    <tr>
                    <tr>
                      <td class="label" colspan="4" align="center">You've
                        Been Successfully Identified In The System</td>
                    </tr>
                    <tr>
                      <td class="label" colspan="4" align="center">Please Enter The
                        Following User And Password Information</td>
                    </tr>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td align="right">&nbsp;</td>
                      <td align="left">&nbsp;</td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <% String user = request.getParameter("user");
                       if (user == null) user = "";
                     %> 
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right">User Id</td>
                      <td align="left">
                        <!-- Start Change - 2015-05-11 - MCeripul - Security Fix -->
                        <!-- <input name="user" type="user" id="user" size="12" maxLength="16" value="%= user %"> -->
                        <input name="user" type="user" id="user" size="12" maxLength="16" value="<%= WebStringUtilities.asHTML(user) %>">
                        <!-- End Change - 2015-05-11 - MCeripul - Security Fix -->
                      </td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <% String password = request.getParameter("password");
                       if (password == null) password = "";
                     %>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right">Password</td>
                      <td align="left">
                        <!-- Start Change - 2015-05-11 - MCeripul - Security Fix -->
                        <!-- <input name="password" type="password" id="password" size="12" maxLength="12" value="%= password %"> -->
                        <input name="password" type="password" id="password" size="12" maxLength="12" value="<%= WebStringUtilities.asHTML(password) %>">
                        <!-- End Change - 2015-05-11 - MCeripul - Security Fix -->
                      </td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <% String passwordConfirm = request.getParameter("passwordConfirm");
                       if (passwordConfirm == null) passwordConfirm = "";
                     %>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right">Confirm Password</td>
                      <td align="left">
                        <!-- Start Change - 2015-05-11 - MCeripul - Security Fix -->
                        <!-- <input name="passwordConfirm" type="password" id="passwordConfirm" size="12" maxLength="12" value="%= passwordConfirm %"> -->
                        <input name="passwordConfirm" type="password" id="passwordConfirm" size="12" maxLength="12" value="<%= WebStringUtilities.asHTML(passwordConfirm) %>">
                        <!-- End Change - 2015-05-11 - MCeripul - Security Fix -->
                        </td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <tr> 
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right"><strong>Password Reminder Question</td>
                      <td align="left">
                          <select name="passwordQuestion">
                      <%
                        String question = request.getParameter("passwordQuestion");
                        if (question == null)
                          question = "";
                        HashMap map = passwordHint.getQuestions();
                        int index;
                        String serviceDesc;
                        String address;
                        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
                          Map.Entry me = (Map.Entry) it.next();


                      %>
                            <option value="<%= me.getKey() %>" <%= question.equals(me.getKey()) ? "selected" : "" %>><%= me.getValue() %>
                      <% } %>
                        </select>
                        </td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <% String passwordAnswer = request.getParameter("passwordAnswer");
                       if (passwordAnswer == null) passwordAnswer = "";
                     %>
                    <tr>
                      <td width="15%" align="center">&nbsp;</td>
                      <td class="label" width="31%" align="right">Answer to Reminder</td>
                      <td width="34%" align="left">
                        <!-- Start Change - 2015-05-11 - MCeripul - Security Fix -->
                        <!-- <input name="passwordAnswer" type="text" id="passwordAnswer" size="32" maxLength="60" value="%= passwordAnswer %"> -->
                        <input name="passwordAnswer" type="text" id="passwordAnswer" size="32" maxLength="60" value="<%= WebStringUtilities.asHTML(passwordAnswer) %>">
                        <!-- End Change - 2015-05-11 - MCeripul - Security Fix -->
                      </td>
                      <td width="20%" align="center">&nbsp;</td>
                    </tr>
                    <%
                      if (personsForAccount.getEmailExists() == false) {
                        String emailAddress = request.getParameter("emailAddress");
                        if (emailAddress== null) emailAddress = "";
                    %>
                    <tr>
                      <td width="15%" align="center">&nbsp;</td>
                      <td class="label" width="31%" align="right">Email Address (required)</td>
                      <td width="34%" align="left">
                        <!-- Start Change - 2015-05-11 - MCeripul - Security Fix -->
                        <!-- <input name="emailAddress" type="text" id="emailAddress" size="32" value="%= emailAddress %"> -->
                        <input name="emailAddress" type="text" id="emailAddress" size="32" value="<%= WebStringUtilities.asHTML(emailAddress) %>">
                        <!-- End Change - 2015-05-11 - MCeripul - Security Fix -->
                      </td>
                      <td width="20%" align="center">&nbsp;</td>
                    </tr>
                    <% }
                    %>
                    <!-- Start Add - 2015-05-11 - MCeripul - Security Fix -->
                    <!-- Captcha Field -->
                    <tr>
                        <!--<td width="25%" align="center" bgcolor=red>&nbsp;</td>-->
                        <td class="Captchalabel" colspan="2" align="right" valign="top">Enter word shown below&nbsp;</td>
                        <td width="25%" align="left"><font class=wssFontArial2 >
                        <script type="text/javascript">sjcap();</script>
                        </td>
                        <td width="25%" align="center">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="100%" colspan="4" align="center">&nbsp;</td>
                    </tr>
                    <!-- End Add - 2015-05-11 - MCeripul - Security Fix -->
                    <tr><td>&nbsp;</td></tr>
                    <tr>
                      <td colspan="4" align="center"> <p align="center">
                        <!-- Start Change - 2015-05-11 - MCeripul - Security Fix -->
                        <!-- <input class="button" type="button" name="Button" value="Submit"  onClick="CheckInput();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
                        <input class="button" type="button" name="Button" value="Submit"  onClick="if(jcap()){ CheckInput(); }else{return false;};">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <!-- End Change - 2015-05-11 - MCeripul - Security Fix -->
                          <input class="button"  type="button" name="CancelButton" id="CancelButton" value="Cancel" onClick="Cancel();">
                        </p></td>
                    </tr>
                    <tr>
                      <td colspan="4" align="center">&nbsp;</td>
                    </tr>
                  </table>
                    <% String origURL = request.getParameter("origURL"); %>
                    <!-- Start Add - 2015-05-11 - MCeripul - Security Fix -->
                    <% origURL = WebStringUtilities.asHTML(origURL); %>
                    <!-- End Add - 2015-05-11 - MCeripul - Security Fix -->
                    <input type="hidden" name="origURL" value="<%= origURL == null ? "" : origURL %>">
              </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
    <tr>
      <td width="750" class=wssTDTopLeft height="50">
        <p align="right"><span style="layout-flow: vertical"></span></td>
    </tr>
  </table>
</div>
</form>
</body>
</html>
