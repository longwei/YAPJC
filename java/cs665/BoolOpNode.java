package cs665;
import cs665ILOC.*;

class BoolOpNode extends ExprNode
{
	ExprNode left, right;
	int op;
	
	public BoolOpNode(int o, ExprNode l, ExprNode r)
	{
		left = l;
		right = r;
		op = o;
		if(verbose)
			System.out.println("Creating BoolOpNode "
			+ " op: " + opToString(op)
			+ ", l= " + left
			+ ", r= " + right
			);
	}
	public String toString()
	{
		return " <"
			+ opToString(op) + " "
			+ (left == null? "null" : left.toString()) + " "
			+ (right == null? "null" : right.toString()) 
			+ "> "
			;
	}
	public String opToString(int op)
	{
		switch(op)
		{
			case sym.LESS: 
				return "opLess"; 
			case sym.EQUAL: 
				return "opEqual";
			case sym.GREATER: 
				return "opGreater";
			default:
		 		System.out.println("Boolean operator error: ");
				return "Operr!!";
				
		}	
	}

	Reg CodeGen()
	{
		System.out.println("BoolOpNode CodeGen...");
		Reg firstReg = left.CodeGen();
		Reg secondReg = right.CodeGen();
		System.out.println( firstReg.debugString() );
		System.out.println( secondReg.debugString() );
		Reg result = new Reg();
		switch(op)
		{
			case sym.LESS:
			  IlocInstr.makeInstr(Opcode.CMPLT, result, firstReg, secondReg).emit();
			  break;
			case sym.EQUAL:
				IlocInstr.makeInstr(Opcode.CMPEQ, result, firstReg, secondReg).emit();
				break;
			case sym.GREATER:
				IlocInstr.makeInstr(Opcode.CMPGT, result, firstReg, secondReg).emit();
				break;
			default: 
				System.out.println("BoolOpNode CodeGen error"); 
				break;
		}
		return result;		
	}

	Type analyze(NameScope ns)
	{
		System.out.println("BoolOpNode analyzing");
		Type lt = left.analyze(ns);
		Type rt = right.analyze(ns);
		
		if ( left.equals(right) ) {
				System.out.println("boolOp on same variable");
			}
		
		if( lt != rt )
		{ 
			System.out.println("Varible type " + lt.toString()+"does not match "+	rt.toString());
			return null;
		}else 	
			return Type.typeBool;
	}	
	
}
