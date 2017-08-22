package cml.generator;

import cml.language.features.Association;
import cml.language.features.Concept;
import cml.language.features.Task;
import cml.language.foundation.Model;
import cml.language.foundation.NamedElement;
import cml.language.loader.ModelVisitor;

class TargetGenerator implements ModelVisitor
{
    private static final String MODEL = "model";
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
    public void visit(Model model)
    {
        generateTargetFiles(MODEL, model);
    }

    @Override
    public void visit(Concept concept)
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
