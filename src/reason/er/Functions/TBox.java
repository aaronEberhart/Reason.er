package reason.er.Functions;

import java.util.ArrayList;
import java.util.Random;

import reason.er.Objects.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TBox<T extends Expression<T>>  extends Box<T>{

	public TBox(int size) {

		universe = Predicate.uppers.length / 2;
		variables = Term.lowers.length / 2;
		
		this.counters = new long[3];
		this.counters[0] = 0;
		this.counters[1] = variables + 1;
		this.counters[2] = 0;
		
		scope = variables + 1;

		makeBox(size);
		
		rand = null;
	}
	
	public TBox(ArrayList<Expression<T>> e) {
		universe = Predicate.uppers.length / 2;
		variables = Term.lowers.length / 2;
		
		expressions = e;
	}
	
	@Override
	protected Expression<T> makeExpression() {
		rand = new Random(System.currentTimeMillis());
		Expression<T> expression = new Expression<T>(newPredicate(rand.nextInt(2)));
		scope = expression.getScope();
		
		while(!expression.isComplete()) {
			transform(rand.nextInt(11), expression);
		}
		scope = variables + 1;
		counters[0] = (counters[0] + 1) % universe;
		counters[1] = variables + 1;
		return expression;
	}

	protected void transform(int randInt, Expression<T> expression) {

		if(scope == 25)
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
				expression.superClass(new Concept(false,counters[1],counters[0]));
				break;
			case 6:
			case 7:
				expression.equivalent(new Concept(false,counters[1],counters[0]));
				break;
			case 8:
				expression.dot(new Quantifier(1), (Role)newPredicate(randInt), counters[2] + universe);
				break;
			case 9:
				expression.dot(new Quantifier(2), (Role)newPredicate(randInt), counters[2] + universe);
				break;
			default:
				expression.negate();
				break;
		}
		if(randInt > 3 && randInt < 10 && expression.getSize() > size) {
			scope = counters[1];
			expression.setScope(scope);
		}
	}
	
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		if(randInt == 0) {
			p = new Concept(negated,counters[1],counters[0]);
			counters[0] = (counters[0] + 1) % universe;
		}
		else if(randInt == 1) {
			p = new QuantifiedRole(negated,rand.nextInt(2) + 1,counters[1],counters[1]-1,counters[2] + universe,counters[0],counters[0]);
			counters[0] = (counters[0] + 1) % universe;
			counters[2] = (counters[2] + 1) % universe;
		}
		else {
			p = new Role(false,counters[1] + 1,counters[1],counters[2] + universe);
			counters[2] = (counters[2] + 1) % universe;
			counters[1] = (counters[1] + 1) % Term.lowers.length;
			if(counters[1]<variables)
				counters[1]+=variables;
		}
		
		return p;
	}
	
	@Override
	public void normalizeExpressions(){
		normals = new ArrayList<Expression<T>>();
		
		for(Expression<T> ex : expressions) {
			if(ex.getOperator() == 'c')
				normals.add(ex.normalize());
			else {
				Expression ex1 = new Expression(ex.root.children[0]);
				Expression ex2 = new Expression(ex.root.children[1]);
				normals.add((new Expression(ex.root.children[0]).subClass(new Expression(ex.root.children[1]))).normalize());
				normals.add((new Expression(ex.root.children[0]).superClass(new Expression(ex.root.children[1]))).normalize());
			}
		}
	}
	
	@Override
	public String toString() {
		return "TBox = " + super.toString();
	}

	
}