<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="com.splwg.selfservice.*, java.util.*,
                 java.util.TreeMap,
                 java.util.Iterator,
                 java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Peak Demand Charts</title>

  <script src="/SelfServiceUI/static/jquery-3.6.min.js"></script>


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
.button {
    background: #0090DD;
    border: white thin outset;
    color: white;
    font-weight: bold;
    cursor: pointer;
}



</style>


 
</head>


<jsp:useBean
  id="SelfService_validUser"
  scope="session"
  class="com.splwg.selfservice.ValidUserBean">
</jsp:useBean>


<jsp:useBean
  id="BillPeriod"
  scope="request"
  class="com.splwg.selfservice.CMBillPeriodBean">
</jsp:useBean>

<jsp:useBean
  id="ChartBean"
  scope="session"
  class="com.splwg.selfservice.CMDataIntervalChartBean">
</jsp:useBean>

<body>



<%@ include file="IncTop.jsp" %>

<!-- This cell used for Adding title to the page-->
<!--  
<td height="40" width="210" valign="middle" align="center" rowSpan="1" colSpan="1">

</td>
-->

<td class="wssTDMidCenter" height="40" width="540" rowSpan="1" colSpan="1"><!-- valign="middle" align="center"  -->
	<b>
		<font class="wssFontArial5" >Peak Demand Tabular Report</font>
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
				<td style="padding-top: 10px; padding-left: 10px;" width="380">
					<div class="" align="left">
						<table style="border: 2px solid grey; border-collapse: collapse;" width="380" align="left" height="" cellspacing="0"
							cellpadding="0">
							<tr style="border: 2px solid grey;">
								<td style="border: 2px solid grey; width: 75px; padding:2px 2px 2px 5px;">
									<font class="wssFontArial2">
										<b>Account Number </b>
										
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
								<td style="border: 2px solid grey; width: 75px; padding:2px 2px 2px 5px;">
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


<!-- start - Right Side Main Content Heading -->
	<div align="left" >
		<table style="" border="0" width="320" align="left" height="" cellspacing="0" cellpadding="0"><!-- here we provide width=320px bcz we take the width same as above (Acct No+Customer Name)-->
			<tr>
				<td style="padding-top: 20px; padding-left: 10px;" class="wssTDMain" valign="middle" align="center" height="40" width="320">
					<b> 
						<font class="wssFontArial5">Peak Demand Tabular Report</font>
					</b>
				</td>
			</tr>
		</table>
	</div>

<!-- End - Right Side Main Content Heading -->



<!-- Start - Selecting Reporting Period Screen Section-->
<form name="Form1" action="<%= response.encodeURL("IntervalDataTabular") %>" method="post" accept-charset="utf-8">
<script type="text/javascript">
function validateFromDate()
{
	console.log("fromDate");
	var date = document.getElementById("fromDate").value;
	console.log(date);
	
	//Here we check whether the user provided the date or not
	if(date.length==0){
		return;
	}
	
	//convert 'date'(string) to 'Date'(object)
	
     var fromdate =new Date(date);
  
	// Extract pieces of the date:
    var month = fromdate.getMonth(); // months start counting from zero!
    var day   = fromdate.getDate();  
    var year  = fromdate.getFullYear();
    console.log("year from date");
    console.log(year);
   
   
    if(isNaN(year)){
    	//isValidFromDateError();
    	window.alert("This is not a valid date");
    	document.getElementById("fromDate").value = "yyyy-MM-dd";
    }
   
    if(fromdate=="Invalid Date" )//|| isNaN(year)
    	{
    	//pop-up 
    	//isValidFromDateError();
    	window.alert("This is not a valid date");
    	document.getElementById("fromDate").value = "yyyy-MM-dd";
    	}
    else
    {	 
    	let isPopUp = isValidYear(year);
    	console.log(isPopUp)
    	if(isPopUp==false)
    	{
    		//pop up
    		//isValidFromDateError();
    		window.alert("This is not a valid date");
    		document.getElementById("fromDate").value = "yyyy-MM-dd";
    	}
    	else{
    		
    		//This Condition is checked when year is valid but it's in the Future
    		var present = new Date();
    		console.log(present);
    		
    		let presentYear  = present.getFullYear();
    		let fromDateYear   = fromdate.getFullYear();

    		if(fromDateYear>presentYear){
    			//isFutureFromDateError();
    			window.alert("Date cannot be in the future.");
    	    	document.getElementById("fromDate").value = "yyyy-MM-dd";
    			
    		}
    		 		
    	}
    }
	
	
	
}

