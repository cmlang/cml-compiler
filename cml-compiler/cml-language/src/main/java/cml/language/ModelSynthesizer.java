package cml.language;

import cml.language.expressions.*;
import cml.language.features.Concept;
import cml.language.features.Import;
import cml.language.features.Module;
import cml.language.features.Task;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

class ModelSynthesizer extends CMLBaseListener
{
    private static final String QUOTE = "\"";
    private static final String INVALID_MODULE_NAME = "Module declaration name (%s) should match the module's directory name: %s";
    private static final String NO_NAME_PROVIDED_FOR_CONCEPT = "No name provided for concept.";
    private static final String NO_NAME_PROVIDED_FOR_TYPE = "No name provided for type.";
    private static final String NO_NAME_PROVIDED_FOR_PROPERTY = "No name provided for property.";
    private static final String NO_NAME_PROVIDED_FOR_TARGET = "No name provided for task.";

    private final Module module;

    ModelSynthesizer(Module module)
    {
        this.module = module;
    }

    @Override
    public void exitCompilationUnit(CompilationUnitContext ctx)
    {
        if (ctx.declarations() != null)
        {
            ctx.declarations()
               .stream()
               .filter(node -> node.conceptDeclaration() != null)
               .forEach(node -> module.addMember(node.conceptDeclaration().concept));

            ctx.declarations()
               .stream()
               .filter(node -> node.taskDeclaration() != null)
               .forEach(node -> module.addMember(node.taskDeclaration().task));
        }
    }

    @Override
    public void exitModuleDeclaration(ModuleDeclarationContext ctx)
    {
        final String name = ctx.NAME().getText();

        if (!name.equals(module.getName()))
        {
            throw new ModelSynthesisException(format(INVALID_MODULE_NAME, name, module.getName()));
        }

        if (ctx.importDeclaration() != null)
        {
            ctx.importDeclaration()
               .forEach(node -> module.addMember(node._import));
        }
    }

    @Override
    public void exitImportDeclaration(ImportDeclarationContext ctx)
    {
        final String name = ctx.NAME().getText();

        ctx._import = Import.create(name);
    }

