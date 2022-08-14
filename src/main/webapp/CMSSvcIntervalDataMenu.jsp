<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.splwg.selfservice.*, java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<!-- http://localhost:8082/SelfServiceUI/CMSSvcIntervalDataMenu -->

<%--@ include file="IncPreamble.jsp" --%>
<%@include file="CIMenu.jsp" %>
<%@ include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>
<style>

 .wssFontArial2 {
    font-family: Tahoma, 'MS Sans Serif', sans-serif;
    font-size: 11px;
}

 .wssFontArial3 {
 	/*font : "Arial";*/
 	 font-family: Tahoma, 'MS Sans Serif', sans-serif;
	font-size : "2px";
}

 .wssFontArial5 {
	color: "#002B5D"; 
        font-family: Tahoma, 'MS Sans Serif', sans-serif;
	font-size: "28px";
}

 .wssTDMain {
	vertical-align:middle;
 	text-align:center; 
	height:40; 
	width:540;
	color:black;

}

/*My styles*/
#reportLink a{
	color:blue;
}

#reportLink a:hover{
	
	color:black;
	
}

.wssIntervalMain{
font-family: Tahoma, 'MS Sans Serif', sans-serif;
font-size:14px;
text-decoration:none;
}
.wssTDLeftLabel{
padding:2px 2px 2px 5px; 
width: 130px; 
border-left: 2px solid grey; 
border-top: 2px solid grey;
border-bottom: 2px solid grey;
}

.wssTDRightLabel{
width: 220px;
background-color:#FFFFFF;
border: 2px solid grey;
}
</style>
</head>

<jsp:useBean
  id="SelfService_validUser"
  scope="session"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>

<body>











<%@ include file="IncTop.jsp" %>

<!-- This cell used for Adding title to the page-->

<td class="wssTDMain" valign="middle" align="center" height="40" width="210">
	<b>
		<font class="wssFontArial5" >Interval Data Reports</font>
	</b>
</td>

<%@ include file="IncLeft.jsp" %>

<!-- The list of links to the other pages should go here. -->
<% DisplayMenu("IntervalDataReports", out, response); %><!-- It is useful for showing current page by heighligting Menu Option with Default Blue Color -->
<%-- DisplayVDERMenu("Pay", out, response); --%> <!-- This Type of menu is also available -->
<%-- DisplayReducedMenu("Pay", out, response); --%> <!-- This Type of menu is also available -->


<%@ include file="CMIncBottom.jsp" %>
<!-- Start - Top Table Start Containing - Acct No + Customer Name -->
	<div align="left">
		<table style="" border="0" width="540" align="left" height=""
			cellspacing="0" cellpadding="0">
			<tr>
				<td style="padding-top: 10px; padding-left: 10px;" width="320">
					<div class="" align="left">


						<table style="border: 2px solid grey; border-collapse: collapse;"
							width="400" align="left" height="" cellspacing="0"
							cellpadding="0">
							<tr style="border: 2px solid grey;">
								<td style="border: 2px solid grey; width: 100px; padding: 2px;">
									<font class="wssFontArial2">
										<b>Account Number</b>
								</font>
								</td>

								<td style="padding:1px;" class="wssTDRightLabel">
									<font class="wssFontArial2" >
										<jsp:getProperty name="SelfService_validUser" property="accountId" />
									</font>
								</td>
							</tr>
							<!--End Row- Account Number -->
							<tr style="border: 2px solid grey;">
								<td style="border: 2px solid grey; width: 100px; padding: 2px;">
									<font class="wssFontArial2">
										<b>Customer Name</b>
								</font>
								</td>

								<td style="padding:1px;" class="wssTDRightLabel">
									<font class="wssFontArial2" style="">
									<jsp:getProperty name="SelfService_validUser" property="entityName" />
								</font>
								</td>
							</tr>
							<!--End Row- Customer Name -->

						</table>
					</div>
				</td><!--Data - Div(Containing - Main Content (Acct No + Customer Name)) -->
			</tr><!--End - Row(Containing - Main Content (Acct No + Customer Name)) -->

		</table><!--End - Table(Containing - Main Content (Acct No + Customer Name)) -->
	</div><!--End - Div(Containing - Main Content (Acct No + Customer Name)) -->

<!-- End - Top Table Start Containing - Acct No + Customer Name -->



	<!-- Tabular Reports 4 hyperlinks Section start-->
	<div align="left">
		<table border="0" width="540"
			align="left" height="" cellspacing="0" cellpadding="0">
			<!--The Content after Section of Acct No + customer Name (Tabular Reports Hyperlinks)-->
			<tr>
				<td id="reportLink" style="padding-top: 50px; padding-left: 10px; padding-bottom: 5px;">

					<font>
						
							<a href="CMSSvcIntervalDataTabular" class="wssIntervalMain">Interval Data Tabular</a>
						
					</font>
				</td>
			</tr>
			<tr>
			<td id="reportLink" style="padding-top: 5px; padding-left: 10px; padding-bottom: 5px;">

					<font>
						
							<a href="CMSSvcPeakDemandTabReportSelect" class="wssIntervalMain">Peak Demand Tabular</a>
						
					</font>
				</td>
			</tr>

			<tr>
				<td id="reportLink" style="padding-top: 5px; padding-left: 10px; padding-bottom: 5px;">

					<font > 
						 
							<a href="CMSSvcIntervalDataChart" class="wssIntervalMain">Interval Data Charts (15min, hour, day)</a>
						
					</font>
				</td>
			</tr>

			<tr>
				<td id="reportLink" style="padding-top: 5px; padding-left: 10px; padding-bottom: 5px;">

					<font  >
						
							<a href="CMSSvcPeakDemandChart" class="wssIntervalMain">Interval Data Charts With Peak Demand</a>
						
					</font>
				</td>
			</tr>

			<tr>
				<td style="padding-top: 90px; padding-left: 10px; padding-bottom: 20px;">
					<p>
						<font class="wssFontArial2">
						<small>
							If you have any questions, please contact us at 212-460-2011 <br>
							or email to <a href="mailto:steamsales@coned.com">steamsales@coned.com</a>
						</small>
						
						</font>
					</p>
				</td>
			</tr>
		
		
		</table><!-- End Table - Tabular Reports 4 hyperlinks Section -->
	</div><!-- End div - Tabular Reports 4 hyperlinks Section -->


















	<%@ include file="CMIncIntervalBottom.jsp" %>
</body>
</html>