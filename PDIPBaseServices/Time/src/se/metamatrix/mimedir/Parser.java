/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */
package se.metamatrix.mimedir;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * The main class for a parser of byte-streams of the MIME
 * Directory format as described in RFC2425. The API for this parser
 * was inspired by the Simple API for XML (SAX) as described on
 * http://www.megginson.com/SAX/.
 *
 * @version $Id: Parser.java,v 1.24 2001/07/24 15:04:22 noa Exp $
 * @author Johan Liedberg <johan.liedberg@metamatrix.se>
 * @author Frida Mörtsell <frida.mortsell@metamatrix.se>
 * @author Daniel Reare   <noa@metamatrix.se>
 * 
 */

public class Parser
{
  private ContentHandler contentHandler = null;
  private ErrorHandler eh = null;
  private Hashtable valueFactories = new Hashtable();
  private Hashtable availableFactories = new Hashtable();
  private boolean toExit = false;

  /**
   * Create a parser that uses the given ContentHandler to pass
   * parsed content to the user. Use of this constructor is
   * discouraged since it discards all error messages. For real
   * work, please use the constructor that also takes an
   * ErrorHandler as an argument
   */
  public Parser(ContentHandler content)
  {
    this(content, new DefaultErrorHandler());
  }

  /**
   * Creates a parser that uses the given ContentHandler and
   * ErrorHandler to pass parsed content and errors to the user.
   */
  public Parser(ContentHandler content, ErrorHandler error)
  {
    this.contentHandler = content;
    this.eh = error;

    availableFactories.put("text", new TextValueFactory());
    availableFactories.put("integer", new IntegerValueFactory());
    availableFactories.put("boolean", new BooleanValueFactory());
    availableFactories.put("datetime", new DateTimeValueFactory());

  }

  /**
   * Parses the MIME directory input. When valid input is
   * encountered, the appropriate method of the ContentHandler
   * object is called. If an error is encountered the appropriate
   * method of the ErrorHandler is called
   *
   * @param in the input stream that is read. The stream is
   *           converted to characters according to the standard 
   *           encoding for this platform.
   */
  public void parse(InputStream in) throws IOException
  {
    parse(new InputStreamReader(in));

  }

  /**
   * Parses the MIME directory input. When valid input is
   * encountered, the appropriate method of the ContentHandler
   * object is called. If an error is encountered the appropriate
   * method of the ErrorHandler is called
   *
   * @param in the reader that is read. This gives the user of the
   *           ability to select an alternate character set and/or
   *           encoding.
   */
  public void parse(Reader in) throws IOException
  {

    StringBuffer logicalLine = new StringBuffer(255);
    BufferedReader br = new BufferedReader(in);
    String physicalLine = null;

    // please observe that RFC2425 is more strict than readLine()
    // about what is a newline. (It only accepts CRLF)

    physicalLine = br.readLine();
    if (physicalLine == null)
      return;
    logicalLine.append(physicalLine);

    physicalLine = br.readLine();
    while (physicalLine != null)
    {
      if (physicalLine.length() > 0)
      {
        if (physicalLine.charAt(0) == ' ')
        {
          logicalLine.append(physicalLine.substring(1));
        }
        else
        {
          getKeyValuePairs(logicalLine.toString());
          logicalLine = new StringBuffer(physicalLine);
        }
      }
      else
      {
        eh.error(ErrorHandler.NOTICE, 300, "Found an empty line, skipping");
      }
      physicalLine = br.readLine();
    }
    //if we have anything left after end of input, submit it
    if (logicalLine.length() > 0)
    {
      getKeyValuePairs(logicalLine.toString());
    }
  }

  /**
   * Notifies the parser that an unrecoverable error has been
   * found and that it should exit as soon as possible.
   *
   * @param errormessage a message of what went wrong.
   */
  public void exit(String errormessage)
  {
    toExit = true;
  }

  /**
   * Parses a logical line of mimedir data into keys, values and
   * parameters. The data is then sent to a ContentHandler supplied
   * by the user via the contentLine method. If any errors or
   * inconsitencies is discovered in the input, those are also
   * reported back to the user via the ErrorHandler interface.
   *
   * @param in a string containing the (logical) contentline.
   */

  // todo: we need to handle the special cases where name, value,
  // key and other are of zero length.

