package cml.language;

import cml.language.features.Concept;
import cml.language.foundation.Model;
import cml.language.features.Target;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser.*;

public class ModelSynthesizer extends CMLBaseListener
{
    private static final String QUOTE = "\"";

    @Override
    public void exitCompilationUnit(CompilationUnitContext ctx)
    {
        ctx.model = Model.create();

        if (ctx.declarations() != null)
        {
            ctx.declarations()
               .stream()
               .filter(node -> node.conceptDeclaration() != null)
               .forEach(node -> ctx.model.addElement(node.conceptDeclaration().concept));

            ctx.declarations()
               .stream()
               .filter(node -> node.targetDeclaration() != null)
               .forEach(node -> ctx.model.addElement(node.targetDeclaration().target));
        }
    }

    @Override
    public void exitConceptDeclaration(ConceptDeclarationContext ctx)
    {
        final String name = ctx.NAME().getText();
        final boolean _abstract = ctx.ABSTRACT() != null;

        ctx.concept = Concept.create(name, _abstract);

        if (ctx.propertyList() != null)
        {
            ctx.propertyList()
               .propertyDeclaration()
               .forEach(node -> ctx.concept.addElement(node.property));
        }
    }

    @Override
    public void exitTargetDeclaration(TargetDeclarationContext ctx)
    {
        final String name = ctx.NAME().getText();

        ctx.target = Target.create(name);

        if (ctx.propertyList() != null)
        {
            ctx.propertyList()
               .propertyDeclaration()
               .forEach(node -> ctx.target.addElement(node.property));
        }
    }

    @Override
    public void exitPropertyDeclaration(PropertyDeclarationContext ctx)
    {
        final String name = ctx.NAME().getText();
        final Type type = (ctx.typeDeclaration() == null) ? null : ctx.typeDeclaration().type;
        final String value = (ctx.STRING() == null) ? null : unwrap(ctx.STRING().getText());

        ctx.property = Property.create(name, value, type);
    }

    @Override
    public void exitTypeDeclaration(TypeDeclarationContext ctx)
    {
        final String name = ctx.NAME().getText();
        final String cardinality = (ctx.CARDINALITY() == null) ? null : ctx.CARDINALITY().getText();

        ctx.type = Type.create(name, cardinality);
    }


    private static String unwrap(String text)
    {
        if (text.startsWith(QUOTE))
        {
            text = text.substring(1);
        }

        if (text.endsWith(QUOTE))
        {
            text = text.substring(0, text.length() - 1);
        }

        return text;
    }
}
