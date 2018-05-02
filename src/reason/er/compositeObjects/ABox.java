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
	
	protected boolean complete, constants;
	
	/**
	 * Make an ABox of size.
	 * @param size int
	 */
	public ABox(int size) {
				
		rand = new RandomInteger();
		
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
	 * Make an ABox expression.
	 */
	@Override
	protected Expression<T,U> makeExpression() {
				
		//initialize rand
		rand = new RandomInteger();
		
		constants = rand.weightedBool(10000,1000);
		complete = false;
 		
		int randInt = rand.nextInt(3);
		
		if(randInt == 2)
			randInt++;
		
		if (randInt == 3 || randInt ==  0) {//if it decided to make a Role or a Constant
			constants = true;
			complete = true;
		}
		
		//make the first term
		Expression<T,U> expression = new Expression<T,U>(newPredicate(randInt));
		ExpressionNode builder = expression.root;
		scope = (long)expression.getScope();
		
		//loop until ground and complete
		while(!(complete && constants)){

			builder = transform(rand.nextInt(7),builder,false,0);

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
				
		return expression;
	}
	
	/**
	 * Mutate the ABox.
	 * @param randInt int
	 * @param expression ExpressionNode
	 * @param fromSub boolean
	 * @param depth int
	 * @return ExpressionNode
	 */
	protected ExpressionNode transform(int randInt, ExpressionNode expression,boolean fromSub,int depth) {
		
		//I'll need this to see if it grew
		int size = expression.getSize();
		
		//just in case it's getting out of hand
		if(((scope + 2 >= quantificationDepth && scope > 0) || (scope - 2 <= (quantificationDepth * -1) && scope < 0) || size >= maxSize / 3.0)){
			constants = fromSub?constants:true;
			randInt = (randInt % 2) + 5;
			complete = ((scope >= quantificationDepth && scope > 0) || (scope <= (quantificationDepth * -1) && scope < 0) || size + 5 >= maxSize)?true:complete;
		}
		//is it time to stop?
		if(constants) {
			expression.complete = !complete||scope>0?rand.nextBoolean():complete;
			randInt = scope<0?randInt % 5:(randInt%2)+5;
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
				if(constants || numSubExpansions >= maxSubExpressions || size + 15 >= maxSize)
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
				if(constants || numSubExpansions >= maxSubExpressions || size + 15 >= maxSize)
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
		
		scope = (long)expression.getScope();
		
		return expression;
	}
	
	/**
	 * Makes a new sub-expression.
	 * @param ran int
	 * @param fromSub boolean
	 * @param depth int
	 * @return Predicate&lt;T,U&gt;
	 */
	protected Predicate<T,U> newSubExpression(int ran,boolean fromSub,int depth){
		
		rand = new RandomInteger();
		
		long tmpScope = scope;		
		
		long[] tmpCounters = this.counters;
		this.counters = resetCounters();
		
		if(constants)
			this.counters[0] = tmpCounters[0];
		
		boolean tmpConst = constants;
		constants = false;
		Predicate<T,U> p = newPredicate(rand.nextInt(2));
		
		if((long)p.getScope() == tmpScope)
			return p;
		
		scope = counters[1];
		ExpressionNode<T,U> builder = new ExpressionNode<T,U>(p);
		
		while((long)builder.getScope() < tmpScope && constants && !(((long)p.getScope()<0 && scope<0) || (long)p.getScope() == scope)) {
			builder = transform(rand.nextInt(ran), builder,true,depth);
			constants = (!tmpConst&&constants) ? false : constants;
		}
		if(tmpConst) {
			while((long)builder.getScope() < tmpScope && !constants) {
				builder = transform(rand.nextInt(ran), builder,true,depth);
				constants = (!tmpConst&&constants) ? false : constants;
			}
		}
		else {
			while((long)builder.getScope() < tmpScope) {
				builder = transform(rand.nextInt(ran), builder,true,depth);
				constants = false;
			}
		}
		
		this.counters = tmpCounters;
		this.scope = tmpScope;
		constants = tmpConst;
		return builder;
	}
	
	/**
 	 * Make a new Predicate.
 	 */
 	@Override
 	protected Predicate newPredicate(int randInt) {
 		boolean negated = rand.nextBoolean();
 		Predicate p;
 		long one = (makeName(rand));
 		long two = -1 * (makeName(rand)+1);
 		
 		if(randInt == 0) {
 			p = new Concept(negated,constants?counters[0]:counters[1],one);
 		}
 		else if(randInt == 1) {
 			
 			p = new QuantifiedRole(negated,rand.nextBoolean(),rand.nextInt(2) + 1,constants?counters[0]:counters[1],constants?counters[1]:counters[1]-1,two,one,one);
 		}
 		else if(randInt == 2) {
 			long num1 = constants?(-1 * rand.nextInt(variables)) - 1:scope+1;
 			long num2 = constants?counters[0]:scope;
 			
 			if(constants) {
 				num2 = scope<0?counters[0]:counters[1];
 				scope = num1;
 			}
 			
 			p = new Role(false,num1,num2,two);
 			if(!constants) {
 				counters[1] = (counters[1] + 1);
 			}else {
 				counters[0] = num1;
 			}
 		}
 		else {			
 			p = new Role(negated,counters[0],(long)(-1 * rand.nextInt(variables)) - 1,two);
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
	public String toDLString() {
		String s = "ABox = {\n\n";
		if(normalized == null) {
			for(Expression e : expressions) {
				s = s + "\t" + String.format("%7s%s\t%s", 
						(!(e.getSize() <= 2 && ((Predicate)(e.root.leaf)).isRole())  ? Term.makeVariable((long)e.getScope()) : e.toString().split("\\(")[1].split("\\)")[0])
						,":", (!(e.getSize() <= 2 && ((Predicate)(e.root.leaf)).isRole()) ? e.toDLString() : e.toString().split("\\(")[0] )) + "\n";
			}
		}
		else {
			for(Expression e : normalized.normals) {
				s = s + "\t" + String.format("%7s%s\t%s", 
						(!(e.getSize() <= 2 && ((Predicate)(e.root.leaf)).isRole()) ? Term.makeVariable((long)e.getScope()) : e.toString().split("\\(")[1].split("\\)")[0])
						,":", (!(e.getSize() <= 2 && ((Predicate)(e.root.leaf)).isRole()) ? e.toDLString() : e.toString().split("\\(")[0] )) + "\n";
			}
		}
		return s;
	}

	@Override
	public String toFSString(int tab) {
		String s = "";
		for(int i = -1; i > -1*variables - 1; i--) {
			s = s + "Declaration( NamedIndividual( :" + Term.makeVariable(i) + " ) )\n";
		}
		for(int i = 0; i < (1 + universe * 2); i++) {
			s = s + "Declaration( " + (i <= universe ? "Class( :" + Predicate.makeLabel(i) : "ObjectProperty( :" + Predicate.makeLabel((universe - i)) )   + " ) )\n";
		}
		s=s+"\n";
		if(normalized == null) {
			for(int i = 0; i < expressions.size(); i++) {  
				if(expressions.get(i).getSize() < 3 && ((Predicate)(expressions.get(i).root.leaf)).isRole() && ((long)((Role)(expressions.get(i).root.leaf)).getTerm(1).getValue()) < 0) {
					s = s + (((Role)(expressions.get(i).root.leaf)).isNegated() ? "Negative" : "") 
							+ "ObjectPropertyAssertion( \n" 
							+ (((Predicate)(expressions.get(i).root.leaf)).isNegated() ? (expressions.get(i)).clone(expressions.get(i)).negate().toFSString(tab+1): expressions.get(i).root.toFSString(tab+1))
							+ "\n\t:" + Term.makeVariable(((long)((Role)(expressions.get(i).root.leaf)).getTerm(0).getValue())) 
							+ "\n\t:" + Term.makeVariable(((long)((Role)(expressions.get(i).root.leaf)).getTerm(1).getValue())) + "\n )\n";
				}
				else {
					s = s + "ClassAssertion( \n" + expressions.get(i).root.toFSString(tab+1) + "\n\t:" + Term.makeVariable(((long)((Predicate)(expressions.get(i).root)).getScope())) + "\n )\n";
				}
			}
		}
		else {
			for(int i = 0; i < normalized.normals.size(); i++) {
				if(normalized.normals.get(i).getSize() < 3 && ((Predicate)(normalized.normals.get(i).root.leaf)).isRole()&&((long)((Role)(normalized.normals.get(i).root.leaf)).getTerm(1).getValue()) < 0) {
					s = s + 
							(((Role)(normalized.normals.get(i).root.leaf)).isNegated() ? "Negative" : "")
							+ "ObjectPropertyAssertion( \n" 
							+ ((normalized.normals.get(i).root.leaf).isNegated() ? ((normalized.normals.get(i)).clone(normalized.normals.get(i)).negate()).toFSString(tab+1): normalized.normals.get(i).root.toFSString(tab+1))
							+ "\n\t:" + Term.makeVariable(((long)((Role)(normalized.normals.get(i).root.leaf)).getTerm(0).getValue())) 
							+ "\n\t:" + Term.makeVariable(((long)((Role)(normalized.normals.get(i).root.leaf)).getTerm(1).getValue())) + "\n )\n";
				}
				else {
					s = s + "ClassAssertion( \n" + normalized.normals.get(i).root.toFSString(tab+1) + "\n\t:" + Term.makeVariable(((long)((Predicate)(normalized.normals.get(i).root)).getScope())) + "\n )\n";
				}
			}
		}
		return s;
	}
	
	@Override
	protected long[] resetCounters() {
		long counters[] = new long[2];
		counters[0] = (-1 * rand.nextInt(variables)) - 1;
		counters[1] = 2;
		return counters;
	}

}
