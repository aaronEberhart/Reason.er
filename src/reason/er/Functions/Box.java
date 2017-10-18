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
	protected ArrayList<Expression<T>> normals;

	protected abstract Expression<T> makeExpression();
	protected abstract Predicate<T> newPredicate(int randInt);
	
	public void normalizeExpressions() {
		normals = new ArrayList<Expression<T>>();
		
		for(Expression<T> ex : expressions) {
			normals.add(ex.normalize());
		}
	}
	
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

	public void addManually(Expression<T> e) {
		expressions.add(e);
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
    		if(normals != null) {
		    	for(int i = 0; i < normals.size(); i++) {
		    		s = s + normals.get(i).toString();
		    		if( i < normals.size() - 1)
		    			s = s + ", \n";
		    	}
    		}else {
    			for(int i = 0; i < expressions.size(); i++) {
		    		s = s + expressions.get(i).toString();
		    		if( i < expressions.size() - 1)
		    			s = s + ", \n";
		    	}
    		}
    	}
    	return s + " }";
    }

}