function validateToDate()
{
	console.log("toDate");
	var date = document.getElementById("toDate").value;
	console.log(date);
	
	//Here we check whether the user provided the date or not
	if(date.length==0){
		return;
	}
	
	//convert 'date'(string) to 'Date'(object)
     var todate =new Date(date);
     console.log(todate);
	// Extract pieces of the date:
    var month = todate.getMonth(); // months start counting from zero!
    var day   = todate.getDate();  
    var year  = todate.getFullYear();
    
    if(isNaN(year)){
    	//isValidToDateError();
    	window.alert("This is not a valid date");
		document.getElementById("toDate").value = "yyyy-MM-dd";
    }
    
    if(todate=="Invalid Date")
    	{
    	//pop-up 
    
    	//isValidToDateError();
    	window.alert("This is not a valid date");
		document.getElementById("toDate").value = "yyyy-MM-dd";
    	}
    else
    {
    	let isPopUp = isValidYear(year);
    	if(isPopUp==false)
    	{
    		//pop up
    		//isValidToDateError();
    		window.alert("This is not a valid date");
    		document.getElementById("toDate").value = "yyyy-MM-dd";
    	}
    	else{
    		
    		//This Condition is checked when year is valid but it's in the Future
    		var present = new Date();
    		console.log(present);
    		
    		let presentYear  = present.getFullYear();
    		let toDateYear   = todate.getFullYear();

    		if(toDateYear>presentYear){
    			//isFutureToDateError();
    			window.alert("Date cannot be in the future.");
    	    	document.getElementById("toDate").value = "yyyy-MM-dd";
    		}
 		
    	}
    	
    }
	
	
	
}

function isValidYear(year)
{	

	if((year.toString().length==4) && (year!=0))
	{
		return true;		
	}
	else
	{
		return false;
	}
	
}

function Cancel() {
    location.href="/SelfService";
}

function resetFromDate(){
	console.log("from");
	document.getElementById("fromDate").value = "yyyy-MM-dd";//this will reset 'fromDate' form field to it's default value eg.dd-mm-yyyy
}

function resetToDate(){
	
	document.getElementById("toDate").value = "yyyy-MM-dd";//this will reset 'toDate' form field to it's default value eg.dd-mm-yyyy
}

/*
function isValidFromDateError(){
		$(document).ready(function(){

			//appends an "active" class to .popup and .popup-content 
		    jQuery(".alert-fromdate").addClass("active");
			
		  //removes the "active" class to .popup and .popup-content 
			jQuery(".close").on("click", function() {
			jQuery(".alert-fromdate").removeClass("active");
		});

		});
	}

function isValidToDateError(){
		$(document).ready(function(){

			//appends an "active" class to .popup and .popup-content 
		    jQuery(".alert-todate").addClass("active");
			
		  //removes the "active" class to .popup and .popup-content 
			jQuery(".close").on("click", function() {
			jQuery(".alert-todate").removeClass("active");
		});

		});
}

function isFutureFromDateError(){
	$(document).ready(function(){

		//appends an "active" class to .popup and .popup-content 
	    jQuery(".alert-futurefromdate").addClass("active");
		
	  //removes the "active" class to .popup and .popup-content 
		jQuery(".close").on("click", function() {
		jQuery(".alert-futurefromdate").removeClass("active");
	});

	});
}

function isFutureToDateError(){
	$(document).ready(function(){

		//appends an "active" class to .popup and .popup-content 
	    jQuery(".alert-futuretodate").addClass("active");
		
	  //removes the "active" class to .popup and .popup-content 
		jQuery(".close").on("click", function() {
		jQuery(".alert-futuretodate").removeClass("active");
	});

	});
}
*/
function resetFutureFromDate(){
	console.log("from");
	document.getElementById("fromDate").value = "yyyy-MM-dd";//this will reset 'fromDate' form field to it's default value eg.dd-mm-yyyy
}


function resetFutureToDate(){
	console.log("to");
	document.getElementById("toDate").value = "yyyy-MM-dd";//this will reset 'fromDate' form field to it's default value eg.dd-mm-yyyy
}

/* This function gets called when user selects bill period and then it map 'billPeriodStartDate' <->'from date' and 'billPeriodEndDate' <->'End date' */
function updateFromAndToDate(){
	console.log("bill");
	
	let billPeriodDates = document.getElementById("billPeriodDate").value;
	
	console.log(billPeriodDates);
	let billS = "2021-10-15"; // date must be set via 'yyyy-mm-dd' format
	let billE = "2021-10-30"; // date must be set via 'yyyy-mm-dd' format
	document.getElementById("fromDate").value = billS;
	document.getElementById("toDate").value  =  billE;
	
	
}


