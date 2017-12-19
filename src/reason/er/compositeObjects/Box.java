package reason.er.compositeObjects;

import java.util.ArrayList;
import java.util.Random;

import reason.er.objects.*;

public abstract class Box <T extends Predicate<T>> {
	
	//TODO enable adding duplicate concept/role names to expressions (not the head)
	
	protected long scope;
	protected int universe, variables;
	protected long counters[];
	protected Random rand;
	
	protected ArrayList<Expression<T>> expressions;
	protected NormalizedBox<T> normalized;

	protected abstract Expression<T> makeExpression();
	protected abstract Predicate<T> newPredicate(int randInt);
	
	public abstract void normalizeExpressions();
	
	protected void makeBox(int size) {
		expressions = new ArrayList<Expression<T>>();
		while(expressions.size() < size) {
			expressions.add(makeExpression());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rand.setSeed(System.currentTimeMillis());
			
		}
	}

	public void addManually(ExpressionNode<T> e) {
		expressions.add(new Expression<T>(e));
	}
	
    public int getNumVars() {
		return universe;
	}
	
    protected long makeName(Random rand) {
    	return Math.abs(rand.nextLong()) % (universe);
    }
    
    public ArrayList<Expression<T>> getNormals(){
		return normalized.getNormals();
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