package reason.er.Functions;

import java.util.Random;

import reason.er.Objects.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TBox<T extends Expression<T>>  extends ExpressionGenerator<T>{

	
	
	public TBox(int size) {

		universe = uppers.length / 2;
		variables = lowers.length / 2;
		
		this.counters = new int[3];
		this.counters[0] = 0;
		this.counters[1] = variables + 1;
		this.counters[2] = 0;
		
		scope = Character.toString(lowers[variables + 1]);

		makeBox(size);
	}
	
	@Override
	protected Expression<T> makeExpression() {
		rand = new Random(System.currentTimeMillis());
		Expression<T> expression = new Expression<T>(newPredicate(rand.nextInt(2)));
		scope = expression.getScope().toString();
		
		while(!expression.isComplete()) {
			transform(rand.nextInt(11), expression);
		}
		scope = Character.toString(lowers[variables + 1]);
		counters[0] = (counters[0] + 1) % universe;
		counters[1] = variables + 1;
		return expression;
	}

	protected void transform(int randInt, Expression<T> expression) {

		if(scope.equals("z"))
			randInt = rand.nextInt(4) + 4;
		
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
			case 5:
				expression.superClass(new Concept(false,scope,makeLabel(counters[0])));
				break;
			case 6:
			case 7:
				expression.equivalent(new Concept(false,scope,makeLabel(counters[0])));
				break;
			case 8:
				expression.dot(new Quantifier(1), (Role)newPredicate(randInt), makeLabel(counters[2] + universe));
				break;
			case 9:
				expression.dot(new Quantifier(2), (Role)newPredicate(randInt), makeLabel(counters[2] + universe));
				break;
			default:
				expression.negate();
				break;
		}
		if(randInt > 3 && randInt < 10 && expression.getSize() > size) {
			scope = makeVariable(counters[1]);
			expression.setScope(new Term(scope));
		}
	}
	
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		if(randInt == 0) {
			p = new Concept(negated,makeVariable(counters[1]),makeLabel(counters[0]));
			counters[0] = (counters[0] + 1) % universe;
		}
		else if(randInt == 1) {
			p = new QuantifiedRole(negated,rand.nextInt(2) + 1,makeVariable(counters[1]),makeVariable(counters[1]-1),makeLabel(counters[2] + universe),makeLabel(counters[0]),makeLabel(counters[0]));
			counters[0] = (counters[0] + 1) % universe;
			counters[2] = (counters[2] + 1) % universe;
		}
		else {
			p = new Role(false,makeVariable(counters[1] + 1),makeVariable(counters[1]),makeLabel(counters[2] + universe));
			counters[2] = (counters[2] + 1) % universe;
			counters[1] = (counters[1] + 1) % lowers.length;
			if(counters[1]<variables)
				counters[1]+=variables;
		}
		
		return p;
	}
	
	@Override
	public String toString() {
		return "TBox = " + super.toString();
	}
	
}