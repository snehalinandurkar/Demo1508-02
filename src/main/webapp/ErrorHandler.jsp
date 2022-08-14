<% 
  // This code is included in most jsp pages, and looks on the server side, whether the string errorMsg is
  // populated, and if yes, is writing its contents to a hidden text field, and displays it on the client
	String errorMsg = request.getParameter("errorMsg");
          if (errorMsg != null && errorMsg.equals("") == false && errorMsg.equals("null") == false) {
          out.println("<input id=ErrorMessage name=ErrorMessage type=hidden value=" + '"' + errorMsg + '"' + ">");

          errorMsg = null;
        %>
          <script>
              window.alert(document.Form1.ErrorMessage.value);
          </script>
        <% 
	} 
%>
