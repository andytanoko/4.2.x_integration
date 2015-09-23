package com.gridnode.pdip.app.channel.flowcontrol;

public interface IFilePartEvent
{
  public static final int _SEND_FILE_PART = 7004;

  public static final int _SEND_FILE_PARTS_FINISHED = 7006;

  public static final int _ACK_FILE_PARTS = 7010;

  public static final int _EXTEND_TIMEOUT = 7012;

  public static final int _CANCEL_SEND_GRIDDOC = 7016;

}