package cs665ILOC;
import java.util.*;
import cs665.Main;

/** IlocInstr
 *
 * each instance represents a single ILOC instruction.
 * A variety of constructors provided to deal with different formats -- more or
 *  less sources, target, etc.  Generally, target is first, then sources.
 *
 * Not every instruction has a value for all the fields;  they are typically 
 * null if they don't have a valid value.  sconst is "nc" when not valid.
 */

public class IlocInstr
{

	Opcode op;
	Reg[] sr;	// source register(s)
	int sconst;	// source constant

	static int nc = -1;	// this "value" indicates no (null) constant
	Reg tr1;	// target reg

	String commentString;	// used in load/store, mainly for debug;
				// (formerly also for VN support)

	/** create instruction, filling in all fields (some null)
	 * - also check validity
	 * - also enter into allInstrs
	 * Should not be called  directly by external customers:
	 *   they should use one of the customized forms, below.
	 */

	private IlocInstr(Opcode a_op, int bits, Reg a_targ, Reg a_sr1, Reg a_sr2, Reg a_sr3, int a_const, boolean a_isStore, String a_commentString)
	{
		//TODO: convert to 0-origin array
		sr = new Reg[4];
		op = a_op;
		sr[1] = a_sr1;
		sr[2] = a_sr2;
		sr[3] = a_sr3;
		tr1 = a_targ;
		sconst = a_const;
		commentString = a_commentString;

		// check if instruction's operands match what the opcode requires.
		// (relax check for ERROR, anything goes there...)
		if (bits != (op.checkBits & B.CHECK.val) && op != Opcode.ERROR)
		{
			Main.internalError("\n** IlocInstr constructor error: wrong operands provided\n**    for " + toString()
					   + "\n** config pattern bits should be 0x" + Integer.toHexString(op.checkBits & B.CHECK.val)
					   + ", is 0x" + Integer.toHexString(bits)
					   + "\n\n** (This usually means you've used the wrong constructor ..\n** 'makeInstr' can't handle all opcodes .. \n** or you don't have the right number or kind of operands for this opcode)"
);
			op = Opcode.ERROR;
		}
	} // end constructor

	/** emit
	 * put (this) instruction into the instruction stream at the next location
	 * A static version is also supplied, if that's helpful.
	 * @return instruction: useful for branch calculations
	 */

	public IlocInstr emit()
	{
		ILOC.addInstr(this);
		return this;
	}

	// This version returns the location at which the instruction was placed.
	public static int emit(IlocInstr i)
	{
		ILOC.addInstr(i);
		return ILOC.nextLoc() - 1;
	}



      ////////////////////  EXTERNAL CONSTRUCTORS ///////////////////
	////////////////	////////////////
	// Specific create routines, for various shape instructions
	//   Mostly, "makeInstr", also some specialized names.
	//
	// The general format is:
	//	makeInstr(Opcode, Target-Register, Source-Registers, [source constant]);
	//
	////////////////	////////////////	////////////////

       

	// reg, reg => reg  such as PLUS, MINUS ..
	public static IlocInstr makeInstr(Opcode a_op, Reg a_targ, Reg a_sr1, Reg a_sr2)
	{
		return new IlocInstr(a_op, B.sst.val, a_targ,
			      a_sr1, a_sr2, /*a_sr3*/null, /*a_const*/nc, false, null);
	}

	// reg + const => reg
	public static IlocInstr makeInstr(Opcode a_op, Reg a_targ, Reg a_sr1, int a_const)
	{
		return new IlocInstr(a_op, B.sct.val, a_targ,
			      a_sr1, /*a_sr2*/null, /*a_sr3*/null, a_const, false, null);
	}

	////////////////////////////////////////////////////////////////////////
	// NOTE WELL :  LOAD AND STORE instructions have special constructors,
	//  "makeLoadInstr(...)"  and "makeStoreInstr(...)"  
	// The normal "makeInstr" constructors will not work properly.
	//  (this is also true for branch, and some other instructions.)
	////////////////////////////////////////////////////////////////////////

