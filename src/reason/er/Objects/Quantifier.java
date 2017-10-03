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
    
	private enum Quant { 
		NONE, EXISTS, FORALL; 
		public String toString() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	};
	
	private Quant quantifier;
	
	public Quantifier(int i) {
		setQuantifier(i);
	}
	
	public void setQuantifier(int i) {
		if(i == 1) {
			this.quantifier = Quant.EXISTS;
		}else if(i == 2) {
			this.quantifier = Quant.FORALL;
		}else {
			this.quantifier = Quant.NONE;
		}
	}

	public String toString() {
		return quantifier.name();
	}

}
