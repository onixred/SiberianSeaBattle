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
package ru.onixred.siberian.sea.battle.layer.archunit.simple;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 23.10.2024
 */
public class PackageDependencyTest {
    public static final String IMPORT_PACKAGES = "ru.onixred.siberian.sea.battle.layer";
    @Test
    @DisplayName("Проверка от каких пакетов не зависит ACL")
    void aclPackageNoDependencyTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        ArchRule rule = noClasses().that().resideInAPackage(Layer.ACL.getPackageName())
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        Layer.CONFIGURATION.getPackageName(),
                        Layer.CONTROLLER.getPackageName(),
                        Layer.REPOSITORY.getPackageName(),
                        Layer.SERVICE.getPackageName()
                        );

        rule.check(javaClasses);
    }

    @Test
    @DisplayName("Проверка от каких пакетов зависит ACL")
    void aclPackageDependencyTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        ArchRule rule = classes().that().resideInAPackage(Layer.ACL.getPackageName())
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(Layer.MODEL.getPackageName(), Layer.DAO.getPackageName());

        rule.check(javaClasses);
    }

    @Test
    @DisplayName("Проверка какие пакеты зависят от пакета ACL")
    void aclPackageHaveDependencyTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        ArchRule rule = classes().that().resideInAPackage(Layer.ACL.getPackageName())
                .should()
                .onlyHaveDependentClassesThat()
                .resideInAnyPackage(
                        Layer.SERVICE.getPackageName());

        rule.check(javaClasses);
    }

    @Test
    @DisplayName("Проверка какие пакеты не зависят от пакета ACL")
    void aclPackageHaveNoDependencyTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        ArchRule rule = noClasses().that().resideInAPackage(Layer.ACL.getPackageName())
                .should()
                .onlyHaveDependentClassesThat()
                .resideInAnyPackage(Layer.CONFIGURATION.getPackageName(),
                        Layer.CONTROLLER.getPackageName(),
                        Layer.DAO.getPackageName(),
                        Layer.MODEL.getPackageName(),
                        Layer.REPOSITORY.getPackageName());
        rule.check(javaClasses);
    }
}

