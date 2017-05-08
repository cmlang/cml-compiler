package templates.lang.common;

import cml.language.features.Concept;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.IOException;

public class InterfaceTest extends LangTest
{
    public InterfaceTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "interface";
    }

    @Test
    public void concept_abstract() throws IOException
    {
        final Concept concept = Concept.create("Book", true);

        concept.addMember(Property.create("title", Type.create("String", null)));

        testInterfaceTemplateWithCreateMethod(concept, "concept_abstract.txt");
    }

    @Test
    public void concept_abstract_ancestor() throws IOException
    {
        final Concept concept = Concept.create("Book", true);

        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addDirectAncestor(Concept.create("Product"));

        testInterfaceTemplateWithCreateMethod(concept, "concept_abstract_ancestor.txt");
    }

    @Test
    public void concept_concrete() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addMember(Property.create("sequel", Type.create("Book", "?")));
        concept.addMember(Property.create("categories", Type.create("Category", "*")));

        testInterfaceTemplateWithCreateMethod(concept, "concept_concrete.txt");
    }

    @Test
    public void concept_concrete_ancestor() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addDirectAncestor(Concept.create("Product"));

        testInterfaceTemplateWithCreateMethod(concept, "concept_concrete_ancestor.txt");
    }

    @Test
    public void concept_concrete_ancestor_empty() throws IOException
    {
        final Concept productConcept = Concept.create("Product");
        productConcept.addMember(Property.create("description", Type.create("String", null)));

        final Concept intermediateConcept = Concept.create("Intermediate");
        intermediateConcept.addDirectAncestor(productConcept);

        final Concept bookConcept = Concept.create("Book");
        bookConcept.addDirectAncestor(intermediateConcept);

        testInterfaceTemplateWithCreateMethod(bookConcept, "concept_concrete_ancestor_empty.txt");
    }

    @Test
    public void concept_concrete_ancestor_multiple() throws IOException
    {
        final Concept baseConcept = Concept.create("Base");
        baseConcept.addMember(Property.create("baseProperty", Type.create("String", null)));

        final Concept productConcept = Concept.create("Product");
        productConcept.addDirectAncestor(baseConcept);

        final Concept stockItemConcept = Concept.create("StockItem");
        stockItemConcept.addDirectAncestor(baseConcept);

        final Concept concept = Concept.create("Book");
        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addDirectAncestor(productConcept);
        concept.addDirectAncestor(stockItemConcept);

        testInterfaceTemplateWithCreateMethod(concept, "concept_concrete_ancestor_multiple.txt");
    }

    @Test
    public void concept_empty() throws IOException
    {
        final Concept concept = Concept.create("EmptyBook");

        testInterfaceTemplateWithConcept(concept, "concept_empty.txt");
    }

    @Test
    public void concept_property_optional() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addMember(Property.create("sequel", Type.create("Book", "?")));

        testInterfaceTemplateWithConcept(concept, "concept_property_optional.txt");
    }

    @Test
    public void concept_property_required() throws IOException
    {
        final Concept concept = Concept.create("StringTitledBook");

        concept.addMember(Property.create("title", Type.create("String", null)));

        testInterfaceTemplateWithConcept(concept, "concept_property_required.txt");
    }

    @Test
    public void concept_property_set() throws IOException
    {
        final Concept concept = Concept.create("Book");

        concept.addMember(Property.create("title", Type.create("String", null)));
        concept.addMember(Property.create("sequel", Type.create("Book", "?")));
        concept.addMember(Property.create("categories", Type.create("Category", "*")));

        testInterfaceTemplateWithConcept(concept, "concept_property_set.txt");
    }

    private void testInterfaceTemplateWithConcept(Concept concept, String expectedOutputFileName) throws IOException
    {
        testTemplateWithConcept("interface", concept, expectedOutputFileName);
    }

    private void testInterfaceTemplateWithCreateMethod(Concept concept, String expectedOutputFileName)
        throws IOException
    {
        final ST template = getTemplate("interface");

        template.add("concept", concept);
        template.add("class_name_suffix", "Impl");

        final String result = template.render();

        assertThatOutputMatches(expectedOutputFileName, result);
    }
}
