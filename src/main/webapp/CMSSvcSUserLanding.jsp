<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Log In</title>
<%@ include file="IncPreamble.jsp" %>
</head>
<body  class="wssBody">
<script type="text/javascript">
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
                      <td width="360" class="wssTDMidCenterH50"></td>
                      <td width="90" class="wssTDMidCenterH50"><font class=wssFontArial2><img border="0" src="/SelfService/graphics/Icons.gif" width="200" height="50"></font></td>
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
                                 class="wssFontArial5">Welcome</font></b></td>
                                <td class=wssTDMidCenter height="40" width="210"></td>
                            </tr>
                            <tr>
                              <td class=wssTDMidCenter height="40" width="210"></td>
                              <td class=wssTDMidCenter height="40" width="270"><b><font
                                 class="wssFontArial5"><%=session.getAttribute("suName")%></font></b></td>
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
                     <% String accountName = session.getAttribute("accountName").toString();
					    String userId = session.getAttribute("userId").toString();
                     %>
                  <tr>
                    <td class="label" width="100%" colspan="4" align="center">You are about to view the pages of WSS account owner:</td>
                  </tr>
                  <tr>
                    <td class="label" width="100%" colspan="4" align="center"><%=accountName%></td>
                  </tr>                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
				  <tr>
                    <td width="25%" align="center">&nbsp;</td>
                    <td class="label" width="25%" align="right"></td>
                    <td width="25%" align="left"><font class=wssFontArial2 ><input type="hidden" name="userId" id="userId" maxLength="16" size="14" value="<%=userId%>"></font></td>
                    <td width="25%" align="center">&nbsp;</td>
                  </tr>
                     <% String password = session.getAttribute("password").toString();
                     %>
                  <tr>
                    <td width="25%" align="center">&nbsp;</td>
                    <td class="label" width="25%" align="right"></td>
                    <td width="25%" align="left"><font class=wssFontArial2 ><input type="hidden" name="password" maxLength="12" size="14" value="<%=password%>"></font></td>
                    <td width="25%" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="2" align="right">
                      <input class="button" type="submit" name="Button" value="Continue"   onClick="CheckInput();"></p>
                    </td>
                    <td width="100%" colspan="2" align="left">
                      <input class="button" type="button" name="Button" value="Cancel"   onClick=location.href="sulogin"></p>
                    </td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="center">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="100%" class="normal" colspan="4" align="left"></td>
                  </tr>
                  <tr>
                    <td width="100%" class="normal" colspan="4" align="left"></td>
                  </tr>
                  <tr>
                    <td width="100%" colspan="4" align="left">
	            	<font class="wssFontArial2"></font></td>
                  </tr>
                 </table>
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
