package cs665;
import cs665ILOC.*;


class whileStmtNode extends StmtNode
{
	// fields
	// child (expression)
	// constructor

	ExprNode condition;
	StmtList whileBody;

	public whileStmtNode(ExprNode condi,StmtList node)
	{
		condition = condi;
		whileBody = node;
		if (verbose) 
			System.out.println("Creating whileStmtNode: " + node);
	}


	public String toString()
	{         
		return "< print " + (condition==null? "null" : condition.toString())
		        + (whileBody==null? "null" : whileBody.toString()) +" >";
	}

	public void analyze(NameScope ns){
		System.out.println("analyzing whileStmt...");
		Type lt = condition.analyze(ns);
		if( !lt.equals(Type.typeBool) ){
			System.out.println("condition is not a bool ");
		}
		whileBody.analyze(ns);
	}
	
	void CodeGen()
	{
		System.out.println("whileStmtNode: CodenGen...");
		int here = ILOC.nextLoc();
	  Reg src = condition.CodeGen();
	  IlocInstr theCBRN = IlocInstr.makeBranchInstr(Opcode.CBRN, src).emit();
		whileBody.CodeGen();
		IlocInstr.makeBranchInstr(Opcode.BR, null, here).emit();
		int there = ILOC.nextLoc();
		theCBRN.setTarget(there);
	}
} // end class whileStmtNode
