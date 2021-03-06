package reason.er.compositeObjects;

import java.util.ArrayList;

import reason.er.ReasonEr;
import reason.er.objects.*;

/**
 * Container for an ABox and a TBox. Has methods that initialize or normalize both.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KnowledgeBase<T,U>{
	
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
	 * @param aboxSize int
	 * @param tboxSize int
	 */
	public KnowledgeBase(int aboxSize, int tboxSize) {
		
		tbox = new TBox<T,U>(tboxSize);
		abox = new ABox<T,U>(aboxSize);
	}
	
	/**
	 * Make a KnowledgeBase out of the ABox and TBox.
	 * @param a ABox&lt;T,U&gt;
	 * @param t TBox&lt;T,U&gt;
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
	 * @return TBox&lt;T,U&gt;
	 */
	public TBox getTBox() {
		return tbox;
	}
	
	/**
	 * Getter for the ABox.
	 * @return ABox&lt;T,U&gt;
	 */
	public ABox getABox() {
		return abox;
	}
	
	@Override
	public String toString() {
		String s = "Individuals:\t";
		for(int i = -1; i > -1*ReasonEr.individuals - 1; i--) {
			s = s + Term.makeVariable(i);
			if(i != -1*ReasonEr.individuals)
				s = s + ", ";
		}
		s = s + "\nClass Names:\t";
		for(int i = 0; i < ReasonEr.conceptNames + 1; i++) {
			s = s + (i < ReasonEr.conceptNames ? Predicate.makeLabel(i) + ", " :  "TOP / BOTTOM" );
		}
		s = s + "\nRole Names: \t";
		for(int i = -1; i >= -1 * ReasonEr.roleNames; i--) {
			s = s + Predicate.makeLabel(i) + (i == -1 * ReasonEr.roleNames  ? "" : ", ");
		}
		return s + "\n\nK = ( ABox, TBox )\n\n" + abox.toString() + "\n\n" + tbox.toString() + "\n";
	}
	
	/**
	 * Returns a functional syntax string representation that can be written to a file.
	 * @param IRI String
	 * @return String
	 */
	public String toFSString(String IRI) {
		String s = "Prefix(:="+IRI+")\nPrefix(owl:=<http://www.w3.org/2002/07/owl#>)\nOntology( "+IRI+"\n\n";
		for(int i = -1; i > -1*ReasonEr.individuals - 1; i--) {
			s = s + "Declaration( NamedIndividual( :" + Term.makeVariable(i) + " ) )\n";
		}
		for(int i = 0; i < ReasonEr.conceptNames; i++) {
			s = s + "Declaration( Class( :" +  Predicate.makeLabel(i) + " ) )\n";
		}
		for(int i = -1; i >= -1 * ReasonEr.roleNames; i--) {
			s = s + "Declaration( ObjectProperty( :" + Predicate.makeLabel(i)   + " ) )\n";
		}
		s=s+"\n";
		return s + abox.toFSString(0) + "\n" + tbox.toFSString(0) + "\n\n )";
	}
	
	/**
	 * Returns a description logic string representation that can be written to a file.
	 * @return String
	 */
	public String toDLString() {
		String s = "Individuals:\t";
		for(int i = -1; i > -1*ReasonEr.individuals - 1; i--) {
			s = s + Term.makeVariable(i);
			if(i != -1*ReasonEr.individuals)
				s = s + ", ";
		}
		s = s + "\nClass Names:\t";
		for(int i = 0; i < ReasonEr.conceptNames + 1; i++) {
			s = s + (i < ReasonEr.conceptNames ? Predicate.makeLabel(i) + ", " :  "TOP / BOTTOM" );
		}
		s = s + "\nRole Names: \t";
		for(int i = -1; i >= -1 * ReasonEr.roleNames; i--) {
			s = s + Predicate.makeLabel(i) + (i == -1 * ReasonEr.roleNames  ? "" : ", ");
		}
		return s + "\n\nK = ( ABox, TBox )\n\n" + abox.toDLString() + "\n\n" + tbox.toDLString() + "\n";
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
