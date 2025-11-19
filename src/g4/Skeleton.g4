grammar Skeleton;

/* [
    joint1:(1,1,1):X:[
        s1:[1,2]
        s2:[2]
    ]:[
        joint2:(0.5:X->Y):[
            s2:[3]
        ]
        joint3:(1,2,3):XYZ:[
            s2:[3]
        ]
    ]
] */

skeleton returns [ org.nexis.model.skeleton.ParsedJoint result ]:
    '[' j=joint ']' { $result = $j.r; } ;

joint returns [ org.nexis.model.skeleton.ParsedJoint r ]:
    { org.nexis.math.Vector3f av = null;
      org.nexis.math.Axis[] ao = null;
      org.nexis.model.skeleton.ParsedAngle[] aa = null;
      org.nexis.model.skeleton.ParsedBinding[] ab = null;
      org.nexis.model.skeleton.ParsedJoint[] al = null; }
    nm=NAME ':' ( (v=vector3 o=axis { av = $v.r; ao = $o.r; } ) | a=angles { aa = $a.r; } ) (':' b=bindings { ab = $b.r; } )? ( ':' l=jointList { al = $l.r; } )?
    { $r = new org.nexis.model.skeleton.ParsedJoint( $nm.text, av, ao, aa, ab, al); };

axis returns [ org.nexis.math.Axis[] r ]:
    ':' t=NAME
    { $r = $t.text.chars().mapToObj(c -> org.nexis.math.Axis.valueOf(String.valueOf((char)c))).toArray(l -> new org.nexis.math.Axis[l]); };

angles returns [ org.nexis.model.skeleton.ParsedAngle[] r ]:
    { java.util.List<org.nexis.model.skeleton.ParsedAngle> l = new java.util.ArrayList<org.nexis.model.skeleton.ParsedAngle>(); }
    '(' j=angle { l.add($j.r); } (',' k=angle { l.add($k.r); } )* ')'
    { $r = l.toArray(new org.nexis.model.skeleton.ParsedAngle[0]); };

angle returns [ org.nexis.model.skeleton.ParsedAngle r ]:
    n=floatNum ':' f=NAME '->' t=NAME
    { $r = new org.nexis.model.skeleton.ParsedAngle(org.nexis.math.Axis.valueOf($f.text), org.nexis.math.Axis.valueOf($t.text), $n.r); };

jointList returns [ org.nexis.model.skeleton.ParsedJoint[] r ]:
    { java.util.List<org.nexis.model.skeleton.ParsedJoint> l = new java.util.ArrayList<org.nexis.model.skeleton.ParsedJoint>(); }
    '[' j=joint { l.add($j.r); } ( k=joint { l.add($k.r); } )* ']'
    { $r = l.toArray(new org.nexis.model.skeleton.ParsedJoint[0]); } ;

bindings returns [ org.nexis.model.skeleton.ParsedBinding[] r ]:
    { java.util.List<org.nexis.model.skeleton.ParsedBinding> l = new java.util.ArrayList<org.nexis.model.skeleton.ParsedBinding>(); }
    '[' n=binding { l.add($n.r); } (  m=binding { l.add($m.r); } )* ']'
    { $r = l.toArray( new org.nexis.model.skeleton.ParsedBinding[0] ); } ;

binding returns [ org.nexis.model.skeleton.ParsedBinding r ]:
    n=NAME ':' i=indList { $r = new org.nexis.model.skeleton.ParsedBinding($n.text, $i.r); } ;

indList returns [ int[] r ]:
    { java.util.ArrayList<Integer> l = new java.util.ArrayList<Integer>(); }
    '[' n=intNum { l.add($n.r); } ( ',' m=intNum { l.add($m.r); } )* ']'
    { $r = l.stream().mapToInt(Integer::intValue).toArray(); };

vector3  returns [ org.nexis.math.Vector3f r ]:
    '(' a=floatNum ',' b=floatNum ',' c=floatNum ')'
    { $r = new org.nexis.math.Vector3f($a.r, $b.r, $c.r); };

floatNum returns [ float r ]:
    s=('+'|'-')? n=DIGITS ('.' m=DIGITS)? (('e'|'E') e=('+'|'-')? p=DIGITS )? {
        StringBuilder sb = new StringBuilder();
        if($s.text != null){
            sb.append($s.text);
        }
        sb.append($n.text);
        if($m.text != null){
            sb.append('.').append($m.text);
        }
        if($p.text != null){
            sb.append('E');
            if($e.text != null){
                sb.append($e.text);
            }
            sb.append($p.text);
        }
        $r = Float.parseFloat(sb.toString());
    };

intNum returns [ int r ]:
    n=DIGITS
    { $r = Integer.parseInt($n.text); } ;

NAME : ('a'..'z' | 'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9' | '_')*;

DIGITS : ('0'..'9')+;

WS : (' '|'\t'|'\n'|'\r')+ -> skip ;
