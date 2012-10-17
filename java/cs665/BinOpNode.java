package cs665;
import cs665ILOC.*;
/** BinOpNode
*
* Binary operators, a kind of expression node
*  Handles things like +, -, * ... note that the grammar
* has taken care of precedence (grouping) so we can use
* the same Node class for all the operators.
*
*/

class BinOpNode extends ExprNode
{
	// basic data //
	int op;	    // operator.   values here are sym.  (sym.PLUS, etc)
	ExprNode left, right;	// children
	Type type;

	/** constructor */
	public BinOpNode(int o, ExprNode l, ExprNode r)
	{
		left = l;
		right = r;
		op = o;
		// always helpful to have debug output available
		if (verbose) 
			System.out.println("Creating BinOpNode "
			+ " op: " + op
			+ ", l= " + left
			+ ", r= " + right
			);
	}

	// there will be other routines


	/** toString
	*print node and subtrees in prefix form
	*/

	public String toString()
{
	return " <"
		+ opToString(op) + " "
		+ left.toString() + " "
		+ right.toString()
		+ "> "
		;
}

/** opToString -- local utility --
* convert int "op" value to string for printout */
String opToString(int op)
{
	switch (op)
	{
		case sym.PLUS:
		return "opPLUS";
		case sym.MINUS:
		return "opMINUS";
		case sym.TIMES:
		return "opTIMES";
		case sym.DIVIDE:
		return "opDIVIDE";
		default:
		System.out.println("Error, unrec operator " + op);
		return " <unk> ";
	}
}

public Type analyze(NameScope ns)
{
	System.out.println("BinOpNode: analyze...");
	Type lt = left.analyze(ns);
	Type rt = right.analyze(ns);
	if( !lt.equals(rt) ){
		System.out.println("type dismatch "+ lt.toString()+ " - " + rt.toString());
		//TODO type casting CUT
	} else { 
		type = lt;
	}
	return lt; 
}


Reg CodeGen()
{
	System.out.println("BinOpNode: CodeGen");
	Reg leftReg = left.CodeGen();
	Reg rightReg = right.CodeGen();
	Reg result = new Reg();
	//System.out.println("***op" + op + "opcode" + Opcode.ADD);
	switch (op)
	{
		case sym.PLUS:
		if (type.equals(Type.typeFloat) ){
			IlocInstr.makeInstr(Opcode.FADD, result, leftReg, rightReg).emit();
		} else{
			IlocInstr.makeInstr(Opcode.ADD, result, leftReg, rightReg).emit();
		}
		break;
		case sym.MINUS:
		if (type.equals(Type.typeFloat) ){
			IlocInstr.makeInstr(Opcode.FSUB, result, leftReg, rightReg).emit();
		} else{
			IlocInstr.makeInstr(Opcode.SUB, result, leftReg, rightReg).emit();
		}
		break;
		case sym.TIMES:
		if (type.equals(Type.typeFloat) ){
			IlocInstr.makeInstr(Opcode.FMULT, result, leftReg, rightReg).emit();
		} else{
			IlocInstr.makeInstr(Opcode.MULT, result, leftReg, rightReg).emit();
		}
		break;
		case sym.DIVIDE:
		if (type.equals(Type.typeFloat) ){
			IlocInstr.makeInstr(Opcode.FDIV, result, leftReg, rightReg).emit();
		} else{
			IlocInstr.makeInstr(Opcode.DIV, result, leftReg, rightReg).emit();
		}
		break;
		default:
		System.out.println("Error, undefined operator " + op);
	}
	return result;
}


} // end BinOpNode

