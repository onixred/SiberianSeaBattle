package ru.onixred.siberian.sea.battle.layer.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.onixred.siberian.sea.battle.layer.model.message.BaseRequestMessage;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class InheritanceTest {

    @Test
    @DisplayName("Проверка все классы реализующие интерфейс BaseRequestMessage должны иметь постфикс RequestMessage")
    void implementBaseRequestMessageTest() {
        String importPackages = "ru.onixred.siberian.sea.battle.layer";
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(importPackages);
        ArchRule rule = classes().that().implement(BaseRequestMessage.class)
                .should().haveSimpleNameEndingWith("RequestMessage");

        rule.check(javaClasses);
    }
}
