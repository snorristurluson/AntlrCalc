package calcplugin;

import calc.CalcLexer;
import calc.CalcParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;

public class CalcParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE =
            new IFileElementType(CalcLanguage.INSTANCE);

    public static final TokenSet COMMENTS =
            PSIElementTypeFactory.createTokenSet(
                    CalcLanguage.INSTANCE);

    public static final TokenSet WHITESPACE =
            PSIElementTypeFactory.createTokenSet(
                    CalcLanguage.INSTANCE,
                    CalcLexer.WS);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        final CalcLexer calcLexer = new CalcLexer(null);
        return new ANTLRLexerAdaptor(CalcLanguage.INSTANCE, calcLexer);
    }

    @Override
    public PsiParser createParser(Project project) {
        final CalcParser parser = new CalcParser(null);
        return new ANTLRParserAdaptor(CalcLanguage.INSTANCE, parser) {
            @Override
            protected ParseTree parse(Parser parser, IElementType root) {
                return ((CalcParser) parser).expression();
            }
        };
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITESPACE;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return null;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return null;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return null;
    }
}
