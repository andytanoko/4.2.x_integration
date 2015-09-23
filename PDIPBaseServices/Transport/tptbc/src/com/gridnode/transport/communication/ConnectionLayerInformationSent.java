package com.gridnode.transport.communication;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

//import com.gridnode.transport.tplog.CommLog;

/**
 * ASSUMPTION :
 *   1) version n of this class MUST extend version n-1.
 *      eg: ver 3 extends ver2 extends ver 1, which extends this class.
 *   2) Every version from 1 onwards MUST have a protected constructor,
 *      which accepts whatever parameters the previous version (superclass)
 *      accepts, as well as an additional int (representing version number).
 *      This constructor is needed to be called by the next version (subclass),
 *      when it is created in the future.
 */
public abstract class ConnectionLayerInformationSent implements Serializable
{
    // List of all the versions up to date.  Currently
    // only up to v1.
    public static final int VERSION_1 = 1;

    int version;

    public ConnectionLayerInformationSent(int version)
    {
	this.version = version;
    }

    public void setVersion(int v)
    {
	this.version = v;
    }

    public int getVersion()
    {
	return this.version;
    }

    abstract public byte[] toByteArray()  throws java.lang.Exception;

    static public ConnectionLayerInformationSent fromByteArray(byte[] b) throws java.lang.Exception
    {
      // Get the version, then call the appropriate subclass.
      ByteArrayInputStream bais = new ByteArrayInputStream(b);
      DataInputStream dais = new DataInputStream(bais);
      int version = dais.readInt();
//      CommLog.debug("CONN_baseClass.toByteArray().  Version is " + version );

      ConnectionLayerInformationSent ret = null;
      switch( version )
      {
        // We only have version 1 right now.  And everything defaults to
        // this version (as the oldest one) anyay.
        default:
          ret = ConnectionLayerInformationSent_v1.fromByteArray(b);
      }
      dais.close();
      bais.close();
      return(ret);
    }

}
