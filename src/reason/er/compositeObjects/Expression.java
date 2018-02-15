package reason.er.compositeObjects;

import reason.er.ReasonEr;
import reason.er.objects.*;

@SuppressWarnings({ "unchecked"})
public class Expression<T,U> extends Predicate<T,U>{

	protected ExpressionNode<T,U> root;
	protected boolean complete, normalized;
	
	public Expression() {
	}
	
	public Expression(Predicate<T,U> p) {
		scope = p.getScope();
		root = new ExpressionNode<T,U>(p);
		negated = p.isNegated();
		complete = false;
		normalized = false;
		size = p.getSize();
	}
	
	public Expression(ExpressionNode<T,U> e){
		scope = e.getScope();
		root = recursiveDeepCopy(e);	
		negated = e.negated;
		complete = true;
		normalized = false;
		size = e.getSize();
	}

	public Expression(Expression<T,U> e) {
		root = (ExpressionNode<T,U>)e;
		scope = e.scope;
		this.size = e.getSize();
		complete = true;
		normalized = false;
	}
	
	public ExpressionNode<T,U> recursiveDeepCopy(ExpressionNode<T,U> e) {
		try{
			ExpressionNode<T,U> ex;
			
			if(e.isLeaf()) {
				ex = new ExpressionNode<T,U>((Predicate<T,U>)e.leaf.clone(e));
				
			}
			else if(e.getChildren().length == 1) {
				ex = new ExpressionNode<T,U>((QuantifiedRole<T,U>)e.leaf.clone(e),recursiveDeepCopy((ExpressionNode<T,U>)e.getChildren()[0]));
			}
			else {
				ex = new ExpressionNode<T,U>(e.operator,recursiveDeepCopy((ExpressionNode<T,U>)e.getChildren()[0]),recursiveDeepCopy((ExpressionNode<T,U>)e.getChildren()[1]));
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
	
	public Expression<T,U> deepCopy(Expression<T,U> e) {
		if(root == null) {
			try {
				return (Expression<T,U>)e.clone();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
		}
		Expression<T,U> exp = new Expression<T,U>(recursiveDeepCopy(e.root));
		exp.size = exp.root.size;
		return exp;
	}
	
	public Expression<T,U> negate() {
		if(complete) {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + "--" +  this.toString());
			}
		}
		else if(root == null && ((ExpressionNode<T,U>)this).getChildren() == null) {
			System.out.println(1);
			if(this.negated)
				size--;
			else
				size++;
			((ExpressionNode<T,U>)this).leaf.negate();
			negated = ((ExpressionNode<T,U>)this).leaf.isNegated();
		}
		else if(root == null && ((ExpressionNode<T,U>)this).getChildren().length == 1) {
			System.out.println(2);
			if(this.negated)
				size--;
			else
				size++;
			((ExpressionNode<T,U>)this).leaf.negate();
			(((ExpressionNode<T,U>)this).getChildren()[0]).negate();
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
	
	public boolean canJoin(Predicate<T,U> p) {
		return !p.isRole() && !complete && (((long)p.getScope()<0 && (long)scope<0) || p.getScope().equals(scope));
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
	
	public void setNormal(boolean b) {
		normalized = b;
	}
	
	public boolean isNormal() {
		return normalized;
	}
	
	public String toString() {
		//String s = root.toString();
		return root.toString() + " Size: " + this.getSize() + "\tScope: " + Term.makeVariable((long)scope);
	}

	public Expression<T,U> getChild(int i){
		return root.getChildren()[i];
	}
	
	public ExpressionNode<T,U> getRoot() {
		return this.root;
	}

	@Override
	public Predicate<T,U> clone(Expression<T,U> e) {
		return recursiveDeepCopy(e.root);
	}

	@Override
	public boolean isExpression() {
    	return true;
    }

}
