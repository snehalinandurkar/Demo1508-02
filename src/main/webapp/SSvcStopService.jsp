<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List,
                 java.util.Iterator,
                 java.util.ArrayList,
                 com.splwg.selfservice.ServiceAgreementBean"%>
<html>
<head>

<title>Disconnect Service</title>
<%@include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>

</head>
<jsp:useBean
  id="accountInfo"
  scope="request"
  class="com.splwg.selfservice.ControlCentralBean">
</jsp:useBean>
<jsp:useBean
  id="saList"
  scope="request"
  class="com.splwg.selfservice.SAforAccountBean">
</jsp:useBean>

<%--
--%>
<body class="wssBody">
<form name="Form1" method="post" action="<%= response.encodeURL("stopService?step=process") %>">
<script type="text/javascript">

    function CheckInput() {
       var ret = convertLocalDateToInternal();
       if (ret != "error")
         document.Form1.submit();
    }

    function convertLocalDateToInternal() {
        var ret;
        var format = document.Form1.DateFormat.value;
        localDate = trimString(document.Form1.StopDate.value);
        if (! localDate) return ''
        var year, month, day
        if (format == 'ddMMyyyy' || format == 'MMddyyyy') {
            var result = localDate.match(/^(\d{2})(\d{2})(\d{4})$/)
            if (! result) result = localDate.match(/^(\d{1,2})\D+(\d{1,2})\D+(\d{1,4})$/)
            if (! result) return -1
            year = result[3] - 0
            if (format == 'MMddyyyy') {
                month = result[1] - 0
                day = result[2] - 0
            } else {
                month = result[2] - 0
                day = result[1] - 0
            }
        } else if (format == 'dd-MM-yyyy' || format == 'dd/MM/yyyy' || format == 'MM/dd/yyyy' || format == 'MM-dd-yyyy') {
            year = localDate.substr(6,4)
            if (format == 'MM/dd/yyyy' || format == 'MM-dd-yyyy') {
                month = localDate.substr(0,2)
                day = localDate.substr(3,2)
            } else {
                month = localDate.substr(3,2)
                day = localDate.substr(0,2)
            }
        } else if (format == 'yyyyMMdd' || format == 'yyyyddMM') {
            var result = localDate.match(/^(\d{4})(\d{2})(\d{2})$/)

            if (! result) result = localDate.match(/^(\d{1,4})\D+(\d{1,2})\D+(\d{1,2})$/)
            if (! result) return -1
            year = result[1] - 0
            if (format == 'yyyyMMdd') {
                month = result[2] - 0
                day = result[3] - 0
            } else {
                month = result[3] - 0
                day = result[2] - 0
            }
        } else {
            window.alert("Invalid Date Format definition. Please turn to customer support");
            ret = "error";
        }

/*        if (year < 100) {
            if (year < 80) {
                year += 2000
            } else {
                year += 1900
            }
          }
*/

        if (! validateDateComponents(year, month, day)) {
            window.alert("Please enter a stop date in the format " + format);
            ret = "error";
        }

        return ret;
    }

    function trimString(object) {
        if (object != null && object.constructor == String) {
            object = object.replace(/^\s+/, '');    // trim leading blanks
            object = object.replace(/\s+$/, '');    // trim trailing blanks
        }
        return object;
    }


    function validateDateComponents(year, month, day) {
        if (year < 1000) return false
        if (day < 1) return false
        if (month < 1 || month > 12) return false

        if (month == 2) {
            var isLeapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
            // Check for leap year
            if (isLeapYear) {
                if (day > 29) return false
            } else {
                if (day > 28) return false
            }
            return true
        }

        if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
            if (day > 30) return false
        } else {
            if (day > 31) return false
        }
        return true
    }

    function buildInternalDate(year, month, day) {
        var monthString = month < 10 ? '0' + month : '' + month
        var dayString = day < 10 ? '0' + day : '' + day
        return year + '-' + monthString + '-' + dayString
    }

    function SetChecked(check){
    if (check.value == "true")
      check.value = "false";
    else
      check.value = "true";
    }

    function SetRadio(radio){
    if (radio.value == "true") {
      radio.checked = false;
      radio.value = "false";
    } else {
      radio.checked = true;
      radio.value = "true"
    }

}

</script>
<div id="Content" class="wssDivMain">
  <table class="wssTableMajor">
    <tr>
      <td class="wssTD100">
        <table class="wssTableMinor">
          <tr class="headerRow">
            <td class="label">Account Number:</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="accountId" /></font></td>
          </tr>
          <tr class="headerRow">
            <td class="label">Customer Name:</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
		<jsp:getProperty name="accountInfo" property="entityName" /></font></td>
          </tr>
        </table>
      </td>
    </tr>
    <%
      List list = saList.getSAList();
      int index;
      String serviceDesc;
