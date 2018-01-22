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
            if(line!=t.m_line){
                line++;
                System.out.println();
            }
	        //System.out.print("(" + t + ": " + t.m_text + ")");
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
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \t\b\012]
WHITE_SPACE_CHAR=[\n\ \t\b\012]
STRING_TEXT=(\\\"|[^\n\"]|\\{WHITE_SPACE_CHAR}+\\)*
COMMENT_TEXT=([^/*\n]|[^*\n]"/"[^*\n]|[^/\n]"*"[^/\n]|"*"[^/\n]|"/"[^*\n])*


%%

<YYINITIAL> "," { return (new Yytoken(0,yytext(),yyline,yychar,yychar+1,"comma")); }
<YYINITIAL> ":" { return (new Yytoken(1,yytext(),yyline,yychar,yychar+1,"colon")); }
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
<YYINITIAL> "=" { return (new Yytoken(14,yytext(),yyline,yychar,yychar+1,"equal")); }
<YYINITIAL> "<>" { return (new Yytoken(15,yytext(),yyline,yychar,yychar+2,"shit")); }
<YYINITIAL> "<"  { return (new Yytoken(16,yytext(),yyline,yychar,yychar+1,"less")); }
<YYINITIAL> "<=" { return (new Yytoken(17,yytext(),yyline,yychar,yychar+2,"lessequal")); }
<YYINITIAL> ">"  { return (new Yytoken(18,yytext(),yyline,yychar,yychar+1,"greater")); }
<YYINITIAL> ">=" { return (new Yytoken(19,yytext(),yyline,yychar,yychar+2,"greaterequal")); }
<YYINITIAL> "&"  { return (new Yytoken(20,yytext(),yyline,yychar,yychar+1,"and")); }
<YYINITIAL> "|"  { return (new Yytoken(21,yytext(),yyline,yychar,yychar+1,"or")); }
<YYINITIAL> ":=" { return (new Yytoken(22,yytext(),yyline,yychar,yychar+2,"penis")); }

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

<YYINITIAL> \"{STRING_TEXT}\" {
	String str =  yytext().substring(1,yytext().length() - 1);

	Utility.Assert(str.length() == yytext().length() - 2);
	return (new Yytoken(40,str,yyline,yychar,yychar + str.length(), "String"));
}
<YYINITIAL> \"{STRING_TEXT} {
	String str =  yytext().substring(1,yytext().length());

	Utility.error(Utility.E_UNCLOSEDSTR);
	Utility.Assert(str.length() == yytext().length() - 1);
	return (new Yytoken(41,str,yyline,yychar,yychar + str.length(), "Unclosed String"));
}
<YYINITIAL> {DIGIT}+ {
	return (new Yytoken(42,yytext(),yyline,yychar,yychar + yytext().length(), "Number"));
}
<YYINITIAL> {ALPHA}({ALPHA}|{DIGIT}|_)* {
	return (new Yytoken(43,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
<YYINITIAL,COMMENT> . {
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
