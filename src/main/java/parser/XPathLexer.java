// Generated from XPath.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class XPathLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, EQ=21, IS=22, ID=23, FILENAME=24, SPC=25;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "T__19", "EQ", "IS", "ID", "FILENAME", "SPC"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'/'", "'//'", "'doc(\"'", "'\")'", "'*'", "'.'", "'..'", "'text()'", 
			"'@'", "'('", "')'", "'['", "']'", "','", "'and'", "'or'", "'not'", "'='", 
			"'\"'", "'''"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "EQ", "IS", "ID", 
			"FILENAME", "SPC"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public XPathLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "XPath.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0019\u008a\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014p\b"+
		"\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015v\b"+
		"\u0015\u0001\u0016\u0001\u0016\u0004\u0016z\b\u0016\u000b\u0016\f\u0016"+
		"{\u0001\u0017\u0001\u0017\u0004\u0017\u0080\b\u0017\u000b\u0017\f\u0017"+
		"\u0081\u0001\u0018\u0004\u0018\u0085\b\u0018\u000b\u0018\f\u0018\u0086"+
		"\u0001\u0018\u0001\u0018\u0000\u0000\u0019\u0001\u0001\u0003\u0002\u0005"+
		"\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n"+
		"\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011"+
		"#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/\u00181\u0019\u0001\u0000"+
		"\u0004\u0002\u0000AZaz\u0005\u0000--09AZ__az\u0005\u0000-.09AZ__az\u0003"+
		"\u0000\t\n\r\r  \u008e\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003"+
		"\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007"+
		"\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001"+
		"\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000"+
		"\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000"+
		"\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000"+
		"\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000"+
		"\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000"+
		"\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000"+
		"\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000"+
		")\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001"+
		"\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000"+
		"\u0000\u00013\u0001\u0000\u0000\u0000\u00035\u0001\u0000\u0000\u0000\u0005"+
		"8\u0001\u0000\u0000\u0000\u0007>\u0001\u0000\u0000\u0000\tA\u0001\u0000"+
		"\u0000\u0000\u000bC\u0001\u0000\u0000\u0000\rE\u0001\u0000\u0000\u0000"+
		"\u000fH\u0001\u0000\u0000\u0000\u0011O\u0001\u0000\u0000\u0000\u0013Q"+
		"\u0001\u0000\u0000\u0000\u0015S\u0001\u0000\u0000\u0000\u0017U\u0001\u0000"+
		"\u0000\u0000\u0019W\u0001\u0000\u0000\u0000\u001bY\u0001\u0000\u0000\u0000"+
		"\u001d[\u0001\u0000\u0000\u0000\u001f_\u0001\u0000\u0000\u0000!b\u0001"+
		"\u0000\u0000\u0000#f\u0001\u0000\u0000\u0000%h\u0001\u0000\u0000\u0000"+
		"\'j\u0001\u0000\u0000\u0000)o\u0001\u0000\u0000\u0000+u\u0001\u0000\u0000"+
		"\u0000-w\u0001\u0000\u0000\u0000/}\u0001\u0000\u0000\u00001\u0084\u0001"+
		"\u0000\u0000\u000034\u0005/\u0000\u00004\u0002\u0001\u0000\u0000\u0000"+
		"56\u0005/\u0000\u000067\u0005/\u0000\u00007\u0004\u0001\u0000\u0000\u0000"+
		"89\u0005d\u0000\u00009:\u0005o\u0000\u0000:;\u0005c\u0000\u0000;<\u0005"+
		"(\u0000\u0000<=\u0005\"\u0000\u0000=\u0006\u0001\u0000\u0000\u0000>?\u0005"+
		"\"\u0000\u0000?@\u0005)\u0000\u0000@\b\u0001\u0000\u0000\u0000AB\u0005"+
		"*\u0000\u0000B\n\u0001\u0000\u0000\u0000CD\u0005.\u0000\u0000D\f\u0001"+
		"\u0000\u0000\u0000EF\u0005.\u0000\u0000FG\u0005.\u0000\u0000G\u000e\u0001"+
		"\u0000\u0000\u0000HI\u0005t\u0000\u0000IJ\u0005e\u0000\u0000JK\u0005x"+
		"\u0000\u0000KL\u0005t\u0000\u0000LM\u0005(\u0000\u0000MN\u0005)\u0000"+
		"\u0000N\u0010\u0001\u0000\u0000\u0000OP\u0005@\u0000\u0000P\u0012\u0001"+
		"\u0000\u0000\u0000QR\u0005(\u0000\u0000R\u0014\u0001\u0000\u0000\u0000"+
		"ST\u0005)\u0000\u0000T\u0016\u0001\u0000\u0000\u0000UV\u0005[\u0000\u0000"+
		"V\u0018\u0001\u0000\u0000\u0000WX\u0005]\u0000\u0000X\u001a\u0001\u0000"+
		"\u0000\u0000YZ\u0005,\u0000\u0000Z\u001c\u0001\u0000\u0000\u0000[\\\u0005"+
		"a\u0000\u0000\\]\u0005n\u0000\u0000]^\u0005d\u0000\u0000^\u001e\u0001"+
		"\u0000\u0000\u0000_`\u0005o\u0000\u0000`a\u0005r\u0000\u0000a \u0001\u0000"+
		"\u0000\u0000bc\u0005n\u0000\u0000cd\u0005o\u0000\u0000de\u0005t\u0000"+
		"\u0000e\"\u0001\u0000\u0000\u0000fg\u0005=\u0000\u0000g$\u0001\u0000\u0000"+
		"\u0000hi\u0005\"\u0000\u0000i&\u0001\u0000\u0000\u0000jk\u0005\'\u0000"+
		"\u0000k(\u0001\u0000\u0000\u0000lp\u0005=\u0000\u0000mn\u0005e\u0000\u0000"+
		"np\u0005q\u0000\u0000ol\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000"+
		"p*\u0001\u0000\u0000\u0000qr\u0005=\u0000\u0000rv\u0005=\u0000\u0000s"+
		"t\u0005i\u0000\u0000tv\u0005s\u0000\u0000uq\u0001\u0000\u0000\u0000us"+
		"\u0001\u0000\u0000\u0000v,\u0001\u0000\u0000\u0000wy\u0007\u0000\u0000"+
		"\u0000xz\u0007\u0001\u0000\u0000yx\u0001\u0000\u0000\u0000z{\u0001\u0000"+
		"\u0000\u0000{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000|.\u0001"+
		"\u0000\u0000\u0000}\u007f\u0007\u0000\u0000\u0000~\u0080\u0007\u0002\u0000"+
		"\u0000\u007f~\u0001\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000\u0000"+
		"\u0081\u007f\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000"+
		"\u00820\u0001\u0000\u0000\u0000\u0083\u0085\u0007\u0003\u0000\u0000\u0084"+
		"\u0083\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086"+
		"\u0084\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087"+
		"\u0088\u0001\u0000\u0000\u0000\u0088\u0089\u0006\u0018\u0000\u0000\u0089"+
		"2\u0001\u0000\u0000\u0000\u0006\u0000ou{\u0081\u0086\u0001\u0006\u0000"+
		"\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}