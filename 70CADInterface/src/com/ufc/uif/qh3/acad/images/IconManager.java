package com.ufc.uif.qh3.acad.images;

import javax.swing.ImageIcon;

/**
 * <p>Title: IconManager</p>
 * <p>Description: </p>
 * @author  Zhongning.B
 * @version $Id: IconManager.java,v 1.1 2010/11/11 03:43:08 wangjf Exp $
 * @Created On Aug 26, 2008
 */
public class IconManager 
{
  public static ImageIcon getIcon(String s)
  {
    java.net.URL imgURL = IconManager.class.getResource(s);
    
    ImageIcon icon = null;
    
    if (imgURL != null) 
    {
      icon = new ImageIcon(imgURL);
    }
    
    return icon;
  }
}
