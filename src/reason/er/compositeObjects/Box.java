package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.objects.*;
import reason.er.util.*;

/**
 * Abstract class for Expression building code that constructs Boxes.
 * Contains static variables that can control the types of Expressions that 
 * it produces. Boxes can normalize themselves.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 */
public abstract class Box<T,U>  {
	
	/**
	 * T identifier for the scope of the entire expression
	 */
	protected long scope;
	/**
	 * long array for managing quantification depth.
	 */
	protected long counters[];
	/**
	 * RandomInteger generator.
	 */
	protected RandomInteger rand;
	/**
	 * Maximum quantification depth.
	 */
	protected final int bound = 50;
	/**
	 * Maximum sub-Expressions allowed per Expression.
	 */
	protected final int maxSubExpansions = 10;
	/**
	 * Maximum size of all Expressions.
	 */
	protected final int maxSize = 50;
	/**
	 * int for tracking the number of sub-Expressions.
	 */
	protected int numSubExpansions;	
	/**
	 * int for Predicate name restriction.
	 */
	protected final int universe = Predicate.uppers.length;
	/**
	 * int for variable use restriction.
	 */
	protected final int variables = Term.lowers.length / 2;
	
	/**
	 * List of random Expressions
	 */
	protected ArrayList<Expression<T,U>> expressions;
	/**
	 * Normalized Expressions.
	 */
	protected NormalizedBox<T,U> normalized;

	/**
	 * Make an expression.
	 * @return Expression&lt;T,U&gt;
	 */
	protected abstract Expression<T,U> makeExpression();
	/**
	 * Make a new predicate based on the int parameter.
	 * @param randInt int
	 * @return Predicate&lt;T,U&gt;
	 */
	protected abstract Predicate<T,U> newPredicate(int randInt);
	/**
	 * Changes the expression with logic operations.
	 * @param randInt int
	 * @param expression ExpressionNode&lt;T,U&gt;
	 * @param fromSub boolean
	 * @return ExpressionNode&lt;T,U&gt;
	 */
	protected abstract Expression<T,U> transform(int randInt, ExpressionNode<T,U> expression, boolean fromSub);
	/**
	 * Reset the counter array.
	 * @return long[] 
	 */
	protected abstract long[] resetCounters();
	/**
	 * Normalize the expressions.
	 */
	public abstract void normalizeExpressions();
	/**
	 * Makes a new sub-expression that is treated like a predicate by the expression tree.
	 * @param ran int
	 * @param fromSub boolean
	 * @return Predicate&lt;T,U&gt;
	 */
	protected abstract Predicate<T,U> newSubExpression(int ran,boolean fromSub);
	/**
	 * Make a box with size elements.
	 * 
	 * @param size int
	 */
	protected void makeBox(int size) {
		expressions = new ArrayList<Expression<T,U>>(size);
		while(expressions.size() < size) {
			expressions.add(makeExpression());
		}
	}

	/**
	 * Manually add an expression node to a box.
	 * 
	 * @param e ExpressionNode&lt;T,U&gt;
	 */
	public void addManually(ExpressionNode<T,U> e) {
		expressions.add(new Expression<T,U>(e));
	}
	
	/**
	 * Return the number of names in use.
	 * @return int
	 */
	public int getNumVars() {
		return universe;
	}
	
	/**
	 * Makes a long int for naming a predicate.
	 * 
	 * @param rand RandomInteger
	 * @return long
	 */
	protected long makeName(RandomInteger rand) {
		return rand.nextInt(universe);
	}
	
	/**
	 * Return the normals.
	 * @return ArrayList&lt;Expression&lt;T,U&gt;&gt;
	 */
	public ArrayList<Expression<T,U>> getNormals(){
		if(normalized != null)
			return normalized.getNormals();
		return null;
	}
	
	/**
	 * Copy the normals.
	 * @return ArrayList&lt;Expression&lt;T,U&gt;&gt;
	 */
	public ArrayList<Expression<T,U>> copyNormals(){
		if(normalized != null)
			return normalized.copyNormals();
		return null;
	}
	
	@Override
	public String toString() {
		String s = "{\n\n";
		if(expressions.size() == 0) {
			s = s + "NULL";
		}else {	
			int j = 0;;
			for(int i = 0; i < expressions.size(); i++) {  
				if(normalized == null) {
					s = s + "\t" +expressions.get(i).toString() + ",";
				}
				else {
					s = s + "\tExpression :" + expressions.get(i).toString() + ",";		   		
					s = s + "\n\tNormal: " + normalized.getFromExpressionIndex(j).toString() + ",";
					if(expressions.get(i).root.operator == '=') {
						j++;
						s = s + "\n\tNormal: " + normalized.getFromExpressionIndex(j).toString() + ",";
					}
					j++;
		   		}
		   		s = s + "\n\n";
			}
		}
		return s + " }";
	}

}