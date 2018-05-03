package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.ReasonEr;
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
	protected final int quantificationDepth = ReasonEr.quantificationDepth;
	/**
	 * Maximum sub-Expressions allowed per Expression.
	 */
	protected final int maxSubExpressions = ReasonEr.maxSubExpressions;
	/**
	 * Maximum size of all Expressions.
	 */
	protected final int maxSize = ReasonEr.maxSize;
	/**
	 * int for tracking the number of sub-Expressions.
	 */
	protected int numSubExpansions;	
	/**
	 * int for Concept name restriction.
	 */
	protected final int conceptNames = ReasonEr.conceptNames;
	/**
	 * int for Role name restriction.
	 */
	protected final int roleNames = ReasonEr.roleNames;
	/**
	 * int for variable use restriction.
	 */
	protected final int variables = ReasonEr.individuals;
	
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
	 * Reset the counter array.
	 * @return long[] 
	 */
	protected abstract long[] resetCounters();
	/**
	 * Normalize the expressions.
	 */
	public abstract void normalizeExpressions();
	/**
	 * Returns a description logic string representation of the object
	 * @return String
	 */
	public abstract String toDLString();
	/**
	 * Returns a functional syntax string representation of the object
	 * @param tab int
	 * @return String
	 */
	public abstract String toFSString(int tab);
	
	/**
	 * Make a box with size elements.
	 * 
	 * @param size int
	 */
	protected void makeBox(int size) {
		expressions = new ArrayList<Expression<T,U>>(size);
		int maxSize = 0;
		while(expressions.size() < size) {
			Expression<T,U> e = makeExpression();			
			expressions.add(e);
			if(e.getSize()> maxSize)
				maxSize = e.getSize();
		}
		//System.out.println("Max size: "+maxSize);
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
		return conceptNames;
	}
	
	/**
	 * Makes a long int for naming a Concept.
	 * 
	 * @param rand RandomInteger
	 * @return long
	 */
	protected long makeConceptName(RandomInteger rand) {
		long name =  rand.nextInt(conceptNames+1);
		if(name == conceptNames && rand.weightedBool(10000, 9000/conceptNames))//90% of all Top and Bottom are re-picked
			return makeConceptName(rand);													//scaled to adjust some for smaller kbs
		return name;
	}
	
	/**
	 * Makes a long int for naming a Role.
	 * 
	 * @param rand RandomInteger
	 * @return long
	 */
	protected long makeRoleName(RandomInteger rand) {
		return -1 * (rand.nextInt(roleNames) + 1);
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
			s = s + "NULL\n";
		}else {	
			int j = 0;
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