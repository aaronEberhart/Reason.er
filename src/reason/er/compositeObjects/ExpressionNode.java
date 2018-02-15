package reason.er.compositeObjects;

import reason.er.ReasonEr;
import reason.er.objects.*;

@SuppressWarnings({"unused","unchecked"})
public class ExpressionNode<T,U> extends Expression<T,U>{
	
	protected char operator;	
	protected Predicate<T,U> leaf;
	protected ExpressionNode<T,U>[] children;
		
	public ExpressionNode() {
	}

	public ExpressionNode(ExpressionNode<T,U> e) {
		if(e.leaf != null)
			leaf = e.leaf;
		if(e.getChildren() != null)
			setChildren(e.getChildren());
		scope = e.getScope();
		negated = e.isNegated();
		size = e.getSize();
	}
	
	public ExpressionNode(Predicate<T,U> p) {
		leaf = p;
		setChildren(null);
		scope = p.getScope();
		negated = p.isNegated();
		size = p.getSize();
	}
	
	public ExpressionNode(QuantifiedRole<T,U> qr, ExpressionNode<T,U> subTree) {
		leaf = qr;
		negated = qr.isNegated();
		scope = (T)qr.getScope();
		setChildren(new ExpressionNode[1]);
		getChildren()[0] = subTree;
		size = subTree.getSize() + 1;
	}
	
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

	public boolean isLeaf() {
		if(getChildren() == null)
			return true;
		else
			return false;
	}
	
	public Expression<T,U> getChild(int i){
		return getChildren()[i];
	}
	
	@Override
	public ExpressionNode<T,U> negate() {
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

	public ExpressionNode<T,U>[] getChildren() {
		return children;
	}

	public void setChildren(ExpressionNode<T,U>[] children) {
		this.children = children;
	}

	@Override
	public boolean isExpression() {
    	return true;
    }
}
