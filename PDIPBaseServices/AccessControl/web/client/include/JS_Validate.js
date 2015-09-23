


function isEmpty(sStr)
{
	//alert("insidejs");
	return((sStr==null) || (sStr.length==0));
}





function isNumeric(nNum)
{
	var reNumeric = /^(0|[1-9][0-9]*)$/;
	return(reNumeric.test(nNum));
}





function isAlphanumeric(sStr)
{
	var reAlphaNumeric = /^[a-zA-Z0-9 ]+$/;
	return(reAlphaNumeric.test(sStr));
}










function isAlphabetic(sStr)
{
	var reAlphabetic = /^[a-zA-Z]+$/;
	return(reAlphabetic.test(sStr));
}










