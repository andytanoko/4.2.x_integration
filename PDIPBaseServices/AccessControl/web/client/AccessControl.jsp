<html>
<title>
Access Control
</title>
<head>
<link rel="stylesheet" type="text/css" href="include/css.css">
</head>

<body>
<form name="frmAccess">
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
  <td class="heading" width="100%"> Access Control Administration</td>
  </tr>
  
  
  <tr><td>&nbsp;</td></tr>
  <tr>
  <td>Role</td><td><select name="Name" class="select"> 
	<option value="All">All</option>
	<option value="Role1">Role1</option>
	<option value="Role2">Role2</option>
	<option value="Role3">Role3</option>
	<option value="Role4">Role4</option>
	<option value="Role5">Role5</option></select>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
  <td>
  Features
  </td>
  <td>
  </td>
  <td>
  Actions
  </td>
  </tr>
  <tr>
  <td>
  <select multiple name="sltFeatures" class="select" style="WIDTH: 145px; HEIGHT: 140px" onChange=" redirect(this.options.selectedIndex) ">
  <option value="Feature1">Feature1</option>
  <option value="Feature2">Feature2</option>
  <option value="Feature3" selected >Feature3</option>
  </td>
  <td>
  </td>
  <td>
  <select multiple name="sltActions" class="select" style="WIDTH: 145px; HEIGHT: 140px">
  <option value="Action5">Action5</option>
  <option value="Action6">Action6</option>
 </td>
  </tr>
  <tr>
  <td>
  <Input type="button" class="button" value="Grant">
  <Input type="button" class="button" value="Deny"></td>
  </table>
  <br><br>
  </td>
  </tr>
  <tr>
    <td colspan="2" bgcolor="9B9B9B">&nbsp;</td>
  </tr>
  </table>
  <Script Language="javascript">
  	var groups = document.frmAccess.sltFeatures.options.length;
  	var group=new Array(groups)
  	for (i=0; i<groups; i++)
  	group[i]=new Array()
  	
  	group[0][0]=new Option("Action1","Action1")
  	group[0][1]=new Option("Action2","Action2")
  	
  	group[1][0]=new Option("Action3","Action3")
  	group[1][1]=new Option("Action4","Action4")
  	
  	group[2][0]=new Option("Action5","Action5")
  	group[2][1]=new Option("Action6","Action6")
  	
  	var temp=document.frmAccess.sltActions
  	
  	function redirect(x){
  	
  	
  	for (m=temp.options.length-1;m>0;m--)
  	temp.options[m]=null
  	for (i=0;i<group[x].length;i++){
  	temp.options[i]=new Option(group[x][i].text,group[x][i].value)
  	}
  	temp.options[0].selected=true
  }
</script>
  </form>
  </body>
</html>