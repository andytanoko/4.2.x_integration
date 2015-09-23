<%@page import="java.util.*,java.io.*"%>
<html>
<head>
<title>
Conversion
</title>
</head>
<body BGCOLOR="#666699">
<h1>
Transformer Generated Output
</h1>

<%
    StringBuffer sb1 = new StringBuffer();
    String line;
    ByteArrayOutputStream outp = (ByteArrayOutputStream)request.getAttribute("Output");
    if(outp != null){
    BufferedReader reder = new BufferedReader(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outp.toByteArray()))));
    while( (line=reder.readLine())!=null)
    {
      sb1.append(line).append("\n");
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
