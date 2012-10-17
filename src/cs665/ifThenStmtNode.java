package cs665;
import cs665ILOC.*;


class ifThenStmtNode extends StmtNode
{
	// fields
	// child (expression)
	// constructor

	// toString
	// * ifThenStmt ::= "if" "(" boolOpExpr ")" "{" stmt "}"
	ExprNode condition;
	StmtList thenBody;

	public ifThenStmtNode(ExprNode condi,StmtList node)
	{
		condition = condi;
		thenBody = node;
		if (verbose) 
			System.out.println("Creating ifThenStmtNode: " + node);
	}


	public String toString()
	{         
		return "< print " + (condition==null? "null" : condition.toString())
		        + (thenBody==null? "null" : thenBody.toString()) +" >";
	}

	public void analyze(NameScope ns){
		System.out.println("analyzing ifThenStmtNode...");
		Type lt = condition.analyze(ns);
		if( !lt.equals(Type.typeBool) ){
			System.out.println("condition is not a bool ");
		}
		thenBody.analyze(ns);
	}
	
	void CodeGen()
	{
		System.out.println("ifThenStmtNode: CodenGen...");
	  Reg src = condition.CodeGen();
	  IlocInstr theCBRN = IlocInstr.makeBranchInstr(Opcode.CBRN, src).emit();
		thenBody.CodeGen();
		int there = ILOC.nextLoc();
		theCBRN.setTarget(there);
		
	}
} // end class ifThenStmtNode
