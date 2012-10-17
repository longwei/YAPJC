/**
 * class ILOC -- support for linear instruction format
 *
 * based on ILOC from Cooper & Torczon "Engineering a Compiler"
 *   and on ILOC simulator from Rice
 *
 * Simplifications:
 *   - no text format.  Instructions created via ILOC() constructor
 *   - simulator does not model latency (yet)
 * Extensions:
 *   - OUTPUTR instruction to print value from register
 *   - BR instead of JUMPI, also "CMPLT" instead of "cmp_LT" etc.
 *   - CBRN to check inverse of condition
 *   - floating point arithmetic  (FADD etc, also FOUTPUTR)
 *   - (values that look like they might be float will be printed in hex)
 * 
 * - some instructions have not yet been implemented.  (see the switch statement in "execute")
 *
 * Addressing:  instructions are stored in an array ("allInstrs") and addressed by
 *  their offset.  The offset can be gleaned by calling nextLoc().  Instructions are 
 *  not stored in "memory" and are not accessible as program data.
 */

package cs665ILOC;
import java.util.*;
import cs665.Main;

/** ILOC - general class to hold 'init' etc
 */

public class ILOC
{
	/** verbose controls debug/trace output */
	final static boolean verbose = true;

	/**  allInstrs holds all the instructions.. */
	private static ArrayList<IlocInstr> allInstrs = new ArrayList<IlocInstr>();

	/** size to allocate for data memory, in bytes */
	static final int dataMemorySize = 1000;


	public static void init()
	{
		// (create space for stack.  10 is an arbitrary value, non-zero)
		final int FPbase = 0x10;
		// set official values into reserved registers
		IlocInstr.makeInstr(Opcode.LOADI, Reg.rFP, FPbase).emit();
		IlocInstr.makeInstr(Opcode.LOADI, Reg.rSP, FPbase +
		    // please set the following line
		    // for YOUR implementation...
				    //NEED REAL:
				    // NameScope.currentScope().maxOffset()
				    //DEFAULT (stack size of 0x100)
				    0x100
		    // to give the size of the frame to allocated
		    // (i.e., how much space has been allocated
		    // by your compiler for local variables in main)

				    ).emit(); 
		// this is just a marker: here's where we start user program.
		IlocInstr.makeInstr(Opcode.NOP).emit();

	}
	/** addInstr -- add an IlocInstr to the current program.
	 */
	public static void addInstr(IlocInstr i)
	{
		if (verbose) System.out.println(" new " + i.toString() 
						     + "    into [" + ILOC.allInstrs.size() + "]");		
		allInstrs.add(i);
	}

	/** what is the location of the next instruction to be emitted?
	 *   We always add to the end of allInstrs, so...
	 * @return index of next instruction slot
	 */
	public static int nextLoc()
	{
		return allInstrs.size();
	}

	public static void debugPrintAllInstrs()
	{
		System.out.println("\nAllInstrs:");
		for (int j = 0; j < allInstrs.size(); j++)
		{
			System.out.println(" [" + Integer.toString(j) + "] " + allInstrs.get(j).toString());
		}
	} // end debugPrintAllInstrs


	/** run
	 * support for running (executing) the program
	 * PC value is available via getPc, setPc
	 * "running" flag is available so "STOP" can halt execution
	 */

	private static int pc;
	static boolean running;
	public static void run()
	{

		System.out.println("\nRun:");
		running = true;
		pc = 0;
		while (running)
		{
			IlocInstr cur = allInstrs.get(pc);
			if (ILOC.verbose)
				System.out.println("  Executing[" + pc + "]: " + cur);

			pc++;
			cur.execute();
		}
	} // end run

	/** setPc -- for branch or call or return */
	static void setPc(int newPc)
	{
		if (verbose)
			System.out.println("   setPc to: " + newPc);
		if (newPc < 0 || newPc >= allInstrs.size())
			Main.internalError("setPc - value (" + newPc 
					   + ") out of bounds");
		pc = newPc;
	}

	/** getPc -- query current PC value.  Used by CALL */
	static int getPc()
	{
		return pc;
	}

	/** num2string
	 * utility for printing numbers..
	 * If value is an int => convert to decimal int (normal case)
	 * If value looks like a float, then => convert to hex
	 *  (seems like a better way than converting to float decimal)
	 */
	public static String num2String(int val)
	{
		if (val < 0x100000 && val > -0x100000)
			return Integer.toString(val);
		else
			return "0x"+Integer.toHexString(val);
	}

}  // end class ILOC

/** IlocMemory
 * models byte-addressable data memory,
 *  There's only one memory, so all of this class is static.
 */

class IlocMemory
{
	static boolean verbose = true;
	private static byte[] memory = new byte[ILOC.dataMemorySize];

	/** check
	 * utility function to check address validity.
	 * @arg addr address for proposed reference
	 * @arg size number of bytes required
	 */
	protected static void check(int addr, int size)
	{
		if (addr < 0 || (addr+size) > memory.length)
		{
			Main.internalError("** IlocMemory ERROR, mem access to " + addr + " out of bounds"
					   + "\n memory size is " + memory.length);
		}
	}
	/** get
	 * read a word out of memory at indicated address.
	 * todo: complain if not aligned
	 */
	static int get(int addr)
	{
		check(addr, 4);
		int val = ((memory[addr  ] & 0xFF)      )
			| ((memory[addr+1] & 0xFF) << 8 )
			| ((memory[addr+2] & 0xFF) << 16)
			| ((memory[addr+3] & 0xFF) << 24)
			;
		if (ILOC.verbose) System.out.println("   get  " + ILOC.num2String(val) + " from mem[" + addr + "]");
		return val;
	}
	/** put:
	 * store value into memory at indicated address.
	 *  complain if not aligned
	 */

	static void set(int addr, int val)
	{
		check(addr, 4);

		memory[addr  ] = (byte) ((val      ) & 0xFF);
		memory[addr+1] = (byte) ((val >> 8 ) & 0xFF);
		memory[addr+2] = (byte) ((val >> 16) & 0xFF);
		memory[addr+3] = (byte) ((val >> 24) & 0xFF); 

		if (verbose) System.out.println("   mem[" + addr + "] := " + ILOC.num2String(val));
	}
}

