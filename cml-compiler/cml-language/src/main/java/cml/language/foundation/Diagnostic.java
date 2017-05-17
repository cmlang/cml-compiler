package cml.language.foundation;

import java.util.*;

import static java.util.Collections.emptyList;

public class Diagnostic
{
    interface Factory<T extends ModelElement>
    {
        Diagnostic create(T self);
    }

    private final String code;
    private final ModelElement element;
    private final List<? extends ModelElement> participants;

    public Diagnostic(String code, ModelElement element)
    {
        this(code, element, emptyList());
    }

    public Diagnostic(String code, ModelElement element, List<? extends ModelElement> participants)
    {
        this.code = code;
        this.element = element;
        this.participants = new ArrayList<>(participants);
    }

    public String getCode()
    {
        return code;
    }

    public ModelElement getElement()
    {
        return element;
    }

    public List<? extends ModelElement> getParticipants()
    {
        return participants;
    }
}
