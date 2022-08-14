<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Forgot Your Password</title>
<%@ include file="IncPreamble.jsp" %>
</head>
<jsp:useBean
  id="reminder"
  scope="session"
  class="com.splwg.selfservice.PasswordReminderBean">
</jsp:useBean>
<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Add -->
<%@ page import="com.splwg.selfservice.WebStringUtilities" %>
<!-- SMedina - 05-11-2015 - Security Vulnerability - End Add -->
<%--
--%>
<body class="wssBody">

<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Add -->
<script type="text/javascript" src="/SelfService/jcap/md5.js"></script>
<script type="text/javascript" src="/SelfService/jcap/jcap.js"></script>
<!-- SMedina - 05-11-2015 - Security Vulnerability - End Add -->

<form name="Form1"
action="<%= response.encodeURL("passwordReminder?step=process") %>"
method="post">
<%@include file="ErrorHandler.jsp" %>
<div align="left" >
  <table width="750"  height="400" class=wssTable0>
    <tr>
      <td class=wssTable750TL100>
        <div align="left">
          <table width="750" height="100" class=wssTable0
            <tr>
              <td width="750" valign="top" align="left" height="50">
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
              <td class=wssTable750TL100>
                <div align="left">
                  <table width="750" height="50" class=wssTable0>
                    <tr>
                      <td width="750" class=wssTDMidCenter height="5"><font class=wssFontArial2><img border="0" src="/SelfService/graphics/Line_01.gif" width="750" height="5"></font></td>
                    </tr>
                    <tr>
                      <td width="750" class=wssTDMidCenter height="40">
                        <div align="left">
                          <table width="750" height="40" class=wssTable0>
                            <tr>
                              <td class=wssTDMidCenter height="40" width="210"></td>
                              <td class=wssTDMidCenter height="40" width="370"><b><font
                                 class="wssFontArial5"">Password Reminder</font></b></td>
                                <td class=wssTDMidCenter height="40" width="110"></td>
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
          <table class=wssTableSimple width="750"  >
            <tr>
       
              <td width="746" class=wssTDTopLeft >

                  <table class=wssTableSimple width="100%" >
                    <tr>
                      <td class=wssTDMidCenter4><font class=wssFontArial3 ></font></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                    <tr>
                    <tr>
                      <td colspan=4 class="label" align="center">Please Answer The
                        Hint Question And Press Submit</td>
                    </tr>
                    <tr>
                      <td class=wssTDMidCenter4>&nbsp;</td>
                    </tr>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td align="right" class="label">User ID</td>
                      <td align="left">
                        <input name="userId" type="text" id="userId" size="12" disabled value="<jsp:getProperty name="reminder" property="userId" />">
                        </td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right"><jsp:getProperty name="reminder" property="passwordQuestion" /></td>
                      <td align="left">
						<input name="passwordAnswer" type="text" id="passwordAnswer" size="32" maxLength="60">
                        </td>
                      <td align="center">&nbsp;</td>
                    </tr>
					
					<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Add -->
					<!-- Captcha Field -->
					<tr>
						<!--<td width="25%" align="center" bgcolor=red>&nbsp;</td>-->
						<td class="Captchalabel" colspan="2" align="right" valign="top">Enter word shown below&nbsp;</td>
						<td width="25%" align="left"><font class=wssFontArial2 >
							<script type="text/javascript">sjcap();</script>
						</td>
						<td width="25%" align="center">&nbsp;</td>
					</tr>
					<!-- SMedina - 05-11-2015 - Security Vulnerability - End Add -->
					
                    <tr>
                      <td width="12%" align="center">&nbsp;</td>
                      <td width="32%" align="right">&nbsp;</td>
                      <td width="38%" align="left">&nbsp;</td>
                      <td width="18%" align="center">&nbsp;</td>
                    </tr>
					
                    <tr>
						<td colspan="4" align="center"> 
							<p align="center"> 
								<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Update -->
								<!-- <input class="button" type="submit" name="Submit" value="Submit"> --> 
								<input class="button" type="submit" name="Submit" value="Submit" onClick="if(jcap()){ CheckInput(); }else{return false;};">
								<!-- SMedina - 05-11-2015 - Security Vulnerability - End Update -->
							</p> 
						</td>
                    </tr>
					
                    <tr>
                      <td class=wssTDMidCenter4>&nbsp;</td>
                    </tr>
                    <tr>
                      <td class=wssTDMidCenter4>&nbsp;</td>
                    </tr>
                    <tr>
                      <td class=wssTDMidCenter4><p>&nbsp; </td>
                    </tr>
                  </table>
                <% String origURL = request.getParameter("origURL"); %>
				<!-- SMedina - 05-11-2015 - Security Vulnerability - Start Update -->
					<!-- <input type="hidden" name="origURL" value="%= origURL == null ? "" : origURL %"> -->
					<input type="hidden" name="origURL" value="<%= origURL == null ? "" : WebStringUtilities.asHTML(origURL)%>">
				<!-- SMedina - 05-11-2015 - Security Vulnerability - End Update -->
                </form>
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
</body>
</html>
