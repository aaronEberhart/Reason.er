package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.objects.*;
import reason.er.util.*;

public abstract class Box <T extends Predicate<T>> {
	
	protected long scope;
	protected long counters[];
	protected RandomInteger rand;
	protected final int universe = Predicate.uppers.length,variables = Term.lowers.length / 2;;
	
	protected ArrayList<Expression<T>> expressions;
	protected NormalizedBox<T> normalized;

	protected abstract Expression<T> makeExpression();
	protected abstract Predicate<T> newPredicate(int randInt);
	protected abstract ExpressionNode<T> transform(int randInt, ExpressionNode<T> expression);
	public abstract void normalizeExpressions();
	protected abstract long[] resetCounters();
	
	protected void makeBox(int size) {
		expressions = new ArrayList<Expression<T>>(size);
		while(expressions.size() < size) {
			expressions.add(makeExpression());
		}
	}

	public void addManually(ExpressionNode<T> e) {
		expressions.add(new Expression<T>(e));
	}
	
    public int getNumVars() {
		return universe;
	}
	
    protected long makeName(RandomInteger rand) {
    	return Math.abs(rand.nextLong());
    }
    
    public ArrayList<Expression<T>> getNormals(){
		return normalized.getNormals();
	}
    
    public ArrayList<Expression<T>> copyNormals(){
		return normalized.copyNormals();
	}
    
    @Override
    public String toString() {
    	String s = "{\n\n";
    	if(expressions.size() == 0) {
    		s = s + "NULL";
    	}else {	
    		int j = 0;;
    		for(int i = 0; i < expressions.size(); i++) {  
    			if(normalized == null) {
    				s = s + "\t" +expressions.get(i).toString() + ",";
    			}
    			else {
    				s = s + "\tExpression :" + expressions.get(i).toString() + ",";		   		
	    			s = s + "\n\tNormal: " + normalized.getFromExpressionIndex(j).toString() + ",";
	    			if(expressions.get(i).root.operator == '=') {
	    				j++;
	    				s = s + "\n\tNormal: " + normalized.getFromExpressionIndex(j).toString() + ",";
	    			}
	    			j++;
		   		}
		   		s = s + "\n\n";
    		}
    	}
    	return s + " }";
    }

}