grammar Associations;

import Names, Types;

associationDeclaration
    returns [Association association]:
    ASSOCIATION NAME
    '{' (associationEndDeclaration ';')* '}';

associationEndDeclaration
    returns [AssociationEnd associationEnd]:
    conceptName=NAME '.' propertyName=NAME
    (':' typeDeclaration)?;

