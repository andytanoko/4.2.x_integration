<%@page import="java.util.*,java.io.*"%>
<html>
<head>
<title>
Conversion
</title>
</head>
<body BGCOLOR="#666699">
<h1>
Validator Generated Output
</h1>

<%
    StringBuffer sb1 = new StringBuffer();
    String line;
    ArrayList list = (ArrayList)request.getAttribute("Output");
   for(int i=0;i<list.size();i++){
   if(list.get(i) instanceof String){
    line = (String)list.get(i);
    sb1.append(line).append("\n");
   }
   if(list.get(i) instanceof ByteArrayOutputStream){
   ByteArrayOutputStream outp = (ByteArrayOutputStream)list.get(i);
   BufferedReader reder = new BufferedReader(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outp.toByteArray()))));
   while( (line=reder.readLine())!=null)
    {
      sb1.append(line).append("\n");
    }
   }
   }

  line = sb1.toString();
%>
<form method="post">
<br><br>
<textarea name=textara rows=20 cols=50>
<%=line%>
</textarea>
</form>
</body>
</html>
