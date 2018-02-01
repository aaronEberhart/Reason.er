package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.objects.*;
import reason.er.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ABox<T extends Expression<T>> extends Box<T> {

	private boolean constants,complete;
	private final long[] mem;
	
	public ABox(int size) {
				
		rand = new RandomInteger();

		constants = rand.weightedBool(10000,1000);
		complete = false;
		
		this.counters = resetCounters();
		mem = counters;
		
		scope = counters[3];
		
		makeBox(size);
		
		rand = null;
	}
	
	public ABox(ArrayList<Expression<T>> e) {
		mem = null;
		expressions = e;
	}
	
	protected ExpressionNode transform(int randInt, ExpressionNode expression) {

		//juggle the booleans
		if(constants && !complete && expression.getScope() <= variables) {
			randInt = (randInt % 2) + 5;
		}
		else if(constants && !complete) {
			expression.complete = rand.weightedBool(10000,1000);
		}
		
		//just in case
		if(scope == 25) {
			complete = true;
			constants = true;
			randInt = (randInt % 2) + 5;
		}
		
		//I'll need this to see if it grew
		int size = expression.getSize();
		
		//pick what to do
		switch(randInt) {
			case 0:
			case 1:
				Predicate p = newSubExpression(11);
				if(p.isExpression())
					expression = expression.and((ExpressionNode)p);
				else
					expression = expression.and(p);
				break;
			case 2:
			case 3:
				Predicate q = newSubExpression(11);
				if(q.isExpression())
					expression = expression.or((ExpressionNode)q);
				else
					expression = expression.or(q);
				break;
			case 4:
				expression = expression.negate();
				break;
			case 5:
				constants = rand.weightedBool(10000,1000);
				expression = expression.dot(new Quantifier(1), (Role)newPredicate(2), counters[2] + universe);	
				break;
			default:
				constants = rand.weightedBool(10000,1000);
				expression = expression.dot(new Quantifier(2), (Role)newPredicate(2), counters[2] + universe);
				break;
			
			
		}
		if(randInt > 4 && expression.getSize() > size) {
			scope = constants?counters[1]:counters[3];
			expression.setScope(scope);
		}		
		
		return expression;
	}
	
	protected Predicate<T> newSubExpression(int ran){
		
		rand = new RandomInteger();
		
		long tmpScope = scope;		
		
		long[] tmpCounters = this.counters;
		this.counters = mem;
		
		if (constants)
			this.counters[1] = tmpCounters[1];
		
		boolean tmpConst = constants;
		constants = false;
		Predicate<T> p = newPredicate(rand.nextInt(2));
		
		if(p.getScope() == tmpScope)
			return p;
		
		scope = counters[3];
		ExpressionNode<T> builder = new ExpressionNode<T>(p);
		
		while(builder.getScope() < tmpScope && constants) {
			builder = transform(rand.nextInt(ran), builder);
			constants = (!tmpConst && constants) ? false : constants;
		}
		while(builder.getScope() != tmpScope && !constants) {
			builder = transform(rand.nextInt(ran), builder);
		}
		
		this.counters = tmpCounters;
		this.scope = tmpScope;
		constants = tmpConst;
		return builder;
	}
	
	@Override
	protected Expression<T> makeExpression() {
		
		//initialize rand
		rand = new RandomInteger();
				
		int randInt = rand.nextInt(3);
		
		if (randInt == 2) {//if it decided to make a Role
			randInt++;
			constants = true;
			complete = false;
		}
			
		//make the first term
		Expression<T> expression = new Expression<T>(newPredicate(randInt));
		ExpressionNode builder = expression.root;
		scope = expression.getScope();
		
		//loop until ground and complete
		while(!complete && !constants) {
			
			builder = transform(rand.nextInt(7),builder);
			expression.root = builder;
			
			if(builder.complete) {
				expression.complete = true;
				complete = true;
			}
		}
		expression.setSize(builder.getSize());
		expression.setScope(builder.getScope());
		
		//reset variable stuff
		this.counters[0] = rand.nextInt(universe) % universe;
		this.counters[1] = rand.nextInt(10000) % variables;
		this.counters[2] = (rand.nextInt(universe) + universe) % universe;
		this.counters[3] = variables + 1;
				
		constants = rand.weightedBool(10000,1000);
		complete = false;
		
		return expression;
	}
	
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		long one = (makeName(rand) % universe);
		
		if(randInt == 0) {
			p = new Concept(negated,constants?counters[1]:counters[3],one);
			counters[0] = (counters[0] + 1) % universe;
		}
		else if(randInt == 1) {
			long two = (makeName(rand) % universe);
			p = new QuantifiedRole(negated,rand.nextBoolean(),rand.nextInt(2) + 1,constants?counters[1]:counters[3],constants?counters[3]:counters[3]-1,one*-1,two,two);
			counters[0] = (counters[0] + 1) % universe;
			counters[2] = (counters[2] + 1) % universe;
		}
		else if(randInt == 2) {
			p = new Role(false,constants?counters[1]:counters[3]+1,counters[3],one*-1);
			counters[2] = (counters[2] + 1) % universe;
			if(!constants) {
				counters[3] = (counters[3] + 1) % Term.lowers.length;
				if(counters[3]<variables)
					counters[3]+=variables;
			}
		}
		else {			
			p = new Role(negated,counters[1],(long)(rand.nextInt(10000) % variables),-1*one);
		}
				
		return p;
	}
	
	@Override
	public void normalizeExpressions() {
		
		ArrayList normals = new ArrayList<Expression<T>>();
		
		for(Expression<T> e : expressions) {
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
		counters[0] = rand.nextInt(universe) % universe;
		counters[1] = rand.nextInt(27);
		counters[2] = (rand.nextInt(universe) + universe) % universe;
		counters[3] = variables + 1;
		return counters;
	}

	
}
