function setAppendField(field)
{
  try
  {
    document.getElementById("appendField").value = field;
  }
  catch(error)
  {
    alert('Error caught in setAppendField()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function append()
{
  try
  {
    var object = "";
    try
    {
      object = document.getElementById("object_value").value;
    }
    catch(error)
    {
      alert('Failed to get Object value');
      throw error;
    }

    var field = "";
    try
    {
      field = document.getElementById("field_value").value;
    }
    catch(error)
    {
      alert('Failed to get Field value');
      throw error;
    }

    if(object == "" || field == "")
    {
      return;
    }

    var appendField = "";
    try
    {
      appendField = document.getElementById("appendField").value;
    }
    catch(error)
    {
      alert('Failed to get appendField value');
      throw error;
    }

    var fieldToAppend;
    try
    {
      var textToAppend = object + field;

      fieldToAppend = document.getElementById(appendField);
      fieldToAppend.value = fieldToAppend.value + textToAppend;
      fieldToAppend.focus()
    }
    catch(error)
    {
      alert('Failed to get fieldToAppend object');
      throw error;
    }
  }
  catch(error)
  {
    alert('Error caught in append()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}
