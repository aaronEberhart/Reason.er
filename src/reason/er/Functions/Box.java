package reason.er.Functions;

import java.util.ArrayList;
import java.util.Random;

import reason.er.Objects.*;

public abstract class Box<T extends Expression<T>> {
	
	protected long scope;
	protected int universe, variables;
	protected long counters[];
	protected Random rand;
	
	protected ArrayList<Expression<T>> expressions;
	protected Normals<T> normalized;

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

	public void addManually(ExpressionNode e) {
		expressions.add(new Expression<T>(e));
	}
	
    public int getNumVars() {
		return universe;
	}
	
    @Override
    public String toString() {
    	String s = "{ ";
    	if(expressions.size() == 0) {
    		s = s + "NULL";
    	}else {	
    		int j = 0;;
    		for(int i = 0; i < expressions.size(); i++) {  
    			if(normalized == null) {
    				s = s + expressions.get(i).toString() + ",";
    			}
    			else {
    				s = s + "Expression :" + expressions.get(i).toString() + ",";		   		
	    			s = s + "\nNormal: " + normalized.getFromExpressionIndex(j).toString() + ",";
	    			if(expressions.get(i).root.operator == '=') {
	    				j++;
	    				s = s + "\nNormal: " + normalized.getFromExpressionIndex(j).toString() + ",";
	    			}
	    			j++;
		   		}
		   		s = s + "\n\n";
    		}
    	}
    	return s + " }";
    }

}