package com.gridnode.transport.communication;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

//import com.gridnode.transport.tplog.TpLog;

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
public abstract class TptLayerInformationSent implements Serializable
{
    // List of all the versions up to date.  Currently
    // only up to v1.
    public static final int VERSION_1 = 1;

    // The HTTP version (for sending/receiving to/from RosettaNett partners).
    public static final int HTTP_VERSION_1 = -1;


    // The maximum length of a file (# bytes) that should be sent.  This is not strictly
    // endofrce, but if a longer file is received, an error msg is logged.
    public static final int MAX_FILE_LEN = 512000;

    int version;


    // Does the data held conform to GridProtocol?  This is used whenever
    // we send from GT to GT.  False if sending/receiving to/from a non-GT
    // (eg: A RosettaNett partner)
    private boolean gridProtocol = false;

    public TptLayerInformationSent(int version)
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

    public boolean usesGridProtocol()
    {
      return this.gridProtocol;
    }

    protected void setGridProtocol(boolean usesGridProtocol)
    {
      this.gridProtocol = usesGridProtocol;
    }

    abstract public byte[] toByteArray()  throws java.lang.Exception;

    static public TptLayerInformationSent fromByteArray(byte[] b) throws java.lang.Exception
    {
      // Get the version, then call the appropriate subclass.
      ByteArrayInputStream bais = new ByteArrayInputStream(b);
      DataInputStream dis = new DataInputStream(bais);
      int version = dis.readInt();
//      TpLog.debug("TPT_baseClass.toByteArray().  Version is " + version );

      TptLayerInformationSent ret = null;
      switch( version )
      {
        // We only have version 1 right now.  And everything defaults to
        // this version (as the oldest one) anyay.
        default:
          ret = TptLayerInformationSent_v1.fromByteArray(b);
      }
      dis.close();
      bais.close();
      return( ret );
    }

}