//      String address;
      String radioName;
      String saName;
      String serviceName;
      String addressName;
      String startName;
      String endName;
      String checkedName;
      ArrayList arr = new ArrayList();
      int j = 0;

      arr.clear();

      int i=1;
      if (list.size() > 0) { %>
    <tr>
      <td width=95%>
        <div id="Content2" class="wssDivSub">
          <table class="table#dataTable" width=98% border="1" cellpadding=0 cellspacing=0>
        <table width="100%">
        <tr class='gridLabel'>
          <td class="gridTd" width="7%">Stop </td>
          <td class="gridTd" width="30%" align=left>Address</td>
          <td class="gridTd" width="18%" align=left>Service</td>
          <td class="gridTd" width="18%" align=left>Start</td>
          <td class="gridTd" width="27%" align=left>End</td>
        </tr>

      <%
      String gridValue="gridAlt";
      for (Iterator it = list.iterator(); it.hasNext();) {

    	if (gridValue == "grid")
  	    gridValue = "gridAlt";
    	else
	    gridValue = "grid";

          ServiceAgreementBean sa = (ServiceAgreementBean) it.next();
          if (arr.contains(sa.getSAId()))
            continue;
          else
            arr.add(sa.getSAId());

          if (!sa.getAddress().equals("")) {
            j++;
            serviceDesc = sa.getServiceDesc();
            index = serviceDesc.indexOf("/");
            if (index > -1) {
               serviceDesc = serviceDesc.substring(index + 2);
            }
            String address = sa.getAddress();
            index = address.indexOf("-");
            if (index > -1) {
              address = address.substring(0, index - 1);
            }
            radioName = "Radio" + i;
            saName = "SA" + i;
            checkedName = "Checked" + i;
            serviceName = "Service" + i;
            addressName = "Address" + i;
            startName = "Start" + i;
            endName = "End" + i;
      %>

    <tr class='<%= gridValue %>'>
      <td class="wssTDMidCenterF" width=5%><input type=hidden name="<%= checkedName %>" id="<%= checkedName%>" value="false">
          <input type="radio" name="<%= radioName%>" id="<%= radioName %>" onClick="SetChecked(<%= checkedName %>); SetRadio(<%= radioName %>);" value="false">
          <input type=hidden name="<%= saName %>" id="<%= saName%>" value="<%= sa.getSAId()%>">
      </td>

      <td class="gridTd" align=left width=39%><%= address %>&nbsp;<input type=hidden name="<%= addressName %>" id="<%= addressName%>" value="<%= address %>"></td>
      <td class="gridTd" align=left width=20%><%= serviceDesc %>&nbsp;<input type=hidden name="<%= serviceName %>" id="<%= serviceName%>" value="<%= serviceDesc %>"></td>
      <td class="gridTd" align=left width=18%><%= sa.getStartDate()%>&nbsp;<input type=hidden name="<%= startName %>" id="<%= startName%>" value="<%= sa.getStartDate() %>"></td>
      <td class="gridTd" align=left width=18%><%= sa.getEndDate()%>&nbsp;<input type=hidden name="<%= endName %>" id="<%= endName%>" value="<%= sa.getEndDate() %>"></td>
    </tr>
  <% i++;
      }
    }
  i = i -1;%>
          </table>
        </div>
      </td>
    </tr>

  </table>
  <% if (j > 0) { %>
  <table class=wssTableMinor2 >
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
      <td width="98%" class="label" align="left">&nbsp;Stop Service On This Date&nbsp;
      
          <input name="StopDate" type="text" value="<%= saList.getToday() %>">
          <input name="DateFormat" id="DateFormat" type=hidden value="<%= saList.getDateFormat() %>">
        

          <input class="button" type="button" name="Button" value="Stop" onClick="CheckInput();">
        <input type="hidden" name="SACount" id="SACount" value="<%= i %>">
        </td>
    </tr>
  </table>
   <%  } else { %>
      <table width="100%">
      <tr>
        <td width="100%" align=center><font color=red><strong>No Service Agreements Exist</strong></font></td>
      </tr>
      </table>
  <% } %>
   <%  } else { %>
      <table width="100%">
      <tr>
        <td width="100%" align=center><font color=red><strong>No service agreements are eligible to be stopped </strong></font></td>
      </tr>
      </table>
  <% } %>
</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">Stop Service</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu("StopService", out, response); %>
<%@ include file="IncBottom.jsp" %>
</form>
</body>
</html>
