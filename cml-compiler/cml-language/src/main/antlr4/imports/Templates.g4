grammar Templates;

import Names, Functions;

templateDeclaration returns [Template template]:
    TEMPLATE '<' templateParameter* '>'
    functionDeclaration;

templateParameter returns [TemplateParameter param]:
    NAME;


