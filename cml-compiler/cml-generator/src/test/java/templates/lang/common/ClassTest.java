package templates.lang.common;

import cml.language.expressions.Expression;
import cml.language.expressions.Literal;
import cml.language.expressions.Path;
import cml.language.features.Concept;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.IOException;

import static java.util.Arrays.asList;
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
        final Concept productConcept = Concept.create("Product");
        productConcept.addMember(Property.create("description", Type.create("String", null)));

        final Concept intermediateConcept = Concept.create("Intermediate");
        intermediateConcept.addDirectAncestor(productConcept);

        final Concept bookConcept = Concept.create("Book");
        bookConcept.addDirectAncestor(intermediateConcept);

        testClassTemplateWithSuffix(bookConcept, "class2__concept_concrete_ancestor_empty.txt");
    }

    @Test
    public void class2__concept_concrete_ancestor_multiple() throws IOException
    {
        final Concept concept = createConceptWithMultipleAncestors(false);

        testClassTemplateWithSuffix(concept, "class2__concept_concrete_ancestor_multiple.txt");
    }

    @Test
    public void class__concept_abstract_ancestor() throws IOException
    {
        final Concept productConcept = Concept.create("Product");
        productConcept.addMember(Property.create("name", Type.create("String", null)));
        productConcept.addMember(Property.create("description", Type.create("String", null)));

        final Concept bookConcept = Concept.create("Book", true);
        bookConcept.addMember(Property.create("title", Type.create("String", null)));
        bookConcept.addMember(
            Property.create(
                "name",
                Type.create("String"),
                Path.create(asList("title")),
                true));
        bookConcept.addDirectAncestor(productConcept);

        testClassTemplateWithConcept(bookConcept, "class__concept_abstract_ancestor.txt");
    }

    @Test
    public void class__concept_concrete_ancestor() throws IOException
    {
        final Concept productConcept = Concept.create("Product");
        productConcept.addMember(Property.create("description", Type.create("String", null)));

        final Concept bookConcept = Concept.create("Book");
        bookConcept.addMember(Property.create("title", Type.create("String", null)));
        bookConcept.addDirectAncestor(productConcept);

        testClassTemplateWithConcept(bookConcept, "class__concept_concrete_ancestor.txt");
    }

    @Test
    public void class__concept_empty() throws IOException
    {
        final Concept concept = Concept.create("Book");

        testClassTemplateWithConcept(concept, "class__concept_empty.txt");
    }

    @Test
    public void class__concept_property_optional() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addMember(Property.create("sequel", Type.create("Book", "?")));

        testClassTemplateWithConcept(concept, "class__concept_property_optional.txt");
    }

    @Test
    public void class__concept_property_required() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addMember(Property.create("title", Type.create("String", null)));

        testClassTemplateWithConcept(concept, "class__concept_property_required.txt");
    }

    @Test
    public void class__concept_property_set() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addMember(Property.create("sequel", Type.create("Book", "?")));
        concept.addMember(Property.create("categories", Type.create("Category", "*")));

        testClassTemplateWithConcept(concept, "class__concept_property_set.txt");
    }

    @Test
    public void class__concept_property_initialized() throws IOException
    {
        final Concept concept = Concept.create("Book");

        final Expression value = Literal.create("No Title", Type.create("String", null));
        concept.addMember(Property.create("title", Type.create("String", null), value));

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
        final Concept baseConcept = Concept.create("Base", true);
        baseConcept.addMember(Property.create("baseConcreteProperty", Type.create("String")));
        baseConcept.addMember(Property.create("baseAbstractProperty", Type.create("String"), null, true));

        final Concept productConcept = Concept.create("Product");
        productConcept.addMember(Property.create("description", Type.create("String", null)));
        productConcept.addMember(Property.create("baseAbstractProperty", null, Literal.create("1", Type.create("String")), true));
        productConcept.addDirectAncestor(baseConcept);

        final Concept stockItemConcept = Concept.create("StockItem");
        stockItemConcept.addMember(Property.create("quantity", Type.create("Integer", null)));
        stockItemConcept.addMember(Property.create("baseAbstractProperty", null, Literal.create("2", Type.create("String")), true));
        stockItemConcept.addDirectAncestor(baseConcept);

        final Concept concept = Concept.create("Book", _abstract);
        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addMember(Property.create("baseAbstractProperty", null, Literal.create("3", Type.create("String")), true));
        concept.addDirectAncestor(productConcept);
        concept.addDirectAncestor(stockItemConcept);

        return concept;
    }

}
