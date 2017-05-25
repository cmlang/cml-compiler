package cml.language;

import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.NamedElement;
import cml.language.foundation.Type;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.ConceptDeclarationContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class ModelAugmenter extends CMLBaseListener
{
    private static final String UNABLE_TO_FIND_CONCEPT_IN_TYPE_DECLARATION = "Unable to find concept in type declaration: %s";
    
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
                                                         .map(name -> module.getConcept(name))
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

        if (!type.isPrimitive())
        {
            module.getConcept(type.getName()).ifPresent(type::setConcept);

            if (!type.getConcept().isPresent())
            {
                throw new ModelAugmentationException(UNABLE_TO_FIND_CONCEPT_IN_TYPE_DECLARATION, type.getName());
            }
        }
    }
}
