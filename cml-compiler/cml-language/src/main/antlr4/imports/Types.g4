grammar Types;

import Names;

typeDeclaration returns [Type type]:
    NAME CARDINALITY?;

CARDINALITY:
    ('?' | '*');


