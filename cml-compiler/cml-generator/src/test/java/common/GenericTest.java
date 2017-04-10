package common;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GenericTest extends TemplateTest
{
    @DataPoints("trueConditions")
    public static final Object[] trueConditions = { Boolean.TRUE, singletonList("Item") };

    @DataPoints("falseConditions")
    public static final Object[] falseConditions = { Boolean.FALSE, null, emptyList(), new ArrayList() };

    @Override
    protected String getTemplatePath()
    {
        return "common/generic";
    }

    @Theory
    public void newLineIf_true(@FromDataPoints("trueConditions") Object cond)
    {
        testTemplateWithCond("newLineIf", cond, "\n");
    }

    @Theory
    public void newLineIf_false(@FromDataPoints("falseConditions") Object cond)
    {
        testTemplateWithCond("newLineIf", cond, "");
    }

    @Theory
    public void newLineIfEither_true(
        @FromDataPoints("trueConditions") Object trueCond,
        @FromDataPoints("falseConditions") Object falseCond)
    {
        testTemplateWithCond2("newLineIfEither", trueCond, falseCond, "\n");
        testTemplateWithCond2("newLineIfEither", falseCond, trueCond, "\n");
    }

    @Theory
    public void newLineIfEither_false(@FromDataPoints("falseConditions") Object cond)
    {
        testTemplateWithCond2("newLineIfEither", cond, cond, "");
    }

    @Theory
    public void newLineIf2_true(@FromDataPoints("trueConditions") Object cond)
    {
        testTemplateWithCond2("newLineIf2", cond, cond, "\n");
    }

    @Theory
    public void newLineIf2_false(@FromDataPoints("falseConditions") Object cond)
    {
        testTemplateWithCond2("newLineIf2", cond, cond, "");
    }

    @Theory
    public void newLineIf2_mixed(
        @FromDataPoints("trueConditions") Object trueCond,
        @FromDataPoints("falseConditions") Object falseCond)
    {
        testTemplateWithCond2("newLineIf2", trueCond, falseCond, "");
        testTemplateWithCond2("newLineIf2", falseCond, trueCond, "");
    }

    @Theory
    public void commaIf_true(@FromDataPoints("trueConditions") Object cond)
    {
        testTemplateWithCond("commaIf", cond, ", ");
    }

    @Theory
    public void commaIf_false(@FromDataPoints("falseConditions") Object cond)
    {
        testTemplateWithCond("commaIf", cond, "");
    }

    @Theory
    public void commaIf2_true(@FromDataPoints("trueConditions") Object cond)
    {
        testTemplateWithCond2("commaIf2", cond, cond, ", ");
    }

    @Theory
    public void commaIf2_false(@FromDataPoints("falseConditions") Object cond)
    {
        testTemplateWithCond2("commaIf2", cond, cond,"");
    }

    @Theory
    public void commaIf2_mixed(
        @FromDataPoints("trueConditions") Object trueCond,
        @FromDataPoints("falseConditions") Object falseCond)
    {
        testTemplateWithCond2("commaIf2", trueCond, falseCond,"");
        testTemplateWithCond2("commaIf2", falseCond, trueCond,"");
    }

    @Test
    public void line_list()
    {
        testLineList(asList("a"), "a");
        testLineList(asList("a", "b"), "a\n\nb");
        testLineList(asList("a", null), "a");
        testLineList(asList("a", null, "c"), "a\n\nc");
        testLineList(asList(null, "b", "c"), "b\n\nc");
        testLineList(asList(null, null, "c"), "c");
        testLineList(asList(null, null, null), "");
        testLineList(asList("a", ""), "a");
        testLineList(asList("a", "", "c"), "a\n\nc");
        testLineList(asList("", "b", ""), "b");
        testLineList(asList("a", "b", "c"), "a\n\nb\n\nc");
        testLineList(asList(new ST("<first([\"a\", \"d\"])>"), "b", "c"), "a\n\nb\n\nc");
    }

    private void testTemplateWithCond(String templateName, Object cond, String expectedResult)
    {
        final ST template = getTemplate(templateName);

        template.add("cond", cond);

        final String result = template.render();

        assertThat(result, is(expectedResult));
    }

    private void testTemplateWithCond2(String templateName, Object cond1, Object cond2, String expectedResult)
    {
        final ST template = getTemplate(templateName);

        template.add("cond1", cond1);
        template.add("cond2", cond2);

        final String result = template.render();

        assertThat(result, is(expectedResult));
    }

    private void testLineList(Object list, String expectedResult)
    {
        testTemplateWithList("line_list", list, expectedResult);
    }

    private void testTemplateWithList(String templateName, Object list, String expectedResult)
    {
        final ST template = getTemplate(templateName);

        template.add("list", list);

        final String result = template.render();

        assertThat(result, is(expectedResult));
    }
}
