/**
 *
 *
 */
package reason.er.Objects;

/**
 *
 * @author aaron
 */
public class Quantifier {
    
	private int quantifier;
	
	public Quantifier(int i) {
		quantifier = i;
	}
	
	public void setQuantifier(int i) {
		quantifier = i;
	}
	
	public int getInteger() {
		return quantifier;
	}
	
	public void flipQuantifier() {
		if(quantifier == 1) {
			quantifier = 2;
		}
		else if(quantifier == 2){
			quantifier = 1;
		}
	}

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
