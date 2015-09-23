package com.gridnode.transport.communication;

/**
 * Title:        GridTalk Transport Controller
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      GridNode Pte. Ltd.
 * @author Goh Kan Mun
 * @version 1.0
 */

//import com.gridnode.security.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;

import com.gridnode.transport.ObjectToString;
 
/**
 * This contains the contents of a message sent/received by the transport layer.
 * This class deals with physically encoding the data to be sent (eg: send
 * it as a string, byteArray, etc), and decoding it when it is received.
 *
 * A GridEvent is currently encoded as a fixed length string, with integer
 * values as hex digits.
 *
 */
public class GridEvent //implements IGridEventDef
{
  // Data to be sent
  private Date date = null;
  private String[] eventData = null;
  private int eventDataCount = 0;
  private int eventID = 0;
  private int eventType = 0;
  private int fileDataCount = 0;
  private int functionID = 0;
  private String language = "EN";
  private int processID = 0;
  private Integer recipientGM = null;
  private Integer recipientNodeID = null;
  private int route = 0;
  private Integer senderGM = null;
  private Integer senderNodeID = null;

  private String gnci = null;

  // The default version is the lowest, so if none is assigned we use the
  // oldest version.
  private String VERSION_NUMBER =  "010000";
  private int VERSION_NUMBER_VALUE = 10000;

  /*------------------------------------------------------
  -- Remember to change these two every release !!!!    --
  ------------------------------------------------------*/
  private final String LATEST_VERSION_NUMBER = "010001";
  private int LATEST_VERSION_NUMBER_VALUE = 10001;


  private int eventSubID = 0;

  // Indexes into the arrays below.  Each one represents one of the
  // fields above.
  static private int LENGTH_FIELD = 0;
  static private int VERSION_FIELD = 1;
  static private int LANGUAGE_FIELD = 2;
  static private int ROUTE_FIELD = 3;
  static private int EVENT_TYPE_FIELD = 4;
  static private int EVENT_ID_FIELD = 5;
  static private int FUNCTION_ID_FIELD_FIELD = 6;
  static private int PROC_ID_FIELD = 7;
  static private int SENDER_NODE_ID_FIELD = 8;
  static private int SENDER_GM_FIELD = 9;
  static private int RECEPIENT_NODE_ID_FIELD = 10;
  static private int RECEPIENT_GM_FIELD = 11;
  static private int DATE_TIME_FIELD = 12;
  static private int EVENT_DATA_COUNT_FIELD = 13;
  static private int FILE_DATA_COUNT_FIELD = 14;
  static private int EVENT_SUB_ID_FIELD = 15;

  static private String NULL_STRING = "_NULL";
  static private String SHORT_NULL_STRING = "_N";

  // Indexes into the arrays below.
  //static private int START_SUBSTR_INDEX = 0;
  //static private int END_SUBSTR_INDEX = 1;

  // Array saying where different elts start and stop in the string, when the
  // GridEvent is converted to/from a string in getEvent() and getEventString().
  // eg: The 'route' field starts at
  // NUM_BYTES_FOR_EVENTS[ROUTE_FIELD][START_SUBSTR_INDEX] and has its last
  // character at ( NUM_BYTES_FOR_EVENTS[ROUTE_FIELD][END_SUBSTR_INDEX] -1 )
  //
  // Note, this array is for version "010000" (ie: 10000 ).
  static private int NUM_BYTES_FOR_EVENTS_VER_010000[][] = {
    {0,3},    // Length
    {3,9},    // Version Number
    {9,11},   // Language
    {11,13},  // Route
    {13,21},  // Event Type
    {21,29},  // Event Id
    {29,37},  // Function Id
    {37,45},  // Process Id
    {45,53},  // Sender Id
    {53,55},  // Sender GM
    {55,63},  // Recepient Id
    {63,65},  // Recepient GM
    {65,81},  // DateTime
    {81,83},  // Event Data Count
    {83,85},  // File Data Count
    {85,93}   // Event Sub-Id
  };
  static private int TOTAL_NUM_BYTES_VER_010000 = 93;

  // This array is the array for version "010001" (ie: 10001 ).
  static private int NUM_BYTES_FOR_EVENTS_VER_010001[][] = {
    {0,3},    // Length
    {3,9},    // Version Number
    {9,11},   // Language
    {11,19},  // Route
    {19,27},  // Event Type
    {27,35},  // Event Id
    {35,43},  // Function Id
    {43,51},  // Process Id
    {51,59},  // Sender Id
    {59,67},  // Sender GM
    {67,75},  // Recepient Id
    {75,83},  // Recepient GM
    {83,99},  // DateTime
    {99,107},  // Event Data Count
    {107,115},  // File Data Count
    {115,123}   // Event Sub-Id
  };
  static private int TOTAL_NUM_BYTES_VER_010001 = 123;

