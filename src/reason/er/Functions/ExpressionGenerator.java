package reason.er.Functions;

import java.util.ArrayList;
import java.util.Random;

import reason.er.Objects.*;

public abstract class ExpressionGenerator<T extends Expression<T>> {
	
	protected static final char[] uppers = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	protected static final char[] lowers = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	protected String scope;
	protected int universe, variables;
	protected int counters[];
	protected Random rand;
	
	protected ArrayList<Expression<T>> expressions;

	protected abstract Expression<T> makeExpression();
	protected abstract Predicate<T> newPredicate(int randInt);
	
	protected void makeBox(int size) {
		expressions = new ArrayList<Expression<T>>();
		while(expressions.size() < size) {
			expressions.add(makeExpression());
			try {
				Thread.sleep(90);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rand.setSeed(System.currentTimeMillis());
			
		}
	}

	protected String makeVariable(long count) {
    	String s = "";
    	
    	do{
    		s = Character.toString(lowers[(((int)count + lowers.length) % lowers.length)]) + s;
    		count = count / lowers.length;
    	}while(count-- > 0);
    	
    	return s;
    }
	
    protected String makeLabel(long count) {
    	String s = "";
    	
    	do{
    		s = Character.toString(uppers[(int)(count % uppers.length)]) + s;
    		count = count / uppers.length;
    	}while(count-- > 0);
    	
    	return s;
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
	    	for(int i = 0; i < expressions.size(); i++) {
	    		s = s + expressions.get(i).toString();
	    		if( i < expressions.size() - 1)
	    			s = s + ", \n";
	    	}
    	}
    	return s + " }";
    }
}