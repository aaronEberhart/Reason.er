package reason.er.objects;

/**
 * The quantifier is simply an integer ID with a toString translation.
 * @author Aaron Eberhart
 */
public class Quantifier {
	
	/**
	 * Integer id.
	 */
	private int quantifier;
	
	/**
	 * Set the quantifier.
	 * @param i int
	 */
	public Quantifier(int i) {
		quantifier = i;
	}
	
	/**
	 * Set the quantifier.
	 * @param i int
	 */
	public void setQuantifier(int i) {
		quantifier = i;
	}
	
	/**
	 * Returns the value of the id.
	 * @return int
	 */
	public int getInteger() {
		return quantifier;
	}
	
	/**
	 * Flips the quantifier value from E to A, or A to E.
	 */
	public void flipQuantifier() {
		if(quantifier == 1) {
			quantifier = 2;
		}
		else if(quantifier == 2){
			quantifier = 1;
		}
	}

	@Override
	public String toString() {
		if(quantifier == 1) {
			return "EXISTS ";
		}else if(quantifier == 2) {
			return "FORALL ";
		}else {
			return "NONE ";
		}
	}

	public String toDLString() {
		if(quantifier == 1) {
			return "Exs ";
		}else if(quantifier == 2) {
			return "All ";
		}else {
			return "";
		}
	}
	
	public String toFSString() {
		if(quantifier == 1) {
			return "ObjectSomeValuesFrom";
		}else if(quantifier == 2) {
			return "ObjectAllValuesFrom";
		}else {
			return "";
		}
	}
}
