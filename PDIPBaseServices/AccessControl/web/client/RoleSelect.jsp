<html>
<title>
User Management
</title>
<head>
<link rel="stylesheet" type="text/css" href="include/css.css">
</head>
<script language="javascript">

	
	function Select(userSelect)
			{
				
				UserId = userSelect;
				sFlag = "set";
			}
	function send()
	{
	
	
	  if (sFlag != "set") 
	  	{
	  		alert("Please Select the user");
	  		return;
		}
	  window.opener.frmUser.sltUsers.value = UserId;
	  window.close();
	
	}
	
	
	
	
</script>
<body>
<form name="frmRolelist">
<table>
<tr>
<td class="heading" >#</td><td class="heading">User Name</td>
</tr>
<tr><td class="normal"><input type="radio" value="user2" onClick="Select('User2')" name="optuser"></td><td class="normal">User2</td></tr>
<tr><td class="normal"><input type="radio" value="user3" onClick="Select('User3')" name="optuser"></td><td class="normal">User3</td></tr>
<tr><td class="normal"><input type="radio" value="user4" onClick="Select('User4')" name="optuser"></td><td class="normal">User4</td></tr>
<tr><td class="normal"><input type="radio" value="user5" onClick="Select('User5')" name="optuser"></td><td class="normal">User5</td></tr>
<tr><td class="normal"><input type="Button"  class=button value="select" onClick="send()"></td><td class="normal"><input type=button name=close value=Close class=button onclick ="window.close()"></td></tr>
</table>
</form>
</body>
</html>