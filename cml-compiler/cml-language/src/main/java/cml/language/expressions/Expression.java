package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.types.Type;

public interface Expression extends ModelElement, Scope
{
    String getKind();
    Type getType();
}

