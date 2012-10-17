package cs665;
import cs665ILOC.*;


class ReturnStmtNode extends StmtNode
{
	// fields
	// child (expression)


	// constructor
	// toString
	ExprNode exNode;
	Type type;


	public ReturnStmtNode()
	{
		if (verbose) 
			System.out.println("Creating void ReturnStmtNode: ");
	}

	public ReturnStmtNode(ExprNode node)
	{
		exNode = node;
		if (verbose) 
			System.out.println("Creating ReturnStmtNode: " + node);
	}


	public String toString()
	{         
		return "< print "+ (exNode==null? "null" : exNode.toString()) +" >";
	}

	public void analyze(NameScope ns){
		this.type = exNode.analyze(ns);
		System.out.println("analyzing PrintStmt..." + type);
	}

	void CodeGen()
	{
		System.out.println("ReturnStmtNode: CodenGen ");
		IlocInstr.makeReturnInstr(Opcode.RETURN, Reg.rRA, Reg.rVal).emit();
	}
} // end class ReturnStmtNode
