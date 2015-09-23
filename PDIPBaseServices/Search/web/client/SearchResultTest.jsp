<%@ taglib uri="/search" prefix="search" %>
<%@ taglib uri="/display" prefix="display" %>

<%@ page import="java.util.ArrayList,java.util.HashMap,com.gridnode.pdip.base.search.helpers.SearchFilterOperator" %>

<HTML>
<HEAD>
<TITLE>Search </TITLE>

<SCRIPT language="javascript">
		
		function next(start)
		{
			document.frmForm.Start.value 	= start;
			document.frmForm.action = "SearchResultTest.jsp";
			document.frmForm.submit();
		}
		function previous()
		{
		}
</SCRIPT>
	
</HEAD>
<BODY>
<form name="frmForm" method="POST">

<% String strStart 				= request.getParameter("Start"); //starting record number
	if(strStart == null)
		strStart = "";
	/* end */
 
	ArrayList valueList = new ArrayList();
	valueList.add("2323423");

	

%>



<input type="hidden" name="Start" value="<%=strStart%>">

<search:Query tableName="Address" >

<search:ComparisonCondition connector="<%=null%>" field="FirstName" compOperator="<%=SearchFilterOperator.EQUAL_OPERATOR%>"  value="LastName" negate="<%=false%>" isFieldValue="<%=true%>" />

<search:InCondition connector="<%=SearchFilterOperator.AND_CONNECTOR%>" field="UID" valueList="<%=valueList%>" negate="<%=true%>" />

<search:RangeCondition connector="<%=SearchFilterOperator.OR_CONNECTOR%>" field="UID" lowValue="1" highValue="875452" negate="<%=false%>" />

</search:Query>

<%ArrayList result = (ArrayList)pageContext.getAttribute("result",pageContext.PAGE_SCOPE);%>

<display:displaytag start="<%=strStart%>" range="5" content="<%=result%>">

<search:ResultDisplay resultSet="<%= alReturn %>" rowId="UID"  />

</display:displaytag>


</form>
</body>

</html>