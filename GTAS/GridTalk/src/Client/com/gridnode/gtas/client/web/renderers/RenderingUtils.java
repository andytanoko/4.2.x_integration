/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RenderingUtils.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-29     Andrew Hill     Created
 */
package com.gridnode.gtas.client.web.renderers;

//testing
/*import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;*/

import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XHTMLSerializer;
import org.w3c.dom.Document;

public class RenderingUtils
{
  private static final Log _log = LogFactory.getLog(RenderingUtils.class); // 20031209 DDJ

  public final static void renderPipeline(PrintWriter writer, IRenderingPipeline pipeline)
    throws RenderingException
  {
    try
    {

      long startTime = System.currentTimeMillis();
      
      Document finalDocument = pipeline.render();
      writeDocument(finalDocument, writer);
      
      long endTime = System.currentTimeMillis();
      if(_log.isInfoEnabled())
      { 
        _log.info("Completed rendering pipeline (in approx " + (endTime - startTime) + " ms)");        
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering pipeline",t);
    }
  }

  public static void writeDocument(Document document, Writer writer) throws RenderingException
  {
    if(document == null) throw new NullPointerException("document is null"); //20030416AH
    if(writer == null) throw new NullPointerException("writer is null"); //20030416AH
    try
    {
      //Xerces method
      OutputFormat format = new OutputFormat(document);
      format.setIndenting(false);
      format.setLineSeparator("\n");
      format.setPreserveSpace(true);
      format.setOmitComments(false);

      XHTMLSerializer s = new XHTMLSerializer(writer, format);
      s.serialize(document);

/*if(DomViewer._evilFlag)
{
  DomViewer dv = new DomViewer();
  dv.displayInFrame(document);
  //XHTMLSerializer t = new XHTMLSerializer(System.out, format);
  //t.serialize(document);
}*/

      //JAXP Method - slower
      /*DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(writer);
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer serializer = tf.newTransformer();
      //serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
      //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
      //serializer.setOutputProperty(OutputKeys.INDENT,"yes");
      serializer.transform(domSource, streamResult);*/

    }
    catch(Throwable t)
    {
      throw new RenderingException("Error writing document to writer",t);
    }
  }
}