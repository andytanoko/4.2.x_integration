<html>
<title>
Process
</title>
<head>
<link rel="stylesheet" type="text/css" href="include/css.css">
</head>
<script language="Javascript">

	function addRole(roleIndex)
	{
	
	var roleIndex;
	roleIndex = document.frmProcess.sltRole.options[document.frmProcess.sltRole.selectedIndex].value;
	
	
	if(frmProcess.txtName.value.length == 0)
		{
		
		
			document.frmProcess.txtName.value=roleIndex;
		}
		else
		{
		
			document.frmProcess.txtName.value= document.frmProcess.txtName.value +','+ roleIndex

		}
	}

</script>
<body>
<form name="frmProcess">
<table width="800" cellspacing="0">
<tr>
  <td colspan="2" bgcolor="9B9B9B"><a href="home.html"><img src="images/home.gif" border="0"></a><a href="useradmin.html"><img src="images/useradmin.gif" border="0"></a><a href="report.html"><img src="images/report.gif" border="0"></a><a href="tools.html"><img src="images/tools.gif" border="0"></a><a href="help.html"><img src="images/help.gif" border="0"></a><a href="exit.html"><img src="images/exit.gif" border="0"></a></td>
</tr>
<tr>
  <td width="100" bgcolor="365D93">
<br><br>
<a href="workbench.html">Workbench</a> <br><br>

<a href="useroffice.html">User Office</a> <br><br>

<a href="view.html">View</a> <br><br>

<a href="search.html">Search</a> <br><br>
<br><br>
</td>
  <td width="500" align="center">&nbsp;

<form Name="frmUser">
<table width="500" cellspacing="0">
  <tr valign="top">
  <td class="heading" width="100%"> Process Role Map</td>
  </tr>
  
  
  <tr><td>&nbsp;</td></tr>
  <tr>
  <td>Role</td><td><select name="sltProcess" class="select"> 
	
	<option value="Process1">Process1</option>
	<option value="Process2">Process2</option>
	<option value="Process3">Process3</option>
	<option value="Process4">Process4</option>
	<option value="Process5">Process5</option></select>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td>Participants</td></tr>
  <tr>
	  <td>Participant1</td>
	  <td><Input type="text" name="txtName" style="WIDTH: 250px;" class="text"></td>
	  <td><select name="sltRole" class="select">
	  <option value="Role1">Role1</option>
	  <option value="Role2">Role2</option>
	  <option value="Role3">Role3</option>
	  <option value="Role4">Role4</option>
	  <option value="Role5">Role5</option></select>
	  </td>
	  <td><input type="button" value="Add" class="button" onClick="addRole();"></td>
  </tr>
  </table>
 <tr>
     <td colspan="2" bgcolor="9B9B9B">&nbsp;</td>
   </tr>
  </table>
  </form>
    </body>
</html>