package reason.er.Functions;

import java.util.ArrayList;

import reason.er.Objects.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class KnowledgeBase<T extends Expression<T>, U extends Expression<U>> {
	
	private ABox<T> abox;
	private TBox<U> tbox;
	
	public KnowledgeBase(int aboxSize, int tboxSize) {
		
		tbox = new TBox<U>(tboxSize);
		abox = new ABox<T>(aboxSize);
	}
	
	public KnowledgeBase(ABox<T> a, TBox<U> t) {
		tbox = t;
		abox = a;
	}
	
	public void normalize() {
		tbox.normalizeExpressions();
		abox.normalizeExpressions();
	}
	
	@Override
	public String toString() {
		return "K = ( ABox, TBox )\n\n" + abox.toString() + "\n\n" + tbox.toString() + "\n";
	}
	
	public static <T extends Expression<T>,U extends Expression <U>> KnowledgeBase<T,U> makeTestKnowledgeBase() {
    	
		ABox<T> abox = new ABox(new ArrayList<T>());
    	
    	abox.addManually(new Expression(new Role(false,(long)0,(long)1,(long)17)));
    	
		TBox<U> tbox = new TBox(new ArrayList<U>());
    	
    	tbox.addManually(
    			new Expression(new QuantifiedRole(false,1,Term.getVarIndex('x'),Term.getVarIndex('w'),Predicate.getLabelIndex('R'),Predicate.getLabelIndex('A'),Predicate.getLabelIndex('T')))
    			.negate()
    			.dot(new Quantifier(2),new Role(false,Term.getVarIndex('y'),Term.getVarIndex('x'),Predicate.getLabelIndex('S')),Predicate.getLabelIndex('S'))
    			.and(new Concept(false,Term.getVarIndex('y'),Predicate.getLabelIndex('B')))
    			.negate()
    			.equivalent(new Concept(false,Term.getVarIndex('y'),Predicate.getLabelIndex('C'))));
    	
    	tbox.addManually(
    			new Expression(new QuantifiedRole(false,2,Term.getVarIndex('x'),Term.getVarIndex('w'),Predicate.getLabelIndex('R'),Predicate.getLabelIndex('A'),Predicate.getLabelIndex('T')))
    			.negate()
    			.dot(new Quantifier(1),new Role(false,Term.getVarIndex('y'),Term.getVarIndex('x'),Predicate.getLabelIndex('S')),Predicate.getLabelIndex('S'))
    			.and(new Concept(false,Term.getVarIndex('y'),Predicate.getLabelIndex('B')))
    			.negate()
    			.superClass(new Concept(false,Term.getVarIndex('y'),Predicate.getLabelIndex('C'))));
    	
    	return new KnowledgeBase(abox,tbox);
    }
}
