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
        Trie trie = new Trie();
		while ((t = yy.yylex()) != null) {
            if (line != t.m_line) {
                line++;
                System.out.println();
            }
            if (t.m_tokenType.equals("id")) {
                System.out.println(t.m_text);
                trie.checkWord(t.m_text);
            }
            System.out.print(t.m_tokenType + " ");
		}
        System.out.println();
        trie.print();
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


class Yylex {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

  private int comment_count = 0;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		55
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NOT_ACCEPT,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NOT_ACCEPT,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"56:8,52:2,53,56:2,51,56:18,52,2,49,56:2,15,19,56,4,5,13,11,1,12,10,14,46,44" +
":9,56,3,17,16,18,56:2,48:4,45,48,54:17,47,54:2,6,50,7,56,55,56,28,24,31,33," +
"27,36,41,43,21,54,30,26,37,22,25,38,54,29,32,23,34,42,39,35,40,54,8,20,9,56" +
":2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,161,
"0,1:2,2,1:11,3,1,4,5,6,7,8,9,10,1:2,11,1:5,12,13,1,14,12,15,12:17,16,1:3,15" +
",17,18,19,20,21,22,21:2,23,24,25,1,26,27,28,29,30,31,32,33,34,35,20,36,37,3" +
"8,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,6" +
"3,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,8" +
"8,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109," +
"110,111,12,112")[0];

	private int yy_nxt[][] = unpackFromString(113,57,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,60,21,61,117,134,159:2,1" +
"18,159,146,159,135,147,148,159:2,99,159,155,136,159:2,119,159,22,159,62,159" +
":2,67,71,-1,23:2,159,71:2,-1:73,24,-1:53,25,26,-1:58,27,-1:56,28,-1:56,29,-" +
"1:59,30,-1:58,159,68,159:13,32,160,159:11,-1:5,159:2,-1:11,33,-1:33,22,-1,2" +
"2,-1:62,23:2,-1:4,26:50,-1,26,-1,26:3,-1:21,159:28,-1:5,159:2,-1:28,74,-1:1" +
"6,33,74,33,-1:31,159:6,153,159:21,-1:5,159:2,-1:25,37,-1:2,37:2,-1:2,37,-1," +
"37,-1:2,37,-1:7,37:3,-1,37,-1:8,1,65:12,75,78,65:38,56,65:3,-1:20,31,-1:57," +
"159:6,156,159:21,-1:5,159:2,-1:11,33,-1:24,59,-1:8,22,-1,22,59,-1:53,63,-1," +
"63,-1:11,66:48,34,70,66:2,-1,66:3,-1,65:12,84,86,65:38,-1,65:3,-1:21,159:2," +
"35,159:25,-1:5,159:2,-1:2,65:12,69,86,65:38,-1,65:3,-1,66:48,64,70,66,77,80" +
",66:3,-1:21,159:8,36,159:19,-1:5,159:2,-1:2,65:12,84,73,65:38,-1,65:3,-1:11" +
",82:2,-1:31,63,-1,63,-1:11,65:12,69,57,65:38,-1,65:3,-1:21,159:6,38,159:21," +
"-1:5,159:2,-1:2,66:48,34,70,66,77,80,66:3,-1,65:12,58,73,65:38,-1,65:3,-1:2" +
"1,159:6,39,159:21,-1:5,159:2,-1:51,66,-1,80:2,-1:24,159:12,40,159:15,-1:5,1" +
"59:2,-1:22,159:9,41,159:18,-1:5,159:2,-1:2,65:12,69,-1,65:38,-1,65:3,-1:21," +
"159:11,42,159:16,-1:5,159:2,-1:2,65:12,-1,73,65:38,-1,65:3,-1:21,159:6,43,1" +
"59:21,-1:5,159:2,-1:22,159:6,44,159:21,-1:5,159:2,-1:22,159,45,159:26,-1:5," +
"159:2,-1:22,159,46,159:26,-1:5,159:2,-1:22,159:20,47,159:7,-1:5,159:2,-1:22" +
",159:6,48,159:21,-1:5,159:2,-1:22,159,49,159:26,-1:5,159:2,-1:22,159:11,50," +
"159:16,-1:5,159:2,-1:22,159,51,159:26,-1:5,159:2,-1:22,159:19,52,159:8,-1:5" +
",159:2,-1:22,159:6,53,159:21,-1:5,159:2,-1:22,159:11,54,159:16,-1:5,159:2,-" +
"1:22,159:4,72,159:2,122,159:20,-1:5,159:2,-1:22,159:13,76,159:14,-1:5,159:2" +
",-1:22,159:11,79,159:16,-1:5,159:2,-1:22,81,159:27,-1:5,159:2,-1:22,159:7,8" +
"3,159:20,-1:5,159:2,-1:22,159:11,85,159:16,-1:5,159:2,-1:22,159:11,87,159:1" +
"6,-1:5,159:2,-1:22,159:5,88,159:22,-1:5,159:2,-1:22,159:8,89,159:19,-1:5,15" +
"9:2,-1:22,159:5,90,159:22,-1:5,159:2,-1:22,159,91,159:26,-1:5,159:2,-1:22,1" +
"59:5,92,159:22,-1:5,159:2,-1:22,159:7,93,159:20,-1:5,159:2,-1:22,159:12,94," +
"159:15,-1:5,159:2,-1:22,159:5,95,159:22,-1:5,159:2,-1:22,159:7,96,159:20,-1" +
":5,159:2,-1:22,159:10,97,159:17,-1:5,159:2,-1:22,159:2,98,159:25,-1:5,159:2" +
",-1:22,159:8,100,159:19,-1:5,159:2,-1:22,159:5,101,159:8,150,159:13,-1:5,15" +
"9:2,-1:22,159:4,102,159:23,-1:5,159:2,-1:22,159:6,103,159:21,-1:5,159:2,-1:" +
"22,159:7,104,159:20,-1:5,159:2,-1:22,159:5,105,159:22,-1:5,159:2,-1:22,106," +
"159:27,-1:5,159:2,-1:22,159:13,107,159:14,-1:5,159:2,-1:22,159:12,108,159:1" +
"5,-1:5,159:2,-1:22,109,159:27,-1:5,159:2,-1:22,159:3,110,159:24,-1:5,159:2," +
"-1:22,159:6,111,159:21,-1:5,159:2,-1:22,159,112,159:26,-1:5,159:2,-1:22,159" +
":2,113,159:25,-1:5,159:2,-1:22,159:8,114,159:19,-1:5,159:2,-1:22,159:7,115," +
"159:20,-1:5,159:2,-1:22,159,116,159:26,-1:5,159:2,-1:22,159:4,149,159:3,120" +
",159:19,-1:5,159:2,-1:22,159:5,121,159:22,-1:5,159:2,-1:22,159:22,123,159:5" +
",-1:5,159:2,-1:22,159:2,124,159:4,125,159:20,-1:5,159:2,-1:22,159:8,126,159" +
":19,-1:5,159:2,-1:22,159:13,127,159:14,-1:5,159:2,-1:22,159:5,128,159:22,-1" +
":5,159:2,-1:22,159:6,129,159:21,-1:5,159:2,-1:22,159,130,159:26,-1:5,159:2," +
"-1:22,159:8,131,159:19,-1:5,159:2,-1:22,159:15,132,159:12,-1:5,159:2,-1:22," +
"159:6,133,159:21,-1:5,159:2,-1:22,159:6,137,159:21,-1:5,159:2,-1:22,159:2,1" +
"38,159:25,-1:5,159:2,-1:22,159:4,139,159:23,-1:5,159:2,-1:22,159:4,140,159:" +
"23,-1:5,159:2,-1:22,159:2,141,159:25,-1:5,159:2,-1:22,142,159:27,-1:5,159:2" +
",-1:22,159:7,143,159:20,-1:5,159:2,-1:22,159:8,144,159:19,-1:5,159:2,-1:22," +
"159:16,145,159:11,-1:5,159:2,-1:22,159:8,151,159:19,-1:5,159:2,-1:22,159:18" +
",152,159:9,-1:5,159:2,-1:22,159:5,158,159:22,-1:5,159:2,-1:22,159:6,154,159" +
":21,-1:5,159:2,-1:22,159:17,157,159:10,-1:5,159:2,-1");

	public Yytoken yylex ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {
				return null;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return (new Yytoken(0,yytext(),yyline,yychar,yychar+1,"comma")); }
					case -3:
						break;
					case 3:
						{ return (new Yytoken(1,yytext(),yyline,yychar,yychar+1,"not")); }
					case -4:
						break;
					case 4:
						{ return (new Yytoken(2,yytext(),yyline,yychar,yychar+1,"semicolon")); }
					case -5:
						break;
					case 5:
						{ return (new Yytoken(3,yytext(),yyline,yychar,yychar+1,"leftparen")); }
					case -6:
						break;
					case 6:
						{ return (new Yytoken(4,yytext(),yyline,yychar,yychar+1,"rightparen")); }
					case -7:
						break;
					case 7:
						{ return (new Yytoken(5,yytext(),yyline,yychar,yychar+1,"leftbracket")); }
					case -8:
						break;
					case 8:
						{ return (new Yytoken(6,yytext(),yyline,yychar,yychar+1,"rightbracket")); }
					case -9:
						break;
					case 9:
						{ return (new Yytoken(7,yytext(),yyline,yychar,yychar+1,"leftbrace")); }
					case -10:
						break;
					case 10:
						{ return (new Yytoken(8,yytext(),yyline,yychar,yychar+1,"rightbrace")); }
					case -11:
						break;
					case 11:
						{ return (new Yytoken(9,yytext(),yyline,yychar,yychar+1,"period")); }
					case -12:
						break;
					case 12:
						{ return (new Yytoken(10,yytext(),yyline,yychar,yychar+1,"plus")); }
					case -13:
						break;
					case 13:
						{ return (new Yytoken(11,yytext(),yyline,yychar,yychar+1,"minus")); }
					case -14:
						break;
					case 14:
						{ return (new Yytoken(12,yytext(),yyline,yychar,yychar+1,"multiplication")); }
					case -15:
						break;
					case 15:
						{ return (new Yytoken(13,yytext(),yyline,yychar,yychar+1,"division")); }
					case -16:
						break;
					case 16:
						{ return (new Yytoken(14,yytext(),yyline,yychar,yychar+1,"mod")); }
					case -17:
						break;
					case 17:
						{ return (new Yytoken(15,yytext(),yyline,yychar,yychar+1,"assignop")); }
					case -18:
						break;
					case 18:
						{ return (new Yytoken(18,yytext(),yyline,yychar,yychar+1,"less")); }
					case -19:
						break;
					case 19:
						{ return (new Yytoken(20,yytext(),yyline,yychar,yychar+1,"greater")); }
					case -20:
						break;
					case 20:
						{
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
					case -21:
						break;
					case 21:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -22:
						break;
					case 22:
						{ return (new Yytoken(45,yytext(),yyline,yychar,yychar + yytext().length(), "intconstant")); }
					case -23:
						break;
					case 23:
						{ }
					case -24:
						break;
					case 24:
						{ return (new Yytoken(17,yytext(),yyline,yychar,yychar+2,"notequal")); }
					case -25:
						break;
					case 25:
						{ yybegin(COMMENT); comment_count = comment_count + 1; }
					case -26:
						break;
					case 26:
						{ }
					case -27:
						break;
					case 27:
						{ return (new Yytoken(16,yytext(),yyline,yychar,yychar+2,"equal")); }
					case -28:
						break;
					case 28:
						{ return (new Yytoken(19,yytext(),yyline,yychar,yychar+2,"lessequal")); }
					case -29:
						break;
					case 29:
						{ return (new Yytoken(21,yytext(),yyline,yychar,yychar+2,"greaterequal")); }
					case -30:
						break;
					case 30:
						{ return (new Yytoken(22,yytext(),yyline,yychar,yychar+1,"and")); }
					case -31:
						break;
					case 31:
						{ return (new Yytoken(23,yytext(),yyline,yychar,yychar+1,"or")); }
					case -32:
						break;
					case 32:
						{ return (new Yytoken(32,yytext(),yyline,yychar,yychar+2,"if")); }
					case -33:
						break;
					case 33:
						{ return (new Yytoken(44,yytext(),yyline,yychar,yychar + yytext().length(),"doubleconstant")); }
					case -34:
						break;
					case 34:
						{
	String str =  yytext().substring(1,yytext().length() - 1);
	Utility.Assert(str.length() == yytext().length() - 2);
	return (new Yytoken(46,str,yyline,yychar,yychar + str.length(), "stringconstant"));
}
					case -35:
						break;
					case 35:
						{ return (new Yytoken(24,yytext(),yyline,yychar,yychar+3,"int")); }
					case -36:
						break;
					case 36:
						{ return (new Yytoken(31,yytext(),yyline,yychar,yychar+3,"for")); }
					case -37:
						break;
					case 37:
						{ return (new Yytoken(45,yytext(),yyline,yychar,yychar + yytext().length(), "intconstant")); }
					case -38:
						break;
					case 38:
						{ return (new Yytoken(43,yytext(),yyline,yychar,yychar+4,"booleanconstant")); }
					case -39:
						break;
					case 39:
						{ return (new Yytoken(29,yytext(),yyline,yychar,yychar+4,"else")); }
					case -40:
						break;
					case 40:
						{ return (new Yytoken(41,yytext(),yyline,yychar,yychar+4,"void")); }
					case -41:
						break;
					case 41:
						{ return (new Yytoken(26,yytext(),yyline,yychar,yychar+5,"break")); }
					case -42:
						break;
					case 42:
						{ return (new Yytoken(27,yytext(),yyline,yychar,yychar+5,"class")); }
					case -43:
						break;
					case 43:
						{ return (new Yytoken(43,yytext(),yyline,yychar,yychar+5,"booleanconstant")); }
					case -44:
						break;
					case 44:
						{ return (new Yytoken(42,yytext(),yyline,yychar,yychar+5,"while")); }
					case -45:
						break;
					case 45:
						{ return (new Yytoken(39,yytext(),yyline,yychar,yychar+6,"return")); }
					case -46:
						break;
					case 46:
						{ return (new Yytoken(38,yytext(),yyline,yychar,yychar+6,"readln")); }
					case -47:
						break;
					case 47:
						{ return (new Yytoken(40,yytext(),yyline,yychar,yychar+6,"string")); }
					case -48:
						break;
					case 48:
						{ return (new Yytoken(28,yytext(),yyline,yychar,yychar+6,"double")); }
					case -49:
						break;
					case 49:
						{ return (new Yytoken(25,yytext(),yyline,yychar,yychar+7,"boolean")); }
					case -50:
						break;
					case 50:
						{ return (new Yytoken(30,yytext(),yyline,yychar,yychar+7,"extends")); }
					case -51:
						break;
					case 51:
						{ return (new Yytoken(37,yytext(),yyline,yychar,yychar+7,"println")); }
					case -52:
						break;
					case 52:
						{ return (new Yytoken(36,yytext(),yyline,yychar,yychar+8,"newarray")); }
					case -53:
						break;
					case 53:
						{ return (new Yytoken(34,yytext(),yyline,yychar,yychar+9,"interface")); }
					case -54:
						break;
					case 54:
						{ return (new Yytoken(33,yytext(),yyline,yychar,yychar+10,"implements")); }
					case -55:
						break;
					case 55:
						{ }
					case -56:
						break;
					case 56:
						{ }
					case -57:
						break;
					case 57:
						{
	comment_count = comment_count - 1;
	Utility.Assert(comment_count >= 0);
	if (comment_count == 0) {
    		yybegin(YYINITIAL);
	}
}
					case -58:
						break;
					case 58:
						{ comment_count = comment_count + 1; }
					case -59:
						break;
					case 60:
						{
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
					case -60:
						break;
					case 61:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -61:
						break;
					case 62:
						{ return (new Yytoken(45,yytext(),yyline,yychar,yychar + yytext().length(), "intconstant")); }
					case -62:
						break;
					case 63:
						{ return (new Yytoken(44,yytext(),yyline,yychar,yychar + yytext().length(),"doubleconstant")); }
					case -63:
						break;
					case 64:
						{
	String str =  yytext().substring(1,yytext().length() - 1);
	Utility.Assert(str.length() == yytext().length() - 2);
	return (new Yytoken(46,str,yyline,yychar,yychar + str.length(), "stringconstant"));
}
					case -64:
						break;
					case 65:
						{ }
					case -65:
						break;
					case 67:
						{
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
					case -66:
						break;
					case 68:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -67:
						break;
					case 69:
						{ }
					case -68:
						break;
					case 71:
						{
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
					case -69:
						break;
					case 72:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -70:
						break;
					case 73:
						{ }
					case -71:
						break;
					case 75:
						{
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
					case -72:
						break;
					case 76:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -73:
						break;
					case 78:
						{
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
					case -74:
						break;
					case 79:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -75:
						break;
					case 81:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -76:
						break;
					case 83:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -77:
						break;
					case 85:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -78:
						break;
					case 87:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -79:
						break;
					case 88:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -80:
						break;
					case 89:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -81:
						break;
					case 90:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -82:
						break;
					case 91:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -83:
						break;
					case 92:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -84:
						break;
					case 93:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -85:
						break;
					case 94:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -86:
						break;
					case 95:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -87:
						break;
					case 96:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -88:
						break;
					case 97:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -89:
						break;
					case 98:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -90:
						break;
					case 99:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -91:
						break;
					case 100:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -92:
						break;
					case 101:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -93:
						break;
					case 102:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -94:
						break;
					case 103:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -95:
						break;
					case 104:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -96:
						break;
					case 105:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -97:
						break;
					case 106:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -98:
						break;
					case 107:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -99:
						break;
					case 108:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -100:
						break;
					case 109:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -101:
						break;
					case 110:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -102:
						break;
					case 111:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -103:
						break;
					case 112:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -104:
						break;
					case 113:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -105:
						break;
					case 114:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -106:
						break;
					case 115:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -107:
						break;
					case 116:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -108:
						break;
					case 117:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -109:
						break;
					case 118:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -110:
						break;
					case 119:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -111:
						break;
					case 120:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -112:
						break;
					case 121:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -113:
						break;
					case 122:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -114:
						break;
					case 123:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -115:
						break;
					case 124:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -116:
						break;
					case 125:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -117:
						break;
					case 126:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -118:
						break;
					case 127:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -119:
						break;
					case 128:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -120:
						break;
					case 129:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -121:
						break;
					case 130:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -122:
						break;
					case 131:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -123:
						break;
					case 132:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -124:
						break;
					case 133:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -125:
						break;
					case 134:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -126:
						break;
					case 135:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -127:
						break;
					case 136:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -128:
						break;
					case 137:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -129:
						break;
					case 138:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -130:
						break;
					case 139:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -131:
						break;
					case 140:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -132:
						break;
					case 141:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -133:
						break;
					case 142:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -134:
						break;
					case 143:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -135:
						break;
					case 144:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -136:
						break;
					case 145:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -137:
						break;
					case 146:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -138:
						break;
					case 147:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -139:
						break;
					case 148:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -140:
						break;
					case 149:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -141:
						break;
					case 150:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -142:
						break;
					case 151:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -143:
						break;
					case 152:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -144:
						break;
					case 153:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -145:
						break;
					case 154:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -146:
						break;
					case 155:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -147:
						break;
					case 156:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -148:
						break;
					case 157:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -149:
						break;
					case 158:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -150:
						break;
					case 159:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -151:
						break;
					case 160:
						{
	return (new Yytoken(47,yytext(),yyline,yychar,yychar + yytext().length(), "id"));
}
					case -152:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
