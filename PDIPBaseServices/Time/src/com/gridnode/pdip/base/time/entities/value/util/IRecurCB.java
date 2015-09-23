package com.gridnode.pdip.base.time.entities.value.util;

import java.util.Date;

//recurrence callback function
public interface IRecurCB
{
  boolean onValue(Date instanceStart, Date instanceEnd);
}
