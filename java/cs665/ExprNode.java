package cs665;
import cs665ILOC.*;

/** ExprNode
 * 
 * for nodes which have (compute) a value
 */

public abstract class ExprNode extends Node
{
	// attributes common to all ExprNodes

	Type type;	// data type 

	// functions common to all ExprNodes
	abstract Type analyze(NameScope ns);
	abstract Reg CodeGen();

} // end class ExprNode
