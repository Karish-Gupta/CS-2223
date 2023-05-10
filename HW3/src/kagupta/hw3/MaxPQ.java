package kagupta.hw3;

import java.util.Arrays;

/**
 * Craft a simplified MaxPQ to store Integers and make available a key operation
 * that you will find useful for this homework assignment -- peekMax() to report
 * the maximum value in the heap without removing it.
 * 
 * COPY THIS CLASS INTO YOUR USERID.HW3 and INSTRUMENT THIS CLASS
 */
public class MaxPQ {
	private int[] pq;                    // store items at indices 1 to N (pq[0] is unused)
	private int N;                       // number of items on priority queue

	// key operations
	int numLess;
	int numExch;

	public MaxPQ(int initCapacity) {
		pq = new int[initCapacity + 1];
		N = 0;
	}

	public boolean isEmpty() { return N == 0;  }
	public int size() { return N; }

	public void insert(int x) {
		pq[++N] = x;
		swim(N);
	}

	public void resetKeyOperationsCount() {
		numExch = 0;
		numLess = 0;
	}
	
	public int keyOperations() { 
		return numExch + numLess;
	}
	
	public String toString() {       // for debugging
		return Arrays.toString(pq);
	}

	public int peekMax() {
		return pq[1];
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
		numExch++;
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
	}
}
