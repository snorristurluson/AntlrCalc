package calcplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CalcFileType extends LanguageFileType {
    public static final CalcFileType INSTANCE = new CalcFileType();
    private CalcFileType() {
        super(CalcLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Calc file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Calc language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "calc";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return CalcIcons.FILE;
    }
}
