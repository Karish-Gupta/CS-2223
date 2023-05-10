package kagupta.hw3;

import edu.princeton.cs.algs4.Queue;

/**
 * MINIMAL BST that just stores integers as the key and values are eliminated.
 * Note that duplicate values can exist (i.e., this is not a symbol table).
 * 
 * COPY this file into your USERID.hw3 package and complete the final four methods 
 * in this class.
 */
public class BST {
	// root of the tree
	Node root;
	
	// Use Node class as is without any change.
	class Node {
		int    key;          // SIMPLIFIED to just use int
		Node   left, right;  // left and right subtrees

		public Node(int key) {
			this.key = key;
		}
		
		public String toString() { return "[" + key + "]"; }
	}

	/** Check if BST is empty. */
	public boolean isEmpty() { return root == null; }

	/** Determine if key is contained. */ 
	public boolean contains(int key) { 
		return contains(root, key);
	}
	
	/** Recursive helper method for contains. */
	boolean contains(Node parent, int key) {
		if (parent == null) return false;
		
		if (key < parent.key) {
			return contains(parent.left, key);
		} else if (key > parent.key) {
			return contains(parent.right, key);
		} else {
			return true; // found it!
		}
	}
	
	/** Invoke add on parent, should it exist. */
	public void add(int key) { 
		root = add(root, key);
	}

	/** Recursive helper method for add. */
	Node add(Node parent, int key) {
		if (parent == null) {
			return new Node(key);
		}
		
		if (key <= parent.key) {
			parent.left = add(parent.left, key);
		} else if (key > parent.key) {
			parent.right = add(parent.right, key); 
		} 
		
		return parent;
	}
	
	// AFTER THIS POINT YOU CAN ADD CODE....
	// ----------------------------------------------------------------------------------------------------

	/** Return a new BST that is a structural copy of this current BST. */

	public BST copy() {
		BST replicaBST = new BST();
		replicaBST.root = copy(this.root, replicaBST.root);
		return replicaBST;
	}

	/** copy helper method for recursion */
	public Node copy(Node original, Node replica) {
		// base case null
		if (original == null) { return null;}

		Node newNode = new Node(original.key);
		replica = newNode;

		replica.left = copy(original.left, replica.left);
		replica.right = copy(original.right, replica.right);
		return replica;
	}
	
	/** Return the count of nodes in the BST whose key is even. */
	public int countIfEven() {
		return countIfEven(this.root, 0);
	}

	/**CountIfEven helper method for recursion */
	public int countIfEven(Node node, int count) {
		if(node == null) { return count;}

		if (node.key % 2 == 0) { count++;}

		count = countIfEven(node.left, count);
		count = countIfEven(node.right, count);

		return count;
	}

	/** Return a Queue<Integer> containing the depths for all nodes in the BST. */
	public Queue<Integer> nodeDepths() {
		Queue<Integer> queue = new Queue<>();
		if (this.root == null) { return queue;}

		nodeDepths(this.root, queue, 0);
		return queue;
	}

	/** NodeDepths helper method for recursion */
	public void nodeDepths(Node node, Queue queue, int depth) {
		if (node == null) { return; }
		queue.enqueue(depth);
		depth++;

		nodeDepths(node.left, queue, depth);
		nodeDepths(node.right, queue, depth);
	}
	
	/** Remove all leaf nodes that are odd. */
	public void removeLeafIfOdd() {
		root = removeLeafIfOdd(this.root);
	}

	/** RemoveLeafIfOdd helper method for recursion */
	public Node removeLeafIfOdd(Node node) {
		if(node == null) { return null;}

		if (node.key % 2 == 0 || node.left != null || node.right != null) {
			Node replica = new Node(node.key);

			replica.left = removeLeafIfOdd(node.left);
			replica.right = removeLeafIfOdd(node.right);

			return replica;
		}
		else {
			return null;
		}
	}
}
