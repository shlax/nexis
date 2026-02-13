// Generated from /home/pochodnicky/wrksp/no-git/NXN/src/g4/KeyFrame.g4 by ANTLR 4.13.2
package org.vortex.model.skeleton.animation.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class KeyFrameParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		NAME=10, DIGITS=11, WS=12;
	public static final int
		RULE_keyFrame = 0, RULE_joint = 1, RULE_angles = 2, RULE_angle = 3, RULE_floatNum = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"keyFrame", "joint", "angles", "angle", "floatNum"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "']'", "':'", "','", "'+'", "'-'", "'.'", "'e'", "'E'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "NAME", "DIGITS", 
			"WS"
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

	@Override
	public String getGrammarFileName() { return "KeyFrame.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public KeyFrameParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeyFrameContext extends ParserRuleContext {
		public org.vortex.model.skeleton.animation.ParsedKeyFrame result;
		public JointContext j;
		public JointContext k;
		public List<JointContext> joint() {
			return getRuleContexts(JointContext.class);
		}
		public JointContext joint(int i) {
			return getRuleContext(JointContext.class,i);
		}
		public KeyFrameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyFrame; }
	}

	public final KeyFrameContext keyFrame() throws RecognitionException {
		KeyFrameContext _localctx = new KeyFrameContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_keyFrame);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 java.util.ArrayList<org.vortex.model.skeleton.animation.ParsedJointAngles> l = new java.util.ArrayList<org.vortex.model.skeleton.animation.ParsedJointAngles>();
			setState(11);
			match(T__0);
			setState(12);
			((KeyFrameContext)_localctx).j = joint();
			 l.add(((KeyFrameContext)_localctx).j.r); 
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAME) {
				{
				{
				setState(14);
				((KeyFrameContext)_localctx).k = joint();
				 l.add(((KeyFrameContext)_localctx).k.r); 
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(22);
			match(T__1);
			 ((KeyFrameContext)_localctx).result =  new org.vortex.model.skeleton.animation.ParsedKeyFrame( l.toArray( new org.vortex.model.skeleton.animation.ParsedJointAngles[0]) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JointContext extends ParserRuleContext {
		public org.vortex.model.skeleton.animation.ParsedJointAngles r;
		public Token n;
		public AnglesContext a;
		public TerminalNode NAME() { return getToken(KeyFrameParser.NAME, 0); }
		public AnglesContext angles() {
			return getRuleContext(AnglesContext.class,0);
		}
		public JointContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joint; }
	}

	public final JointContext joint() throws RecognitionException {
		JointContext _localctx = new JointContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_joint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			((JointContext)_localctx).n = match(NAME);
			setState(26);
			match(T__2);
			setState(27);
			((JointContext)_localctx).a = angles();
			 ((JointContext)_localctx).r =  new org.vortex.model.skeleton.animation.ParsedJointAngles( (((JointContext)_localctx).n!=null?((JointContext)_localctx).n.getText():null), ((JointContext)_localctx).a.r );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnglesContext extends ParserRuleContext {
		public org.vortex.model.skeleton.animation.ParsedJointAngle[] r;
		public AngleContext i;
		public AngleContext j;
		public List<AngleContext> angle() {
			return getRuleContexts(AngleContext.class);
		}
		public AngleContext angle(int i) {
			return getRuleContext(AngleContext.class,i);
		}
		public AnglesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_angles; }
	}

	public final AnglesContext angles() throws RecognitionException {
		AnglesContext _localctx = new AnglesContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_angles);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 java.util.ArrayList<org.vortex.model.skeleton.animation.ParsedJointAngle> l = new java.util.ArrayList<org.vortex.model.skeleton.animation.ParsedJointAngle>();
			setState(31);
			match(T__0);
			setState(32);
			((AnglesContext)_localctx).i = angle();
			 l.add(((AnglesContext)_localctx).i.r); 
			setState(40);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(34);
				match(T__3);
				setState(35);
				((AnglesContext)_localctx).j = angle();
				 l.add(((AnglesContext)_localctx).j.r); 
				}
				}
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(43);
			match(T__1);
			 ((AnglesContext)_localctx).r =  l.toArray(new org.vortex.model.skeleton.animation.ParsedJointAngle[0]);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AngleContext extends ParserRuleContext {
		public org.vortex.model.skeleton.animation.ParsedJointAngle r;
		public Token a;
		public FloatNumContext v;
		public TerminalNode NAME() { return getToken(KeyFrameParser.NAME, 0); }
		public FloatNumContext floatNum() {
			return getRuleContext(FloatNumContext.class,0);
		}
		public AngleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_angle; }
	}

	public final AngleContext angle() throws RecognitionException {
		AngleContext _localctx = new AngleContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_angle);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			((AngleContext)_localctx).a = match(NAME);
			setState(47);
			match(T__2);
			setState(48);
			((AngleContext)_localctx).v = floatNum();
			 ((AngleContext)_localctx).r =  new org.vortex.model.skeleton.animation.ParsedJointAngle(org.vortex.math.Axis.valueOf((((AngleContext)_localctx).a!=null?((AngleContext)_localctx).a.getText():null)), ((AngleContext)_localctx).v.r);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FloatNumContext extends ParserRuleContext {
		public float r;
		public Token s;
		public Token n;
		public Token m;
		public Token e;
		public Token p;
		public List<TerminalNode> DIGITS() { return getTokens(KeyFrameParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(KeyFrameParser.DIGITS, i);
		}
		public FloatNumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatNum; }
	}

	public final FloatNumContext floatNum() throws RecognitionException {
		FloatNumContext _localctx = new FloatNumContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_floatNum);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4 || _la==T__5) {
				{
				setState(51);
				((FloatNumContext)_localctx).s = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__4 || _la==T__5) ) {
					((FloatNumContext)_localctx).s = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(54);
			((FloatNumContext)_localctx).n = match(DIGITS);
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(55);
				match(T__6);
				setState(56);
				((FloatNumContext)_localctx).m = match(DIGITS);
				}
			}

			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7 || _la==T__8) {
				{
				setState(59);
				_la = _input.LA(1);
				if ( !(_la==T__7 || _la==T__8) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4 || _la==T__5) {
					{
					setState(60);
					((FloatNumContext)_localctx).e = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__4 || _la==T__5) ) {
						((FloatNumContext)_localctx).e = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(63);
				((FloatNumContext)_localctx).p = match(DIGITS);
				}
			}


			        StringBuilder sb = new StringBuilder();
			        if((((FloatNumContext)_localctx).s!=null?((FloatNumContext)_localctx).s.getText():null) != null){
			            sb.append((((FloatNumContext)_localctx).s!=null?((FloatNumContext)_localctx).s.getText():null));
			        }
			        sb.append((((FloatNumContext)_localctx).n!=null?((FloatNumContext)_localctx).n.getText():null));
			        if((((FloatNumContext)_localctx).m!=null?((FloatNumContext)_localctx).m.getText():null) != null){
			            sb.append('.').append((((FloatNumContext)_localctx).m!=null?((FloatNumContext)_localctx).m.getText():null));
			        }
			        if((((FloatNumContext)_localctx).p!=null?((FloatNumContext)_localctx).p.getText():null) != null){
			            sb.append('E');
			            if((((FloatNumContext)_localctx).e!=null?((FloatNumContext)_localctx).e.getText():null) != null){
			                sb.append((((FloatNumContext)_localctx).e!=null?((FloatNumContext)_localctx).e.getText():null));
			            }
			            sb.append((((FloatNumContext)_localctx).p!=null?((FloatNumContext)_localctx).p.getText():null));
			        }
			        ((FloatNumContext)_localctx).r =  Float.parseFloat(sb.toString());
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\fE\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0005\u0000\u0012\b\u0000\n\u0000\f\u0000\u0015\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\'\b\u0002\n\u0002\f\u0002"+
		"*\t\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0003\u00045\b\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004:\b\u0004\u0001\u0004"+
		"\u0001\u0004\u0003\u0004>\b\u0004\u0001\u0004\u0003\u0004A\b\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0000\u0000\u0005\u0000\u0002\u0004\u0006"+
		"\b\u0000\u0002\u0001\u0000\u0005\u0006\u0001\u0000\b\tE\u0000\n\u0001"+
		"\u0000\u0000\u0000\u0002\u0019\u0001\u0000\u0000\u0000\u0004\u001e\u0001"+
		"\u0000\u0000\u0000\u0006.\u0001\u0000\u0000\u0000\b4\u0001\u0000\u0000"+
		"\u0000\n\u000b\u0006\u0000\uffff\uffff\u0000\u000b\f\u0005\u0001\u0000"+
		"\u0000\f\r\u0003\u0002\u0001\u0000\r\u0013\u0006\u0000\uffff\uffff\u0000"+
		"\u000e\u000f\u0003\u0002\u0001\u0000\u000f\u0010\u0006\u0000\uffff\uffff"+
		"\u0000\u0010\u0012\u0001\u0000\u0000\u0000\u0011\u000e\u0001\u0000\u0000"+
		"\u0000\u0012\u0015\u0001\u0000\u0000\u0000\u0013\u0011\u0001\u0000\u0000"+
		"\u0000\u0013\u0014\u0001\u0000\u0000\u0000\u0014\u0016\u0001\u0000\u0000"+
		"\u0000\u0015\u0013\u0001\u0000\u0000\u0000\u0016\u0017\u0005\u0002\u0000"+
		"\u0000\u0017\u0018\u0006\u0000\uffff\uffff\u0000\u0018\u0001\u0001\u0000"+
		"\u0000\u0000\u0019\u001a\u0005\n\u0000\u0000\u001a\u001b\u0005\u0003\u0000"+
		"\u0000\u001b\u001c\u0003\u0004\u0002\u0000\u001c\u001d\u0006\u0001\uffff"+
		"\uffff\u0000\u001d\u0003\u0001\u0000\u0000\u0000\u001e\u001f\u0006\u0002"+
		"\uffff\uffff\u0000\u001f \u0005\u0001\u0000\u0000 !\u0003\u0006\u0003"+
		"\u0000!(\u0006\u0002\uffff\uffff\u0000\"#\u0005\u0004\u0000\u0000#$\u0003"+
		"\u0006\u0003\u0000$%\u0006\u0002\uffff\uffff\u0000%\'\u0001\u0000\u0000"+
		"\u0000&\"\u0001\u0000\u0000\u0000\'*\u0001\u0000\u0000\u0000(&\u0001\u0000"+
		"\u0000\u0000()\u0001\u0000\u0000\u0000)+\u0001\u0000\u0000\u0000*(\u0001"+
		"\u0000\u0000\u0000+,\u0005\u0002\u0000\u0000,-\u0006\u0002\uffff\uffff"+
		"\u0000-\u0005\u0001\u0000\u0000\u0000./\u0005\n\u0000\u0000/0\u0005\u0003"+
		"\u0000\u000001\u0003\b\u0004\u000012\u0006\u0003\uffff\uffff\u00002\u0007"+
		"\u0001\u0000\u0000\u000035\u0007\u0000\u0000\u000043\u0001\u0000\u0000"+
		"\u000045\u0001\u0000\u0000\u000056\u0001\u0000\u0000\u000069\u0005\u000b"+
		"\u0000\u000078\u0005\u0007\u0000\u00008:\u0005\u000b\u0000\u000097\u0001"+
		"\u0000\u0000\u00009:\u0001\u0000\u0000\u0000:@\u0001\u0000\u0000\u0000"+
		";=\u0007\u0001\u0000\u0000<>\u0007\u0000\u0000\u0000=<\u0001\u0000\u0000"+
		"\u0000=>\u0001\u0000\u0000\u0000>?\u0001\u0000\u0000\u0000?A\u0005\u000b"+
		"\u0000\u0000@;\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000\u0000AB\u0001"+
		"\u0000\u0000\u0000BC\u0006\u0004\uffff\uffff\u0000C\t\u0001\u0000\u0000"+
		"\u0000\u0006\u0013(49=@";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}