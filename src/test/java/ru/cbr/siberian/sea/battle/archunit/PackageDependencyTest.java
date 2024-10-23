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
package ru.cbr.siberian.sea.battle.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
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
    @Test
    @DisplayName("Проверка от каких пакетов не зависит ACL")
    void AclPackageNoDependencyTest() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ru.cbr.siberian.sea.battle");
        ArchRule rule = noClasses().that().resideInAPackage(Layer.ACL.getPackageName())
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        Layer.CONFIGURATION.getPackageName(),
                        Layer.CONTROLLER.getPackageName(),
                        Layer.REPOSITORY.getPackageName(),
                        Layer.SERVICE.getPackageName(),
                        Layer.DAO.getPackageName());

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Проверка от каких пакетов зависит ACL")
    void AclPackageDependencyTest() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ru.cbr.siberian.sea.battle");
        ArchRule rule = classes().that().resideInAPackage(Layer.ACL.getPackageName())
                .should()
                .dependOnClassesThat()
                .resideInAPackage(
                        Layer.MODEL.getPackageName());

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Проверка кто зависит от пакета ACL")
    void AclPackageHaveDependencyTest() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ru.cbr.siberian.sea.battle");
        ArchRule rule = classes().that().resideInAPackage(Layer.ACL.getPackageName())
                .should()
                .onlyHaveDependentClassesThat()
                .resideInAnyPackage(
                        Layer.SERVICE.getPackageName());

        rule.check(importedClasses);
    }
}
