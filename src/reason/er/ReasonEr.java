/**
 * 
 */
package reason.er;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import reason.er.Functions.*;
import reason.er.Objects.*;
/**
 *
 * @author aaron
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReasonEr {
	
	public static final Exception expression = new Exception("Invalid expression: ");
	
	public static void main(String[] args) {
		
    	KnowledgeBase kb = new KnowledgeBase(0,99);//KnowledgeBase.makeTestKnowledgeBase();//
    	
//    	System.out.println(kb.toString());
//    	toFile("knowledgeBase.txt",kb.toString());
    	
    	kb.normalize();
    	
    	System.out.println(kb.toString());
    	toFile("normalizedKnowledgeBase.txt",kb.toString());
    	
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
