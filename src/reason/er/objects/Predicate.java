/**
 * 
 */
package reason.er.objects;

import java.util.ArrayList;

import reason.er.compositeObjects.*;

/**
 *
 * @author Aaron
 */
@SuppressWarnings("rawtypes")
public abstract class Predicate<T> {

	public static final char[] uppers = {'A','B','C','D','E','F','G','H','I','J','K','L','M',
										 'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	protected int size;
	protected ArrayList<Term<T>> terms;
    protected long scope;
    protected long label;
    protected boolean negated;
    
    
	public abstract Predicate<T> clone(Expression e);
    
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
    
    public boolean isExpression() {
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
    
    public long getLabel() {
    	return label;
    }
    
    protected String makeLabel(long count) {
    	String s = count < 0?"R":"C",t = "";
    	count=count<0?count*-1:count;
    	do{
    		t = Character.toString(uppers[(int)(count % uppers.length)])+t;
    		count = count / uppers.length;
    	}while(count-- > 0);
    	
    	return s+t;
    }

    public static long getLabelIndex(char c) {
    	int i = 0;
    	for(char x : uppers) {
    		if(x == c)
    			return (long)i;
    		i++;
    	}
    	return -1;
    }
}
