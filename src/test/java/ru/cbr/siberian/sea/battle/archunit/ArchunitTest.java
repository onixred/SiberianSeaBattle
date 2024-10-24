package ru.cbr.siberian.sea.battle.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchunitTest {

    @Test
    void archunitRule() {
        JavaClasses importPackages = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("ru.cbr.siberian.sea.battle");

        ArchRule rule = classes()
                .that()
                .resideInAnyPackage("ru.cbr.siberian.sea.battle..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "ru.cbr.siberian.sea.battle..",
                        "org.springframework..",
                        "jakarta.persistence..",
                        "org.modelmapper..",
                        "jakarta.validation..",
                        "org.slf4j..",
                        "org.hibernate.proxy..",
                        "lombok..",
                        "java.."
                );
        rule.check(importPackages);
    }
}
