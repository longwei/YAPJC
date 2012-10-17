package cs665;
/** file contains various kind of StmtNode
* 
*/

/** AssignStmt -- for assignment statements */
import java.util.*;
import cs665ILOC.*;

/* StmtList-- for Stmt list */
class StmtList extends StmtNode
{
	Vector<StmtNode> stmtlist = new Vector<StmtNode>();
	{
		System.out.println("Stmtlist is created!\n");
	}

	public void add(StmtNode st)
	{
		System.out.println("add a stmt " +st + " \n");
		stmtlist.add(st);
	}
	public Vector<StmtNode> getStmtList()
	{
		return stmtlist;
	}
	public void analyze(NameScope ns)
	{
		System.out.println("StmtList analyzing");
		for(StmtNode s:stmtlist){ 
			s.analyze(ns);  
		}
	}
	public void CodeGen()
	{
		for(StmtNode s:stmtlist){
			s.CodeGen();
		} 
	}

}