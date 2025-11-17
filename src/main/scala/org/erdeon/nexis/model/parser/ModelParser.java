// Generated from /home/pochodnicky/wrksp/no-git/NXN/src/g4/Model.g4 by ANTLR 4.13.2
package org.erdeon.nexis.model.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.erdeon.nexis.math.Vector2f;
import org.erdeon.nexis.math.Vector3f;
import org.erdeon.nexis.model.ParsedModel;
import org.erdeon.nexis.model.ParsedTriangle;
import org.erdeon.nexis.model.ParsedVertex;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ModelParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, DIGITS=12, WS=13;
	public static final int
		RULE_model = 0, RULE_triangles = 1, RULE_triangle = 2, RULE_vertex = 3, 
		RULE_uvs = 4, RULE_points = 5, RULE_vector3 = 6, RULE_vector2 = 7, RULE_floatNum = 8, 
		RULE_intNum = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"model", "triangles", "triangle", "vertex", "uvs", "points", "vector3", 
			"vector2", "floatNum", "intNum"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':'", "'['", "','", "']'", "'('", "')'", "'+'", "'-'", "'.'", 
			"'e'", "'E'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"DIGITS", "WS"
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
	public String getGrammarFileName() { return "Model.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ModelParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModelContext extends ParserRuleContext {
		public ParsedModel result;
		public PointsContext p;
		public TrianglesContext t;
		public PointsContext points() {
			return getRuleContext(PointsContext.class,0);
		}
		public TrianglesContext triangles() {
			return getRuleContext(TrianglesContext.class,0);
		}
		public ModelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_model; }
	}

	public final ModelContext model() throws RecognitionException {
		ModelContext _localctx = new ModelContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_model);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(20);
			((ModelContext)_localctx).p = points();
			setState(21);
			match(T__0);
			setState(22);
			((ModelContext)_localctx).t = triangles();
			 ((ModelContext)_localctx).result =  new ParsedModel( ((ModelContext)_localctx).p.r.toArray(new Vector3f[0] ), ((ModelContext)_localctx).t.r.toArray( new ParsedTriangle[0] ) ); 
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
	public static class TrianglesContext extends ParserRuleContext {
		public java.util.List<ParsedTriangle> r;
		public TriangleContext f;
		public TriangleContext fi;
		public List<TriangleContext> triangle() {
			return getRuleContexts(TriangleContext.class);
		}
		public TriangleContext triangle(int i) {
			return getRuleContext(TriangleContext.class,i);
		}
		public TrianglesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triangles; }
	}

	public final TrianglesContext triangles() throws RecognitionException {
		TrianglesContext _localctx = new TrianglesContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_triangles);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 ((TrianglesContext)_localctx).r =  new java.util.ArrayList<ParsedTriangle>(); 
			setState(26);
			match(T__1);
			setState(27);
			((TrianglesContext)_localctx).f = triangle();
			 _localctx.r.add(((TrianglesContext)_localctx).f.r); 
			setState(35);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(29);
				match(T__2);
				setState(30);
				((TrianglesContext)_localctx).fi = triangle();
				 _localctx.r.add(((TrianglesContext)_localctx).fi.r); 
				}
				}
				setState(37);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(38);
			match(T__3);
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
	public static class TriangleContext extends ParserRuleContext {
		public ParsedTriangle r;
		public VertexContext a;
		public VertexContext b;
		public VertexContext c;
		public List<VertexContext> vertex() {
			return getRuleContexts(VertexContext.class);
		}
		public VertexContext vertex(int i) {
			return getRuleContext(VertexContext.class,i);
		}
		public TriangleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triangle; }
	}

	public final TriangleContext triangle() throws RecognitionException {
		TriangleContext _localctx = new TriangleContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_triangle);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(T__1);
			setState(41);
			((TriangleContext)_localctx).a = vertex();
			setState(42);
			match(T__2);
			setState(43);
			((TriangleContext)_localctx).b = vertex();
			setState(44);
			match(T__2);
			setState(45);
			((TriangleContext)_localctx).c = vertex();
			setState(46);
			match(T__3);
			 ((TriangleContext)_localctx).r =  new ParsedTriangle(((TriangleContext)_localctx).a.r, ((TriangleContext)_localctx).b.r, ((TriangleContext)_localctx).c.r); 
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
	public static class VertexContext extends ParserRuleContext {
		public ParsedVertex r;
		public IntNumContext i;
		public Vector3Context n;
		public UvsContext u;
		public IntNumContext intNum() {
			return getRuleContext(IntNumContext.class,0);
		}
		public Vector3Context vector3() {
			return getRuleContext(Vector3Context.class,0);
		}
		public UvsContext uvs() {
			return getRuleContext(UvsContext.class,0);
		}
		public VertexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vertex; }
	}

	public final VertexContext vertex() throws RecognitionException {
		VertexContext _localctx = new VertexContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_vertex);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			((VertexContext)_localctx).i = intNum();
			setState(50);
			match(T__0);
			setState(51);
			((VertexContext)_localctx).n = vector3();
			setState(52);
			match(T__0);
			setState(53);
			((VertexContext)_localctx).u = uvs();
			 ((VertexContext)_localctx).r =  new ParsedVertex(((VertexContext)_localctx).i.r, ((VertexContext)_localctx).n.r, ((VertexContext)_localctx).u.r.toArray( new Vector2f[0] ) ); 
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
	public static class UvsContext extends ParserRuleContext {
		public java.util.List<Vector2f> r;
		public Vector2Context p;
		public Vector2Context pi;
		public List<Vector2Context> vector2() {
			return getRuleContexts(Vector2Context.class);
		}
		public Vector2Context vector2(int i) {
			return getRuleContext(Vector2Context.class,i);
		}
		public UvsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uvs; }
	}

	public final UvsContext uvs() throws RecognitionException {
		UvsContext _localctx = new UvsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_uvs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 ((UvsContext)_localctx).r =  new java.util.ArrayList<>(); 
			setState(57);
			match(T__1);
			setState(58);
			((UvsContext)_localctx).p = vector2();
			 _localctx.r.add(((UvsContext)_localctx).p.r); 
			setState(66);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(60);
				match(T__2);
				setState(61);
				((UvsContext)_localctx).pi = vector2();
				 _localctx.r.add(((UvsContext)_localctx).pi.r); 
				}
				}
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(69);
			match(T__3);
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
	public static class PointsContext extends ParserRuleContext {
		public java.util.List<Vector3f> r;
		public Vector3Context p;
		public Vector3Context pi;
		public List<Vector3Context> vector3() {
			return getRuleContexts(Vector3Context.class);
		}
		public Vector3Context vector3(int i) {
			return getRuleContext(Vector3Context.class,i);
		}
		public PointsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_points; }
	}

	public final PointsContext points() throws RecognitionException {
		PointsContext _localctx = new PointsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_points);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 ((PointsContext)_localctx).r =  new java.util.ArrayList<>(); 
			setState(72);
			match(T__1);
			setState(73);
			((PointsContext)_localctx).p = vector3();
			 _localctx.r.add(((PointsContext)_localctx).p.r); 
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(75);
				match(T__2);
				setState(76);
				((PointsContext)_localctx).pi = vector3();
				 _localctx.r.add(((PointsContext)_localctx).pi.r); 
				}
				}
				setState(83);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(84);
			match(T__3);
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
	public static class Vector3Context extends ParserRuleContext {
		public Vector3f r;
		public FloatNumContext a;
		public FloatNumContext b;
		public FloatNumContext c;
		public List<FloatNumContext> floatNum() {
			return getRuleContexts(FloatNumContext.class);
		}
		public FloatNumContext floatNum(int i) {
			return getRuleContext(FloatNumContext.class,i);
		}
		public Vector3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vector3; }
	}

	public final Vector3Context vector3() throws RecognitionException {
		Vector3Context _localctx = new Vector3Context(_ctx, getState());
		enterRule(_localctx, 12, RULE_vector3);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			match(T__4);
			setState(87);
			((Vector3Context)_localctx).a = floatNum();
			setState(88);
			match(T__2);
			setState(89);
			((Vector3Context)_localctx).b = floatNum();
			setState(90);
			match(T__2);
			setState(91);
			((Vector3Context)_localctx).c = floatNum();
			setState(92);
			match(T__5);
			 ((Vector3Context)_localctx).r =  new Vector3f(((Vector3Context)_localctx).a.r, ((Vector3Context)_localctx).b.r, ((Vector3Context)_localctx).c.r); 
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
	public static class Vector2Context extends ParserRuleContext {
		public Vector2f r;
		public FloatNumContext a;
		public FloatNumContext b;
		public List<FloatNumContext> floatNum() {
			return getRuleContexts(FloatNumContext.class);
		}
		public FloatNumContext floatNum(int i) {
			return getRuleContext(FloatNumContext.class,i);
		}
		public Vector2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vector2; }
	}

	public final Vector2Context vector2() throws RecognitionException {
		Vector2Context _localctx = new Vector2Context(_ctx, getState());
		enterRule(_localctx, 14, RULE_vector2);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(T__4);
			setState(96);
			((Vector2Context)_localctx).a = floatNum();
			setState(97);
			match(T__2);
			setState(98);
			((Vector2Context)_localctx).b = floatNum();
			setState(99);
			match(T__5);
			 ((Vector2Context)_localctx).r =  new Vector2f(((Vector2Context)_localctx).a.r, ((Vector2Context)_localctx).b.r); 
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
		public List<TerminalNode> DIGITS() { return getTokens(ModelParser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(ModelParser.DIGITS, i);
		}
		public FloatNumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatNum; }
	}

	public final FloatNumContext floatNum() throws RecognitionException {
		FloatNumContext _localctx = new FloatNumContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_floatNum);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6 || _la==T__7) {
				{
				setState(102);
				((FloatNumContext)_localctx).s = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__6 || _la==T__7) ) {
					((FloatNumContext)_localctx).s = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(105);
			((FloatNumContext)_localctx).n = match(DIGITS);
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(106);
				match(T__8);
				setState(107);
				((FloatNumContext)_localctx).m = match(DIGITS);
				}
			}

			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9 || _la==T__10) {
				{
				setState(110);
				_la = _input.LA(1);
				if ( !(_la==T__9 || _la==T__10) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(112);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6 || _la==T__7) {
					{
					setState(111);
					((FloatNumContext)_localctx).e = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__6 || _la==T__7) ) {
						((FloatNumContext)_localctx).e = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(114);
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

	@SuppressWarnings("CheckReturnValue")
	public static class IntNumContext extends ParserRuleContext {
		public int r;
		public Token n;
		public TerminalNode DIGITS() { return getToken(ModelParser.DIGITS, 0); }
		public IntNumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intNum; }
	}

	public final IntNumContext intNum() throws RecognitionException {
		IntNumContext _localctx = new IntNumContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_intNum);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			((IntNumContext)_localctx).n = match(DIGITS);
			 ((IntNumContext)_localctx).r =  Integer.parseInt((((IntNumContext)_localctx).n!=null?((IntNumContext)_localctx).n.getText():null)); 
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
		"\u0004\u0001\r{\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001\"\b\u0001\n\u0001\f\u0001"+
		"%\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004A\b\u0004\n\u0004\f\u0004"+
		"D\t\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005"+
		"P\b\u0005\n\u0005\f\u0005S\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0003\bh\b\b\u0001\b\u0001"+
		"\b\u0001\b\u0003\bm\b\b\u0001\b\u0001\b\u0003\bq\b\b\u0001\b\u0003\bt"+
		"\b\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0000\u0000\n\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0000\u0002\u0001\u0000\u0007"+
		"\b\u0001\u0000\n\u000bw\u0000\u0014\u0001\u0000\u0000\u0000\u0002\u0019"+
		"\u0001\u0000\u0000\u0000\u0004(\u0001\u0000\u0000\u0000\u00061\u0001\u0000"+
		"\u0000\u0000\b8\u0001\u0000\u0000\u0000\nG\u0001\u0000\u0000\u0000\fV"+
		"\u0001\u0000\u0000\u0000\u000e_\u0001\u0000\u0000\u0000\u0010g\u0001\u0000"+
		"\u0000\u0000\u0012w\u0001\u0000\u0000\u0000\u0014\u0015\u0003\n\u0005"+
		"\u0000\u0015\u0016\u0005\u0001\u0000\u0000\u0016\u0017\u0003\u0002\u0001"+
		"\u0000\u0017\u0018\u0006\u0000\uffff\uffff\u0000\u0018\u0001\u0001\u0000"+
		"\u0000\u0000\u0019\u001a\u0006\u0001\uffff\uffff\u0000\u001a\u001b\u0005"+
		"\u0002\u0000\u0000\u001b\u001c\u0003\u0004\u0002\u0000\u001c#\u0006\u0001"+
		"\uffff\uffff\u0000\u001d\u001e\u0005\u0003\u0000\u0000\u001e\u001f\u0003"+
		"\u0004\u0002\u0000\u001f \u0006\u0001\uffff\uffff\u0000 \"\u0001\u0000"+
		"\u0000\u0000!\u001d\u0001\u0000\u0000\u0000\"%\u0001\u0000\u0000\u0000"+
		"#!\u0001\u0000\u0000\u0000#$\u0001\u0000\u0000\u0000$&\u0001\u0000\u0000"+
		"\u0000%#\u0001\u0000\u0000\u0000&\'\u0005\u0004\u0000\u0000\'\u0003\u0001"+
		"\u0000\u0000\u0000()\u0005\u0002\u0000\u0000)*\u0003\u0006\u0003\u0000"+
		"*+\u0005\u0003\u0000\u0000+,\u0003\u0006\u0003\u0000,-\u0005\u0003\u0000"+
		"\u0000-.\u0003\u0006\u0003\u0000./\u0005\u0004\u0000\u0000/0\u0006\u0002"+
		"\uffff\uffff\u00000\u0005\u0001\u0000\u0000\u000012\u0003\u0012\t\u0000"+
		"23\u0005\u0001\u0000\u000034\u0003\f\u0006\u000045\u0005\u0001\u0000\u0000"+
		"56\u0003\b\u0004\u000067\u0006\u0003\uffff\uffff\u00007\u0007\u0001\u0000"+
		"\u0000\u000089\u0006\u0004\uffff\uffff\u00009:\u0005\u0002\u0000\u0000"+
		":;\u0003\u000e\u0007\u0000;B\u0006\u0004\uffff\uffff\u0000<=\u0005\u0003"+
		"\u0000\u0000=>\u0003\u000e\u0007\u0000>?\u0006\u0004\uffff\uffff\u0000"+
		"?A\u0001\u0000\u0000\u0000@<\u0001\u0000\u0000\u0000AD\u0001\u0000\u0000"+
		"\u0000B@\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000CE\u0001\u0000"+
		"\u0000\u0000DB\u0001\u0000\u0000\u0000EF\u0005\u0004\u0000\u0000F\t\u0001"+
		"\u0000\u0000\u0000GH\u0006\u0005\uffff\uffff\u0000HI\u0005\u0002\u0000"+
		"\u0000IJ\u0003\f\u0006\u0000JQ\u0006\u0005\uffff\uffff\u0000KL\u0005\u0003"+
		"\u0000\u0000LM\u0003\f\u0006\u0000MN\u0006\u0005\uffff\uffff\u0000NP\u0001"+
		"\u0000\u0000\u0000OK\u0001\u0000\u0000\u0000PS\u0001\u0000\u0000\u0000"+
		"QO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000RT\u0001\u0000\u0000"+
		"\u0000SQ\u0001\u0000\u0000\u0000TU\u0005\u0004\u0000\u0000U\u000b\u0001"+
		"\u0000\u0000\u0000VW\u0005\u0005\u0000\u0000WX\u0003\u0010\b\u0000XY\u0005"+
		"\u0003\u0000\u0000YZ\u0003\u0010\b\u0000Z[\u0005\u0003\u0000\u0000[\\"+
		"\u0003\u0010\b\u0000\\]\u0005\u0006\u0000\u0000]^\u0006\u0006\uffff\uffff"+
		"\u0000^\r\u0001\u0000\u0000\u0000_`\u0005\u0005\u0000\u0000`a\u0003\u0010"+
		"\b\u0000ab\u0005\u0003\u0000\u0000bc\u0003\u0010\b\u0000cd\u0005\u0006"+
		"\u0000\u0000de\u0006\u0007\uffff\uffff\u0000e\u000f\u0001\u0000\u0000"+
		"\u0000fh\u0007\u0000\u0000\u0000gf\u0001\u0000\u0000\u0000gh\u0001\u0000"+
		"\u0000\u0000hi\u0001\u0000\u0000\u0000il\u0005\f\u0000\u0000jk\u0005\t"+
		"\u0000\u0000km\u0005\f\u0000\u0000lj\u0001\u0000\u0000\u0000lm\u0001\u0000"+
		"\u0000\u0000ms\u0001\u0000\u0000\u0000np\u0007\u0001\u0000\u0000oq\u0007"+
		"\u0000\u0000\u0000po\u0001\u0000\u0000\u0000pq\u0001\u0000\u0000\u0000"+
		"qr\u0001\u0000\u0000\u0000rt\u0005\f\u0000\u0000sn\u0001\u0000\u0000\u0000"+
		"st\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000uv\u0006\b\uffff\uffff"+
		"\u0000v\u0011\u0001\u0000\u0000\u0000wx\u0005\f\u0000\u0000xy\u0006\t"+
		"\uffff\uffff\u0000y\u0013\u0001\u0000\u0000\u0000\u0007#BQglps";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}