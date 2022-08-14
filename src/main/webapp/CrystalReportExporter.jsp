<%@page import="com.crystaldecisions.report.web.viewer.*" %>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions" %>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat" %>
<%@page import="com.crystaldecisions.reports.sdk.*" %>
<%@page import="com.crystaldecisions.sdk.occa.report.lib.*" %>
<%@page import="com.crystaldecisions.sdk.occa.report.data.*" %>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.*" %>

<%@ include file="logonSupport.jsp" %>
<%@ include file="helper.jsp" %>
<%@ include file="errorHandler.jsp" %>
<%
    try
    {
        ReportExportControl exporter = new ReportExportControl();
        Object reportSource = session.getAttribute("reportSource");
        exporter.setReportSource(reportSource);
        Fields parameterFields = populateParametersForViewer(response, session);
        exporter.setParameterFields(parameterFields);
        exporter.setEnableParameterPrompt(false);
        exporter.refresh();
        ExportOptions exportOptions = new ExportOptions();
        ReportExportFormat exportFormat = ReportExportFormat.PDF;
        exportOptions.setExportFormatType(exportFormat);
        exporter.setExportOptions(exportOptions);
        String mimeType = "application/pdf";
        response.setContentType(mimeType);
        exporter.setExportAsAttachment(false);
        exporter.processHttpRequest(request, response, getServletConfig().getServletContext(), null);
        exporter.dispose();

    } catch (SDKException sdkEx) {
        new ErrorHandler(sdkEx.getMessage(), response, session);
    }
%>
