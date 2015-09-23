package com.gridnode.transport.communication;

/**
 * See Assumption 2 in the base class.  If not followed, we may LOSE
 * BACKWARD COMPATIBILITY.
 */

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001(C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionLayerInformationSent_v1.java
 *
 *********************************************************************************************************************
 * Date             Author                  Changes
 *********************************************************************************************************************
 * Mar 11 2001      Roger Ng                Banner created.
 * Mar 12 2001      Roger Ng                Add logging statement in toByteArray().
 */

//import com.gridnode.transport.GridNodeCommInfo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

//import com.gridnode.transport.ObjectToString;
//import com.gridnode.transport.tplog.CommLog;

public class ConnectionLayerInformationSent_v1 extends ConnectionLayerInformationSent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2564402606413033821L;
		TptLayerInformationSent data;
    //String sysEvent;
    public ConnectionLayerInformationSent_v1(TptLayerInformationSent data /*, String sysEvent*/)
    {
	this(data, /*sysEvent,*/  ConnectionLayerInformationSent.VERSION_1);
    }

    protected ConnectionLayerInformationSent_v1(TptLayerInformationSent data, /*String sysEvent,*/ int version)
    {
	super(version);
	this.data = data;
        //this.sysEvent = sysEvent;
    }
/*
    public String getSysEvent()
    {
      return this.sysEvent;
    }

    public void removeSysEvent()
    {
      this.sysEvent = null;
    }
*/
    public TptLayerInformationSent getData()
    {
      return( this.data );
    }

    public byte[] toByteArray() throws java.lang.Exception
    {
      //int totalLength;

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);

      // Version first
      dos.writeInt( this.version );
//      CommLog.debug("CONN.toByteArray().  Version is " + this.version );

      // Write the length of data (TptLayerInfoSent) to byte array, followed
      // by the data itself.
      byte[] dba = data.toByteArray();
      dos.writeInt( dba.length );
      dos.write( dba );
      //CommLog.debug("CONN.toByteArray().  data (TptLayerInfoSent) len is " + dba.length );

      // Length of sysevent String, followed by the string itself
      /*
      byte[] tmp = ObjectToString.stringToByteArray( this.sysEvent );
      dos.writeInt( tmp.length );
      dos.write( tmp );
      CommLog.debug("CONN.toByteArray().  syseventLen is " + tmp.length );
      dos.flush();
      */

      byte ret[] = baos.toByteArray();
      dos.close();
      baos.close();
//      CommLog.debug("CONN.toByteArray() finished.  Returning array of " +ret.length+ " bytes." );
      return(ret);
    }

    static public ConnectionLayerInformationSent fromByteArray(byte[] theByteArray) throws java.lang.Exception
    {
      ByteArrayInputStream bais = new ByteArrayInputStream(theByteArray);
      DataInputStream dis = new DataInputStream(bais);

      // Version first
      int version = dis.readInt();
//      CommLog.debug("CONN.fromByteArray().  Version is " + version );

      // Length of data (TptLayerInfoSent) to byte array, followed
      // by the data itself.
      int dataLen = dis.readInt();
      //CommLog.debug("CONN.fromByteArray().  dataLen (TptLayerInfoSent) is " + dataLen );
      byte ta[] = new byte[dataLen];
      int numBytes = dis.read(ta, 0, dataLen);
      //CommLog.debug("CONN.fromByteArray().  Num Bytes read in is " + numBytes );

      // Length if sysevent String, followed by the string itself
      /*
      int seLength = dis.readInt();
      CommLog.debug("CONN.fromByteArray().  syseventLen is " + seLength );
      byte seba[] = new byte[seLength];
      numBytes = dis.read(seba, 0, seLength);
      String ses = ObjectToString.byteArrayToStr( seba );
      CommLog.debug("CONN.fromByteArray().  Num Bytes read in is " + numBytes );
      */

      TptLayerInformationSent tpt = TptLayerInformationSent.fromByteArray(ta);
      ConnectionLayerInformationSent_v1 c = new ConnectionLayerInformationSent_v1(tpt);

      //CommLog.debug("CONN.fromByteArray().  Con_v1 constructed successfully" );
      return(c);
    }

}
