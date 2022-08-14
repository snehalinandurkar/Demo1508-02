<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.splwg.selfservice.*,
                 java.util.*"%>
<html>
<head>
<title>Customer Registration</title>
<%@ include file="IncPreamble.jsp" %>
</head>
<!-- Start Add - 2015-05-12 - MCeripul - Security Fix -->
<script type="text/javascript" src="/SelfService/jcap/md5.js"></script>
<script type="text/javascript" src="/SelfService/jcap/jcap.js"></script>
<!-- End Add - 2015-05-12 - MCeripul - Security Fix -->
<%--
--%>
<body class="wssBody">
<form name="Form1"
action="<%= response.encodeURL("registration?step=process") %>"
method="post" accept-charset="utf-8">
<input type="hidden" name="_charset_" value="">
<script type="text/javascript">
function SetChecked(check){
if (check.value == "true")
  check.value = "false";
else
  check.value = "true";
}
function SetCheckBox(radio){
if (radio.checked == true) {
  radio.checked = false;
} else {
  radio.checked = true;
}
}

function CountryChange() {
  var val = document.Form1.country.value;
  document.location.href = "registration?country=" + val;
}

function CheckInput() {
  if (document.Form1.accountId.value == "") {
    window.alert("Please enter an account id");
  } else if (document.Form1.accountId.value.length > 10) {
    window.alert("Account ID too long.");
  } else if (document.Form1.lastName.value == "") {
    window.alert("Please enter a last name");
  } else {
    document.Form1.submit();
  }
}

function Cancel() {
    location.href="/SelfService";
}

</script>
<div align=left>
  <table width="750" height="400" class=wssTable10 >
    <tr>
      <td width="750" class=wssTable0 height="100" >
        <div align="left">
          <table class=wssTable0 width="750" height="100">
            <tr>
              <td width="750" class=wssTDTopLeft height="50">
                <div align="left">
                  <table class=wssTable0 width="750" height="50">
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
                              <td class=wssTDMidCenter height="40" width="370"><b><font
                                 class="wssFontArial5">Customer Registration</font></b></td>
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
      <td width="750" class=wssTDTopLeft height="279">
        <div class="wssDivReg" align="left">
          <table width="750" >
            <tr>
              <td width="4" class=wssTDTopLeft><img border="0" src="/SelfService/graphics/Button_03.gif" width="4" height="5"></td>
              <td width="746" class=wssTDTopLeft>
                 <table width="100%" >
                    <tr><td>&nbsp;</td></tr>

                    <tr>
                      <td colspan="2" class=label align="center"><b>
                        <%@include file="ErrorHandler.jsp" %>
                        Please Enter The Following Data:</b></td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                </table>
                <table width=100%>
                     <% String accountId = request.getParameter("accountId");
                       if (accountId == null) accountId = "";
                     %>
                    <tr>
                      <td align="center" >&nbsp;</td>
                      <td class="label" align="right" >Account Id</td>
                      <td align="left" >
                        <!-- Start Change - 2015-05-12 - MCeripul - Security Fix -->
                        <!-- <input name="accountId" type="text" id="accountId" size="10" value="%= accountId %"> -->
                        <input name="accountId" type="text" id="accountId" size="10" value="<%= WebStringUtilities.asHTML(accountId) %>">
                        <!-- End Change - 2015-05-12 - MCeripul - Security Fix -->
                      </td>
                      <td align="center">&nbsp;</td>
                    </tr>
                     <% String firstName = request.getParameter("firstName");
                       if (firstName == null) firstName = "";
                     %>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td align="right" class="label" >First Name</td>
                      <td align="left">
                        <!-- Start Change - 2015-05-12 - MCeripul - Security Fix -->
                        <!-- <input name="firstName" type="text" id="firstName" size="20" value="%= firstName %"> -->
                        <input name="firstName" type="text" id="firstName" size="20" value="<%= WebStringUtilities.asHTML(firstName) %>">
                        <!-- End Change - 2015-05-12 - MCeripul - Security Fix -->
                       </td>
                       <td align="center">&nbsp;</td>
                    </tr>
                     <% String lastName = request.getParameter("lastName");
                       if (lastName == null) lastName = "";
                     %>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right" >Last (Business) Name</td>
                      <td align="left">
                        <!-- Start Change - 2015-05-12 - MCeripul - Security Fix -->
                        <!-- <input name="lastName" type="text" id="lastName" size="20" value="%= lastName %"> -->
                        <input name="lastName" type="text" id="lastName" size="20" value="<%= WebStringUtilities.asHTML(lastName) %>">
                        <!-- End Change - 2015-05-12 - MCeripul - Security Fix -->
                       </td>
                       <td align="center">&nbsp;</td>
                    </tr>
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td class="label" align="right" >Receive
                        Marketing Info
		      </td>
			<input type=hidden name="receiveMktChk" id="receiveMktChk" value="true">
                      <td align="left"><input name="receiveMarketingInfo" type="checkbox" id="receiveMarketingInfo" onClick="SetChecked(receiveMktChk); " checked></td>
                      <td align="center">&nbsp;</td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr></tr>
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
                    <tr>
                      <td align="center">&nbsp;</td>
                      <td colspan="2" align="center"> <p >
                            <!-- Start Change - 2015-05-12 - MCeripul - Security Fix -->
                            <!-- <input type="button" name="Button" value="Submit" class="button" onClick="CheckInput();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
                            <input type="button" name="Button" value="Submit" class="button" onClick="if(jcap()){ CheckInput(); }else{return false;};">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <!-- End Change - 2015-05-12 - MCeripul - Security Fix -->
                            <input type="button" name="CancelButton" id="CancelButton" value="Cancel" class="button" onClick="Cancel();">
                        </p></td>
                        <td align="center">&nbsp;</td>
                    </tr>
                    <tr>
                      <td colspan="2" align="center">&nbsp;</td>
                    </tr>
                  </table>
                </form>
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
</body>
</html>
