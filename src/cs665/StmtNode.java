package cs665;
import cs665ILOC.*;
/** StmtNode
 * 
 * for nodes which do NOT have a value, including declarations
 */

public abstract class StmtNode extends Node
{
	// attributes common to all StmtNodes

	// functions common to all StmtNodes
	abstract void analyze(NameScope ns);
	abstract void CodeGen();
} // end class StmtNode
