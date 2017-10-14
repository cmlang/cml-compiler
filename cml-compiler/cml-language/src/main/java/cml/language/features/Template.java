package cml.language.features;

import cml.language.foundation.NamedElementBase;

public class Template extends NamedElementBase
{
    private final Function function;

    public Template(TempModule module, Function function)
    {
        super(module, function.getName());

        this.function = function;
    }

    public Function getFunction()
    {
        return function;
    }

    @Override
    public String getDiagnosticId()
    {
        return function.getDiagnosticId();
    }
}
