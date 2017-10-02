grammar Associations;

import Names, Types;

associationDeclaration
    returns [Association association]:
    ASSOCIATION NAME
    '{' (associationEndDeclaration ';')* '}';

associationEndDeclaration
    returns [Association association]:
    conceptName=NAME '.' propertyName=NAME
    (':' typeDeclaration)?;

