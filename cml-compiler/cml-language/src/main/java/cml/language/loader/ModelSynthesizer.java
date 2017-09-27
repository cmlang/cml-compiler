package cml.language.loader;

import cml.language.expressions.*;
import cml.language.features.*;
import cml.language.foundation.Property;
import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser.*;
import cml.language.types.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static cml.language.transforms.InvocationTransforms.invocationOf;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.empty;
import static org.jooq.lambda.Seq.seq;

class ModelSynthesizer extends CMLBaseListener
{
    private static final String QUOTE = "\"";
    private static final String INVALID_MODULE_NAME = "Module declaration name (%s) should match the module's directory name: %s";
    private static final String NO_NAME_PROVIDED_FOR_CONCEPT = "No name provided for concept.";
    private static final String NO_NAME_PROVIDED_FOR_ASSOCIATION = "No name provided for association.";
    private static final String NO_NAME_PROVIDED_FOR_PROPERTY = "No name provided for property.";
    private static final String NO_NAME_PROVIDED_FOR_TARGET = "No name provided for task.";
    private static final String NO_CONCEPT_NAME_PROVIDED_FOR_ASSOCIATION_END = "No concept name provided for association end.";
    private static final String NO_PROPERTY_NAME_PROVIDED_FOR_ASSOCIATION_END = "No property name provided for association end.";

    private final TempModule module;

    ModelSynthesizer(TempModule module)
    {
        this.module = module;
    }

    @Override
    public void exitModuleDeclaration(ModuleDeclarationContext ctx)
    {
        final String name = ctx.NAME().getText();

        if (!name.equals(module.getName()))
        {
            throw new ModelSynthesisException(format(INVALID_MODULE_NAME, name, module.getName()));
        }
    }

    @Override
    public void exitImportDeclaration(ImportDeclarationContext ctx)
    {
        final String name = ctx.NAME().getText();

        ctx._import = Import.create(module, name);
    }

