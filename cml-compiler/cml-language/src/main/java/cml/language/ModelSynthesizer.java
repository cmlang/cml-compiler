package cml.language;

import cml.language.expressions.*;
import cml.language.features.*;
import cml.language.foundation.Location;
import cml.language.foundation.Property;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser.*;
import cml.language.types.NamedType;
import cml.language.types.Type;
import cml.language.types.TypeParameter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

import static cml.language.transforms.InvocationTransforms.invocationOf;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;

class ModelSynthesizer extends CMLBaseListener
{
    private static final String QUOTE = "\"";
    private static final String INVALID_MODULE_NAME = "Module declaration name (%s) should match the module's directory name: %s";
    private static final String NO_NAME_PROVIDED_FOR_CONCEPT = "No name provided for concept.";
    private static final String NO_NAME_PROVIDED_FOR_ASSOCIATION = "No name provided for association.";
    private static final String NO_NAME_PROVIDED_FOR_PARAMETER = "No name provided for parameter.";
    private static final String NO_NAME_PROVIDED_FOR_PROPERTY = "No name provided for property.";
    private static final String NO_NAME_PROVIDED_FOR_TARGET = "No name provided for task.";
    private static final String NO_NAME_PROVIDED_FOR_MACRO = "No name provided for macro.";
    private static final String NO_MACRO_PROVIDED_FOR_INVOCATION = "No macro provided for invocation.";
    private static final String NO_CONCEPT_NAME_PROVIDED_FOR_ASSOCIATION_END = "No concept name provided for association end.";
    private static final String NO_PROPERTY_NAME_PROVIDED_FOR_ASSOCIATION_END = "No property name provided for association end.";
    private static final String NO_VARIABLE_NAME_PROVIDED_FOR_ASSIGNMENT = "No variable name provided for assignment.";

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
               .filter(node -> node.associationDeclaration() != null)
               .forEach(node -> module.addMember(node.associationDeclaration().association));

            ctx.declarations()
               .stream()
               .filter(node -> node.taskDeclaration() != null)
               .forEach(node -> module.addMember(node.taskDeclaration().task));

            ctx.declarations()
               .stream()
               .filter(node -> node.templateDeclaration() != null)
               .forEach(node -> module.addMember(node.templateDeclaration().template));
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

        ctx.concept.setLocation(locationOf(ctx));
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_ASSOCIATION);
        }

        final String name = ctx.NAME().getText();

        ctx.association = Association.create(name);

        if (ctx.associationEndDeclaration() != null)
        {
            ctx.associationEndDeclaration()
               .forEach(node -> ctx.association.addMember(node.associationEnd));
        }

        ctx.association.setLocation(locationOf(ctx));
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
        final AssociationEnd associationEnd = AssociationEnd.create(conceptName, propertyName, type);

        associationEnd.setLocation(locationOf(ctx));

        ctx.associationEnd = associationEnd;
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
        final Property property = Property.create(name, type, value, ctx.DERIVED() != null);

        property.addMember(value);

        property.setLocation(locationOf(ctx));

        ctx.property = property;
    }

    @Override
    public void exitTypeDeclaration(TypeDeclarationContext ctx)
    {
        if (ctx.NAME() != null)
        {
            final String name = ctx.NAME().getText();
            final String cardinality = (ctx.cardinality() == null) ? null : ctx.cardinality().getText();

            ctx.type = NamedType.create(name, cardinality);
        }
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
        else if (ctx.invocationExpression() != null) ctx.expr = ctx.invocationExpression().invocation;
        else if (ctx.comprehensionExpression() != null) ctx.expr = invocationOf(ctx.comprehensionExpression().comprehension);
        else if (ctx.operator != null && ctx.expression().size() == 1) ctx.expr = createUnary(ctx);
        else if (ctx.operator != null && ctx.expression().size() == 2) ctx.expr = createInfix(ctx);
        else if (ctx.assignmentExpression() != null) ctx.expr = ctx.assignmentExpression().assignment;
        else if (ctx.inner != null) ctx.expr = ctx.inner.expr;
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

    @Override
    public void exitAssignmentExpression(final AssignmentExpressionContext ctx)
    {
        if (ctx.variable == null)
        {
            throw new ModelSynthesisException(NO_VARIABLE_NAME_PROVIDED_FOR_ASSIGNMENT);
        }

        final String variable = ctx.variable.getText();
        final Expression value = ctx.value.expr;

        ctx.assignment = Assignment.create(variable, value);
    }

    @Override
    public void exitConditionalExpression(final ConditionalExpressionContext ctx)
    {
        final Expression cond = ctx.cond.expr;
        final Expression then = ctx.then.expr;
        final Expression else_ = ctx.else_.expr;
        final Conditional conditional = Conditional.create(cond, then, else_);

        conditional.addMember(cond);
        conditional.addMember(then);
        conditional.addMember(else_);

        ctx.conditional = conditional;
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
                         .collect(toList());
    }

    @Override
    public void exitLiteralExpression(LiteralExpressionContext ctx)
    {
        final String text = getText(ctx);

        if (text != null)
        {
            final NamedType type = NamedType.create(getPrimitiveTypeName(ctx), null);
            ctx.literal = Literal.create(text, type);
        }
    }

    @Override
    public void exitInvocationExpression(InvocationExpressionContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_MACRO_PROVIDED_FOR_INVOCATION);
        }

        final String name = ctx.NAME().getText();
        final List<Expression> arguments = ctx.expression()
                                              .stream()
                                              .map(e -> e.expr)
                                              .collect(toList());

        ctx.invocation = Invocation.create(name, arguments);
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
        final Expression expression = ctx.expression().expr;

        ctx.keyword = new Keyword(name, expression);
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
        if (ctx.BOOLEAN() != null) return "Boolean";
        else if (ctx.STRING() != null) return "String";
        else if (ctx.INTEGER() != null) return "Integer";
        else if (ctx.LONG() != null) return "Long";
        else if (ctx.SHORT() != null) return "Short";
        else if (ctx.BYTE() != null) return "Byte";
        else if (ctx.DECIMAL() != null) return "Decimal";
        else if (ctx.FLOAT() != null) return "Float";
        else if (ctx.DOUBLE() != null) return "Double";
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
        final Token token = ctx.getStart();

        return Location.createLocation(token.getLine(), token.getCharPositionInLine() + 1);
    }
}
