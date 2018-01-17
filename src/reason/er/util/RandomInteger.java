package reason.er.util;

/**
 * 
 * @author https://www.javamex.com/tutorials/random_numbers/numerical_recipes.shtml
 *
 **/

public class RandomInteger {

	  private long u;
	  private long v = 4101842887655102017L;
	  private long w = 1;
	  
	  public RandomInteger() {
	    this(System.nanoTime());
	  }
	  
	  public RandomInteger(long seed) {
	    u = seed ^ v;
	    nextLong();
	    v = u;
	    nextLong();
	    w = v;
	    nextLong();
	  }
	  
	  public long nextLong() {
	    try {
	      u = u * 2862933555777941757L + 7046029254386353087L;
	      v ^= v >>> 17;
	      v ^= v << 31;
	      v ^= v >>> 8;
	      w = 4294957665L * (w & 0xffffffff) + (w >>> 32);
	      long x = u ^ (u << 21);
	      x ^= x >>> 35;
	      x ^= x << 4;
	      long ret = (x + v) ^ w;
	      return ret;
	    } finally {
	    }
	  }
	  
	  protected int next(int bits) {
	    return (int) (nextLong() >>> (64-bits));
	  }


}
