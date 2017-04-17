grammar Types;

@header
{
import cml.language.foundation.*;
}

import Names;

typeNode returns [Type type]:
    NAME CARDINALITY?;

CARDINALITY:
    ('?' | '*');


