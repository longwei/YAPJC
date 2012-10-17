package cs665ILOC;
import java.util.*;


/** Reg
 * 
 * class to handle allocation of Reg's for ILOC generation
 * and also to support them at runtime  (get, put value).
 */

public class Reg
{
	// note: must create allRegs before creating indiv Reg objects ...
	static ArrayList<Reg> allRegs = new ArrayList<Reg>();	// keep track of all..

	static int lastPhysReg = 15;
	/* (skip this now ...)
	   // long create of special registers for system etc.
	static
	{
		for (int j = 0; j<=lastPhysReg; ++j)
		{
			new Reg(j);
		}
	}
	static Reg rVal = allRegs.get(1);	// holds arg/return value
	static Reg rFP = allRegs.get(13);	// frame pointer
	static Reg rSP = allRegs.get(14);	// stack pointer (first free)
	Static Reg rRA = allRegs.get(15);	// holds return address on call
	*/
	// short create of some special registers.
	public static Reg rVal = new Reg(1);
	public static Reg rFP = new Reg(13);	// frame pointer
	public static Reg rSP = new Reg(14);	// stack pointer (first free)
	public static Reg rRA = new Reg(15);

	// CAREFUL: if you add special registers, then update lastPhysReg
	// so that nextReg starts off right!

	static int nextReg = lastPhysReg + 1;    // next available register

	// Fields of Reg
	protected int num;		// Register number

	protected int myValue;	      // registers store integer values
	protected boolean hasValue;   // (runtime) error checking:  make sure we don't
				// try to read a register's value before it
				// has been given one.
    /** 
     * Primary constructor:  creates a new register.
     */
	public Reg()
	{
		num =  nextReg++;
		//- if (ILOC.verbose) System.out.println("new reg " + toString());
		allRegs.add(this);


	}


    /** alternate constructor, when you know the physical register number
     * you want.  Primarly for reserved system registers.
     */
	Reg(int n)
	{
		num =  n;
		if (ILOC.verbose) System.out.println("new special reg " + toString());
		allRegs.add(this);
	}

    /** get
     * value setting
     * NOT intended for use from compiler, just ILOC simulator */

	public int get()
	{
		if (! hasValue)
		{
			System.out.println(" Runtime Error: accessing undef value from reg " + toString() + "\n (This cannot happen if the compiled code sequence is correct)");
		}
		return myValue;
	}

	/** fget : get value, interpret bits as float value
	 */
	public float fget()
	{
		return Float.intBitsToFloat(get());
	}


		


	// Note: allow us to delay setting reg to show pipelining/instr delay.
	// -> void set(int newValue, int timeIncr)
	// For now, just do the set right away
				     
	/** set 
	 *  value setting 
	 * NOT intended for use from compiler, just ILOC simulator */

	void set(int newValue)
	{
		myValue = newValue;
		hasValue = true;
	}

	/** fset 
	 * set float value, keeping bit pattern intact
	 */
	void fset(float newValue)
	{
		set(Float.floatToIntBits(newValue));
	}

	public String toString()
	{
		switch (num) 
		{
		    // special system register, maybe?
		//case 0:
			// return "rZero";
		case 1:
			return "rVal";
		case 13:
			return "rFP";
		case 14:
			return "rSP";
		case 15:
			return "rRA";

		default:
			    // normal register 
			return "r"+num;
		}
	}

	// like toString, but more info (prints value if it has one)
	public String debugString()
	{
		return "Reg: " + toString()
			// (superfluous:)  + ", hasValue: " + hasValue
			+ (hasValue?  ", val= " + myValue : "")
			;
	}

	/** debug print out all registers */
	public static void debugPrintAll()
	{
		System.out.println("AllRegs:");
		Iterator<Reg> iter = allRegs.iterator();
		while (iter.hasNext())
		{
			System.out.println(" " + iter.next().debugString());
		}
	}
} // end class Reg



