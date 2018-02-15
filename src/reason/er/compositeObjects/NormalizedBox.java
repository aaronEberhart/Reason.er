package reason.er.compositeObjects;

import java.util.ArrayList;

import reason.er.objects.*;

@SuppressWarnings({"unchecked","rawtypes"})
public class NormalizedBox<T,U>{

	protected ArrayList<Expression<T,U>> normals;
	
	public NormalizedBox(ArrayList<Expression<T,U>> expressions) {
		normals = new ArrayList<Expression<T,U>>(expressions.size());
		
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
		
		here.negate();
		((QuantifiedRole)here.leaf).getQuantifier().flipQuantifier();
		((QuantifiedRole)here.leaf).getConcept().negate();
		
		if(here.numChildren() <= 1 && here.children[0].leaf != null) {
			here.children[0].negate();
		}
		
		here.children[0] = normalizeTree(here.children[0]);
		
		((QuantifiedRole)here.leaf).setSize(here.children[0].getSize() + 1);
		return here;
	}

	public Expression<T,U> normalize(Expression e){
		
		if(e.isNormal() || e.getSize() <= 2)
			return e;
		else {
			Expression<T,U> ex = new Expression(normalizeTree(e.root));
			ex.setSize(ex.root.getSize());
			ex.setScope(ex.root.getScope());
			ex.setNormal(true);
			return ex;
		}
	}
	
	public ExpressionNode normalizeTree(ExpressionNode here) {
		
		if(here.numChildren() == 1 && here.children[0].numChildren() == 2 && here.isNegated()) {
			here.negate();
			((QuantifiedRole)here.leaf).getQuantifier().flipQuantifier();
			here.children[0].negate();
			here.children[0] = normalizeTree(here.children[0]);
			((QuantifiedRole)here.leaf).setSize(((QuantifiedRole)here.leaf).getConcept().getSize() + ((QuantifiedRole)here.leaf).getRole().getSize());
			here.setSize(((QuantifiedRole)here.leaf).getSize());
			return here;
		}
		else if(here.isLeaf() && here.leaf.getClass().equals(ExpressionNode.class)) {
			here= (ExpressionNode)here.leaf;
			here = normalizeTree(here);
			return here;
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
	
	public ArrayList<Expression<T,U>> getNormals(){
		return normals;
	}

	public ArrayList<Expression<T,U>> copyNormals(){
		ArrayList<Expression<T,U>> list = new ArrayList();
		
		for(Expression<T,U> e : normals) {
			list.add(e.deepCopy(e));
		}
		
		return list;
	}

	public void appendExpressions(ArrayList<Expression<T,U>> expressions) {
		for(Expression<T,U> ex : expressions) {
			normals.add(normalize(ex));
		}
	}

}
