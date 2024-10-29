package ru.cbr.siberian.sea.battle.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.metrics.ArchitectureMetrics;
import com.tngtech.archunit.library.metrics.ComponentDependencyMetrics;
import com.tngtech.archunit.library.metrics.LakosMetrics;
import com.tngtech.archunit.library.metrics.MetricsComponents;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cbr.siberian.sea.battle.acl.GameMapper;
import ru.cbr.siberian.sea.battle.model.message.MatchUI;

import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchunitTest {

    @Test
    void archunitRuleTest() {
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

    @Test
    void resideInAPackageTest() {
        JavaClasses importPackages = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("ru.cbr.siberian.sea.battle");
        ArchRule rule = noClasses()
                .that()
                .haveNameNotMatching(GameMapper.class.getName())
                .should()
                .dependOnClassesThat().resideInAPackage("lombok.experimental..");
        rule.check(importPackages);
    }

    @Test
    void layeredTest() {
        JavaClasses importPackages = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("ru.cbr.siberian.sea.battle");

        Architectures.LayeredArchitecture layeredArchitecture = layeredArchitecture().consideringAllDependencies()
                .layer("acl").definedBy("ru.cbr.siberian.sea.battle.acl..")
                .layer("configuration").definedBy("ru.cbr.siberian.sea.battle.configuration..")
                .layer("controller").definedBy("ru.cbr.siberian.sea.battle.controller..")
                .layer("dao").definedBy("ru.cbr.siberian.sea.battle.dao..")
                .layer("model").definedBy("ru.cbr.siberian.sea.battle.model")
                .layer("model.enumeration").definedBy("ru.cbr.siberian.sea.battle.model.enumeration..")
                .layer("model.game").definedBy("ru.cbr.siberian.sea.battle.model.game..")
                .layer("model.message").definedBy("ru.cbr.siberian.sea.battle.model.message..")
                .layer("repository").definedBy("ru.cbr.siberian.sea.battle.repository..")
                .layer("service").definedBy("ru.cbr.siberian.sea.battle.service..")

                .whereLayer("acl").mayOnlyBeAccessedByLayers("service", "model.enumeration")
                .whereLayer("configuration").mayNotBeAccessedByAnyLayer()
                .whereLayer("controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("dao").mayOnlyBeAccessedByLayers("repository",  "acl")
                .whereLayer("model").mayOnlyBeAccessedByLayers("service", "model.message", "acl")
                .whereLayer("model.enumeration").mayOnlyBeAccessedByLayers("dao", "model", "model.message", "repository", "service", "acl")
                .whereLayer("model.game").mayOnlyBeAccessedByLayers("service", "acl")
                .whereLayer("model.message").mayOnlyBeAccessedByLayers("controller", "service")
                .whereLayer("repository").mayOnlyBeAccessedByLayers("service")
                .whereLayer("service").mayOnlyBeAccessedByLayers("controller");
        layeredArchitecture.check(importPackages);
    }

    @Test
    void beFreeOfCyclesTest() {
        JavaClasses importPackages = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("ru.cbr.siberian.sea.battle");
        SliceRule sliceRule = slices()
                .matching("ru.cbr.siberian.sea.battle.(*)..")
                .should()
                .beFreeOfCycles();
        sliceRule.check(importPackages);
    }

    @Test
    void lakosMetricsTest() {
        JavaClasses importPackages = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("ru.cbr.siberian.sea.battle");

        Set<JavaPackage> subpackages = importPackages.getPackage("ru.cbr.siberian.sea.battle").getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
        LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(metricsComponents);
        //https://www.archunit.org/userguide/html/000_Index.html
        assertTrue(metrics.getCumulativeComponentDependency() <= 21, "CCD - Сумма зависимомтей всех компонентов " + metrics.getCumulativeComponentDependency());
        assertTrue(metrics.getAverageComponentDependency() <= 3, "ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());

        assertTrue(metrics.getRelativeAverageComponentDependency() < 0.5, "RACD - ACD деленное на количество всех компонентов " + metrics.getRelativeAverageComponentDependency());
        assertTrue(metrics.getNormalizedCumulativeComponentDependency() < 1.24, "CCD системы, деленная на CCD сбалансированного бинарного дерева с тем же количеством компонентов " + metrics.getNormalizedCumulativeComponentDependency());
    }

    @Test
    void componentDependencyMetricsTest() {
        String importPackages = "ru.cbr.siberian.sea.battle";

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(importPackages);

        Set<JavaPackage> subpackages = javaClasses.getPackage("ru.cbr.siberian.sea.battle").getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
        ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(metricsComponents);
        int efferentCoupling = metrics.getEfferentCoupling(importPackages + ".acl");
        assertTrue(efferentCoupling <= 2, "Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости)" + efferentCoupling);
        int afferentCoupling = metrics.getAfferentCoupling(importPackages + ".acl");
        assertTrue(afferentCoupling <= 1, "Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости)" + afferentCoupling);
        double instability = metrics.getInstability(importPackages + ".acl");
        assertTrue(instability <= 0.7, "I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям" + instability);
        double abstractness = metrics.getAbstractness(importPackages + ".acl");
        assertTrue(abstractness <= 0, "A - num(abstract_classes) / num(all_classes) в пакете" + abstractness);
        double normalizedDistanceFromMainSequence = metrics.getNormalizedDistanceFromMainSequence(importPackages + ".acl");
        assertTrue(normalizedDistanceFromMainSequence <= 0.5, "D -  | A + I - 1 | нормализованное расстояние от идеальной линии между (A=1, I=0) и (A=0, I=1)" + normalizedDistanceFromMainSequence);
    }


    @Test
    void shouldNotUseFieldInjectionTest() {
        JavaClasses importPackages = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("ru.cbr.siberian.sea.battle");
        ArchRule rule = noFields().should()
                .beAnnotatedWith(Autowired.class);

        rule.check(importPackages);
    }

    @Test
    void shouldFollowNamingConventionTest() {
        String importPackages = "ru.cbr.siberian.sea.battle";

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(importPackages);
        ArchRule rule = classes().that().areNotAnonymousClasses().and()
                .resideInAnyPackage(importPackages + ".acl", importPackages + "acl.*")
                .should()
                .haveNameMatching(".*Mapper");

        rule.check(javaClasses);
    }

    @Test
    void shouldNotCreateMatchUIInGameMapperTest() {
        String importPackages = "ru.cbr.siberian.sea.battle";

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(importPackages);
        ArchRule rule = constructors().that().areDeclaredInClassesThat()
                .areAssignableTo(MatchUI.class).should().onlyBeCalled().byClassesThat().haveNameNotMatching(GameMapper.class.getName());

        rule.check(javaClasses);
    }
}
