<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Log In</title>
<%@ include file="IncPreamble.jsp" %>
<!-- Start Add - 2015-05-13 - MCeripul - Security Fix -->
<%
if(session!=null) { 
    session.invalidate();   
} 
%>
<!-- End Add - 2015-05-13 - MCeripul - Security Fix -->
</head>
<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Add -->
<%@ page import="com.splwg.selfservice.WebStringUtilities" %>
<!-- SMedina - 05-11-2015 - Security Vulnerability - End Add -->
<%--
--%>
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
    session.invalidate();
  }
}

function CheckKey() {

  var charCode = event.keyCode;
    // make sure that captcha code has been entered prior to submitting
    // Added July 06, 2015 SA
  if (charCode == 13 && jcap()) 
  {
    document.Form1.submit();
  }

}

</script>

<script type="text/javascript" src="/SelfService/jcap/md5.js"></script>
<script type="text/javascript" src="/SelfService/jcap/jcap.js"></script>

<form name="Form1" onKeyPress="CheckKey();"
action="<%= response.encodeURL("/SelfServiceVK/SSvcController/authenticate") %>"
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
                      <td width="210" class="wssTDMidCenterH50"><font class=wssFontArial2><a href="http://www.ConEd.com"><img border="0" src="/SelfService/graphics/Self_Service_Logo.gif" width="210" height="50"></a></font></td>
                      <td width="90" class="wssTDMidCenterH50"></td>
                      <td width="360" class="wssTDMidCenterH50">&nbsp;</td>
                      <td width="90" class="wssTDMidCenterH50"><font class=wssFontArial2><img border="0" src="/SelfService/graphics/Icons.gif" width="210" height="50"><a href="http://www.splwg.com"></a></font></td>
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
                    <% String userId = request.getParameter("userId");
                      if (userId == null) userId = "";
                     %> 
                   <tr>
                    <td width="25%" align="center">&nbsp;</td>
                    <td class="label" width="25%" align="right">User Id</td>
                    <td width="25%" align="left">
						<font class=wssFontArial2 >
							<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Update -->
							<!-- <input type="text" name="userId" id="userId" maxLength="16" size="14" value=%= userId % > -->
							<input type="text" name="userId" id="userId" maxLength="16" size="14" value="<%= WebStringUtilities.asHTML(userId)%>">
							<!-- SMedina - 05-11-2015 - Security Vulnerability - End Update -->
						</font>
					</td>
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
                    <td width="25%" align="left">
						<font class=wssFontArial2 >
                            <!-- Start Change - 2015-05-19 - MCeripul - Security Fix -->
							<!-- <input type="password" name="password" maxLength="12" size="14" value="%= password %>" > -->
                            <input type="password" name="password" maxLength="12" size="14" value="<%= WebStringUtilities.asHTML(password) %>" >
                            <!-- End Change - 2015-05-19 - MCeripul - Security Fix -->
						</font>
					</td>
                    <td width="25%" align="center">&nbsp;</td>
                  </tr>

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
                    <td width="100%" colspan="4" align="center">
                      <p align="center"><input class="button" type="button" name="Button" value="Log In" onClick="if(jcap()){ CheckInput(); }else{return false;};"></p>
                    </td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="100%" class="normal" colspan="4" align="left"><a href="<%= response.encodeURL("registration") %>">
	            	To Register, click here</a></td>
                  </tr>
                  <tr>
                    <td width="100%" class="normal" colspan="4" align="left"><a id="pw" name="pw" onClick="Jump();" href="" >
	            	Forgot your Password, click here</a></td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="left"><p class="normal">For  Billing Inquires, please contact your assigned Customer Service Representative  during normal billing hours, contact information located on your bill or&nbsp; email &quot;<a href="mailto:conedsteamops@coned.com">dl - conedsteamops@coned.com</a>&quot;. &nbsp;&nbsp;For more information please visit <a href="http://www.conEd.com/steam">www.conEd.com/steam</a>.</p></td>
                  </tr>
                 </table>
                    <% String origURL = request.getParameter("origURL"); %>
					<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Update -->
                    <!-- <input type="hidden" name="origURL" value="%= origURL == null ? "" : origURL %"> -->					
					<input type="hidden" name="origURL" value="<%= origURL == null ? "" : WebStringUtilities.asHTML(origURL)%>">
					<!-- SMedina - 05-11-2015 - Security Vulnerability - End Update -->
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
        <p align="right"><b><font class="wssFontArial2">&copy;
        2007 <a href="http://www.conEd.com">Con Edison</a>.        All Rights Reserved.</font></b></td>
    </tr>
  </table>
</div>
</form>
</body>
</html>
