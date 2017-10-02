grammar Associations;

import Names, Types;

associationDeclaration
    returns [Association association]:
    ASSOCIATION NAME
    '{' (associationEndDeclaration ';')* '}';

associationEndDeclaration
    returns [TempAssociationEnd associationEnd]:
    conceptName=NAME '.' propertyName=NAME
    (':' typeDeclaration)?;