    @Override
    public void exitConceptDeclaration(ConceptDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_CONCEPT);
        }

        final String name = ctx.NAME().getText();
        final boolean _abstract = ctx.ABSTRACT() != null;

        ctx.concept = Concept.create(name, _abstract);

        if (ctx.propertyList() != null)
        {
            ctx.propertyList()
               .propertyDeclaration()
               .forEach(node -> ctx.concept.addMember(node.property));
        }
    }

    @Override
    public void exitTaskDeclaration(TaskDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_TARGET);
        }

        final String name = ctx.NAME().getText();

        ctx.task = Task.create(name);

        if (ctx.constructorDeclaration() != null)
        {
            final String constructor = ctx.constructorDeclaration().NAME().getText();

            ctx.task.setConstructor(constructor);
        }

        if (ctx.propertyList() != null)
        {
            ctx.propertyList()
               .propertyDeclaration()
               .forEach(node -> ctx.task.addMember(node.property));
        }
    }

    @Override
    public void exitPropertyDeclaration(PropertyDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_PROPERTY);
        }

        final String name = ctx.NAME().getText();
        final Type type = (ctx.typeDeclaration() == null) ? null : ctx.typeDeclaration().type;
        final Expression value = (ctx.expression() == null) ? null : ctx.expression().expr;
        final Property property = Property.create(name, value, type);

        property.addMember(value);

        ctx.property = property;
    }

    @Override
    public void exitTypeDeclaration(TypeDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_TYPE);
        }

        final String name = ctx.NAME().getText();
        final String cardinality = (ctx.cardinality() == null) ? null : ctx.cardinality().getText();

        ctx.type = Type.create(name, cardinality);
    }

    @Override
    public void exitExpression(ExpressionContext ctx)
    {
        if (ctx.literalExpression() != null) ctx.expr = ctx.literalExpression().literal;
        else if (ctx.pathExpression() != null) ctx.expr = ctx.pathExpression().path;
        else if (ctx.operator != null && ctx.expression().size() == 1) ctx.expr = createUnary(ctx);
        else if (ctx.operator != null && ctx.expression().size() == 2) ctx.expr = createInfix(ctx);
        else if (ctx.cond != null) ctx.expr = createConditional(ctx);
        else if (ctx.queryExpression() != null) ctx.expr = ctx.queryExpression().expr;
    }

    private Unary createUnary(ExpressionContext ctx)
    {
        final String operator = ctx.operator.getText();
        final Expression expr = ctx.expression().get(0).expr;
        final Unary unary = Unary.create(operator, expr);

        unary.addMember(expr);
        
        return unary;
    }

    private Infix createInfix(ExpressionContext ctx)
    {
        final String operator = ctx.operator.getText();
        final Expression left = ctx.expression().get(0).expr;
        final Expression right = ctx.expression().get(1).expr;
        final Infix infix = Infix.create(operator, left, right);

        infix.addMember(left);
        infix.addMember(right);

        return infix;
    }

    private Conditional createConditional(ExpressionContext ctx)
    {
        final Expression cond = ctx.cond.expr;
        final Expression then = ctx.then.expr;
        final Expression else_ = ctx.else_.expr;
        final Conditional conditional = Conditional.create(cond, then, else_);

        conditional.addMember(cond);
        conditional.addMember(then);
        conditional.addMember(else_);

        return conditional;
    }

    @Override
    public void exitQueryExpression(QueryExpressionContext ctx)
    {
        if (ctx.pathExpression() != null)
        {
            ctx.expr = ctx.pathExpression().path;
        }
        else if (ctx.queryExpression() != null)
        {
            final QueryExpressionContext baseCtx = ctx.queryExpression();

            if (baseCtx.joinExpression() != null)
            {
                final Join join = baseCtx.joinExpression().join;
                final Transform transform = createTransformWithVariables(
                    join.getVariables(),
                    ctx.transformDeclaration().transform);

                if (join.isComplete())
                {
                    ctx.expr = createQuery(join, transform);
                }
                else
                {
                    ctx.expr = createQuery(join.getFirstPath(), transform);
                }
            }
            else if (isSelectionTransform(baseCtx))
            {
                final Query baseQuery = (Query) baseCtx.expr;
                final Transform transform = createTransformWithVariables(
                    baseQuery.getTransform().getVariables(),
                    ctx.transformDeclaration().transform);

                ctx.expr = createQuery(baseCtx.expr, transform);
            }
            else
            {
                ctx.expr = createQuery(baseCtx.expr, ctx.transformDeclaration().transform);
            }
        }
    }

    private Query createQuery(Expression baseExpr, Transform transform)
    {
        final Query query = Query.create(baseExpr, transform);

        query.addMember(baseExpr);

        if (transform.getExpr().isPresent())
        {
            baseExpr.addMember(transform.getExpr().get());
        }

        if (transform.getInit().isPresent())
        {
            baseExpr.addMember(transform.getInit().get());
        }

        return query;
    }

    private boolean isSelectionTransform(QueryExpressionContext ctx)
    {
        final TransformDeclarationContext transformCtx = ctx.transformDeclaration();

        return transformCtx != null && (transformCtx.SELECT() != null || transformCtx.REJECT() != null);
    }

    private Transform createTransformWithVariables(List<String> variables, Transform originalTransform)
    {
        return Transform.create(
            originalTransform.getOperation(),
            variables,
            originalTransform.getExpr().orElseGet(null)
        );
    }

    @Override
    public void exitJoinExpression(JoinExpressionContext ctx)
    {
        final List<String> variables = ctx.enumeratorDeclaration()
                                          .stream()
                                          .map(e -> e.NAME().getText())
                                          .collect(Collectors.toList());

        final List<Path> paths = ctx.enumeratorDeclaration()
                                          .stream()
                                          .map(e -> e.pathExpression().path)
                                          .collect(Collectors.toList());

        ctx.join = Join.create(variables, paths);
    }

    @Override
    public void exitTransformDeclaration(TransformDeclarationContext ctx)
    {
        final String operation = ctx.operation.getText();
        final String suffix = ctx.suffix == null ? null : ctx.suffix.getText();

        ctx.transform = Transform.create(operation, suffix, ctx.expr.expr);
    }

    @Override
    public void exitPathExpression(PathExpressionContext ctx)
    {
        ctx.path = Path.create(pathNames(ctx));
    }

    private List<String> pathNames(PathExpressionContext ctx)
    {
        return ctx.NAME().stream()
                         .map(ParseTree::getText)
                         .collect(Collectors.toList());
    }

    @Override
    public void exitLiteralExpression(LiteralExpressionContext ctx)
    {
        final String text = getText(ctx);

        if (text != null)
        {
            final Type type = Type.create(getPrimitiveTypeName(ctx), null);
            ctx.literal = Literal.create(text, type);
        }
    }

    private static String getText(LiteralExpressionContext ctx)
    {
        if (ctx.BOOLEAN() != null) return ctx.BOOLEAN().getText();
        else if (ctx.STRING() != null) return unwrap(ctx.STRING().getText());
        else if (ctx.INTEGER() != null) return ctx.INTEGER().getText();
        else if (ctx.DECIMAL() != null) return ctx.DECIMAL().getText();
        else return null;
    }

    private static String getPrimitiveTypeName(LiteralExpressionContext ctx)
    {
        if (ctx.BOOLEAN() != null) return "Boolean";
        else if (ctx.STRING() != null) return "String";
        else if (ctx.INTEGER() != null) return "Integer";
        else if (ctx.DECIMAL() != null) return "Decimal";
        else return null;
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
