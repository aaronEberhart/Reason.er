package reason.er.Functions;

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
}
