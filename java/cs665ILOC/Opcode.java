package cs665ILOC;
import java.util.*;
import cs665.Main;

/********************
 * ILOC instruction support section.
 */

/** B - bits for cross-checking opcode properties.  (see note below)
 */
enum B {
	/* 
	 *
	 * first define bits for the basic properties...
	 *   one, two, or three sources:   s(source)1/2/3,
 	 *   sc(source constant), t(target), none.
	 */
	s1(0x1), s2(0x2), s3(0x4), sc1(0x8), t(0x10),
		none(0),
		  // CHECK ==  all those that need checking at creation time
		CHECK(B.s1.val | B.s2.val | B.s3.val | B.sc1.val | B.t.val),
		  // LOAD and STORE are special properties, will be added to other stuff
		LOAD(0x20),
		STORE(0x40),

	/* Now define the opcode properties groups, a combination of the individual props.
	 * For example, "sst" is SourceSourceTarget
	 * Each opcode is characterized by one of these combinations.  When an
	 * instruction is created, the bit vector associated with the opcode
	 * is checked against the actual set of arguments supplied to the 
	 * constructor.  This helps keep the user from creating invalid
	 * instructions.  Note that it is a run-time check.
	 */
		sst(B.s1.val | B.s2.val  | B.t.val),	// two source regs  => target reg
		sct(B.s1.val | B.sc1.val | B.t.val),	// source reg & constant => target
		st (B.s1.val             | B.t.val),	// one source reg => target
		ct (           B.sc1.val | B.t.val),	// constant  => target
		s  (B.s1.val),				// one source reg, no target
		ss (B.s1.val | B.s2.val),		// two source reg, no target (RETURN)

		sc (B.s1.val | B.sc1.val),	// source reg & constant (for CBR)

		// store has some extra complexity, an extra source instead of a target.
		// (have that in sr[3])
		scs(B.s1.val | B.sc1.val | B.s3.val),	// source reg & constant, another source regs  (storeAI instr)
		sss(B.s1.val | B.s2.val  | B.s3.val),	// two source regs, another source reg  (storeAO instr)
		;

	// Each 'B' enum has a 'val' field to store the bitmask.
	int val;

	/**  constructor, what's actually invoked in the above notation.  Seems to be ok
	 * that the constructors reference enums in B itself .. note those are actually
	 * all defined prior to being referenced.
	 */
	B(int v)
		{ val = v;}
}

/** public enum: Opcode
 * argument specifies the 'B' bitmask, which is used by constructors for checking validity --
 * allows verifying that we're creating an instruction with as many fields of the right kind
 * as this opcode expects.  Verify that we using the right constructor.
 *
 * Opcodes (and ILOC) taken from Cooper & Torczon "Engineering a Compiler".  See Appendix A, 
 * esp. the opcode summar at the end of section A.5. 
 */
public enum Opcode 
{
	        NOP    (B.none.val),	// no operation
		ADD    (B.sst.val),	// integer arithmetic
		SUB    (B.sst.val),
		MULT   (B.sst.val),
		DIV    (B.sst.val),
		FADD   (B.sst.val),	//(extension)  float arithmetic
		FSUB   (B.sst.val),
		FMULT  (B.sst.val),
		FDIV   (B.sst.val),
		LOGAND (B.sst.val),     // ext: calculate boolean AND
		ADDI   (B.sct.val),	// integer add register + immed value
		SUBI   (B.sct.val),
		MULTI  (B.sct.val),
		DIVI   (B.sct.val),
		LSHIFT (B.sst.val),	// left shift Reg s1 by amount in Reg s2
		LSHIFTI(B.sct.val),	// left shift by immediate amount
		RSHIFT (B.sst.val),
		RSHIFTI(B.sct.val),
		CVTI2F (B.st.val),	// ext: convert Int to Float
		CVTF2I (B.st.val),	// ext: convert Float to Int
		LOADI  (B.ct.val),	// load immediate value (contained in instruction)
		LOAD   (B.st.val + B.LOAD.val),	// load from memory, address given by contents of Reg s1
		LOADAI (B.sct.val +B.LOAD.val),	// load, address given by contents of Reg s1 + constant
		LOADAO (B.sst.val +B.LOAD.val),	// load, address given by sum of Reg s1 and Reg s2
		CLOAD   (B.st.val +B.LOAD.val),	// load character (single byte)
		CLOADAI (B.sct.val+B.LOAD.val),
		CLOADAO (B.sst.val+B.LOAD.val),
		STORE   (B.ss.val +B.STORE.val),	// store instructions (address calc like LOAD)
		STOREAI (B.scs.val+B.STORE.val),
		STOREAO (B.sss.val+B.STORE.val),
		CSTORE  (B.st.val +B.STORE.val),	// store character
		CSTOREAI(B.scs.val+B.STORE.val),
		CSTOREAO(B.sss.val+B.STORE.val),
		BR     (B.sc.val),	// Branch (unconditional) to literal address
					// (called "JUMPI" by C&T)
		CBR    (B.sc.val),	// Conditional branch (if Reg s1 is True)
					// (condition can be in any register.  0 is false, non-zero is true)
		CBRN   (B.sc.val),	// Ext: Conditional Not branch (if Reg s2 is False)
		CMPLT  (B.sst.val),	// compare Reg s1 LT Reg s2, set target register accordingly
					// (target can be any register.  False is 0, True is 1)
		CMPLE  (B.sst.val),
		CMPEQ  (B.sst.val),
		CMPNE  (B.sst.val),
		CMPGE  (B.sst.val),
		CMPGT  (B.sst.val),
		I2I    (B.st.val),	// integer move, reg->reg
		C2C    (B.st.val),	// ditto, for char
		C2I    (B.st.val),	// translate ascii code to integer
		I2C    (B.st.val),	// translate integer to character
		OUTPUT (B.s.val),
		COUTPUT(B.s.val),
		OUTPUTR(B.s.val),	// ext: output value from register
		FOUTPUTR(B.s.val),	// ext: output float value from reg
		CALL   (B.ct.val),
		RETURN (B.ss.val),
		STOP   (B.none.val),
		ERROR  (B.none.val),
		
		;
	// bit vector associated with each opcode, for checking validity of constructor.
	final int checkBits;
	// implicit constructor
	Opcode(int b)
	{
		checkBits = b;
	}

}
