package cml.language.loader;

import cml.language.expressions.Invocation;
import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.NamedElement;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.ConceptDeclarationContext;
import cml.language.types.NamedType;
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
    public void enterExpression(final CMLParser.ExpressionContext ctx)
    {
        // Some invocations may have been created from comprehensions.
        // They are not in the parse tree's invocation nodes.

        if (ctx.expr instanceof Invocation)
        {
            System.out.println("expr: " + ctx.expr);
            final Invocation invocation = (Invocation) ctx.expr;

            augmentFunctionOfInvocation(invocation);
            augmentFunctionTypeOfLambdas(invocation);
        }
    }

    @Override
    public void exitExpression(final CMLParser.ExpressionContext ctx)
    {
        // Some invocations may have been created from comprehensions.
        // They are not in the parse tree's invocation nodes.

        if (ctx.expr instanceof Invocation)
        {
            augmentScopeOfLambdas((Invocation) ctx.expr);
        }
    }

    private void augmentFunctionOfInvocation(Invocation invocation)
    {
        if (invocation.getFunction().isPresent()) return;

        module.getTemplate(invocation.getName()).ifPresent(t -> invocation.setFunction(t.getFunction()));

        seq(invocation.getArguments()).filter(a -> a instanceof Invocation)
                                      .map(a -> (Invocation)a)
                                      .forEach(this::augmentFunctionOfInvocation);
    }

    private void augmentFunctionTypeOfLambdas(Invocation invocation)
    {
        seq(invocation.getTypedLambdaArguments()).filter(t -> !t.v2.getFunctionType().isPresent())
                                                 .forEach(t -> t.v2.setFunctionType(t.v1));

        seq(invocation.getArguments()).filter(a -> a instanceof Invocation)
                                      .map(a -> (Invocation)a)
                                      .forEach(this::augmentFunctionTypeOfLambdas);
    }

    private void augmentScopeOfLambdas(Invocation invocation)
    {
        if (invocation.getFunction().isPresent())
        {
            seq(invocation.getArguments()).filter(a -> a instanceof Invocation)
                                          .map(a -> (Invocation)a)
                                          .forEach(this::augmentScopeOfLambdas);

            invocation.getTypedLambdaArguments().forEach(
                (functionType, lambda) ->
                {
                    if (lambda.getFunctionType().isPresent() && !lambda.isExpressionInSomeScope())
                    {
                        invocation.getExpressionScopeFor(lambda).ifPresent(lambda::addExpressionToScope);
                    }

                    if (lambda.getExpression() instanceof Invocation)
                    {
                        augmentScopeOfLambdas((Invocation) lambda.getExpression());
                    }
                });
        }
    }
}
