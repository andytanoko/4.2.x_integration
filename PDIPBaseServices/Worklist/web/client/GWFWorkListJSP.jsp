<html>
<head>
<script language="JavaScript">
<!--
function assign(inValue){
  document.myfrm1.reqAction.value=inValue;
  document.myfrm1.submit();
}

function rej(){
  var al=prompt("Enter Reject Note"," ");
  document.myfrm1.rejectNote.value=al;
  document.myfrm1.reqAction.value="Reject";
  document.myfrm1.submit();
}

function update(){
// alert("Function Called");
 document.myfrm1.reqAction.value=null;
 document.myfrm1.submit();
}
-->
</script>

</head>
<body>
<%@page import="java.util.*,java.io.*"%>
<form method="post" action="<%=request.getContextPath()%>/GWFWorkListServlet" name="myfrm1">
<!--
<applet code="com.gridnode.gridflow.worklist.ui.GWFWorkListApplet" width=2 height=2 MAYSCRIPT>
<PARAM NAME=name value='<%= request.getAttribute("user")%>'>
</applet>
-->
<H2>WorkItems for <%= request.getAttribute("user")%></H2>
<input type="hidden" name="user" value="<%= request.getAttribute("user")%>">
<input type="hidden" name="rejectNote">
<br>
<%
String workItems = (String)request.getAttribute("workitems");
%>
<hr>
<table border=0>
    <tr>
        <th></th>
        <th>WorkItem Description</th>
        <th>WorkItem Comments</th>
        <th>WorkItemDate<br></th>
    </tr>
<%
if(workItems != null)
{
%>
    <%=workItems%>
<%
}
%>
</table>
<hr>
<br><br>
<input type = "hidden" name="reqAction">
<input type="button" name="execute" value="Execute" onClick="assign('Execute')">
<input type="button" name="reject"  value="Reject" onClick="javascript:rej()">
<br>
</form>
</body>
</html>