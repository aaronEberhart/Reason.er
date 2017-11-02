package reason.er.Functions;

import reason.er.Objects.*;

@SuppressWarnings({"rawtypes","unused"})
public class ExpressionNode extends Expression{
	
	protected char operator;
	
	public Predicate leaf;
	protected Expression[] children;
			
	
	private ExpressionNode() {
	}

	public ExpressionNode(Predicate p) {
		leaf = p;
		children = null;
		negated = p.isNegated();
		size = p.getSize();
	}
	
	public ExpressionNode(QuantifiedRole qr, ExpressionNode subTree) {
		leaf = qr;
		negated = qr.isNegated();
		children = new Expression[1];
		children[0] = subTree;
		size = subTree.getSize();
	}
	
	public ExpressionNode(char o, ExpressionNode n1, ExpressionNode n2) {
		operator = o;
		children = new Expression[2];
		children[0] = n1;
		children[1] = n2;
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
