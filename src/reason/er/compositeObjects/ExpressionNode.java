package reason.er.compositeObjects;

import reason.er.ReasonEr;
import reason.er.objects.*;

/**
 * 
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
@SuppressWarnings({"unused","unchecked"})
public class ExpressionNode<T,U> extends Expression<T,U>{
	
	/**
	 * The logical operator, if it is binary.
	 */
	protected char operator;
	/**
	 * The Predicate at the node, if it is a leaf.
	 */
	protected Predicate<T,U> leaf;
	/**
	 * the children of an internal node.
	 */
	protected ExpressionNode<T,U>[] children;
	
	/**
	 * Empty constructor.
	 */
	public ExpressionNode() {}

	/**
	 * Make an ExpressionNode from another.
	 * @param e ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode(ExpressionNode<T,U> e) {
		if(e.leaf != null)
			leaf = e.leaf;
		if(e.getChildren() != null)
			setChildren(e.getChildren());
		scope = e.getScope();
		negated = e.isNegated();
		size = e.getSize();
	}
	
	/**
	 * Make an ExpressionNode from a Predicate.
	 * @param p Predicate&lt;T,U&gt;
	 */
	public ExpressionNode(Predicate<T,U> p) {
		leaf = p;
		setChildren(null);
		scope = p.getScope();
		negated = p.isNegated();
		size = p.getSize();
	}
	
	/**
	 * Make a special QuantifiedRole node subtree.
	 * @param qr QuantifiedRole&lt;T,U&gt;
	 * @param subTree ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode(QuantifiedRole<T,U> qr, ExpressionNode<T,U> subTree) {
		leaf = qr;
		negated = qr.isNegated();
		scope = (T)qr.getScope();
		setChildren(new ExpressionNode[1]);
		getChildren()[0] = subTree;
		size = subTree.getSize() + 1;
	}
	
	/**
	 * Make a binary node.
	 * @param o char
	 * @param n1 ExpressionNode&lt;T,U&gt;
	 * @param n2 ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode(char o, ExpressionNode<T,U> n1, ExpressionNode<T,U> n2) {
		operator = o;
		setChildren(new ExpressionNode[2]);
		getChildren()[0] = n1;
		getChildren()[1] = n2;
		scope = n1.getScope();
		if(n1 != null)
			size = n1.getSize() + n2.getSize() + 1;
		else
			size = n2.getSize() + 1;
	}

	/**
	 * Checks whether a node is a leaf.
	 * @return boolean
	 */
	public boolean isLeaf() {
		if(getChildren() == null)
			return true;
		else
			return false;
	}
	
	/**
	 * Gets the i'th child of the node.
	 * @return Expression&lt;T,U&gt;
	 */
	public Expression<T,U> getChild(int i){
		if(children == null)
			return null;
		return getChildren()[i];
	}
	
	/**
	 * Negates the ExpressionNode.
	 */
	public ExpressionNode<T,U> negate() {
		negated = negated ? false : true;
		size = negated ? size + 1 : size - 1;
		if(this.leaf != null) {
			leaf.negate();
		}
		return this;
	}
	
	/**
	 * Gets the number of children.
	 * @return int
	 */
	public int numChildren() {
		if(getChildren() == null)
			return 0;
		else
			return getChildren().length;
	}
	
	/**
	 * Performs conjunction.
	 * @param p Predicate&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> and(Predicate<T,U> p) {
		if(canJoin(p)) {
			return  new ExpressionNode<T,U>('^', new ExpressionNode<T,U>(p), this);
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " ^ " + this.toString());
			}
		}
		return this;
	}
	/**
	 * Performs conjunction.
	 * @param p ExpressionNode&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> and(ExpressionNode<T,U> p) {
		if(canJoin(p)) {
			return  new ExpressionNode<T,U>('^', p, this);
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " ^ " + this.toString());
			}
		}
		return this;
	}
	/**
	 * Performs disjunction.
	 * @param p Predicate&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> or(Predicate<T,U> p) {
				
		if(canJoin(p)) {
			return new ExpressionNode<T,U>('v', this, new ExpressionNode<T,U>(p));
		}
		else {
			try {
				throw ReasonEr.expression;
			} catch (Exception ex) {
				System.out.println(ex + p.toString() + " v " + this.toString());
			}
		}
		return this;
	}
	/**
	 * performs disjunction.
	 * @param p ExpressionNode&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> or(ExpressionNode<T,U> p) {
		
		if(canJoin(p)) {
			return new ExpressionNode<T,U>('v', this, p);
		}
		else {
			try {
				throw ReasonEr.expression;
			} catch (Exception ex) {
				System.out.println(ex + p.toString() + " v " + this.toString());
			}
		}
		return this;
	}
	/**
	 * Creates a superclass Expression.
	 * @param p Predicate&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> superClass(Predicate<T,U> p) {
		if(!p.isNegated() && canJoin(p)) {
			ExpressionNode<T,U> node = new ExpressionNode<T,U>('c', new ExpressionNode<T,U>(p), this);
			node.complete = true;
			return node;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " c " + this.toString());
			}
		}
		return this;
	}
	/**
	 * Creates a subclass expression.
	 * @param p Predicate&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> subClass(Predicate<T,U> p) {
		if(((ExpressionNode<T,U>)this).isLeaf() && canJoin(p)) {
			ExpressionNode<T,U> node = new ExpressionNode<T,U>('c', (ExpressionNode<T,U>)this, new ExpressionNode<T,U>(p));
			node.complete = true;
			return node;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + root.toString() + " c " + p.toString());
			}
		}
		return this;
	}
	/**
	 * Creates an equivalence.
	 * @param p Predicate&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> equivalent(Predicate<T,U> p) {
		if(!p.isNegated() && canJoin(p)) {
			ExpressionNode<T,U> node = new ExpressionNode<T,U>('=', new ExpressionNode<T,U>(p), (ExpressionNode<T,U>)this);
			node.complete = true;
			return node;
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " = " + this.toString());
			}
		}
		return this;
	}
	/**
	 * Quantifies the Expression with a Role.
	 * @param q Quantifier
	 * @param r Role
	 * @param name &lt;U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> dot(Quantifier q, Role<T,U> r, U name) {		
		if(QuantifiedRole.canQuantify(q, r, null, this)) {
			ExpressionNode<T,U> node = new ExpressionNode<T,U>(new QuantifiedRole<T,U>(q,r,this,name),(ExpressionNode<T,U>)this);
			node.size = node.getChildren()[0].size + 1;			
			node.scope = r.getScope();
			return node;
		}
		else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + q.toString() + " " + r.toString() + "." +  this.toString());
			}
		}
		return this;
	}
	
	public String toString() {
		if(getChildren() == null) {
			return leaf.toString();
		}else if(getChildren().length == 1){
			String s = ((QuantifiedRole<T,U>)leaf).getQuantifier().toString() + " " + ((QuantifiedRole<T,U>)leaf).getRole().toString() + "." + getChildren()[0].toString();
			if(negated)
				s = "--" + s;
			return s;
		}else {
			String s = "( " + getChildren()[0].toString() + " " + operator + " " + getChildren()[1].toString() + " )";
			if(negated)
				s = "--" + s;
			return s;
		}
	}

	/**
	 * Gets the children
	 * @return ExpressionNode&lt;T,U&gt;[]
	 */
	public ExpressionNode<T,U>[] getChildren() {
		if(children == null)
			return null;
		return children;
	}

	/**
	 * Sets the children.
	 * @param children ExpressionNode&lt;T,U&gt;[]
	 */
	public void setChildren(ExpressionNode<T,U>[] children) {
		this.children = children;
	}

	/**
	 * Returns true.
	 */
	@Override
	public boolean isExpression() {
		return true;
	}
}
