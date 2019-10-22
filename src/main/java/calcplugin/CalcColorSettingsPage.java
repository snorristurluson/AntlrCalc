package calcplugin;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class CalcColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Identifier", CalcSyntaxHighlighter.IDENTIFIER),
            new AttributesDescriptor("Keyword", CalcSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("String", CalcSyntaxHighlighter.STRING_LITERAL),
            new AttributesDescriptor("Parentheses", CalcSyntaxHighlighter.PARENTHESES),
    };

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return CalcIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new CalcSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return
                "if(a>func(12.3), 'true', 'false')";
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Calc";
    }
}