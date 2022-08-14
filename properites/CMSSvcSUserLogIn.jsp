<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Log In</title>
<%@ include file="IncPreamble.jsp" %>
</head>
<body  class="wssBody">
<script type="text/javascript">
function Jump(){
  var per = Form1.userId.value;
  if (per == "" || per == null) {
    window.alert("Please Enter Your User id");
    var str="login";
    document.getElementById("pw").href = str;
  } else {
    var str="passwordReminder?personId=" + per;
    document.getElementById("pw").href = str;
  }
}

function PasswordChange(){
  var per = Form1.userId.value;
  if (per == "" || per == null) {
    window.alert("Please Enter Your User id");
    var str="login";
    document.getElementById("pwChange").href = str;
  } else {
    var str="passwordChange?personId=" + per;
    document.getElementById("pwChange").href = str;
  }
}

function CheckInput() {
  if (document.Form1.userId.value == "") {
    window.alert("Please Enter Your User id");
    return;
  } else if (document.Form1.password.value == "") {
    window.alert("Please Enter Your Password");
    return;
  } else {
    document.Form1.submit();
  }
}

function CheckKey() {

  var charCode = event.keyCode;
  if (charCode == 13) {
    document.Form1.submit();
  }

}

</script>
<form name="Form1" onkeypress="CheckKey();"
action="<%= response.encodeURL("/SelfService/SSvcController/authenticate") %>"
method="post" accept-charset="utf-8">
<input type="hidden" name="_charset_" value="">
<%@include file="ErrorHandler.jsp" %>
<div align="left">
  <table width="750" height="510" class=wssTable0>
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
                      <td width="360" class="wssTDMidCenterH50"><font class=wssFontArial2><img border="0" src="/SelfService/graphics/Icons.gif" width="200" height="50"></font></td>
                      <td width="90" class="wssTDMidCenterH50"><font class=wssFontArial2><a href="http://www.oracle.com"><img border="0" src="/SelfService/graphics/Oracle_logo.gif" width="212" height="30"></a></font></td>
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
                                 class="wssFontArial5">Log In</font></b></td>
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
      <td width="750" class=wssTDTopLeft >
        <div class="wssDivReg" align="left">
          <table width="750" class=wssTableSimple>
            <tr>
              <td width="4" class=wssTDTopLeft><img border="0" src="/SelfService/graphics/Button_03.gif" width="4" height="5"></td>
              <td width="746" class=wssTDTopLeft>
                <table border="0" width="100%"  cellspacing="5" cellpadding="0">
                  <tr>
                    <td width="100%" colspan="4" align="center"><font face="Arial" size="3" ></font></td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="label" width="100%" colspan="4" align="center">Please Enter Your User Id And Password</td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
                   <tr>
                    <td width="25%" align="center">&nbsp;</td>
                    <td class="label" width="25%" align="right">User Id</td>
                    <td width="25%" align="left"><font class=wssFontArial2 ><input type="text" name="userId" id="userId" maxLength="16" size="14" value=""></font></td>
                    <td width="25%" align="center">&nbsp;</td>
                  </tr>
                  <script>
                  document.Form1.userId.focus();
                  </script>
                     <% String password = request.getParameter("password");
                      if (password == null) password = "";
                     %>
                  <tr>
                    <td width="25%" align="center">&nbsp;</td>
                    <td class="label" width="25%" align="right">Password</td>
                    <td width="25%" align="left"><font class=wssFontArial2 ><input type="password" name="password" maxLength="12" size="14" value=""></font></td>
                    <td width="25%" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="25%" align="center">&nbsp;</td>
                    <td class="label" width="25%" align="right">Password</td>
                    <td width="25%" align="left"><font class=wssFontArial2 ><input type="text" name="acctId" maxLength="10" size="14" value=""></font></td>
                    <td width="25%" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">
                      <p align="center"><input class="button" type="button" name="Button" value="Log In"   onClick="CheckInput();"></p>
                    </td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="100%" class="normal" colspan="4" align="left"><a href="<%= response.encodeURL("registration") %>"><!--To Register, click here--></a></td>
                  </tr>
                  <tr>
                    <td width="100%" class="normal" colspan="4" align="left"><a id="pw" name="pw" onClick="Jump();" href="" >
	            	<!--Forgot your Password, click here--></a></td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="left">
	            	<font class="wssFontArial2"><!--Forgot your user id? Call Customer service at 1-800-CALL-UTILITY--></font></td>
                  </tr>
                 </table>
                    <% String origURL = request.getParameter("origURL"); %>
                    <input type="hidden" name="origURL" value="<%= origURL == null ? "" : origURL %>">
              </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td width="750" class=wssTDTopLeft height="50">
        <p align="right"><span style="layout-flow: vertical"><b><font class="wssFontArial2">Copyright (c) 2000, 2007, Oracle. <span style="mso-spacerun: yes">
        </span>All Rights Reserved.</font></b></span></td>
    </tr>
  </table>
</div>
</form>
</body>
</html>
