package reason.er.objects;

import java.util.ArrayList;

import reason.er.compositeObjects.*;

/**
 * 
 * @author Aaron Eberhart
 *
 * @param T generic
 * @param U generic
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Role<T,U> extends Predicate<T,U> {

	/**
	 * Empty constructor.
	 */
	public Role() {
	}
	
	/**
	 * Manual Role constructor.
	 * @param hasSign boolean
	 * @param t generic
	 * @param u generic
	 * @param name long
	 */
	public Role(boolean hasSign, T t, T u, U name){
    	Term<T> s = new Term<T>(t);
    	Term<T> r = new Term<T>(u);    	
    	this.terms = new ArrayList(2);
    	this.terms.add(s);
    	this.terms.add(r);
    	this.label = name;
    	this.scope = (T)s.getValue();
    	this.negated = hasSign;
    	this.size = 1;
    	if(negated)
    		size++;
    }
	
	/**
	 * Returns true. This is a Role.
	 */
	@Override
	public boolean isRole() {
		return true;
	}
	
	@Override
	public String toString() {	
		String s = makeLabel() + "(" + this.terms.get(0).toString() + "," + this.terms.get(1).toString() + ")";
    	if(negated)
    		s = "--" + s;
    	return s;
    }

	@Override
	public Predicate<T,U> clone(Expression<T,U> e) {
		return new Role(e.negated,(T)((Term)terms.get(0)).getValue(),(U)((Term)terms.get(1)).getValue(),(long)label);
	}
	
	
}
