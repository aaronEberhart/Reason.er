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
		
    	KnowledgeBase kb = new KnowledgeBase(9,9);//makeTestKnowledgeBase();
    	
    	System.out.println(kb.toString());
    	toFile("knowledgeBase.txt",kb.toString());
    	
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
    	
    	abox.addManually(new Expression(new Role(false,0,1,17)));
    	
		TBox<U> tbox = new TBox(new ArrayList<U>());
    	
    	tbox.addManually(new Expression(new QuantifiedRole(false,1,23,22,19,0,19)).dot(new Quantifier(2),new Role(false,25,23,18),3)
    			.and(new Concept(false,25,1)).equivalent(new Concept(false,25,2)));
    	
    	return new KnowledgeBase(abox,tbox);
    }
}
