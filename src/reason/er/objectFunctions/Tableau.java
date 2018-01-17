package reason.er.objectFunctions;

import java.util.ArrayList;
import reason.er.compositeObjects.*;
import reason.er.objectFunctions.*;
import reason.er.objects.*;

@SuppressWarnings({ "rawtypes" , "unchecked" })
public class Tableau <T extends Expression<T>> {
	
	NormalizedBox<T> expressions;
	ArrayList<ArrayList<Predicate<T>>> model;
	ArrayList<Term<T>> blocks;
	
	public Tableau(KnowledgeBase<T,T> kb) {
		
		expressions = unifyBoxes(kb.getABox(),kb.getTBox());
		
		model = new ArrayList<>();
		blocks = new ArrayList<>();
		
	}
	
	public boolean run() {
		return true;
	}
	
	private NormalizedBox<T> unifyBoxes(ABox<T> a, TBox<T> t){
		ArrayList<Expression<T>> kb = a.copyNormals();
		kb.addAll(t.copyNormals());
		return new NormalizedBox<T>(kb);
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
