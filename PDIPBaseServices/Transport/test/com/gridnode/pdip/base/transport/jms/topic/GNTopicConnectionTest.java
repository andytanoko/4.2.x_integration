package com.gridnode.pdip.base.transport.jms.topic;

import com.gridnode.pdip.base.transport.jms.topic.GNTopicConnection;

public class GNTopicConnectionTest
{
  private static String _hostname = null;
  private static int _port = 443;
  private static String _username = null;
  private static String _password = null;
  private static String _topicname = null;

  public GNTopicConnectionTest()
  {
  }

  public static void main(String[] args)
  {
    try
    {
      if (args.length > 0)
        _hostname = args[0];
      if (args.length > 1)
        _topicname = args[1];
      if (args.length > 2)
        _username = args[2];
      if (args.length > 3)
        _password = args[3];

      GNTopicConnection connection = new GNTopicConnection(_hostname, _port, _username, _password);
      connection.close();
      connection._configTransport = null;
      connection = null;
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

}