package calcplugin.psi;

import calcplugin.CalcFileType;
import calcplugin.CalcIcons;
import calcplugin.CalcLanguage;
import calcplugin.psi.CallSubtree;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import org.antlr.intellij.adaptor.SymtabUtils;
import org.antlr.intellij.adaptor.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CalcPSIFileRoot extends PsiFileBase implements ScopeNode {
    public CalcPSIFileRoot(FileViewProvider viewProvider) {
        super(viewProvider, CalcLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return CalcFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Sample Language file";
    }

    @Override
    public Icon getIcon(int flags) {
        return CalcIcons.FILE;
    }

    /** Return null since a file scope has no enclosing scope. It is
     *  not itself in a scope.
     */
    @Override
    public ScopeNode getContext() {
        return null;
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
//		System.out.println(getClass().getSimpleName()+
//		                   ".resolve("+element.getName()+
//		                   " at "+Integer.toHexString(element.hashCode())+")");
        if ( element.getParent() instanceof CallSubtree) {
            return SymtabUtils.resolve(this, CalcLanguage.INSTANCE,
                    element, "/script/function/ID");
        }
        return SymtabUtils.resolve(this, CalcLanguage.INSTANCE,
                element, "/script/vardef/ID");
    }
}
