package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.compositeObjects.*;
import reason.er.objects.*;
import reason.er.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TBox<T extends Expression<T>>  extends Box<T>{

	protected ArrayList<Long> names;
	
	public TBox(int size) {

		names = new ArrayList<>();
		
		universe = Predicate.uppers.length / 2;
		variables = Term.lowers.length / 2;
		
		this.counters = resetCounters();
		
		scope = counters[1];

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
		
		rand = new RandomInteger();
		Expression<T> expression = new Expression<T>(newPredicate(rand.nextInt(2)));
		scope = expression.getScope();
		
		ExpressionNode builder = expression.root;
		
		while(!expression.isComplete()) {
			
			builder = transform(rand.nextInt(11), builder);
			expression.root = builder;
			
			if(builder.complete)
				expression.complete = true;
		}
		
		expression.setSize(builder.getSize());
		expression.setScope(builder.getScope());
		
		names.clear();
		scope = variables + 1;
		counters[0] = (counters[0] + 1) % universe;
		counters[1] = variables + 1;
		
		return expression;
	}

	protected Predicate<T> newSubExpression(int ran){
		rand = new RandomInteger();
		long[] tmpCounters = this.counters;
		long tmpScope = scope;
		this.counters = resetCounters();
		
		Predicate<T> p = newPredicate(rand.nextInt(2));
		ExpressionNode<T> builder = new ExpressionNode<T>(p);
		
		if(p.getScope() == scope)
			return p;
		
		scope = counters[1];
		
		while(builder.getScope() < tmpScope) {
			
			int num = rand.nextBoolean() ? rand.nextInt(4) : (rand.nextInt(3) + 8);
					
			builder = transform(num, builder);
		}
		
		this.counters = tmpCounters;
		this.scope = tmpScope;
		return builder;
	}
	
	protected ExpressionNode transform(int randInt, ExpressionNode expression) {

		if(scope == 25)
			randInt = rand.nextInt(4) + 4;
		
		int size = expression.getSize();
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
			case 5:
				expression = expression.superClass(new Concept(false,expression.getScope(),endExpression(rand)));
				break;
			case 6:
			case 7:
				expression = expression.equivalent(new Concept(false,expression.getScope(),endExpression(rand)));
				break;
			case 8:
				expression = expression.dot(new Quantifier(1), (Role)newPredicate(randInt), counters[2] + universe);
				scope+=1;
				break;
			case 9:
				expression = expression.dot(new Quantifier(2), (Role)newPredicate(randInt), counters[2] + universe);
				scope+=1;
				break;
			default:
				expression = expression.negate();
				break;
		}
		if(randInt > 3 && randInt < 10 && expression.getSize() > size) {
			scope = counters[1];
			expression.setScope(scope);
		}

		return expression;
	}
	
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		long one = (makeName(rand) % universe) * (rand.nextInt(3) + 1);
		long two = ((makeName(rand) % universe) + universe) * (rand.nextInt(3) + 1);
		
		if(randInt == 0) {
			p = new Concept(negated,counters[1],one);
			names.add(one);
			counters[0] = (counters[0] + 1) % universe;
		}
		else if(randInt == 1) {
			
			p = new QuantifiedRole(negated,rand.nextBoolean(),rand.nextInt(2) + 1,counters[1],counters[1]-1,two,one,one);
			names.add(one);
			counters[0] = (counters[0] + 1) % universe;
			counters[2] = (counters[2] + 1) % universe;
		}
		else {
			p = new Role(false,counters[1] + 1,counters[1],two);
			counters[2] = (counters[2] + 1) % universe;
			counters[1] = (counters[1] + 1) % Term.lowers.length;
			if(counters[1]<variables)
				counters[1]+=variables;
		}
		
		return p;
	}

	public boolean usedBefore(long i) {
    	for(Long l : names) {
    		if(l.longValue() == i)
    			return true;
    	}
    	return false;
    }
	
	public long endExpression(RandomInteger rand) {
		long l = (makeName(rand) % universe) / 2;
		
		while(usedBefore(l)) {
			l += (universe * 2);
		}
		
		return l;
	}
	
	@Override
	public void normalizeExpressions() {
		ArrayList normals = new ArrayList<Expression<T>>();
		
		for(Expression<T> e : expressions) {
			
			Expression ex = e.deepCopy(e);
			
			final ExpressionNode right = (ExpressionNode)ex.root.getChildren()[1];
			final ExpressionNode left = (ExpressionNode)ex.root.getChildren()[0];
			
			if(ex.getOperator() == 'c'){
				normals.add(
						(new Expression(normalizeSubset(left,right))));
			}
			else {
				Expression ex2 = e.deepCopy(e);
				
				final ExpressionNode up = (ExpressionNode)ex2.root.getChildren()[1];
				final ExpressionNode down = (ExpressionNode)ex2.root.getChildren()[0];
				
				normals.add(
						(new Expression(normalizeSubset(left,right))));
				normals.add(
						(new Expression(normalizeSubset(up,down))));
				
			}
		}
		
		this.normalized = new NormalizedBox(normals);
	}
	
	public Expression normalizeSubset(ExpressionNode left, ExpressionNode right) {
		left.negate();
		return left.or(right);
	}
	
	@Override
	public String toString() {
		return "TBox = " + super.toString();
	}

	

	@Override
	protected long[] resetCounters() {
		long [] counters = new long[3];
		counters[0] = 0;
		counters[1] = variables + 1;
		counters[2] = 0;
		return counters;
	}

	
}