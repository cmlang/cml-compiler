grammar Types;

import Names;

typeNode returns [Type type]:
    NAME CARDINALITY?;

CARDINALITY:
    ('?' | '*');


