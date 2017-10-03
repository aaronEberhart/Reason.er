/**
 * 
 */
package reason.er;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import reason.er.Functions.*;
/**
 *
 * @author aaron
 */
public class ReasonEr {

	
	public static final Exception expression = new Exception("Invalid expression: ");
	
	
    @SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) {
    	
    	KnowledgeBase kb = new KnowledgeBase(99,99);
    	
    	System.out.println(kb.toString());
    	toFile("file.txt",kb.toString());
    	
    	System.out.println("DONE");
    }
    
    public static void toFile(String filename, String text) {
    	try{
    		BufferedWriter f = new BufferedWriter(new FileWriter(new File(filename)));
    		f.write(text);
    		f.close();
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }
        
}
