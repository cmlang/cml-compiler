package cml.language.foundation;

import java.util.HashSet;
import java.util.Set;

public class Diagnostic
{
    interface Factory<T extends ModelElement>
    {
        Diagnostic create(T self);
    }

    private final String code;
    private final ModelElement element;
    private final Set<ModelElement> participants = new HashSet<>();

    public Diagnostic(String code, ModelElement element)
    {
        this.code = code;
        this.element = element;
    }

    public String getCode()
    {
        return code;
    }

    public ModelElement getElement()
    {
        return element;
    }

    public Set<ModelElement> getParticipants()
    {
        return participants;
    }

    boolean addParticipants(Set<? extends ModelElement> participants)
    {
        return this.participants.addAll(participants);
    }
}
