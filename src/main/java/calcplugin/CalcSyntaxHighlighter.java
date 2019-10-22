package calcplugin;

import calc.CalcLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class CalcSyntaxHighlighter implements SyntaxHighlighter {
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("CALC_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

    static final TextAttributesKey KEYWORD =
            createTextAttributesKey("CALC_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

    private static final TextAttributesKey NUMBER =
            createTextAttributesKey("CALC_NUMBER", DefaultLanguageHighlighterColors.CONSTANT);

    static final TextAttributesKey STRING_LITERAL =
            createTextAttributesKey("CALC_STRING_LITERAL", DefaultLanguageHighlighterColors.STRING);

    static final TextAttributesKey PARENTHESES =
            createTextAttributesKey("CALC_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        final CalcLexer lexer = new CalcLexer(null);
        return new ANTLRLexerAdaptor(CalcLanguage.INSTANCE, lexer);
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (!(tokenType instanceof TokenIElementType)) return EMPTY_KEYS;
        TokenIElementType myType = (TokenIElementType) tokenType;
        int ttype = myType.getANTLRTokenType();
        TextAttributesKey attrKey;

        switch (ttype) {
            case CalcLexer.IDENTIFIER:
                attrKey = IDENTIFIER;
                break;

            case CalcLexer.IF:
                attrKey = KEYWORD;
                break;

            case CalcLexer.NUMBER:
                attrKey = NUMBER;
                break;

            case CalcLexer.SingleQuoteString:
            case CalcLexer.DoubleQuoteString:
                attrKey = STRING_LITERAL;
                break;

            case CalcLexer.OPEN_PAREN:
            case CalcLexer.CLOSE_PAREN:
                attrKey = PARENTHESES;
                break;

            default:
                return EMPTY_KEYS;
        }
        return new TextAttributesKey[]{attrKey};
    }
}
