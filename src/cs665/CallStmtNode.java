package cs665;
import cs665ILOC.*;
/** file contains various kind of CallStmtNode (Declarations)
* 
*/

class CallStmtNode extends StmtNode
{	
	protected CallExprNode myinvoke;

	CallStmtNode()
	{
		System.out.println("Creating CallStmtNode\n");
	}

	CallStmtNode(CallExprNode fname)
	{
		myinvoke = fname;
		System.out.println("Creating Declnode < "+ myinvoke.toString() + " >\n");
	}
	
	/* return name of the declnode */
	public String toString()
	{
		return myinvoke.toString();
	}

	void CodeGen(){
		System.out.println("CallStmtNode codeGen...");
		myinvoke.CodeGen();
	}

	void analyze(NameScope ns){
		System.out.println("CallStmtNode Analyzing...");
		myinvoke.analyze(ns);
		}	      
	} // end class CallStmtNode