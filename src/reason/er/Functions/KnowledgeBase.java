package reason.er.Functions;

public class KnowledgeBase<T extends Expression<T>, U extends Expression<U>> {
	
	private ABox<T> abox;
	private TBox<U> tbox;
	
	public KnowledgeBase(int aboxSize, int tboxSize) {
		
		tbox = new TBox<U>(tboxSize);
		abox = new ABox<T>(aboxSize);
	}
	
	@Override
	public String toString() {
		return "K = ( ABox, TBox )\n\n" + abox.toString() + "\n\n" + tbox.toString() + "\n";
	}
}