  private int TOTAL_NUM_BYTES;  // Set by constructor, its value will
                                // depend on the version.  Once set is not
                                // changed.


  /**
   * Private constructor.  Only used by getEvent().
   */
  private GridEvent(Integer senderNodeID, Integer recipientNodeID)
  {
    // No need to check range of sender + recepient node ids,
    // because the 8 characters assigned can hold a MAX_INT.
    // They can also be null.
    this.senderNodeID = senderNodeID;
    this.recipientNodeID = recipientNodeID;
    setTotalNumBytes();
  }

  /**
   * 1 of 3 public constructors
   */
  public GridEvent(Integer senderNodeID, Integer recipientNodeID, int eventID,
                   String[] eventData, String language, int functionID, int processID,
                   int eventType)
  {
    // No need to check range of sender + recepient node ids,
    // because the 8 characters assigned can hold a MAX_INT.
    // They can also be null.
    this.senderNodeID = senderNodeID;
    this.recipientNodeID = recipientNodeID;

    this.setEventID(eventID);
    this.setEventData(eventData);

    if (eventData == null)
      this.setEventDataCount(0);
    else
      this.setEventDataCount(eventData.length);

    this.setFunctionID(functionID);
    this.setProcessID(processID);
    Date d = new Date(0);
    this.setDate( d );   /* TBD : get date from Transport Controller */
    this.setEventType(eventType);

    setTotalNumBytes();
    this.setLanguague(language);
  }

  /**
   * 2nd public constuctor.  Same as first, but it also sets eventSubId.
   */
  public GridEvent(Integer senderNodeID, Integer recipientNodeID, int eventID,
                   String[] eventData, String language, int functionID,
                   int processID, int eventType, int eventSubID)
  {
    this(senderNodeID, recipientNodeID, eventID, eventData, language,
         functionID, processID, eventType);
    this.setEventSubID(eventSubID);
  }

  /**
   * 3nd public constuctor.  Same as 2nd, but it also sets GNCI.
   */
  public GridEvent(Integer senderNodeID, Integer recipientNodeID, int eventID,
                   String[] eventData, String language, int functionID,
                   int processID, int eventType, int eventSubID, String gnci)
  {
    this(senderNodeID, recipientNodeID, eventID, eventData, language,
         functionID, processID, eventType);
    this.setEventSubID(eventSubID);
    this.setGNCI( gnci );
  }

  public void setGNCI( String gnci )
  {
    this.gnci = gnci;
  }

  public String getGNCI()
  {
    return( this.gnci );
  }

  /**
  * WARNING: senderNodeId may be null
  */
  public Integer getSenderNodeID()
  {
    return senderNodeID;
  }

  /**
  * WARNING: RecepientNodeId may be null.
  */
  public Integer getRecipientNodeID()
  {
    return recipientNodeID;
  }

  public int getEventID()
  {
    return eventID;
  }

  public int getEventSubID()
  {
    return eventSubID;
  }

  /**
   * The array returned may be null.
   * Individual elements in the array may also be null.
   */
  public String[] getEventData()
  {
    return eventData;
  }

  /**
   * The data returned may be null.
   */
  public String getEventData(int position)
  {
    return eventData[position];
  }

  public int getEventDataCount()
  {
    return eventDataCount;
  }

  public int getProcessID()
  {
    return processID;
  }

  public int getFunctionID()
  {
    return functionID;
  }

  public int getEventType()
  {
    return eventType;
  }

  public String getLanguague()
  {
    return language;
  }

  public String getVersionNumber()
  {
    return VERSION_NUMBER;
  }

  public int getRoute()
  {
    return route;
  }

  /**
  * WARNING : Date may be null
  */
  public Date getDate()
  {
    return date;
  }

  /**
   * WARNING: If no value set for SenderGM, returns null.
   */
  public String getSenderGM()
  {
    if (senderGM!=null)
      return( senderGM.toString() );
    else
      return( null );
  }

  /**
   * WARNING: If no value set for SenderGM, returns null.
   */
  public Integer getSenderGMInt()
  {
    return( senderGM );
  }

  /**
   * WARNING: If no value set for RecepientGM, returns null.
   */
  public String getRecipientGM()
  {
    if (recipientGM!=null)
      return recipientGM.toString();
    else
      return( null );
  }

  /**
   * WARNING: If no value set for RecepientGM, returns null.
   */
  public Integer getRecipientGMInt()
  {
    return( recipientGM );
  }

  public int getFileDataCount()
  {
    return fileDataCount;
  }

  /*----------------------------------------------------
   -- Private methods to set the GridEvent elements.  --
   ----------------------------------------------------*/

  private void setEventID(int eventID)
  {
    this.eventID = eventID;
  }

  public void setEventSubID(int eventSubID)
  {
    this.eventSubID = eventSubID;
  }

