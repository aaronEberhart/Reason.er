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
		
    	KnowledgeBase kb = makeTestKnowledgeBase();
    	
    	System.out.println(kb.toString());
    	toFile("knowledgeBase.txt",kb.toString());
    	
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
        
    public static <T extends Expression<T>,U extends Expression <U>> KnowledgeBase<T,U> makeTestKnowledgeBase() {
    	
    	ABox<T> abox = new ABox(new ArrayList<T>());
    	
    	abox.addManually(new Expression(new Role(false,(long)0,(long)1,(long)17)));
    	
		TBox<U> tbox = new TBox(new ArrayList<U>());
    	
    	tbox.addManually(new Expression(new QuantifiedRole(false,1,(long)23,(long)22,(long)19,(long)0,(long)19)).negate().dot(new Quantifier(2),new Role(false,(long)24,(long)23,(long)18),(long)3)
    			.and(new Concept(false,(long)24,(long)1)).negate().equivalent(new Concept(false,(long)24,(long)2)));
    	
    	return new KnowledgeBase(abox,tbox);
    }
}