	// [reg] => reg  LOAD
	//   -- commentString intended to identify the variable name, for tracing
	public static IlocInstr makeLoadInstr(Opcode a_op, Reg a_targ, Reg a_sr1, String commentString)
	{
		return new IlocInstr(a_op, B.st.val, a_targ,
			      a_sr1, /*a_sr2*/null, /*a_sr3*/null, /*a_const*/nc, false, commentString);
	}
	// [reg + const] => reg  LOADAI
	public static IlocInstr makeLoadInstr(Opcode a_op, Reg a_targ, Reg a_sr1, int a_const, String commentString)
	{
		return new IlocInstr(a_op, B.sct.val, a_targ,
			      a_sr1, /*a_sr2*/null, /*a_sr3*/null, a_const, false, commentString);
	}
	// [reg, reg] => reg    (LOADAO)
	public static IlocInstr makeLoadInstr(Opcode a_op, Reg a_targ, Reg a_sr1, Reg a_sr2, String commentString)
	{
		return new IlocInstr(a_op, B.sst.val, a_targ,
			      a_sr1, a_sr2, /*a_sr3*/null, /*a_const*/nc, false, commentString);
	}

	// reg => (reg)		(STORE)
	public static IlocInstr makeStoreInstr(Opcode a_op, Reg a_sr3, Reg a_sr1, String commentString)
	{
		return new IlocInstr(a_op, B.ss.val, /*a_targ*/null,
				     a_sr1, /*a_sr2*/null, a_sr3, /*a_const*/nc, true, commentString);
	}
	// reg => reg + const	(STOREAI)
	public static IlocInstr makeStoreInstr(Opcode a_op, Reg a_sr3, Reg a_sr1, int a_const, String commentString)
	{
		return new IlocInstr(a_op, B.scs.val, /*a_targ*/null,
			      a_sr1, /*a_sr2*/null, a_sr3, a_const, true, commentString);
	}

	// reg => reg, reg  (STOREAO)
	public static IlocInstr makeStoreInstr(Opcode a_op, Reg a_sr3, Reg a_sr1, Reg a_sr2, String commentString)
	{
		return new IlocInstr(a_op, B.sss.val,null/*a_targ*/,
			      a_sr1, a_sr2, a_sr3, /*a_const*/nc, true, commentString);
	}


	// only one source Reg: i2i etc.
	public static IlocInstr makeInstr(Opcode a_op, Reg a_targ, Reg a_sr1)
	{
		return new IlocInstr(a_op, B.st.val, a_targ,
			      a_sr1, /*a_sr2*/null, /*a_sr3*/null, /*a_const*/nc, false, null);
	}

	// Only one source const: loadi
	public static IlocInstr makeInstr(Opcode a_op, Reg a_targ, int a_const)
	{
		return new IlocInstr(a_op, B.ct.val, a_targ,
			      /*a_sr1*/null, /*a_sr2*/null, /*a_sr3*/null, a_const, false, null);
	}
	// float constant  => reg   [F]LOADI
	// (special constructor to take float value, cast to int bits within instruction
	// creats comment String which  provides the float value in readable form 
	public static IlocInstr makeFLoadiInstr(Opcode a_op, Reg a_targ, float a_const)
	{
		String stringVal = Float.toString(a_const);
		return new IlocInstr(a_op, B.ct.val, a_targ,
				     /*a_sr1*/null, /*a_sr2*/null, /*a_sr3*/null,
				     /*constant*/ Float.floatToIntBits(a_const), false, stringVal);
	}

	// one source Reg, no target:  output ..
	public static IlocInstr makeInstr(Opcode a_op, Reg a_sr1)
	{
		return new IlocInstr(a_op, B.s.val, /*a_targ*/null,
			      a_sr1, /*a_sr2*/null, /*a_sr3*/null, /*a_const*/nc, false, null);
	}

	// no source or target:  nop, stop
	public static IlocInstr makeInstr(Opcode a_op)
	{
		return new IlocInstr(a_op, B.none.val, /*a_targ*/null,
			      /*a_sr1*/null, /*a_sr2*/null, /*a_sr3*/null, /*a_const*/nc, false, null);
	}

