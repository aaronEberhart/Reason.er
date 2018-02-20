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
@SuppressWarnings({"rawtypes"})
public class ReasonEr {
	
	/**	
	 * Exception message for expression errors.
	 */
	public static final Exception expression = new Exception("Invalid expression: ");
	public static final int NUMTESTS = 100;
	public static final int ABOXSIZE = 99;
	public static final int TBOXSIZE = 99;
	
	/**
	 * Main class for program.
	 * 
	 * @param args String
	 */
	public static void main(String[] args) {
		
		
		
		prepDirs();
		
		KnowledgeBase kb;
		int i = 0;
		for(; i < NUMTESTS; i++) {
			
			System.out.println("Making Expressions " + i);
			
			kb = new KnowledgeBase(ABOXSIZE,TBOXSIZE);
		
			toFile("output\\knowledgeBases\\knowledgeBase["+i+"].txt",kb.toString());
			System.out.println("Normalizing Expressions "+i+"\n");
		
			kb.normalize();
		
			toFile("output\\normalizedKnowledgeBase["+i+"].txt",kb.toString());
			
//			Tableau t = new Tableau(kb);
//			t.run();
//		
//			toFile("output\\tableaux\\tableau["+i+"].txt",t.toString());
		
		}
		
		System.out.println("\n\n"+i+" TESTS DONE");
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
		
	/**
	 * Make sure the output directories exist.
	 */
	public static void prepDirs() {
		File f1 = new File("output");
		if(!f1.exists())
			f1.mkdir();
		
		File f2 = new File("output\\knowledgeBases");
		if(!f2.exists())
			f2.mkdir();
		
		File f3 = new File("output\\normalizedKnowledgeBases");
		if(!f3.exists())
			f3.mkdir();
		
		File f4 = new File("output\\tableaux");
		if(!f4.exists())
			f4.mkdir();
	}
	
}
