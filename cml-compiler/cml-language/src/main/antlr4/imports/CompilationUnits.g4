grammar CompilationUnits;

import Modules, Concepts, Targets;

compilationUnit:
    declarations*;

declarations:
    moduleDeclaration | conceptDeclaration | targetDeclaration;