    @Override
    public void exitConceptDeclaration(ConceptDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_CONCEPT);
        }

        final String name = ctx.NAME().getText();
        final boolean _abstract = ctx.ABSTRACTION() != null;
        final List<Property> propertyList = seq(ctx.propertyList() == null ? empty() : ctx.propertyList().propertyDeclaration())
            .map(node -> node.property)
            .toList();

        ctx.concept = Concept.create(module, name, _abstract, propertyList, locationOf(ctx));
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_ASSOCIATION);
        }

        final String name = ctx.NAME().getText();
        final List<AssociationEnd> associationEnds = seq(ctx.associationEndDeclaration() == null ? empty() : ctx.associationEndDeclaration())
            .map(node -> node.associationEnd)
            .toList();

        ctx.association = Association.create(module, name, associationEnds, locationOf(ctx));
    }

    @Override
    public void exitAssociationEndDeclaration(AssociationEndDeclarationContext ctx)
    {
        if (ctx.conceptName == null)
        {
            throw new ModelSynthesisException(NO_CONCEPT_NAME_PROVIDED_FOR_ASSOCIATION_END);
        }

        if (ctx.propertyName == null)
        {
            throw new ModelSynthesisException(NO_PROPERTY_NAME_PROVIDED_FOR_ASSOCIATION_END);
        }

        final String conceptName = ctx.conceptName.getText();
        final String propertyName = ctx.propertyName.getText();
        final @Nullable Type type = (ctx.typeDeclaration() == null) ? null : ctx.typeDeclaration().type;

        ctx.associationEnd = AssociationEnd.create(conceptName, propertyName, type, locationOf(ctx));
    }

    @Override
    public void exitTaskDeclaration(TaskDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_TARGET);
        }

        final String name = ctx.NAME().getText();
        final String constructor = ctx.constructorDeclaration() == null ? null : ctx.constructorDeclaration().NAME().getText();
        final List<Property> propertyList = seq(ctx.propertyList() == null ? empty() : ctx.propertyList().propertyDeclaration())
            .map(node -> node.property)
            .toList();

        ctx.task = Task.create(module, name, constructor, propertyList, locationOf(ctx));
    }

    @Override
    public void exitTemplateDeclaration(final TemplateDeclarationContext ctx)
    {
        ctx.template = new Template(module, ctx.functionDeclaration().function);
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

        ctx.property = Property.create(name, type, value, ctx.DERIVED() != null, locationOf(ctx));
    }

    @Override
    public void exitTypeDeclaration(TypeDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            if (ctx.params != null) ctx.type = new FunctionType(ctx.params.type, ctx.result.type);
            else if (ctx.tuple != null) ctx.type = ctx.tuple.type;
            else if (ctx.inner != null) ctx.type = ctx.inner.type;
        }
        else
        {
            final String name = ctx.NAME().getText();
            final String cardinality = (ctx.cardinality() == null) ? null : ctx.cardinality().getText();

            ctx.type = NamedType.create(name, cardinality);
        }
    }

    @Override
    public void exitTupleTypeDeclaration(final TupleTypeDeclarationContext ctx)
    {
        final Seq<TupleTypeElement> elements = seq(ctx.tupleTypeElementDeclaration()).map(c -> c.element);
        final String cardinality = (ctx.cardinality() == null) ? null : ctx.cardinality().getText();

        ctx.type = new TupleType(elements, cardinality);
    }

    @Override
    public void exitTupleTypeElementDeclaration(final TupleTypeElementDeclarationContext ctx)
    {
        final Type type = ctx.type.type;
        final Optional<String> name = ofNullable(ctx.name).map(Token::getText);

        ctx.element = new TupleTypeElement(type, name.orElse(null));
    }

    @Override
    public void exitTypeParameterList(final TypeParameterListContext ctx)
    {
        ctx.params = seq(ctx.typeParameter()).map(c -> c.param);
    }

    @Override
    public void exitFunctionDeclaration(final FunctionDeclarationContext ctx)
    {
        final String name = ctx.name.getText();
        final Type type = ctx.resultType.type;
        final Stream<FunctionParameter> params = ctx.functionParameterList().params;

        ctx.function = new Function(name, type, params);
    }

    @Override
    public void exitFunctionParameterList(final FunctionParameterListContext ctx)
    {
        ctx.params = seq(ctx.functionParameterDeclaration()).map(c -> c.param);
    }

    @Override
    public void exitFunctionParameterDeclaration(final FunctionParameterDeclarationContext ctx)
    {
        final String name = ctx.name.getText();

        ctx.param = new FunctionParameter(name, ctx.type.type);
    }

    @Override
    public void exitTypeParameter(final TypeParameterContext ctx)
    {
        final String name = ctx.name.getText();

        ctx.param = new TypeParameter(name);
    }

    @Override
    public void exitExpression(ExpressionContext ctx)
    {
        if (ctx.literalExpression() != null) ctx.expr = ctx.literalExpression().literal;
        else if (ctx.pathExpression() != null) ctx.expr = ctx.pathExpression().path;
        else if (ctx.conditionalExpression() != null) ctx.expr = ctx.conditionalExpression().conditional;
        else if (ctx.lambdaExpression() != null) ctx.expr = ctx.lambdaExpression().lambda;
        else if (ctx.invocationExpression() != null) ctx.expr = ctx.invocationExpression().invocation;
        else if (ctx.comprehensionExpression() != null) ctx.expr = invocationOf(ctx.comprehensionExpression().comprehension);
        else if (ctx.operator != null && ctx.type != null) ctx.expr = createTypeExpression(ctx);
        else if (ctx.operator != null && ctx.expression().size() == 1) ctx.expr = createUnary(ctx);
        else if (ctx.operator != null && ctx.expression().size() == 2) ctx.expr = createInfix(ctx);
        else if (ctx.inner != null) ctx.expr = ctx.inner.expr;

        createLocation(ctx, ctx.expr);
    }

    private Expression createTypeExpression(final ExpressionContext ctx)
    {
        assert ctx.expression().size() == 1;

        final Expression expr = ctx.expression().get(0).expr;
        final String operator = ctx.operator.getText().replace('?', 'q').replace('!', 'b');

        if (operator.equals(TypeCast.ASB) || operator.equals(TypeCast.ASQ))
        {
            final Type castType = ctx.type.type;
            return new TypeCast(expr, operator, castType);
        }
        else
        {
            final Type checkedType = ctx.type.type;
            return new TypeCheck(expr, operator, checkedType);
        }
    }

    private Unary createUnary(ExpressionContext ctx)
    {
        final String operator = ctx.operator.getText();
        final Expression expr = ctx.expression().get(0).expr;

        return new Unary(operator, expr);
    }

    private Infix createInfix(ExpressionContext ctx)
    {
        final String operator = ctx.operator.getText();
        final Expression left = ctx.expression().get(0).expr;
        final Expression right = ctx.expression().get(1).expr;

        return new Infix(operator, left, right);
    }

    @Override
    public void exitConditionalExpression(final ConditionalExpressionContext ctx)
    {
        final Expression cond = ctx.cond.expr;
        final Expression then = ctx.then.expr;
        final Expression else_ = ctx.else_.expr;

        ctx.conditional = new Conditional(cond, then, else_);
    }

    @Override
    public void exitPathExpression(PathExpressionContext ctx)
    {
        final List<String> pathNames = ctx.NAME()
                                          .stream()
                                          .map(ParseTree::getText)
                                          .collect(toList());

        ctx.path = Path.create(pathNames);
    }

    @Override
    public void exitLiteralExpression(LiteralExpressionContext ctx)
    {
        final String text = getText(ctx);

        if (text != null)
        {
            final NamedType type = NamedType.create(getPrimitiveTypeName(ctx), null);
            ctx.literal = new Literal(text, type);
        }
    }

    @Override
    public void exitLambdaExpression(final LambdaExpressionContext ctx)
    {
        final Seq<String> parameters = ctx.lambdaParameterList() == null ? empty() : ctx.lambdaParameterList().params;
        final Expression expr = ctx.expression().expr;

        ctx.lambda = new Lambda(parameters, expr);
    }

    @Override
    public void exitLambdaParameterList(final LambdaParameterListContext ctx)
    {
        ctx.params = seq(ctx.NAME()).map(ParseTree::getText);
    }

    @Override
    public void exitInvocationExpression(InvocationExpressionContext ctx)
    {
        final String name = ctx.NAME().getText();
        final Optional<Lambda> lambda = ofNullable(ctx.lambdaExpression()).map(c -> c.lambda);
        final List<Expression> arguments = expressionsOf(ctx).concat(seq(lambda)).toList();

        ctx.invocation = Invocation.create(name, arguments);
    }

    private Seq<Expression> expressionsOf(final InvocationExpressionContext ctx)
    {
        final AtomicInteger index = new AtomicInteger(2);
        return seq(ctx.expression()).map(e -> {
            final int i = index.incrementAndGet();
            return e.expr == null ? new InvalidExpression(ctx.getChild(i).getText()) : e.expr;
        });
    }

    @Override
    public void exitComprehensionExpression(ComprehensionExpressionContext ctx)
    {
        final Path path = ctx.pathExpression() == null ? null : ctx.pathExpression().path;
        final Stream<Enumerator> enumerators = ctx.enumeratorDeclaration()
                                                .stream()
                                                .map(e -> e.enumerator);
        final Stream<Query> queries = ctx.queryStatement()
                                       .stream().map(q -> q.query);

        if (path == null)
        {
            ctx.comprehension = new Comprehension(seq(enumerators), seq(queries));
        }
        else
        {
            ctx.comprehension = new Comprehension(path, seq(queries));
        }
    }

    @Override
    public void exitEnumeratorDeclaration(EnumeratorDeclarationContext ctx)
    {
        final String variable = ctx.var.getText();
        final Path path = ctx.pathExpression().path;

        ctx.enumerator = new Enumerator(variable, path);
    }

    @Override
    public void exitQueryStatement(final QueryStatementContext ctx)
    {
        if (ctx.NAME() == null)
        {
            final Stream<Keyword> keywords = ctx.keywordExpression()
                                                .stream()
                                                .map(k -> k.keyword);

            ctx.query = new Query(keywords);
        }
        else
        {
            final String name = ctx.NAME().getText();

            ctx.query = new Query(name);
        }
    }

    @Override
    public void exitKeywordExpression(final KeywordExpressionContext ctx)
    {
        final String name = ctx.NAME().getText();
        final Seq<String> parameters = ctx.lambdaParameterList() == null ? empty() : ctx.lambdaParameterList().params;
        final Expression expression = ctx.expression().expr;

        ctx.keyword = new Keyword(name, parameters, expression);
    }

    private static String getText(LiteralExpressionContext ctx)
    {
        if (ctx.BOOLEAN() != null) return ctx.BOOLEAN().getText();
        else if (ctx.STRING() != null) return unwrap(ctx.STRING().getText());
        else if (ctx.INTEGER() != null) return ctx.INTEGER().getText();
        else if (ctx.DECIMAL() != null) return ctx.DECIMAL().getText();
        else if (ctx.LONG() != null) return extractSuffix(ctx.LONG().getText());
        else if (ctx.SHORT() != null) return extractSuffix(ctx.SHORT().getText());
        else if (ctx.BYTE() != null) return extractSuffix(ctx.BYTE().getText());
        else if (ctx.FLOAT() != null) return extractSuffix(ctx.FLOAT().getText());
        else if (ctx.DOUBLE() != null) return extractSuffix(ctx.DOUBLE().getText());
        else return null;
    }

    private static String extractSuffix(String text)
    {
        return text.substring(0, text.length() - 1);
    }

    private static String getPrimitiveTypeName(LiteralExpressionContext ctx)
    {
        if (ctx.BOOLEAN() != null) return "BOOLEAN";
        else if (ctx.STRING() != null) return "STRING";
        else if (ctx.INTEGER() != null) return "INTEGER";
        else if (ctx.LONG() != null) return "LONG";
        else if (ctx.SHORT() != null) return "SHORT";
        else if (ctx.BYTE() != null) return "BYTE";
        else if (ctx.DECIMAL() != null) return "DECIMAL";
        else if (ctx.FLOAT() != null) return "FLOAT";
        else if (ctx.DOUBLE() != null) return "DOUBLE";
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

    private Location locationOf(ParserRuleContext ctx)
    {
        return createLocation(ctx, null);
    }

    private Location createLocation(ParserRuleContext ctx, ModelElement element)
    {
        final Token token = ctx.getStart();

        return Location.createLocation(token.getLine(), token.getCharPositionInLine() + 1, element);
    }
}
