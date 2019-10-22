package calcplugin;

import calc.CalcLexer;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CalcBraceMatcher implements PairedBraceMatcher {
    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }

    @NotNull
    @Override
    public BracePair[] getPairs() {
        BracePair[] bracePairs = new BracePair[1];
        TokenIElementType open = new TokenIElementType(CalcLexer.OPEN_PAREN, "(", CalcLanguage.INSTANCE);
        TokenIElementType close = new TokenIElementType(CalcLexer.CLOSE_PAREN, ")", CalcLanguage.INSTANCE);
        bracePairs[0] = new BracePair(open, close, false);
        return bracePairs;
    }
}
