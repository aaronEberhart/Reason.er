package reason.er.Functions;

import reason.er.ReasonEr;
import reason.er.Objects.*;

@SuppressWarnings({"rawtypes","unused","unchecked"})
public class ExpressionNode extends Expression{
	
	protected char operator;	
	public Predicate leaf;
	protected ExpressionNode[] children;
		
	public ExpressionNode() {
	}

	public ExpressionNode(ExpressionNode e) {
		if(e.leaf != null)
			leaf = e.leaf;
		if(e.children != null)
			children = e.children;
		scope = e.getScope();
		negated = e.isNegated();
		size = e.getSize();
	}
	
	public ExpressionNode(Predicate p) {
		leaf = p;
		children = null;
		scope = p.getScope();
		negated = p.isNegated();
		size = p.getSize();
	}
	
	public ExpressionNode(QuantifiedRole qr, ExpressionNode subTree) {
		leaf = qr;
		negated = qr.isNegated();
		scope = qr.getScope();
		children = new ExpressionNode[1];
		children[0] = subTree;
		size = subTree.getSize() + 1;
	}
	
	public ExpressionNode(char o, ExpressionNode n1, ExpressionNode n2) {
		operator = o;
		children = new ExpressionNode[2];
		children[0] = n1;
		children[1] = n2;
		scope = n1.getScope();
		if(n1 != null)
			size = n1.getSize() + n2.getSize() + 1;
		else
			size = n2.getSize() + 1;
	}

	public boolean isLeaf() {
		if(children == null)
			return true;
		else
			return false;
	}
	
	public Expression getChild(int i){
		return children[i];
	}
	
	@Override
	public ExpressionNode negate() {
		negated = negated ? false : true;
		size = negated ? size + 1 : size - 1;
		if(this.leaf != null) {
			leaf.negate();
		}
//		if(this.numChildren() == 1) {
//			this.children[0].negate();
//		}
		return this;
	}
	
	public int numChildren() {
		if(children == null)
			return 0;
		else
			return children.length;
	}
	
	public ExpressionNode and(Predicate p) {
		if(canJoin(p)) {
			return  new ExpressionNode('^', new ExpressionNode(p), this);
		}else {
			try {
				throw ReasonEr.expression;
			} catch (Exception e) {
				System.out.println(e + p.toString() + " ^ " + this.toString());
			}
		}
		return this;
	}
	public ExpressionNode or(Predicate p) {
				
		if(canJoin(p)) {
			return new ExpressionNode('v', this, new ExpressionNode(p));
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
	public ExpressionNode superClass(Predicate p) {
		if(!p.isNegated() && canJoin(p)) {
			ExpressionNode node = new ExpressionNode('c', new ExpressionNode(p), this);
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
	public ExpressionNode subClass(Predicate p) {
		if(((ExpressionNode)this).isLeaf() && canJoin(p)) {
			ExpressionNode node = new ExpressionNode('c', (ExpressionNode)this, new ExpressionNode(p));
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
	public ExpressionNode equivalent(Predicate p) {
		if(!p.isNegated() && canJoin(p)) {
			ExpressionNode node = new ExpressionNode('=', new ExpressionNode(p), (ExpressionNode)this);
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
	public ExpressionNode dot(Quantifier q, Role r, long name) {		
		if(QuantifiedRole.canQuantify(q, r, null, this)) {
			ExpressionNode node = new ExpressionNode(new QuantifiedRole(q,r,this,name),(ExpressionNode)this);
			node.size = node.children[0].size + 1;			
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