	// branch instructions, funny formats
	public static IlocInstr makeBranchInstr(Opcode a_op, Reg a_sr1, int a_loc)
	{
		// branch target location goes into sconst field. 
		return new IlocInstr(a_op, B.sc.val, /*a_targ*/null,
			     a_sr1, /*a_sr2*/null, /*a_sr3*/null, a_loc, false, null)
			;
	}
	public static IlocInstr makeBranchInstr(Opcode a_op, Reg a_sr1)
	{
		// branch target not yet known (forward branch or call)
		return new IlocInstr(a_op, B.sc.val, /*a_targ*/null,
				     a_sr1, /*a_sr2*/null, /*a_sr3*/null, /*a_loc*/nc, false, null)
			;
	}
	/** make a call instr.
	 *    show "target" == return address reg.
	 * @arg a_op  opcode, usually Opcode.CALL
	 * @arg a_targ register which will get current PC (for return),
	 *        usually rRA
	 * @arg a_loc index of target [instruction].  If not known, then specify as 
	 *   IlocInstr.nc and then change it later with setTarget()
	 * TODO: also show return value register as explicit target
	 */
	public static IlocInstr makeCallInstr(Opcode a_op, Reg a_targ, int a_loc, String id)
	{
		// branch target location goes into sconst field.  Should be 'nc' if
		// not yet known (forward branch or call)
		return new IlocInstr(a_op, B.ct.val, a_targ,
			     /*a_sr1*/null, /*a_sr2*/null, /*a_sr3*/null, a_loc, false, id)
;
	}
	/** make return instr.
	 * @arg a_op  opcode, usually Opcode.RETURN
	 * @arg Aa_sr1 "source 1" ==  return addr, usually rRA
	 * @arg sr[2] = return val reg, usually rVal.
	 */
	public static IlocInstr makeReturnInstr(Opcode a_op, Reg a_sr1, Reg a_sr2)
	{
		return new IlocInstr(a_op, B.ss.val, /*a_targ*/null,
			     a_sr1, a_sr2, /*a_sr3*/null, /*a_const*/nc, false, null)
;

	}



              //////////  END OF CONSTRUCTORS ///////////

	/** set target of branch instruction.  Useful for forward branches.
	 * When creating instruction, specify destination as IlocInstr.nc,
	 * that serves as "not yet" indicator, which can be checked.
	 */
	public void setTarget (int target)
	{
		if (sconst != nc)
		{
			// oops, looks like it's already been set.  That's not right.
			Main.internalError("setTarget but const already set to " + sconst);
		}
		if (ILOC.verbose)
			System.out.println("Setting target to " + target);
		if (target < 0 || target > ILOC.nextLoc())
		{
			// don't let me create a bad jump!  (Note:  allow
			// value == allInstrs.size() so it can jump to the "next"
			// instruction .. useful for jump-around cases like IF.)
			Main.internalError("setTarget, value " + target +
					   " out of bounds");
		}
		sconst = target;
	}



	/** SPECIAL:  copy instruction for value numbering .. do not enter into list, rather
	 * return it for caller to deposit.
	 */
	public static IlocInstr createCopyInstr(Reg a_targ, Reg a_sr1)
	{
		return new IlocInstr(Opcode.I2I, B.st.val, a_targ,
			      a_sr1, /*a_sr2*/null, /*a_sr3*/null, /*a_const*/nc, false, null);
	}