  void getKeyValuePairs(String in)
  {
    // this a little bit hairy, mainly because of the fact that
    // various chars with special meaning (':', ';') can be quoted 
    // or escaped.

    final int NAME = 0;
    final int PARAM_NAME = 1;
    final int BEFORE_PARAM_VALUE = 2;
    final int PARAM_VALUE_UNQUOTED = 3;
    final int PARAM_VALUE_QUOTED = 4;
    final int AFTER_PARAM_VALUE = 5;
    final int VALUE = 6;

    boolean nextEscaped = false;

    int state = NAME;
    char cb[] = in.toCharArray();
    String group = null;
    String name = null;
    String paramName = null;
    int start = 0, i;
    Vector paramVector = new Vector();
    Vector values = new Vector();

    for (i = 0; i < cb.length; i++)
    {
      if (state == NAME)
      {
        if (cb[i] == '.')
        {
          group = new String(cb, start, i);
          group = group.toLowerCase();
          start = i + 1;
        }
        else if (cb[i] == ';')
        {
          name = new String(cb, start, i - start);
          name = name.toLowerCase();
          start = i + 1;
          state = PARAM_NAME;
        }
        else if (cb[i] == ':')
        {
          name = new String(cb, start, i - start);
          name = name.toLowerCase();
          start = i + 1;
          state = VALUE;
        }
        else if (cb[i] == ' ')
        {
          eh.error(ErrorHandler.ERROR, 101, "Key contains whitespace");
        }
      }
      else if (state == PARAM_NAME)
      {
        if (cb[i] == '=')
        {
          paramName = new String(cb, start, i - start);
          paramName = paramName.toLowerCase();
          start = i + 1;
          state = BEFORE_PARAM_VALUE;
        }
      }
      else if (state == BEFORE_PARAM_VALUE)
      {
        if (cb[i] == '"')
        {
          state = PARAM_VALUE_QUOTED;
          start = i + 1;
        }
        else
        {
          state = PARAM_VALUE_UNQUOTED;
        }
      }
      else if (state == PARAM_VALUE_UNQUOTED)
      {
        if (cb[i] == ';')
        {
          String paramValue = new String(cb, start, i - start);
          paramVector.add(new Parameter(paramName, paramValue));
          start = i + 1;
          state = PARAM_NAME;
        }
        else if (cb[i] == ':')
        {
          String paramValue = new String(cb, start, i - start);
          paramVector.add(new Parameter(paramName, paramValue));
          start = i + 1;
          state = VALUE;
        }
        else if (cb[i] == ',')
        {
          String paramValue = new String(cb, start, i - start);
          paramVector.add(new Parameter(paramName, paramValue));
          start = i + 1;
          state = BEFORE_PARAM_VALUE;
        }
      }
      else if (state == PARAM_VALUE_QUOTED)
      {
        if (cb[i] == '"')
        {
          String paramValue = new String(cb, start, i - start);
          paramVector.add(new Parameter(paramName, paramValue));
          start = i + 1;
          state = AFTER_PARAM_VALUE;
        }
      }
      else if (state == AFTER_PARAM_VALUE)
      {
        if (cb[i] == ';')
        {
          start = i + 1;
          state = PARAM_NAME;
        }
        else if (cb[i] == ',')
        {
          start = i + 1;
          state = BEFORE_PARAM_VALUE;
        }
        else if (cb[i] == ':')
        {
          start = i + 1;
          state = VALUE;
        }
      }
      else if (state == VALUE)
      {
        if (nextEscaped)
        {
          nextEscaped = false;
        }
        else
        {
          if (cb[i] == '\\')
          {
            nextEscaped = true;
          }
          else if (cb[i] == ',' && getMultiplicity(name))
          {
            values.add(new String(cb, start, i - start));
            start = i + 1;
          }
        }
      }
    }
    if (state == VALUE)
    {
      values.add(new String(cb, start, i - start));
    }
    else
    {
      eh.error(ErrorHandler.ERROR, 103, "No value part " + "the string probably got truncated");
      return;
    }

    //create the parameter array
    Parameter parameters[] = new Parameter[paramVector.size()];
    for (int j = 0; j < parameters.length; j++)
    {
      parameters[j] = (Parameter) paramVector.get(j);
    }
//    //set up the factory
//    ValueFactory vf = (ValueFactory) valueFactories.get(name);
//    if (vf == null)
//    {
//      eh.error(ErrorHandler.WARNING, 202, "No valuetype factory associated with value '" + name + "'. Defaulting to TextValueFactory");
//      vf = new TextValueFactory();
//    }
    for (int j = 0; j < values.size(); j++)
    {
      Object data = values.get(j);
//      try
//      {
//        data = vf.parseMimedirString((String) values.get(j));
//      }
//      catch (ParseException pe)
//      {
//        // todo: detta ger data == null, vilket inte är
//        // optimalt. Kanske är det bättre att skicka en
//        // ErrorHandler till aktuell ValueFactory
//        eh.error(ErrorHandler.ERROR, 102, "Error when parsing data OJ: " + pe.getMessage());
//      }
      contentHandler.contentLine(name, parameters, data, group);
    }
  }

  /**
   * If a valuepart of a string contains a comma, this method is
   * called to see if the parser should split up the string in
   * several contentLines. The default value is false, which means
   * that commas is included in the line passed to the client via
   * contentLine() as they are.
   */
  private boolean getMultiplicity(String name)
  {
//    ValueFactory vf = (ValueFactory) valueFactories.get(name);
//    return vf == null ? false : vf.canHaveMultipleValues();
		return true;
  }

  /**
   * Adds the given array of Value mappings between a value name and
   * a valuetype name to the internal list that the parser keeps of
   * valid value names. The valuetype names should correspond to any
   * of the builtin names "text", "integer" or "boolean". If any
   * user supplied valuetype factories is to be used, they must be
   * indicated to the parser before setValues() is called to be
   * visible. 
   * 
   * @param values an array of Value's representing key and
   *              valuetype name pairs that the parser should be aware of
   * @throws IllegalArgumentException if any of the given values has an
   * unrecognized valueTypeName.
   */
  public void setValues(Value values[]) throws IllegalArgumentException
  {
    String valueTypeName = null;
    ValueFactory factory = null;
    for (int i = 0; i < values.length; i++)
    {
      valueTypeName = values[i].getValueTypeName();
      factory = (ValueFactory) availableFactories.get(valueTypeName);
      if (factory == null)
      {
        throw new IllegalArgumentException("don't know anything about valuetype " + valueTypeName);
      }
      valueFactories.put(values[i].getName(), factory);
    }
  }

  public void setValueTypeFactories(ValueFactory factories[])
  {
    for (int i = 0; i < factories.length; i++)
    {
      availableFactories.put(factories[i].getValueTypeName(), factories[i]);
    }
  }
}