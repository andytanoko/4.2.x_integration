<%@ page import="com.gridnode.pdip.base.docservice.filesystem.tree.*" %>
<%@ taglib uri="/docservice" prefix="docservice" %>

<html>
    <head>
        <script>
            function submitPage(type, id)
            {
                document.detailsform.nodeType.value=type;
                document.detailsform.id.value=id;
                document.detailsform.submit();
            }
            
            function submitAction(actionName)
            {
                document.NodeDetailsViewForm.actionType.value=actionName;
                document.NodeDetailsViewForm.submit();
            }
            function openAddDocumentWindow()
            {
                window.open('AddDocumentNode.jsp?nodeType=<%= request.getParameter("nodeType") %>&id=<%= request.getParameter("id") %>');
            }
            
            function openNodeNameWindow(pageTypeName)
            {
                window.open('NodeName.jsp?pageType=' + pageTypeName + '&nodeType=' + 
                    document.NodeDetailsViewForm.nodeType.value + '&id=' + 
                    document.NodeDetailsViewForm.id.value);
            }
            function submitNodeNameAction(actionTypeName, newName)
            {
                document.NodeDetailsViewForm.actionType.value=actionTypeName;
                document.NodeDetailsViewForm.newNodeName.value=newName;
                document.NodeDetailsViewForm.submit();
            }
            
            function refreshPage()
            {
                javascript:submitPage(document.NodeDetailsViewForm.nodeType.value,
                    document.NodeDetailsViewForm.id.value);
                parent.parent.frames[0].location.href='ServerExplorer.jsp';
            }
        </script>
    </head>
    <body>
        <%
            if (request.getParameter("nodeType") != null)
            {
                int i = 0;
        %>
            <docservice:node nodeType='<%= request.getParameter("nodeType") %>' id='<%= request.getParameter("id") %>' name="data">
                <BR>Current location type:&nbsp;&nbsp;
                <%
                    DocumentTreeNode tn = (DocumentTreeNode) pageContext.getAttribute("data");
                    if (tn != null)
                    {
                        if (tn.getType() == DocumentTreeNode.ROOT_NODE)
                        {
                %>
                            Root
                <%
                        }
                        else if (tn.getType() == DocumentTreeNode.DOMAIN_NODE)
                        {
                %>
                            Domain
                <%
                        }
                        else if (tn.getType() == DocumentTreeNode.FOLDER_NODE)
                        {
                %>
                            Folder
                <%
                        }
                        else if (tn.getType() == DocumentTreeNode.DOCUMENT_NODE)
                        {
                %>
                            Document
                <%
                        }
                %>
                        <BR>Current path:&nbsp;&nbsp;<%= tn.getCanonicalPath() %>
                <%
                    }
                %>
            </docservice:node>
            <docservice:nodedetails nodeType='<%= request.getParameter("nodeType") %>' id='<%= request.getParameter("id") %>'>
                <docservice:actionresult resultType="FAILURE" id="message">
                    <BR>Failed to find the node details
                    <BR>Message from server: <%= pageContext.getAttribute("message") %>
                </docservice:actionresult>

                <docservice:actionresult resultType="SYSTEM_ERROR" id="message">
                    <BR>System error
                    <BR>Message from server: <%= pageContext.getAttribute("message") %>
                </docservice:actionresult>
                <docservice:actionresult resultType="NO_DATA" id="message">
                    <BR>No data found
                    <script>
                        parent.frames[1].location.href='NodeDetailsControl.jsp?DataFound=false&nodeType=<%= request.getParameter("nodeType") %>';
                    </script>
                    <form name="NodeDetailsViewForm" method=post action="NodeAction.jsp" >
                    <input type=hidden name="actionType" >
                    <input type=hidden name="newNodeName" >
                    <input type=hidden name="nodeType" value='<%= request.getParameter("nodeType") %>' >
                    <input type=hidden name="id" value='<%= request.getParameter("id") %>' >
                    </form>
                </docservice:actionresult>

                <docservice:nodetable>
                    <form name="NodeDetailsViewForm" method=post action="NodeAction.jsp" >
                    <input type=hidden name="actionType" >
                    <input type=hidden name="newNodeName" >
                    <input type=hidden name="nodeType" value='<%= request.getParameter("nodeType") %>' >
                    <input type=hidden name="id" value='<%= request.getParameter("id") %>' >
                    <table cellspacing=1 cellpadding=1 border=0 width=100%>
                    <tr>
                        <td>
                        <td><b>Name
                        <td><b>Type
                        <td><b>Size
                        <td><b>Author
                        <td><b>Created On
                        <td><b>Last Accessed On
                    <tr>
                    <docservice:iterator>
                        <docservice:element id="treeNode">
                            <tr>
                            <%
                                DocumentTreeNode node = (DocumentTreeNode) pageContext.getAttribute("treeNode");
                            %>
                            
                                <td><input type=checkbox name='check<%= i++ %>' value='<%= node.getType() + ";" + node.getId() %>'>
                            <%
                                if (node.getType() == DocumentTreeNode.FOLDER_NODE ||
                                    node.getType() == DocumentTreeNode.DOMAIN_NODE)
                                {
                            %>
                                    <td><%= node.getName() %>
                                    <td><%= node.getType() == DocumentTreeNode.FOLDER_NODE ? "Folder" : "Domain" %>
                                    <td>
                                    <td>
                                    <td>
                                    <td>
                            <%
                                }
                                else if (node.getType() == DocumentTreeNode.DOCUMENT_NODE)
                                {
                                    DocumentNode docNode = (DocumentNode) node;
                            %>
                                    <td><%= node.getName() %>
                                    <td>Document
                                    <td><%= docNode.getSize() %>
                                    <td><%= docNode.getAuthor() %>
                                    <td><%= docNode.getCreatedOnDate() %>
                                    <td><%= docNode.getLastAccessedDate() %>
                            <%
                                }
                                else if (node.getType() == DocumentTreeNode.FILE_NODE)
                                {
                                    FileNode fileNode = (FileNode) node;
                            %>
                                    <td><%= node.getName() %>
                                    <td><%= fileNode.getIsMainFile().equals(new Boolean(true)) ? "Main file" : "Attachment" %> 
                                    <td>
                                    <td>
                                    <td>
                                    <td>
                            <%
                                }
                            %>
                            </tr>
                        </docservice:element>
                                                
                    </docservice:iterator>
                    </table>
                    <script>
                        parent.frames[1].location.href='NodeDetailsControl.jsp?DataFound=true&nodeType=<%= request.getParameter("nodeType") %>';
                    </script>
                    </form>
                </docservice:nodetable>
            </docservice:nodedetails>

            <%
            }
            %>

        <form name="detailsform" method=post action="NodeDetailsView.jsp">
            <input type=hidden name="nodeType" />
            <input type=hidden name="id" />
        </form>
    </body>
</html>
