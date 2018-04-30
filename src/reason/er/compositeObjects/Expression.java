package reason.er.compositeObjects;

import reason.er.objects.*;

/**
 * The Expression is a handle for grasping a tree of ExpressionNodes.
 * It has a root and booleans to track whether it is complete or
 * normalized.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
@SuppressWarnings({ "unchecked"})
public class Expression<T,U> extends Concept<T,U>{

	/**
	 * The root of the Expression tree.
	 */
	protected ExpressionNode<T,U> root;
	/**
	 * Booleans to track the status of an altered Expression.
	 */
	protected boolean complete, normalized;
	
	/**
	 * Empty constructor.
	 */
	public Expression() {}
	
	/**
	 * Makes an expression from a Predicate.
	 * @param p Predicate&lt;T,U&gt;
	 */
	public Expression(Predicate<T,U> p) {
		scope = p.getScope();
		root = new ExpressionNode<T,U>(p);
		negated = p.isNegated();
		complete = false;
		normalized = false;
		size = p.getSize();
	}
	
	/**
	 * Makes an Expression from an ExpressionNode,
	 * @param e ExpressionNode&lt;T,U&gt;
	 */
	public Expression(ExpressionNode<T,U> e){
		scope = e.getScope();
		root = recursiveDeepCopy(e);	
		negated = e.negated;
		complete = true;
		normalized = false;
		size = e.getSize();
	}

	/**
	 * Makes an Expression from another expression.
	 * @param e Expression&lt;T,U&gt;
	 */
	public Expression(Expression<T,U> e) {
		root = (ExpressionNode<T,U>)e;
		scope = e.scope;
		this.size = e.getSize();
		complete = true;
		normalized = false;
	}
	
	/**
	 * Makes a copy of the ExpressionNode in memory.
	 * @param e ExpressionNode&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
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
	
	/**
	 * Makes a copy of the Expression in memory.
	 * @param e Expression&lt;T,U&gt;
	 * @return Expression&lt;T,U&gt;
	 */
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

	/**
	 * Verifies that a Predicate is allowed to join the Expression.
	 * @param p Predicate&lt;T,U&gt;
	 * @return boolean
	 */
	public boolean canJoin(Predicate<T,U> p) {
		return !p.isRole() && !complete && p.getScope().equals(scope);
	}
	
	/**
	 * Checks whether the Expression is complete or not.
	 * @return boolean
	 */
	public boolean isComplete() {
		return complete;
	}
	
	/**
	 * Returns the operator at the root of the Expression.
	 * @return char
	 */
	public char getOperator() {
		if(complete)
			return root.operator;
		else return 0;
	}
	
	/**
	 * Sets the operator.
	 * @param c char
	 */
	public void setOperator(char c) {
		root.operator = c;
	}
	
	/**
	 * Sets whether the Expression has been normalized.
	 * @param b boolean
	 */
	public void setNormal(boolean b) {
		normalized = b;
	}
	
	/**
	 * Checks if the Expression has been normalized.
	 * @return boolean
	 */
	public boolean isNormal() {
		return normalized;
	}
	
	public String toString() {
		return root.toString() + "\tSize: " + this.getSize() + "\tScope: " + Term.makeVariable((long)scope);
	}
	
	public String toDLString() {
		if(root != null)
			return root.toDLString(true);
		return ((ExpressionNode)this).toDLString(false);
	}
	
	public String toFSString(int tab) {
		if(size <= 2 && !((Predicate)(root.leaf)).isExpression()) {
			String s = ":" + ((Predicate)(root.leaf)).makeLabel();
			s = negated?"\tObjectComplementOf( "+s+" )":"\t"+s;
			return s;
		}
		return root.toFSString(tab);
	}

	/**
	 * Negates an Expression
	 */
	public Expression<T,U> negate() {
		root.negate();
		return this;
	}
	
	/**
	 * Returns child[i] of the Expression.
	 * @param i int
	 * @return Expression&lt;T,U&gt;
	 */
	public Expression<T,U> getChild(int i){
		if(root.getChildren() == null)
			return null;
		return root.getChildren()[i];
	}
	
	/**
	 * Gets the root.
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> getRoot() {
		return this.root;
	}

	@Override
	public Predicate<T,U> clone(Expression<T,U> e) {
		return recursiveDeepCopy(e.root);
	}

	/**
	 * Returns true.
	 * @return true
	 */
	@Override
	public boolean isExpression() {
		return true;
	}

}