	/** toString
	 *  Provide a nice representation of the instruction
	 */
	public String toString()
	{
		String s;
		// check for no-operand instr
		if (tr1 == null && sr[1] == null && sconst == nc)
			s =  "Inst: " + op;
		// check for store:  tr1 == null
		else if (isStore())
		{
			s = "Inst: "+ op + " "  + sr[3] + " => ("  + sr[1] + ")";
			if (sr[2] != null)
				s += ", (" + sr[2] + ")";
			else if (sconst != nc )	
				s += ", " + ILOC.num2String(sconst);
		}
		else if (isLoad())
		{
			s = "Inst: "+ op + " ("  + sr[1] + ")";
			if (sr[2] != null)
				s += ", (" + sr[2] + ")";
			else if (sconst != nc )
				s += ", " + ILOC.num2String(sconst);
			// or could be just LOAD, with only sr[1]

			// has to be a target for LOAD
			s += " => " + tr1;
		}
		// check for target-only

		else if (sr[1] == null)
		{
			s = "Inst: " + op + "  " + ILOC.num2String(sconst);
			if (tr1 != null)
				s += " => " + tr1;
		}
		// check for single source
		else if (sr[2] == null)
		{
			if (sconst == nc )
				s = "Inst: " + op + " " + sr[1];
			else
				s = "Inst: " + op + " " + sr[1] + ",  " + ILOC.num2String(sconst);
			if (tr1 != null)
				s += " => " + tr1;
		}
		else
		{
			s = "Inst: " + op + " " + sr[1] + ", " + sr[2];
			if (tr1 != null)
				s += " => " + tr1;
		}
		if (commentString != null)	    // particularly load/store
			s += "        // " + commentString;
		return s;
	}

	boolean isStore()
	{
		return (op.checkBits & B.STORE.val) != 0;
	}

        boolean isLoad()
	{
		return (op.checkBits & B.LOAD.val) != 0;
	}

	/** execute
	 * execute this instruction
	 */

