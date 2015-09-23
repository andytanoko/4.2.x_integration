package com.gridnode.pdip.framework.db.filter;

import java.io.File;

import org.exolab.castor.mapping.Mapping;

import com.gridnode.pdip.framework.util.SystemUtil;

public class DataFilterMapping
  extends Mapping
  implements java.io.Serializable
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2954315468951173336L;

	public DataFilterMapping()
  {
    super(DataFilterMapping.class.getClassLoader());

    initDataFilterMapping();
  }

  private void initDataFilterMapping()
  {
    try
    {
    	File mappingFile = new File(SystemUtil.getWorkingDirPath(), "conf/default/pdip/framework/filter.map");
    	loadMapping(mappingFile.toURL());
      //loadMapping("file:///"+System.getProperty("user.dir")+ "/conf/default/pdip/framework/filter.map");
    }
    catch (Exception ex)
    {
      System.out.println("[DataFilterMapping.loadMapping] Unable to load filter mapping");
      ex.printStackTrace(System.out);
    }
//    System.out.println("************* initializing mapping");
//    MappingRoot mappingRoot = new MappingRoot();
//
//    mappingRoot.addClassMapping(createDataFilterClassMapping());
//    mappingRoot.addClassMapping(createValueFilterClassMapping());
//    mappingRoot.addClassMapping(createDataFilterGroupClassMapping());
//    mappingRoot.addClassMapping(createDataTypeFilterClassMapping());
//
//    loadMapping(mappingRoot);
  }
