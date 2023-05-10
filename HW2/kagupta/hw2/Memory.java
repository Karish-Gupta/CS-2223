package kagupta.hw2;

import com.sun.org.apache.xerces.internal.impl.xs.models.XSAllCM;
import edu.princeton.cs.algs4.AllowFilter;

import java.util.Arrays;

/**
 * COPY this class into your USERID.hw2
 *
 * Responsible for allocating memory from within a designated block of chars.
 *
 * Can reallocate memory (and copy existing chars to smaller or larger destination).
 *
 * Can defragment available by combining neighboring regions together. ONLY possible if the blocks
 * of allocated memory appear in sorted order within the available list (worth five points on this question).
 *
 * Can alert user that excess memory remains unfree'd
 *
 * Address ZERO is always invalid.
 */
public class Memory {

	/** USE THIS StorageNode CLASS AS IS. */
	class StorageNode {
		int           addr;        // address into storage[] array
		int           numChars;    // how many chars are allocated

		StorageNode   next;        // the next StorageNode in linked list.

		/** Allocates a new Storage Node. */
		public StorageNode (int addr, int numChars) {
			this.addr = addr;
			this.numChars = numChars;
			this.next = null;
		}

		/** Allocates a new Storage Node and makes it head of the linked list, next. */
		public StorageNode (int addr, int numChars, StorageNode next) {
			this.addr = addr;
			this.numChars = numChars;
			this.next = next;
		}

		/** Helper toString() method. */
		public String toString() {
			return "[" + addr + " @ " + numChars + " = " + (addr+numChars-1) + "]";
		}
	}

	/** Storage of char[] that this class manages. */
	final char[] storage;
	int numAlloc;
	int numAvail;

	StorageNode allocated;
	StorageNode available;


	// YOU CAN ADD FIELDS HERE

	public Memory(int N) {
		// memory address 0 is not valid, so make array N+1 in size and never use address 0.
		storage = new char[N+1];
		available = new StorageNode(1, N);	// numChar of size N b/c
		allocated = new StorageNode(1, 0);

		numAvail = N;
		numAlloc = 0;

	}

	/**
	 * Make a useful debug() method.
	 *
	 * You should print information about the AVAILABLE memory chunks and the ALLOCATED memory chunks.
	 *
	 * This will prove to be quite useful during debugging.
	 */
	public String toString() {
		StorageNode nextAlloc = allocated;
		StorageNode nextAvail = available;

		while (nextAlloc.next != null) {
			System.out.println("[" + nextAlloc.addr + " @ " + nextAlloc.numChars + " = " + (nextAlloc.addr + nextAlloc.numChars-1) + "]");
			nextAlloc = nextAlloc.next;
		}
		System.out.println("-----------------------------------------------");
		while (nextAvail.next != null) {
			System.out.println("[" + nextAvail.addr + " @ " + nextAvail.numChars + " = " + (nextAvail.addr + nextAvail.numChars-1) + "]");
			nextAvail = nextAvail.next;
		}

		return "Done";
	}

	/**
	 * Report on # of StorageNode in allocated list (used for testing/debugging)
	 */
	public int blocksAllocated() {
		throw new RuntimeException("COMPLETE IMPLEMENTATION");
	}

	/**
	 *  Report on # of StorageNode in available list (used for testing/debugging)
	 */
	public int blocksAvailable() {
		throw new RuntimeException("COMPLETE IMPLEMENTATION");
	}

	/**
	 * Report on memory that was allocated but not free'd.
	 * Performance must be O(1).
	 */
	public int charsAllocated() {
		return numAlloc;
	}

	/**
	 * Report on available memory remaining to be allocated.
	 * Performance must be O(1).
	 */
	public int charsAvailable() {
		return numAvail;
	}

	/**
	 * Return the char at the given address.
	 * Unprotected: can return char for any address of memory.
	 */
	public char getChar(int addr) {
		return storage[addr];

	}

	/**
	 * Get char[] at the given address for given number of chars, if valid.
	 * Unprotected: can return char[] for any address of memory.
	 * Awkward that you do not have ability to know IN ADVANCE whether this many
	 * characters are stored there, but a runtime exception will tell you.
	 */
	public char[] getChars(int addr, int numChars) {
		char[] get = new char[numChars];

		for (int i = 0; i < numChars; i++) {
			get[i] = storage[addr + i];
		}

		return get;
	}

	/**
	 * Determines if the current address is valid allocation. Throws Runtime Exception if not.
	 * Performance proportional to number of allocated blocks.
	 */
	void validateAllocated(int addr) {
		validateAllocated(addr, 1);
	}

