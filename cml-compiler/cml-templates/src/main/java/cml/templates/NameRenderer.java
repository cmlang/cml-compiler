package cml.templates;

import org.stringtemplate.v4.StringRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.stream;
import static java.util.Collections.synchronizedMap;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.splitByCharacterTypeCamelCase;

public class NameRenderer extends StringRenderer
{
    private static final String PASCAL_CASE = "pascal-case";
    private static final String CAMEL_CASE = "camel-case";
    private static final String UNDERSCORE_CASE = "underscore-case";
    private static final String LOWER_CASE = "lower-case";
    private static final String UPPER_CASE = "upper-case";
    private static final String INDEXED = "indexed";
    private static final String INC_INDEXED = "inc-indexed";

    private static final String UNDERSCORE = "_";
    private static final String DASH = "-";
    private static final String DOT = ".";

    private final Map<String, AtomicInteger> indices = synchronizedMap(new HashMap<>());

    @Override
    public String toString(Object o, String formatString, Locale locale)
    {
        if (o == null)
        {
            return "";
        }
        else if (PASCAL_CASE.equals(formatString))
        {
            return pascalCase(locale, o.toString());
        }
        else if (CAMEL_CASE.equals(formatString))
        {
            return camelCase(locale, o.toString());
        }
        else if (UNDERSCORE_CASE.equals(formatString))
        {
            return underscoreCase(locale, o.toString());
        }
        else if (LOWER_CASE.equals(formatString))
        {
            return o.toString().toLowerCase(locale);
        }
        else if (UPPER_CASE.equals(formatString))
        {
            return o.toString().toUpperCase(locale);
        }
        else if (INC_INDEXED.equals(formatString))
        {
            return new_indexed(o.toString());
        }
        else if (INDEXED.equals(formatString))
        {
            return indexed(o.toString());
        }
        else
        {
            return super.toString(o, formatString, locale);
        }
    }

    private String new_indexed(final String text)
    {
        indices.computeIfAbsent(text, key -> new AtomicInteger(0));

        return text + indices.get(text).incrementAndGet();
    }

    private String indexed(final String text)
    {
        indices.computeIfAbsent(text, key -> new AtomicInteger(0));

        return text + indices.get(text);
    }

    public static String pascalCase(Locale locale, String str)
    {
        final List<String> words = words(str);
        final StringBuilder builder = new StringBuilder(str.length() + words.size());

        for (String p : words)
        {
            builder.append(capitalized(locale, p));
        }

        return builder.toString();
    }

    public static String camelCase(Locale locale, String str)
    {
        final List<String> words = words(str);
        final StringBuilder builder = new StringBuilder(str.length() + words.size());

        builder.append(words.get(0).toLowerCase(locale));
        words.remove(0);

        for (String p : words)
        {
            builder.append(capitalized(locale, p));
        }

        return builder.toString();
    }

    public static String underscoreCase(Locale locale, String str)
    {
        final List<String> words = words(str);
        final StringBuilder builder = new StringBuilder(str.length() + words.size());

        for (String p : words)
        {
            builder.append(p.toLowerCase(locale)).append(UNDERSCORE);
        }

        return builder.substring(0, builder.length() - 1);
    }

    private static List<String> words(String str)
    {
        return stream(splitByCharacterTypeCamelCase(str))
            .map(String::trim)
            .filter(word -> !isSeparator(word))
            .collect(toList());
    }

    private static boolean isSeparator(String word)
    {
        return word.equals(UNDERSCORE) || word.equals(DASH) || word.equals(DOT);
    }

    private static String capitalized(Locale locale, String word)
    {
        final StringBuilder builder = new StringBuilder(word.length());

        builder.append(word.substring(0, 1).toUpperCase(locale));
        builder.append(word.substring(1).toLowerCase(locale));

        return builder.toString();
    }
}
