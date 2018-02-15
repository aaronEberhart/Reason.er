package reason.er;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import reason.er.objectFunctions.*;
import reason.er.compositeObjects.*;
import reason.er.objects.*;

/**
 * @author Aaron Eberhart
 */
@SuppressWarnings({ "rawtypes" , "unchecked" })
public class ReasonEr {
	
	/**	
	 * Exception message for expression errors.
	 */
	public static final Exception expression = new Exception("Invalid expression: ");
	public static final int NUMTESTS = 1;
	
	/**
	 * Main class for program.
	 * 
	 * @param args String
	 */
	public static void main(String[] args) {
		
		System.out.println("Making Expressions");
		
    	KnowledgeBase kb;
    	
    	for(int i = 0; i < NUMTESTS; i++) {
    		
    		kb = new KnowledgeBase(9999,9999);
    	
//    		System.out.println(kb.toString());
    		toFile("knowledgeBase["+i+"].txt",kb.toString());
    	
    		System.out.println("Normalizing Expressions");
    	
    		kb.normalize();
    	
//    		System.out.println(kb.toString());
    		toFile("normalizedKnowledgeBase["+i+"].txt",kb.toString());
//    	
//    		Tableau t = new Tableau(kb);
//    		t.run();
//    	
//    		System.out.println(t.toString());
//    		toFile("tableau["+i+"].txt",t.toString());
    	
    	}
    	
    	System.out.println("\nDONE");
    }
    
	/**
	 * Write text to a file.
	 * 
	 * @param filename String
	 * @param text String
	 */
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
