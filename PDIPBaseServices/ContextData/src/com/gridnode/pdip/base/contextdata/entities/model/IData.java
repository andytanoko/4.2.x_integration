package com.gridnode.pdip.base.contextdata.entities.model;

public interface IData {

    public static String FILE_TYPE="File";
    public static String ENTITY_TYPE="Entity";
    public static String MEM_TYPE="Memory";

    public String getDataType();
    public void setDataType(String dataType);
    public Object getData();
    public void setData(Object obj);
}