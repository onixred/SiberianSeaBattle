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

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import java.lang.annotation.Annotation;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 29.10.2024
 */
public class AnnotationsForAllPublicMethodsToClassRuleTest implements ArchUnitRuleTest {


    //""

    /**
     * Все публичные методы классов из пакета resideInAPackage должны использовать аннотацию annotation
     *
     * @param packagePath      путь базового пакета для анализа
     * @param resideInAPackage резиденты пакета
     * @param annotation       аннотация которая должна быть на методах
     */
    public static void execute(String packagePath, String resideInAPackage, Class<? extends Annotation> annotation) {

        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(packagePath);


        ArchRule rule = ArchRuleDefinition.methods().that().arePublic()
                .and().areDeclaredInClassesThat().resideInAPackage(resideInAPackage)
                .should()
                .beAnnotatedWith(annotation);

        rule.check(importedClasses);
    }

}
