<%@ page import="com.crystaldecisions.sdk.occa.infostore.*,
                 com.crystaldecisions.sdk.plugin.desktop.common.*,
                 com.crystaldecisions.sdk.framework.*,
                 com.crystaldecisions.sdk.occa.security.*,
                 com.crystaldecisions.sdk.exception.SDKException,
                 com.crystaldecisions.sdk.occa.managedreports.IReportSourceFactory,
                 java.util.Locale,
                 com.crystaldecisions.sdk.occa.report.reportsource.IReportSource"
        %>

// WSS start

<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions" %>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat" %>

// WSS end

<%@ include file="logonSupport.jsp" %>
<%@ include file="errorHandler.jsp" %>
<%@ include file="schedule.jsp" %>

<%!
    private void scheduleReport(HttpSession session, HttpServletRequest request, HttpServletResponse response, Logon logonObject, IEnterpriseSession enterpriseSession, IInfoObjects oInfoObjects)
            throws SDKException, IOException {
        session.setAttribute("enterpriseSession", enterpriseSession);
        session.setAttribute("oInfoObjects", oInfoObjects);
        session.setAttribute("reportParameters", request.getParameter("parms"));
        session.setAttribute("cdxUser", logonObject.getCCBUser());
        doScheduling(request, response, session);
    }


    private void callHistory(HttpSession session, HttpServletResponse response, Logon logonObject, IEnterpriseSession enterpriseSession)
            throws SDKException, IOException {
        session.setAttribute("enterpriseSession", enterpriseSession);
        session.setAttribute("reportCode", logonObject.getReportName());
        session.setAttribute("dateFormat", logonObject.getDateFormat());
        session.setAttribute("timeFormat", logonObject.getTimeFormat());
        Locale locale = new Locale(logonObject.getCCBUserLanguage(), "");
        session.setAttribute("locale", locale);
        response.sendRedirect("instances.jsp");
    }

    private void viewReport(HttpSession session, HttpServletRequest request, HttpServletResponse response, Logon logonObject, IEnterpriseSession enterpriseSession, IInfoObject oInfoObject)
            throws SDKException, IOException {
        session.setAttribute("enterpriseSession", enterpriseSession);
        session.setAttribute("reportParameters", request.getParameter("parms"));
        session.setAttribute("cdxUser", logonObject.getCCBUser());
        //Use the PS report factory to obtain a report source that will be processed on the Page Server.
        IReportSourceFactory factoryPS = (IReportSourceFactory) enterpriseSession.getService("PSReportFactory");
        Locale locale = new Locale(logonObject.getCCBUserLanguage(), "");
        Object reportSource = factoryPS.openReportSource((oInfoObject), locale);
        session.setAttribute("reportSource", reportSource);
        session.setAttribute("oInfoObject", oInfoObject);
        //View the report in the standard DHTML viewer.
        response.sendRedirect("CrystalReportViewer.jsp");

    }

// WSS Start

    private void exportReport(HttpSession session, HttpServletRequest request, HttpServletResponse response, Logon logonObject, IEnterpriseSession enterpriseSession, IInfoObject oInfoObject)
                  throws SDKException, IOException {
        session.setAttribute("enterpriseSession", enterpriseSession);
        session.setAttribute("reportParameters", request.getParameter("parms"));
        session.setAttribute("cdxUser", logonObject.getCCBUser());
        // Use the PS report factory to obtain a report source that will be
        // processed on the Page Server.
        IReportSourceFactory factoryPS = (IReportSourceFactory) enterpriseSession.getService("PSReportFactory");
        Locale locale = new Locale(logonObject.getCCBUserLanguage(), "");
        Object reportSource = factoryPS.openReportSource((oInfoObject), locale);
        session.setAttribute("reportSource", reportSource);
        session.setAttribute("oInfoObject", oInfoObject);
        response.sendRedirect("CrystalReportExporter.jsp");

     }
// WSS End

%>

