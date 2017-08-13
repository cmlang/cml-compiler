package cml.language.expressions;

import cml.language.foundation.ModelElementBase;

public class Enumerator extends ModelElementBase
{
    private final String variable;
    private final Path path;

    public Enumerator(String variable, Path path)
    {
        this.variable = variable;
        this.path = path;
    }

    public String getVariable()
    {
        return variable;
    }

    public Path getPath()
    {
        return path;
    }
}
