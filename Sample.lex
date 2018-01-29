import java.lang.System;

class Sample {
    public static void main(String argv[]) throws java.io.IOException {
		if (argv.length != 1) {
			System.exit(-1);
		}
		java.io.FileInputStream yyin = null;
		try {
			yyin = new java.io.FileInputStream(argv[0]);
		}catch (Exception e){
			System.exit(-1);
		}
		// lex is a JLex-generated scanner that
		// will read from yyin
		Yylex yy = new Yylex(yyin);
		Yytoken t;

        int line = 0;
		while ((t = yy.yylex()) != null) {
            if (line != t.m_line) {
                line++;
                System.out.println();
            }
            System.out.print(t.m_tokenType + " ");
		}
        System.out.println();
	}
}

class Utility {
  public static void Assert
    (
     boolean expr
     )
      {
	if (false == expr) {
	  throw (new Error("Error: Assertion failed."));
	}
      }

  private static final String errorMsg[] = {
    "Error: Unmatched end-of-comment punctuation.",
    "Error: Unmatched start-of-comment punctuation.",
    "Error: Unclosed string.",
    "Error: Illegal character."
    };

  public static final int E_ENDCOMMENT = 0;
  public static final int E_STARTCOMMENT = 1;
  public static final int E_UNCLOSEDSTR = 2;
  public static final int E_UNMATCHED = 3;

  public static void error
    (
     int code
     )
      {
	System.out.println(errorMsg[code]);
      }
}

class Yytoken {
  Yytoken
    (
     int index,
     String text,
     int line,
     int charBegin,
     int charEnd,
	 String tokenType
     )
      {
	m_index = index;
	m_text = new String(text);
	m_line = line;
	m_charBegin = charBegin;
	m_charEnd = charEnd;
	m_tokenType = tokenType;
      }

  public int m_index;
  public String m_text;
  public int m_line;
  public int m_charBegin;
  public int m_charEnd;
  public String m_tokenType;
  public String toString() {
      return m_tokenType+": "+m_text + " " + m_line;
  }
}

%%

%{
  private int comment_count = 0;
%}
%line
%char
%state COMMENT

