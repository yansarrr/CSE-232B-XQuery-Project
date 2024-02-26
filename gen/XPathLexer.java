// Generated from /Users/shaojie/Desktop/Winter24/CSE232B/CSE-232B-XQuery-Project/src/main/antlr4/XPath.g4 by ANTLR 4.13.1

    package org.cse232b.antlr4;

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
		EQ=18, IS=19, FileName=20, String=21, StringConstant=22, SPC=23;
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
			"EQ", "IS", "FileName", "String", "StringConstant", "SPC"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'/'", "'//'", "'doc(\"'", "'\")'", "'*'", "'.'", "'..'", "'text()'", 
			"'@'", "'('", "')'", "'['", "']'", "','", "'and'", "'or'", "'not'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "EQ", "IS", "FileName", "String", 
			"StringConstant", "SPC"
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
		"\u0004\u0000\u0017\u008f\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0003\u0011f\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0003\u0012l\b\u0012\u0001\u0013\u0004\u0013o\b\u0013\u000b"+
		"\u0013\f\u0013p\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0004\u0014y\b\u0014\u000b\u0014\f\u0014z\u0001\u0015"+
		"\u0004\u0015~\b\u0015\u000b\u0015\f\u0015\u007f\u0001\u0015\u0004\u0015"+
		"\u0083\b\u0015\u000b\u0015\f\u0015\u0084\u0001\u0015\u0001\u0015\u0001"+
		"\u0016\u0004\u0016\u008a\b\u0016\u000b\u0016\f\u0016\u008b\u0001\u0016"+
		"\u0001\u0016\u0000\u0000\u0017\u0001\u0001\u0003\u0002\u0005\u0003\u0007"+
		"\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b"+
		"\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013"+
		"\'\u0014)\u0015+\u0016-\u0017\u0001\u0000\u0003\u0005\u0000..09AZ__az"+
		"\b\u0000 \"\'\',.09;;??AZaz\u0003\u0000\t\n\r\r  \u0095\u0000\u0001\u0001"+
		"\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001"+
		"\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000"+
		"\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000"+
		"\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000"+
		"\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000"+
		"\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000"+
		"\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000"+
		"\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000"+
		"\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'"+
		"\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000"+
		"\u0000\u0000\u0000-\u0001\u0000\u0000\u0000\u0001/\u0001\u0000\u0000\u0000"+
		"\u00031\u0001\u0000\u0000\u0000\u00054\u0001\u0000\u0000\u0000\u0007:"+
		"\u0001\u0000\u0000\u0000\t=\u0001\u0000\u0000\u0000\u000b?\u0001\u0000"+
		"\u0000\u0000\rA\u0001\u0000\u0000\u0000\u000fD\u0001\u0000\u0000\u0000"+
		"\u0011K\u0001\u0000\u0000\u0000\u0013M\u0001\u0000\u0000\u0000\u0015O"+
		"\u0001\u0000\u0000\u0000\u0017Q\u0001\u0000\u0000\u0000\u0019S\u0001\u0000"+
		"\u0000\u0000\u001bU\u0001\u0000\u0000\u0000\u001dW\u0001\u0000\u0000\u0000"+
		"\u001f[\u0001\u0000\u0000\u0000!^\u0001\u0000\u0000\u0000#e\u0001\u0000"+
		"\u0000\u0000%k\u0001\u0000\u0000\u0000\'n\u0001\u0000\u0000\u0000)x\u0001"+
		"\u0000\u0000\u0000+}\u0001\u0000\u0000\u0000-\u0089\u0001\u0000\u0000"+
		"\u0000/0\u0005/\u0000\u00000\u0002\u0001\u0000\u0000\u000012\u0005/\u0000"+
		"\u000023\u0005/\u0000\u00003\u0004\u0001\u0000\u0000\u000045\u0005d\u0000"+
		"\u000056\u0005o\u0000\u000067\u0005c\u0000\u000078\u0005(\u0000\u0000"+
		"89\u0005\"\u0000\u00009\u0006\u0001\u0000\u0000\u0000:;\u0005\"\u0000"+
		"\u0000;<\u0005)\u0000\u0000<\b\u0001\u0000\u0000\u0000=>\u0005*\u0000"+
		"\u0000>\n\u0001\u0000\u0000\u0000?@\u0005.\u0000\u0000@\f\u0001\u0000"+
		"\u0000\u0000AB\u0005.\u0000\u0000BC\u0005.\u0000\u0000C\u000e\u0001\u0000"+
		"\u0000\u0000DE\u0005t\u0000\u0000EF\u0005e\u0000\u0000FG\u0005x\u0000"+
		"\u0000GH\u0005t\u0000\u0000HI\u0005(\u0000\u0000IJ\u0005)\u0000\u0000"+
		"J\u0010\u0001\u0000\u0000\u0000KL\u0005@\u0000\u0000L\u0012\u0001\u0000"+
		"\u0000\u0000MN\u0005(\u0000\u0000N\u0014\u0001\u0000\u0000\u0000OP\u0005"+
		")\u0000\u0000P\u0016\u0001\u0000\u0000\u0000QR\u0005[\u0000\u0000R\u0018"+
		"\u0001\u0000\u0000\u0000ST\u0005]\u0000\u0000T\u001a\u0001\u0000\u0000"+
		"\u0000UV\u0005,\u0000\u0000V\u001c\u0001\u0000\u0000\u0000WX\u0005a\u0000"+
		"\u0000XY\u0005n\u0000\u0000YZ\u0005d\u0000\u0000Z\u001e\u0001\u0000\u0000"+
		"\u0000[\\\u0005o\u0000\u0000\\]\u0005r\u0000\u0000] \u0001\u0000\u0000"+
		"\u0000^_\u0005n\u0000\u0000_`\u0005o\u0000\u0000`a\u0005t\u0000\u0000"+
		"a\"\u0001\u0000\u0000\u0000bf\u0005=\u0000\u0000cd\u0005e\u0000\u0000"+
		"df\u0005q\u0000\u0000eb\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000"+
		"f$\u0001\u0000\u0000\u0000gh\u0005=\u0000\u0000hl\u0005=\u0000\u0000i"+
		"j\u0005i\u0000\u0000jl\u0005s\u0000\u0000kg\u0001\u0000\u0000\u0000ki"+
		"\u0001\u0000\u0000\u0000l&\u0001\u0000\u0000\u0000mo\u0007\u0000\u0000"+
		"\u0000nm\u0001\u0000\u0000\u0000op\u0001\u0000\u0000\u0000pn\u0001\u0000"+
		"\u0000\u0000pq\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000rs\u0005"+
		".\u0000\u0000st\u0005x\u0000\u0000tu\u0005m\u0000\u0000uv\u0005l\u0000"+
		"\u0000v(\u0001\u0000\u0000\u0000wy\u0007\u0000\u0000\u0000xw\u0001\u0000"+
		"\u0000\u0000yz\u0001\u0000\u0000\u0000zx\u0001\u0000\u0000\u0000z{\u0001"+
		"\u0000\u0000\u0000{*\u0001\u0000\u0000\u0000|~\u0005\"\u0000\u0000}|\u0001"+
		"\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f}\u0001\u0000\u0000"+
		"\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0082\u0001\u0000\u0000"+
		"\u0000\u0081\u0083\u0007\u0001\u0000\u0000\u0082\u0081\u0001\u0000\u0000"+
		"\u0000\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000"+
		"\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000"+
		"\u0000\u0086\u0087\u0005\"\u0000\u0000\u0087,\u0001\u0000\u0000\u0000"+
		"\u0088\u008a\u0007\u0002\u0000\u0000\u0089\u0088\u0001\u0000\u0000\u0000"+
		"\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u0089\u0001\u0000\u0000\u0000"+
		"\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000"+
		"\u008d\u008e\u0006\u0016\u0000\u0000\u008e.\u0001\u0000\u0000\u0000\b"+
		"\u0000ekpz\u007f\u0084\u008b\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}