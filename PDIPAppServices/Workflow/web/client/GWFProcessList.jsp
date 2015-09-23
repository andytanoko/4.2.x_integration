<%@page
		import="java.util.*,
				java.io.*,
                                java.util.*,
                                com.gridnode.pdip.app.workflow.web.*,
                                com.gridnode.pdip.app.workflow.engine.*"
%>
<html>
<head>
<script language="JavaScript">
<!--
-->
</script>

</head>
<body>
<%
        int sortIndex=(request.getParameter("sortIndex")==null)?1:Integer.parseInt(request.getParameter("sortIndex"));
        String action=request.getParameter("action");
        GWFProcessListHandler processListHandler=new GWFProcessListHandler();
        if(request.getParameter("action")!=null &&
           request.getParameter("ProcessUId")!=null &&
           request.getParameter("ProcessType")!=null &&
           request.getParameter(GWFFactory.ENGINE_TYPE)!=null &&
           request.getParameter("action").equals("Start"))
        {
            processListHandler.startProcess(new Long(request.getParameter("ProcessUId")),request.getParameter("ProcessType"),request.getParameter(GWFFactory.ENGINE_TYPE));
        }
	ArrayList processList=new ArrayList(processListHandler.getBpssProcessList(null));
        Collections.sort(processList,new ObjectArrayComparator(sortIndex));
	Iterator iterator=processList.iterator();
%>
<br><h2>Bpss Process Selection List :</h2><br>
<table>
	<tr>
		<th>S.No</th>
                <th><a href="GWFProcessList.jsp?sortIndex=1">Process Spec Name</a></th>
		<th><a href="GWFProcessList.jsp?sortIndex=2">Process Spec UUID</a></th>
                <th><a href="GWFProcessList.jsp?sortIndex=3">Process Spec Version</a></th>
		<th><a href="GWFProcessList.jsp?sortIndex=5">Name</a></th>
		<th><a href="GWFProcessList.jsp?sortIndex=6">Type</a></th>
		<th></th>
	</tr>
<%
	int count=1;
	while(iterator.hasNext())
	{
		Object objArr[]=(Object[])iterator.next();
%>
	<tr>
		<td><%=count++%></td>
		<td><%=objArr[1]%></td>
      		<td><%=objArr[2]%></td>
                <td><%=objArr[3]%></td>
		<td><%=objArr[5]%></td>
		<td><%=objArr[6]%></td>
		<td><a href="GWFProcessList.jsp?action=Start&ProcessUId=<%=objArr[4]%>&ProcessType=<%=objArr[6]%>&<%=GWFFactory.ENGINE_TYPE+"="+GWFFactory.BPSS_ENGINE%>">Start</a></td>
	</tr>
<%
	}
%>
</table>
<br><hr><br>
<%
	processList=new ArrayList(processListHandler.getXpdlProcessList(null));
    Collections.sort(processList,new ObjectArrayComparator(sortIndex));
	iterator=processList.iterator();
%>
<br><h2>Xpdl Process Selection List :</h2><br>
<table>
	<tr>
		<th>S.No</th>
                <th><a href="GWFProcessList.jsp?sortIndex=1">Process Spec Name</a></th>
		<th><a href="GWFProcessList.jsp?sortIndex=2">Process Spec UUID</a></th>
                <th><a href="GWFProcessList.jsp?sortIndex=3">Process Spec Version</a></th>
		<th><a href="GWFProcessList.jsp?sortIndex=5">Name</a></th>
		<th><a href="GWFProcessList.jsp?sortIndex=6">Type</a></th>
		<th></th>
	</tr>
<%
	count=1;
	while(iterator.hasNext())
	{
		Object objArr[]=(Object[])iterator.next();
%>
	<tr>
		<td><%=count++%></td>
		<td><%=objArr[1]%></td>
      		<td><%=objArr[2]%></td>
                <td><%=objArr[3]%></td>
		<td><%=objArr[5]%></td>
		<td><%=objArr[6]%></td>
		<td><a href="GWFProcessList.jsp?action=Start&ProcessUId=<%=objArr[4]%>&ProcessType=<%=objArr[6]%>&<%=GWFFactory.ENGINE_TYPE+"="+GWFFactory.XPDL_ENGINE%>">Start</a></td>
	</tr>
<%
	}
%>
</table>


</body>
</html>