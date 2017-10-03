/**
 * 
 */
package reason.er.Objects;

import java.util.ArrayList;

/**
 *
 * @author Aaron
 */
public abstract class Predicate<T> {

	
	protected ArrayList<Term<T>> terms;
    protected Term<T> scope;
    protected String label;
    protected boolean negated;
    
    public boolean isNegated() {
    	return negated;
    }
    
    public void negate() {
    	if(negated)
    		negated = false;
    	else
    		negated = true;
    }
    
    public Term<T> getScope() {
    	return this.scope;
    }
    
    public Term<T> getTerm(int i) {
    	return terms.get(i);
    }

    protected void setLabel(String s) {
    	label = s;
    }

    public boolean isRole() {
		return false;
	}
}
