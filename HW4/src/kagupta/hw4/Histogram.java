package kagupta.hw4;

import java.util.Random;
import kagupta.hw4.AVLTreeST;

/** 
 * COPY this class into your USERID.hw4 package and complete this class.
 */
public class Histogram {

	// You will need some symbol table that you can use to store the keys
	// in such a way that you can retrieve them in order.

	AVLTreeST AVL = new AVLTreeST();
	public Histogram() {
		AVLTreeST AVL = new AVLTreeST();
	}


	/** 
	 * Increase the count for the number of times 'key' exists in the Histogram.
	 * @param key
	 */
	public void record(int key) {
		// If key does not exist
		if (AVL.get(key) == null) {
			AVL.put(key, 1);
		}
		// If key exists
		else {
			int val = (int) AVL.get(key) + 1;
			AVL.put(key, val);
		}
	}
	
	/** Return whether histogram is empty. */
	public boolean isEmpty() {
		return AVL.isEmpty();
	}
	
	/** Return the lowest integer key in the histogram. */
	public int minimum() {
		return (int) AVL.min();
	}

	/** Return the largest integer key in the histogram. */
	public int maximum() {
		return (int) AVL.max();
	}
	
	/** Return sum of counts for keys from lo (inclusive) to high (inclusive). */
	public int total(int lo, int hi) {


		int[] order = new int[AVL.size()];
		int index = 0;
		for (Object keys : AVL.keysInOrder()) {
			order[index] = (int) keys;
			index++;
		}


		int total = 0;
		for (int i = 0; i < order.length; i++) {
			if (order[i] >= lo && order[i] <= hi) {
				total += (int) AVL.get((Comparable) order[i]);
			}
		}

		return total;
	}
	
	/** Produce a report for all keys (and their counts) in ascending order of keys. */
	public void report() {
		System.out.println("Raw Histogram");

		for (Object keys : AVL.keysInOrder()) {
			if ((int) keys > 9) {
				System.out.println((int) keys + "    " + AVL.get((Comparable) keys));
			}
			else {
				System.out.println((int) keys + "     " + AVL.get((Comparable) keys));
			}
		}
	}
	
	/** 
	 * Produce a report for all bins (with aggregate counts) in ascending order by range.
	 * 
	 * The first range label that is output should be "0 - (binSize-1)" since the report always starts from 0.
	 * 
	 * It is acceptable if the final range label includes values that exceed maximum().
	 */
	public void report(int binSize) {
		System.out.println("Histogram (binSize=" + binSize + ")");

		for (int i = 0; i <= maximum(); i+= binSize) {

			// added if statement for print formatting
			if (i > 9) {
				System.out.println(String.format("%d-%d\t%d", i, i + binSize - 1, total(i, i + binSize - 1)));

			} else {
				System.out.println(String.format("%d-%d\t%5d", i, i + binSize - 1, total(i, i + binSize - 1)));
			}
		}
	}
	
	/** 
	 * Kick things off with random integers, but using a fixed generated sequence so you can reproduce.
	 * 
	 * USE THIS METHOD AS IS WITHOUT ANY CHANGES.
	 */
	public static void main(String[] args) {
		Histogram sample = new Histogram();
		Random rnd = new Random(0);
		for (int i = 0; i < 20; i++) {
			int v = rnd.nextInt(20);
			sample.record(v);
		}
		
		sample.report();
		sample.report(1);
		sample.report(5);
	}
}
