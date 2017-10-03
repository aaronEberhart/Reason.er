/**
 *
 *
 */
package reason.er.Objects;

/**
 *
 * @author aaron
 */
public class Term<T> {
    
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

	public void setValue(String makeVariable) {
		this.term = (T) makeVariable;
		
	}

}
