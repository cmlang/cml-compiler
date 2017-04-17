grammar Properties;

@header
{
import cml.language.foundation.*;
}

import Literals, Names, Types;

propertyListNode:
    '{' (propertyNode ';')* '}';

propertyNode returns [Property property]:
    NAME (':' typeNode)? ('=' STRING)?;
