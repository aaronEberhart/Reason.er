package reason.er.Functions;

import reason.er.ReasonEr;
import reason.er.Functions.*;
import reason.er.Objects.*;

@SuppressWarnings({"rawtypes", "unchecked","unlikely-arg-type"})
public class Expression<T extends Predicate> extends Concept{
	
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
	
	public Expression(ExpressionNode e){
		
		if(e.isLeaf()) 
			scope = e.getScope();
		
		root = recursiveDeepCopy(e);	
		negated = e.negated;
		complete = true;
		size = e.getSize();
	}

	public Expression(Expression e) {
		root = (ExpressionNode)e;
		complete = true;
	}
	
	public ExpressionNode recursiveDeepCopy(ExpressionNode e) {
		try{
			if(e.isLeaf())
				return new ExpressionNode((Predicate)e.leaf.clone(e));
			else if(e.children.length == 1) {
				ExpressionNode ex = new ExpressionNode((QuantifiedRole)e.leaf.clone(e),recursiveDeepCopy((ExpressionNode)e.children[0]));
				return ex;
			}
			else {
				ExpressionNode ex = new ExpressionNode(e.operator,recursiveDeepCopy((ExpressionNode)e.children[0]),recursiveDeepCopy((ExpressionNode)e.children[1]));
				if(e.negated)
					ex.negated = true;
				return ex;
			}
		}catch(Exception e1) {
			e1.printStackTrace();
			return null;
		}

	}
	public Expression deepCopy(Expression e) {
		if(root == null) {
			try {
				return (Expression)e.clone();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
		}
		Expression exp = new Expression(recursiveDeepCopy(e.root));
		return exp;
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
		
		if(canJoin(p) && !((ExpressionNode)this).isLeaf() && !((ExpressionNode)p).isLeaf()) {
			root = new ExpressionNode('v', root, new ExpressionNode(p));
			root.leaf = null;
			size= this.getSize() + p.getSize() + 1;
			}
		else if(canJoin(p) && ((ExpressionNode)this).isLeaf()) {
			root = null;
			
			Expression[] c = new Expression[2];
			ExpressionNode d = (ExpressionNode)this;
			
			c[0] = recursiveDeepCopy(d);
			if(((ExpressionNode)this).leaf.isNegated())
				((ExpressionNode)c[0]).leaf.negate();
			c[1] = (Expression)p;
			((ExpressionNode)this).children = c;

			((ExpressionNode)this).operator = 'v';
			((ExpressionNode)this).leaf = null;
			size = this.getSize() + p.getSize() + 1;
			}
		else if(canJoin(p) && ((ExpressionNode)p).isLeaf()) {
			root = null;
			
			Expression[] c = new Expression[2];
			c[0] = (Expression)p;
			c[1] = recursiveDeepCopy((ExpressionNode)this);

			((ExpressionNode)this).operator = 'v';
			((ExpressionNode)this).leaf = null;
			((ExpressionNode)this).children = c;
			size = this.getSize() + p.getSize() + 1;
		}
		else {
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
		else if(root != null && root.negated) {
			root.negated = false;
			if(root.isLeaf())
				root.leaf.negate();
			size = size - 1;
		}else if (root != null) {
			root.negated = true;
			if(root.isLeaf())
				root.leaf.negate();
			size+=1;
		}else {
			if(this.negated)
				size--;
			else
				size++;
			negated = negated?false:true;
		}
		
		return this;
	}
	
	public Expression<T> deMorgan(){
		if(root.operator == 'v') {
			root = new ExpressionNode('^',new ExpressionNode(root.children[0].negate()),new ExpressionNode(root.children[1].negate()));
			return this;
		}
		else if(root.operator == '^') {
			root = new ExpressionNode('v',new ExpressionNode((Expression)root.children[0].negate()),new ExpressionNode((Expression)root.children[1].negate()));
			return this;
		}else
			return this;
	}

	public Expression<T> negateQuantifier(){
		if(((QuantifiedRole)root.leaf).getQuantifier().getInteger() == 1) {
			root = new ExpressionNode(new QuantifiedRole(new Quantifier(2),((QuantifiedRole)root.leaf).getRole(),root.children[0],((QuantifiedRole)root.leaf).getScope()), new ExpressionNode(root.children[0].negate()));
		}			
		else if(((QuantifiedRole)root.leaf).getQuantifier().getInteger() == 2) {
			root = new ExpressionNode(new QuantifiedRole(new Quantifier(1),((QuantifiedRole)root.leaf).getRole(),root.children[0],((QuantifiedRole)root.leaf).getScope()), new ExpressionNode(root.children[0].negate()));
		}
		return this;
	}

	public Expression<T> normalize(){
		if(this.size <= 2)
			return this;
		else if((root.operator == 'v' || root.operator == '^') && root.negated) {
			this.deMorgan();
			this.root.children[0].normalize();
			this.root.children[1].normalize();
			return this;
		}
		else if(root.negated && root.isLeaf() && root.isRole()) {
			this.negateQuantifier();
			this.root.children[0].normalize();
			return this;
		}
		else {
			this.root.children[0].normalize();
			this.root.children[1].normalize();
			return this;
		}
	}
	
	public boolean canJoin(Predicate<T> p) {
		return !p.isRole() && !complete && p.getScope() == scope;
	}
	
	public boolean isComplete() {
		return complete;
	}
    
	public char getOperator() {
		if(complete)
			return root.operator;
		else return 0;
	}
	
	public void setScope(long t) {
		scope = t;
	}
	
	public String toString() {
		//String s = root.toString();
		return root.toString();// + " Size: " + this.getSize();
	}

	public Expression getChild(int i){
		return root.children[i];
	}
	
	public ExpressionNode getRoot() {
		return this.root;
	}

}
