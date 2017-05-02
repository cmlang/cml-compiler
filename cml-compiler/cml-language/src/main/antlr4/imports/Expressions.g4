grammar Expressions;

import Literals;

expression returns [Literal literal]: literalExpression;

literalExpression returns [Literal literal]: STRING | INTEGER | DECIMAL;

