package cml.language;

import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.NamedElement;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser.ConceptDeclarationContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (ctx.ancestorList() != null)
        {
            final List<String> ancestorNames = ctx.ancestorList().NAME()
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

            final List<String> missingAncestorNames = ancestorNames.stream()
                                                                   .filter(name -> !foundAncestorNames.contains(name))
                                                                   .collect(Collectors.toList());

            foundAncestors.forEach(ancestor -> ctx.concept.addDirectAncestor(ancestor));
            missingAncestorNames.forEach(name -> ctx.concept.addMissingAncestor(name));
        }
    }
}
