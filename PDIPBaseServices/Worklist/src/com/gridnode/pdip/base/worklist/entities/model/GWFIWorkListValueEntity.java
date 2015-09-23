package com.gridnode.pdip.base.worklist.entities.model;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */



 
public interface GWFIWorkListValueEntity {

  public static final String ENTITY_NAME = "GWFWorkListValueEntity";

  public static final Number UID = new Integer(0);  //Integer

  public static final Number ICAL_WI_DESCRIPTION = new Integer(1);  //Description

  public static final Number ICAL_COMMENTS  = new Integer(2);  //Comments

  public static final Number ICAL_REQST_STATUS = new Integer(3);  //Request Status

  public static final Number USER_ID  = new Integer(4);  //User ID

  public static final Number ICAL_CREATION_DT = new Integer(5);  //Creation DT

  public static final Number UNASSIGNED = new Integer(6); //Unassigned Flag

  public static final Number PROCESSDEF_KEY = new Integer(7); //Process Defination Key.

  public static final Number ACTIVITY_ID = new Integer(8); //Activity Id.

  public static final Number PERFORMER = new Integer(9); //Performer.

  public static final Number RTACTIVITY_UID = new Integer(10); //Runtime Activity UID.

  public static final Number CONTEXT_UID = new Integer(11); //Context UID.

}
