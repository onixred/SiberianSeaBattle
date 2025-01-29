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
package ru.onixred.siberian.sea.battle.layer.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.onixred.siberian.sea.battle.layer.model.message.BaseRequestMessage;

import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.are;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 29.10.2024
 */
public class AnnotationTest {


    @Test
    @DisplayName("Проверка все поля классов реализующие интерфейс BaseRequestMessage используют аннотации проверки NotBlank или NotNull")
    void annotationBaseRequestMessageTest() {
        String importPackages = "ru.onixred.siberian.sea.battle.layer";
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(importPackages);
        ArchRule rule = classes().that().areAssignableTo(BaseRequestMessage.class)
                .should().onlyAccessFieldsThat(are(annotatedWith(NotBlank.class)).or(annotatedWith(NotNull.class)));

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Проверка во всех классах в пакете controller на методах должна стоять аннотация MessageMapping")
    void annotationMessageMappingTest() {
        String importPackages = "ru.onixred.siberian.sea.battle.layer";
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(importPackages);

        ArchRule rule = methods().that().arePublic()
                        .and().areDeclaredInClassesThat().resideInAPackage("..controller..")
                        .should()
                                .beAnnotatedWith(MessageMapping.class);


        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Проверка во всех классах в пакете controller на методах должна стоять аннотация PreAuthorize")
    void annotationPreAuthorizeTest() {
        String importPackages = "ru.onixred.siberian.sea.battle.layer";
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(importPackages);

        ArchRule rule = methods().that().arePublic()
                .and().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(PreAuthorize.class);

        rule.check(importedClasses);
    }


}
