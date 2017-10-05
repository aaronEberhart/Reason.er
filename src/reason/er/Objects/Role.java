/**
 * 
 */
package reason.er.Objects;

import java.util.ArrayList;

/**
 *
 * @author aaron
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Role<T,U> extends Predicate {

	public Role() {
	}
	
	public Role(boolean hasSign, T t, U u, String name){
    	Term<T> s = new Term<T>(t);
    	Term<U> r = new Term<U>(u);    	
    	this.terms = new ArrayList();
    	this.terms.add(s);
    	this.terms.add(r);
    	this.label = name;
    	this.scope = s;
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
		String s = this.label + "(" + this.terms.get(0) + "," + this.terms.get(1) + ")";
    	if(negated)
    		s = "--" + s;
    	return s;
    }
	
}
