function updateDateField(fieldName, paramIndex)
{
  try
  {
    var valueField = document.entityForm.elements[fieldName];
    if(valueField.length)
    {
      valueField = valueField[paramIndex];
    }
    
    var yearValue = getDateFieldValue(fieldName,'year', true, paramIndex);
    var monthValue = getDateFieldValue(fieldName,'month', true, paramIndex);
    var dayValue = getDateFieldValue(fieldName,'day', true, paramIndex);
    var hourValue = getDateFieldValue(fieldName,'hour', true, paramIndex);
    var minuteValue = getDateFieldValue(fieldName,'minute', true, paramIndex);
    var secondValue = getDateFieldValue(fieldName,'second', true, paramIndex);
    
    valueField.value =  yearValue
                        + '-' + monthValue
                        + '-' + dayValue
                        + ' ' + hourValue
                        + ':' + minuteValue
                        + ':' + secondValue;
  }
  catch(error)
  {
    alert('Error caught by updateDateField('
          + fieldName
          + ')\ntype=' + error.type
          + '\nname=' + error.name );
    throw error;
  }
}

function getDateFieldValue(fieldName, postfix, pad, paramIndex)
{
  var value = getFieldValue(fieldName + '_' + paramIndex + '_' + postfix);
  if(value)
  {
    if( pad && (value.length < 2) )
    {
      value = "0" + value;
    }
    return value;
  }
  else
  {
    return "00";
  }
}
