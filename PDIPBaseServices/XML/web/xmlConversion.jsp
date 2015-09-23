<%@page import="java.io.*,java.util.*,com.gridnode.xml.*,com.gridnode.xml.control.*,com.gridnode.xml.control.ejb.*, com.gridnode.xml.control.event.*, com.gridnode.xml.control.exceptions.*,com.gridnode.xml.control.util.*,com.gridnode.xml.helpers.*,com.gridnode.xml.helpers.convertor.*,com.gridnode.xml.helpers.convertor.readers.*"%>
<html>
<head>
<title>
xmlConversion
</title>
</head>
<body BGCOLOR="#666699">
<h1>
<%
String conv = (String)request.getParameter("conversion");
%>
<%=conv%> XML File
</h1>
<form method="post">
<br>
<%
  StringBuffer sb = (StringBuffer)request.getAttribute("content");
%>
<textarea name=textara rows=20 cols=50>
<%
if(sb==null)
 {

    String srcFile = (String)request.getParameter("fileup");
    String rlsFile = (String)request.getParameter("fileup1");
    String outFile = (String)request.getParameter("output");
    srcFile = srcFile.replace('\\','/');
    rlsFile = rlsFile.replace('\\','/');
    outFile = outFile.replace('\\','/');
    System.out.println(" From Jsp File .. Name "+srcFile);
    System.out.println(" From Jsp File .. Name "+rlsFile);
    System.out.println(" From Jsp File .. Name "+outFile);

    com.gridnode.xml.control.util.XmlClientControllerTestClient1 client = new  com.gridnode.xml.control.util.XmlClientControllerTestClient1();
    client.xmlClientController = client.create();
    com.gridnode.xml.control.event.ConvertXmlEvent conEvent = new com.gridnode.xml.control.event.ConvertXmlEvent();
    conEvent.ConvertXmlEvent(com.gridnode.xml.control.event.ConvertXmlEvent.CONVERT_XML,srcFile,outFile,rlsFile);
    System.out.println(" Event Name is .... ... "+conEvent.getEventName());
    java.util.Collection col = client.handleEvent(conEvent);
    Iterator ite = col.iterator();
    while(ite.hasNext())
    {
      System.out.println(" Updated are ... "+ite.next());
    }
    BufferedReader red = new BufferedReader(new InputStreamReader(new FileInputStream("c:/jaggs/posorder.xml")));
    String line=null;
    StringBuffer sb1 = new StringBuffer();
    while( (line=red.readLine())!=null)
    {
    sb1.append(line);
    }

  line = sb1.toString();
   request.setAttribute("content",null);
 %>
 <%=line%>
<%}
%>
 </textarea>




<!--
<input type="submit" name="Submit" value="Submit" >
<input type="reset" value="Reset">
-->
</form>
</body>
</html>
