package reason.er.objects;

import java.util.ArrayList;

import reason.er.ReasonEr;
import reason.er.compositeObjects.*;;

/**
 * 
 * @author Aaron Eberhart
 *
 * @param T generic
 * @param U generic
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class QuantifiedRole<T,U> extends Role {

	/**
	 * Empty constructor.
	 */
	public QuantifiedRole() {}
	
	/**
	 * Manual QuantifiedRole constructor.
	 * 
	 * @param hasSign boolean
	 * @param conceptNegated boolean
	 * @param i integer
	 * @param t T
	 * @param u T
	 * @param name1 U
	 * @param name2 U
	 * @param name3 U
	 */
	public QuantifiedRole(boolean hasSign,boolean conceptNegated, int i, T t, T u, U name1, U name2, U name3) {
		
		this.terms = new ArrayList(3);
		
		Quantifier q = new Quantifier(i);
		Role<T,U> r = new Role<T,U>(false,t,u, name1);
		Concept<T,U> s = new Concept<T,U>(conceptNegated,u, name2);
		
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
	
	/**
	 * Partial QuantifiedRole constructor.
	 * @param q Quantifier
	 * @param r Role
	 * @param c Expression
	 * @param name U
	 */
	public QuantifiedRole(Quantifier q, Role<T,U> r, Expression<T,U> c, U name) {
		
		if(canQuantify(q,r,null,c)) {
			this.terms = new ArrayList(3);
			this.terms.add(r);
			this.terms.add(new Concept<T,U>(c.negated,new Term(r.getTerm(1)), name));
			this.terms.add(q);
			
			this.label = name;
			this.scope = r.getScope();
			this.negated = r.isNegated();
			this.size = 1;
		}
	}

	/**
	 * Partial QuantifiedRole constructor.
	 * @param q Quantifier
	 * @param r Role
	 * @param c Concept
	 * @param name1 U
	 * @param name2 U
	 * @param name3 U
	 */
	public QuantifiedRole(Quantifier q, Role<T,U> r, Concept<T,U> c, U name1, U name2, U name3) {
		
		if(canQuantify(q,r,c,null)){
			this.terms = new ArrayList(3);
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
	
	/**
	 * Tests to see if the Quantifier+Role is compatible with the Concept or Expression
	 * @param q Quantifier
	 * @param r Role
	 * @param c Concept
	 * @param d Expression
	 * @return boolean
	 */
	public static boolean canQuantify(Quantifier q, Role r,  Concept c, Expression d) {
		if(d == null && (!r.getTerm(1).getValue().equals(c.getScope()) || r.isNegated() )){// || //c.isNegated()) {)) {
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
	public String toString() {
		String s = "";
		if(negated)
			s += "--";
		return s + this.terms.get(2).toString() + " "  + this.terms.get(0).toString() + "." + this.terms.get(1).toString();
	}

	/**
	 * Returns true.
	 * 
	 * @return true
	 */
	public boolean isExpression() {
		if(this.terms.get(1) == null)
			return true;
		return false;
	}
	
	/**
	 * Gets the Role of the QuantifiedRole
	 * @return Role
	 */
	public Role getRole() {
		return (Role)this.terms.get(0);
	}
	
	/**
	 * Gets the Concept of the QuantifiedRole.
	 * @return Concept
	 */
	public Concept getConcept() {
		return (Concept)this.terms.get(1);
	}

	/**
	 * Gets the Quantifier of the QuantifiedRole.
	 * @return Quantifier
	 */
	public Quantifier getQuantifier() {
		return ((Quantifier)this.terms.get(2));
	}

	/**
	 * Overrides superclass Role.
	 * @return false
	 */
	@Override
	public boolean isRole() {
		return false;
	}
	
	@Override
	public Predicate<T,U> clone(Expression e) {
		if(this.isExpression()) {
			return new ExpressionNode<T,U>(new QuantifiedRole<T,U>(new Quantifier(getQuantifier().getInteger()),(Role<T,U>)terms.get(0),((ExpressionNode)e).getChild(0),(U)label),(ExpressionNode)((ExpressionNode)e).getChild(0));
		}
		QuantifiedRole qr = new QuantifiedRole(negated,getConcept().negated,this.getQuantifier().getInteger(),(T)((Role)terms.get(0)).getTerm(0).getValue(),(U)((Role)terms.get(0)).getTerm(1).getValue(),
				(long)((Role)terms.get(0)).label,(long)((Concept)terms.get(1)).label,(long)label);
		qr.setSize(this.getSize());
		return qr;
	}
}
