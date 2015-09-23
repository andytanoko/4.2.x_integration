<%@page import="java.util.*,java.io.*"%>
<html>
<head>
<title>
Conversion
</title>
</head>
<body BGCOLOR="#666699">
<h1>
Conversion Generated File
</h1>

<% File f = (File)request.getAttribute("OutputFile");
   BufferedReader reder = new BufferedReader(new FileReader(f));
   String line=null;

   StringBuffer sb1 = new StringBuffer();
    while( (line=reder.readLine())!=null)
    {
      sb1.append(line).append("\n");
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
