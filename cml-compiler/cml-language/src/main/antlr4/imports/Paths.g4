grammar Paths;

import Names;

pathExpression returns [Path path]:
    NAME ('.' NAME)*;
