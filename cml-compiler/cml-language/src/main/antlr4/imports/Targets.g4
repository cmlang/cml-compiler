grammar Targets;

@header
{
import cml.language.features.*;
}

import Names, Properties;

targetDeclaration returns [Target target]:
    'target' NAME propertyListNode;


