package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.objects.*;
import reason.er.util.*;

/**
 * ABox expression generator.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ABox<T,U> extends Box<T,U> {
	
	/**
	 * Make an ABox of size.
	 * @param size int
	 */
	public ABox(int size) {
				
		rand = new RandomInteger();
		
		this.counters = resetCounters();
		
		scope = counters[1];
		
		numSubExpansions = 0;
		
		makeBox(size);
		
		rand = null;
	}
	
	/**
	 * Make an ABox from a list of expressions.
	 * @param e ArrayList&lt;Expression&lt;T,U&gt;&gt;
	 */
	public ABox(ArrayList<Expression<T,U>> e) {
		numSubExpansions = 0;
		expressions = e;
	}
	
	/**
	 * Make an ABox expression.
	 */
	@Override
	protected Expression<T,U> makeExpression() {
				
		//initialize rand
		rand = new RandomInteger();
		
		int randInt = rand.nextInt(2);
			
		//make the first term
		Expression<T,U> expression = new Expression<T,U>(newPredicate(randInt));
		ExpressionNode builder = expression.root;
		
		expression.setSize(builder.getSize());
		expression.setScope((T)builder.getScope());
		
		//reset variable stuff
		this.counters = resetCounters();
				
		return expression;
	}
	
	/**
	 * Make a new Predicate.
	 */
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		long one = makeName(rand);
		
		if(randInt == 0) {
			p = new Concept(negated,counters[0],one);
		}
		else {			
			p = new Role(negated,counters[0],(long)(-1 * rand.nextInt(variables)) - 1,-1*one);
		}
				
		return p;
	}
	
	/**
	 * Normalize the expressions.
	 */
	@Override
	public void normalizeExpressions() {
		
		ArrayList normals = new ArrayList<Expression<T,U>>();
		
		for(Expression<T,U> e : expressions) {
			normals.add(e.deepCopy(e));			
		}
		
		this.normalized = new NormalizedBox(normals);
		
	}
	
	@Override
	public String toString() {
		return "ABox = " + super.toString();
	}

	@Override
	protected long[] resetCounters() {
		long counters[] = new long[2];
		counters[0] = (-1 * rand.nextInt(variables)) - 1;
		counters[1] = 2;
		return counters;
	}

}
