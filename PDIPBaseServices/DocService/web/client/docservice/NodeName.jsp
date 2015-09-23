<%@ page import="com.gridnode.pdip.base.docservice.filesystem.tree.*" %>
<%@ taglib uri="/docservice" prefix="docservice" %>
<html>
<head>
<script>
function submitForm()
{
    if (document.newnodename.nodeName.value == '')
    {
        alert("Enter the name");
    }
    else
    {
        javascript:opener.submitNodeNameAction(document.newnodename.pageType.value, 
            document.newnodename.nodeName.value);
        window.close();
    }
}
</script>
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
    <form name="newnodename">
        <input type=hidden name="pageType" value='<%= request.getParameter("pageType") %>'>
        New Name: <input type=text name="nodeName">
        <BR><input type=button value="Submit" onClick="javascript:submitForm()"><input type=reset>
    </form>
</body>
</html>

