package reason.er.Functions;

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
		if(this.numChildren() == 1) {
			this.children[0].negate();
		}
		return this;
	}
	
	public int numChildren() {
		if(children == null)
			return 0;
		else
			return children.length;
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
