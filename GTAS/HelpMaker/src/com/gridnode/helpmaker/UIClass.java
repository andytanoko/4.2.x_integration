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

public class UIClass {
  boolean packFrame = false;

  /**Construct the application*/
  public UIClass() {
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

    frame.srcFileTF.setText("c:\\temp\\doc\\gtas_ag.htm");
    frame.destFolderTF.setText("c:\\temp\\doc\\javahelp");
    frame.cssTF.setText("gridnode.css");
//    frame.titleTF.setText("Admin Guide");
    frame.shortnameTF.setText("gtas_ag");

    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }
  /**Main method*/
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch(Exception e) {
      e.printStackTrace();
      Logger.write(e.getMessage());
    }
    new UIClass();
  }
}
