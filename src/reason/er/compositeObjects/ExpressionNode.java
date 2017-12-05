package reason.er.compositeObjects;

import reason.er.ReasonEr;
import reason.er.objects.*;

@SuppressWarnings({"unused","unchecked"})
public class ExpressionNode<T extends Predicate<T>> extends Expression<T>{
	
	protected char operator;	
	protected Predicate<T> leaf;
	protected ExpressionNode<T>[] children;
		
	public ExpressionNode() {
	}

	public ExpressionNode(ExpressionNode<T> e) {
		if(e.leaf != null)
			leaf = e.leaf;
		if(e.getChildren() != null)
			setChildren(e.getChildren());
		scope = e.getScope();
		negated = e.isNegated();
		size = e.getSize();
	}
	
	public ExpressionNode(Predicate<T> p) {
		leaf = p;
		setChildren(null);
		scope = p.getScope();
		negated = p.isNegated();
		size = p.getSize();
	}
	
	public ExpressionNode(QuantifiedRole<T,T> qr, ExpressionNode<T> subTree) {
		leaf = qr;
		negated = qr.isNegated();
		scope = qr.getScope();
		setChildren(new ExpressionNode[1]);
		getChildren()[0] = subTree;
		size = subTree.getSize() + 1;
	}
	
	public ExpressionNode(char o, ExpressionNode<T> n1, ExpressionNode<T> n2) {
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

	public boolean isLeaf() {
		if(getChildren() == null)
			return true;
		else
			return false;
	}
	
	public Expression<T> getChild(int i){
		return getChildren()[i];
	}
	
	@Override
	public ExpressionNode<T> negate() {
		negated = negated ? false : true;
		size = negated ? size + 1 : size - 1;
		if(this.leaf != null) {
			leaf.negate();
		}
		return this;
	}
	
	public int numChildren() {
		if(getChildren() == null)
			return 0;
		else
			return getChildren().length;
	}
	
	public ExpressionNode<T> and(Predicate<T> p) {
		if(canJoin(p)) {
			return  new ExpressionNode<T>('^', new ExpressionNode<T>(p), this);
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " ^ " + this.toString());
			}
		}
		return this;
	}
	public ExpressionNode<T> or(Predicate<T> p) {
				
		if(canJoin(p)) {
			return new ExpressionNode<T>('v', this, new ExpressionNode<T>(p));
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
	public ExpressionNode<T> superClass(Predicate<T> p) {
		if(!p.isNegated() && canJoin(p)) {
			ExpressionNode<T> node = new ExpressionNode<T>('c', new ExpressionNode<T>(p), this);
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
	public ExpressionNode<T> subClass(Predicate<T> p) {
		if(((ExpressionNode<T>)this).isLeaf() && canJoin(p)) {
			ExpressionNode<T> node = new ExpressionNode<T>('c', (ExpressionNode<T>)this, new ExpressionNode<T>(p));
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
	public ExpressionNode<T> equivalent(Predicate<T> p) {
		if(!p.isNegated() && canJoin(p)) {
			ExpressionNode<T> node = new ExpressionNode<T>('=', new ExpressionNode<T>(p), (ExpressionNode<T>)this);
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
	public ExpressionNode<T> dot(Quantifier q, Role<T,T> r, long name) {		
		if(QuantifiedRole.canQuantify(q, r, null, this)) {
			ExpressionNode<T> node = new ExpressionNode<T>(new QuantifiedRole<T,T>(q,r,this,name),(ExpressionNode<T>)this);
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
			String s = ((QuantifiedRole<T,T>)leaf).getQuantifier().toString() + " " + ((QuantifiedRole<T,T>)leaf).getRole().toString() + "." + getChildren()[0].toString();
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

	public ExpressionNode<T>[] getChildren() {
		return children;
	}

	public void setChildren(ExpressionNode<T>[] children) {
		this.children = children;
	}

	
}
