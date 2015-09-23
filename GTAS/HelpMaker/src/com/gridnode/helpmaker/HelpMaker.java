package com.gridnode.helpmaker;

import javax.swing.UIManager;
import java.awt.*;

/**
 * Title:        Help Maker
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      GridNode
 * @author Ferry
 * @version 1.0
 */

public class HelpMaker {
  boolean packFrame = false;

  /**Construct the application*/
  public HelpMaker() {
    HelpMakerFrame frame = new HelpMakerFrame();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);


    frame.setVisible(true);
  }
  /**Main method*/
  public static void main(String[] args) {
    try {
    	System.out.println("num args="+args.length);
    	if (args.length == 7 && args[0].equals("-xui"))
    	{
    		String title = args[1];
    		String shortname = args[2];
    		String inputHtml = args[3];
    		String destDir = args[4];
    		String stylesheet = args[5];
    		String splitLevel = args[6];
    		HtmlReader reader = new HtmlReader(inputHtml,
    		                                   destDir,
    		                                   stylesheet,
    		                                   title,
    		                                   shortname,
    		                                   Integer.parseInt(splitLevel));
        reader.split();
    	}
    	else
    	{
    		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        new HelpMaker();
    	}
    }
    catch(Exception e) {
      e.printStackTrace();
      Logger.write(e.getMessage());
    }
  }
}
