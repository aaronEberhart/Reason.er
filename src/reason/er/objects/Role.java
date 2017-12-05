/**
 * 
 */
package reason.er.objects;

import java.util.ArrayList;

import reason.er.compositeObjects.*;

/**
 *
 * @author aaron
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Role<T,U> extends Predicate {

	public Role() {
	}
	
	public Role(boolean hasSign, T t, U u, long name){
    	Term<T> s = new Term<T>(t);
    	Term<U> r = new Term<U>(u);    	
    	this.terms = new ArrayList();
    	this.terms.add(s);
    	this.terms.add(r);
    	this.label = name;
    	this.scope = (long)s.getValue();
    	this.negated = hasSign;
    	this.size = 1;
    	if(negated)
    		size++;
    }
	
	@Override
	public boolean isRole() {
		return true;
	}
	
	public String toString() {	
		String s = makeLabel(this.label) + "(" + this.terms.get(0).toString() + "," + this.terms.get(1).toString() + ")";
    	if(negated)
    		s = "--" + s;
    	return s;
    }

	@Override
	public Predicate<T> clone(Expression e) {
		return new Role(e.negated,((Term)terms.get(0)).getValue(),((Term)terms.get(1)).getValue(),label);
	}
	
	
}
