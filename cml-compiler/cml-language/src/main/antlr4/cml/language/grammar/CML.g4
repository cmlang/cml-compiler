grammar CML;

@header
{
import cml.language.foundation.*;
import cml.language.features.*;
}

import Concepts, Targets, Ignored;

compilationUnit returns [Model model]:
    declarations*;

declarations:
    conceptDeclaration | targetDeclaration;

