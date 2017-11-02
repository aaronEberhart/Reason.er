package reason.er.Functions;

import java.util.ArrayList;

import reason.er.Objects.Concept;
import reason.er.Objects.QuantifiedRole;
import reason.er.Objects.Quantifier;
import reason.er.Objects.Role;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class KnowledgeBase<T extends Expression<T>, U extends Expression<U>> {
	
	private ABox<T> abox;
	private TBox<U> tbox;
	
	public KnowledgeBase(int aboxSize, int tboxSize) {
		
		tbox = new TBox<U>(tboxSize);
		abox = new ABox<T>(aboxSize);
	}
	
	public KnowledgeBase(ABox<T> a, TBox<U> t) {
		tbox = t;
		abox = a;
	}
	
	public void normalize() {
		tbox.normalizeExpressions();
		abox.normalizeExpressions();
	}
	
	@Override
	public String toString() {
		return "K = ( ABox, TBox )\n\n" + abox.toString() + "\n\n" + tbox.toString() + "\n";
	}
	
	public static <T extends Expression<T>,U extends Expression <U>> KnowledgeBase<T,U> makeTestKnowledgeBase() {
    	
		ABox<T> abox = new ABox(new ArrayList<T>());
    	
    	abox.addManually(new Expression(new Role(false,(long)0,(long)1,(long)17)));
    	
		TBox<U> tbox = new TBox(new ArrayList<U>());
    	
    	tbox.addManually(new Expression(new QuantifiedRole(false,1,(long)23,(long)22,(long)19,(long)0,(long)19)).negate().dot(new Quantifier(2),new Role(false,(long)24,(long)23,(long)18),(long)3)
    			.and(new Concept(false,(long)24,(long)1)).negate().equivalent(new Concept(false,(long)24,(long)2)));
    	tbox.addManually(new Expression(new QuantifiedRole(false,1,(long)23,(long)22,(long)19,(long)0,(long)19)).negate().dot(new Quantifier(2),new Role(false,(long)24,(long)23,(long)18),(long)3)
    			.and(new Concept(false,(long)24,(long)1)).negate().superClass(new Concept(false,(long)24,(long)2)));
    	
    	return new KnowledgeBase(abox,tbox);
    }
}
