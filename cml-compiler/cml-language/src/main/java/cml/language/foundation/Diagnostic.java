package cml.language.foundation;

import cml.language.generated.ModelElement;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class Diagnostic
{
    private final String code;
    private final ModelElement element;
    private final @Nullable String message;
    private final List<? extends ModelElement> participants;

    public Diagnostic(String code, ModelElement element)
    {
        this(code, element, emptyList());
    }

    public Diagnostic(String code, ModelElement element, @Nullable String message)
    {
        this(code, element, message, emptyList());
    }

    public Diagnostic(String code, ModelElement element, List<? extends ModelElement> participants)
    {
        this(code, element, null, participants);
    }

    @SuppressWarnings("WeakerAccess")
    public Diagnostic(String code, ModelElement element, @Nullable String message, List<? extends ModelElement> participants)
    {
        this.code = code;
        this.element = element;
        this.message = message;
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

    public Optional<String> getMessage()
    {
        return Optional.ofNullable(message);
    }

    public List<? extends ModelElement> getParticipants()
    {
        return participants;
    }
}
