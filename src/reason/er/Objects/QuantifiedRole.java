package reason.er.Objects;

import java.util.ArrayList;

import reason.er.ReasonEr;
import reason.er.Functions.*;;

@SuppressWarnings({"rawtypes","unchecked"})
public class QuantifiedRole<T,U> extends Role {

	public QuantifiedRole() {}
	
	public QuantifiedRole(boolean hasSign, int i, T t, U u, long name1, long name2, long name3) {
		
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
    	this.size = s.size + 1;
    	if(negated)
    		size++;
	}
	
	public QuantifiedRole(Quantifier q, Role<T,U> r, Expression c, long name) {
		
		if(canQuantify(q,r,null,c)) {
			this.terms = new ArrayList();
			this.terms.add(r);
			this.terms.add(new Concept<U>(false,new Term(r.getTerm(1)), name));
			this.terms.add(q);
			
			this.label = name;
			this.scope = r.getScope();
			this.negated = r.isNegated();
			this.size = 1;
		}
	}

	public QuantifiedRole(Quantifier q, Role<T,U> r, Concept<U> c, long name1, long name2, long name3) {
		
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
			this.size = c.size + 1;
			if(negated)
				size++;
		}
	}
	
	public static boolean canQuantify(Quantifier q, Role r,  Concept c, Expression d) {
		if(d == null && (!r.getTerm(1).getValue().equals(c.getScope()) || (r.isNegated() || c.isNegated()))) {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + q.toString() + " " + r.toString() + "." + c.toString());
				return false;
			}
		}
		else if(c == null && !r.getTerm(1).getValue().equals(d.getScope()) && !d.isComplete() && !r.isNegated() && !d.isNegated()){
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
		String s = "";
    	if(negated)
    		s += "--";
		return s + this.terms.get(2).toString() + " "  + this.terms.get(0).toString() + "." + this.terms.get(1).toString();
	}

	public boolean isExpression() {
		if(this.terms.get(1) == null)
			return true;
		return false;
	}
	
	public Role getRole() {
		return (Role)this.terms.get(0);
	}
	
	public Concept getConcept() {
		return (Concept)this.terms.get(1);
	}

	public Quantifier getQuantifier() {
		return ((Quantifier)this.terms.get(2));
	}

	
	public Predicate clone(Expression e) {
		if(this.isExpression()) {
			return new ExpressionNode(new QuantifiedRole(new Quantifier(getQuantifier().getInteger()),(Role)terms.get(0),((ExpressionNode)e).getChild(0),label),(ExpressionNode)((ExpressionNode)e).getChild(0));
		}
		QuantifiedRole qr = new QuantifiedRole(negated,this.getQuantifier().getInteger(),(T)((Role)terms.get(0)).getTerm(0).getValue(),(U)((Role)terms.get(0)).getTerm(1).getValue(),
				((Role)terms.get(0)).label,((Concept)terms.get(1)).label,label);
		qr.setSize(this.getSize());
		return qr;
	}
}
