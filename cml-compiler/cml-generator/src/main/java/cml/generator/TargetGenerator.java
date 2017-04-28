package cml.generator;

import cml.io.Console;
import cml.language.ModelVisitor;
import cml.language.features.Concept;
import cml.language.features.Task;
import cml.language.Model;

import java.util.HashMap;
import java.util.Map;

class TargetGenerator implements ModelVisitor.Delegate
{
    private static final String MODEL = "model";
    private static final String CONCEPT = "concept";
    private static final String TASK = "task";

    private final Console console;
    private final TargetFileRenderer targetFileRenderer;
    private final Task task;
    private final String targetDirPath;
    private final Map<String, Object> baseTemplateArgs;
    
    TargetGenerator(
        Console console,
        TargetFileRenderer targetFileRenderer,
        Task task,
        String targetDirPath)
    {
        this.console = console;
        this.targetFileRenderer = targetFileRenderer;
        this.task = task;
        this.targetDirPath = targetDirPath;
        this.baseTemplateArgs = new HashMap<>();

        baseTemplateArgs.put(TASK, getTargetProperties(task));
    }

    @Override
    public void visit(Model model)
    {
        console.println("%s files:", MODEL);

        generateTargetFiles(MODEL, model);
    }

    @Override
    public void visit(Concept concept)
    {
        console.println("\n%s files:", concept.getName());

        generateTargetFiles(CONCEPT, concept);
    }

    private void generateTargetFiles(String elementType, Object modelElement)
    {
        final Map<String, Object> templateArgs = new HashMap<>(baseTemplateArgs);
        templateArgs.put(elementType, modelElement);

        targetFileRenderer.renderTargetFiles(task, targetDirPath, elementType, templateArgs);
    }

    private static Map<String, Object> getTargetProperties(final Task task)
    {
        final Map<String, Object> properties = new HashMap<>();

        task.getProperties()
            .stream()
            .filter(property -> property.getValue().isPresent())
            .forEach(property -> properties.put(property.getName(), property.getValue().get()));

        return properties;
    }
}
