
package reason.er.objects;

import java.util.ArrayList;

import reason.er.ReasonEr;
import reason.er.compositeObjects.*;

/**
 * The Concept class is intended to model the behavior of ALC Concepts. It is
 * the most basic Predicate and as such has only one Term, which is identical 
 * with its scope.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Concept<T,U> extends Predicate<T,U> {
	
	/**
	 * Empty constructor.
	 */
	public Concept() {}
	
	/**
	 * Manual Concept constructor.
	 * @param hasSign boolean
	 * @param t &lt;T&gt;
	 * @param name &lt;U&gt;
	 */
	public Concept(boolean hasSign, T t, U name){
		
		this.terms = new ArrayList(1);
		Term<T> s = new Term<T>(t);
		this.terms.add(s);
		
		this.label = name;
		this.scope = s.getValue();
		this.negated = hasSign;
		this.size = 1;
		if(negated)
			size++;
	}
	
	/**
	 * Partial Concept constructor.
	 * @param hasSign boolean
	 * @param t Term&lt;T&gt;
	 * @param name &lt;U&gt;
	 */
	public Concept(boolean hasSign, Term<T> t, U name){
		
		this.terms = new ArrayList(1);
		this.terms.add(t);
		
		this.label = name;
		this.scope = (T)t.getValue();
		this.negated = hasSign;
		this.size = 1;
		if(negated)
			size++;
	}

	/**
	 * Gets the Term.
	 * @return Term&lt;T&gt;
	 */
	public Term<T> getTerm() {
		return (Term<T>) this.terms.get(0);
	}
	
	@Override
	public String toString() {
		String s = negated ? "--" : "";
		return ((long)label == ReasonEr.conceptNames ? (negated?"BOTTOM":"TOP") : s + makeLabel() + "(" + this.getTerm().toString() + ")");
	}
	
	@Override
	public String toDLString() {
		String s = negated ? "--" : "";
		return ((long)label == ReasonEr.conceptNames ? (negated?"BOTTOM":"TOP") : s + makeLabel());
	}

	@Override
	public String toFSString(int tab) {
		String indent = new String(new char[tab]).replace("\0", "\t");
		//System.out.println(indent+"*");
		return indent + ((long)label == ReasonEr.conceptNames ? (negated?"owl:Nothing":"owl:Thing") :(negated ? "ObjectComplementOf( :" + makeLabel() + " )": ":" + makeLabel() ));
	}
	
	@Override
	public Predicate<T,U> clone(Expression<T,U> e) {
		return new Concept<T,U>(e.negated, this.getTerm().getValue(), label);
	}

}
