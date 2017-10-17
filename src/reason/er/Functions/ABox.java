package reason.er.Functions;

import java.util.ArrayList;
import java.util.Random;
import reason.er.Objects.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ABox<T extends Expression<T>> extends Box<T> {

	private boolean constants,complete, oneMoreTime;
	
	public ABox(int size) {
		
		rand = new Random(System.currentTimeMillis());

		constants = weightedBool(rand);
		
		if(constants)
			complete = weightedBool(rand);
		else
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
	
	protected void transform(int randInt, Expression<T> expression) {

		//juggle the booleans
		constants = constants?true:weightedBool(rand);
		if(constants && !oneMoreTime) {
			complete = complete?true:weightedBool(rand);
			randInt = (randInt % 2) + 5;
		}
		else {
			complete = false;
			randInt = randInt % 5;
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
				expression.and(newPredicate(randInt));
				break;
			case 2:
			case 3:
				expression.or(newPredicate(randInt % 2));
				break;
			case 4:
				expression.negate();
				break;
			case 5:
				expression.dot(new Quantifier(1), (Role)newPredicate(2), counters[2] + universe);
				break;
			default:
				expression.dot(new Quantifier(2), (Role)newPredicate(2), counters[2] + universe);
				break;
			
			
		}
		if(randInt > 4 && expression.getSize() > size) {
			scope = constants?counters[1]:counters[3];
			expression.setScope(scope);
		}		
		
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
		else if(constants) {
			oneMoreTime = rand.nextBoolean();
		}
			
		//make the first term
		Expression<T> expression = new Expression<T>(newPredicate(randInt));
		scope = expression.getScope();
		
		//loop until ground and complete
		while(!complete && !constants) {
			transform(rand.nextInt(7),expression);
			if(oneMoreTime) {
				transform(rand.nextInt(5),expression);
				oneMoreTime = rand.nextBoolean();
				complete = true;
			}
		}
		
		//reset variable stuff
		this.counters[0] = rand.nextInt(universe) % universe;
		this.counters[1] = rand.nextInt(10000) % variables;
		this.counters[2] = (rand.nextInt(universe) + universe) % universe;
		this.counters[3] = variables + 1;
				
		constants = weightedBool(rand);
		if(constants)
			complete = weightedBool(rand);
		else
			complete = false;
		
		oneMoreTime = rand.nextBoolean();
		
		return expression;
	}
	
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		
		if(randInt == 0) {
			p = new Concept(negated,constants||oneMoreTime?counters[1]:counters[3],counters[0]);
			counters[0] = (counters[0] + 1) % universe;
		}
		else if(randInt == 1) {
			p = new QuantifiedRole(negated,rand.nextInt(2) + 1,constants||oneMoreTime?counters[1]:counters[3],constants||oneMoreTime?counters[3]:counters[3]-1,counters[2] + universe,counters[0],counters[0]);
			counters[0] = (counters[0] + 1) % universe;
			counters[2] = (counters[2] + 1) % universe;
		}
		else if(randInt == 2) {
			p = new Role(false,constants?counters[1]:counters[3]+1,counters[3],counters[2] + universe);
			counters[2] = (counters[2] + 1) % universe;
			if(!constants) {
				counters[3] = (counters[3] + 1) % Term.lowers.length;
				if(counters[3]<variables)
					counters[3]+=variables;
			}
		}
		else {
			p = new Role(negated,counters[1],(long)(rand.nextInt(10000) % variables),counters[2] + universe);
			complete = true;
		}
				
		return p;
	}
	
	private boolean weightedBool(Random rand) {
		return rand.nextInt(10000)>1000?true:false;
	}
	
	@Override
	public String toString() {
		return "ABox = " + super.toString();
	}

	@Override
	public ArrayList<Expression<T>> normalize() {
		// TODO Auto-generated method stub
		return null;
	}	

}
