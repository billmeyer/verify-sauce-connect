<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>JSP Snoop Page</title>

        <%@ page import="javax.servlet.http.HttpUtils,java.util.Enumeration" %>
        <%@ page import="java.lang.management.*" %>
        <%@ page import="java.util.*" %>

        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500" rel="stylesheet">

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <!-- Bootstrap core CSS -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0/css/bootstrap.min.css"
              rel="stylesheet">
        <!-- Material Design Bootstrap -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.5.0/css/mdb.min.css" rel="stylesheet">
    </head>

    <body class="fixed-sn">

        <main class="mt-5">

            <!--Main container-->
            <div class="container">
                <h2>Request information</h2>
                <table class="table table-bordered table-striped table-sm">
                    <tbody>
                        <tr>
                            <th>Requested URL:</th>
                            <td id="requestedURL"><%= HttpUtils.getRequestURL(request) %>
                            </td>
                        </tr>
                        <tr>
                            <th>Request method:</th>
                            <td id="requestMethod"><%= request.getMethod() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Request URI:</th>
                            <td id="requestURI"><%= request.getRequestURI() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Request protocol:</th>
                            <td id="requestProtocol"><%= request.getProtocol() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Servlet path:</th>
                            <td id="servletPath"><%= request.getServletPath() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Path info:</th>
                            <td id="pathInfo"><%= request.getPathInfo() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Path translated:</th>
                            <td id="pathTranslated"><%= request.getPathTranslated() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Query string:</th>
                            <td>
                                <% if (request.getQueryString() != null)
                                {
                                    out.write(request.getQueryString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                                }
                                %>
                            </td>
                        </tr>
                        <tr>
                            <th>Content length:</th>
                            <td id="contentLength"><%= request.getContentLength() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Content type:</th>
                            <td id="contentType"><%= request.getContentType() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Server name:</th>
                            <td id="serverName"><%= request.getServerName() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Server port:</th>
                            <td id="serverPort"><%= request.getServerPort() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Remote user:</th>
                            <td id="remoteUser"><%= request.getRemoteUser() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Remote address:</th>
                            <td id="remoteAddress"><%= request.getRemoteAddr() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Remote host:</th>
                            <td id="remoteHost"><%= request.getRemoteHost() %>
                            </td>
                        </tr>
                        <tr>
                            <th>Authorization scheme:</th>
                            <td id="authorizationScheme"><%= request.getAuthType() %>
                            </td>
                        </tr>

                    </tbody>
                </table>

                <%
                    Enumeration e = request.getHeaderNames();
                    if (e != null && e.hasMoreElements())
                    {
                %>

                <h2>Request headers</h2>
                <table class="table table-bordered table-striped table-sm">
                    <thead>
                        <tr>
                            <th>Header</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            while (e.hasMoreElements())
                            {
                                String k = (String) e.nextElement();
                        %>
                        <tr>
                            <td><%= k %>
                            </td>
                            <td><%= request.getHeader(k) %>
                            </td>
                        </tr>
                        <%
                            }
                        %>

                    </tbody>
                </table>
                <%
                    }
                %>


                <%
                    e = request.getParameterNames();
                    if (e != null && e.hasMoreElements())
                    {
                %>

                <H2>Request parameters</H2>
                <table class="table table-bordered table-striped table-sm">
                    <tr>
                        <th>Parameter</th>
                        <th>Value</th>
                        <th>Multiple values</th>
                    </tr>
                    <%
                        while (e.hasMoreElements())
                        {
                            String k = (String) e.nextElement();
                            String val = request.getParameter(k);
                            String vals[] = request.getParameterValues(k);
                    %>
                    <tr>
                        <td><%= k.replaceAll("<", "&lt;").replaceAll(">", "&gt;") %>
                        </td>
                        <td><%= val.replaceAll("<", "&lt;").replaceAll(">", "&gt;") %>
                        </td>
                        <td><%
                            for (int i = 0; i < vals.length; i++)
                            {
                                if (i > 0)
                                {
                                    out.print("<BR>");
                                }
                                out.print(vals[i].replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                            }
                        %></td>
                    </tr>
                    <%
                        }
                    %>
                </
                >
                <%
                    }
                %>


                <%
                    e = request.getAttributeNames();
                    if (e != null && e.hasMoreElements())
                    {
                %>
                <h2>Request Attributes</h2>
                <table class="table table-bordered table-striped table-sm">
                    <thead>
                        <tr>
                            <th>Attribute</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            while (e.hasMoreElements())
                            {
                                String k = (String) e.nextElement();
                                Object val = request.getAttribute(k);
                        %>
                        <tr>
                            <td><%= k.replaceAll("<", "&lt;").replaceAll(">", "&gt;") %>
                            </td>
                            <td><%= val.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;") %>
                            </td>
                        </tr>
                        <%
                            }
                        %>

                    </tbody>
                </table>
                <%
                    }
                %>


                <%
                    e = getServletConfig().getInitParameterNames();
                    if (e != null && e.hasMoreElements())
                    {
                %>
                <h2>Init parameters</h2>
                <table class="table table-bordered table-striped table-sm table-responsive">
                    <thead>
                        <tr>
                            <th>Parameter</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            while (e.hasMoreElements())
                            {
                                String k = (String) e.nextElement();
                                String val = getServletConfig().getInitParameter(k);
                        %>
                        <tr>
                            <td><%= k %>
                            </td>
                            <td><%= val %>
                            </td>
                        </tr>
                        <%
                            }
                        %>

                    </tbody>
                </table>
                <%
                    }
                %>

                <h2>JVM Memory Monitor</h2>
                <table class="table table-bordered table-striped table-sm">
                    <tbody>
                        <tr>
                            <td colspan="2" align="center">
                                Memory MXBean
                            </td>
                        </tr>

                        <tr>
                            <td>Heap Memory Usage</td>
                            <td>
                                <%=ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()%>
                            </td>
                        </tr>

                        <tr>
                            <td>Non-Heap Memory Usage</td>
                            <td>
                                <%=ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()%>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2"></td>
                        </tr>

                        <tr>
                            <td colspan="2" align="center">
                                Memory Pool MXBeans
                            </td>
                        </tr>

                    </tbody>
                </table>
                <%
                    Iterator iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
                    while (iter.hasNext())
                    {
                        MemoryPoolMXBean item = (MemoryPoolMXBean) iter.next();
                %>

                <table class="table table-bordered table-striped table-sm">

                    <tbody>
                        <tr>
                            <td colspan="2" align="center"><strong><%= item.getName() %>
                            </strong></td>
                        </tr>

                        <tr>
                            <td>Type</td>
                            <td><%= item.getType() %>
                            </td>
                        </tr>

                        <tr>
                            <td>Usage</td>
                            <td><%= item.getUsage() %>
                            </td>
                        </tr>

                        <tr>
                            <td>Peak Usage</td>
                            <td><%= item.getPeakUsage() %>
                            </td>
                        </tr>

                        <tr>
                            <td>Collection Usage</td>
                            <td><%= item.getCollectionUsage() %>
                            </td>
                        </tr>

                    </tbody>
                </table>


                <%
                    }
                %>

            </div>
        </main>

        <!-- SCRIPTS -->
        <!-- JQuery -->
        <script type="text/javascript" src="js/jquery-3.2.1.min.js"></script>
        <!-- Bootstrap tooltips -->
        <script type="text/javascript" src="js/popper.min.js"></script>
        <!-- Bootstrap core JavaScript -->
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <!-- MDB core JavaScript -->
        <script type="text/javascript" src="js/mdb.min.js"></script>

    </body>
</html>

