package reason.er.Functions;

import reason.er.ReasonEr;
import reason.er.Objects.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Expression<T extends Predicate> extends Concept{
	
	private class ExpressionNode extends Expression{
		
		protected char operator;
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
			size = p.getSize();
		}
		
		private ExpressionNode(QuantifiedRole qr, ExpressionNode subTree) {
			leaf = qr;
			children = new Expression[1];
			children[0] = subTree;
			size = subTree.getSize();
		}
		
		private ExpressionNode(char o, ExpressionNode n1, ExpressionNode n2) {
			operator = o;
			children = new Expression[2];
			children[0] = n1;
			children[1] = n2;
			size = n1.getSize() + n2.getSize() + 1;
		}

		private boolean isLeaf() {
			if(children == null)
				return true;
			else
				return false;
		}
		
		public String toString() {
			if(children == null) {
				return leaf.toString();
			}else if(children.length == 1){
				String s = ((QuantifiedRole)leaf).getQuantifier().toString() + " " + ((QuantifiedRole)leaf).getRole().toString() + "." + children[0].toString();
				if(negated)
					s = "--" + s;
				return s;
			}else {
				String s = "( " + children[0].toString() + " " + operator + " " + children[1].toString() + " )";
				if(negated)
					s = "--" + s;
				return s;
			}
		}
	}
		
	protected ExpressionNode root;
	private boolean complete;
	
	public Expression() {
	}
	
	public Expression(Predicate p) {
		scope = p.getScope();
		root = new ExpressionNode(p);
		negated = p.isNegated();
		complete = false;
		size = p.getSize();
	}
	
	public Expression<T> and(Predicate<T> p) {
		if(canJoin(p)) {
			root = new ExpressionNode('^', new ExpressionNode(p), root);
			size= this.getSize() + p.getSize() + 1;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " ^ " + root.toString());
			}
		}
		return this;
	}
	public Expression<T> or(Predicate<T> p) {
		if(canJoin(p)) {
			root = new ExpressionNode('v', new ExpressionNode(p), root);
			size= this.getSize() + p.getSize() + 1;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " v " + root.toString());
			}
		}
		return this;
	}
	public Expression<T> superClass(Predicate<T> p) {
		if(!p.isNegated() && canJoin(p)) {
			root = new ExpressionNode('c', new ExpressionNode(p), root);
			complete = true;
			size= this.getSize() + p.getSize() + 1;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " c " + root.toString());
			}
		}
		return this;
	}
	public Expression<T> subClass(Predicate<T> p) {
		if(root.isLeaf() && canJoin(p)) {
			root = new ExpressionNode('c', root, new ExpressionNode(p));
			complete = true;
			size= this.getSize() + p.getSize() + 1;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + root.toString() + " c " + p.toString());
			}
		}
		return this;
	}
	public Expression<T> equivalent(Predicate<T> p) {
		if(!p.isNegated() && canJoin(p)) {
			root = new ExpressionNode('=', new ExpressionNode(p), root);
			complete = true;
			size= this.getSize() + p.getSize() + 1;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " = " + root.toString());
			}
		}
		return this;
	}
	public Expression<T> dot(Quantifier q, Role r, long name) {		
		if(QuantifiedRole.canQuantify(q, r, null, this)) {
			size += 1;
			root = new ExpressionNode(new QuantifiedRole(q,r,this,name),root);
			scope = r.getScope();
		}
		else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + q.toString() + " " + r.toString() + "." +  root.toString());
			}
		}
		return this;
	}
	
	public Expression<T> negate() {
		if(complete)
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + "--" +  root.toString());
			}
		else if(root == null && ((ExpressionNode)this).children == null) {
			if(this.negated)
				size--;
			else
				size++;
			((ExpressionNode)this).leaf.negate();
		}
		else if(root == null && ((ExpressionNode)this).children.length == 1) {
			if(this.negated)
				size--;
			else
				size++;
			((ExpressionNode)this).leaf.negate();
			this.negated = this.negated?false:true;
		}
		else if(root.negated) {
			root.negated = false;
			if(root.isLeaf())
				root.leaf.negate();
			size = size - 1;
		}else {
			root.negated = true;
			if(root.isLeaf())
				root.leaf.negate();
			size+=1;
		}
		
		return this;
	}
	
	public Expression<T> deMorgan(){
		if(root.operator == 'v' && root.negated) {
			root = new ExpressionNode('^',new ExpressionNode(root.children[0].negate()),new ExpressionNode(root.children[1].negate()));
			return this;
		}
		else if(root.operator == '^' && root.negated) {
			root = new ExpressionNode('v',new ExpressionNode((Expression)root.children[0].negate()),new ExpressionNode((Expression)root.children[1].negate()));
			return this;
		}else
			return this;
	}

	@SuppressWarnings("unlikely-arg-type")
	public Expression<T> negateQuantifier(){
		if(root.negated && root.isLeaf() && root.isRole()) {
			if(((QuantifiedRole)root.leaf).getQuantifier().equals(Quantifier.getQuantifier().EXISTS)) {
				root = new ExpressionNode(new QuantifiedRole(new Quantifier(2),((QuantifiedRole)root.leaf).getRole(),root.children[0],((QuantifiedRole)root.leaf).getScope()), new ExpressionNode(root.children[0].negate()));
			}
			else if(((QuantifiedRole)root.leaf).getQuantifier().equals(Quantifier.getQuantifier().FORALL)) {
				root = new ExpressionNode(new QuantifiedRole(new Quantifier(1),((QuantifiedRole)root.leaf).getRole(),root.children[0],((QuantifiedRole)root.leaf).getScope()), new ExpressionNode(root.children[0].negate()));
			}
			return this;
		}
		else	
			return this;
	}


	public boolean canJoin(Predicate<T> p) {
		return !p.isRole() && !complete && p.getScope() == scope;
	}
	
	public boolean isComplete() {
		return complete;
	}
    
	public void setScope(long t) {
		scope = t;
	}
	
	public String toString() {
		return root.toString();// + " Size: " + this.getSize();
	}



}
