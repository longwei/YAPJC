package cs665;
import java.util.*;
/** file contains various kind of NameScope
* 
*/
class NameScope
{	
	String namescope;
	HashMap<String, DeclNode> hm;
	HashMap<String, FuncDecl> fdhm;
	Allocator myAlloc = new Allocator();
	//HashTable
	NameScope()
	{
		hm = new HashMap<String, DeclNode>();
		fdhm = new HashMap<String, FuncDecl>();
		System.out.println("Creating empty NameScope\n");
	}
	NameScope(String ns)
	{
		namescope = ns;
		hm = new HashMap<String, DeclNode>();
		fdhm = new HashMap<String, FuncDecl>();
		System.out.println("Creating NameScope: " + ns +"\n");

	}
	/* return name of the declnode */
	public String toString()
	{
		return namescope;
	}

	public DeclNode lookUp(String name){
		System.out.println("lookup " + name + " ...");
		return hm.get(name);
	}

	public void enter(String name, DeclNode declnode){
		System.out.println(namescope + " "+ name +" entered");
		hm.put(name, declnode);	
	}
	
	public FuncDecl lookUpF(String name){
		System.out.println("lookupF function " + name + " ...");
		FuncDecl temp = fdhm.get(name);
		if (temp == null) {System.out.println("not found here NS in " + this.toString() );} 
		return temp;
	}

	public void enterF(String name, FuncDecl funcdecl){
		System.out.println(namescope + " function "+ name +" enteredF");
		fdhm.put(name, funcdecl);	
	}
} // end class DeclNode

class Allocator{
	//keeps track of the nextFree location in the current stack frame
	static int nextFree = 0;
	Allocator()
	{
		System.out.println("Creating allocator\n");
	}
	int reserve(int sizeofDeclNode){
		nextFree += sizeofDeclNode;
		System.out.println("reserved sizeof " + sizeofDeclNode +"\n");
		return nextFree;
	}
	void funcReserve(){
		nextFree += 8;
	}
	int getReserved(){
		return nextFree;
	}
}