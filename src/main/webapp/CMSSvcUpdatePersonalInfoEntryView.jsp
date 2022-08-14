<!--*
	* CMSSvcUpdatePersonalInfoEntryView.jsp
	*
	* Created on December 7, 2011, by Jordan Fernandez
	* Part of CorDaptix Web Self Service (10321)
	* Cloned SSvcUpdatePersonalInfoEntryForm.jsp
	* Implements the Action for updating personal information
	* Added processing for super user access. All input fields 
	* (textbox, buttons, checkbox and drow-down menus) are disabled.
	*
	*-->
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.splwg.selfservice.*,
                 java.util.*,
                 java.util.HashMap,
                 java.util.Iterator,
                 java.util.Map"%>
<html>
<head>
<title>Update Personal Information</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>


<script type="text/javascript">
<!--

function SetChecked(check){
if (check.value == "true")
  check.value = "false";
else
  check.value = "true";
}

function phoneFormatNumDigits(fmtString) { 
  var count = 0;
  var i = 0;
  var c = "";
  for (i=0; i<fmtString.length; i++) {
    c = fmtString.charAt(i);
    if (c >= '0' && c <= '9') count++;
  }
  return count;
}

function formatPhoneNumber(element) {
  var inputphone = element.value;
  var outputphone = "";
  var phonedigits = "";
  var i = 0, j = 0;
  var c = "";
  var phoneFormat = "<%=
    ((Properties) application.getAttribute("properties"))
        .getProperty("com.splwg.selfservice.PhoneFormat")%>";
  for (i=0; i<inputphone.length; i++) {
    c = inputphone.charAt(i);
    if (c >= '0' && c <= '9') phonedigits += c;
  }
  if (phonedigits.length == phoneFormatNumDigits(phoneFormat)) {
    for (i=0, j=0; i<phoneFormat.length; i++) {
      c = phoneFormat.charAt(i);
      if (c >= '0' && c <= '9') {
        outputphone += phonedigits.charAt(j++);
      } else {
        outputphone += c;
      }
    }
    element.value = outputphone;
  }
}


// -->
</script>
</head>
<jsp:useBean
  id="SelfService_validUser"
  scope="session"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<jsp:useBean
  id="person"
  scope="request"
  class="com.splwg.selfservice.PersonBean">
</jsp:useBean>
<jsp:useBean
  id="SelfService_country"
  scope="request"
  class="com.splwg.selfservice.CountryBean">
</jsp:useBean>
<jsp:useBean
  id="language"
  scope="session"
  class="com.splwg.selfservice.LanguageBean">
</jsp:useBean>

