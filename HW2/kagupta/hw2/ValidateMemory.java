package kagupta.hw2;

/**
 * COPY this class into your USERID.hw2
 * 
 * Sample Validate Memory. Just for small things.
 */
public class ValidateMemory {

	public static void main(String[] args) {
		Memory mem = new Memory(64);                    // Manage a one-dimensional array of 64 char values.
		
		int addr1 = mem.alloc(32);                      // Allocate storage to work with
		mem.setChars(addr1, "hotel".toCharArray());     // Store some characters
		equals("hotel", mem.getChars(addr1, 5));        // should be 'hotel'  
		
		mem.setChar(addr1 + 2, 'v');
		equals("hovel", mem.getChars(addr1, 5));        // should be 'hovel'	
		
		mem.setChars(addr1+4, "ring".toCharArray());
		equals("hovering", mem.getChars(addr1, 8));     // should be 'hovering'	
		
		int addr2 = mem.alloc(7);
		try {
			mem.setChars(addr2, "this is too long".toCharArray());
		} catch (RuntimeException re) {
			// cannot set these chars since not enough allocated memory at that address.
		}
		
		mem.setChars(addr2, "perfect".toCharArray());   // this works.
		equals("perfect", mem.getChars(addr2, 7)); 
		
		int addr3 = mem.alloc(8);
		int addr4 = mem.realloc(addr1, 5);              // reallocation will release old block and assign to new one
		
		System.out.println(mem.getChars(addr4, 5));     // prints 'hover'
		
		mem.free(addr2);
		mem.free(addr3);
		
		System.out.println(mem.charsAllocated());// returns 5
		System.out.println(mem.charsAvailable());// returns 59
	}
	
	static void equals(Character c1, Character c2) {
		if (!c1.equals(c2)) { throw new RuntimeException ("Character " + c1 + " doesn't match " + c2); }
	}
	static void equals(String s, char[] chars) {
		if (!s.equals(String.valueOf(chars))) { throw new RuntimeException ("String " + s + " doesn't match " + String.valueOf(chars)); }
	}
	static void equals(char[] chars1, char[] chars2) {
		if (!String.valueOf(chars1).equals(String.valueOf(chars2))) { throw new RuntimeException ("String " + String.valueOf(chars1) + " doesn't match " + String.valueOf(chars2)); }
	}
}
