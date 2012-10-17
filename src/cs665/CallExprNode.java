package cs665;
import cs665ILOC.*;

/**  CallExprNode
 *
 * Reference to a name (variable).  
 * Build this for an apparently scalar refernce, i.e., just a name, no subscripts.
 */

class CallExprNode extends ExprNode 
{
	String fName;	    // text of name, as written
	// will be more as we do more analysis
	FuncDecl myFuncDecl;
	int myfirstInstr;

	/** constructor */
	public CallExprNode(String sval)
	{
		fName = sval;
		if (verbose) 
			System.out.println("Creating CallExprNode: " + fName);
	
	}

	public String toString()
	{
		return fName;
	}
	
	public Type analyze(NameScope ns)
	{
		System.out.println("analyzing CallExprNode(default int)" + fName);
		if (ns instanceof FuncBodyNameScope){
			myFuncDecl = ((FuncBodyNameScope)ns).lookUpF(fName);
			while (myFuncDecl == null){
				myFuncDecl = ((FuncBodyNameScope)ns).lookUpFParent(fName);
			}
		} else {
			myFuncDecl = ns.lookUpF(fName);
			System.out.println("CallExprNode Analyze a main scope");}
		System.out.println("find " + myFuncDecl.toString() );
		// myOffset = myDecl.getOffset();
		// System.out.println("return type for " + fName + " " + myDecl.getType().toString() );
		// return myDecl.getType();//FIXME
		return Type.typeInt;
		
	}
	
	Reg CodeGen()
	{
		System.out.println("CallExprNode: codeGen " + fName);
		myfirstInstr = myFuncDecl.getAddr();
		System.out.println("myfirstInstr: " + myfirstInstr);
		IlocInstr.makeCallInstr(Opcode.CALL, Reg.rRA, myfirstInstr, "call "+ myFuncDecl.toString() ).emit();
		return Reg.rVal;
	}
}

