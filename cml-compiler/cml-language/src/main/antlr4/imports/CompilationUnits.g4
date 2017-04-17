grammar CompilationUnits;

import Concepts, Targets;

compilationUnit returns [Model model]:
    declarations*;

declarations:
    conceptDeclaration | targetDeclaration;

