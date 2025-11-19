grammar Model;

/* [
    (-0.5,-0.5,0.5)
    (0.5,-0.5,0.5)
    (-0.5,0.5,0.5)
]:[
    [
        0:(0,0,1):[(0.375,0)]
        1:(0,0,1):[(0.625,0)]
        2:(0,0,1):[(0.375,0.25)]
    ]
] */

model returns [ org.nexis.model.ParsedModel result ] :
    p=points ':' t=triangles
    { $result = new org.nexis.model.ParsedModel( $p.r.toArray(new org.nexis.math.Vector3f[0] ), $t.r.toArray( new org.nexis.model.ParsedTriangle[0] ) ); };

triangles returns [ java.util.List<org.nexis.model.ParsedTriangle> r ] :
    { $r = new java.util.ArrayList<org.nexis.model.ParsedTriangle>(); }
    '[' f=triangle { $r.add($f.r); } ( fi=triangle { $r.add($fi.r); } )* ']' ;

triangle returns [ org.nexis.model.ParsedTriangle r ] :
    '[' a=vertex b=vertex c=vertex ']'
    { $r = new org.nexis.model.ParsedTriangle($a.r, $b.r, $c.r); } ;

vertex returns [ org.nexis.model.ParsedVertex r ] :
    i=intNum ':' n=vector3 ':' u=uvs
    { $r = new org.nexis.model.ParsedVertex($i.r, $n.r, $u.r.toArray( new org.nexis.math.Vector2f[0] ) ); } ;

uvs returns [ java.util.List<org.nexis.math.Vector2f> r ] :
    { $r = new java.util.ArrayList<>(); }
    '[' p=vector2 { $r.add($p.r); } (',' pi=vector2 { $r.add($pi.r); } )* ']' ;

points returns [ java.util.List<org.nexis.math.Vector3f> r ] :
    { $r = new java.util.ArrayList<>(); }
    '[' p=vector3 { $r.add($p.r); } ( pi=vector3 { $r.add($pi.r); } )* ']' ;

vector3  returns [ org.nexis.math.Vector3f r ]:
    '(' a=floatNum ',' b=floatNum ',' c=floatNum ')'
    { $r = new org.nexis.math.Vector3f($a.r, $b.r, $c.r); } ;

vector2  returns [ org.nexis.math.Vector2f r ]:
    '(' a=floatNum ',' b=floatNum ')'
    { $r = new org.nexis.math.Vector2f($a.r, $b.r); } ;

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
    } ;

intNum returns [ int r ]:
    n=DIGITS
    { $r = Integer.parseInt($n.text); } ;

DIGITS : ('0'..'9')+;

WS : (' '|'\t'|'\n'|'\r')+ -> skip ;
