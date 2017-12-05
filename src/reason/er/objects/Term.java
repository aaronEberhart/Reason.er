/**
 *
 *
 */
package reason.er.objects;

/**
 *
 * @author aaron
 */
public class Term<T> {
    
	public static final char[] lowers = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	protected T term;
	
	public Term(T t) {
		this.term = t;
	}

	public T getValue() {
		return term;
	}
	
	public void setValue(T term) {
		this.term = term;
	}
	
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
	
	private boolean isString(T t) {
		try {
			String s = (String)t;
			return true;
		}catch(Exception e){
			return false;
		}
	}

	private boolean isLong(T t) {
		try {
			long s = (long)t;
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	private boolean isInt(T t) {
		try {
			int s = (int)t;
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public void setValue(String makeVariable) {
		this.term = (T) makeVariable;
	}
	
	public static String makeVariable(long count) {
    	String s = "";
    	
    	do{
    		s = Character.toString(lowers[(((int)count + lowers.length) % lowers.length)]) + s;
    		count = count / lowers.length;
    	}while(count-- > 0);
    	
    	return s;
    }
	
	public static long getVarIndex(char c) {
    	int i = 0;
    	for(char x : lowers) {
    		if(x == c)
    			return (long)i;
    		i++;
    	}
    	return -1;
    }
}
