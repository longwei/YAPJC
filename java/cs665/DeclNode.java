package cs665;
import cs665ILOC.*;
/** file contains various kind of DeclNode (Declarations)
* 
*/

class DeclNode extends StmtNode
{	
	protected VblRefNode name;
	protected Type type;
	protected int offset;
	protected Boolean thefinal;
	protected ExprNode initializer;

	DeclNode()
	{
		System.out.println("Creating Declnode\n");
		thefinal = false;
	}

	DeclNode(int type, VblRefNode name, Type t)
	{
		this.name = name;
		this.type = t;
		System.out.println("Creating Declnode < "+ name.toString() + " >\n");
	}
	
	DeclNode(int type, VblRefNode name, ExprNode init, Type t)
	{
		this.name = name;
		this.type = t;
		this.initializer = init;
		System.out.println("Creating Declnode < "+ name.toString() + " >\n");
	}

	/* return name of the declnode */
	public String toString()
	{
		return name.toString();
	}

	public void finalDecl()
	{
		thefinal = true;
		return;
	}

	public Type getType()
	{
		return type;
	}
	
	public int getOffset()
	{
		return offset;
	}
	void CodeGen(){
		if (initializer != null){
			Reg rightReg = initializer.CodeGen();
			IlocInstr.makeStoreInstr(Opcode.STOREAI, rightReg, Reg.rFP, offset, "initializer " + initializer.toString() ).emit();
		}
	}

	void analyze(NameScope ns){
		System.out.println("DeclNode Analyzing...");
		ns.enter(name.toString(), this);
		if (type.equals(type.typeFloat) ){
			offset = ns.myAlloc.reserve(8);
		} 
		else {
			offset = ns.myAlloc.reserve(4);
			}
		}	      
	} // end class DeclNode