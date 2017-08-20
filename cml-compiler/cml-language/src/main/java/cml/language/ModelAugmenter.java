package cml.language;

import cml.language.expressions.Invocation;
import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.NamedElement;
import cml.language.types.NamedType;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.ConceptDeclarationContext;
import cml.language.types.Type;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jooq.lambda.Seq.seq;

class ModelAugmenter extends CMLBaseListener
{
    private final Module module;

    ModelAugmenter(Module module)
    {
        this.module = module;
    }

    @Override
    public void enterConceptDeclaration(ConceptDeclarationContext ctx)
    {
        if (ctx.generalizations() != null)
        {
            final List<String> ancestorNames = ctx.generalizations().NAME()
                                                  .stream()
                                                  .map(ParseTree::getText)
                                                  .collect(Collectors.toList());

            final List<Concept> foundAncestors = ancestorNames.stream()
                                                         .map(module::getConcept)
                                                         .filter(Optional::isPresent)
                                                         .map(Optional::get)
                                                         .collect(Collectors.toList());

            final List<String> foundAncestorNames = foundAncestors.stream()
                                                                  .map(NamedElement::getName)
                                                                  .collect(Collectors.toList());

            foundAncestors.forEach(ancestor -> ctx.concept.addDirectAncestor(ancestor));

            final List<String> missingAncestorNames = ancestorNames.stream()
                                                                   .filter(name -> !foundAncestorNames.contains(name))
                                                                   .collect(Collectors.toList());

            if (missingAncestorNames.size() > 0)
            {
                final String missingAncestors = missingAncestorNames.toString();
                throw new ModelAugmentationException(
                    "Unable to find ancestors: %s",
                    missingAncestors.substring(1, missingAncestors.length() - 1));
            }
        }
    }

    @Override
    public void enterAssociationEndDeclaration(CMLParser.AssociationEndDeclarationContext ctx)
    {
        final AssociationEnd associationEnd = ctx.associationEnd;

        module.getConcept(associationEnd.getConceptName())
              .ifPresent(associationEnd::setConcept);

        if (associationEnd.getConcept().isPresent())
        {
            associationEnd.getConcept().get().getAllProperties()
                                             .stream()
                                             .filter(property -> property.getName().equals(associationEnd.getPropertyName()))
                                             .findFirst()
                                             .ifPresent(associationEnd::setProperty);
        }
    }

    @Override
    public void enterTypeDeclaration(CMLParser.TypeDeclarationContext ctx)
    {
        final Type type = ctx.type;

        if (type != null && type instanceof NamedType && !type.isPrimitive())
        {
            final NamedType namedType = (NamedType)type;

            module.getConcept(namedType.getName()).ifPresent(type::setConcept);
        }
    }

    @Override
    public void enterExpression(CMLParser.ExpressionContext ctx)
    {
        if (ctx.expr instanceof Invocation)
        {
            augmentInvocation((Invocation) ctx.expr);
        }
    }

    private void augmentInvocation(Invocation invocation)
    {
        module.getTemplate(invocation.getName()).ifPresent(t -> invocation.setFunction(t.getFunction()));

        if (invocation.getFunction().isPresent())
        {
            invocation.getTypedLambdaArguments().forEach(
                (functionType, lambda) -> lambda.setFunctionType(functionType));

            invocation.getArguments().forEach(
                expression -> invocation.getParentScopeOf(expression).addMember(expression));
        }

        seq(invocation.getArguments()).filter(a -> a instanceof Invocation)
                                      .map(a -> (Invocation)a)
                                      .forEach(this::augmentInvocation);
    }
}
