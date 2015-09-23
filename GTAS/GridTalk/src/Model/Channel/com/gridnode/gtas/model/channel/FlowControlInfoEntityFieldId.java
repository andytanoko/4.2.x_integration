package com.gridnode.gtas.model.channel;

import java.util.Hashtable;

public class FlowControlInfoEntityFieldId
{

  private Hashtable _table;
  private static FlowControlInfoEntityFieldId _self = null;

  public FlowControlInfoEntityFieldId()
  {
     _table = new Hashtable();

    _table.put(IFlowControlInfo.ENTITY_NAME,
      new Number[]
      {
        IFlowControlInfo.IS_ZIP,
        IFlowControlInfo.ZIP_THRESHOLD,
        IFlowControlInfo.IS_SPLIT,
        IFlowControlInfo.SPLIT_THRESHOLD,
        IFlowControlInfo.SPLIT_SIZE
      });
  }


  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new FlowControlInfoEntityFieldId();
    }
    return _self._table;
  }


}





