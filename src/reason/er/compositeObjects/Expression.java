package reason.er.compositeObjects;

import reason.er.ReasonEr;
import reason.er.objects.*;

@SuppressWarnings({ "unchecked"})
public class Expression<T extends Predicate<T>> extends Concept<T>{
	
	protected ExpressionNode<T> root;
	protected boolean complete, normalized;
	
	public Expression() {
	}
	
	public Expression(Predicate<T> p) {
		scope = p.getScope();
		root = new ExpressionNode<T>(p);
		negated = p.isNegated();
		complete = false;
		normalized = false;
		size = p.getSize();
	}
	
	public Expression(ExpressionNode<T> e){
		scope = e.getScope();
		root = recursiveDeepCopy(e);	
		negated = e.negated;
		complete = true;
		normalized = false;
		size = e.getSize();
	}

	public Expression(Expression<T> e) {
		root = (ExpressionNode<T>)e;
		scope = e.scope;
		this.size = e.getSize();
		complete = true;
		normalized = false;
	}
	
	public ExpressionNode<T> recursiveDeepCopy(ExpressionNode<T> e) {
		try{
			ExpressionNode<T> ex;
			
			if(e.isLeaf()) {
				ex = new ExpressionNode<T>((Predicate<T>)e.leaf.clone(e));
				
			}
			else if(e.getChildren().length == 1) {
				ex = new ExpressionNode<T>((QuantifiedRole<T,T>)e.leaf.clone(e),recursiveDeepCopy((ExpressionNode<T>)e.getChildren()[0]));
			}
			else {
				ex = new ExpressionNode<T>(e.operator,recursiveDeepCopy((ExpressionNode<T>)e.getChildren()[0]),recursiveDeepCopy((ExpressionNode<T>)e.getChildren()[1]));
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
	public Expression<T> deepCopy(Expression<T> e) {
		if(root == null) {
			try {
				return (Expression<T>)e.clone();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
		}
		Expression<T> exp = new Expression<T>(recursiveDeepCopy(e.root));
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
		else if(root == null && ((ExpressionNode<T>)this).getChildren() == null) {
			System.out.println(1);
			if(this.negated)
				size--;
			else
				size++;
			((ExpressionNode<T>)this).leaf.negate();
			negated = ((ExpressionNode<T>)this).leaf.isNegated();
		}
		else if(root == null && ((ExpressionNode<T>)this).getChildren().length == 1) {
			System.out.println(2);
			if(this.negated)
				size--;
			else
				size++;
			((ExpressionNode<T>)this).leaf.negate();
			(((ExpressionNode<T>)this).getChildren()[0]).negate();
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
	
	public void setOperator(char c) {
		root.operator = c;
	}

	public void setScope(long t) {
		scope = t;
	}
	
	public void setNormal(boolean b) {
		normalized = b;
	}
	
	public boolean isNormal() {
		return normalized;
	}
	
	public String toString() {
		//String s = root.toString();
		return root.toString() + " Size: " + this.getSize();
	}

	public Expression<T> getChild(int i){
		return root.getChildren()[i];
	}
	
	public ExpressionNode<T> getRoot() {
		return this.root;
	}

}
