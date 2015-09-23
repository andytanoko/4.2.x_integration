package com.gridnode.pdip.base.rnif.model;

//import org.jdom.Namespace;

public interface IRNConstant
{
  public final static String PART_CONTENT_TYPE = "application/xml";

  public final static String ROSETTANET_TIMEZONE = "GMT";

  public final static String ADMIN_AUTH_CODE = "RosettaNet"; // for preamble

  // names of temp files to be used for validation
  public final static String PREAMBLE_FILE = "preamble.xml";
  public final static String DHEADER_FILE = "delivery_header.xml";
  public final static String SHEADER_FILE = "service_header.xml";

}
