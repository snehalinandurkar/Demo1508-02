<%@ page import="java.io.File"%>
<%@ page import="java.io.InputStream"%>
 <%-- This goes in the <head> section, after <title>. --%>
<%
    ServletContext ctxt = this.getServletConfig().getServletContext();
    InputStream inputStream = ctxt.getResourceAsStream(File.separator + "CMwss.css");
    if (inputStream!=null){  %>
            <LINK REL=StyleSheet HREF="../CMwss.css" TYPE="text/css"/>
      <%  } else { %>
            <LINK REL=StyleSheet HREF="../CMwss.css" TYPE="text/css"/>
      <%   }
%>

<script type="text/javascript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
// -->
</script>
