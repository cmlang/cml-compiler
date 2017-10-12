package cml.language.loader;

import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.generated.*;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.ConceptDeclarationContext;
import cml.language.types.TempNamedType;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cml.language.functions.ModuleFunctions.conceptOf;
import static cml.language.generated.AssociationEnd.createAssociationEnd;
import static cml.language.generated.Inheritance.createInheritance;
import static org.jooq.lambda.Seq.seq;

class ModelAugmenter extends CMLBaseListener
{
    private static final String NO_CONCEPT_NAME_PROVIDED_FOR_ASSOCIATION_END = "No concept name provided for association end.";
    private static final String NO_PROPERTY_NAME_PROVIDED_FOR_ASSOCIATION_END = "No property name provided for association end.";

    private final TempModule module;

    ModelAugmenter(TempModule module)
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

            final List<TempConcept> foundAncestors = ancestorNames.stream()
                                                                  .map(name -> conceptOf(module, name))
                                                                  .filter(Optional::isPresent)
                                                                  .map(Optional::get)
                                                                  .collect(Collectors.toList());

            final List<String> foundAncestorNames = foundAncestors.stream()
                                                                  .map(NamedElement::getName)
                                                                  .collect(Collectors.toList());

            foundAncestors.forEach(ancestor -> createInheritance(ancestor, ctx.concept));

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
        if (ctx.conceptName == null)
        {
            throw new ModelSynthesisException(NO_CONCEPT_NAME_PROVIDED_FOR_ASSOCIATION_END);
        }

        if (ctx.propertyName == null)
        {
            throw new ModelSynthesisException(NO_PROPERTY_NAME_PROVIDED_FOR_ASSOCIATION_END);
        }

        final Association association = ctx.association;
        final String conceptName = ctx.conceptName.getText();
        final String propertyName = ctx.propertyName.getText();
        final @Nullable Type propertyType = (ctx.typeDeclaration() == null) ? null : ctx.typeDeclaration().type;
        final Optional<TempConcept> concept = conceptOf(module, conceptName);
        final Optional<Property> property = seq(concept).flatMap(c -> c.getAllProperties().stream())
                                                        .filter(p -> p.getName().equals(propertyName))
                                                        .findFirst();

        createAssociationEnd(association, locationOf(ctx), conceptName, propertyName, propertyType, concept.orElse(null), property.orElse(null));
    }

    @Override
    public void enterTypeDeclaration(CMLParser.TypeDeclarationContext ctx)
    {
        final Type type = ctx.type;

        if (type != null && type instanceof TempNamedType && !type.isPrimitive())
        {
            final TempNamedType namedType = (TempNamedType)type;

            conceptOf(module, namedType.getName()).ifPresent(namedType::setConcept);
        }
    }

    private Location locationOf(ParserRuleContext ctx)
    {
        return createLocation(ctx, null);
    }

    private Location createLocation(ParserRuleContext ctx, ModelElement element)
    {
        final Token token = ctx.getStart();

        return Location.createLocation(token.getLine(), token.getCharPositionInLine() + 1, element);
    }
}
