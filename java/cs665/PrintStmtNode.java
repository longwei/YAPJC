package cs665;
import cs665ILOC.*;


class PrintStmtNode extends StmtNode
{
	// fields
	// child (expression)


	// constructor

	// toString
	ExprNode exNode;
	Type type;


	public PrintStmtNode(ExprNode node)
	{
		exNode = node;
		if (verbose) 
			System.out.println("Creating PrintStmtNode: " + node);
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
		System.out.println("PrintStmtNode: CodenGen ");
		if (type.equals(Type.typeFloat) ){
			IlocInstr.makeInstr(Opcode.FOUTPUTR, exNode.CodeGen()).emit();
		} else if (type.equals(Type.typeInt) ){
			IlocInstr.makeInstr(Opcode.OUTPUTR, exNode.CodeGen()).emit();
		} else {
			System.out.println("print stmt error");
	 }	
	}
} // end class PrintStmtNode