<%--
--%>
<script>
function CountryChange() {
  var val = document.Form1.country.value;
  document.location.href = "updatepersonalinformation?country=" + val;
}
</script>
<body class="wssBody">
<form name="Form1" action="<%= response.encodeURL("UpdatePersonalInformation") %>" method="post" accept-charset="utf-8">
<input type="hidden" name="_charset_" value="">
<%@include file="ErrorHandler.jsp" %>
<div id="Content" class="wssDivMainPersonalInfo">
  <div class="wssDivLeft">
    <table class="wssTableMajor">
      <tr>
        <td class="wssTD100">
          <table class="wssTableMinor">
            <tr  class="headerRow">
              <td class="label">Account Number</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="SelfService_validUser" property="accountId" /></font></td>
            </tr>
            <tr  class="headerRow">
             <td class="label">Customer Name</td>
              <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="person" property="entityName" /></font></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td width="100%">
      <table class="wssTableMinor2">
            <tr>
        <td class="wssTDMidCenter2"><b><font class="wssFontArial3">
        </font></b></td>
            </tr>
               <tr>
	<td class="wssTDMidLeft"><b><font class="wssFontArial2"><%= SelfService_validUser.getHomePhoneLabel() %></font></b></td>
	<%
	  String homePhone = request.getParameter("homePhone");
	  if (homePhone == null) homePhone = person.getHomePhone();
	  String homePhoneExt = request.getParameter("homePhoneExt");
	  if (homePhoneExt == null) homePhoneExt = person.getHomePhoneExt();
	%>
	<td class="wssTDMidJustify">
		<input type="text" name="homePhone" size="37" maxLength="24" value="<%= homePhone %>"
		onchange="formatPhoneNumber(document.Form1.homePhone);" disabled="true"><b><font
		class="wssFontArial2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Extension &nbsp;</font></b><input type="text"
		name="homePhoneExt" size="6" maxLength="6" value="<%= homePhoneExt %>" disabled="true"></td>
            </tr>
            <tr>
	<td class="wssTDMidLeft"><b><font class="wssFontArial2"><%= SelfService_validUser.getBusinessPhoneLabel() %></font></b></td>
	<%
	  String businessPhone = request.getParameter("businessPhone");
	  if (businessPhone == null) businessPhone = person.getBusinessPhone();
	  String businessPhoneExt = request.getParameter("businessPhoneExt");
	  if (businessPhoneExt == null) businessPhoneExt = person.getBusinessPhoneExt();
	%>
	<td class="wssTDMidJustify"><input type="text"
		name="businessPhone" size="37" maxLength="24" value="<%= businessPhone %>"
		onchange="formatPhoneNumber(document.Form1.businessPhone);" disabled="true"><b>
	<font class="wssFontArial2">&nbsp;&nbsp;&nbsp;&nbsp; Extension &nbsp;</font></b><input type="text"
	name="businessPhoneExt" size="6" maxLength="6" value="<%= businessPhoneExt %>" disabled="true"></td>
            </tr>
            <tr>
	<td class="wssTDMidLeft"><b><font class="wssFontArial2">Phone Format</font></b></td>
	<%
      String phoneFormat = ((Properties) application.getAttribute("properties"))
              .getProperty("com.splwg.selfservice.PhoneFormat");
	%>
	<td class="wssTDMidJustify"><font class="wssFontArial2"><%= phoneFormat %></font></td>
            </tr>
            <tr>
	<td class="wssTDMidLeft"><b><font class="wssFontArial2">E-Mail</font></b></td>
	<%
  	  String email = request.getParameter("email");
	  if (email == null) email = person.getEmail();
	%>
	<td class="wssTDMidJustify"><input type="text"
	name="email" size="63" value="<%= email %>" disabled="true"></td>
            </tr>

            <tr>
	<td class="wssTDMidLeft">&nbsp;</td>
	<td >
    <table width=100% border=0 cellspacing=0 cellpadding=0>
    <tr>
    
	  
	  <td width=50% align=right><b>
	<font class="wssFontArial2">Receive Marketing Info</font></b>
    <%  String receive = "";
        if (request.getParameter("Check1") != null) {
          if (request.getParameter("Check1").equals("true"))
            receive = "RCVE";
          else
            receive = "NRCV";
        } else {
          receive = person.getReceive();
        }
        if (receive.equals("RCVE")) {
     %>
     <input type=hidden name="Check1" id="Check1" value="true">
     <input name="receiveMarketingInfo" type="checkbox" id="receiveMarketingInfo" disabled = "true" onClick="SetChecked(Check1); " checked>
     <% }else { %>
     <input type=hidden name="Check1" id="Check1" value="false">
     <input name="receiveMarketingInfo" type="checkbox" id="receiveMarketingInfo" disabled = "true" onClick="SetChecked(Check1); ">
     <% } %>

     </td></tr></table></td>
            </tr>
	<tr><td class="wssTDMidLeft"><b><font class="wssFontArial2">Mailing Address</font></b></td>
	<%
	  String address1Style = "";
      String address2Style = "";
      String address3Style = "";
      String address4Style = "";
      String houseTypeStyle = "";
      String number1Style = "";
      String number2Style = "";
      String inCityLimitStyle = "";
      String geoCodeStyle = "";
      String countryStyle = "";
      String cityStyle = "";
      String countyStyle = "";
      String stateStyle = "";
      String postalStyle = "";

      if (SelfService_country.isAddress1Available()) {
        address1Style = "visible";
      } else {
        address1Style = "hidden";
      }

      if (SelfService_country.isAddress2Available()) {
        address2Style = "visible";
      } else {
        address2Style = "hidden";
      }

      if (SelfService_country.isAddress3Available()) {
        address3Style = "visible";
      } else {
        address3Style = "hidden";
      }

      if (SelfService_country.isAddress4Available()) {
        address4Style = "visible";
      } else {
        address4Style = "hidden";
      }

      if (SelfService_country.isHouseTypeAvailable()) {
        houseTypeStyle = "visible";
      } else {
        houseTypeStyle = "hidden";
      }
      if (SelfService_country.isNumber1Available()) {
        number1Style = "visible";
      } else {
        number1Style = "hidden";
      }
      if (SelfService_country.isNumber2Available()) {
        number2Style = "visible";
      } else {
        number2Style = "hidden";
      }
      if (SelfService_country.isInCityLimitAvailable()) {
        inCityLimitStyle = "visible";
      } else {
        inCityLimitStyle = "hidden";
      }

      if (SelfService_country.isGeoCodeAvailable()) {
        geoCodeStyle = "visible";
      } else {
        geoCodeStyle = "hidden";
      }

	    if (SelfService_country.isCityAvailable()) {
	      cityStyle = "visible";
	    } else {
	      cityStyle = "hidden";
	    }
	    if (SelfService_country.isCountyAvailable()) {
	      countyStyle = "visible";
	    } else {
	      countyStyle = "hidden";
	    }
	    if (SelfService_country.isStateAvailable()) {
	      stateStyle = "visible";
	    } else {
	      stateStyle = "hidden";
	    }
	    if (SelfService_country.isPostalAvailable()) {
	      postalStyle = "visible";
	    } else {
	      postalStyle = "hidden";
	    }

	  String addr1 = request.getParameter("addr1");
	  if (addr1 == null) addr1 = person.getAddr1();

      String addr2 = request.getParameter("addr2");
	  if (addr2 == null) addr2 = person.getAddr2();

      String addr3 = request.getParameter("addr3");
	  if (addr3 == null) addr3 = person.getAddr3();

      String addr4 = request.getParameter("addr4");
	  if (addr4 == null) addr4 = person.getAddr4();

      String houseType = request.getParameter("houseType");
	  if (houseType == null) houseType = person.getHouseType();

      String number1 = request.getParameter("number1");
	  if (number1 == null) number1 = person.getNumber1();

      String number2 = request.getParameter("number2");
	  if (number2 == null) number2 = person.getNumber2();

      String inCityLimit = request.getParameter("inCityLimit");
	  if (inCityLimit == null) inCityLimit = person.getInCityLimit();

      String city = request.getParameter("city");
	  if (city == null) city = person.getCity();

      String geoCode = request.getParameter("geoCode");
	  if (geoCode == null) geoCode = person.getGeoCode();

      String county = request.getParameter("county");
	  if (county == null) county = person.getCounty();

      String state = request.getParameter("state");
	  if (state == null) state = person.getState();

      String postal = request.getParameter("postal");
	  if (postal == null) postal = person.getPostal();

	  String country = request.getParameter("country");
	  if (country == null) country = SelfService_country.getCountry();
	%>
    
    <tr><td></td><td colspan=2>
    <div id="Content2" class="wssDivAddress">


    <table width=100% border=0 cellspacing=0 cellpadding=0>
