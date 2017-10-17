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
    
	public enum Quant { 
		NONE, EXISTS, FORALL; 
		public String toString() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	};
	
	private static Quant quantifier;
	
	public Quantifier(int i) {
		setQuantifier(i);
	}
	
	public void setQuantifier(int i) {
		if(i == 1) {
			Quantifier.quantifier = Quant.EXISTS;
		}else if(i == 2) {
			Quantifier.quantifier = Quant.FORALL;
		}else {
			Quantifier.quantifier = Quant.NONE;
		}
	}
	
	public static Quant getQuantifier() {
		return quantifier;
	}

	public String toString() {
		return quantifier.name();
	}

}
