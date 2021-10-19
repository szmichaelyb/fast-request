package io.github.kings1990.plugin.fastrequest.view.component;

import com.intellij.lang.Language;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.ui.LanguageTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyLanguageTextField extends LanguageTextField {

    private Project myProject;
    private FileType fileType;
    private Language language;

    public MyLanguageTextField(Project myProject, Language language, FileType fileType) {
        super(language, myProject, "", false);
        this.myProject = myProject;
        this.fileType = fileType;
        this.language = language;
    }

    @Override
    protected @NotNull EditorEx createEditor() {
        EditorEx editor = super.createEditor();
        setUpEditor(editor);
        return editor;
    }

    @Override
    public void setText(@Nullable String text) {
        super.setFileType(fileType);
        Document document = createDocument(text, language, myProject, new SimpleDocumentCreator());
        setDocument(document);
        PsiFile psiFile = PsiDocumentManager.getInstance(getProject()).getPsiFile(document);
        if (psiFile != null) {
            WriteCommandAction.runWriteCommandAction(
                    getProject(),
                    () -> {
                        CodeStyleManager.getInstance(getProject()).reformat(psiFile);
                    }
            );
        }
    }

    private EditorEx setUpEditor(EditorEx editor) {
        editor.offsetToVisualPosition(100);
        editor.offsetToLogicalPosition(100);
        editor.setHorizontalScrollbarVisible(true);
        editor.setShowPlaceholderWhenFocused(true);
        editor.setVerticalScrollbarVisible(true);
        editor.setCaretEnabled(true);
        editor.setEmbeddedIntoDialogWrapper(true);
        EditorSettings settings = editor.getSettings();
        settings.setLeadingWhitespaceShown(true);
        settings.setTrailingWhitespaceShown(true);
        settings.setGutterIconsShown(true);
        settings.setSmartHome(true);
        settings.setLineNumbersShown(true);
        settings.setIndentGuidesShown(true);
        settings.setUseSoftWraps(true);
        settings.setAutoCodeFoldingEnabled(true);
        settings.setFoldingOutlineShown(true);
        settings.setAllowSingleLogicalLineFolding(true);
        settings.setRightMarginShown(true);
        settings.setCaretRowShown(true);
        settings.setLineMarkerAreaShown(true);
        settings.setDndEnabled(true);
        return editor;
    }

    public void setMyProject(Project myProject) {
        this.myProject = myProject;
    }

    @Override
    public void setFileType(@NotNull FileType fileType) {
        this.fileType = fileType;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
