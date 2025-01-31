/*
 *  Copyright 2023 Contributors to the Sports-club.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ru.onixred.siberian.sea.battle.core.rule;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.are;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 29.10.2024
 */
public class AnnotationsForAllClassesAssignableToClassRuleTest implements ArchUnitRuleTest {


    /**
     * Все поля классов реализующие интерфейс assignableClass используют аннотации из списка annotations
     *
     * @param packagePath     путь базового пакета для анализа
     * @param assignableClass базовый класс
     * @param annotations     список аннотаций которые должны быть на полях
     */
    public void execute(String packagePath, Class<?> assignableClass, List<Class<? extends Annotation>> annotations) {

        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(packagePath);


        DescribedPredicate<CanBeAnnotated> predicate = null;
        for (Class<? extends Annotation> annotation : annotations) {
            if (predicate == null) {
                predicate = are(annotatedWith(annotation));
            } else {
                predicate.or(annotatedWith(annotation));
            }

        }
        ArchRule rule = classes().that().areAssignableTo(assignableClass)
                .should().onlyAccessFieldsThat(predicate);
        rule.check(importedClasses);
    }
}