</script>
	<div align="left">
		<table style="" border="0" width="540" align="left" height="" cellspacing="0" cellpadding="0">
			<tr>
				<td style="padding-top: 10px; padding-left: 10px;" width="350">
					<div class="" align="left">
						<table style="border-collapse:seperate;border-spacing:0px 2px;" width="350" align="left" height="" cellspacing="0"
							cellpadding="0">
							
							<tr>
								<td class="wssTDLeftLabel">
									<font class="wssFontArial2">
										<b>Data Unit</b>
									</font>
								</td>

								<td class="wssTDRightLabel" style="padding:2px 2px 2px 5px;">
									<font class="wssFontArial2" >
										Mass Flow(lb/hr)
									</font>
								</td>
							</tr>
							<!--End Row- Data Unit -->
							
							<!--End Row- Granularity  -->
							
							<tr>
								<td class="wssTDLeftLabel">
									<font class="wssFontArial2">
										<b>From Date</b>
									</font>
								</td>

								<td class="wssTDRightLabel">
									<input type="date" onBlur="validateFromDate()" name="FromDate" id="fromDate" style="width: 230px; border:none;" class="wssFontArial2" />
								</td>
								
							</tr>
							<!--End Row- From Date  -->
							
							<tr>
								<td class="wssTDLeftLabel">
									<font class="wssFontArial2">
										<b>To Date</b>
									</font>
								</td>

								<td class="wssTDRightLabel">
									<input type="date" onBlur="validateToDate()" name="ToDate" id="toDate" style="width: 230px; border:none;" class="wssFontArial2" />
								</td>
							
							</tr>
							<!--End Row- To Date  -->
							
							<tr style="">
								<td class="wssTDLeftLabel">
									<font class="wssFontArial2">
										<b>Bill Period</b>
									</font>
								</td>

								<td class="wssTDRightLabel">
									
										
										<select name="billPeriod" id="billPeriodDate" onChange="updateFromAndToDate()" style="width:230px;border:none;" class="wssFontArial2"><!-- style="width:220px;border:none; -->
												
												
											
												
												
											<option style="display:none">
												<%for ( int fontSize = 1; fontSize <= 3; fontSize++){ %>
													<option value="10/15/2021-10/30/2021" class="wssFontArial2"><span >10/15/2021</span>&nbsp; - &nbsp; <span>10/30/2021</span></option>
														
												<%}%>								
										</select>
								</td>
								
							</tr>
							<!--End Row- Bill Period  -->
							
							
 						<tr>                     		
                      		<td colspan="2" align="center"> 
                      			<p>							
									 <input type="submit" name="Button" value="Open Report" class="button" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <!-- onClick="if(jcap()){ CheckInput(); }else{return false;};" -->
									 <input type="reset" name="CancelButton" id="CancelButton" value="Cancel" class="button" onClick="Cancel();">
								</p>
							</td>
							
						</tr>	
						<!-- End Row - Button -->
						</table>
					</div>
				</td><!--Data - Div(Containing - Main Content (Acct No + Customer Name)) -->
			</tr><!--End - Row(Containing - Main Content (Acct No + Customer Name)) -->

		</table><!--End - Table(Containing - Main Content (Acct No + Customer Name)) -->
	</div><!--End - Div(Containing - Main Content (Acct No + Customer Name)) -->

<!-- End - Selecting Reporting Period Screen Section-->
</form><!-- End form  -->



<!-- Start - Errors -->
<!--  From Date Error  
<div class="alert-fromdate" style="margin-top:300px;">
  <span class=" close closebtn" onClick="resetFromDate()">&times;</span> 
<strong class="wssFontArial2" style="color:red;">ERROR:</strong><span class="wssFontArial2" style="color:red;">&nbsp;This is not a valid date. from</span><!-- we here add 'margin-top' explicitly bcz only this property is changes on 'fromDate' and 'toDate' fields
</div>
-->
<!--  To Date Error  
<div class="alert-todate" style="margin-top:300px;">
  <span class=" close closebtn" onClick="resetToDate()">&times;</span> 
<strong class="wssFontArial2" style="color:red;">ERROR:</strong><span class="wssFontArial2" style="color:red;">&nbsp;This is not a valid date. to</span><!-- we here add 'margin-top' explicitly bcz only this property is changes on 'fromDate' and 'toDate' fields
</div>
-->
<!-- Future From Date Error 
<div class="alert-futurefromdate" style="margin-top:300px;">

	<span class=" close closebtn" onClick="resetFutureFromDate()">&times;</span>	
	<strong class="wssFontArial2" style="color:red;">ERROR:</strong><span class="wssFontArial2" style="color:red;">&nbsp;Date cannot be in the future.</span>
</div>
-->
<!-- Future To Date Error  
<div class="alert-futuretodate" style="margin-top:300px;">

	<span class=" close closebtn" onClick="resetFutureToDate()">&times;</span>	
	<strong class="wssFontArial2" style="color:red;">ERROR:</strong><span class="wssFontArial2" style="color:red;">&nbsp;Date cannot be in the future.</span>
</div>
-->
<!-- End - Errors -->

<%@ include file="CMIncIntervalBottom.jsp" %>

	
</body>
</html>