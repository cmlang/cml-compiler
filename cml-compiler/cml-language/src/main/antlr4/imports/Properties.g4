grammar Properties;

import Literals, Names, Types;

propertyListNode:
    '{' (propertyNode ';')* '}';

propertyNode returns [Property property]:
    NAME (':' typeNode)? ('=' STRING)?;
