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
	 * Booleans to control the expression stages.
	 */
	private boolean constants,complete, first;
	
	/**
	 * Make an ABox of size.
	 * @param size int
	 */
	public ABox(int size) {
				
		rand = new RandomInteger();
		
		constants = rand.weightedBool(10000,1000);
		
		complete = false;
		
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
	 * Mutate the ABox.
	 */
	protected ExpressionNode transform(int randInt, ExpressionNode expression,boolean fromSub) {
		
		//I'll need this to see if it grew
		int size = expression.getSize();
		
		//just in case it's getting out of hand
		if(((scope + 2 >= bound && scope > 0) || (scope - 2 <= (bound * -1) && scope < 0) || size + 15 >= maxSize)){
			constants = fromSub?constants:true;
			randInt = (randInt % 2) + 5;
			complete = ((scope >= bound && scope > 0) || (scope <= (bound * -1) && scope < 0) || size + 1 >= maxSize)?true:complete;
		}
		//is it time to stop?
		if(constants && !complete) {
			expression.complete = rand.weightedBool(100,25);
		}
		
		//we're done
		if(expression.complete)
				return expression;
		
		//get ready to continue
		scope = (long)expression.getScope();
		
		//pick what to do
		switch(randInt) {
			case 0:
			case 1:
				if(constants || numSubExpansions >= maxSubExpansions || size + 15 >= maxSize)
					expression = expression.and(newPredicate(randInt));
				else {
					Predicate p = newSubExpression(11,false);
					if(p.isExpression())
						expression = expression.and((ExpressionNode)p);
					else
						expression = expression.and(p);
					numSubExpansions++;
				}
				break;
			case 2:
			case 3:
				if(constants || numSubExpansions >= maxSubExpansions || size + 15 >= maxSize)
					expression = expression.and(newPredicate(randInt % 2));
				else {
					Predicate q = newSubExpression(11,false);
					if(q.isExpression())
						expression = expression.or((ExpressionNode)q);
					else
						expression = expression.or(q);
					numSubExpansions++;
				}
				break;
			case 4:
				expression = expression.negate();
				break;
			case 5:
				if(!constants && !fromSub)
					constants = rand.weightedBool(10000,1000);
				expression = expression.dot(new Quantifier(1), (Role)newPredicate(2), -1 * (makeName(rand) % universe));	
				break;
			default:
				if(!constants && !fromSub)
					constants = rand.weightedBool(10000,1000);				
				expression = expression.dot(new Quantifier(2), (Role)newPredicate(2), -1 * (makeName(rand) % universe));
				break;
			
			
		}	
		
		return expression;
	}
	
	/**
	 * Makes a new sub-expression.
	 */
	@Override
	protected Predicate<T,U> newSubExpression(int ran,boolean fromSub){
		
		rand = new RandomInteger();
		
		long tmpScope = scope;		
		
		long[] tmpCounters = this.counters;
		this.counters = resetCounters();
		
		if(constants)
			this.counters[0] = tmpCounters[0];
		
		boolean tmpConst = constants,tmpFirst = first;
		first = true;
		constants = false;
		Predicate<T,U> p = newPredicate(rand.nextInt(2));
		
		if((long)p.getScope() == tmpScope)
			return p;
		
		scope = counters[1];
		ExpressionNode<T,U> builder = new ExpressionNode<T,U>(p);
		
		while((long)builder.getScope() < tmpScope && constants && !(((long)p.getScope()<0 && scope<0) || (long)p.getScope() == scope)) {
			builder = transform(rand.nextInt(ran), builder,true);
			constants = (!tmpConst&&constants) ? false : constants;
		}
		if(tmpConst) {
			while((long)builder.getScope() < tmpScope && !constants) {
				builder = transform(rand.nextInt(ran), builder,true);
				constants = (!tmpConst&&constants) ? false : constants;
			}
		}
		else {
			while((long)builder.getScope() < tmpScope) {
				builder = transform(rand.nextInt(ran), builder,true);
				constants = false;
			}
		}
		
		this.counters = tmpCounters;
		this.scope = tmpScope;
		constants = tmpConst;
		first = tmpFirst;
		return builder;
	}
	
	/**
	 * Make an ABox expression.
	 */
	@Override
	protected Expression<T,U> makeExpression() {
				
		//initialize rand
		rand = new RandomInteger();
				
		first = true;
		
		int randInt = rand.nextInt(3);
		
		if (randInt == 2) {//if it decided to make a Role
			randInt++;
			constants = true;
			complete = true;
		}
			
		//make the first term
		Expression<T,U> expression = new Expression<T,U>(newPredicate(randInt));
		ExpressionNode builder = expression.root;
		scope = (long)expression.getScope();
		
		//loop until ground and complete
		while(!(complete && constants)){
//			System.out.println(builder);
			builder = transform(rand.nextInt(7),builder,false);
//			System.out.println(builder+"\n");
			expression.root = builder;
			
			if(builder.complete) {
				expression.complete = true;
				complete = true;
			}
			
		}
		expression.setSize(builder.getSize());
		expression.setScope((T)builder.getScope());
		
		//reset variable stuff
		this.counters = resetCounters();
				
		constants = rand.weightedBool(10000,1000);
		complete = false;
		
//		if(expression.getSize() > maxSize)
//			System.out.println(expression.getSize());
		
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
			p = new Concept(negated,constants?counters[0]:counters[1],one);
		}
		else if(randInt == 1) {
			long two = makeName(rand);
			p = new QuantifiedRole(negated,rand.nextBoolean(),rand.nextInt(2) + 1,constants?counters[0]:counters[1],constants?counters[1]:counters[1]-1,one*-1,two,two);
		}
		else if(randInt == 2) {
			long num1 = constants?(-1 * rand.nextInt(variables)) - 1:scope+1;
			long num2 = constants&&!first?counters[0]:scope;
			
			if(constants && first) {
				num2 = scope<0?counters[0]:counters[1];
				scope = num1;
				first = false;
			}
			
			p = new Role(false,num1,num2,one*-1);
			if(!constants) {
				counters[1] = (counters[1] + 1);
			}else {
				counters[0] = num1;
			}
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
