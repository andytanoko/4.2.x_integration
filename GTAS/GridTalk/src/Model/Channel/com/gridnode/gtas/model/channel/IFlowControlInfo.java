package com.gridnode.gtas.model.channel;

public interface IFlowControlInfo
{

  public static final String ENTITY_NAME = "FlowControlInfo";

  public static final int DEFAULT_SPLIT_THRESHOLD = 1024;

  public static final int DEFAULT_SPLIT_SIZE = 64;

  public static final Number IS_ZIP = new Integer(1);

  public static final Number ZIP_THRESHOLD = new Integer(2);

  public static final Number IS_SPLIT = new Integer(3);

  public static final Number SPLIT_THRESHOLD = new Integer(4);

  public static final Number SPLIT_SIZE = new Integer(5);

}