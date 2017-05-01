grammar Expressions;

import Literals;

expression returns [Object value]: literal;

literal returns [Object value]: STRING | INTEGER | DECIMAL;

