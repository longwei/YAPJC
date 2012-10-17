package cs665;

class Type extends Node /* ..? */
{
	// fields
	public static Type typeInt = new TheIntType();
	public static Type typeFloat = new TheFloatType();
	public static Type typeBool = new TheBoolType();
	// functions
	/* basic constructor, should be enough for built-in scalar types */
	Type () 
	{
	}
}

class TheIntType extends Type
{
	TheIntType()
	{
		if (verbose) 
			System.out.println("Begin to Create IntType: " );
	}
	public String toString()
	{
		return "int";
	}
	public int size()
	{
		return 4;
	}
}

class TheFloatType extends Type
{
	TheFloatType()
	{
		if (verbose) 
			System.out.println("Begin to Create FloatType: " );
	}
	public String toString()
	{
		return "float";
	}
	public int size()
	{
		return 8;
	}
}

class TheBoolType extends Type
{
	TheBoolType()
	{
		if (verbose) 
			System.out.println("Begin to Create BoolType: " );
	}
	public String toString()
	{
		return "boolean";
	}
	public int size()
	{
		return 4;
	}
}
