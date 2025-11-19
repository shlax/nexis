grammar KeyFrame;

/* [
    joint1 : [ X: 30, Y:45 ],
    joint2 : [ X: 30, Z:45 ]
] */
keyFrame returns [ org.nexis.model.skeleton.animation.ParsedKeyFrame result ]:
    { java.util.ArrayList<org.nexis.model.skeleton.animation.ParsedJointAngles> l = new java.util.ArrayList<org.nexis.model.skeleton.animation.ParsedJointAngles>(); }
    '[' j=joint { l.add($j.r); } ( k=joint { l.add($k.r); } )*  ']'
    { $result = new org.nexis.model.skeleton.animation.ParsedKeyFrame( l.toArray( new org.nexis.model.skeleton.animation.ParsedJointAngles[0]) ); }
;

joint returns [ org.nexis.model.skeleton.animation.ParsedJointAngles r ]:
    n=NAME ':' a=angles
    { $r = new org.nexis.model.skeleton.animation.ParsedJointAngles( $n.text, $a.r ); }
;

angles returns [ org.nexis.model.skeleton.animation.ParsedJointAngle[] r ]:
    { java.util.ArrayList<org.nexis.model.skeleton.animation.ParsedJointAngle> l = new java.util.ArrayList<org.nexis.model.skeleton.animation.ParsedJointAngle>(); }
    '[' i=angle { l.add($i.r); } (',' j=angle { l.add($j.r); } )* ']'
    { $r = l.toArray(new org.nexis.model.skeleton.animation.ParsedJointAngle[0]); }
;

angle returns [ org.nexis.model.skeleton.animation.ParsedJointAngle r ]:
    a=NAME ':' v=floatNum { $r = new org.nexis.model.skeleton.animation.ParsedJointAngle(org.nexis.math.Axis.valueOf($a.text), $v.r); }
;

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

NAME : ('a'..'z' | 'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9' | '_')*;

DIGITS : ('0'..'9')+;

WS : (' '|'\t'|'\n'|'\r')+ -> skip ;
