package kagupta.hw2;

/**
 * COPY this class into your USERID.hw2
 * 
 * This is a more complicated version of a question you might see on the midterm.

Your task is to modify the program so it produces the following

     N                Value               Actual            Predicted
     2                  216                 c2                 S(2)
     4                12672                 c4                 S(4)
     8               792576                 c8                 S(8)
    16             50429952                ...                 ...
    32           3222798336                ...                 ...
    64         206183596032                ...                 ...
   128       13194542186496                ...                 ...
   256      844431372582912                ...                 ...
   512    54043298607661056                ...                 ...
  1024  3458766163087982592                ...                 ...

 note: the above is revised from my initial code posted on Friday. The above is proper.
 
 Where each of the c2, c4, c8, ... is replaced by an integer that reflects the actual number of times 
 that power() was invoked during proc(a, 0, a.length-1) for arrays, a, of size 2, 4, 8, 16, ...
 The final Predicted column reflects the prediction of S(N), which you should implement below
 in the model(n) method.

 Note for the bonus question: add a fifth column that shows your prediction of Val. Implement your 
 formula in the bonusModel(int n) method and show its output (which should match Val column)

 */
public class Analysis {

	static int actual;
	
	/** 
	 * Helper method to compute base^exp as a long.
	 */
	static long power(long base, int exp) {
		actual++;
		return (long) Math.pow(base, exp);
	}
	
	/**
	 * This is the method under review. Do not change this method.
	 */
	public static long proc(int[] A, int lo, int hi) {
		long v = power(A[lo], 2) + power(A[hi], 2);
		if (lo == hi) { 
			return v + power(v, 2);
		}

		int m = (lo + hi) / 2;
		long total = proc(A, lo, m) + 3*proc(A, m+1, hi);
		while (hi > lo) {
			total += power(A[lo], 2);
			lo += 2;
		}
		
		return total;
	}
	
	/**
	 * Complete your analysis of the code and modify this function to return the prediction
	 * of how many times Math.power() is called for an initial problem of size n.
	 */
	static long model(int n) {
		return (5*n - 2 + ((n*log2(n)) / 2));
	}


	public static int log2(int x) {
		return (int) (Math.log(x) / Math.log(2));
	}
	
	/**
	 * Bonus question. Can you come up with a (more complicated) formula that predicts
	 * the value, Value, or proc(a,0,n-1) when a is composed of alternating integers 0,n,0,n, ... 
	 */
	static long bonusModel(int n) {
		return 0;   // FILL IN WITH YOUR BONUS MODEL FORMULA
	}
	
	/** 
	 * Launch the experiment.
	 * 
	 * You will need to make some changes to this method
	 */
	public static void main(String[] args) {
		System.out.println(String.format("%6s %20s %20s %20s", "N", "Value", "Actual", "Predicted"));
		for (int n = 2; n <= 1024; n *= 2) {
			actual = 0;

			int[] a = new int[n];
			for (int i = 0; i < n; i++) { if (i%2 == 1) { a[i] = n; } }   // alternating 0,n,0,n, ... values

			// initiate the request on an array of size n using indices of lo=0 and hi=n-1
			long val = proc(a, 0, n-1);
			
			// WILL NEED TO FIX BELOW
			System.out.println(String.format("%6d %20d %20d %20d", n, val, actual, model(n)));
		}
	}
}
