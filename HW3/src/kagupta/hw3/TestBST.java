package kagupta.hw3;

import kagupta.hw3.BST;

public class TestBST {
	
	/** Print stack trace and execute if fails. */
	static void check(boolean b) {
		try {
			if (!b) {
				throw new RuntimeException("FAILURE");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		//             6              DEPTH = 0
		//           /    \
		//          2      8          DEPTH = 1
		//         / \    /
		//        1   3  7            DEPTH = 2
		//             \
		//              5             DEPTH = 3
		//             /
		//            4               DEPTH = 4
		//
		// SUM DEPTH = 1x0 + 2x1 + 3x2 = 8
		kagupta.hw3.BST b = new kagupta.hw3.BST();
		check(b.countIfEven() == 0);
		b.add(6);
		b.add(2);
		b.add(1);
		b.add(8);
		b.add(7);
		b.add(3);
		b.add(5);
		b.add(4);
		check(b.countIfEven() == 4);
		
		// not always easy to check if EXACTLY the same, but WE ARE 
		// in the same package, so WE CAN CHECK. HOWEVER, PLEASE 
		// DON'T WRITE CODE LIKE THIS EVER AGAIN...
		BST b2 = b.copy();
		check(b.root.key == b2.root.key);
		check(b.root.left.key == b2.root.left.key);
		check(b.root.right.key == b2.root.right.key);
		check(b.root.left.left.key == b2.root.left.left.key);
		check(b.root.left.right.key == b2.root.left.right.key);
		check(b.root.right.left.key == b2.root.right.left.key);
		check(b.contains(4));  // tiny check...
		check(b2.contains(4));

		int sum = 0;
		for (int i : b.nodeDepths()) {
			sum += i;
		}
		check(sum == 15);

		b2.removeLeafIfOdd();
		sum = 0;
		for (int i :  b2.nodeDepths()) {
			sum += i;
		}
		check(sum == 11);
		
		check(b2.countIfEven() == 4);
		check(b2.root.left.left == null);
		check(b2.root.right.left == null);
	}
}