	/** Determines if the current address is valid allocation for the given number of characters. */
	void validateAllocated(int addr, int numChar) throws RuntimeException {

		StorageNode nextAlloc = allocated;
		int n = 0; // set constant to 0 by default

		while (nextAlloc != null) {
			if (nextAlloc.addr <= (addr + nextAlloc.numChars) && addr >= nextAlloc.addr) {
				if (nextAlloc.numChars >= numChar) {
					n = 1; // changed constant means this is a valid allocation
				}
			}

			nextAlloc = nextAlloc.next;
		}
		if (n == 0) throw new RuntimeException();
	}

	/**
	 * Internally allocates given memory if possible and return its starting address.
	 *
	 * Must ZERO out all memory that is allocated.
	 * @param numChars   number of consecutive char to be allocated
	 */
	public int alloc(int numChars) throws RuntimeException {
		StorageNode nextAvail = available;
		StorageNode newNode = new StorageNode(0,0);

		if (nextAvail == null) {
			throw new RuntimeException("Full");
		}
		while (nextAvail != null) {

			if (nextAvail.numChars >= numChars) {
				// insert the new node at the head of the linked list
				newNode.next = allocated;
				allocated = newNode;

				// input addr and numchar
				newNode.addr = nextAvail.addr;
				newNode.numChars = numChars;

				// fill in the memory with "\0" for the correct location
				for (int i = newNode.addr; i < newNode.numChars + 1; i++) {
					storage[i] = '\0';
				}

				// update available
				nextAvail.addr = nextAvail.addr + numChars;
				nextAvail.numChars = nextAvail.numChars - numChars;


				numAlloc += numChars;
				numAvail -= numChars;

				// ensure that when memory is full, there are no nodes.
				if (nextAvail.numChars == 0) {
					available = null;
				}

				return newNode.addr;
			}

			nextAvail = nextAvail.next;

		}
		throw new RuntimeException("Improper LL");
	}

	/** Reallocate to larger or smaller space and copy existing chars there, while free'ing the old memory. */
	public int realloc(int addr, int newSize) {
		StorageNode nextAlloc = allocated;

		// validate allocated address
		validateAllocated(addr);


		// find target
		while (nextAlloc != null) {
			if (nextAlloc.addr == addr) {
				// copy minimum number of values from old size to new size
				int newAddr = copyAlloc(Arrays.copyOf(getChars(addr, nextAlloc.numChars), newSize));

				// free old block
				free(addr);

				return newAddr;
			}
			nextAlloc = nextAlloc.next;
		}
		return -1;

	}

	/**
	 * Internally allocates sufficient memory in which to copy the given char[]
	 * array and return the starting address of memory.
	 * @param chars - the characters to be copied into the new memory
	 * @return address of memory that was allocated
	 */
	public int copyAlloc(char[] chars) {
		int addr = alloc(chars.length);
		setChars(addr, chars);
		return addr;
	}


	public int canMerge() {
		StorageNode prevAvail = available;
		StorageNode nextAvail = available;

		while (nextAvail != null) {
			if (nextAvail.addr == (prevAvail.addr + prevAvail.numChars)) {
				return prevAvail.addr;
			}
			nextAvail = nextAvail.next;
		}
		return -1;
	}
	public void merge(int addr) {
		StorageNode prevAvail = available;
		StorageNode nextAvail = available;

		while (nextAvail != null) {
			if (addr == nextAvail.addr) {
				prevAvail.numChars = prevAvail.numChars + nextAvail.numChars;
				prevAvail.next = nextAvail.next;
			}
			prevAvail = nextAvail;
			nextAvail = nextAvail.next;
		}
	}


	/**
	 * Free the memory currently associated with address and add back to
	 * available list.
	 *
	 * if addr is not within a range of allocated memory, then FALSE is returned.
	 */
	public boolean free(int addr) {
		StorageNode next = allocated;
		StorageNode nextAvail = available;
		StorageNode node = new StorageNode(0, 0);

		int n = 0; // check to see if valid node has been found

		if (numAlloc == 0) {
			throw new RuntimeException("Nothing Allocated");
		}

		while (next != null) {
			if (next.addr <= (addr + next.numChars) && addr >= next.addr) {
				node = next;

				if (nextAvail == null) {
					available = node;
					n = 1;
					break;
				}

				// update available list to ensure the list is in order of address
				while (nextAvail.next != null && nextAvail.next.addr < node.addr) {
					nextAvail = nextAvail.next;
				}
				StorageNode temp = nextAvail.next;

				if (temp == null) {
					node.next = available;
					available = node;
				}
				else {
					nextAvail.next = node;
					node.next = temp;
				}

				// address was found
				n = 1;
				break;
			}
			next = next.next;
		}

		// enter 0's in the newly freed storage
		if (n == 1) {
			for (int i = 0; i < node.numChars; i++) {
				storage[i + addr] = 0;
			}

			int mergeVar = canMerge();

			while (mergeVar != -1) {
				merge(mergeVar);
				mergeVar = canMerge();
			}
			numAlloc -= node.numChars;
			numAvail += node.numChars;
			return true;
		}
		else return false;
	}

