<html>
<title>
User Management
</title>
<head>
<link rel="stylesheet" type="text/css" href="include/css.css">
<script src="include/movemenu.js">
</script>
</head>

<body>

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
<table width="400" cellspacing="0">
  <tr valign="top">
  <td class="heading" width="100%"> Role Configuration</td>
  </tr>
  
  <tr><td>&nbsp;</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
  <td>Name</td><td><select name="Name" class="select"> 
	<option value="choice1">All</option>
	<option value="choice2">Celestica Malaysia</option>
	<option value="choice3">Juroung Hi Tech</option>
	<option value="choice4">Kaifa</option>
	<option value="choice5">Read-Rite</option>
	<option value="choice6">Box Global</option></select>
    </td>
  </tr>
  <tr>
  <td>Description</td>
  <td><input type="text" name="txtDescription"></td>
  </tr>
  <tr><td valign="top">All Users</td><td></td><td>Associated Users</td></tr>
  <tr><td><select multiple name="sltAllUsers"  class="select" style="WIDTH: 145px; HEIGHT: 140px">
  <option value="User1">User1</option>
  <option value="User2">User2</option>
  <option value="User3">User3</option>
  <option value="User4">User4</option>
  <option value="User5">User5</option>
  </td>
  <td align="middle" >
  	
		<NOBR>   
		              <input type="button" style="width:90" class="button" onclick="moveDualList( document.frmUser.sltAllUsers,  document.frmUser.sltAssUsers, false )"  
		              name="Add     >>"  value="Add       >>">     <BR>
		              <NOBR>       
		              <input type="button" style="width:90" class="button" onclick="moveDualList( document.frmUser.sltAssUsers, document.frmUser.sltAllUsers,  false )"  
		              name="Add     <<"  value="Add       <<">     <BR>
		              <NOBR>       
		              <input type="button" style="width:90" class="button" onclick="moveDualList( document.frmUser.sltAllUsers,  document.frmUser.sltAssUsers, true  )"  
		              name="Add All >>"  value="Add All >>">     <BR>
		              <NOBR>       
		              <input type="button" style="width:90" class="button" onclick="moveDualList( document.frmUser.sltAssUsers,  document.frmUser.sltAllUsers,  true  )"  
		              name="Add All <<"  value="Add All <<">     <BR>
              </NOBR>
	
  </td>
  <td>
  <select multiple name="sltAssUsers"  class="select" style="WIDTH: 145px; HEIGHT: 140px">
  	
  </td>
  </tr>
  <tr><td valign="top"> Access List</td><td></td><td> Associated Access</td></tr>
  <tr><td><select multiple name="sltAllAccess"  class="select" style="WIDTH: 145px; HEIGHT: 140px">
    <option value="User1">Access1</option>
    <option value="User2">Access2</option>
    <option value="User3">Access3</option>
    <option value="User4">Access4</option>
    <option value="User5">Access5</option>
    </td>
    <td align="middle" >
    	
  		<NOBR>   
  		              <input type="button" style="width:90" class="button" onclick="moveDualList( document.frmUser.sltAllAccess,  document.frmUser.sltAssAccess, false )"  
  		              name="Add     >>"  value="Add       >>">     <BR>
  		              <NOBR>       
  		              <input type="button" style="width:90" class="button" onclick="moveDualList( document.frmUser.sltAssAccess, document.frmUser.sltAllAccess,  false )"  
  		              name="Add     <<"  value="Add       <<">     <BR>
  		              <NOBR>       
  		              <input type="button" style="width:90" class="button" onclick="moveDualList( document.frmUser.sltAllAccess,  document.frmUser.sltAssAccess, true  )"  
  		              name="Add All >>"  value="Add All >>">     <BR>
  		              <NOBR>       
  		              <input type="button" style="width:90" class="button" onclick="moveDualList( document.frmUser.sltAssAccess,  document.frmUser.sltAllAccess,  true  )"  
  		              name="Add All <<"  value="Add All <<">     <BR>
                </NOBR>
  	
    </td>
    <td>
    <select multiple name="sltAssAccess"  class="select" style="WIDTH: 145px; HEIGHT: 140px">
    	
    </td>
  </tr>

  </tr>
</table>
<br><br>
</td>
</tr>
<tr>
  <td colspan="2" bgcolor="9B9B9B">&nbsp;</td>
</tr>
</table>
</form>
</body>
</html>