package templates.lang.common;

import cml.language.features.Concept;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.IOException;

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
        productConcept.addElement(Property.create("description", null, Type.create("String", null)));

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
        productConcept.addElement(Property.create("description", null, Type.create("String", null)));

        final Concept bookConcept = Concept.create("Book", true);
        bookConcept.addElement(Property.create("title", null, Type.create("String", null)));
        bookConcept.addDirectAncestor(productConcept);

        testClassTemplateWithConcept(bookConcept, "class__concept_abstract_ancestor.txt");
    }

    @Test
    public void class__concept_concrete_ancestor() throws IOException
    {
        final Concept productConcept = Concept.create("Product");
        productConcept.addElement(Property.create("description", null, Type.create("String", null)));

        final Concept bookConcept = Concept.create("Book");
        bookConcept.addElement(Property.create("title", null, Type.create("String", null)));
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

        concept.addElement(Property.create("title", null, Type.create("String", null)));
        concept.addElement(Property.create("sequel", null, Type.create("Book", "?")));

        testClassTemplateWithConcept(concept, "class__concept_property_optional.txt");
    }

    @Test
    public void class__concept_property_required() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addElement(Property.create("title", null, Type.create("String", null)));

        testClassTemplateWithConcept(concept, "class__concept_property_required.txt");
    }

    @Test
    public void class__concept_property_set() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addElement(Property.create("title", null, Type.create("String", null)));
        concept.addElement(Property.create("sequel", null, Type.create("Book", "?")));
        concept.addElement(Property.create("categories", null, Type.create("Category", "*")));

        testClassTemplateWithConcept(concept, "class__concept_property_set.txt");
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
        final Concept baseConcept = Concept.create("Base");
        baseConcept.addElement(Property.create("baseProperty", null, Type.create("String", null)));

        final Concept productConcept = Concept.create("Product");
        productConcept.addElement(Property.create("description", null, Type.create("String", null)));
        productConcept.addDirectAncestor(baseConcept);

        final Concept stockItemConcept = Concept.create("StockItem");
        stockItemConcept.addElement(Property.create("quantity", null, Type.create("Integer", null)));
        stockItemConcept.addDirectAncestor(baseConcept);

        final Concept concept = Concept.create("Book", _abstract);
        concept.addElement(Property.create("title", null, Type.create("String", null)));
        concept.addDirectAncestor(productConcept);
        concept.addDirectAncestor(stockItemConcept);

        return concept;
    }

}
