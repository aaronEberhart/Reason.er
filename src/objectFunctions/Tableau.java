package objectFunctions;

import java.util.ArrayList;

import reason.er.compositeObjects.ABox;
import reason.er.compositeObjects.Expression;
import reason.er.compositeObjects.TBox;
import reason.er.objects.*;

public class Tableau <T extends Expression<T>> {
	
	ArrayList<ArrayList<Predicate<T>>> model;
	ArrayList<Term<T>> blocks;
	
	public Tableau(ABox<T> a, TBox<T> t) {
		model = new ArrayList<>();
		blocks = new ArrayList<>();
		
		ArrayList<Expression<T>> kb = unifyBoxes(a,t);
		
		
	}
	
	
	
	private ArrayList<Expression<T>> unifyBoxes(ABox<T> a, TBox<T> t){
		ArrayList<Expression<T>> kb = a.getNormals();
		kb.addAll(t.getNormals());
		return kb;
	}
	
	public String toString() {
		String s = "\nModel:\n\n";
		for(ArrayList<Predicate<T>> l : model) {
			for(Predicate<T> p : l){
				s = s + p.toString() + "\n";
			}
		}
		return s;
	}
}