  private void setEventData(String[] eventData)
  {
    this.eventData = eventData;
  }
  /*
  private void setEventData(String eventData, int position)
  {
    this.eventData[position] = eventData;
  }*/

  private void setEventDataCount(int eventDataCount)
  {
    this.eventDataCount = eventDataCount;
  }

  private void setProcessID(int processID)
  {
    this.processID = processID;
  }

  private void setFunctionID(int functionID)
  {
    this.functionID = functionID;
  }

  private void setEventType(int eventType)
  {
    this.eventType = eventType;
  }

  private void setLanguague(String language)
  {
    if ( (language==null) ||
         (language.length()>this.getLengthOfField(LANGUAGE_FIELD)) ) {
    }
    else {
      this.language = language;
    }
  }

  /**
   * This method used to set the version number.  Version number must be
   * a 6 char string of digits or else exception is thrown.
   * If not called, will default to "010000".
   *
   * @throw IllegalArgumentException
   * @throw NumberFormatException
   */
  public void setVersionNumber(String versionNumber)
  {
    if ( (versionNumber==null) ||
         (versionNumber.length()!=6) ) {
      throw new IllegalArgumentException("Version null or incorrect");
    }
    else {
      this.VERSION_NUMBER = versionNumber;
      this.VERSION_NUMBER_VALUE = Integer.parseInt(versionNumber);

      // If the version is later than the current latest one (eg: version x
      // sending to version x+1), set it to the current latest one (x).
      if (this.VERSION_NUMBER_VALUE > this.LATEST_VERSION_NUMBER_VALUE )
      {
        this.VERSION_NUMBER = this.LATEST_VERSION_NUMBER;
        this.VERSION_NUMBER_VALUE = this.LATEST_VERSION_NUMBER_VALUE;
      }
    }
    this.setTotalNumBytes();
  }

  private void setRoute(int route)
  {
    this.route = route;
  }

  /**
   * Used by test driver only.
   * utcTimeMs is the time returned from a call to Date.getTime().
   */
  private void setDate(Date d) {
    this.date = d;
  }

  /**
   * If string is null, date will be set to null.
   *//*
  private void setDate(String date)
  {
    if (date==null)
      this.date = null;
    else
      this.date = Date.valueOf(date);
  }*/

  private void setSenderGM(Integer senderGM)
  {
      this.senderGM = senderGM;
  }

  private void setRecipientGM(Integer recipientGM)
  {
    this.recipientGM = recipientGM;
  }

  private void setFileDataCount(int fileDataCount)
  {
    this.fileDataCount = fileDataCount;
  }

  /* to compile

  static public Object getEncryptedEvent(String event, byte[] sessionKey)  //encrypt with sessionKey
  {
    IEncryptDecryptEventMgr en = EncryptDecryptEventMgr.getInstance();
    return en.encryptEvent(sessionKey, event);
  }

  static public Object getEncryptedEvent(String event, String productKey)  //productKey
  {
    IEncryptDecryptEventMgr en = EncryptDecryptEventMgr.getInstance();
    return en.encryptEvent(productKey, event);
  }

  static public String getDecryptedEvent(byte[] sessionKey, byte[] encryptedEvent)
  {
    IEncryptDecryptEventMgr de = EncryptDecryptEventMgr.getInstance();
    return de.decryptEvent(sessionKey, encryptedEvent);
  }

  static public String getDecryptedEvent(String productKey, byte[] encryptedEvent)
  {
    IEncryptDecryptEventMgr de = EncryptDecryptEventMgr.getInstance();
    return de.decryptEvent(productKey, encryptedEvent);
  }

  to compile */

  /**
  * Converts the data of the event passed in to a string.  This method is
  * kept so it will compile with older code.
  */
  static public String getEventString(GridEvent event)
  {
    return( event.getEventString() );
  }

  /**
   * Returns a string of specified length which is used to specify a null value.
   * The null string will be :
   *   If a string of length < 5 is required, the substring "_N" padded
   *   to the right with spaces.
   *   If a string of length >= 5 needed, the substring "_NULL" padded
   *   to the right with spaces.
   *
   * Length MUST be >= 2.
   */
  static private String getNullString(int length)
  {
    int i;
    String ret = "";
    if (length < 5) {
      ret = SHORT_NULL_STRING;
      // Pad the SHORT NULL STRING to the desired length
      for(i=2; i<length; i++) {
        ret = ret.concat(" ");
      }
    }
    else
    {
      ret = NULL_STRING;
      // Pad the NULL STRING to the desired length
      for(i=5; i<length; i++) {
        ret = ret.concat(" ");
      }
    }
    return(ret);
  }

