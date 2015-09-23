<%@ taglib uri="/search" prefix="search" %>

<%@ page import="java.util.ArrayList,com.gridnode.pdip.base.search.helpers.SearchFilterOperator"%>

<HTML>
<HEAD>
<TITLE>Search </TITLE>
</HEAD>
<BODY>
<form name="frmForm" method="POST">
<% 
	ArrayList list		= new ArrayList();
	list.add("iCal_COMP_ID");
	list.add("DATE_VALUE");

%>

<search:FieldDisplay tableName="Address" fieldName="FirstName" field="FirstName" compOperator="<%=SearchFilterOperator.EQUAL_OPERATOR%>"  value="LastName" negate="<%=false%>" isFieldValue="<%=true%>" distinct="<%=true%>"  />


</form>
</body>

</html>