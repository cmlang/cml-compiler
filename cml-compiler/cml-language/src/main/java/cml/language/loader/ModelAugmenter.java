package cml.language.loader;

import cml.language.features.AssociationEnd;
import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.generated.NamedElement;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.ConceptDeclarationContext;
import cml.language.types.NamedType;
import cml.language.types.TempType;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cml.language.functions.ModuleFunctions.conceptOf;

class ModelAugmenter extends CMLBaseListener
{
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

        conceptOf(module, associationEnd.getConceptName())
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
        final TempType type = ctx.type;

        if (type != null && type instanceof NamedType && !type.isPrimitive())
        {
            final NamedType namedType = (NamedType)type;

            conceptOf(module, namedType.getName()).ifPresent(type::setConcept);
        }
    }
}