  /**
   * String 's' must NOT be null.
   *
   * Checks to see if the string passed in is a string representing a null value
   * created by the getNullString method().
   */
  static private boolean isNullString(String s)
  {
    //boolean ret = true;

    if (s.length() < 2) {
      return( false );
    }

    if (s.length() < 5) {
      if ( s.substring(0,2).compareTo(SHORT_NULL_STRING) != 0 ) {
        return( false );
      }

      for(int i=2; i<s.length(); i++) {
        if ( s.charAt(i) != ' ' ) {
          return( false );
        }
      }

    }
    else
    {
      if ( s.substring(0,5).compareTo( NULL_STRING ) != 0 ) {
        return( false );
      }

      for(int i=5; i<s.length(); i++) {
        if ( s.charAt(i) != ' ' ) {
          return( false );
        }
      }

    }

    return( true );
  }

  /**
   * Returns the total number of bytes in the string representing this
   * GridEvent.  Based on the version number.
   *
   * The version number should have been set before calling this.
   */
  private void setTotalNumBytes()
  {
    switch ( this.VERSION_NUMBER_VALUE )
    {
      case(10000) :
        this.TOTAL_NUM_BYTES = TOTAL_NUM_BYTES_VER_010000;
        //System.out.println("**0** Total num bytes set to " + this.TOTAL_NUM_BYTES );
        break;
      case(10001) :
        this.TOTAL_NUM_BYTES = TOTAL_NUM_BYTES_VER_010001;
        //System.out.println("**1** Total num bytes set to " + this.TOTAL_NUM_BYTES );
        break;
    }
  }

  /**
   * Returns the length of the field passed in, in the string representing
   * this GridEvent.
   *
   * The version number should have been set before calling this method.
   *
   * @param fieldIndex The index representing which field we are getting the
   * length for (eg: EVENT_ID_FIELD).
   */
  private int getLengthOfField(int fieldIndex) {
    int ret = 0;
    //System.out.println("Version number value = " + this.VERSION_NUMBER_VALUE );
    switch ( this.VERSION_NUMBER_VALUE )
    {
      case(10000) :
        ret = NUM_BYTES_FOR_EVENTS_VER_010000[fieldIndex][1] -
          NUM_BYTES_FOR_EVENTS_VER_010000[fieldIndex][0];
        break;
      case(10001) :
        ret = NUM_BYTES_FOR_EVENTS_VER_010001[fieldIndex][1] -
          NUM_BYTES_FOR_EVENTS_VER_010001[fieldIndex][0];
        break;
    }
    //System.out.println("fieldLen of field " + fieldIndex + " = " + ret );
    return(ret);
  }

  /**
   *  Returns the Integer passed in as a string (in hex).  The string returned
   *  has the length specified, and is padded by zeros.  If the Integer is null,
   *  then null string is returned.
   */
  private String getIntegerValueAsString(Integer i, int lengthOfReturnedString) {
    if (i==null) {
      return( getNullString( lengthOfReturnedString ) );
    }
    else {
      return( ObjectToString.padString(
        Integer.toString( i.intValue(), 16), lengthOfReturnedString, '0', true ) );
    }
  }

  /**
   *  Returns the String passed in as an Integer object.  Integer returned
   *  may be null.
   */
  static private Integer getIntegerValueFromString(String s) {
    if ( isNullString(s) ) {
      return(null);
    }
    else {
      return( Integer.valueOf(s,16) );
    }
  }

