package kagupta.hw2;

/**
 * Use this class "AS IS" and copy it into your USERID.hw2 package.
 * 
 * As you work on the Memory class, you can use this program to determine if you are completing its functionality properly. 
 */
public class StressProgram {
	
	/** Throws exception if b is false. */
	static void check(boolean b) {
		if (!b) throw new RuntimeException("FAILURE");
	}
	
	/** Known failure. Call this from within a try... catch. */
	static void fail() {
		throw new RuntimeException("SHOULD FAIL");
	}
	
	/**
	 * Run the Memory program through its paces.
	 */
	public static void main(String[] args) {
		int size = 16384;  							         // must be divisible by 4 for later code to work
		Memory mem = new Memory(size);
		check (0 == mem.charsAllocated());
		 
		// handle error cases first
		// ===============================================================================================
		try {
			mem.setChar(20, 'a');                            // can't set memory that hasn't been allocated
			fail();
		} catch (RuntimeException re) {
			
		} 
		
		try {
			mem.setChars(20, new char[] { 'a', 'b', 'c'});   // can't set memory that hasn't been allocated
			fail();
		} catch (RuntimeException re) {
			System.out.println("throws error when invalid setChar... (+3 points)");
		} 
		
		try {
			mem.free(20);                                    // error to free memory not allocated!
			fail();
		} catch (RuntimeException re) {
			
		} 
		
		try {
			mem.realloc(20, 99);                             // error to reallocate memory not allocated
			fail();
		} catch (RuntimeException re) {
			
		} 
		
		int addrx = mem.alloc(size);                         // take it all
		check (0 == mem.charsAvailable());
		try {
			mem.alloc(20);                                   // none left!
			fail();
		} catch (RuntimeException re) {
			
		}
		
		try {
			mem.realloc(addrx, size*2);                      // cannot reallocate with larger space
		} catch (RuntimeException re) {
			
		}
		
		check(mem.free(addrx));
		check (0 == mem.charsAllocated());                   // back to ZERO		
		
		int chunkSize = size/4;								 // grab 4 x size/4 chunks of memory
		int addr0 = mem.alloc(chunkSize);
		check(size-chunkSize == mem.charsAvailable()); 
		int addr1 = mem.alloc(chunkSize);
		check(size-2*chunkSize == mem.charsAvailable()); 
		int addr2 = mem.alloc(chunkSize);
		check(size-3*chunkSize == mem.charsAvailable()); 
		int addr3 = mem.alloc(chunkSize);
		check(0 == mem.charsAvailable());
		
		// release everything and then try to grab WHOLE THING (which you can do if free merges things)
		check(mem.free(addr0));
		check(mem.free(addr1));
		check(mem.free(addr2));
		check(mem.free(addr3));
		
		try {
			int addr_all = mem.alloc(size);                  // IF this fails, then cannot get the four points
			mem.free(addr_all);
			System.out.println("Free Combines Available Regions...   (+4 points)");
		} catch (RuntimeException re) {
			System.out.println("Free does not combine available regions -4 points *************");
		}
 
		addr0 = mem.alloc(chunkSize);
		check(size-chunkSize == mem.charsAvailable()); 
		addr1 = mem.alloc(chunkSize);
		check(size-2*chunkSize == mem.charsAvailable()); 
		addr2 = mem.alloc(chunkSize);
		check(size-3*chunkSize == mem.charsAvailable()); 
		addr3 = mem.alloc(chunkSize);
		check(0 == mem.charsAvailable());
		
		// leave as allocated two big chunks, one at end and one near front.
		check(mem.free(addr0));
		check(mem.free(addr2));         
		check(size-2*chunkSize == mem.charsAvailable()); 
		
		addr0 = mem.alloc(chunkSize);                        // reallocate when size is the same as one that was free'd
		addr2 = mem.alloc(chunkSize);
		check(mem.free(addr0));
		check(mem.free(addr2));
		check(size-2*chunkSize == mem.charsAvailable()); 
		
		int[] addresses = new int[509];                      // keep locate array of allocated chunks of 8 bytes each
		for (int i = 0; i < addresses.length; i++) {
			addresses[i] = mem.alloc(8);
			mem.setInt(addresses[i], i);                     // just to invoke setInt
			check(i == mem.getInt(addresses[i]));            // and confirm it works.
		}
		check(addresses.length*8 + 2*chunkSize == mem.charsAllocated());
		check(size-2*chunkSize-addresses.length*8 == mem.charsAvailable()); 
		
		// now, using the fact that 509 % prime != 0, cycle through all of them
		// in a seemingly random fashion until all are visited (and free'd).
		int idx = 0;
		check(mem.free(addresses[idx]));                     // free 0th one first
		idx = 13;     // must be prime
		int numFreed = 1;
		while (idx != 0) {
			check(mem.free(addresses[idx]));                 // must always be true
			numFreed++;
			idx = (idx + 13) % addresses.length;
		}
		
		check(addresses.length == numFreed);                 // all of these were free'd
		check(size-2*chunkSize == mem.charsAvailable()); 
		
		check(mem.free(addr1));                              // now release these chunks...
		try {
			mem.free(addr1);   // ERROR!                           
			fail();            
		} catch (RuntimeException re) {
			System.out.println("throws error on double frees...      (+3 points)");
		}
		
		check(mem.free(addr3));  
		check(size == mem.charsAvailable()); 
		
		// go through and allocate chunks WHILE ALSO releasing chunks ten behind...
		for (int i = 0; i < addresses.length; i++) {
			addresses[i] = mem.alloc(8);
			if (i >= 10) {
				mem.free(addresses[i-10]);                   // release old ones...
			}
		}
		// now must release final ten that had not yet been free'd
		for (int i = addresses.length-1; i >= addresses.length-11; i--) {
			mem.free(addresses[i]);
		}
		
		check(size == mem.charsAvailable()); 
		check(0 == mem.charsAllocated());                    // now back to ZERO
		
		// now handle char[] arrays
		int addrs = mem.copyAlloc("testing".toCharArray());
		check("testing".equals(mem.createString(addrs, 7)));
		check("testing".equals(String.valueOf(mem.getChars(addrs, 7))));
		
		mem.setChars(addrs, "replace".toCharArray());        // there is enough room
		check("replace".equals(mem.createString(addrs, 7)));	
		
		try {
			// not enough room
			mem.setChars(addrs, "not enough room".toCharArray());   // NOT enough room
			fail();
		} catch (RuntimeException re) {
			
		}
		check(mem.free(addrs));    // done with this
		
		// deal with int[] arrays
		int addri = mem.copyAlloc(new int[] { 2, 3, 4});
		check(2 == mem.getInt(addri));
		check(3 == mem.getInt(addri+4));
		check(4 == mem.getInt(addri+8));
		check(mem.free(addri));
		 
		addrs = mem.alloc(20);                               // reallocation tests
		addr1 = mem.alloc(10);
		addr2 = mem.alloc(40);
		addrs = mem.realloc(addrs, 30);
		mem.setChar(addrs, 'z');
		char[] values = mem.getChars(addrs, 30);             // must work
		check(values[0] == 'z');
		addrs = mem.realloc(addrs, 10);                      // shrink
		check(mem.free(addr2));
		check(mem.free(addr1)); 
		
		try {
			mem.getChars(addrs, 30);                         // must fail
			fail();
		} catch (RuntimeException re) {
			
		}
		
		values = mem.getChars(addrs, 10);                    // must work
		
		mem.setChars(addrs, "abcdefghij".toCharArray());     // fill all ten
		addrs = mem.realloc(addrs, 15);                      // GROW
		values = mem.getChars(addrs, 15);
		check("abcdefghij\0\0\0\0\0".equals(String.valueOf(values)));
		
		check(mem.free(addrs));
		
		check(size == mem.charsAvailable());
		check(0 == mem.charsAllocated());
		
		// special case. Allocate two blocks and free first one. Then request fewer char than in that block
		addr1 = mem.alloc(20);
		addr2 = mem.alloc(30);
		check(mem.free(addr1));
		addr1 = mem.alloc(10);   // less than had been released
		addr3 = mem.alloc(40);   // not enough room up front so has to go further
		check(mem.free(addr3));
		check(mem.free(addr1));
		check(mem.free(addr2));
		
		check(size == mem.charsAvailable());
		check(0 == mem.charsAllocated());                    // Back to ZERO
	}
}