	void execute()
	{
	switch(op) {
	case NOP:
		break;
	case ADD:
		tr1.set(sr[1].get() + sr[2].get());
		break;
	case SUB:
		tr1.set(sr[1].get() - sr[2].get());
		break;
	case MULT:
		tr1.set(sr[1].get() * sr[2].get());
		break;
	case DIV:
		tr1.set(sr[1].get() / sr[2].get());
		break;	  
	case FADD:
		tr1.fset(sr[1].fget() + sr[2].fget());
		break;
	case FSUB:
		tr1.fset(sr[1].fget() - sr[2].fget());
		break;
	case FMULT:
		tr1.fset(sr[1].fget() * sr[2].fget());
		break;
	case FDIV:
		tr1.fset(sr[1].fget() / sr[2].fget());
		break;
	case LOGAND:
		tr1.set(sr[1].get() & sr[2].get());
		break;
	case ADDI:
		tr1.set(sr[1].get() + sconst);
		break;
	case SUBI:
		tr1.set(sr[1].get() - sconst);
		break;
	case MULTI:
		tr1.set(sr[1].get() * sconst);
		break;
	case DIVI:
		tr1.set(sr[1].get() / sconst);
		break;
	case LSHIFT:
		System.out.println("NYI - " + op);
		break;
	case LSHIFTI:
		tr1.set(sr[1].get() << sconst);
		break;
	case RSHIFT:
		System.out.println("NYI - " + op);
		break;
	case RSHIFTI:
		System.out.println("NYI - " + op);
		break;
	case CVTI2F:
		{
			// first convert to a float value
			int i = sr[1].get();
			float f = (float) i;
			// then pack those bits back into Reg container
			tr1.fset(f);
			break;
		}
	case CVTF2I:
		{
			float f = sr[1].fget();
			int i = (int)f;
			tr1.set(i);
			break;
		}
	case LOADI:
		tr1.set(sconst);
		break;
	case LOAD:
	{
		int addr = sr[1].get();
		tr1.set(IlocMemory.get(addr));
		break;
	}
	case LOADAI:
	{
		int addr = sr[1].get() + sconst;
		tr1.set(IlocMemory.get(addr));
		break;
	}
	case LOADAO:
	{
		int addr = sr[1].get() + sr[2].get();
		tr1.set(IlocMemory.get(addr));
		break;
	}

	case CLOAD:
		System.out.println("NYI - " + op);
		break;
	case CLOADAI:
		System.out.println("NYI - " + op);
		break;
	case CLOADAO:
		System.out.println("NYI - " + op);
		break;
	case STORE:
	{
		int addr = sr[1].get();
		IlocMemory.set(addr, sr[3].get());
		break;
	}

	case STOREAI:
	{
		int addr = sr[1].get() + sconst;
		IlocMemory.set(addr, sr[3].get());
		break;
	}
	
	case STOREAO:
	{
		int addr = sr[1].get() + sr[2].get();
		IlocMemory.set(addr, sr[3].get());
		break;
	}

	case CSTORE:
		System.out.println("NYI - " + op);
		break;
	case CSTOREAI:
		System.out.println("NYI - " + op);
		break;
	case CSTOREAO:
		System.out.println("NYI - " + op);
		break;
	case BR:
		if (sconst == IlocInstr.nc)
			Main.internalError("oops, BR target still 'nc'");
		ILOC.setPc(sconst);
		break;
	case CBR:
		if (sconst == IlocInstr.nc)
			Main.internalError("oops, CBR target still 'nc'");
		if (sr[1].get() != 0)
		{
			if (ILOC.verbose) System.out.println("   taking branch");
			ILOC.setPc(sconst);
		}
		else
		{
			if (ILOC.verbose) System.out.println("   not taking branch");
		}
		break;
	case CBRN:
		if (sconst == IlocInstr.nc)
			Main.internalError("oops, CBRN target still 'nc'");
		// branch on negated condition (i.e., false)
		if (sr[1].get() == 0)
		{
			if (ILOC.verbose) System.out.println("   taking branch");
			ILOC.setPc(sconst);
		}
		else
		{
			if (ILOC.verbose) System.out.println("   not taking branch");
		}
		break;
	case CMPLT:
		tr1.set(bool2int(sr[1].get() < sr[2].get()));
		break;
	case CMPLE:
		tr1.set(bool2int(sr[1].get() <= sr[2].get()));
		break;
	case CMPEQ:
		tr1.set(bool2int(sr[1].get() == sr[2].get()));
		break;
	case CMPNE:
		tr1.set(bool2int(sr[1].get() != sr[2].get()));
		break;
	case CMPGE:
		tr1.set(bool2int(sr[1].get() >= sr[2].get()));
		break;
	case CMPGT:
		tr1.set(bool2int(sr[1].get() > sr[2].get()));
		break;

	case I2I:
		{
			int val = sr[1].get();
			tr1.set(val);
			if (ILOC.verbose) System.out.println("   move " + ILOC.num2String(val) );
		}
		break;
	case C2C:
	case C2I:
	case I2C:
		System.out.println("NYI - " + op);
		break;
	case OUTPUT:
		System.out.println("NYI - " + op);
		break;
	case COUTPUT:
		System.out.println("NYI - " + op);
		break;
	case OUTPUTR:
		System.out.println("PRINT:  " + sr[1].get());
		break;
	case FOUTPUTR:
		System.out.println("PRINT:  " + sr[1].fget());
		break;
	case CALL:
		if (ILOC.verbose) System.out.println("about to call " + sconst
						     + " from " + (ILOC.getPc()-1));
		// save the current PC(+1) in the indicated register, for return
		tr1.set(ILOC.getPc());
		// check that target address has been filled in
		if (sconst == nc)
		{
			Main.internalError("no address provided for CALL");
		}
		// now do the transfer.
		ILOC.setPc(sconst);
		break;
		// perhaps should also have CALLV, value returning call.

	case RETURN:
		{
		int retAddr = sr[1].get();
		//  value to be returned is in sr[2].  Shown as explicit arg so data flow
		//  calcs will be right.
		if (ILOC.verbose) System.out.println("about to return to " + retAddr);
		ILOC.setPc(retAddr);
		break;
		}
	case STOP:
		System.out.println("STOP.");
		ILOC.running = false;
		break;
	case ERROR:
		Main.error("executing ERROR instruction, continuing");
		break;
	default:
		System.out.println(" ERROR -- unrecog instr: " + toString());
		ILOC.running = false;
	} // end switch(opcode)

	} // end execute

	// convert boolean to int so we can handle as "normal" machine value
	int bool2int(boolean b)
	{
		if (b)
			return 1;
		else 
			return 0;
	}


} // end class IlocInstr
