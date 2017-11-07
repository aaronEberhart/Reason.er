package reason.er.Functions;

import java.util.ArrayList;

import reason.er.Objects.*;

@SuppressWarnings({"unchecked","rawtypes","unused"})
public class Normals <T extends Expression<T>>{

	protected ArrayList<Expression<T>> normals;
	
	public Normals(ArrayList<Expression<T>> expressions) {
		normals = new ArrayList<Expression<T>>();
		
		for(Expression e : expressions) {
			normals.add(normalize(e));
		}
	}

	public Object getFromExpressionIndex(int i) {
		return normals.get(i);
	}

	public ExpressionNode deMorgan(ExpressionNode here){
		
		here.children[0].negate();
		here.children[1].negate();
		
		here.children[0] = normalizeTree(here.children[0]);
		here.children[1] = normalizeTree(here.children[1]);
		
		if(here.operator == 'v') {			
			here.operator = '^';
		}
		else if(here.operator == '^') {
			here.operator = 'v';
		}
		
		here.negate();
		
		return here;
	}

	public ExpressionNode negateQuantifier(ExpressionNode here){
		System.out.println(here);
		here.negate();
		((QuantifiedRole)here.leaf).getQuantifier().flipQuantifier();
		((QuantifiedRole)here.leaf).getConcept().negate();
		here.children[0] = normalizeTree(here.children[0]);
		((QuantifiedRole)here.leaf).setSize(here.children[0].getSize() + 1);
		System.out.println(here);
		return here;
	}

	public Expression<T> normalize(Expression e){
		if(e.getSize() <= 2)
			return e;
		else
			return normalizeTree(e.root);
	}
	
	public ExpressionNode normalizeTree(ExpressionNode here) {
		
		if(here.isLeaf() && here.leaf.getClass().equals(ExpressionNode.class)) {
			return normalizeTree((ExpressionNode)here.leaf);
		}
		else if (here.isLeaf() && !(here.leaf.getClass().equals(QuantifiedRole.class) && here.isNegated())) {
			return here;
		}
		if (here.isLeaf() && here.leaf.getClass().equals(QuantifiedRole.class) && here.isNegated()) {
			here.negate();
			((QuantifiedRole)here.leaf).getQuantifier().flipQuantifier();
			((QuantifiedRole)here.leaf).getConcept().negate();
			((QuantifiedRole)here.leaf).setSize(((QuantifiedRole)here.leaf).getConcept().getSize() + ((QuantifiedRole)here.leaf).getRole().getSize());
			here.setSize(((QuantifiedRole)here.leaf).getSize());
			return here;
		}
		else if((here.operator == 'v' || here.operator == '^') && here.isNegated()) {
			return deMorgan(here);
		}
		else if(here.children.length <= 1 && here.leaf != null && here.isNegated()) {
			return negateQuantifier(here);
		}
		else if(here.children.length == 1){
			here.children[0] = normalizeTree(here.children[0]);
			return here;
		}else {
			here.children[0] = normalizeTree(here.children[0]);
			here.children[1] = normalizeTree(here.children[1]);

			return here;
		}
	}
	
}
