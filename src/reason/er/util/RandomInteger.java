package reason.er.util;

/**
 * This class was adapted from an example I found online. 
 * Methods that are from the example are labeled "Original".
 * Methods that I wrote are labeled "New".
 * 
 * 
 * @author Original Code : Neil Coffey : https://www.javamex.com/tutorials/random_numbers/numerical_recipes.shtml
 * @author <br>New Code : Aaron Eberhart
 **/
public class RandomInteger {

		private long u;
		private long v = 4101842887655102017L;
		private long w = 1;

		/**
		 * Original
		 */
		public RandomInteger() {
			this(System.nanoTime());
		}

		/**
		 * Original
		 * 
		 * @param seed long
		 */
		public RandomInteger(long seed) {
			u = seed ^ v;
			nextLong();
			v = u;
			nextLong();
			w = v;
			nextLong();
		}

		/**
		 * Original
		 * @return long
		 */
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
			} finally {}
		}

		/**
		 * New
		 * Returns a positive integer in the range [0,bound)
		 * @param bound int
		 * @return int
		 */
		public int nextInt(int bound) {
			int val = (int)nextLong();
			val = val < 0 ? val *= -1 : val;
			return bound ==  0 ? 0 : val % bound;
		}
		
		/**
		 * New
		 * Generates a random integer in the range [0,2).
		 * Returns true if 0, false if 1.
		 * @return boolean 1:1
		 */
		public boolean nextBoolean() {
			return nextInt(2)==0?true:false;
		}
		
		/**
		 * New<br>
		 * Generates a random integer in the range [0,upper).<br>
		 * If the integer is greater than or equal to lower return true, false otherwise.<br>
		 * The odds of true should be approximately: 1 - (lower / upper)<br>
		 * If lower is greater than or equal to upper, this method will ALWAYS
		 * return false.
		 * @param upper int
		 * @param lower int
		 * @return boolean 
		 */
		public boolean weightedBool(int upper, int lower) {
			return nextInt(upper)>=lower?true:false;
		}
		
		/**
		 * Original
		 * @param bits int
		 * @return int
		 */
		protected int next(int bits) {
			return (int) (nextLong() >>> (64-bits));
		}


}
