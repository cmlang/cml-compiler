package cml.language.invariants;

import cml.language.expressions.Invocation;
import cml.language.features.Function;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.ModelElement;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.jooq.lambda.Seq.seq;

public class InvocationInvariant implements Invariant<Invocation>
{
    private static final String MESSAGE__UNABLE_TO_FIND_FUNCTION_OF_INVOCATION = "Unable to find function of invocation: ";
    private static final String MESSAGE__SHOULD_MATCH_NUMBER_OF_PARAMS_IN_FUNCTION = "Number of arguments in invocation should match the number of parameters in function: ";
    private static final String MESSAGE__SHOULD_MATCH_PARAMETER_TYPE_IN_FUNCTION = "Argument type should match parameter type in function: ";
    private static final String MESSAGE__SHOULD_MATCH_NUMBER_OF_PROPS_IN_CONCEPT = "Number of arguments in invocation should match the number of properties in concept: ";
    private static final String MESSAGE__SHOULD_MATCH_PARAMETER_TYPE_IN_CONCEPT_PROPERTY = "Argument type should match parameter type in concept property: ";

    @Override
    public boolean evaluate(final Invocation self)
    {
        if (self.getParameters().size() == self.getArguments().size())
        {
            return seq(self.getParameterizedArguments()).allMatch(t -> self.typeMatches(t.v1, t.v2));
        }

        return false;
    }

    @Override
    public Diagnostic createDiagnostic(final Invocation self)
    {
        return new Diagnostic(
            "matching_function_for_invocation",
            self,
            getDiagnosticMessage(self),
            getDiagnosticParticipants(self));
    }

    private String getDiagnosticMessage(final Invocation self)
    {
        if (self.getFunction().isPresent())
        {
            final Function function = self.getFunction().get();

            if (function.getParameters().size() == self.getArguments().size())
            {
                return MESSAGE__SHOULD_MATCH_PARAMETER_TYPE_IN_FUNCTION + self.getName();
            }
            else
            {
                return MESSAGE__SHOULD_MATCH_NUMBER_OF_PARAMS_IN_FUNCTION + self.getName();
            }
        }
        else if (self.getConcept().isPresent())
        {
            if (self.getParameters().size() == self.getArguments().size())
            {
                return MESSAGE__SHOULD_MATCH_PARAMETER_TYPE_IN_CONCEPT_PROPERTY + self.getName();
            }
            else
            {
                return MESSAGE__SHOULD_MATCH_NUMBER_OF_PROPS_IN_CONCEPT + self.getName();
            }
        }

        return MESSAGE__UNABLE_TO_FIND_FUNCTION_OF_INVOCATION + self.getName();
    }

    private List<? extends ModelElement> getDiagnosticParticipants(final Invocation self)
    {
        if (self.getFunction().isPresent())
        {
            final Function function = self.getFunction().get();

            if (function.getParameters().size() == self.getArguments().size())
            {
                return seq(self.getParameterizedArguments()).filter(t -> !self.typeMatches(t.v1, t.v2))
                                                       .flatMap(Tuple2::toSeq)
                                                       .map(e -> (ModelElement)e)
                                                       .toList();
            }
        }

        return emptyList();
    }
}