	/**
	 * Set char, but only if it is properly contained as an allocated segment of memory.
	 * Performance proportional to number of allocated blocks.
	 * @exception if the addr is not within address of memory that was formerly allocated.
	 */
	public void setChar(int addr, char value) throws RuntimeException {
		validateAllocated(addr);
		setChars(addr, new char[]{value});
	}

	/**
	 * Set consecutive char values starting with addr to contain the char values passed in, but only if
	 * the full range is properly contained as an allocated segment of memory.
	 * Performance proportional to number of allocated blocks.
	 * @exception if the addr is not within address of memory that was formerly allocated.
	 */
	public void setChars(int addr, char[] values) throws RuntimeException {
		validateAllocated(addr);

		for (int i = 0; i <values.length; i++) {
			storage[i + addr] = values[i];
		}

	}

	// ================================================================================================================
	// ======================================== EVERYTHING ELSE BELOW REMAINS AS IS ===================================
	// ================================================================================================================

	/**
	 * Sets int, but only if it is properly contained as an allocated segment of memory.
	 * Performance proportional to number of allocated blocks.
	 * USE AS IS.
	 * @exception if the addr is not within address of memory that was formerly allocated with sufficient space
	 */
	public void setInt(int addr, int value) throws RuntimeException {
		validateAllocated(addr, 4);
		setChar(addr,   (char)((value & 0xff000000) >> 24));
		setChar(addr+1, (char)((value & 0xff0000) >> 16));
		setChar(addr+2, (char)((value & 0xff00) >> 8));
		setChar(addr+3, (char)(value & 0xff));
	}

	/**
	 * Return the 4-chars at the given address as an encoded integer.
	 * Performance proportional to number of allocated blocks.
	 * USE AS IS.
	 */
	public int getInt(int addr) {
		validateAllocated(addr, 4);
		return (getChar(addr) << 24) + (getChar(addr+1) << 16) + (getChar(addr+2) << 8) + getChar(addr+3);
	}

	/**
	 * Allocate new memory large enough to contain room for an array of numbers and copy
	 * numbers[] into the memory, returning the address of the first char.
	 *
	 * USE AS IS.
	 *
	 * Because int values are 32-bits, this means that the total number of char allocated
	 * will be 4*numbers.length
	 *
	 * @param numbers   The int[] values to be copied into the newly allocated storage.
	 */
	public int copyAlloc(int[] numbers) {
		int addr = alloc(4*numbers.length);
		for (int i = 0; i < numbers.length; i++) {
			setInt(addr+4*i, numbers[i]);
		}

		return addr;
	}

	/**
	 * Return the string which is constructed from the sequence of char from addr
	 * for len characters.
	 * USE AS IS.
	 */
	public String createString(int addr, int len) {
		return String.valueOf(storage, addr, addr+len-1);
	}

	/**
	 * Return those allocated nodes whose allocated char[] matches the pattern of char[] passed in.
	 * ONLY COMPLETE FOR BONUS
	 * @param pattern
	 */
	public java.util.Iterator<StorageNode> match(char[] pattern) {
		throw new RuntimeException("BONUS IMPLEMENTATION");
	}

	/** This sample program recreates the linked list example from Q2 on the homework. */
	public static void main(String[] args) {
		Memory mem = new Memory(32);

		mem.alloc(2);   // don't use address in this small example...
		int first  = mem.alloc(8);
		mem.alloc(3);
		int third  = mem.alloc(8);
		mem.alloc(3);
		int second = mem.alloc(8);

		mem.setInt(first, 178);   // first node stores 178
		mem.setInt(second, 992);  // second node stores 992
		mem.setInt(third, 194);   // third node stores 194

		mem.setInt(first+4, second);    // have next pointer for first to point to second
		mem.setInt(second+4, third);    // have next pointer for second to point to third
		mem.setInt(third+4, 0);         // have next pointer for third to be 0 (END OF LIST)

		// How to loop through list?
		System.out.println("Numbers should print in order from 178 -> 992 -> 194");
		int addr = first;
		while (addr != 0) {
			int value = mem.getInt(addr);    // get value of node pointed to by addr.
			System.out.println(value);

			addr = mem.getInt(addr+4);       // advance to next one in the list
		}

		System.out.println("Allocated bytes should be 32: " + mem.charsAllocated());
		System.out.println("Available bytes should be 0: " + mem.charsAvailable());
	}
}
