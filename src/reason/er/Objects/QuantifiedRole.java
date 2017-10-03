package reason.er.Objects;

import java.util.ArrayList;

import reason.er.ReasonEr;
import reason.er.Functions.Expression;

@SuppressWarnings({"rawtypes","unchecked"})
public class QuantifiedRole<T,U> extends Role {

	public QuantifiedRole() {}
	
	public QuantifiedRole(boolean hasSign, int i, T t, U u, String name1, String name2, String name3) {
		
		this.terms = new ArrayList();
		
		Quantifier q = new Quantifier(i);
		Role<T,U> r = new Role<T,U>(false,t,u, name1);
		Concept<U> s = new Concept<U>(false,u, name2);
    	
		this.terms.add(r);
		this.terms.add(s);
		this.terms.add(q);
		
		this.label = name3;
    	this.scope = r.getScope();
    	this.negated = hasSign;
	}
	
	public QuantifiedRole(Quantifier q, Role<T,U> r, Expression c, String name) {
		
		if(canQuantify(q,r,null,c)) {
			this.terms = new ArrayList();
			this.terms.add(r);
			this.terms.add(c);
			this.terms.add(q);
			
			this.label = name;
			this.scope = r.getScope();
			this.negated = r.isNegated();
		}
	}

	public QuantifiedRole(Quantifier q, Role<T,U> r, Concept<U> c, String name1, String name2, String name3) {
		
		if(canQuantify(q,r,c,null)){
			this.terms = new ArrayList();
			r.setLabel(name1);
			c.setLabel(name2);
			
			this.terms.add(r);
			this.terms.add(c);
			this.terms.add(q);
			
			this.label = name3;
			this.scope = r.getScope();
			this.negated = r.isNegated();
		}
	}
	
	public static boolean canQuantify(Quantifier q, Role r,  Concept c, Expression d) {
		if(d == null && (!r.getTerm(1).getValue().equals(c.getScope().getValue()) || (r.isNegated() || c.isNegated()))) {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + q.toString() + " " + r.toString() + "." + c.toString());
				return false;
			}
		}
		else if(c == null && !r.getTerm(1).getValue().equals(d.getScope().getValue()) && !d.isComplete() && !r.isNegated() && !d.isNegated()){
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + q.toString() + " " + r.toString() + "." + d.toString());
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean isRole() {
		return false;
	}

	public String toString() {
		return this.terms.get(2).toString() + " "  + this.terms.get(0).toString() + "." + this.terms.get(1).toString();
	}

	public Role getRole() {
		return (Role)this.terms.get(0);
	}
	
	public Concept getConcept() {
		return (Concept)this.terms.get(1);
	}

	public Quantifier getQuantifier() {
		return (Quantifier)this.terms.get(2);
	}

}