  /**
  * Encodes this event's data to a string.
  */
  public String getEventString()
  {
    StringBuffer strEvent = new StringBuffer();

    strEvent.append( ObjectToString.padString(
      Integer.toString(this.TOTAL_NUM_BYTES, 16),
      getLengthOfField(LENGTH_FIELD), '0', true) );

    strEvent.append( ObjectToString.padString(
      getVersionNumber(), getLengthOfField(VERSION_FIELD), '0', true) );
    strEvent.append( ObjectToString.padString(
      getLanguague(), getLengthOfField(LANGUAGE_FIELD),' ', false) );
    strEvent.append( ObjectToString.padString(
      Integer.toString(getRoute(), 16), getLengthOfField(ROUTE_FIELD), '0', true) );
    strEvent.append( ObjectToString.padString(
      Integer.toString( getEventType(), 16 ), getLengthOfField(EVENT_TYPE_FIELD), '0', true ) );
    strEvent.append( ObjectToString.padString(
      Integer.toString( getEventID(), 16 ), getLengthOfField(EVENT_ID_FIELD), '0', true ) );

    String tmp = ObjectToString.padString( Integer.toString( getFunctionID(), 16 ), getLengthOfField(FUNCTION_ID_FIELD_FIELD), '0', true );
    strEvent.append( tmp );

    strEvent.append( ObjectToString.padString(
      Integer.toString( getProcessID(), 16 ), getLengthOfField(PROC_ID_FIELD), '0', true ) );

    strEvent.append(
      getIntegerValueAsString(
        getSenderNodeID(), getLengthOfField(SENDER_NODE_ID_FIELD) ) );

    strEvent.append(
      getIntegerValueAsString(
        getSenderGMInt(), getLengthOfField(SENDER_GM_FIELD) ) );

    strEvent.append(
      getIntegerValueAsString(
        this.getRecipientNodeID(), getLengthOfField(RECEPIENT_NODE_ID_FIELD) ) );

    strEvent.append(
      getIntegerValueAsString(
        this.getRecipientGMInt(), getLengthOfField(RECEPIENT_GM_FIELD) ) );

    // Date and time : UTC : milliseconds since epoch.  It may be null.
    String tmpStr;
    if ( getDate() == null )
    {
      tmpStr = getNullString(getLengthOfField(DATE_TIME_FIELD));
      strEvent.append( tmpStr );
    }
    else
    {
      long l = getDate().getTime();
      tmpStr = ObjectToString.padString( Long.toString(l,16),
                 getLengthOfField(DATE_TIME_FIELD), '0', true );
      strEvent.append( tmpStr );
    }
    //System.out.println("DateTime was [" + tmpStr + "]" );

    tmpStr = ObjectToString.padString(
      Integer.toString( getEventDataCount(), 16 ),
        getLengthOfField(EVENT_DATA_COUNT_FIELD),
        '0', true );
    strEvent.append( tmpStr );
    //System.out.println("EventDataCount = [" + tmpStr + "]" );

    strEvent.append( ObjectToString.padString(
      Integer.toString( getFileDataCount(), 16 ),
      getLengthOfField(FILE_DATA_COUNT_FIELD),
      '0', true ) );

    strEvent.append( ObjectToString.padString(
      Integer.toString( this.getEventSubID(), 16 ), getLengthOfField(EVENT_SUB_ID_FIELD), '0', true ) );

    // Next, add the gnci.
    if (this.gnci==null)
    {
      strEvent.append( "0005" );
      strEvent.append( getNullString(5) );
    }
    else
    {
      strEvent.append( ObjectToString.padString(
        Integer.toString( this.gnci.length() ), 4, '0', true ) );
      strEvent.append( this.gnci );
    }

    // Add all the data to the string.  For each data element, save the
    // length of the data element, and the element itself as a string.
    String currentData;
    for(int i=0; i<getEventDataCount(); i++) {
      currentData = getEventData(i);
      if (currentData==null) {
        strEvent.append( "0005" );
        strEvent.append( getNullString(5) );
      }
      else {
        strEvent.append( ObjectToString.padString(
          Integer.toString( currentData.length() ), 4, '0', true ) );
        strEvent.append( currentData );
      }
    }

    return strEvent.toString();
  }

  /**
   * Version number is an int representing the version.
   * eg: 10000 for version "010000".
   */
  static private String getFieldFromString(String s, int fieldId, int versionNo) {
    String ret = null;
    switch ( versionNo )
    {
      case(10000) :
        ret = s.substring(NUM_BYTES_FOR_EVENTS_VER_010000[fieldId][0],
          NUM_BYTES_FOR_EVENTS_VER_010000[fieldId][1]);
        break;
      case(10001) :
        ret = s.substring(NUM_BYTES_FOR_EVENTS_VER_010001[fieldId][0],
          NUM_BYTES_FOR_EVENTS_VER_010001[fieldId][1]);
        break;
    }
    return(ret);
  }

