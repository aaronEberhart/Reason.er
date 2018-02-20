package reason.er.compositeObjects;

import java.util.ArrayList;

import reason.er.objects.*;

/**
 * A specialized type of Box analog that is separate because it lacks the generator variables and methods
 * but has methods necessary to normalize expressions.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class NormalizedBox<T,U>{

	/**
	 * List of normalized Expressions.
	 */
	protected ArrayList<Expression<T,U>> normals;
	
	/**
	 * Adds the list of expressions then normalizes them.
	 * @param expressions ArrayList&lt;Expression&lt;T,U&gt;&gt;
	 */
	public NormalizedBox(ArrayList<Expression<T,U>> expressions) {
		normals = new ArrayList<Expression<T,U>>(expressions.size());
		
		for(Expression e : expressions) {
			normals.add(normalize(e));
		}
	}

	/**
	 * Gets an expression from the list
	 * @param i int
	 * @return Expression&lt;T,U&gt;
	 */
	public Expression<T,U> getFromExpressionIndex(int i) {
		return normals.get(i);
	}

	/**
	 * Performs DeMorgan operation on the node
	 * @param here ExpressionNode&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> deMorgan(ExpressionNode<T,U> here){
		
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

	/**
	 * Push the negation through the quantifier.
	 * @param here ExpressionNode&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> negateQuantifier(ExpressionNode<T,U> here){
		
		if(!here.isNegated())
			return here;
		
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

	/**
	 * Normalizes an expression, if it needs to be.
	 * @param e Expression&lt;T,U&gt;
	 * @return Expression&lt;T,U&gt;
	 */
	public Expression<T,U> normalize(Expression<T,U> e){
		
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
	
	/**
	 * Normalizes the expression tree node passed to it.
	 * @param here ExpressionNode&lt;T,U&gt;
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	public ExpressionNode<T,U> normalizeTree(ExpressionNode<T,U> here) {
		
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
	
	/**
	 * Returns the normals.
	 * @return ArrayList&lt;Expression&lt;T,U&gt;&gt;
	 */
	public ArrayList<Expression<T,U>> getNormals(){
		return normals;
	}

	/**
	 * Returns a copy of the normals.
	 * @return ArrayList&lt;Expression&lt;T,U&gt;&gt;
	 */
	public ArrayList<Expression<T,U>> copyNormals(){
		ArrayList<Expression<T,U>> list = new ArrayList();
		
		for(Expression<T,U> e : normals) {
			list.add(e.deepCopy(e));
		}
		
		return list;
	}

	/**
	 * Appends more expressions to the list after normalizing them.
	 * @param expressions ArrayList&lt;Expression&lt;T,U&gt;&gt;
	 */
	public void appendExpressions(ArrayList<Expression<T,U>> expressions) {
		for(Expression<T,U> ex : expressions) {
			normals.add(normalize(ex));
		}
	}

}
