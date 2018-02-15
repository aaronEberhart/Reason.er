package reason.er.objects;

/**
 *
 * @author Aaron Eberhart
 */
public class Quantifier {
    
	/**
	 * Integer id.
	 */
	private int quantifier;
	
	/**
	 * Set the quantifier.
	 * @param i integer
	 */
	public Quantifier(int i) {
		quantifier = i;
	}
	
	/**
	 * Set the quantifier.
	 * @param i integer
	 */
	public void setQuantifier(int i) {
		quantifier = i;
	}
	
	/**
	 * Returns the value of the id.
	 * @return integer
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
			return "EXISTS";
		}else if(quantifier == 2) {
			return "FORALL";
		}else {
			return "NONE";
		}
	}

}