<tr>
	<td align=left width=15%><b><font class="label">Country</td>
    	<td align=left width=80%>
		<select name="country" id="country" disabled = "true" onchange=CountryChange();>
                  <% int i = 1;
                   TreeMap map2 = SelfService_country.getCountryList();
                   for (Iterator it = map2.entrySet().iterator(); it.hasNext();) {
                      Map.Entry me = (Map.Entry) it.next();
                  %>
                    <option value="<%= me.getValue() %>" <%= country.equalsIgnoreCase(me.getValue().toString()) ? "selected" : "" %>><%= me.getKey() %>
                  <% i++;
                     }
                  %>
                  </select>
	 </td>
	</tr>
    <% if (address1Style.equals("visible")) { %>
    <tr>
	<td align=left width=15%><b><font class="wssFontArial2"><%= SelfService_country.getAddress1Label() %></font></b></td>
    <td align=left width=80%><input type="text" name="addr1" size="45"
	value="<%= addr1 %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (address2Style.equals("visible")) { %>
    <tr>
	<td align=left width=15%><b><font class="wssFontArial2"><%= SelfService_country.getAddress2Label() %></font></b></td>
    <td align=left width=80%><input type="text" name="addr2" size="45"
	value="<%= addr2 %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (address3Style.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getAddress3Label() %></font></b></td>
    <td align=left width=70%><input type="text" name="addr3" size="45"
	value="<%= addr3 %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (address4Style.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getAddress4Label() %></font></b></td>
    <td align=left width=70%><input type="text" name="addr4" size="45"
	value="<%= addr4 %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (houseTypeStyle.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getHouseTypeLabel() %></font></b></td>
    <td align=left width=70%><input type="text" name="houseType" size="20"
	value="<%= houseType %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (number1Style.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getNumber1Label() %></font></b></td>
    <td align=left width=70%><input type="text" name="number1" size="10"
	value="<%= number1 %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (number2Style.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getNumber2Label() %></font></b></td>
    <td align=left width=70%><input type="text" name="number2" size="10"
	value="<%= number2 %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (inCityLimitStyle.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getInCityLimitLabel() %></font></b></td>
    <td align=left width=70%>
    
    <% if ("true".equals(inCityLimit.trim())) { %>
        <input type="checkbox" name="inCityLimit" size="10"	value="<%=inCityLimit %>" disabled = "true" sonClick="this.value == 'true' ? this.value='false' : this.value = 'true'" checked></td>
    <% } else { %>
    	<input type="checkbox" name="inCityLimit" size="10"	value="<%=inCityLimit %>" onClick="this.value == 'true' ? this.value='false' : this.value = 'true'" disabled = "true"></td>    
   	<% } %>
   	</tr>
    <% } %>

    <% if (cityStyle.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getCityLabel() %></font></b></td>
    <td align=left width=70%><input type="text" name="city" size="20"
	value="<%= city %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (geoCodeStyle.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getGeoCodeLabel() %></font></b></td>
    <td align=left width=70%><input type="text" name="geoCode" size="10"
	value="<%= geoCode %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (countyStyle.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getCountyLabel() %></font></b></td>
    <td align=left width=70%><input type="text" name="county" size="20"
	value="<%= county %>" disabled = "true"></td>
	</tr>
    <% } %>

    <% if (stateStyle.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getStateLabel() %></font></b></td>
    <td align=left width=70%><select name="state" disabled = "true">
                    <option value="">
                  <% HashMap states = SelfService_country.getStates();

                     i = 1;
                     for (Iterator it = states.entrySet().iterator(); it.hasNext();) {
                      Map.Entry mapEntry = (Map.Entry) it.next();
                  %>
                    <option value="<%= mapEntry.getKey() %>" <%= mapEntry.getKey().equals(state) ? "selected" : "" %>><%= mapEntry.getValue()%>
                  <% i++;
                     }
                  %>
                  </select></td>
	</tr>
    <% } %> 
    
    <% if (postalStyle.equals("visible")) { %>
    <tr>
	<td align=left wdth=15%><b><font class="wssFontArial2"><%= SelfService_country.getPostalLabel() %>:</font></b></td>
    <td align=left width=70%><input type="text" name="postal" size="10"
	value="<%= postal %>" disabled = "true"></td>
	</tr>
    <% } %>
    </table></div><td></tr>
    <tr><td colspan=4 align=center>	<input type="submit" value="Update" class="button" disabled = "true">
	</td></tr>
	          </table>
	<input type="hidden" name="step" value="process">
	<input type="hidden" name="previousVersion"
	value="<jsp:getProperty name="person" property="version" />">
	</form>
	        </td>
	      </tr>
	    </table>
	  </div>
	  </div>
<%@ include file="/CMIncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Update Personal Information</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("UpdatePersonalInformation", out, response); %>
<%@ include file="IncBottomPersonalInfo.jsp" %>
</body>
</html>
