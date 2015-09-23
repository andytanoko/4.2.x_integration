<%@ page import="com.gridnode.pdip.base.docservice.filesystem.tree.*" %>
<html>
<body>
<%
String isDataFound = request.getParameter("DataFound");
String nodeTypeStr = request.getParameter("nodeType");
Object cutToPaste = session.getAttribute("cut");
Object copyToPaste = session.getAttribute("copy");

if (isDataFound == null)
{
}
else if (isDataFound.equals("true"))
{
    int nodeType = Integer.parseInt(nodeTypeStr);
    if (nodeType == DocumentTreeNode.DOCUMENT_NODE ||
        nodeType == DocumentTreeNode.ROOT_NODE)
    {
%>
        <input type=button name=cut value="Cut" disabled/>
        <input type=button name=cut value="Copy" disabled/>
        <input type=button name=cut value="Paste" disabled/>
        <input type=button name=cut value="Delete" disabled/>
        <input type=button name=rename value="Rename" onClick="javascript:parent.frames[0].openNodeNameWindow('rename');"/>
        <input type=button name=add value="Add Document" disabled/>
        <input type=button name=add value="Create Folder" disabled/>
<%
    }
    else 
    {
%>
        <input type=button name=cut value="Cut" onClick="javascript:parent.frames[0].submitAction('cut');"/>
        <input type=button name=copy value="Copy" onClick="javascript:parent.frames[0].submitAction('copy');"/>
<%
        if (cutToPaste != null || copyToPaste != null)
        {
%>
            <input type=button name=paste value="Paste" onClick="javascript:parent.frames[0].submitAction('paste');"/>
<%  
        }
        else
        {
%>
            <input type=button name=paste value="Paste" disabled/>
<%
        }
%>
            <input type=button name=delete value="Delete" onClick="javascript:parent.frames[0].submitAction('delete');"/>
            <input type=button name=rename value="Rename" onClick="javascript:parent.frames[0].openNodeNameWindow('rename');"/>
            <input type=button name=add value="Create Folder" onClick="javascript:parent.frames[0].openNodeNameWindow('createfolder');"/>
<%
        if (nodeType != DocumentTreeNode.DOMAIN_NODE)
        {
%>
            <input type=button name=add value="Add Document" onClick="javascript:parent.frames[0].openAddDocumentWindow();"/>
<%
        }
        else
        {
%>
            <input type=button name=add value="Add Document" disabled/>
<%
        }
    }
}
else
{
    int nodeType = Integer.parseInt(nodeTypeStr);
%>
    <input type=button name=cut value="Cut" disabled />
    <input type=button name=copy value="Copy" disabled/>
<%
    if (cutToPaste != null || copyToPaste != null)
    {
%>
    <input type=button name=paste value="Paste" onClick="javascript:parent.frames[0].submitAction('paste');"/>
<%
    }
    else
    {
%>
    <input type=button name=paste value="Paste" disabled/>
<%
    }
%>
    <input type=button name=delete value="Delete" disabled/>
    <input type=button name=add value="Create Folder" onClick="javascript:parent.frames[0].openNodeNameWindow('createfolder');"/>
<%
    if (nodeType != DocumentTreeNode.DOMAIN_NODE)
    {
%>
    <input type=button name=add value="Add Document" onClick="javascript:parent.frames[0].openAddDocumentWindow();"/>
<%
    }
    else
    {
%>
    <input type=button name=add value="Add Document" disabled/>
<%
    }
}
%>
</body>
</html>