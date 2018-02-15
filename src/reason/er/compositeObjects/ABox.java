package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.objects.*;
import reason.er.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ABox<T,U> extends Box<T,U> {

	private boolean constants,complete;
	private final long[] mem;
	private double time;
	
	public ABox(int size) {
				
		rand = new RandomInteger();

		constants = rand.weightedBool(10000,1000);
		complete = false;
		
		this.counters = resetCounters();
		mem = counters;
		
		scope = counters[3];
		
		numSubExpansions = 0;
		
		makeBox(size);
		
		rand = null;
	}
	
	public ABox(ArrayList<Expression<T,U>> e) {
		numSubExpansions = 0;
		mem = null;
		expressions = e;
	}
	
	protected ExpressionNode transform(int randInt, ExpressionNode expression,boolean fromSub) {
		
		//I'll need this to see if it grew
		int size = expression.getSize();
		
		//just in case
		if(!fromSub && ((scope >= bound && scope > 0) || (scope <= (bound * -1) && scope < 0) || size + 6 >= maxSize)){
				constants = true;
				randInt = (randInt % 2) + 5;
		}else if(constants && !complete) {
				expression.complete = rand.weightedBool(10000,1);
			
		}
		
		//pick what to do
		switch(randInt) {
			case 0:
			case 1:
				if(numSubExpansions >= maxSubExpansions || size + 6 >= maxSize)
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
				if(numSubExpansions >= maxSubExpansions || size + 6 >= maxSize)
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
				expression = expression.dot(new Quantifier(1), (Role)newPredicate(2), -1 * (makeName(rand) % universe));//counters[2] + universe);	
				break;
			default:
				if(!constants && !fromSub)
					constants = rand.weightedBool(10000,1000);
				expression = expression.dot(new Quantifier(2), (Role)newPredicate(2), -1 * (makeName(rand) % universe));// counters[2] + universe);
				break;
			
			
		}
		if(randInt > 4 && expression.getSize() > size) {
			scope = constants?counters[1]:counters[3];
			expression.setScope(scope);
		}		
		
		return expression;
	}
	
	protected Predicate<T,U> newSubExpression(int ran,boolean fromSub){
		
		rand = new RandomInteger();
		
		long tmpScope = scope;		
		
		long[] tmpCounters = this.counters;
		this.counters = mem;
		
		if (constants)
			this.counters[1] = tmpCounters[1];
		
		boolean tmpConst = constants;
		constants = false;
		Predicate<T,U> p = newPredicate(rand.nextInt(2));
		
		if((long)p.getScope() == tmpScope)
			return p;
		
		scope = counters[3];
		ExpressionNode<T,U> builder = new ExpressionNode<T,U>(p);
		
		while((long)builder.getScope() < tmpScope && constants && !(((long)p.getScope()<0 && scope<0) || (long)p.getScope() == scope)) {
			builder = transform(rand.nextInt(ran), builder,true);
			constants = (!tmpConst&&constants) ? false : constants;
		}
		while((long)builder.getScope() != tmpScope && !constants) {
			builder = transform(rand.nextInt(ran), builder,true);
		}
		
		this.counters = tmpCounters;
		this.scope = tmpScope;
		constants = tmpConst;
		return builder;
	}
	
	@Override
	protected Expression<T,U> makeExpression() {
				
		//initialize rand
		rand = new RandomInteger();
				
		int randInt = rand.nextInt(3);
		
		if (randInt == 2) {//if it decided to make a Role
			randInt++;
			constants = true;
			complete = false;
		}
			
		//make the first term
		Expression<T,U> expression = new Expression<T,U>(newPredicate(randInt));
		ExpressionNode builder = expression.root;
		scope = (long)expression.getScope();
		
		//loop until ground and complete
		while(!complete && !constants) {
			
			builder = transform(rand.nextInt(7),builder,false);
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
		
		
		return expression;
	}
	
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		long one = makeName(rand);
		
		if(randInt == 0) {
			p = new Concept(negated,constants?counters[1]:counters[3],one);
		}
		else if(randInt == 1) {
			long two = makeName(rand);
			p = new QuantifiedRole(negated,rand.nextBoolean(),rand.nextInt(2) + 1,constants?counters[1]:counters[3],constants?counters[3]:counters[3]-1,one*-1,two,two);
		}
		else if(randInt == 2) {
			p = new Role(false,constants?counters[1]:counters[3]+1,counters[3],one*-1);
			if(!constants) {
				counters[3] = (counters[3] + 1);
			}
		}
		else {			
			p = new Role(negated,counters[1],(long)(-1 * rand.nextInt(variables)) - 1,-1*one);
		}
				
		return p;
	}
	
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
		long counters[] = new long[4];
		//counters[0] = rand.nextInt(universe) - 1;
		counters[1] = (-1 * rand.nextInt(variables)) - 1;
		//counters[2] = (rand.nextInt(universe) + universe) % universe;
		counters[3] = 2;
		return counters;
	}



	
}
