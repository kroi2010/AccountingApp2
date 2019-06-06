package home.accounting.controller;


import com.sun.glass.events.KeyEvent;
import com.sun.glass.ui.Robot;

import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;

@SuppressWarnings("restriction")
public class TextFieldPasteListener extends TextField {
  /* @Override
   public void paste() {
	   final Clipboard myClipboard = Clipboard.getSystemClipboard();
       if (myClipboard.hasString()) {
           final String myText = myClipboard.getString();
           if (myText != null) {
               System.out.println("Pasted: "+myText+" from field "+super.getId());
               confirmPaste(myText);
           }
       }
       //super.paste();
       try {
           Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();
           robot.keyPress(KeyEvent.VK_ENTER);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
   private Boolean confirmPaste(String text){
	   boolean good = true;
	   System.out.println("Whole: "+super.getText()+text);
	   return good;
   }*/
}