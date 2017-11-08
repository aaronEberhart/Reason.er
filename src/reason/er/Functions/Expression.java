package reason.er.Functions;

import reason.er.ReasonEr;
import reason.er.Functions.*;
import reason.er.Objects.*;

@SuppressWarnings({"rawtypes", "unchecked","unlikely-arg-type"})
public class Expression<T extends Predicate> extends Concept{
	
	protected ExpressionNode root;
	protected boolean complete;
	
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
		scope = e.getScope();
		
		root = recursiveDeepCopy(e);	
		negated = e.negated;
		complete = true;
		size = e.getSize();
	}

	public Expression(Expression e) {
		root = (ExpressionNode)e;
		scope = e.scope;
		this.size = e.getSize();
		complete = true;
	}
	
	public ExpressionNode recursiveDeepCopy(ExpressionNode e) {
		try{
			ExpressionNode ex;
			
			if(e.isLeaf()) {
				ex = new ExpressionNode((Predicate)e.leaf.clone(e));
				
			}
			else if(e.children.length == 1) {
				ex = new ExpressionNode((QuantifiedRole)e.leaf.clone(e),recursiveDeepCopy((ExpressionNode)e.children[0]));
			}
			else {
				ex = new ExpressionNode(e.operator,recursiveDeepCopy((ExpressionNode)e.children[0]),recursiveDeepCopy((ExpressionNode)e.children[1]));
			}
			
			if(!ex.isLeaf() && e.negated) {
				ex.negated = true;
				ex.size++;
			}
			
			return ex;
			
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
		exp.size = exp.root.size;
		return exp;
	}
	
	public Expression<T> negate() {
		if(complete) {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + "--" +  this.toString());
			}
		}
		else if(root == null && ((ExpressionNode)this).children == null) {
			System.out.println(1);
			if(this.negated)
				size--;
			else
				size++;
			((ExpressionNode)this).leaf.negate();
			negated = ((ExpressionNode)this).leaf.isNegated();
		}
		else if(root == null && ((ExpressionNode)this).children.length == 1) {
			System.out.println(2);
			if(this.negated)
				size--;
			else
				size++;
			((ExpressionNode)this).leaf.negate();
			(((ExpressionNode)this).children[0]).negate();
			this.negated = this.negated?false:true;
		}
		else if(root != null && root.negated) {
			System.out.println(3);
			root.negated = false;
			if(root.isLeaf())
				root.leaf.negate();
			root.size = root.size - 1;
			size = size - 1;
		}else if (root != null) {
			System.out.println(4);
			root.negated = true;
			if(root.isLeaf())
				root.leaf.negate();
			root.size += 1;
			size+=1;
		}
		else {
			System.out.println(5);
			if(this.negated)
				size--;
			else
				size++;
			negated = negated?false:true;
		}
		
		return this;
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
