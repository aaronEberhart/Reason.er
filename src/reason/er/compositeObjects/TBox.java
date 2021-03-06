package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.ReasonEr;
import reason.er.compositeObjects.*;
import reason.er.objects.*;
import reason.er.util.*;

/**
 * TBox expression generator.
 * @author Aaron Eberhart
 *
 * @param <T> generic
 * @param <U> generic
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class TBox<T,U> extends Box<T,U>{

	/**
	 * Predicate name tracking for preventing cycles.
	 */
	protected ArrayList<Long> names;
	
	/**
	 * Make a TBox of size elements.
	 * 
	 * @param size int
	 */
	public TBox(int size) {

		names = new ArrayList<>();
		
		this.counters = resetCounters();
		
		scope = counters[0];
		
		numSubExpansions = 0;

		makeBox(size);
		
		rand = null;
	}
	
	/**
	 * Make a TBox of the ArrayList e.
	 * 
	 * @param e ArrayList&lt;Expression&lt;T,U&gt;&gt;
	 */
	public TBox(ArrayList<Expression<T,U>> e) {
		numSubExpansions = 0;
		expressions = e;
	}
	
	/**
	 * Make a TBox expression.
	 * 
	 * @return Expression&lt;T,U&gt;
	 */
	@Override
	protected Expression<T,U> makeExpression() {
		
		rand = new RandomInteger();
		Expression<T,U> expression = new Expression<T,U>(newPredicate(rand.nextInt(2)));
		scope = (long)expression.getScope();
		
		ExpressionNode builder = expression.root;
		
		while(!expression.isComplete()) {
//			System.out.println(builder);
			builder = transform(rand.nextInt(11), builder, false,0);
//			System.out.println(builder+"\n");
			expression.root = builder;
			
			if(builder.complete)
				expression.complete = true;
			
			
		}
		
		expression.setSize(builder.getSize());
		expression.setScope((T)builder.getScope());
		
		names.clear();
		counters[0] = 2;
		
 		return expression;
	}

	/**
	 * Make a sub-expression.
	 * @param ran int
	 * @param fromSub boolean
	 * @param depth int
	 * @return Predicate&lt;T,U&gt;
	 */
	protected Predicate<T,U> newSubExpression(int ran, boolean fromSub,int depth){
		rand = new RandomInteger();
		long[] tmpCounters = this.counters;
		long tmpScope = scope;
		this.counters = resetCounters();
		
		depth = (int)(tmpCounters[0] - counters[0]);
		this.counters[0] = 2 + rand.nextInt(depth);
		
		Predicate<T,U> p = newPredicate(depth <= 0?rand.nextInt(1):rand.nextInt(2));
		ExpressionNode<T,U> builder = new ExpressionNode<T,U>(p);
		
		if((long)p.getScope() == scope)
			return p;
		
		scope = counters[0];
		
		while((long)builder.getScope() < tmpScope) {
			
			int num = rand.nextBoolean() ? rand.nextInt(4) : (rand.nextInt(3) + 8);
					
			builder = transform(num, builder,true,depth);
		}
		
		this.counters = tmpCounters;
		this.scope = tmpScope;
		return builder;
	}
	
	/**
	 * Add to or change the expression.
	 * @param randInt int
	 * @param expression ExpressionNode
	 * @param fromSub boolean
	 * @param depth int
	 * @return ExpressionNode
	 */
	protected ExpressionNode transform(int randInt, ExpressionNode expression,boolean fromSub,int depth) {

		int size = fromSub?expression.getSize()+depth:expression.getSize();
		boolean noSub = false;
		
		if(size >= maxSize / 3.0 || (scope + 2 >= quantificationDepth && scope > 0) || (scope - 2 <= (quantificationDepth * -1) && scope < 0)){
			if(!fromSub && size + 6 >= maxSize)
				randInt = (randInt % 4) + 4;
			else if(!fromSub)
				noSub = true;
			else if(fromSub) {// && ((scope >= bound && scope > 0) || (scope <= (bound * -1) && scope < 0))){
				randInt = (randInt % 2) + 8;
			}
		}
		
		switch(randInt) {
			case 0:
			case 1:
				if(noSub || numSubExpansions >= maxSubExpressions)
					expression = expression.and(newPredicate(randInt));
				else {
					Predicate p = newSubExpression(11,false,depth);
					if(p.isExpression())
						expression = expression.and((ExpressionNode)p);
					else
						expression = expression.and(p);
					numSubExpansions++;
				}
				break;
			case 2:
			case 3:
				if(noSub || numSubExpansions >= maxSubExpressions)
					expression = expression.and(newPredicate(randInt % 2));
				else {
					Predicate q = newSubExpression(11,false,depth);
					if(q.isExpression())
						expression = expression.or((ExpressionNode)q);
					else
						expression = expression.or(q);
					numSubExpansions++;
				}
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
				expression = expression.dot(new Quantifier(1), (Role)newPredicate(randInt), -1 * (makeConceptName(rand) % conceptNames));
				scope+=1;
				break;
			case 9:
				expression = expression.dot(new Quantifier(2), (Role)newPredicate(randInt), -1 * (makeConceptName(rand) % conceptNames));
				scope+=1;
				break;
			default:
				expression = expression.negate();
				break;
		}
		if(randInt > 3 && randInt < 10 && expression.getSize() > size) {
			scope = counters[0];
			expression.setScope(scope);
		}
		

		return expression;
	}
	
	/**
	 * Make a new Predicate.
	 */
	@Override
	protected Predicate newPredicate(int randInt) {
		boolean negated = rand.nextBoolean();
		Predicate p;
		long one = makeConceptName(rand);
		long two = makeRoleName(rand);
		
		if(randInt == 0) {
			p = new Concept(negated,counters[0],one);
			if(!usedBefore(one))
				names.add(one);
		}
		else if(randInt == 1) {
			
			p = new QuantifiedRole(negated,rand.nextBoolean(),rand.nextInt(2) + 1,counters[0],counters[0]-1,two,one,one);
			if(!usedBefore(one))
				names.add(one);
		}
		else {
			p = new Role(false,counters[0] + 1,counters[0],two);
			counters[0] = (counters[0] + 1);
		}
		
		return p;
	}

	/**
	 * Check if a predicate label has been used yet.
	 * 
	 * @param i long
	 * @return boolean
	 */
	public boolean usedBefore(long i) {
		for(Long l : names) {
			if(l.longValue() == i)
				return true;
		}
		return false;
	}
	
	/**
	 * Find a unique name for the left side of the expression.
	 * 
	 * @param rand RandomInteger
	 * @return long
	 */
	public long endExpression(RandomInteger rand) {
		long l = rand.nextInt(conceptNames);
		if(l == 0)
			l++;
		long initial = l;
		while(usedBefore(l)) {
			l = (l+1)%(conceptNames);
			if(l==initial) {
				return conceptNames;
			}
		}
		
		return l;
	}
	
	/**
	 * Normalize the expressions.
	 */
	@Override
	public void normalizeExpressions() {
		ArrayList normals = new ArrayList<Expression<T,U>>();
		
		for(Expression<T,U> e : expressions) {
			
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
	
	/**
	 * Helper for normalizing subset.
	 * 
	 * @param left ExpressionNode
	 * @param right ExpressionNode
	 * @return Expression
	 */
	public Expression normalizeSubset(ExpressionNode left, ExpressionNode right) {
		left.negate();
		return left.or(right);
	}
	
	@Override
	protected long[] resetCounters() {
		long [] counters = new long[1];
		counters[0] = 2;
		return counters;
	}
	
	@Override
	public String toString() {
		return "TBox = " + super.toString();
	}
	
	@Override
	public String toDLString() {
		String s = "TBox = {\n\n";
		if(expressions.size() == 0) {
			s = s + "NULL\n";
		}else {	
			int j = 0;
			for(int i = 0; i < expressions.size(); i++) {  
				if(normalized == null) {
					s = s + "\t" + expressions.get(i).toDLString();
				}
				else {	   		
					s = s + "\t" + normalized.getFromExpressionIndex(j).toDLString();
					if(expressions.get(i).root.operator == '=') {
						j++;
						s = s + "\n\t" + normalized.getFromExpressionIndex(j).toDLString();
					}
					j++;
		   		}
		   		s = s + "\n\n";
			}
		}
		return s + " }";
	}

	@Override
	public String toFSString(int tab) {
		String s = "";
		
		if(normalized == null) {
			for(int i = 0; i < expressions.size(); i++) {  
				s = s + "\n" + expressions.get(i).toFSString(0);
			}
		}
		else {
			for(int i = 0; i < normalized.normals.size(); i++) {
				s = s + "\nSubClassOf( \n\towl:Thing\n" + normalized.getFromExpressionIndex(i).toFSString(1) + "\n )";
			}
		}
		return s;
	}

}