<%@ page import="java.lang.reflect.Method"%>
<%@ page import="java.lang.reflect.InvocationTargetException"%>
<%!
public void DisplayMenu(String mnuItem, JspWriter out, HttpServletResponse response) {
    // Used for administering the menu on the left side of the Self Service page
        boolean menuDisplayed = false;
        Class my = this.getClass();
        Method m[]= my.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {
          if (m[i].toString().indexOf("CMDisplayMenu") > 0) {
             try {
             	Object parameters[] = new Object[] {mnuItem, out, response};
             	m[i].invoke(this, parameters);
             	menuDisplayed = true;
			 } catch (IllegalAccessException ex) {
				System.out.println(ex.getMessage());				
			 } catch (InvocationTargetException ex) {
				System.out.println(ex.getMessage());
			 }
             break;           
          }
        }
        if (menuDisplayed == false) {
            CIDisplayMenu(mnuItem, out, response);
        }
}

public void DisplayVDERMenu(String mnuItem, JspWriter out, HttpServletResponse response) {
    // Used for administering the menu on the left side of the Self Service page
        boolean menuDisplayed = false;
        Class my = this.getClass();
        Method m[]= my.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {
          if (m[i].toString().indexOf("CMDisplayVDERMenu") > 0) {
             try {
             	Object parameters[] = new Object[] {mnuItem, out, response};
             	m[i].invoke(this, parameters);
             	menuDisplayed = true;
			 } catch (IllegalAccessException ex) {
				System.out.println(ex.getMessage());				
			 } catch (InvocationTargetException ex) {
				System.out.println(ex.getMessage());
			 }
             break;           
          }
        }
        if (menuDisplayed == false) {
            CIDisplayVDERMenu(mnuItem, out, response);
        }
}

public void DisplayReducedMenu(String mnuItem, JspWriter out, HttpServletResponse response) {
    // Used for administering the menu on the left side of the Self Service page
        boolean menuDisplayed = false;
        Class my = this.getClass();
        Method m[]= my.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {
          if (m[i].toString().indexOf("CMDisplayReducedMenu") > 0) {
             try {
             	Object parameters[] = new Object[] {mnuItem, out, response};
             	m[i].invoke(this, parameters);
             	menuDisplayed = true;
			 } catch (IllegalAccessException ex) {
				System.out.println(ex.getMessage());				
			 } catch (InvocationTargetException ex) {
				System.out.println(ex.getMessage());
			 }
             break;
          }
        }
        if (menuDisplayed == false) {
            CIDisplayReducedMenu(mnuItem, out, response);
        }
}

private void DisplayIt(String mnuItem, JspWriter out, HttpServletResponse response, java.util.LinkedList mnuList) {
    try {
        for (int i = 0 ; i<mnuList.size() ; i++)
        {
            com.splwg.selfservice.MenuItem item = (com.splwg.selfservice.MenuItem)mnuList.get(i);

            if (item.getItemKey() == mnuItem)
            {
                out.println("	<tr class='menuTd'>");
                /*
                out.println("		<td width='15' valign='middle' align='left' height='15'><img border='0'");
                out.println("			src='/SelfService/graphics/Button_02.gif' width='16' height='20'>");
                out.println("		</td>");
                */
                out.println("		<td width='15' valign='middle' align='left' height='15'>&nbsp;</td>");

                out.println("		<td width='195' valign='middle' align='left' height='15' bgcolor='#CEE9F8'>");
                out.println("			<span");
                out.println("				style='layout-flow: vertical'><font class=wssFontArial2");
                out.println("				color='#002B5D'><b>" + item.getItemDescription() + "</b></font>");
                out.println("			</span>");
                out.println("		</td>");
                out.println("	</tr>");
            }
            else
            {
                   String entryValue = (String) item.getItemKey();
                String url="";

                if (item.getItemLocation() == "local")
                    url = response.encodeURL(item.getItemURL());
                else
                    url = item.getItemURL();

                out.println("	<tr class='menuTd'>");
                out.println("		<td width='15' valign='middle' align='left' height='15'></td>");
                out.println("		<td width='195' valign='middle' align='left' height='15'>");
                out.println("			<span");
                out.println("				style='layout-flow: vertical'><font class=wssFontArial2 color='#002B5D'><b><a");
                out.println("				href='" + url  + "'");
                out.println("				style='text-decoration:none'>" + item.getItemDescription() + "</a></b></font>");
                out.println("			</span>");
                out.println("		</td>");
                out.println("	</tr>");
            }
        }
    }
    catch(Exception e)
    {
        System.out.println("An error has occured at RefreshPage Function");
        System.out.println(e.getMessage());
    }


} // end DisplayIt


%>

