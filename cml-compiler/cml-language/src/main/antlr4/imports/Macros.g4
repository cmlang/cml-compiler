grammar Macros;

import Names, Types;

macroDeclaration returns [Macro macro]:
    'macro' NAME macroParameterList ':' typeDeclaration ';';

macroParameterList:
    '(' macroParameterDeclaration (',' macroParameterDeclaration)* ')';

macroParameterDeclaration returns [Parameter parameter]:
   (scope=NAME '/')? name=NAME (':' typeDeclaration);

