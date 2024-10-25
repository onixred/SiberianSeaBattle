package ru.cbr.siberian.sea.battle.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.cbr.siberian.sea.battle.acl.MatchMapper;
import ru.cbr.siberian.sea.battle.service.MatchService;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class InheritanceTest {

    @Test
    @DisplayName("Проверка кто зависит от класса GameMapper")
    void InheritanceTest() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("ru.cbr.siberian.sea.battle");
        ArchRule rule = classes().that().haveNameMatching(MatchMapper.class.getName())
                .should()
                .onlyHaveDependentClassesThat().haveNameMatching(MatchService.class.getName());

        rule.check(importedClasses);
    }
}
