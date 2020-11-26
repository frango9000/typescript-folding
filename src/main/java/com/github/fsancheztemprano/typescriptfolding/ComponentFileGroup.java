package com.github.fsancheztemprano.typescriptfolding;

import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComponentFileGroup extends ProjectViewNode<PsiFile> {
    private final String name;
    private final String iconType;
    private final List<AbstractTreeNode<?>> children;

    protected ComponentFileGroup(Project project,
                                 ViewSettings viewSettings,
                                 PsiFile baseFile,
                                 String name,
                                 String iconType) {
        super(project, baseFile, viewSettings);
        this.name = name;
        this.iconType = iconType;
        this.children = new ArrayList<>();
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        for (AbstractTreeNode<?> childNode : children) {
            ProjectViewNode<?> treeNode = (ProjectViewNode<?>) childNode;
            if (treeNode.contains(file)) {
                return true;
            }
        }
        return false;
    }

    public void addChild(AbstractTreeNode<?> node, String extension) {
        if (node instanceof PsiFileNode) {
            PsiFileNode n  = (PsiFileNode) node;
            children.add(new NamedFileNode(n.getProject(), n.getValue(), n.getSettings(), "." + extension, n));
        }
    }

    @NotNull
    @Override
    public List<AbstractTreeNode<?>> getChildren() {
        return children;
    }

    AbstractTreeNode<?> getOriginalFirstChild() {
        if (children.size() == 0)
            return null;
        AbstractTreeNode<?> first = children.get(0);
        if (first instanceof NamedFileNode) {
            return ((NamedFileNode)first).original;
        }
        return first;
    }

    public int getChildrenCount() {
        return children.size();
    }

    @Override
    protected void update(PresentationData presentation) {
        presentation.setPresentableText(name);
        if (iconType.equals("service"))
            presentation.setIcon(AllIcons.Nodes.Static);
        else
            presentation.setIcon(AllIcons.Nodes.Class);
    }

    static class NamedFileNode extends PsiFileNode {
        private final String name;
        private final AbstractTreeNode<?> original;

        public NamedFileNode(Project project, PsiFile psiFile, ViewSettings viewSettings, String name, AbstractTreeNode<?> original) {
            super(project, psiFile, viewSettings);
            this.name = name;
            this.original = original;
        }

        @Override
        public void update(@NotNull PresentationData presentationData) {
            super.update(presentationData);
            presentationData.setPresentableText(this.name);
        }
    }
}
