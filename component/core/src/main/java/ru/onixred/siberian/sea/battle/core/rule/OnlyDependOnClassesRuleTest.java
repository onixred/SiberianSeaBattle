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

import java.util.List;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 31.01.2025
 */
public class OnlyDependOnClassesRuleTest implements ArchUnitRuleTest {


    /**
     * Все классы из пакета resideInAPackage должны содержать заданный список зависимостей resideInAnyPackages
     *
     * @param packagePath     путь базового пакета для анализа
     * @param resideInAPackage резиденты пакета
     * @param resideInAnyPackages   список зависимостей
     */
    public static void execute(String packagePath, String resideInAPackage, List<String> resideInAnyPackages) {

        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(packagePath);


       String[] packages = resideInAnyPackages.toArray(new String[0]);
        ArchRule rule = ArchRuleDefinition.classes()
                .that()
                .resideInAnyPackage(resideInAPackage)
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage(packages);

        rule.check(importedClasses);
    }
}
