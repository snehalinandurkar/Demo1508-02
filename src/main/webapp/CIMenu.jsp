<%!
     public void CIDisplayMenu(String mnuItem, JspWriter out, HttpServletResponse response) {
        try {
		java.util.LinkedList mnuList = new java.util.LinkedList();


		mnuList.add(new com.splwg.selfservice.MenuItem("Home","conEd.com Home","http://www.conEd.com","external"));
		mnuList.add(new com.splwg.selfservice.MenuItem("MyAccounts","My Accounts","MyAccounts"));
		mnuList.add(new com.splwg.selfservice.MenuItem("Account","Account Information","Account"));
		mnuList.add(new com.splwg.selfservice.MenuItem("FinancialHistory","Account Financial History","FinancialHistory"));
		mnuList.add(new com.splwg.selfservice.MenuItem("BillingHistory","Billing History","BillingHistory"));
		//mnuList.add(new com.splwg.selfservice.MenuItem("Pay","Make Payment","Pay"));
		//mnuList.add(new com.splwg.selfservice.MenuItem("StopService","Stop Service","StopService"));
		mnuList.add(new com.splwg.selfservice.MenuItem("UpdatePersonalInformation","Update Personal Info","UpdatePersonalInformation"));
        mnuList.add(new com.splwg.selfservice.MenuItem("ChangePassword","Change Password","passwordChange"));
		mnuList.add(new com.splwg.selfservice.MenuItem("LogOut","Log Out","LogOut"));
		
		mnuList.add(new com.splwg.selfservice.MenuItem("DataInterval","Data Interval","DataInterval"));

		DisplayIt(mnuItem, out, response, mnuList);
	}
	catch(Exception e)
	{
		System.out.println("An error has occured at RefreshPage Function");
		System.out.println(e.getMessage());
	}
     }

public void CIDisplayVDERMenu(String mnuItem, JspWriter out, HttpServletResponse response){
        try
        {
                java.util.LinkedList mnuList = new java.util.LinkedList();

                mnuList.add(new com.splwg.selfservice.MenuItem("Home","conEd.com Home","http://www.conEd.com","external"));
                mnuList.add(new com.splwg.selfservice.MenuItem("MyAccounts","My Accounts","MyAccounts"));
                mnuList.add(new com.splwg.selfservice.MenuItem("AllocationHistory","Project History","AllocationHistory"));
                // mnuList.add(new com.splwg.selfservice.MenuItem("AllocationInformation","Value Stack Allocation Information","AllocationInformation"));
                // mnuList.add(new com.splwg.selfservice.MenuItem("SubscriberPlan","Subscriber Plan","SubscriberPlan"));
                // mnuList.add(new com.splwg.selfservice.MenuItem("SubscriberPlanResult","Subscriber Plan Result","SubscriberPlanResult"));
                mnuList.add(new com.splwg.selfservice.MenuItem("StatementHistory","Statement History","StatementHistory"));
                
              //CM - Interval
        		mnuList.add(new com.splwg.selfservice.MenuItem("IntervalDataReports","Interval Data Reports","IntervalDataReports"));
              
                mnuList.add(new com.splwg.selfservice.MenuItem("UpdatePersonalInformation","Update Personal Info","UpdatePersonalInformation"));
                mnuList.add(new com.splwg.selfservice.MenuItem("ChangePassword","Change Password","passwordChange"));
                mnuList.add(new com.splwg.selfservice.MenuItem("LogOut","Log Out","LogOut"));
                
        		//mnuList.add(new com.splwg.selfservice.MenuItem("DataInterval","Data Interval","DataInterval"));

                DisplayIt(mnuItem, out, response, mnuList);
        }
        catch(Exception e)
        {
                System.out.println("An error has occured at RefreshPage Function");
                System.out.println(e.getMessage());
        }

}//End function


     public void CIDisplayReducedMenu(String mnuItem, JspWriter out, HttpServletResponse response) {
        try {
        java.util.LinkedList mnuList = new java.util.LinkedList();

        mnuList.add(new com.splwg.selfservice.MenuItem("Home","conEd.com Home","http://www.conEd.com","external"));
        mnuList.add(new com.splwg.selfservice.MenuItem("MyAccounts","My Accounts","MyAccounts"));
        mnuList.add(new com.splwg.selfservice.MenuItem("ChangePassword","Change Password","passwordChange"));
        mnuList.add(new com.splwg.selfservice.MenuItem("LogOut","Log Out","LogOut"));
        DisplayIt(mnuItem, out, response, mnuList);
    }
    catch(Exception e)
    {
        System.out.println("An error has occured at RefreshPage Function");
        System.out.println(e.getMessage());
    }

}//End function



%>

