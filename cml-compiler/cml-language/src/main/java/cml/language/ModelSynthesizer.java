package cml.language;

import cml.language.expressions.*;
import cml.language.features.*;
import cml.language.foundation.Location;
import cml.language.foundation.Parameter;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import cml.language.grammar.CMLBaseListener;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

class ModelSynthesizer extends CMLBaseListener
{
    private static final String PIPE_OPERATOR = "|";
    private static final String COMMA_OPERATOR = ",";
    private static final String QUOTE = "\"";
    private static final String INVALID_MODULE_NAME = "Module declaration name (%s) should match the module's directory name: %s";
    private static final String NO_NAME_PROVIDED_FOR_CONCEPT = "No name provided for concept.";
    private static final String NO_NAME_PROVIDED_FOR_ASSOCIATION = "No name provided for association.";
    private static final String NO_NAME_PROVIDED_FOR_TYPE = "No name provided for type.";
    private static final String NO_NAME_PROVIDED_FOR_PARAMETER = "No name provided for parameter.";
    private static final String NO_NAME_PROVIDED_FOR_PROPERTY = "No name provided for property.";
    private static final String NO_NAME_PROVIDED_FOR_TARGET = "No name provided for task.";
    private static final String NO_NAME_PROVIDED_FOR_MACRO = "No name provided for macro.";
    private static final String NO_MACRO_PROVIDED_FOR_INVOCATION = "No macro provided for invocation.";
    private static final String NO_NAMED_EXPRESSIONS_FOR_PIPE = "No named expressions provided for pipe expression:";
    private static final String NO_CONCEPT_NAME_PROVIDED_FOR_ASSOCIATION_END = "No concept name provided for association end.";
    private static final String NO_PROPERTY_NAME_PROVIDED_FOR_ASSOCIATION_END = "No property name provided for association end.";
    private static final String NO_VARIABLE_NAME_PROVIDED_FOR_ASSIGNMENT = "No variable name provided for assignment.";
    private static final String ONLY_SINGLE_NAMED_PATH_ALLOWED_FOR_PIPE = "Only single-named paths are allowed after pipes";

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
               .filter(node -> node.macroDeclaration() != null)
               .forEach(node -> module.addMember(node.macroDeclaration().macro));
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
    public void exitMacroDeclaration(MacroDeclarationContext ctx)
    {
        if (ctx.NAME() == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_MACRO);
        }

        final String name = ctx.NAME().getText();
        final Type type = (ctx.typeDeclaration() == null) ? null : ctx.typeDeclaration().type;

        ctx.macro = Macro.create(name, type);

        if (ctx.macroParameterList() != null)
        {
            ctx.macroParameterList()
               .macroParameterDeclaration()
               .forEach(node -> ctx.macro.addMember(node.parameter));
        }
    }

    @Override
    public void exitMacroParameterDeclaration(MacroParameterDeclarationContext ctx)
    {
        if (ctx.name == null)
        {
            throw new ModelSynthesisException(NO_NAME_PROVIDED_FOR_PARAMETER);
        }

        final String name = ctx.name.getText();
        final Type type = (ctx.typeDeclaration() == null) ? null : ctx.typeDeclaration().type;
        final String scopeName = (ctx.scope == null) ? null : ctx.scope.getText();
        final Parameter parameter = Parameter.create(name, type, scopeName);

        parameter.setLocation(locationOf(ctx));

        ctx.parameter = parameter;
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
        else if (ctx.conditionalExpression() != null) ctx.expr = ctx.conditionalExpression().conditional;
        else if (ctx.invocationExpression() != null) ctx.expr = ctx.invocationExpression().invocation;
        else if (ctx.comprehensionExpression() != null) ctx.expr = ctx.comprehensionExpression().comprehension;
        else if (ctx.operator != null && ctx.expression().size() == 1) ctx.expr = createUnary(ctx);
        else if (ctx.operator != null && ctx.expression().size() == 2) ctx.expr = createInfixOrInvocation(ctx);
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

    private Expression createInfixOrInvocation(ExpressionContext ctx)
    {
        if (ctx.operator.getText().equals(COMMA_OPERATOR)) return null;
        else if (ctx.operator.getText().equals(PIPE_OPERATOR)) return createInvocationFromPipeInfix(ctx);
        else return createInfix(ctx);
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

    private Expression createInvocationFromPipeInfix(ExpressionContext ctx)
    {
        if (ctx.expression(1).pathExpression() == null) return createInvocationFromKeywords(ctx);
        else return createInvocationFromPath(ctx);
    }

    @NotNull
    private Expression createInvocationFromKeywords(ExpressionContext ctx)
    {
        final ExpressionContext command = getFirstKeyword(ctx.expression(1));

        if (command == null)
        {
            throw new ModelSynthesisException(NO_NAMED_EXPRESSIONS_FOR_PIPE + ctx.getText());
        }

        final String name = command.keyword.getText();
        final Expression expr = command.arg.expr;
        final Expression seq = ctx.expression(0).expr;

        if (expr == null)
        {
            throw new ModelSynthesisException(NO_NAMED_EXPRESSIONS_FOR_PIPE + ctx.getText());
        }

        if (seq == null)
        {
            throw new ModelSynthesisException(NO_NAMED_EXPRESSIONS_FOR_PIPE + ctx.getText());
        }

        final LinkedHashMap<String, Expression> namedArguments = new LinkedHashMap<>();
        namedArguments.put("seq", seq);
        namedArguments.put("expr", expr);

        final ExpressionContext keywordList = getKeywordList(ctx.expression(1));

        for (int i = 1; i < keywordList.expression().size(); i++) {
            namedArguments.put(keywordList.expression(i).keyword.getText(), keywordList.expression(i).arg.expr);
        }

        return Invocation.create(name, namedArguments);
    }

    private Expression createInvocationFromPath(ExpressionContext ctx)
    {
        final Path path = ctx.expression(1).pathExpression().path;

        if (path.getNames().size() > 1) {
            throw new ModelSynthesisException(ONLY_SINGLE_NAMED_PATH_ALLOWED_FOR_PIPE);
        }

        final String name = path.getNames().get(0);
        final Expression seq = ctx.expression(0).expr;

        final LinkedHashMap<String, Expression> namedArguments = new LinkedHashMap<>();
        namedArguments.put("seq", seq);

        return Invocation.create(name, namedArguments);
    }

    private ExpressionContext getFirstKeyword(ExpressionContext ctx)
    {
        while (ctx.keyword == null && (ctx.inner != null || ctx.operator != null)) {
            ctx = ctx.expression(0);
        }

        return ctx;
    }

    private ExpressionContext getKeywordList(ExpressionContext ctx)
    {
        while (ctx.keyword == null && ctx.inner != null) {
            ctx = ctx.expression(0);
        }

        return ctx;
    }

    @Override
    public void exitComprehensionExpression(ComprehensionExpressionContext ctx)
    {
        final List<Enumerator> enumerators = ctx.enumeratorDeclaration()
                                                .stream()
                                                .map(e -> e.enumerator)
                                                .collect(toList());
        final Expression expression = ctx.expression().expr;

        ctx.comprehension = new Comprehension(enumerators, expression);
    }

    @Override
    public void exitEnumeratorDeclaration(EnumeratorDeclarationContext ctx)
    {
        final String variable = ctx.var.getText();
        final Path path = ctx.pathExpression().path;

        ctx.enumerator = new Enumerator(variable, path);
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
            final Type type = Type.create(getPrimitiveTypeName(ctx), null);
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