  /**
   * Constructs and returns a new GridEvent, with data from the string 'event'.
   * This string would have been created by the getEventString() method.
   */
  static public GridEvent getEvent(String event)
  {
  /*
              System.out.print("Str index [" );
              for(int  x=0; x<8;x++) {
                for(int y=0; y<10; y++) {
                  System.out.print(y);
                }
              }
              System.out.println("");
              System.out.println("String is [" + event + "]" );
              System.out.println("Length of string is " + event.length() );
    */
System.out.println("EventString = " + event);
    String tmpStr;

    // First thing is to get the version number.  It must always be in the
    // same plave, no matter what the version.
    int versionNo = Integer.parseInt( event.substring(3,9) );
    //System.out.println("Version no from string = " + versionNo );

    // Get Events from the string.

    // The senderId, senderGM, recepientId and recepientGM may be null.
    Integer lSenderId, lSenderGM, lRecepientId, lRecepientGM;


    lSenderId = getIntegerValueFromString(getFieldFromString(event,SENDER_NODE_ID_FIELD,versionNo));
    lSenderGM = getIntegerValueFromString(getFieldFromString(event,SENDER_GM_FIELD,versionNo));
    lRecepientId = getIntegerValueFromString(getFieldFromString(event,RECEPIENT_NODE_ID_FIELD,versionNo));
    lRecepientGM = getIntegerValueFromString(getFieldFromString(event,RECEPIENT_GM_FIELD,versionNo));

    GridEvent newEvent = new GridEvent(lSenderId, lRecepientId);
    newEvent.setSenderGM( lSenderGM );
    newEvent.setRecipientGM( lRecepientGM );
    newEvent.setVersionNumber( getFieldFromString(event,VERSION_FIELD,versionNo) );
    newEvent.setRoute( Integer.valueOf( getFieldFromString(event,ROUTE_FIELD,versionNo), 16 ).intValue() );

    newEvent.setProcessID( Integer.valueOf( getFieldFromString(event,PROC_ID_FIELD,versionNo),16 ).intValue() );
    newEvent.setLanguague( getFieldFromString(event,LANGUAGE_FIELD,versionNo) );
    newEvent.setFunctionID( Integer.valueOf( getFieldFromString(event,FUNCTION_ID_FIELD_FIELD,versionNo), 16 ).intValue() );
    newEvent.setFileDataCount( Integer.valueOf( getFieldFromString(event,FILE_DATA_COUNT_FIELD,versionNo), 16 ).intValue() );
    newEvent.setEventType( Integer.valueOf( getFieldFromString(event,EVENT_TYPE_FIELD,versionNo),16 ).intValue() );
    newEvent.setEventID( Integer.valueOf( getFieldFromString(event,EVENT_ID_FIELD,versionNo), 16 ).intValue() );
    newEvent.setEventSubID( Integer.valueOf( getFieldFromString(event,EVENT_SUB_ID_FIELD,versionNo), 16 ).intValue() );

    // The date may be null
    tmpStr = getFieldFromString(event,DATE_TIME_FIELD,versionNo);
    if ( isNullString(tmpStr)) {
      Date d = null;
      newEvent.setDate(d);
    }
    else
    {
      long dateLong = Long.parseLong( tmpStr, 16 );
      Date d = new Date(dateLong);
      newEvent.setDate( d );
    }

    //System.out.println("Event Data Count field is [" + getFieldFromString(event,EVENT_DATA_COUNT_FIELD) + "]" );

    newEvent.setEventDataCount( Integer.valueOf( getFieldFromString(event,EVENT_DATA_COUNT_FIELD,versionNo), 16 ).intValue() );

    // Set the total number of bytes in the string.  This is based on the
    // version number, which was the first thing read in.
    newEvent.setTotalNumBytes();

    // Extract the gnci
    int gnciLength;
    String localGnci;
    int currentPos = newEvent.TOTAL_NUM_BYTES;
    gnciLength = Integer.parseInt( event.substring( currentPos, currentPos+4 ) );
    //System.out.println("GNCI length is " + gnciLength );
    currentPos = currentPos + 4;
    tmpStr = event.substring(currentPos, currentPos+gnciLength);
    if (tmpStr.compareTo(GridEvent.NULL_STRING)==0)
    {
      localGnci = null;
    }
    else
    {
      localGnci = event.substring(currentPos, currentPos+gnciLength);
    }
    newEvent.setGNCI( localGnci );
    currentPos = currentPos + gnciLength;


    // Extract each data element
    String currentData[] = new String[newEvent.getEventDataCount()];
    //int currentPos = newEvent.TOTAL_NUM_BYTES + gnciLength;
    int strLen;
    for(int i=0; i<newEvent.getEventDataCount(); i++) {
      strLen = Integer.parseInt( event.substring( currentPos, currentPos+4 ) );
      //System.out.println("strLen = " + strLen + ",i = " + i + ", currentPos = " + currentPos);
      currentPos = currentPos + 4;
      tmpStr = event.substring(currentPos, currentPos+strLen);
      //System.out.println("tmpStr = " + tmpStr);
      if (tmpStr.compareTo(GridEvent.NULL_STRING)==0) {
        currentData[i] = null;
      }
      else {
        currentData[i] = event.substring(currentPos, currentPos+strLen);
      }
      newEvent.setEventData( currentData );
      currentPos = currentPos + strLen;
      //currentDataIndex++;
    }
    return(newEvent);
  }

  /*
  static public GridEvent getDummyEvent(Integer senderNodeID, Integer recipientNodeID)
  {
    GridEvent dummy = new GridEvent(senderNodeID, recipientNodeID);
    return dummy;
  }
  */

  /************************************************
   ** All methods below are for testing only     **
   ************************************************/

  /**
   * Makes user enter yes or no from the keyboard and returns result.
   */
  private static boolean getYesNo(String prompt)
  {
    BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
    String i;
    boolean ret = false;
    boolean dataValid = false;

    while( dataValid == false ) {
      System.out.println( prompt + " [y/n] : " );
      try {
        i = d.readLine();
        if ( i.length()>0 ) {
          if ((i.charAt(0)=='Y') || (i.charAt(0)=='y')) {
            dataValid = true;
              ret = true;
          }
          else if ((i.charAt(0)=='n') || (i.charAt(0)=='N')) {
            dataValid = true;
            ret = false;
          }
          else {
            dataValid = false;
          }

        }
        else {
          dataValid = false;
        }

      } catch ( java.io.IOException e  ) {
        dataValid = false;
      }
    }
    return(ret);
  }

