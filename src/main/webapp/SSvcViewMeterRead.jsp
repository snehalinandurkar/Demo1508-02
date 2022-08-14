<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.splwg.selfservice.*" %>
<html>
<head>
<title>View Meter Read</title>
<%@ include file="IncPreamble.jsp" %>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>

<% String saId="";
   String badgeNbr=""; %>
</head>
<jsp:useBean
  id="viewMeterRead"
  scope="request"
  class="com.splwg.selfservice.ViewMeterReadBean">
</jsp:useBean>
<jsp:useBean
  id="SelfService_validUser"
  scope="session"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>
<body class="wssBody">
<form name="Form1" method="post" action="<%= response.encodeURL("enterMeterRead") %>">
<%@include file="ErrorHandler.jsp" %>
<script type="text/javascript">
function Jump(field){
  var sp = field.value;
  var sa = document.Form1.saName.value;
  document.location.href = "viewMeterRead?spId=" + sp + "&saId=" + sa;

}
</script>

<div id="Content" class="wssDivMainLarge">
  <table class="wssTableMajor">
    <tr>
      <td class="wssTD100">
        <table class="wssTableMinor">
          <tr class="headerRow">
            <td class="label">Account Number</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
				<jsp:getProperty name="SelfService_validUser" property="accountId" /></font></td>
          </tr>
          <tr class="headerRow">
            <td class="label">Customer Name</td>
            <td class="wssTDWhiteMidLeft"><font class="wssFontArial2">
				<jsp:getProperty name="SelfService_validUser" property="entityName" /></font></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr/>
          
    <tr>
      <td width=100%>
        <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1" >
	<caption class="label" align="center"><font size=3>Service Point Info</font></caption>
	
	<%
        List list = viewMeterRead.getSPList();
        String spName;
	String gridValue="gridAlt";
        int j=1;
		for (Iterator it = list.iterator(); it.hasNext();) {
		    ServicePointBean sp = (ServicePointBean) it.next();
            if (sp.getSelected() == "true" || list.size() == 1){
              saId = sp.getSAId();
              badgeNbr = sp.getBadgeNbr();
	    if (gridValue == "grid")
		gridValue = "gridAlt";
    	    else
		gridValue = "grid";
        %>
          <tr class="<%= gridValue %>" >
            <td width="5%" class="gridTd"><input type=radio NAME=Radio1 id=Radio1 checked></td>
            <td width=75% class="gridTd">&nbsp;<%= sp.getSPInfo() %>&nbsp;</td>
         </tr>
         <input type=hidden name="saName" id="saName" value="<%= saId %>">
        <% } else {
               spName = "spName" + j;
		%>
          <tr class="<%= gridValue %>">
            <td width="5%" bgcolor="white"><input TYPE=RADIO name=Radio1 id=Radio1  onClick="Jump(<%= spName %>);"></td>
            <td class="wssTDMidCenterF"><font class="wssFontArial2">&nbsp;<%= sp.getSPInfo() %>&nbsp;<input type=hidden name="<%= spName %>" id="<%= spName %>" value="<%= sp.getSPId() %>"></td>
          </tr>
		<% }
            j++;
        }
        %>
        </table>
      </td>
    </tr>
    <tr></tr>
    <tr>
      <td width="100%">
	   <table width=98% class="table#dataTable" cellpadding="0" cellspacing="0" border="1">        		
		<caption class="label" align="center"><font size=3>Registers for Meter <%= badgeNbr %></font></caption>
          	<tr class="gridLabel">
            		<td width="10%" class="gridTd">Sequence</td>
            		<td width="10%" class="gridTd">UOM</td>
            		<td width="10%" class="gridTd">TOU</td>
            		<td width="25%" class="gridTd">Date/Time Last Read</td>
            		<td width="9%" class="gridTd">Last Reading</td>
            		<td width="25%" class="gridTd">Date/Time New Read</td>
            		<td width="11%" class="gridTd">New Reading</td>
          	</tr>
<%
	List list2 = viewMeterRead.getRegisterList();
        int i;
        String readName;
        String regName;
	gridValue = "gridAlt";
        i = 0;
	for (Iterator it2 = list2.iterator(); it2.hasNext();) {
	    MeterRegisterBean reg = (MeterRegisterBean) it2.next();
            i++;
            readName = "Read" + i;
            regName = "RegisterId" + i;
	    if (gridValue == "grid")
		gridValue = "gridAlt";
    	    else
		gridValue = "grid";
%>
          <tr class="<%= gridValue %>" align="center" valign="top">
            <td width="10%" class="gridTd"><%= reg.getReadSequence() %>&nbsp;</td>
            <td width="10%" class="gridTd"><%= reg.getUOM() %>&nbsp;</td>
            <td width="10%" class="gridTd"><%= reg.getTOU() %>&nbsp;<input name="SAId" id="SAId" type="hidden" value="<%= saId %>"><input name="MeterConfigurationId" id="MeterConfigurationId" type="hidden" value="<%= reg.getMeterConfigurationId() %>"><input name="<%= regName %>" id="<%= regName %>" type="hidden" value="<%= reg.getRegisterId() %>"> </td>
            <td width="25%" class="gridTd">&nbsp;<%= reg.getReadDateTime() %>&nbsp;</td>
            <td width="9%" class="gridTd"><%= reg.getRegisterRead() %>&nbsp;</td>
            <td width="25%" class="gridTd"><%= viewMeterRead.dateString %>&nbsp;</td>
            <td width="11%" class="gridTd">
            	<input name="<%= readName %>" id="<%= readName %>" type="text" size="10">
            </td>
          </tr>
<%
      }
 %>
        </table>  <table width="100%">
        <tr>
            <td align="center">
                <input type="submit" name="Submit" value="Add Meter Read" class="button">
                <input type="hidden" name="RegCount" value="<%= i %>">
            </td>
       </tr>
      </td>
    </tr>
  </table>
  </table>
  &nbsp;</div>
<%@ include file="IncTop.jsp" %>
<td class="wssTDMain"><b><font class="wssFontArial5">View / Add Meter Reads</font></b></td>
<%@ include file="IncLeft.jsp" %>
<% DisplayMenu(" ", out, response); %>
<%@ include file="IncBottomLow.jsp" %>
</form>
</body>
</html>
