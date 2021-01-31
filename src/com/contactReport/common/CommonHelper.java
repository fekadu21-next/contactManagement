/*
 * CommonHelper.java
 *
 * Created on February 25, 2010, 11:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.contactReport.common;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author bishnu
 */
public class CommonHelper {
    
    /** Creates a new instance of CommonHelper */
    public CommonHelper() {
    }
    
    
    public static  void setFrameInCenter(JFrame jframe) {
        // make the frame half the height and width
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //int height = screenSize.height;
        //int width = screenSize.width;
        //jframe.setSize(width/2, height/2);
       // jframe.setL
        // here's the part where i center the jframe on screen
        jframe.setLocationRelativeTo(null);
        
    }
    
    public static  boolean isStringEmptyOrNull(String str) {
        if(str == null){
            return true;
        }else if(str != null && str.length()==0){
            return true;
        }
        return false;
    }
    
}
