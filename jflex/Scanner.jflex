package cs665;

import java_cup.runtime.SymbolFactory;
%%
%cup
%class Scanner
%{
	public Scanner(java.io.InputStream r, SymbolFactory sf){
		this(r);
		this.sf=sf;
	}
	private SymbolFactory sf;
%}
%eofval{
    return sf.newSymbol("EOF",sym.EOF);
%eofval}

Ident = [a-zA-Z] [a-zA-Z0-9]*

%%

/* operators and special characters */
";" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Semicolon",   sym.SEMI);   }
"+" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Plus",        sym.PLUS);   }
"-" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Minus",       sym.MINUS);  }
"*" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Times",       sym.TIMES);  }
"/" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Divide",      sym.DIVIDE); }
"(" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Left Paren",  sym.LPAREN); }
")" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Right Paren", sym.RPAREN); }
"{" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Left CurlyB",  sym.LCURLY); }
"}" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Right CurlyB", sym.RCURLY); }
"=" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Assign",      sym.ASSIGN); }
"<" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Less",        sym.LESS);   }
">" { System.out.println("  Scan: '"+yytext()+"'"); return sf.newSymbol("Greater",     sym.GREATER); }
"==" { System.out.println(" Scan: '"+yytext()+"'"); return sf.newSymbol("Equal",       sym.EQUAL);  }


/* KEYWORDS */
"print" { System.out.println("  Scan: keyword '"+yytext()+"'"); return sf.newSymbol("PRINT",   sym.PRINT); } 
"float" { System.out.println("  Scan: keyword '"+yytext()+"'"); return sf.newSymbol("FLOAT",   sym.FLOAT); } 
"int"   { System.out.println("  Scan: keyword '"+yytext()+"'"); return sf.newSymbol("INT",     sym.INT); }
"final" { System.out.println("  Scan: keyword '"+yytext()+"'"); return sf.newSymbol("FINAL",   sym.FINAL); }
"if"    { System.out.println("  Scan: keyword '"+yytext()+"'"); return sf.newSymbol("IF",      sym.IF); }
"while" { System.out.println("  Scan: keyword '"+yytext()+"'"); return sf.newSymbol("WHILE",   sym.WHILE); }
"boolean" {System.out.println(" Scan: keyword '"+yytext()+"'"); return sf.newSymbol("BOOLEAN", sym.BOOLEAN); }
"return" { System.out.println(" Scan: keyword '"+yytext()+"'"); return sf.newSymbol("RETURN", sym.RETURN); }

/* TRUE */
"true"  { System.out.println("  Scan: keyword '"+yytext()+"'"); return sf.newSymbol("TRUE",    sym.TRUE); }
/* FALSE */
"false" { System.out.println("  Scan: keyword '"+yytext()+"'"); return sf.newSymbol("FALSE",   sym.FALSE); }

/* NUMBER */
[0-9]+ { System.out.println("  Scan: NUMBER "+yytext()); return sf.newSymbol("Integral Number",sym.NUMBER, yytext()); }

/* FLOATNUMBER */
(0|[1-9][0-9]*)("."[0-9]+) { System.out.println("  Scan: floatnumber "+yytext());
                           return sf.newSymbol("FLOATNUMBER",sym.FLOATNUMBER, yytext()); }

/* Names */
{Ident}           {System.out.println("  Scan: IDENT " + yytext()); return sf.newSymbol("Identifier", sym.IDENT, yytext()); }

[ \t\r\n\f] { /* ignore white space. (do nothing) */ }
. { System.err.println("Illegal character: '"+yytext()+"'"); }
