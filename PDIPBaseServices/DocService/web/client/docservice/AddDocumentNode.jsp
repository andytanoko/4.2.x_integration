<%@ page import="com.gridnode.pdip.base.docservice.filesystem.tree.*" %>
<%@ taglib uri="/docservice" prefix="docservice" %>
<html>
<head>
</head>
<body>
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
    <%
        String nodeType = request.getParameter("nodeType");
        String id = request.getParameter("id");
    %>
    <form name="adddocument" METHOD=POST ENCTYPE="multipart/form-data" action="AddDocumentAction.jsp">
        <input type=hidden name="parentNodeType" value='<%= nodeType %>'>
        <input type=hidden name="parentId" value='<%= id %>'>
        <input type=hidden name="actionType" value="adddocument">
        Name: <input type=text name="nodeName">
        <br>Document Type: <input type=text name="docType">
        <br>Author: <input type=text name="author">
        <br>Main file:<input type=file name="mainFile">
        <br>Attachments:
        <br><input type=file name="attachment1">
        <br><input type=file name="attachment2">
        <br><input type=file name="attachment3">
        <br><input type=file name="attachment4">
        <br><input type=file name="attachment5">
        <BR><input type=submit value="Add Document"><input type=reset>
    </form>
</body>
</html>

