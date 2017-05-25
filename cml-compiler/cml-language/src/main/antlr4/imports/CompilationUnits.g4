grammar CompilationUnits;

import Modules, Concepts, Associations, Tasks;

compilationUnit:
    declarations*;

declarations:
    moduleDeclaration | conceptDeclaration | associationDeclaration | taskDeclaration;