ALPHA=[A-Za-z]
HEXDIGIT=[0-9A-Fa-f]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \t\b\012]
WHITE_SPACE_CHAR=[\n\ \t\b\012]
STRING_TEXT=(\\\"|[^\n\"]|\\{WHITE_SPACE_CHAR}+\\)*
COMMENT_TEXT=([^/*\n]|[^*\n]"/"[^*\n]|[^/\n]"*"[^/\n]|"*"[^/\n]|"/"[^*\n])*


%%

<YYINITIAL> "," { return (new Yytoken(0,yytext(),yyline,yychar,yychar+1,"comma")); }
<YYINITIAL> "!" { return (new Yytoken(1,yytext(),yyline,yychar,yychar+1,"not")); }
<YYINITIAL> ";" { return (new Yytoken(2,yytext(),yyline,yychar,yychar+1,"semicolon")); }
<YYINITIAL> "(" { return (new Yytoken(3,yytext(),yyline,yychar,yychar+1,"leftparen")); }
<YYINITIAL> ")" { return (new Yytoken(4,yytext(),yyline,yychar,yychar+1,"rightparen")); }
<YYINITIAL> "[" { return (new Yytoken(5,yytext(),yyline,yychar,yychar+1,"leftbracket")); }
<YYINITIAL> "]" { return (new Yytoken(6,yytext(),yyline,yychar,yychar+1,"rightbracket")); }
<YYINITIAL> "{" { return (new Yytoken(7,yytext(),yyline,yychar,yychar+1,"leftbrace")); }
<YYINITIAL> "}" { return (new Yytoken(8,yytext(),yyline,yychar,yychar+1,"rightbrace")); }
<YYINITIAL> "." { return (new Yytoken(9,yytext(),yyline,yychar,yychar+1,"period")); }
<YYINITIAL> "+" { return (new Yytoken(10,yytext(),yyline,yychar,yychar+1,"plus")); }
<YYINITIAL> "-" { return (new Yytoken(11,yytext(),yyline,yychar,yychar+1,"minus")); }
<YYINITIAL> "*" { return (new Yytoken(12,yytext(),yyline,yychar,yychar+1,"multiplication")); }
<YYINITIAL> "/" { return (new Yytoken(13,yytext(),yyline,yychar,yychar+1,"division")); }
<YYINITIAL> "%" { return (new Yytoken(14,yytext(),yyline,yychar,yychar+1,"mod")); }
<YYINITIAL> "=" { return (new Yytoken(15,yytext(),yyline,yychar,yychar+1,"assignop")); }
<YYINITIAL> "==" { return (new Yytoken(16,yytext(),yyline,yychar,yychar+2,"equal")); }
<YYINITIAL> "!=" { return (new Yytoken(17,yytext(),yyline,yychar,yychar+2,"notequal")); }
<YYINITIAL> "<"  { return (new Yytoken(18,yytext(),yyline,yychar,yychar+1,"less")); }
<YYINITIAL> "<=" { return (new Yytoken(19,yytext(),yyline,yychar,yychar+2,"lessequal")); }
<YYINITIAL> ">"  { return (new Yytoken(20,yytext(),yyline,yychar,yychar+1,"greater")); }
<YYINITIAL> ">=" { return (new Yytoken(21,yytext(),yyline,yychar,yychar+2,"greaterequal")); }
<YYINITIAL> "&&"  { return (new Yytoken(22,yytext(),yyline,yychar,yychar+1,"and")); }
<YYINITIAL> "||"  { return (new Yytoken(23,yytext(),yyline,yychar,yychar+1,"or")); }
<YYINITIAL> "int" { return (new Yytoken(24,yytext(),yyline,yychar,yychar+3,"int")); }
<YYINITIAL> "boolean" { return (new Yytoken(25,yytext(),yyline,yychar,yychar+7,"boolean")); }
<YYINITIAL> "break" { return (new Yytoken(26,yytext(),yyline,yychar,yychar+5,"break")); }
<YYINITIAL> "class" { return (new Yytoken(27,yytext(),yyline,yychar,yychar+5,"class")); }
<YYINITIAL> "double" { return (new Yytoken(28,yytext(),yyline,yychar,yychar+6,"double")); }
<YYINITIAL> "else" { return (new Yytoken(29,yytext(),yyline,yychar,yychar+4,"else")); }
<YYINITIAL> "extends" { return (new Yytoken(30,yytext(),yyline,yychar,yychar+7,"extends")); }
<YYINITIAL> "for" { return (new Yytoken(31,yytext(),yyline,yychar,yychar+3,"for")); }
<YYINITIAL> "if" { return (new Yytoken(32,yytext(),yyline,yychar,yychar+2,"if")); }
<YYINITIAL> "implements" { return (new Yytoken(33,yytext(),yyline,yychar,yychar+10,"implements")); }
<YYINITIAL> "interface" { return (new Yytoken(34,yytext(),yyline,yychar,yychar+9,"interface")); }
<YYINITIAL> "extends" { return (new Yytoken(35,yytext(),yyline,yychar,yychar+7,"extends")); }
<YYINITIAL> "newarray" { return (new Yytoken(36,yytext(),yyline,yychar,yychar+8,"newarray")); }
<YYINITIAL> "println" { return (new Yytoken(37,yytext(),yyline,yychar,yychar+7,"println")); }
<YYINITIAL> "readln" { return (new Yytoken(38,yytext(),yyline,yychar,yychar+6,"readln")); }
<YYINITIAL> "return" { return (new Yytoken(39,yytext(),yyline,yychar,yychar+6,"return")); }
<YYINITIAL> "string" { return (new Yytoken(40,yytext(),yyline,yychar,yychar+6,"string")); }
<YYINITIAL> "void" { return (new Yytoken(41,yytext(),yyline,yychar,yychar+4,"void")); }
<YYINITIAL> "while" { return (new Yytoken(42,yytext(),yyline,yychar,yychar+5,"while")); }
<YYINITIAL> "true" { return (new Yytoken(43,yytext(),yyline,yychar,yychar+4,"booleanconstant")); }
<YYINITIAL> "false" { return (new Yytoken(43,yytext(),yyline,yychar,yychar+5,"booleanconstant")); }
<YYINITIAL> {DIGIT}+"."{DIGIT}*(("E"|"e")("+"|"-")?{DIGIT}+)? { return (new Yytoken(44,yytext(),yyline,yychar,yychar + yytext().length(),"doubleconstant")); }
<YYINITIAL> {DIGIT}+ { return (new Yytoken(45,yytext(),yyline,yychar,yychar + yytext().length(), "intconstant")); }
<YYINITIAL> ("0X"|"0x"){HEXDIGIT}+ { return (new Yytoken(45,yytext(),yyline,yychar,yychar + yytext().length(), "intconstant")); }
<YYINITIAL> \"{STRING_TEXT}\" {
	String str =  yytext().substring(1,yytext().length() - 1);

	Utility.Assert(str.length() == yytext().length() - 2);
	return (new Yytoken(46,str,yyline,yychar,yychar + str.length(), "stringconstant"));
}
<YYINITIAL> {ALPHA}({ALPHA}|{DIGIT}|_)* {
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}

<YYINITIAL> {NONNEWLINE_WHITE_SPACE_CHAR}+ { }

<YYINITIAL,COMMENT> \n { }

<YYINITIAL> "/*" { yybegin(COMMENT); comment_count = comment_count + 1; }

<COMMENT> "/*" { comment_count = comment_count + 1; }
<COMMENT> "*/" {
	comment_count = comment_count - 1;
	Utility.Assert(comment_count >= 0);
	if (comment_count == 0) {
    		yybegin(YYINITIAL);
	}
}
<COMMENT> {COMMENT_TEXT} { }

<YYINITIAL> "//".* { }
<YYINITIAL> {ALPHA}({ALPHA}|{DIGIT}|_)* {
	return (new Yytoken(43,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
<YYINITIAL,COMMENT> . {
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
