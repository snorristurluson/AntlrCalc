package calcplugin;

import com.intellij.lang.Language;

public class CalcLanguage extends Language {
    public static final CalcLanguage INSTANCE = new CalcLanguage();
    private CalcLanguage() {
        super("Calc");
    }
}