<%
    try {
        Logon logonform = new Logon(response, request);
        session.setAttribute("userLanguage", logonform.getCCBUserLanguage());
        String repositoryAction = logonform.getAction();
        session.setAttribute("repositoryAction", repositoryAction);
        IEnterpriseSession enterpriseSession = null;
        try {
            enterpriseSession = CrystalEnterprise.getSessionMgr().logon(logonform.getUsername(), logonform.getPassword(), logonform.getCmsname(), logonform.getAuthType());
        }
        catch (SDKException sdkEx) {
           // sdkEx.printStackTrace();
            System.out.println( sdkEx.getMessage());
            String L_LOGONNOTVALID = MessageCatalogRepository.getMessage("LOGONNOTVALID", logonform.getCCBUserLanguage());
            new ErrorHandler(L_LOGONNOTVALID, response, session);
            return;
        }
        //Grab the InfoStore from the httpsession
        IInfoStore infoStore = (IInfoStore) enterpriseSession.getService("", "InfoStore");
        //Query for the report folder in the CMS.
        IInfoObjects fInfoObjects = (IInfoObjects) infoStore.query("SELECT * FROM CI_INFOOBJECTS WHERE  SI_PROGID = 'CrystalEnterprise.Folder' AND SI_NAME = "
                + "'" + logonform.getReportFolder() + "'");

        if (fInfoObjects.size() > 0) {
            IInfoObject fInfoObject = (IInfoObject) fInfoObjects.get(0);
            String folderID = Integer.toString(fInfoObject.getID());
            //Query for the report object in the CMS.
            IInfoObjects oInfoObjects = (IInfoObjects) infoStore.query("SELECT TOP 1 * " +
                    "FROM CI_INFOOBJECTS " +
                    "WHERE SI_PROGID = 'CrystalEnterprise.Report' AND SI_INSTANCE=0 AND SI_NAME='" + logonform.getReportName() + "'" + " AND SI_PARENT_FOLDER =" + "'" + folderID + "'");
            if (oInfoObjects.size() > 0) {

                //Retrieve the latest instance of the report
                IInfoObject oInfoObject = (IInfoObject) oInfoObjects.get(0);
                int id = oInfoObject.getID();
                session.setAttribute("reportID", Integer.toString(id));
                HashMap hm = new HashMap();
                hm.put("HISTORY", new Integer(1));
                hm.put("SUBMIT", new Integer(2));
                hm.put("SCHEDULE", new Integer(3));
// WSS Start
                hm.put("FILE", new Integer(4));
// WSS End
                Integer translatedCode = (Integer) hm.get(repositoryAction);
                switch (translatedCode.intValue()) {
                    case 1:
                        callHistory(session, response, logonform, enterpriseSession);
                        break;
                    case 2:
                        viewReport(session, request, response, logonform, enterpriseSession, oInfoObject);
                        break;
                    case 3:
                        scheduleReport(session, request, response, logonform, enterpriseSession, oInfoObjects);
                        break;
// WSS Start
                    case 4:
                        exportReport(session, request, response, logonform, enterpriseSession, oInfoObject);
                        break;
// WSS End
                    default:  // should never happen
                        String L_ACTIONINVALID = MessageCatalogRepository.getMessage("ACTIONINVALID", logonform.getCCBUserLanguage());
                        out.println(L_ACTIONINVALID + repositoryAction);
                }

            } else {
                String L_REPORTNOTFOUND = MessageCatalogRepository.getMessage("REPORTNOTFOUND", logonform.getCCBUserLanguage());
                new ErrorHandler((logonform.getReportName() + " - " + L_REPORTNOTFOUND), response, session);
                return;
            }
        } else {
            String L_FOLDERNOTFOUND = MessageCatalogRepository.getMessage("FOLDERNOTFOUND", logonform.getCCBUserLanguage());
            new ErrorHandler((logonform.getReportFolder() + " - " + L_FOLDERNOTFOUND), response, session);
            return;
        }

    }
    catch (SDKException sdkEx) {
        new ErrorHandler(sdkEx.getMessage(), response, session);
    }
    catch (RuntimeException ex) {
        new ErrorHandler(ex.getMessage(), response, session);
    }

%>
