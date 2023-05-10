package kagupta.hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

/**
 * COPY this class into your USERID.hw2 but ONLY FOR BONUS
 * 
 * Use a memory-efficient algorithm to compute largest and second largest, with
 * least number of comparisons (as identified by Tournament algorithm on day 2 of class).
 * 
 * Please do not even attempt this until done with entire assignment. This took 5+ hours to complete over several days.
 * 
 * Talk to me if this seems confusing. Again, bonus question only.
 * 
 * @Param T
 */
public abstract class BonusFirstSecondLargest<T extends Comparable<T>> {

	// since we are using generics, need a placeholder (to be implemented by subclass) for returning
	// an instance that is lower than any of the ones being compared.
	abstract T lowest();

	long numComparisons = 0;

	// You can use up to O(n) extra storage.

	// return solution as array of two values.
	T[] solution = (T[]) new Comparable[2];

	// get computed largest
	public T getLargest() {
		return solution[0];
	}

	// get second computed largest
	public T getSecondLargest() {
		return solution[1];
	}

	public BonusFirstSecondLargest(T[] A) {
		// everything can be done in the constructor... Once done, solution[] is properly determined.
		solution[0] = A[0]; // HACK! REPLACE WITH REAL CODE
		solution[1] = A[1];
	}

	public static void main(String[] args) {
		// most comparisons for 10,000 random trials.
		String whichCase = "worst";  // change to "worst"
		
		System.out.println("N\tCmpTour\tCmpNaiv\tTimeT\tTimeN");
		for (int num = 64; num <= 65536*4; num *= 2) {
			long max = 0;
			int numComparisons = 0;
			double totalTourn = 0;
			double totalNaive = 0;
			for (int t = 0; t < 10000; t++) {
				int[] sample_int;
				
				if (whichCase.equals("average")) {
					sample_int = StdRandom.permutation(num);
				} else {
					sample_int = new int[num];
					for (int xx = 0; xx < sample_int.length; xx++) { sample_int[xx] = num-xx-1; } // in descending order.
				}
				
				Integer[] sample = new Integer[sample_int.length];
				for (int xx = 0; xx < sample.length; xx++ ) { sample[xx] = sample_int[xx]; }
				
				StopwatchCPU timer = new StopwatchCPU();
				BonusFirstSecondLargest<Integer> fsl = new BonusFirstSecondLargest<Integer>(sample) {
					@Override
					Integer lowest() { return Integer.MIN_VALUE; }
				};
				totalTourn += timer.elapsedTime();
				
				timer = new StopwatchCPU();
				int first = sample_int[0];
				int second = sample_int[1];
				numComparisons = 1;  
				if (second > first) {
					int tmp = second;
					second = first;
					first = tmp;
				}
				for (int i = 2; i < num; i++) {
					if (sample_int[i] > first) {
						numComparisons++;
						second = first;
						first  = sample_int[i];
					} else if (sample_int[i] > second) {
						numComparisons += 2;   // SNEAKY
						second = sample_int[i];
					} else {
						numComparisons += 2;   // EXTRA SNEAKY!
					}
				}
				totalNaive += timer.elapsedTime();
				
				int check[] = new int[2];
				check[0] = fsl.getLargest();
				check[1] = fsl.getSecondLargest();
				if (check[0] != num-1 && check[1] != num-2) {
					System.err.println("INVALID RESULTS: ");
				}
				if (fsl.numComparisons > max) {
					max = fsl.numComparisons;
				}
			}
			System.out.println(num + "\t" + max + "\t" + numComparisons + "\t" + totalTourn + "\t" + totalNaive);
		}

	}
	
	
}
