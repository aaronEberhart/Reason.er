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

	public static final char[] uppers = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	protected int size;
	protected ArrayList<Term<T>> terms;
    protected long scope;
    protected long label;
    protected boolean negated;
    
    public boolean isNegated() {
    	return negated;
    }
    
    public Predicate<T> negate() {
    	if(negated) {
    		negated = false;size--;
    	}
    	else {
    		negated = true;size++;
    	}
    	return this;
    }
    
    public Term<T> getTerm(int i) {
    	return terms.get(i);
    }

    protected void setLabel(long s) {
    	label = s;
    }

    public boolean isRole() {
		return false;
	}
    
    public long getScope() {
    	return scope;
    }
	
    public int getSize() {
    	return size;
    }
    
    public void setSize(int i) {
    	size = i;
    }
    
    protected String makeLabel(long count) {
    	String s = "";
    	
    	do{
    		s = Character.toString(uppers[(int)(count % uppers.length)]) + s;
    		count = count / uppers.length;
    	}while(count-- > 0);
    	
    	return s;
    }
}