  private static int testInputInteger(String name, int defaultValue)
  {
    BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
    String i;
    int ret = -1;
    boolean dataValid = false;

    while( dataValid == false ) {
      System.out.println("Please enter " + name + ", enter for default ("+defaultValue+")");
      try {
        dataValid = true;
        i = d.readLine();
        if ( i.length()>0 ) {
          try {
            ret = Integer.parseInt( i );
            System.out.println( name + " = " + ret );
          }
          catch ( NumberFormatException nfe )
          {
            dataValid = false;
          }
        }
        else
        {
          ret = defaultValue;
          System.out.println( name + " defaulted to " + defaultValue );
        }
      } catch ( java.io.IOException e  ) {
        dataValid = false;
      }
    }
    return(ret);
  }

  /**
   *
   */
  private static long testInputLong(String name, long defaultValue)
  {
    BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
    String i;
    long ret = -1;
    boolean dataValid = false;

    while( dataValid == false ) {
      System.out.println("Please enter " + name + ", enter for default ("+defaultValue+")");
      try {
        dataValid = true;
        i = d.readLine();
        if ( i.length()>0 ) {
          try {
            ret = Long.parseLong( i );
            System.out.println( name + " = " + ret );
          }
          catch ( NumberFormatException nfe )
          {
            dataValid = false;
          }
        }
        else
        {
          ret = defaultValue;
          System.out.println( name + " defaulted to " + defaultValue );
        }
      } catch ( java.io.IOException e  ) {
        dataValid = false;
      }
    }
    return(ret);
  }


  /**
   * Prompts user to enter a valid integer, and returns it (as a String).
   */
  private static String testInputStrInt(String name, String defaultValue)
  {
    BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
    String i = "";
    boolean dataValid = false;

    while( dataValid == false ) {
      System.out.println("Please enter " + name + ", enter for default");
      try {
        dataValid = true;
        i = d.readLine();
        if ( i.length()>0 ) {
          try {
            //int tmp = 
        	Integer.parseInt( i );
          }
          catch ( NumberFormatException nfe )
          {
            dataValid = false;
          }
        }
        else
        {
          i = defaultValue;
          System.out.println( name + " defaulted to " + defaultValue );
        }
      } catch ( java.io.IOException e  ) {
        dataValid = false;
      }
    }
    return(i);
  }

  /**
   * Prompts user to enter a String returns it.  May return null.
   */
  private static String testInputString(String name)
  {
    BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
    String i = "";
    boolean dataValid = false;

    while( dataValid == false ) {
      System.out.println("Please enter " + name + " Enter 'NULL' for NULL String.)" );
      try {
        i = d.readLine();
        dataValid = true;
        if (i.compareTo("NULL")==0) {
          i = null;
        }
      } catch ( java.io.IOException e  ) {
        dataValid = false;
      }
    }
    return(i);
  }

