package cs665;
import cs665ILOC.*;

/**  VblRefNode
 *
 * Reference to a name (variable).  
 * Build this for an apparently scalar refernce, i.e., just a name, no subscripts.
 */

class VblRefNode extends ExprNode 
{
	String vName;	    // text of name, as written
	// will be more as we do more analysis
	DeclNode myDecl;
	int myOffset;

	/** constructor */
	public VblRefNode(String sval)
	{
		vName = sval;
		if (verbose) 
			System.out.println("Creating VblRefNode: " + vName);
	
	}

	public String toString()
	{
		return vName;
	}
	
	public int getOffset()
	{
		return myOffset;
	}
	
	public Type analyze(NameScope ns)
	{
		System.out.println("analyzing VblRef" + vName);
		myDecl = ns.lookUp(vName);
		myOffset = myDecl.getOffset();
		System.out.println("return type for " + vName + " " + myDecl.getType().toString() );
		return myDecl.getType();//FIXME
		
	}
	
	Reg CodeGen()
	{
		System.out.println("VblRefNode: CodeGen: " + vName);
		Reg retReg = new Reg();
    IlocInstr.makeLoadInstr(Opcode.LOADAI, retReg, Reg.rFP, myOffset, "vblRef " + vName).emit();
	  return retReg;
	}
}

