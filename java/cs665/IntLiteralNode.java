package cs665;
import cs665ILOC.*;


/**  IntLiteralNode
 *
 * (might decide later to split into different classes for different types)
 */

class IntLiteralNode extends ExprNode 
{
	String sValue;	    // value as a string.

	/* (may also want constructor with integer value?  Add later if so ) */

	/* constructor with String value 
	* (which Scanner returns) 
	*/

	public IntLiteralNode(String sval)
	{
		sValue = sval;
		if (verbose) 
			System.out.println("Creating IntLiteralNode: " + sValue);
	}


	public String toString()
	{
		return sValue;
	}

  Reg CodeGen()
	{
	  System.out.println("IntLiteralNode: CodeGen");
	  Reg newReg = new Reg();
	  IlocInstr.makeInstr(Opcode.LOADI, newReg, 
	      Integer.parseInt(sValue) ).emit();
	  return newReg;
	}
	
	public Type analyze(NameScope ns)
	{
		if (verbose) 
			System.out.println("Analyzing IntLiteralNode: " + sValue);
		return Type.typeInt;
	}
}