  /**
   * For use by test driver.
   */
  private static GridEvent testCreateGE()
  {
    String versionNo, language;
    int eventId, eventType, fDataCount, functionId, route, eventSubId;
    Integer senderNodeId, recepNodeId, senderGM, recepientGM;
    Date date;
    int processId, eventDataCount;
    String data[] = null;
    int tmp;
    String gnci;

    GridEvent ret;
    versionNo = testInputStrInt("version number", "00000");

    tmp = testInputInteger( "sender node id", -1 );
    if (tmp == -1)
      senderNodeId = null;
    else
      senderNodeId = new Integer( tmp );

    tmp = testInputInteger( "recepient node id", -1 );
    if ( tmp == -1 )
      recepNodeId = null;
    else
      recepNodeId = new Integer( tmp );

    tmp = testInputInteger( "sender GM", -1 );
    if (tmp == -1 )
      senderGM = null;
    else
      senderGM = new Integer( tmp );

    tmp = testInputInteger( "recepient GM", -1 );
    if (tmp==-1)
      recepientGM=null;
    else
      recepientGM = new Integer( tmp );

    long tmpLong = testInputLong( "date-UTC  (Enter for null)", -1 );
    if (tmpLong == -1)
      date = null;
    else
      date = new Date( (long)tmpLong );

    eventId = testInputInteger( "eventID", 0 );
    eventType = testInputInteger( "eventType", 0 );
    fDataCount = testInputInteger( "File Data Count", 0 );
    functionId = testInputInteger( "Function ID", 0 );
    language = testInputString( "Language" );
    if ( (language.length()<1) || (language.length()>2) ) {
      System.out.println("Language is invalid, using 'EN'.");
      language = "EN";
    }
    route = testInputInteger( "route", 0 );

    gnci = testInputString( "GridNode Comm Info" );
    System.out.println("gnci is [" + gnci + "]" );

    eventDataCount = testInputInteger("eventDataCount", 0);
    eventSubId = testInputInteger( "Event SubId ID", 0 );

    if (eventDataCount>0) {
    boolean enterData = getYesNo( "Do you want to enter the data for " +  eventDataCount + " data elts " );
    data = new String[eventDataCount];
    if (enterData==false) {
      // Generate default data for the number of data elts required
      if (eventDataCount > 0) {
        int counter = 0;
        for(int i=0; i<eventDataCount; i++) {
          data[i] = Integer.toString(counter);
          counter++;
          if (counter==10) counter=0;
        }
      }
    }
    else {
      for(int i=0; i<eventDataCount; i++) {
        data[i] = testInputString( "data elt " + i );
      }
    }
    }

    processId = testInputInteger( "processId", 0 );

    ret = new GridEvent( senderNodeId, recepNodeId, eventId,
                   data, language, functionId,
                   processId, eventType, eventSubId );
    System.out.println("Setting version number to " + versionNo );
    ret.setVersionNumber(versionNo);
    ret.setSenderGM(senderGM);
    ret.setRecipientGM(recepientGM);
    ret.setDate(date);
    ret.setFileDataCount(fDataCount);
    ret.setRoute(route);
    System.out.println("gnci SET is [" + gnci + "]" );
    ret.setGNCI(gnci);

    // Have to set the total num bytes.  Even though this was called in the
    // GridEvent constructor above, it will be wrong because the version number
    // was not set then.
    System.out.println("***************");
    ret.setTotalNumBytes();

    // No need to set eventDataCount because the data array passed
    // in will already have set it.

    return( ret );
  }


  /**
  * Prints out the events and data in this GE to stdout.
  */
  public void testPrintGE() {
    if (this.senderGM==null)
      System.out.println("senderGM = null");
    else
      System.out.println("SenderGM = " + senderGM );

    if (this.senderNodeID==null)
      System.out.println("senderID = null");
    else
      System.out.println("SenderID = " + this.senderNodeID );

    if (this.recipientGM==null)
      System.out.println("recipientGM = null");
    else
      System.out.println("recipientGM = " + this.recipientGM );

    if (this.recipientNodeID==null)
      System.out.println("recipientNodeID = null");
    else
      System.out.println("recipientNodeID = " + this.recipientNodeID );

    if (this.date==null)
      System.out.println("Date = null");
    else
      System.out.println("Date (UTC) = " + this.date.getTime() );

    if (this.gnci==null)
      System.out.println("GNCI = [NULL]" );
    else
      System.out.println("GNCI = [" + this.gnci + "]" );

    System.out.println("Process id = " + this.processID );

    System.out.println("Version Number is " + this.VERSION_NUMBER + "  " + this.VERSION_NUMBER_VALUE );

    System.out.println("Event Data Count = " + this.eventDataCount );
    System.out.print("Data Elements :");
    for(int i=0; i<this.eventDataCount;i++) {
      System.out.print(" " +  i + "["+this.getEventData(i)+"] " );
    }
    System.out.println();

    System.out.println("EventId = " + this.eventID );
    System.out.println("eventType = " + this.eventType );
    System.out.println("FileDataCount = " + this.fileDataCount );
    System.out.println("Function Id = " + this.functionID );
    System.out.println("Route = " + this.route );
    System.out.println("language = [" + this.language + "]" );

    System.out.println("EventSubID = " + this.eventSubID );

  }

  /**
   * Test Driver
   */
  public static void main(String[] args)
  {
    GridEvent g = null;
    String resultingStr = null;
    BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
    int choice = -1;
    try {
      while( choice!=0 ) {
        System.out.println("GridEvent Test Driver choices");
        System.out.println("  0 - exit");
        System.out.println("  1 - Construct GE");
        System.out.println("  2 - Convert GE to String");
        System.out.println("  3 - string to GE");
        try {
          choice = Integer.parseInt( d.readLine() );
          switch( choice ) {
            case 1 :
              g = testCreateGE();
              break;
            case 2 :
              resultingStr = g.getEventString();
              System.out.print("Str index [" );
              for( int x=0; x<10;x++) {
                for(int y=0; y<10; y++) {
                  System.out.print(y);
                }
              }
              System.out.println("");
              System.out.println("String is [" + resultingStr + "]" );
              System.out.println("Length of string is " + resultingStr.length() );
              break;
            case 3 :
              g = getEvent( resultingStr );
              g.testPrintGE();
              break;
          }

        }
        catch( NumberFormatException nfe )
        {
          choice = -1;
        }

      }  // end while
    } catch( java.io.IOException e  )
    {
    }


  }

}
