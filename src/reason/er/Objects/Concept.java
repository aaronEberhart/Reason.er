/**
 *
 *
 */
package reason.er.Objects;

import java.util.ArrayList;

/**
 *
 * @author aaron
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Concept<T> extends Predicate {
    
	public Concept() {}
	
	public Concept(boolean hasSign, T t, String name){
		
		this.terms = new ArrayList();
		Term<T> s = new Term<T>(t);
    	this.terms.add(s);
    	
    	this.label = name;
    	this.scope = s;
    	this.negated = hasSign;
    }

	public Term<T> getTerm() {
		return (Term<T>) this.terms.get(0);
	}
	
	@Override
    public String toString() {
    	String s = "";
    	if(negated)
    		s += "--";
    	return s + this.label + "(" + this.getTerm().toString() + ")";
    }
}
