grammar Annotations;

import Names;

annotationList:
  '[' ( annotationDeclaration (',' annotationDeclaration)* )? ']';

annotationDeclaration:
  NAME;

