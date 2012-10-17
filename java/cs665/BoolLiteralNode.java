package cs665;
import cs665ILOC.*;


/**  BoolLiteralNode
 *
 * Numeric (integer?) literal
 * (might decide later to split into different classes for different types)
 */

class BoolLiteralNode extends ExprNode 
{
	int isTrue;
	
	public BoolLiteralNode(int boolCode)
	{
		switch (boolCode)
		{
			case sym.TRUE:
			isTrue = 1;
			break;
			case sym.FALSE:
			isTrue = 0;
			break;
			default:
			System.out.println("Error, unrec bool ");
		}
	}


	public String toString()
	{
		if ( isTrue == 1) {return "TRUE";
		} else {return "FALSE";}
	}

  Reg CodeGen()
	{
	  System.out.println("BoolLiteralNode: CodeGen");
	  Reg retReg = new Reg();
	  IlocInstr.makeInstr(Opcode.LOADI, retReg, isTrue).emit();
	  return retReg;
	}
	
	public Type analyze(NameScope ns)
	{
		if (verbose) 
			System.out.println("Analyzing BoolLiteralNode: " + this.toString() );
		return Type.typeBool;
	}
}

