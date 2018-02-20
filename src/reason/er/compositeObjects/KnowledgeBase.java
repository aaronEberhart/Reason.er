package reason.er.compositeObjects;

import java.util.ArrayList;

import reason.er.objects.*;

/**
 * Container for an ABox and a TBox. Has methods that initialize or normalize both.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KnowledgeBase<T,U> extends Expression<T,U>{
	
	/**
	 * ABox for the KnowledgeBase.
	 */
	private ABox<T,U> abox;
	/**
	 * TBox for the KnowledgeBase.
	 */
	private TBox<T,U> tbox;
	
	/**
	 * Make a new randomized KnowledgeBase of the size specified.
	 * @param aboxSize integer
	 * @param tboxSize integer
	 */
	public KnowledgeBase(int aboxSize, int tboxSize) {
		
		tbox = new TBox<T,U>(tboxSize);
		abox = new ABox<T,U>(aboxSize);
	}
	
	/**
	 * Make a KnowledgeBase out of the ABox and TBox.
	 * @param a ABox
	 * @param t TBox
	 */
	public KnowledgeBase(ABox<T,U> a, TBox<T,U> t) {
		tbox = t;
		abox = a;
	}
	
	/**
	 * Make a hard-coded KnowledgeBase.
	 */
	public KnowledgeBase() {
		makeTestKnowledgeBase();
	}
	
	/**
	 * Normalize the KnowledgeBase.
	 */
	public void normalize() {
		tbox.normalizeExpressions();
		abox.normalizeExpressions();
	}
	
	/**
	 * Getter for the TBox.
	 * @return tbox
	 */
	public TBox getTBox() {
		return tbox;
	}
	
	/**
	 * Getter for the ABox.
	 * @return abox
	 */
	public ABox getABox() {
		return abox;
	}
	
	@Override
	public String toString() {
		return "K = ( ABox, TBox )\n\n" + abox.toString() + "\n\n" + tbox.toString() + "\n";
	}
	
	/**
	 * Create a sample hard-coded KnowledgeBase.
	 */
	private void makeTestKnowledgeBase() {
		
		abox = new ABox(new ArrayList<T>());
		
		abox.addManually(new ExpressionNode(new Role(false,(long)0,(long)1,(long)17)));
		
		tbox = new TBox(new ArrayList<U>());
		
		tbox.addManually(
				(ExpressionNode)new ExpressionNode(new QuantifiedRole(false,true,1,Term.getVarIndex('x'),Term.getVarIndex('w'),Predicate.getLabelIndex('R'),Predicate.getLabelIndex('A'),Predicate.getLabelIndex('T')))
				.negate()
				.dot(new Quantifier(2),new Role(false,Term.getVarIndex('y'),Term.getVarIndex('x'),Predicate.getLabelIndex('S')),Predicate.getLabelIndex('S'))
				.and(new Concept(false,Term.getVarIndex('y'),Predicate.getLabelIndex('B')))
				.negate()
				.equivalent(new Concept(false,Term.getVarIndex('y'),Predicate.getLabelIndex('C'))));
		
		tbox.addManually(
				(ExpressionNode)new ExpressionNode(new QuantifiedRole(false,false,2,Term.getVarIndex('x'),Term.getVarIndex('w'),Predicate.getLabelIndex('R'),Predicate.getLabelIndex('A'),Predicate.getLabelIndex('T')))
				.negate()
				.dot(new Quantifier(1),new Role(false,Term.getVarIndex('y'),Term.getVarIndex('x'),Predicate.getLabelIndex('S')),Predicate.getLabelIndex('S'))
				.and(new Concept(false,Term.getVarIndex('y'),Predicate.getLabelIndex('B')))
				//.negate()
				.superClass(new Concept(false,Term.getVarIndex('y'),Predicate.getLabelIndex('C'))));
		
	}
}
