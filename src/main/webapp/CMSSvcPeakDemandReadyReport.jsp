<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Peak Demand Report Ready</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>

<%--@ include file="IncPreamble.jsp" --%>
<%@include file="CIMenu.jsp" %>
<%@include file="CMMenu.jsp" %>
<%@include file="Menu.jsp" %>
<style>

.wssFontArial2 {
    font-family: Tahoma, 'MS Sans Serif', sans-serif;
    font-size: 11px;
}
 .wssFontArial3 {
 	font : "Arial";
	font-size : "16";
}

 .wssFontArial5 {
	color: "#002B5D"; 
    font-family: Tahoma, 'MS Sans Serif', sans-serif;
	font-size: "28";
}

 .wssTDMain {
	vertical-align:middle;
 	text-align:center; 
	height:40; 
	width:540;
	color:black;

}
 .wssTDMidCenter {
	vertical-align : middle; 
	text-align : center;
	
}
.button {
    background: #0090DD;
    border: white thin outset;
    color: white;
    font-weight: bold;
    cursor: pointer;
}

/* Alert Error Pop Up*/
.alert-todate {
  visibility: hidden;
  padding:3px 3px 2px 5px;
  background-color: #f44336;
  color: white;
  position: absolute;
  margin-left:10px;
  text-align: left;
}
.alert-todate.active {
  /*Shows pop-up content when "active" class is present */
  visibility: visible;
}

.alert-fromdate {
  visibility: hidden;
  padding:3px 3px 2px 5px;
  background-color: #f44336;
  color: white;
  position: absolute;
  margin-left:10px;
  text-align: left;
}
.alert-fromdate.active {
  /*Shows pop-up content when "active" class is present */
  visibility: visible;
}

.closebtn {
  margin-left: 15px;
  color: white;
  font-weight: bold;
  float: right;
  font-size: 22px;  
  cursor: pointer;
  transition: 0.3s;
  
}

.closebtn:hover {
  color: black;
}

.alert-futuredate {
  visibility: hidden;
  padding:3px 3px 2px 5px;
  background-color: #f44336;
  color: white;
  position: absolute;
  margin-left:10px;
  text-align: left;
}
.alert-futuredate.active {
  /*Shows pop-up content when "active" class is present */
  visibility: visible;
}


</style>


 
</head>


<body>











<%@ include file="IncTop.jsp" %>

<!-- This cell used for Adding title to the page-->
<!--  
<td height="40" width="210" valign="middle" align="center" rowSpan="1" colSpan="1">

</td>
-->

<td class="wssTDMidCenter" height="40" width="540" rowSpan="1" colSpan="1"><!-- valign="middle" align="center"  -->
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
		<table style="" border="0" width="540" align="left" height="" cellspacing="0" cellpadding="0">
			<tr>
				<td style="padding-top: 10px; padding-left: 10px;" width="350">
					<div class="" align="left">
						<table style="border: 2px solid grey; border-collapse: collapse;" width="350" align="left" height="" cellspacing="0"
							cellpadding="0">
							<tr style="border: 2px solid grey;">
								<td style="border: 2px solid grey; width: 110px; padding:2px 2px 2px 5px;">
									<font class="wssFontArial2">
										<b>Account Number </b>
										
								</font>
								</td>

								<td style="border: 2px solid grey; width: 220px; padding:2px 2px 2px 5px;">
									<font class="wssFontArial2" >
										<b>9876543210 </b>
									</font>
								</td>
							</tr>
							<!--End Row- Account Number -->
							<tr style="border: 2px solid grey;">
								<td style="border: 2px solid grey; width: 110px; padding:2px 2px 2px 5px;">
									<font class="wssFontArial2">
										<b>Customer Name</b>
								</font>
								</td>

								<td style="border: 2px solid grey; width: 220px; padding:2px 2px 2px 5px;">
									<font class="wssFontArial2" style="">
									<b>S L GREEN MANAGEMENT LLC</b>
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




<!-- start - Right Side Main Content Heading -->
	<div align="left" >
		<table style="" border="0" width="540" align="left" height="" cellspacing="0" cellpadding="0"><!-- here we provide width=320px bcz we take the width same as above (Acct No+Customer Name)-->
			<tr>
				<td style="padding-top: 50px; padding-left: 10px;"  width="540">
					<b> 
						<font class="wssFontArial5" style="">Your Report is Ready.</font>
					</b>
				</td>
			</tr>
		</table>
	</div>

<!-- End - Right Side Main Content Heading -->

<script type="text/javascript">

</script>
	


<!-- start - Right Side Main Content Below Spinner -->
	<div align="left" >
		<table style="" border="0" width="540" align="left"  cellspacing="0" cellpadding="0"><!-- here we provide width=320px bcz we take the width same as above (Acct No+Customer Name)-->
			<tr>
				<td style=" padding-left: 10px; padding-top:0px;"  width="540">
					<p> 
						<font class="" style="font-family: Tahoma, 'MS Sans Serif', sans-serif;color:#808080;">It will open in a new browser window or tab.<br>
			
						
						</font>
					</p>
				</td>
			</tr>
		</table>
	</div>

<!-- End - Right Side Main Content Below Spinner -->

<!-- start - Right Side Main content Footer -->
	<div align="left" >
		<table style="" border="0" width="540" align="left"  cellspacing="0" cellpadding="0"><!-- here we provide width=320px bcz we take the width same as above (Acct No+Customer Name)-->
			<tr>
				<td style=" padding-left: 10px;padding-top:100px;" valign="middle" width="540">
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
		</table>
	</div>
<!-- End - Right Side Main content Footer -->



	<%@ include file="CMIncIntervalBottom.jsp" %>
</body>
</html>