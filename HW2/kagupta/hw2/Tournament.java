package kagupta.hw2;

/**
 * Instrument and use to compute Best / Avg. / Worst on applying algorithm
 */
public class Tournament {
	public int[] pq;                    // store items at indices 1 to N (pq[0] is unused)
	public int N;                       // number of items on priority queue

	static int BEST    = 1;
	static int AVERAGE = 2;
	static int WORST   = 3;

	int numLess;
	int numExc;


	public Tournament(int initCapacity) {
		pq = new int[initCapacity + 1];
		N = 0;
	}

	public boolean isEmpty() { return N == 0;  }
	public int size() { return N; }

	public void insert(int x) {
		pq[++N] = x;
		swim(N);
	}

	public int delMax() {
		int max = pq[1];
		exch(1, N--);
		sink(1);
		return max;
	}

	/***************************************************************************
	 * Helper functions to restore the heap invariant.
	 ***************************************************************************/
	private void swim(int k) {
		while (k > 1 && less(k/2, k)) {
			exch(k, k/2);
			k = k/2;
		}
	}

	private void sink(int k) {
		while (2*k <= N) {
			int j = 2*k;
			if (j < N && less(j, j+1)) j++;
			if (!less(k, j)) break;
			exch(k, j);
			k = j;
		}
	}

	/***************************************************************************
	 * Helper functions for compares and swaps.
	 ***************************************************************************/
	private boolean less(int i, int j) {
		numLess++;
		return pq[i] < pq[j];
	}

	private void exch(int i, int j) {
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		numExc++;
	}

	/** TODO: YOU MUST FILL THIS IN. IT CANNOT BE RECURSIVE. */
	static int model_S(int N) {
		return 0;
	}

	static void trial(int type) {
		System.out.println(String.format("%10s %10s %10s %10s %10s %10s", "N", "Max", "Second", "#Less", "#Exch", "#Total"));
		for (int size = 64; size <= 65536; size *= 2) {
			Tournament heap = new Tournament(size);

			// once all are populated, you can immediately determine the LARGEST. with just one comparison
			// of elements heap.pq[2] and heap.pq[3] (DO YOU SEE WHY?) you can compute SECOND LARGEST
			populate(heap, size, type);


			int max = heap.pq[1];
			int second;
			if (heap.less(2,3)) {
				second = heap.pq[3];
			} else {
				second = heap.pq[2];
			}



			// INSTRUMENT ABOVE so you count total number of times two elements are compared with each other
			// and total number of times two elements are exchanged. Report below			
			System.out.println(String.format("%10d %10d %10d %10d %10d %10d", size, max, second, heap.numLess, heap.numLess, (heap.numLess+heap.numExc)));
		}
	}

	public static void main(String[] args) {
		System.out.println("========================\nBEST CASE\n========================");
		trial(BEST);
		System.out.println("========================\nAVERAGE CASE\n========================");
		trial(AVERAGE);
		System.out.println("========================\nWORST CASE\n========================");
		trial(WORST);

		System.out.println("\nModel Predicting final column in WORST Case");
		for (int size = 64; size <= 65536; size *= 2) {
			System.out.println(size + "\t" + model_S(size));
		}
	}

	/**
	 * Complete this implementation.
	 * 
	 * choose 'size' integers to insert (one at a time) for the three different cases.
	 * 
	 * You can choose what these numbers are to be. In case you want to use a random generator, you can use the
	 * given 'rnd' to generate a pseudo-random integer in the range from 0..size-1
	 * 
	 * @param heap   Tournament that is empty and ready to be populated
	 * @param size   how many integers to add
	 * @param type   whether to create the BEST case, an AVERAGE case, and the WORST case for the tournament algorithm.
	 */
	static void populate (Tournament heap, int size, int type) {
		java.util.Random rnd = new java.util.Random();  // will be useful in one of these cases...

		int decreasingVal = size;

		if (type == BEST) {
			 for (int i = 0; i < size; i++) {
			 	heap.insert(decreasingVal);
				 decreasingVal = decreasingVal - 1;
			 }

		} else if (type == AVERAGE) {
			for (int i = 0; i < size; i++) {
				heap.insert(rnd.nextInt());
			}
		} else if (type == WORST) {
			for (int i = 0; i < size; i++) {
				heap.insert(i);
			}
		}

		// by the time you return, must be 'size' in place
		if (heap.N != size) {
			throw new RuntimeException("MUST FILL UP TOURNAMENT");
		}
	}
}
