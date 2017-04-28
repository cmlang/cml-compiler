grammar CompilationUnits;

import Modules, Concepts, Tasks;

compilationUnit:
    declarations*;

declarations:
    moduleDeclaration | conceptDeclaration | taskDeclaration;

