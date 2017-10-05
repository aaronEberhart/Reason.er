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
public class ReasonEr {

	
	public static final Exception expression = new Exception("Invalid expression: ");
	
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
    	
//    	ABox abox = new ABox(new ArrayList<>());
//    	TBox tbox = new TBox(new ArrayList<>());
//    	
//    	tbox.addManually(new Expression(new QuantifiedRole(false,1,'o','n',"N","A","N"))
//    			.and(new Concept(true,'o',"B")).negate().superClass(new Concept(false,'o',"C")));
//    	
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
