package reason.er;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import reason.er.objectFunctions.*;
import reason.er.compositeObjects.*;
import reason.er.objects.*;
import reason.er.util.RandomInteger;

/**
 * Test class for program.
 * @author Aaron Eberhart
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ReasonEr {
	
	/**	
	 * Exception message for expression errors.
	 */
	public static final Exception expression = new Exception("Invalid expression: ");
	/**
	 * Number of tests to run.
	 */
	public static final int NUMTESTS = 10;
	/**
	 * Size of each ABox to make.
	 */
	public static final int ABOXSIZE = 15;
	/**
	 * Size of each TBox to make.
	 */
	public static final int TBOXSIZE = 15;
	/**
	 * Maximum quantification depth.
	 */
	public final static int quantificationDepth = 10;
	/**
	 * Maximum sub-Expressions allowed per Expression.
	 */
	public final static int maxSubExpressions = 10;
	/**
	 * Maximum size of all Expressions.
	 */
	public final static  int maxSize = 5;
	/**
	 * int for Predicate name restriction.
	 */
	public final static int universe = 5;//Predicate.uppers.length / 2;
	/**
	 * int for variable use restriction.
	 */
	public final static int individuals = 5;//Term.lowers.length / 2;
	
	/**
	 * Main method for program.
	 * 
	 * @param args String
	 */
	public static void main(String[] args) {
		
		prepDirs();
	
		KnowledgeBase kb;
		int i = 0;
		
		for(; i < NUMTESTS; i++) {
			
//			j = testWeightedBoolean(100,10,j);
			
			System.out.println("Making Expressions " + i);
			
			kb = new KnowledgeBase(ABOXSIZE,TBOXSIZE);
		
			toFile("output\\knowledgeBases\\debug\\debugKnowledgeBase["+i+"].txt",kb.toString());
			toFile("output\\knowledgeBases\\descriptionLogic\\dlKnowledgeBase["+i+"].txt",kb.toDLString());
			toFile("output\\knowledgeBases\\functionalSyntax\\fsKnowledgeBase["+i+"].owl",kb.toFSString("<http://www.randomOntology.com/not/a/real/IRI/>"));
			
			System.out.println("Normalizing Expressions "+i+"\n");
		
			kb.normalize();
		
			toFile("output\\normalizedKnowledgeBases\\debug\\debugNormalizedKnowledgeBase["+i+"].txt",kb.toString());
			toFile("output\\normalizedKnowledgeBases\\descriptionLogic\\dlNormalizedKnowledgeBase["+i+"].txt",kb.toDLString());
			toFile("output\\normalizedKnowledgeBases\\functionalSyntax\\fsNormalizedKnowledgeBase["+i+"].owl",kb.toFSString("<http://www.randomOntology.com/not/a/real/IRI/>"));
			
			
			
//			Tableau t = new Tableau(kb);
//			
//			System.out.println("Running Tableau "+i+"\n");
//			
//			t.run();
//		
//			toFile("output\\tableaux\\tableau["+i+"].txt",t.toString());
			
		
		}
		
		System.out.println("\n\n"+i+" TESTS DONE\n");
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
		
		File f5 = new File("output\\knowledgeBases\\functionalSyntax");
		if(!f5.exists())
			f5.mkdir();
		
		File f6 = new File("output\\normalizedKnowledgeBases\\functionalSyntax");
		if(!f6.exists())
			f6.mkdir();
		
		File f7 = new File("output\\knowledgeBases\\descriptionLogic");
		if(!f7.exists())
			f7.mkdir();
		
		File f8 = new File("output\\normalizedKnowledgeBases\\descriptionLogic");
		if(!f8.exists())
			f8.mkdir();
		

		File f9 = new File("output\\knowledgeBases\\debug");
		if(!f9.exists())
			f9.mkdir();
		
		File f10 = new File("output\\normalizedKnowledgeBases\\debug");
		if(!f10.exists())
			f10.mkdir();
	}
	
	/**
	 * Method to test the odds of the weightedBoolean function.
	 * 
	 * @param upper int
	 * @param lower int
	 * @param j int
	 * @return int
	 */
	public static int testWeightedBoolean(int upper, int lower, int j) {
		RandomInteger rand;
		for(int i = 0; i < NUMTESTS; i++) {
			rand = new RandomInteger();
			if(rand.weightedBool(upper, lower))
				j++;
		}
		return j;
	}
	
}

