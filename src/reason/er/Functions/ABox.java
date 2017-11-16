package reason.er.Functions;

import java.util.ArrayList;
import java.util.Random;
import reason.er.Objects.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ABox<T extends Expression<T>> extends Box<T> {

	private boolean constants,complete;
	
	public ABox(int size) {
				
		rand = new Random(System.currentTimeMillis());

		constants = weightedBool(rand);
		complete = false;
		
		universe = Predicate.uppers.length / 2;
		variables = Term.lowers.length / 2;
		
		this.counters = new long[4];
		this.counters[0] = rand.nextInt(universe) % universe;
		this.counters[1] = rand.nextInt(10000) % variables ;
		this.counters[2] = (rand.nextInt(universe) + universe) % universe;
		this.counters[3] = variables + 1;
		
		scope = counters[3];
		
		makeBox(size);
		
		rand = null;
	}
	
	public ABox(ArrayList<Expression<T>> e) {
		universe = Predicate.uppers.length / 2;
		variables = Term.lowers.length / 2;
		
		expressions = e;
	}
	
	protected ExpressionNode transform(int randInt, ExpressionNode expression) {

		//juggle the booleans
		if(constants && !complete && expression.getScope() <= variables) {
			randInt = (randInt % 2) + 5;
		}
		else if(constants && !complete) {
			expression.complete = weightedBool(rand);
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
				expression = expression.and(newPredicate(randInt));
				break;
			case 2:
			case 3:
				expression = expression.or(newPredicate(randInt % 2));
				break;
			case 4:
				expression = expression.negate();
				break;
			case 5:
				expression = expression.dot(new Quantifier(1), (Role)newPredicate(2), counters[2] + universe);
				constants = true;
				break;
			default:
				expression = expression.dot(new Quantifier(2), (Role)newPredicate(2), counters[2] + universe);
				constants = true;
				break;
			
			
		}
		if(randInt > 4 && expression.getSize() > size) {
			scope = constants?counters[1]:counters[3];
			expression.setScope(scope);
		}		
		
		return expression;
	}
	
	@Override
	protected Expression<T> makeExpression() {
		
		//initialize rand
		rand = new Random(System.currentTimeMillis());
				
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
		
		//reset variable stuff
		this.counters[0] = rand.nextInt(universe) % universe;
		this.counters[1] = rand.nextInt(10000) % variables;
		this.counters[2] = (rand.nextInt(universe) + universe) % universe;
		this.counters[3] = variables + 1;
				
		constants = weightedBool(rand);
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
			counters[0] = (counters[0] + 1) % universe;
		}
		else if(randInt == 1) {
			long two = makeName(rand);
			p = new QuantifiedRole(negated,rand.nextBoolean(),rand.nextInt(2) + 1,constants?counters[1]:counters[3],constants?counters[3]:counters[3]-1,one + universe,two,two);
			counters[0] = (counters[0] + 1) % universe;
			counters[2] = (counters[2] + 1) % universe;
		}
		else if(randInt == 2) {
			p = new Role(false,constants?counters[1]:counters[3]+1,counters[3],one + universe);
			counters[2] = (counters[2] + 1) % universe;
			if(!constants) {
				counters[3] = (counters[3] + 1) % Term.lowers.length;
				if(counters[3]<variables)
					counters[3]+=variables;
			}
		}
		else {			
			p = new Role(negated,counters[1],(long)(rand.nextInt(10000) % variables),one + universe);
			//complete = true;
		}
				
		return p;
	}
	
	private boolean weightedBool(Random rand) {
		return rand.nextInt(10000)>1000?true:false;
	}
	
	@Override
	public void normalizeExpressions() {
		
		ArrayList normals = new ArrayList<Expression<T>>();
		
		for(Expression<T> e : expressions) {
			normals.add(e.deepCopy(e));			
		}
		
		this.normalized = new Normals(normals);
		
	}
	
	@Override
	public String toString() {
		return "ABox = " + super.toString();
	}

	
}
