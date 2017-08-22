package templates.lang.common;

import cml.language.foundation.Model;
import cml.language.expressions.Expression;
import cml.language.expressions.Literal;
import cml.language.expressions.Path;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.Property;
import cml.language.types.NamedType;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertNotNull;

public class ClassTest extends LangTest
{
    public ClassTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "class";
    }

    @Test
    public void class2__concept_abstract_ancestor_multiple() throws IOException
    {
        final Concept concept = createConceptWithMultipleAncestors(true);

        testClassTemplateWithSuffix(concept, "class2__concept_abstract_ancestor_multiple.txt");
    }

    @Test
    public void class2__concept_concrete_ancestor_empty() throws IOException
    {
        final Module module = createModule();

        final Concept product = Concept.create("Product");
        product.addMember(Property.create("description", NamedType.create("String")));
        module.addMember(product);

        final Concept intermediate = Concept.create("Intermediate");
        intermediate.addDirectAncestor(product);
        module.addMember(intermediate);

        final Concept book = Concept.create("Book");
        book.addDirectAncestor(intermediate);
        module.addMember(book);

        testClassTemplateWithSuffix(book, "class2__concept_concrete_ancestor_empty.txt");
    }

    @Test
    public void class2__concept_concrete_ancestor_multiple() throws IOException
    {
        final Concept concept = createConceptWithMultipleAncestors(false);

        concept.addMember(Property.create("initProperty", null, Literal.create("initialValue", NamedType.create("String"))));

        testClassTemplateWithSuffix(concept, "class2__concept_concrete_ancestor_multiple.txt");
    }

    @Test
    public void class__concept_abstract() throws IOException
    {
        final Module module = createModule();

        final Concept product = Concept.create("Product");
        product.addMember(Property.create("description", NamedType.create("String")));
        module.addMember(product);

        final Concept book = Concept.create("Book", true);
        book.addMember(Property.create("title", NamedType.create("String")));
        book.addDirectAncestor(product);
        module.addMember(book);

        testClassTemplateWithConcept(book, "class__concept_abstract.txt");
    }

    @Test
    public void class__concept_concrete() throws IOException
    {
        final Module module = createModule();

        final Concept product = Concept.create("Product", true);
        product.addMember(Property.create("name", NamedType.create("String"), null, true));
        product.addMember(Property.create("description", NamedType.create("String")));
        product.addMember(Property.create("value", NamedType.create("Decimal")));
        module.addMember(product);

        final Concept book = Concept.create("Book");
        book.addMember(Property.create("title", NamedType.create("String")));
        book.addMember(
            Property.create(
                "name",
                NamedType.create("String"),
                Path.create(singletonList("title")),
                true));
        book.addMember(Property.create("value", null, Literal.create("3", NamedType.create("Decimal")), true));
        book.addDirectAncestor(product);
        module.addMember(book);

        testClassTemplateWithConcept(product, "class__concept_concrete__product.txt");
        testClassTemplateWithConcept(book, "class__concept_concrete__book.txt");
    }

    @Test
    public void class__concept_empty() throws IOException
    {
        final Module module = createModule();

        final Concept concept = Concept.create("Book");
        module.addMember(concept);

        testClassTemplateWithConcept(concept, "class__concept_empty.txt");
    }

    @Test
    public void class__concept_property_optional() throws IOException
    {
        final Module module = createModule();

        final Concept concept = Concept.create("Book");
        module.addMember(concept);

        concept.addMember(Property.create("title", NamedType.create("String", null)));
        concept.addMember(Property.create("sequel", NamedType.create("Book", "?")));

        testClassTemplateWithConcept(concept, "class__concept_property_optional.txt");
    }

    @Test
    public void class__concept_property_required() throws IOException
    {
        final Module module = createModule();

        final Concept concept = Concept.create("Book");
        module.addMember(concept);

        concept.addMember(Property.create("title", NamedType.create("String", null)));

        testClassTemplateWithConcept(concept, "class__concept_property_required.txt");
    }

    @Test
    public void class__concept_property_sequence() throws IOException
    {
        final Module module = createModule();

        final Concept concept = Concept.create("Book");
        module.addMember(concept);

        concept.addMember(Property.create("title", NamedType.create("String", null)));
        concept.addMember(Property.create("sequel", NamedType.create("Book", "?")));
        concept.addMember(Property.create("categories", NamedType.create("String", "*")));

        testClassTemplateWithConcept(concept, "class__concept_property_sequence.txt");
    }

    @Test
    public void class__concept_property_initialized() throws IOException
    {
        final Module module = createModule();
        
        final Concept concept = Concept.create("Book");
        module.addMember(concept);

        final Expression value = Literal.create("No Title", NamedType.create("String", null));
        concept.addMember(Property.create("title", NamedType.create("String", null), value));

        testClassTemplateWithConcept(concept, "class__concept_property_initialized.txt");
    }

    private void testClassTemplateWithConcept(Concept concept, String expectedOutputFileName) throws IOException
    {
        testTemplateWithConcept("class", concept, expectedOutputFileName);
    }

    private void testClassTemplateWithSuffix(Concept concept, String expectedOutputFileName) throws IOException
    {
        final String templateName = "class2";

        final ST template = getTemplate(templateName);
        assertNotNull("Expected template: " + templateName, template);

        template.add("concept", concept);
        template.add("class_name_suffix", "Impl");

        final String result = template.render();
        assertThatOutputMatches(expectedOutputFileName, result);
    }

    private static Concept createConceptWithMultipleAncestors(boolean _abstract)
    {
        final Module module = createModule();

        final Concept baseConcept = Concept.create("Base", true);
        baseConcept.addMember(Property.create("baseConcreteProperty", NamedType.create("String")));
        baseConcept.addMember(Property.create("baseAbstractProperty", NamedType.create("String"), null, true));
        baseConcept.addMember(Property.create("derivedBaseProperty", null, Literal.create("initValue", NamedType.create("String")), true));
        module.addMember(baseConcept);

        final Concept product = Concept.create("Product");
        product.addMember(Property.create("description", NamedType.create("String", null)));
        product.addMember(Property.create("baseAbstractProperty", null, Literal.create("1", NamedType.create("String")), true));
        product.addDirectAncestor(baseConcept);
        module.addMember(product);

        final Concept stockItem = Concept.create("StockItem");
        stockItem.addMember(Property.create("quantity", NamedType.create("Integer", null)));
        stockItem.addMember(Property.create("baseAbstractProperty", null, Literal.create("2", NamedType.create("String")), true));
        stockItem.addDirectAncestor(baseConcept);
        module.addMember(stockItem);

        final Concept book = Concept.create("Book", _abstract);
        book.addMember(Property.create("title", NamedType.create("String")));
        book.addMember(Property.create("baseAbstractProperty", null, Literal.create("3", NamedType.create("String")), true));
        book.addDirectAncestor(product);
        book.addDirectAncestor(stockItem);
        module.addMember(book);

        if (_abstract)
        {
            book.addMember(Property.create("abstractProperty", NamedType.create("String"), null, true));
        }

        return book;
    }

    private static Module createModule()
    {
        final Model model = Model.create();
        final Module module = Module.create("some_module");
        model.addMember(module);
        return module;
    }
}
