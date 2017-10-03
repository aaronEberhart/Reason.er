package reason.er.Functions;

import reason.er.ReasonEr;
import reason.er.Objects.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Expression<T extends Predicate> extends Concept{
	
	private class ExpressionNode extends Expression{
		
		protected char operator;
		private boolean negated;
		protected Predicate leaf;
		protected Expression[] children;
				
		private ExpressionNode() {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		private ExpressionNode(Predicate p) {
			leaf = p;
			children = null;
			negated = p.isNegated();
		}
		
		private ExpressionNode(QuantifiedRole qr, ExpressionNode subTree) {
			leaf = qr;
			children = new Expression[1];
			children[0] = subTree;
		}
		
		private ExpressionNode(char o, ExpressionNode n1, ExpressionNode n2) {
			operator = o;
			children = new Expression[2];
			children[0] = n1;
			children[1] = n2;
		}

		private boolean isCompound() {
			if(children != null)
				return true;
			else
				return false;
		}
		
		public String toString() {
			if(children == null) {
				return leaf.toString();
			}else if(children.length == 1){
				return ((QuantifiedRole)leaf).getQuantifier().toString() + " " + ((QuantifiedRole)leaf).getRole().toString() + "." + children[0].toString();
			}else {
				String s = "( " + children[0].toString() + " " + operator + " " + children[1].toString() + " )";
				if(negated)
					s = "--" + s;
				return s;
			}
		}
	}
		
	protected ExpressionNode root;
	private Term<T> scope;
	private boolean complete;
	private int size;
	
	public Expression() {
	}
	
	public Expression(Predicate p) {
		scope = p.getScope();
		root = new ExpressionNode(p);
		negated = p.isNegated();
		complete = false;
		size = 1;
	}
	
	public void and(Predicate<T> p) {
		if(canJoin(p)) {
			root = new ExpressionNode('^', new ExpressionNode(p), root);
			size++;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " ^ " + root.toString());
			}
		}
		
	}
	public void or(Predicate<T> p) {
		if(canJoin(p)) {
			root = new ExpressionNode('v', new ExpressionNode(p), root);
			size++;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " v " + root.toString());
			}
		}
	}
	public void superClass(Predicate<T> p) {
		if(!p.isNegated() && canJoin(p)) {
			root = new ExpressionNode('c', new ExpressionNode(p), root);
			complete = true;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " c " + root.toString());
			}
		}
	}
	public void subClass(Predicate<T> p) {
		if(!root.isCompound() && canJoin(p)) {
			root = new ExpressionNode('c', root, new ExpressionNode(p));
			complete = true;
			size++;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + root.toString() + " c " + p.toString());
			}
		}
	}
	public void equivalent(Predicate<T> p) {
		if(!p.isNegated() && canJoin(p)) {
			boolean sub = root.isNegated();
			root = new ExpressionNode('=', new ExpressionNode(p), root);
			negated = p.isNegated();
			root.children[1].negated = sub;
			complete = true;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " = " + root.toString());
			}
		}
	}
	public void dot(Quantifier q, Role r, String name) {		
		if(QuantifiedRole.canQuantify(q, r, null, this)) {
			root = new ExpressionNode(new QuantifiedRole(q,r,this,name), root);
			size++;;
		}
	}
	
	public void negate() {
		if(complete)
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + "--" +  root.toString());
			}
		else if(root.negated) 
			root.negated = false;
		else
			root.negated = true;
	}
	
	public boolean canJoin(Predicate<T> p) {
//		System.out.println(scope);
//		System.out.println(p.getScope());
		return !p.isRole() && !complete && p.getScope().toString().equals(scope.toString());
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	@Override
	public Term<T> getScope() {
    	return this.scope;
    }
    
	public void setScope(Term<T> t) {
		scope = t;
	}
	
	public String toString() {
		return root.toString();
	}

	
	public int getSize() {
		return size;
	}

}
