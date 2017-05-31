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

        concept.addMember(Property.create("initProperty", null, Literal.create("initialValue", Type.create("String"))));

        testClassTemplateWithSuffix(concept, "class2__concept_concrete_ancestor_multiple.txt");
    }

    @Test
    public void class__concept_abstract() throws IOException
    {
        final Concept productConcept = Concept.create("Product");
        productConcept.addMember(Property.create("description", Type.create("String")));

        final Concept bookConcept = Concept.create("Book", true);
        bookConcept.addMember(Property.create("title", Type.create("String")));
        bookConcept.addDirectAncestor(productConcept);

        testClassTemplateWithConcept(bookConcept, "class__concept_abstract.txt");
    }

    @Test
    public void class__concept_concrete() throws IOException
    {
        final Concept productConcept = Concept.create("Product", true);
        productConcept.addMember(Property.create("name", Type.create("String"), null, true));
        productConcept.addMember(Property.create("description", Type.create("String")));
        productConcept.addMember(Property.create("value", Type.create("Decimal")));

        final Concept bookConcept = Concept.create("Book");
        bookConcept.addMember(Property.create("title", Type.create("String")));
        bookConcept.addMember(
            Property.create(
                "name",
                Type.create("String"),
                Path.create(asList("title")),
                true));
        bookConcept.addMember(Property.create("value", null, Literal.create("3", Type.create("Decimal")), true));
        bookConcept.addDirectAncestor(productConcept);

        testClassTemplateWithConcept(productConcept, "class__concept_concrete__product.txt");
        testClassTemplateWithConcept(bookConcept, "class__concept_concrete__book.txt");
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
    public void class__concept_property_sequence() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addMember(Property.create("sequel", Type.create("Book", "?")));
        concept.addMember(Property.create("categories", Type.create("Category", "*")));

        testClassTemplateWithConcept(concept, "class__concept_property_sequence.txt");
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
        baseConcept.addMember(Property.create("derivedBaseProperty", null, Literal.create("initValue", Type.create("String")), true));

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

        if (_abstract)
        {
            concept.addMember(Property.create("abstractProperty", Type.create("String"), null, true));
        }

        return concept;
    }

}
