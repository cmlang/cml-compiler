package cml.language.foundation;

import cml.language.generated.Element;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class Diagnostic
{
    private final String code;
    private final Element element;
    private final @Nullable String message;
    private final List<? extends Element> participants;

    public Diagnostic(String code, Element element)
    {
        this(code, element, emptyList());
    }

    public Diagnostic(String code, Element element, @Nullable String message)
    {
        this(code, element, message, emptyList());
    }

    public Diagnostic(String code, Element element, List<? extends Element> participants)
    {
        this(code, element, null, participants);
    }

    @SuppressWarnings("WeakerAccess")
    public Diagnostic(String code, Element element, @Nullable String message, List<? extends Element> participants)
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

    public Element getElement()
    {
        return element;
    }

    public Optional<String> getMessage()
    {
        return Optional.ofNullable(message);
    }

    public List<? extends Element> getParticipants()
    {
        return participants;
    }
}
