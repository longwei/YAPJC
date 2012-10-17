package cs665;
import cs665ILOC.*;
import java_cup.runtime.*;
public class Main
{
	public static void main(String argv[]) 
	{
		// interface to jFlex scanner
		Scanner scanner = null;
		SymbolFactory sf = new DefaultSymbolFactory();
		
		try 
		{
			// if no args given on command line, read from keyboard
			// - otherwise, arg is name of file to read.
			if (argv.length==0)
			{
				scanner = new Scanner(System.in, sf);
				System.err.println ("Begin processing from keyboard");
			}
			else
			{
				scanner = new Scanner(new java.io.FileInputStream(argv[0]), sf);
				System.err.println ("Begin processing " + argv[0]);
			}

			// create a Parser and run it //
			StmtNode result = (StmtNode)(new Parser(scanner, sf).parse().value);
			// now print the tree (invoking Node.toString)
			System.out.println("Result of Parse: " + result);		

			NameScope ns = new NameScope("main");
			System.out.println("Begin analyze ----------- ");
      result.analyze(ns);
      System.out.println("analyze done----------- ");	
			// program tree is at 'result'.  Perform other actions ...
			// Now generate code
			System.out.println("Begin codeGen ----------- ");
			ILOC.init();
			result.CodeGen();//	theTree.codeGen();
			IlocInstr.makeInstr(Opcode.STOP).emit();    // don't fall of the end.
			ILOC.debugPrintAllInstrs();

			// --- now execute the program! ---
			ILOC.run();
			System.out.println("all done ");


		}
		catch (java.io.FileNotFoundException e) {
			System.err.println("File not found : \""+argv[0]+"\"");
		}
		catch (java.io.IOException e) {
			System.err.println("IO error scanning file \""+argv[0]+"\"");
			System.err.println(e);
		}
		catch (Exception e) {
			System.err.println("Unexpected exception:");
			e.printStackTrace();
		}
	}

	//////////////////////////////	
	/* some error routines that might be useful */

	/** Main.error 
	 *  Report an error in the source program detected by the compiler
	 * Print on System.err so message will get out even if output is
	 * being redirected to a file.  Warning:  this can cause sync confusion
	 * with normal console output.  (flush() first?)
	 */
	public static void error(String msg)
	{
		System.err.println("** Error:  " + msg);
	}


	/** Main.internalError 
	 *  Report an error inside the compiler -- such as a pointer that
	 *  shouldn't be null at this point.  (also known as an assertion)
	 *  Exits, since things are obviously fouled up.
	 */

	public static void internalError(String msg)
	{
		System.err.println("***** Internal Error:  \n "
				   + msg
				   + "\n stopping..");
		System.exit(1);
	}
	

}

