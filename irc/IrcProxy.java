/***
 * Irc class : simple implementation of a chat using JAVANAISE
 * Contact: 
 *
 * Authors: 
 */

package irc;

import java.awt.*;
import java.awt.event.*;


import jvn.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IrcProxy {
	public TextArea		text;
	public TextField	data;
	Frame 			frame;
	SentenceInterface       sentence;


  /**
  * main method
  * create a JVN object nammed IRC for representing the Chat application
  **/
	public static void main(String argv[]) {
	   try {
		             System.out.println("irc.IrcProxy.main()");
//		// initialize JVN
//		JvnServerImpl js = JvnServerImpl.jvnGetServer();
//		
//		// look up the IRC object in the JVN server
//		// if not found, create it, and register it in the JVN server
//		JvnObject jo = js.jvnLookupObject("IRC");

                SentenceInterface sentence = (SentenceInterface) JvnProxy.newInstance("IRC");
		   
		if (sentence == null) {
			System.out.println("Création d'un objet Sentence..");
			sentence = (SentenceInterface) JvnProxy.newInstance(new Sentence());
			System.out.println("Création terminée.");
		}
		// create the graphical part of the Chat application
		 new IrcProxy(sentence);
	   
	   } catch (Exception e) {
		   System.out.println("IRCProxy problem : " + e);
		   e.printStackTrace();
	   }
	}

  /**
   * IRC Constructor
   @param jo the JVN object representing the Chat
   **/
	public IrcProxy(SentenceInterface jo) {
		sentence = jo;
		frame=new Frame();
		frame.setLayout(new GridLayout(1,1));
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		frame.add(text);
		data=new TextField(40);
		frame.add(data);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListenerProxy(this));
		frame.add(read_button);
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListenerProxy(this));
		frame.add(write_button);
		frame.setSize(545,201);
		text.setBackground(Color.black); 
		frame.setVisible(true);
                
                frame.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e){
                        try {
                            JvnServerImpl.jvnGetServer().jvnTerminate();
                            System.exit(0);
                        } catch (JvnException ex) {
                            Logger.getLogger(Irc.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
	}
}


 /**
  * Internal class to manage user events (read) on the CHAT application
  **/
 class readListenerProxy implements ActionListener {
	IrcProxy ircProxy;
  
	public readListenerProxy (IrcProxy i) {
		ircProxy = i;
	}
   
 /**
  * Management of user events
  **/
	public void actionPerformed (ActionEvent e) {
	 try {
		// lock the object in read mode
		
		// invoke the method
		String s = ircProxy.sentence.read();
		
		// unlock the object
//		irc.sentence.jvnUnLock();
		
		// display the read value
		ircProxy.data.setText(s);
		ircProxy.text.append(s+"\n");
	   } catch (Exception je) {
		   System.out.println("readListenerProxy problem : " + je.getMessage());
	   }
	}
}

 /**
  * Internal class to manage user events (write) on the CHAT application
  **/
 class writeListenerProxy implements ActionListener {
	IrcProxy ircProxy;
  
	public writeListenerProxy (IrcProxy i) {
        	ircProxy = i;
	}
  
  /**
    * Management of user events
   **/
	public void actionPerformed (ActionEvent e) {
	   try {	
			// get the value to be written from the buffer
		    String s = ircProxy.data.getText();
		        	
		    // lock the object in write mode
//			irc.sentence.jvnLockWrite();
			
			// invoke the method
			ircProxy.sentence.write(s);
			
			// unlock the object
	   } catch (Exception ex) {
		   System.out.println("writeListenerProxy problem  : " + ex);
                   ex.printStackTrace();
	   }
	}
}




