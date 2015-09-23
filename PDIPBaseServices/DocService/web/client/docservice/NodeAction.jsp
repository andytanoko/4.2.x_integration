<%@ page import="com.gridnode.pdip.base.docservice.util.*,
                 com.gridnode.pdip.base.docservice.action.*" %>
<%@ taglib uri="/docservice" prefix="docservice" %>

<docservice:nodeaction>
    <docservice:actionresult resultType="FAILURE" id="message">
        <BR>Action failed!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="SYSTEM_ERROR" id="message">
        <BR>System error!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="ILLEGAL_ARGUMENTS" id="message">
        <BR>Logical error!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="SOURCE_NOT_FOUND" id="message">
        <BR>Path not found!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="DESTINATION_NOT_FOUND" id="message">
        <BR>Destination path not found!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="DOCUMENT_PARENT" id="message">
        <BR>Cannot paste a document to a domain!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="INTER_DOMAIN_TRANSFER" id="message">
        <BR>Cannot transfer files from one domain to another!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="FILE_MANAGER_ERROR" id="message">
        <BR>File manager couldn't execute the requested action!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="NAME_EXISTS" id="message">
        <BR>A component with the same name exists!!
        <BR>Message from server: <%= pageContext.getAttribute("message") %>
    </docservice:actionresult>
    <docservice:actionresult resultType="SUCCESS" id="message">
        <script>
            parent.parent.frames[0].location.href='ServerExplorer.jsp';
        </script>
    </docservice:actionresult>
</docservice:nodeaction>
<jsp:include page="NodeDetailsView.jsp"/>
