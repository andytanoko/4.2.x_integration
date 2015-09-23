package com.gridnode.pdip.framework.file.access;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.Vector;

public class MultiFileBlock implements java.io.Serializable {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6911805631413413777L;
	private Vector _descriptor;
  private byte[] _data;
  //private int _size;

  public MultiFileBlock() {
    _descriptor=new Vector();
    _data=null;
    //_size=0;
  }

  public int getParts() {
    return _descriptor.size();
  }

  public BlockFileDescriptor getBlockFileDescriptor(int index) {
    return (BlockFileDescriptor)_descriptor.get(index);
  }

  public byte[] getBlockData() {
    return _data;
  }

/*  public byte[] getAllBlockFileData() {
    byte[] block = new byte[_size];
    int offset=0;
    for (int i=0; i<_data.size(); i++) {
      byte[] buf = (byte[])_data.get(i);
      System.arraycopy(buf, 0, block, offset, buf.length);
      offset += buf.length;
    }
    return block;
  }

  public boolean addBlockFileData(byte[] data) {
    return _data.add(data);
  }

  public void addBlockFileData(int index, byte[] data) {
    _data.add(index, data);
  }
*/

  public void setBlockData(byte[] data) {
    _data = data;
  }

  public boolean addBlockFileDescriptor(BlockFileDescriptor des) {
    return _descriptor.add(des);
  }

  public void addBlockFileDescriptor(int index, BlockFileDescriptor des) {
    _descriptor.add(index, des);
  }
}