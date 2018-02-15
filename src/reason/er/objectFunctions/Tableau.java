package reason.er.objectFunctions;

import java.util.ArrayList;
import reason.er.compositeObjects.*;
import reason.er.objectFunctions.*;
import reason.er.objects.*;

@SuppressWarnings({ "rawtypes" , "unchecked" })
public class Tableau<T,U> extends Expression<T,U> {
	
	NormalizedBox<T,U> expressions;
	ArrayList<ArrayList<Predicate<T,U>>> model;
	ArrayList<Term<U>> blocks;
	
	public Tableau(KnowledgeBase<T,T> kb) {
		
		expressions = unifyBoxes(kb.getABox(),kb.getTBox());
		
		model = new ArrayList<>();
		blocks = new ArrayList<>();
		
	}
	
	public boolean run() {
		return true;
	}
	
	private NormalizedBox<T,U> unifyBoxes(ABox<T,U> a, TBox<T,U> t){
		ArrayList<Expression<T,U>> kb = a.copyNormals();
		kb.addAll(t.copyNormals());
		return new NormalizedBox<T,U>(kb);
	}
	
	public String toString() {
		String s = "\nModel:\n\n";
		for(ArrayList<Predicate<T,U>> l : model) {
			for(Predicate<T,U> p : l){
				s = s + p.toString() + "\n";
			}
		}
		return s;
	}

}