/*
  public void loadMapping(MappingRoot root)
  {
    System.out.println("************* Load mapping");

    StringWriter writer = null;
    StringReader reader = null;
    try
    {
      writer = new StringWriter();
      Marshaller marshaller = new Marshaller(writer);
      marshaller.setLogWriter(new PrintWriter(System.out));
//      marshaller.setNSPrefixAtRoot(false);
      marshaller.marshal(root);
//      marshaller.setValidation(false);
      writer.flush();

      String rootStr = writer.getBuffer().toString();
      System.out.println("******** Root String:");
      System.out.println(rootStr);
      reader = new StringReader(rootStr);

      InputSource ipSource = new InputSource(reader);
      loadMapping(ipSource);
    }
    catch (Exception ex)
    {
      System.out.println("[DataFilterMapping.loadMapping] Unable to load filter mapping");
      ex.printStackTrace(System.out);
    }
    finally
    {
      try
      {
        if (writer != null) writer.close();
        if (reader != null) reader.close();
      }
      catch (Exception ex) {}
    }
  }

  private ClassMapping createDataFilterClassMapping()
  {
    ClassMapping map = new ClassMapping();
    MapTo mapTo;

    map.setName(DataFilter.class.getName());
    map.setDescription("Serializable Data Filter");

    //Root element
    mapTo = new MapTo();
    mapTo.setXml("Filter");
    map.setMapTo(mapTo);

    //elements

    // negate
    map.addFieldMapping(createFieldMapping(
      "negate", NodeType.ATTRIBUTE, "boolean", "hasNegation", "setNegate",
      false));

    //value filter
    map.addFieldMapping(createFieldMapping(
      "ValueFilter", NodeType.ELEMENT, ValueFilter.class.getName(), false));

    //left filter
    map.addFieldMapping(createFieldMapping(
      "LeftFilter", NodeType.ELEMENT, DataFilter.class.getName(), false));

    //right filter
    map.addFieldMapping(createFieldMapping(
      "RightFilter", NodeType.ELEMENT, DataFilter.class.getName(), false));

    //connector
    map.addFieldMapping(createFieldMapping(
      "Connector", NodeType.ELEMENT, "string", false));

    //next filter
    map.addFieldMapping(createFieldMapping(
      "NextFilter", NodeType.ELEMENT, DataFilter.class.getName(), false));

    //next connector
    map.addFieldMapping(createFieldMapping(
      "NextConnector", NodeType.ELEMENT, "string", false));

    return map;
  }

  private ClassMapping createValueFilterClassMapping()
  {
    ClassMapping map = new ClassMapping();
    MapTo mapTo;

    map.setName(ValueFilter.class.getName());
    map.setDescription("Serializable Value Filter");

    //Root element
    mapTo = new MapTo();
    mapTo.setXml("ValueFilter");
    map.setMapTo(mapTo);

    //elements

    // filter field
    map.addFieldMapping(createFieldMapping(
      "FilterField", NodeType.ELEMENT, null, true));

    //operator
    map.addFieldMapping(createFieldMapping(
      "Operator", NodeType.ELEMENT, "string", false));

    //single value
    map.addFieldMapping(createFieldMapping(
      "SingleValue", NodeType.ELEMENT, null, false));

    //low value
    map.addFieldMapping(createFieldMapping(
      "LowValue", NodeType.ELEMENT, null, false));

    //high value
    map.addFieldMapping(createFieldMapping(
      "HighValue", NodeType.ELEMENT, null, false));

    //domain values
    map.addFieldMapping(createFieldMapping(
      "DomainValues", "DomainValue", NodeType.ELEMENT, null,
      CollectionType.VECTOR, false));

    return map;
  }

  private ClassMapping createDataFilterGroupClassMapping()
  {
    ClassMapping map = new ClassMapping();
    MapTo mapTo;

    map.setName(DataFilterGroup.class.getName());
    map.setDescription("Serializable Data Filter Group");

    //Root element
    mapTo = new MapTo();
    mapTo.setXml("DataFilterGroup");
    map.setMapTo(mapTo);

    //elements

    //filters
    map.addFieldMapping(createFieldMapping(
      "Filters", "DataFilter", NodeType.ELEMENT, DataTypeFilter.class.getName(),
      CollectionType.VECTOR, false));

    return map;
  }

  private ClassMapping createDataTypeFilterClassMapping()
  {
    ClassMapping map = new ClassMapping();
    MapTo mapTo;

    map.setName(DataTypeFilter.class.getName());
    map.setDescription("Serializable Typed Data Filter");

    //Root element
    mapTo = new MapTo();
    mapTo.setXml("DataFilter");
    map.setMapTo(mapTo);

    //elements

    // type
    map.addFieldMapping(createFieldMapping(
      "type", NodeType.ATTRIBUTE, "string", true));

    //connector
    map.addFieldMapping(createFieldMapping(
      "andOr", NodeType.ATTRIBUTE, "string", false));

    //data filter
    map.addFieldMapping(createFieldMapping(
      "Filter", NodeType.ELEMENT, DataFilter.class.getName(), true));

    return map;
  }

  private FieldMapping createFieldMapping(
    String name, NodeType nodeType, String type, String getMethod, String setMethod,
    boolean required)
  {
    BindXml bind = new BindXml();
    FieldMapping field = new FieldMapping();

    bind.setName(name);
    bind.setNode(nodeType);
    bind.setType(type);
    field.setBindXml(bind);
    field.setGetMethod(getMethod);
    field.setName(name);
    field.setRequired(required);
    if (type != null)
      bind.setType(type);
    field.setSetMethod(setMethod);

    return field;
  }

  private FieldMapping createFieldMapping(
    String name, NodeType nodeType, String type, boolean required)
  {
    BindXml bind = new BindXml();
    FieldMapping field = new FieldMapping();

    bind.setName(name);
    bind.setNode(nodeType);
    if (type != null)
      bind.setType(type);
    field.setBindXml(bind);
    field.setName(name);
    field.setRequired(required);
    field.setType(type);

    return field;
  }

  private FieldMapping createFieldMapping(
    String name, String xmlName, NodeType nodeType, String type,
    CollectionType collectionType, boolean required)
  {
    BindXml bind = new BindXml();
    FieldMapping field = new FieldMapping();

    bind.setName(xmlName);
    bind.setNode(nodeType);
    if (type != null)
      bind.setType(type);
    field.setBindXml(bind);
    field.setName(name);
    field.setRequired(required);
    field.setType(type);
    field.setCollection(CollectionType.VECTOR);

    return field;
  }
*/
  /*
  //for domain type filter
  private static final String CRI_FIELD_ID_1 = "TEST_FIELD_ID_1";
  private static final Collection CRI_LIST_1 = new ArrayList();
  private static final String CRI_VALUE_1_1 = "TEST_VALUE_1_1";
  private static final String CRI_VALUE_1_2 = "TEST_VALUE_1_2";
  private static final String CRI_VALUE_1_3 = "TEST_VALUE_1_3";

  //for range type filter
  private static final String CRI_FIELD_ID_2 = "TEST_FIELD_ID_2";
  private static final Integer CRI_VALUE_2_1 = new Integer(1);
  private static final Long CRI_VALUE_2_2    = new Long(999999);

  //for single type filter
  private static final String CRI_FIELD_ID_3 = "TEST_FIELD_ID_3";
  private static final Boolean CRI_VALUE_3   = Boolean.TRUE;
  private static final IDataFilter CRITERIA_2 = new DataFilterImpl();

  static
  {
    CRI_LIST_1.add(CRI_FIELD_ID_1);
    CRI_LIST_1.add(CRI_FIELD_ID_2);
    CRI_LIST_1.add(CRI_FIELD_ID_3);

    CRITERIA_2.addDomainFilter(null, CRI_FIELD_ID_1, CRI_LIST_1, false);

    CRITERIA_2.addRangeFilter(CRITERIA_2.getAndConnector(),
      CRI_FIELD_ID_2, CRI_VALUE_2_1, CRI_VALUE_2_2, false);

    CRITERIA_2.addSingleFilter(CRITERIA_2.getOrConnector(),
      CRI_FIELD_ID_3, CRITERIA_2.getEqualOperator(), CRI_VALUE_3, true);
  }


  public static void main(String[] args)
  {
    try
    {
      DataFilterMapping mapping = new DataFilterMapping();

      String filterStr = com.gridnode.pdip.framework.db.DataFilterHandler.serializeToString(
        CRITERIA_2, "default");

      System.out.println("Filter Str: "+filterStr);

      DataFilterImpl filter = (DataFilterImpl)com.gridnode.pdip.framework.db.DataFilterHandler.deserializeFromString(
        filterStr);
      System.out.println("Filter: "+filter.getFilterExpr());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

  }*/

}

