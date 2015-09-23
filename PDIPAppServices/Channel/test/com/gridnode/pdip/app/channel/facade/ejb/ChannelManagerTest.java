package com.gridnode.pdip.app.channel.facade.ejb;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.model.ChannelInfo;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.*;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.*;
import junit.framework.AssertionFailedError;

public class ChannelManagerTest
{

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(new TestSuite(ChannelManagerChannelInfoTest.class));
    junit.textui.TestRunner.run(new TestSuite(ChannelManagerCommInfoTest.class));
  }

}