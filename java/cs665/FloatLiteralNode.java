package cs665;
import cs665ILOC.*;


/**  FloatLiteralNode
*
* Float numeric literal
*/

class FloatLiteralNode extends ExprNode 
{
	String sValue;	    // value as a string.

	/* (may also want constructor with integer value?  Add later if so ) */

	/* constructor with String value 
	* (which Scanner returns) 
	*/
	public FloatLiteralNode(String sval)
{
	sValue = sval;
	if (verbose) 
		System.out.println("Creating FloatLiteralNode: " + sValue);
}


public String toString()
{
	return sValue;
}
public Reg CodeGen()
{

	Reg target = new Reg();

	IlocInstr.makeFLoadiInstr(Opcode.LOADI, target, new Float(sValue)).emit();

	return target;
}

public Type analyze(NameScope ns)
{
	if (verbose) 
		System.out.println("Analyzing FloadLiterallNode: " + sValue);
	return Type.typeFloat;
}

}

