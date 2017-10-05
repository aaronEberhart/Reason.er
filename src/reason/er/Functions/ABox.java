package reason.er.Functions;

import java.util.ArrayList;
import java.util.Random;
import reason.er.Objects.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ABox<T extends Expression<T>> extends ExpressionGenerator<T> {

	private boolean constants,complete;
	
	public ABox(int size) {
		
		rand = new Random(System.currentTimeMillis());

		constants = weightedBool(rand);
		
		if(constants)
			complete = weightedBool(rand);
		else
			complete = false;
		
		universe = uppers.length / 2;
		variables = lowers.length / 2;
		
		this.counters = new int[4];
		this.counters[0] = rand.nextInt(universe) % universe;
		this.counters[1] = rand.nextInt(10000) % variables ;
		this.counters[2] = (rand.nextInt(universe) + universe) % universe;
		this.counters[3] = variables + 1;
		
		scope = makeVariable(counters[3]);
		
		makeBox(size);
	}
	
	public ABox(ArrayList<Expression<T>> e) {
		universe = uppers.length / 2;
		variables = lowers.length / 2;
		
		expressions = e;
	}
	
	protected void transform(int randInt, Expression<T> expression) {

		constants = constants?true:weightedBool(rand);
		if(constants)
			complete = complete?true:weightedBool(rand);
		else
			complete = false;		
		
		if(scope.equals("z")) {
			complete = true;
			constants = true;
			randInt = (randInt % 2) + 4;
		}
			
		if(constants)
			randInt = (randInt % 2) + 5;
		else
			randInt = randInt % 5;
		
		int size = expression.getSize();
		
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
				expression.dot(new Quantifier(1), (Role)newPredicate(2), makeLabel(counters[2] + universe));
				break;
			default:
				expression.dot(new Quantifier(2), (Role)newPredicate(2), makeLabel(counters[2] + universe));
				break;
			
			
		}
		if(randInt > 3 && randInt < 6 && expression.getSize() > size) {
			scope = makeVariable(constants?counters[1]:counters[3]);
			expression.setScope(new Term(scope));
		}		
		
	}
	
	@Override
	protected Expression<T> makeExpression() {
		rand = new Random(System.currentTimeMillis());
		
		int randInt = rand.nextInt(3);
		if (randInt == 2) {
			randInt++;
			constants = true;
			complete = false;
		}
			
		Expression<T> expression = new Expression<T>(newPredicate(randInt));
		
		complete = randInt==2?true:complete;
		
		while(!complete && !constants)
			transform(rand.nextInt(7),expression);
		
		
		this.counters[0] = rand.nextInt(universe) % universe;
		this.counters[1] = rand.nextInt(10000) % variables;
		this.counters[2] = (rand.nextInt(universe) + universe) % universe;
		this.counters[3] = variables + 1;
		
		try {
			Thread.sleep(31);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		constants = weightedBool(rand);
		if(constants)
			complete = weightedBool(rand);
		else
			complete = false;
		
		return expression;
	}
	
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		
		if(randInt == 0) {
			p = new Concept(negated,makeVariable(constants?counters[1]:counters[3]),makeLabel(counters[0]));
			counters[0] = (counters[0] + 1) % universe;
		}
		else if(randInt == 1) {
			p = new QuantifiedRole(negated,rand.nextInt(2) + 1,makeVariable(constants?counters[1]:counters[3]),makeVariable(constants?counters[3]:counters[3]-1),makeLabel(counters[2] + universe),makeLabel(counters[0]),makeLabel(counters[0]));
			counters[0] = (counters[0] + 1) % universe;
			counters[2] = (counters[2] + 1) % universe;
		}
		else if(randInt == 2) {
			p = new Role(false,makeVariable(constants?counters[1]:counters[3]+1),makeVariable(counters[3]),makeLabel(counters[2] + universe));
			counters[2] = (counters[2] + 1) % universe;
			if(!constants) {
				counters[3] = (counters[3] + 1) % lowers.length;
				if(counters[3]<variables)
					counters[3]+=variables;
			}
		}
		else {
			p = new Role(false,makeVariable(counters[1]),makeVariable((counters[1] + 1)%variables),makeLabel(counters[2] + universe));
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

	

}
