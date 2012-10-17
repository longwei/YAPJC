package cs665;
import cs665ILOC.*;
/** AssignStmtNode
*/

class AssignStmtNode extends StmtNode
{
	// constructor
	// basic value 

  VblRefNode lhs;
	ExprNode rhs;
	Type type;
	
	public AssignStmtNode(VblRefNode l, ExprNode r){
		lhs = l;
		rhs = r;

		if (verbose) 
			System.out.println("Creating AssignStmtNode "
			+ lhs + " = " + rhs
			);
	}


	public String toString()
	{
		return " <"
			+ (lhs == null? "null" : lhs.toString()) + " = "
			+ (rhs == null? "null" : rhs.toString())
			+ "> "
			;
	}
	public void CodeGen(){
		Reg rightReg = rhs.CodeGen();
		IlocInstr.makeStoreInstr(Opcode.STOREAI, rightReg, Reg.rFP, lhs.getOffset(), "Assignment " + lhs.toString() ).emit();
		return;
	}
	
	public void analyze(NameScope ns){
		System.out.println("analyzing AssignStmt...");
		Type lt = lhs.analyze(ns);
		Type rt = rhs.analyze(ns);
		if( !lt.equals(rt) ) 
		{
			System.out.println("AssignStmt error: " + lt + " = " + rt);
		} else {
			type = rt;
		}
		return;
	}	
}