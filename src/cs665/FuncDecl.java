package cs665;
import cs665ILOC.*;
/** file contains various kind of DeclNode (Declarations)
* 
*/

class FuncDecl extends StmtNode
{	
	protected String name;
	protected Type type;
	protected int myfirstaddr;
	protected StmtList body;
	public FuncBodyNameScope myfns;
	int myOffset;

	FuncDecl()
	{
		myfns = new FuncBodyNameScope();
		myfns.myAlloc.funcReserve();
		System.out.println("Creating FuncDecl\n");
	}

	FuncDecl(Type t, String ident, StmtList sl)
	{
		this.name = ident;
		this.type = t;
		this.body = sl;
		this.myfns = new FuncBodyNameScope(name);
		myfns.myAlloc.funcReserve();
		System.out.println("Creating FuncDecl < "+ name.toString() + " >\n");
	}

	/* return name of the declnode */
	public String toString()
	{
		return (name.toString());
	}

	public Type getType()
	{
		return type;
	}

	public int getAddr()
	{
		return myfirstaddr;
		
	}

	void analyze(NameScope ns){
		System.out.println("FuncDecl Analyzing...");
		myfns.setParent(ns);
		ns.enterF(name.toString(), this);
		body.analyze(myfns);
	}

	void CodeGen(){
		System.out.println("FuncDecl codeGen...");
		IlocInstr BR = IlocInstr.makeBranchInstr(Opcode.BR, null).emit();
		myOffset = myfns.getFrameSize();
		myfirstaddr = ILOC.nextLoc();
		//prolog
	  IlocInstr.makeStoreInstr(Opcode.STOREAI, Reg.rFP, Reg.rSP, 0, "save callers FP").emit();
		IlocInstr.createCopyInstr(Reg.rFP, Reg.rSP).emit();//new FP
		IlocInstr.makeStoreInstr(Opcode.STOREAI, Reg.rRA, Reg.rFP, 4, "RA").emit();
		IlocInstr.makeInstr(Opcode.ADDI, Reg.rSP, Reg.rSP, myOffset).emit();
		body.CodeGen();
		//epilog
		IlocInstr.makeLoadInstr(Opcode.LOADAI, Reg.rRA, Reg.rFP, 4, "restore RA").emit();
		IlocInstr.createCopyInstr(Reg.rSP, Reg.rFP).emit();//restore SP
		IlocInstr.makeLoadInstr(Opcode.LOADAI, Reg.rFP, Reg.rSP, 0, "callers FP").emit();
		
		IlocInstr.makeReturnInstr(Opcode.RETURN, Reg.rRA, Reg.rVal).emit();
		//Jump around
		int there = ILOC.nextLoc();
		BR.setTarget(there);
	}
}