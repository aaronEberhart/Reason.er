package reason.er.objects;

import java.util.ArrayList;

import reason.er.compositeObjects.*;

/**
 * Abstract class that describes all Predicate Objects. Predicates 
 * have a label, terms, a scope, a size, and can be negated.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
public abstract class Predicate<T,U> {

	/**
	 * Array of capital letters for name generation.
	 */
	public static final char[] uppers = {'A','B','C','D','E','F','G','H','I','J','K','L','M',
										 'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	/**
	 * The size of the predicate.
	 */
	protected int size;
	/**
	 * The list of Terms
	 */
	protected ArrayList<Term<T>> terms;
	/**
	 * The outermost Term scope of the Predicate.
	 */
	protected T scope;
	/**
	 * The name of the Predicate.
	 */
	protected U label;
	/**
	 * Indicator of negation.
	 */
	protected boolean negated;
	
	/**
	 * Make a unique copy of the Predicate in memory.
	 * @param e Expression&lt;T,U&gt;
	 * @return Predicate&lt;T,U&gt;
	 */
	public abstract Predicate<T,U> clone(Expression<T,U> e);
	
	/**
	 * Returns a String representation of the Description
	 * Logic object.
	 * @return  String
	 */
	public abstract String toDLString();

	/**
	 * Returns a String representation of the Functional
	 * Syntax object.
	 * @return  String
	 */
	public abstract String toFSString(int tab);

	/**
	 * Indicates whether the Predicate is negated.
	 * @return boolean
	 */
	public boolean isNegated() {
		return negated;
	}
	
	/**
	 * Negates the Predicate. Adjusts the size.
	 * @return self
	 */
	public Predicate<T,U> negate() {
		if(negated) {
			negated = false;size--;
		}
		else {
			negated = true;size++;
		}
		return this;
	}
	
	/**
	 * Returns Term[i] of the Predicate.
	 * @param i int
	 * @return Term&lt;T&gt;[i]
	 */
	public Term<T> getTerm(int i) {
		return terms.get(i);
	}
	
	/**
	 * Sets the name of the Predicate to s.
	 * @param s &lt;U&gt;
	 */
	protected void setLabel(U s) {
		label = s;
	}

	/**
	 * Returns the label of the Predicate.
	 * @return &lt;U&gt;
	 */
	public U getLabel() {
		return label;
	}
	
	/**
	 * Returns false
	 * @return false
	 */
	public boolean isRole() {
		return false;
	}
	
	/**
	 * Returns false.
	 * @return false
	 */
	public boolean isExpression() {
		return false;
	}
	
	/**
	 * Gets the scope of the Predicate.
	 * @return &lt;T&gt;
	 */
	public T getScope() {
		return scope;
	}
	
	/**
	 * Sets the scope of the Predicate.
	 * @param t &lt;T&gt;
	 */
	public void setScope(T t) {
		scope = t;
	}
	
	/**
	 * Gets the size of the Predicate.
	 * @return int
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Sets the size of the Predicate.
	 * @param i int
	 */
	public void setSize(int i) {
		size = i;
	}
	
	/**
	 * Makes a string to represent the unique Predicate id.
	 * @return String
	 */
	public String makeLabel() {
		long count;
		
		try {
			count = (long)label;
		}catch(Exception e){
			count = 0;
		}
		String s  = "";
		
		if(count<0) {
			count*=-1;
			do{
				int index = (int)(count % (uppers.length / 2)) + (uppers.length / 2);
				s = Character.toString(uppers[index])+s;
				count = count / uppers.length;
			}while(count-- > 0);
		}
		else {
			do{
				int index = (int)(count % (uppers.length / 2));
				s = Character.toString(uppers[index])+s;
				count = count / uppers.length;
			}while(count-- > 0);
		}
		return s;
	}

	public static String makeLabel(long label) {
		long count;
		
		try {
			count = (long)label;
		}catch(Exception e){
			count = 0;
		}
		String s  = "";
		
		if(count<0) {
			count*=-1;
			count--;
			do{
				int index = (int)(count % (uppers.length / 2)) + (uppers.length / 2);
				s = Character.toString(uppers[index])+s;
				count = count / (uppers.length/2);
			}while(count-- > 0);
		}
		else {
			do{
				int index = (int)(count % (uppers.length / 2));
				s = Character.toString(uppers[index])+s;
				count = count / (uppers.length/2);
			}while(count-- > 0);
		}
		return s;
	}
	
	/**
	 * Returns the index of the character in the name array. Only works \
	 * for values -13 - x - 13.
	 * @param c char 
	 * @return long
	 */
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
