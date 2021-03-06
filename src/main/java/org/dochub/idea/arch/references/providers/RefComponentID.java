package org.dochub.idea.arch.references.providers;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import org.dochub.idea.arch.completions.providers.Components;
import org.dochub.idea.arch.completions.providers.Contexts;
import org.dochub.idea.arch.utils.PsiUtils;
import org.jetbrains.yaml.psi.*;

public class RefComponentID extends RefBaseID {
    static private final String keyword = "components";

    @Override
    protected String getKeyword() {
        return keyword;
    }

    @Override
    public ElementPattern<? extends PsiElement> getRefPattern() {
        return PlatformPatterns.or(
                // Ссылки в идентификаторах компонентов
                PlatformPatterns.psiElement(YAMLKeyValue.class)
                        .withParent(psi(YAMLMapping.class))
                        .withSuperParent(2,
                                psi(YAMLKeyValue.class)
                                        .withName(PlatformPatterns.string().equalTo(keyword))
                                        .withSuperParent(2, psi(YAMLDocument.class))
                        )
                ,
                // Ссылки в линках компонентов
                PlatformPatterns.psiElement()
                        .notEmpty()
                        .withParent(
                                psi(YAMLKeyValue.class)
                                        .withName("id")
                                        .withSuperParent(4,
                                                psi(YAMLKeyValue.class)
                                                        .withName("links")
                                                        .and(Components.rootPattern)
                                        )
                        )
                ,
                // Ссылки в контекстах
                PlatformPatterns.psiElement()
                        .withParent(psi(YAMLSequenceItem.class)
                                .withSuperParent(2,
                                        psi(YAMLKeyValue.class)
                                                .withName(PlatformPatterns.string().equalTo(keyword))
                                                .and(Contexts.rootPattern)
                                )
                        )

        );
    }
}
