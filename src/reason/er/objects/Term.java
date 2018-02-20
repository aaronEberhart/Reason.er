package reason.er.objects;


/**
 * This class is an attempt to allow different datatypes into the variable binding
 * scheme. Currently the long type has been developed the most as it is used 
 * in the other classes. Adding functionality to this should be straightforward 
 * if it was necessary.
 * 
 * @author Aaron Eberhart
 *
 * @param <T> generic
 */
public class Term<T> {
	
	/**
	 * Array of lower case letters for name generation.
	 */
	public static final char[] lowers = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	/**
	 * Generic variable in Term.
	 */
	protected T term;
	
	/**
	 * Make a generic Term
	 * @param t &lt;T&gt;
	 */
	public Term(T t) {
		this.term = t;
	}

	/**
	 * Gets the Term value.
	 * @return the value of the Term
	 */
	public T getValue() {
		return term;
	}
	
	/**
	 * Set the value of the Term.
	 * @param term &lt;T&gt;
	 */
	public void setValue(T term) {
		this.term = term;
	}
	
	@Override
	public String toString() {
		if(isString(term))
			return (String)term;
		else if(isInt(term))
			return makeVariable(Integer.toUnsignedLong((int)term));
		else if(isLong(term))
			return makeVariable((long)term);
		else
			return this.term.toString();
	}
	
	/**
	 * Check if a Term is of String type.
	 * @param t &lt;T&gt;
	 * @return boolean
	 */
	private boolean isString(T t) {
		try {
			String s = (String)t;
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Check if a Term is of long type.
	 * @param t &lt;T&gt;
	 * @return boolean
	 */
	private boolean isLong(T t) {
		try {
			long s = (long)t;
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Check if a Term is of int type.
	 * @param t &lt;T&gt;
	 * @return boolean
	 */
	private boolean isInt(T t) {
		try {
			int s = (int)t;
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Returns a string representation of a long type Term.
	 * 
	 * @param count long
	 * @return string
	 */
	public static String makeVariable(long count) {
		String s = "";
		
		if(count<0) {
			count*=-1;
			count--;
			do{
				int index = (int)(count % (lowers.length / 2));
				s = Character.toString(lowers[index]) + s;
				count = count / (lowers.length / 2);
			}while(count-- > 0);
		}else {
			count--;
			do{
				int index = (int)(count % (lowers.length / 2)) + (lowers.length / 2);
				s = Character.toString(lowers[index]) + s;
				count = count / (lowers.length / 2);
			}while(count-- > 0);
		}
		return s;
	}
	
	/**
	 * Returns the index of a char in the lower case array.
	 * @param c character
	 * @return index
	 */
	public static long getVarIndex(char c) {
		int i = 0;
		for(char x : lowers) {
			if(x == c)
				return i<13?(long)-1*i:(long)(i - 13);
			i++;
		}
		return -1;
	}
}
