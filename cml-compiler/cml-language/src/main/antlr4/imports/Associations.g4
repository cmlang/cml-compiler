grammar Associations;

import Names, Types;

associationDeclaration returns [Association association]:
    'association' NAME '{' (associationEndDeclaration ';')* '}';

associationEndDeclaration returns [AssociationEnd associationEnd]:
    conceptName=NAME '.' propertyName=NAME (':' typeDeclaration)?;

