/**
 *
 *
 */
package reason.er.Objects;

import java.util.ArrayList;

import reason.er.Functions.*;

/**
 *
 * @author aaron
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Concept<T> extends Predicate {
    
	public Concept() {}
	
	public Concept(boolean hasSign, T t, long name){
		
		this.terms = new ArrayList();
		Term<T> s = new Term<T>(t);
    	this.terms.add(s);
    	
    	this.label = name;
    	this.scope = (long)s.getValue();
    	this.negated = hasSign;
    	this.size = 1;
    	if(negated)
    		size++;
    }
	
	public Concept(boolean hasSign, Term t, long name){
		
		this.terms = new ArrayList();
    	this.terms.add(t);
    	
    	this.label = name;
    	this.scope = name;
    	this.negated = hasSign;
    	this.size = 1;
    	if(negated)
    		size++;
    }

	public Term<T> getTerm() {
		return (Term<T>) this.terms.get(0);
	}
	
	@Override
    public String toString() {
    	String s = "";
    	if(negated)
    		s += "--";
    	return s + makeLabel(this.label) + "(" + this.getTerm().toString() + ")";
    }

	@Override
	public Predicate clone(Expression e) {
		return new Concept(e.negated, this.getTerm().getValue(), label);
	}
}
