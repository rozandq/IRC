/***
 * Sentence class : used for representing the text exchanged between users
 * during a chat application
 * Contact: 
 *
 * Authors: 
 */

package irc;
import java.util.logging.Level;
import java.util.logging.Logger;
import jvn.JvnAnnotation;
import test.coordMain;

public class Sentence implements SentenceInterface, java.io.Serializable {
	String 		data;
  
	public Sentence() {
		data = new String("");
	}
	
        @JvnAnnotation(type = JvnAnnotation.JvnAnnotationType.WRITE)
	public void write(String text) {
            if(coordMain.printDebug) System.out.println("irc.Sentence.write()");;
            data = text;
            
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException ex) {
//                System.err.println("sleep err");;
//            }
	}
        
        @JvnAnnotation(type = JvnAnnotation.JvnAnnotationType.READ)
	public String read() {
            if(coordMain.printDebug) System.out.println("irc.Sentence.read()");
            return data;	
	}
	
}