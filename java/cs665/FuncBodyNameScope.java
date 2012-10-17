package cs665;
import java.util.*;
/** file contains various kind of FuncBodyNameScope
* 
*/
class FuncBodyNameScope extends NameScope
{	
	String namescope;
	HashMap<String, DeclNode> localVariablHashMap;
	HashMap<String, FuncDecl> childrenFuncDeclHashMap;
	Allocator myAlloc = new Allocator();
	public NameScope parent;
	// //HashTable
	FuncBodyNameScope()
	{
		// parent = parent;
		localVariablHashMap = new HashMap<String, DeclNode>();
		childrenFuncDeclHashMap = new HashMap<String, FuncDecl>();
		System.out.println("Creating empty FuncBodyNameScope\n");
	}
	FuncBodyNameScope(String ns)
	{
		// parent = parent;
		namescope = ns;
		localVariablHashMap = new HashMap<String, DeclNode>();
		childrenFuncDeclHashMap = new HashMap<String, FuncDecl>();
		System.out.println("Creating FuncBodyNameScope: " + ns +"\n");
	}	
	public int getFrameSize(){
		return myAlloc.getReserved();
	}
	public void setParent(NameScope parent){
		System.out.println( namescope + " is a children of " + parent.toString() );
		this.parent = parent;
	}

	public FuncDecl lookUpFParent(String name){
		System.out.println("lookupFParent function " + name + "in " + parent.toString() );
		FuncDecl temp = null;
		if (parent == null) {System.out.println("parent is null");}
		else {temp = parent.lookUpF(name);
			if (temp == null) {
				System.out.println("not found here FBNS" + namescope);} 
			}
		return temp;
	}
	
	
} // end class DeclNode
