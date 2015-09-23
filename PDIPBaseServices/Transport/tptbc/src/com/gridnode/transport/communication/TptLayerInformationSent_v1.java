package com.gridnode.transport.communication;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001(C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionMgr.java
 *
 *********************************************************************************************************************
 * Date             Author                  Changes
 *********************************************************************************************************************
 * Nov 13 2001      Roger Ng                Version control started.
 * Nov 13 2001      Roger Ng                Bug fix where msg being sent >65535 bytes.
 * Mar 12 2001      Roger Ng                Add logging statement in toByteArray().
 */

//import com.gridnode.transport.GridNodeCommInfo;
import java.io.*;

/**
 * See Assumption 2 in the base class.  If not followed, we may LOSE
 * BACKWARD COMPATIBILITY.
 */

public class TptLayerInformationSent_v1 extends TptLayerInformationSent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3540138584400699018L;
		byte[] fileContents;
    String gridEventStr;
    String fileName;

    transient BufferedInputStream bf = null;
    transient BufferedOutputStream bfo = null;

    /**
     * @param file The file to be sent.  It should be not longer than
     *             TptLayerInformationSent.MAX_FILE_LEN bytes.  May be null if
     *             none to send.
     * @param gridEvent gridEvent
     * @param version version (eg: TptLayerInformationSent.VERSION_1).
     *
     * @exception FileNotFoundException The file passed in was not found
     * @exception IllegalArgumentException May be thrown if the file passed in
     *            is too long
     * @exception IOException Error reading the file
     */
    public TptLayerInformationSent_v1(File file, GridEvent g) throws FileNotFoundException, IllegalArgumentException, IOException
    {
      this( file, g, TptLayerInformationSent.VERSION_1 );
    }

    /**
     * @param file The file to be sent.  It should be not longer than
     *             TptLayerInformationSent.MAX_FILE_LEN bytes.  May be null if
     *             none to send.
     * @param gridEvent gridEvent
     * @param version version (eg: TptLayerInformationSent.VERSION_1).
     *
     * @exception FileNotFoundException The file passed in was not found
     * @exception IllegalArgumentException May be thrown if the file passed in
     *            is too long
     * @exception IOException Error reading the file
     */
    protected TptLayerInformationSent_v1(File file, GridEvent g, int version) throws FileNotFoundException, IllegalArgumentException, IOException
    {
	super(version);

        if (file!=null)
        {
          try{
            this.bf= new BufferedInputStream(new FileInputStream(file));
          }
          catch(FileNotFoundException ex)
          {
//            TpLog.err( "<<<<< ----- File not found ("+file.getPath() + file.getName()+")----->>>>", ex);
            throw ex;
          }
          long fileLength = file.length();
          if (fileLength > (long)TptLayerInformationSent.MAX_FILE_LEN )
          {
//            TpLog.err("Error: File " + file.getPath() + file.getName() + " for sending is too long ("+fileLength+" bytes)");
            throw new IllegalArgumentException("Error: File " + file.getPath() + file.getName() + " for sending is too long ("+fileLength+" bytes)");
          }

          fileContents = new byte[ (int)fileLength];
          fileName = file.getName();

//          TpLog.debug("TptLayerInfoSent_v1: Sending file, name is ["+fileName+"]");
          int numBytesRead = 0;
          int i = 0;
          int numBytesRemaining = (int)fileLength;
          while( (numBytesRead<(int)fileLength) && (i!=(-1)) )
          {
            i = bf.read(fileContents, numBytesRead, (int)numBytesRemaining);
            if ( i!=(-1) )
            {
              numBytesRead =numBytesRead + i;
              numBytesRemaining = numBytesRemaining - i;
            }
          }
          if ( numBytesRemaining!=0)
          {
            throw new IOException("Error reading from file " +
              file.getAbsolutePath() + " Only " + numBytesRead + " of " +
              file.length() + " bytes read." );
          }

//          TpLog.debug("TptLayerInfoSent_v1: Sending file, num bytes sent = " + numBytesRead );
          bf.close();
          bf = null;
        }

	this.gridEventStr = g.getEventString();

    }

    /**
     * @param file Data to be sent.  It should be not longer than
     *             TptLayerInformationSent.MAX_FILE_LEN bytes.  May be null if
     *             none to send.
     * @param fileName Filename of the file that the data above is from.  Null if
     *             data is null.  If data is not null, this must be not null too,
     *             else NullPointerException is thrown.
     *
     * @exception FileNotFoundException The file passed in was not found
     * @exception IllegalArgumentException May be thrown if the file passed in
     *            is too long
     * @exception IOException Error reading the file
     */
    public TptLayerInformationSent_v1(byte[] data, String fileName, GridEvent g) throws FileNotFoundException, IllegalArgumentException, IOException
    {
      super(TptLayerInformationSent.VERSION_1);

      if (data!=null)
      {
        this.fileContents = data;
        this.fileName = fileName;
        if ( (fileContents!=null) && (fileName==null) )
          throw new NullPointerException("TptLayerInformationSent_v1 constr() : data provided, but filename is null");
      }
      this.gridEventStr = g.getEventString();
    }


    public GridEvent getGridEvent()
    {
	return GridEvent.getEvent(this.gridEventStr);
    }

    public byte[] getFileContents()
    {
	return this.fileContents;
    }

    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * Saves the file held by this object into the directory passed in.
     * A filehandle to this saved file is returned.  The saved
     * file has the same name as it had when it was passed in to this object's
     * constructor. (eg: If the file was named aaa.txt when thsi object
     * constructed, if C:\tmp is passed in as the directory param, then the
     * file is saved to C:\tmp\aaa.txt, and its filehandle is returned. )
     *
     * If no file is held by this object, nothing happens.
     *
     * @param directory The directory that the file will be saved in.  Should be
     *                  an existing, writeable directory.
     */
    public File getFile(File directory) throws FileNotFoundException, IOException
    {
      File ret = null;

      if (fileContents!=null)
      {
//        TpLog.debug("File Contents byte array received is " + fileContents.length + " bytes long");
//        TpLog.debug("File received is named [" + fileName + "] in directory [" + directory.getAbsolutePath());
        ret = new File(directory, fileName);
        bfo = new BufferedOutputStream(new FileOutputStream(ret));
        bfo.write(this.fileContents);
        bfo.close();
//        TpLog.debug( ret.length() + " bytes written successfully to file " + ret.getAbsolutePath());
        bfo = null;
      }
      return(ret);
    }

    public byte[] toByteArray() throws java.lang.Exception
    {
      //int totalLength;

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);

      // Version first
      dos.writeInt( this.version );
//      TpLog.debug("TPT.toByteArray().  Version is " + this.version );

      // Write the length of the file contents as an int, followed by the file
      // contents themselves.
      if ( this.fileContents==null )
      {
        dos.writeInt( -1 );
        //TpLog.debug("TPT.toByteArray().  fclen is zero (NULL) ");
      }
      else
      {
        dos.writeInt( this.fileContents.length );
        dos.write( this.fileContents );

        //TpLog.debug("TPT.toByteArray().  fclen is " + this.fileContents.length );
      }

      // Length if Grid Event String, followed by the string itself
      int BLOCK_SIZE = 20000;
      int numTimes = this.gridEventStr.length() / BLOCK_SIZE;

      dos.writeInt(numTimes);
      int start = 0;
      int end = BLOCK_SIZE;
      for (int j = 0; j < numTimes; j++, end+=BLOCK_SIZE, start +=BLOCK_SIZE)
        dos.writeUTF(this.gridEventStr.substring(start, end));
//      TpLog.debug("gridEventStr.length() = " + this.gridEventStr.length() + "  numTimes=" + numTimes + " start=" + start );
      dos.writeUTF(this.gridEventStr.substring(start, this.gridEventStr.length()));  // extra time for remainder



      //TpLog.debug("TPT.toByteArray().  gelen is " + tmp.length );

      // Length of filename, followed by the filename itself
      //tmp = ObjectToString.stringToByteArray( this.fileName );
      if (this.fileName==null)
      {
        dos.writeBoolean( false );
      }
      else
      {
        dos.writeBoolean( true );
        dos.writeUTF( this.fileName );
      }
      //TpLog.debug("TPT.toByteArray().  fnlen is " + tmp.length );
      dos.flush();
      //TpLog.debug("TPT.toByteArray().  flush OK" );
      dos.close();
      //TpLog.debug("TPT.toByteArray().  close OK" );

      byte ret[] = baos.toByteArray();
      //TpLog.debug("TPT.toByteArray().  toByteArray OK" );

      baos.close();
//      TpLog.debug("TPT.toByteArray() finished.  Returning array of " +ret.length+ " bytes." );
      return( ret );
    }

    static public TptLayerInformationSent fromByteArray(byte[] theByteArray) throws java.lang.Exception
    {
      ByteArrayInputStream bais = new ByteArrayInputStream(theByteArray);
      DataInputStream dis = new DataInputStream(bais);

      // Version first
      int version = dis.readInt();
//      TpLog.debug("TPT.fromByteArray().  Version is " + version );

      // Length of file contents byte array, followed by the byte array itself
      int fileLength = dis.readInt();
      //TpLog.debug("TPT.fromByteArray().  fclen is " + fileLength );
      byte fba[];
      if (fileLength == -1 )  // byte array was null.
      {
        fba = null;
      }
      else
      {
        if (fileLength != 0 )
        {
          fba = new byte[fileLength];
          dis.readFully(fba);//, 0, fileLength);
        }
        else  // zero len (empty) file
        {
          fba = new byte[0];
        }
      }

      StringBuffer str = new StringBuffer();
      int numTimes = dis.readInt();
      for (int j = 0; j < numTimes; j++)
        str.append(dis.readUTF());
      str.append(dis.readUTF());
      String ges = str.toString();

      //TpLog.debug("TPT.fromByteArray().  num bytes read is " + numBytes );

      boolean hasFile = dis.readBoolean();
      String fns = null;
      if (hasFile)
        fns = dis.readUTF();


      TptLayerInformationSent_v1 t = new TptLayerInformationSent_v1(
        fba, fns, GridEvent.getEvent(ges) );
      //TpLog.debug("TPT.fromByteArray().  Tpt_v1 constructed successfully" );

      dis.close();
      bais.close();
      return(t);
    }


    public static void main(String[] args)
    {
      try
      {

      System.out.println("First test");
      GridEvent g = new GridEvent(new Integer(101), new Integer(102), 103,
        null, "EN", 666, 333, 999 );
      TptLayerInformationSent_v1 v1 = new TptLayerInformationSent_v1(null, g );

      byte[] b = v1.toByteArray();

      System.out.println("byteArray is " + b.length + " long");

      TptLayerInformationSent_v1 v = (TptLayerInformationSent_v1)TptLayerInformationSent.fromByteArray(b);

      System.out.println("Conversion done");

      String originalGridEventString = g.getEventString();
      String newGridEventString = v.getGridEvent().getEventString();
      System.out.println("Comparison of GridEvents is " + originalGridEventString.compareTo(newGridEventString) );

      System.out.println("Second test");
      v1 = new TptLayerInformationSent_v1( new File("C:\\temp\\cert\\signedCertFile.txt"), g );
      b = v1.toByteArray();

      System.out.println("byteArray is " + b.length + " long");
      v = (TptLayerInformationSent_v1)TptLayerInformationSent.fromByteArray(b);
      System.out.println("Conversion done");
      originalGridEventString = g.getEventString();
      newGridEventString = v.getGridEvent().getEventString();
      System.out.println("Comparison of GridEvents is " + originalGridEventString.compareTo(newGridEventString) );

      byte originalFileContents[] = v1.getFileContents();
      byte newFileContents[] = v.getFileContents();
      boolean equal = true;
      System.out.println("Comparison of FileContent lengths originalFileContents:" + originalFileContents.length + "  newFileContents:" + newFileContents.length );
      for(int i=0;i<originalFileContents.length;i++)
      {
        if (originalFileContents[i] != newFileContents[i])
          equal = false;
      }
      System.out.println("Comparison of FileContent being the same " + equal );

      String origFileName = v1.getFileName();
      String newFileName = v.getFileName();
      System.out.println("Comparison of file names :" + origFileName.compareTo(newFileName));

      }
      catch( Exception e)
      {
        e.printStackTrace();
      }

    }

}
