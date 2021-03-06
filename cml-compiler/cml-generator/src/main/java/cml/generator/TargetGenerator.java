package cml.generator;

import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.foundation.TempModel;
import cml.language.generated.Association;
import cml.language.generated.NamedElement;
import cml.language.generated.Task;
import cml.language.loader.ModelVisitor;

class TargetGenerator implements ModelVisitor
{
    private static final String MODEL = "model";
    private static final String MODULE = "module";
    private static final String CONCEPT = "concept";
    private static final String ASSOCIATION = "association";

    private final TargetFileRenderer targetFileRenderer;
    private final Task task;
    private final String targetDirPath;

    TargetGenerator(
        TargetFileRenderer targetFileRenderer,
        Task task,
        String targetDirPath)
    {
        this.targetFileRenderer = targetFileRenderer;
        this.task = task;
        this.targetDirPath = targetDirPath;
    }

    @Override
    public void visit(TempModel model)
    {
        generateTargetFiles(MODEL, model);
    }

    @Override
    public void visit(final TempModule module)
    {
        generateTargetFiles(MODULE, module);
    }

    @Override
    public void visit(TempConcept concept)
    {
        generateTargetFiles(CONCEPT, concept);
    }

    @Override
    public void visit(Association association)
    {
        generateTargetFiles(ASSOCIATION, association);
    }

    private void generateTargetFiles(String namedElementType, NamedElement namedElement)
    {
        targetFileRenderer.renderTargetFiles(task, targetDirPath, namedElementType, namedElement);
    }
}
