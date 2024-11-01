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
import ru.cbr.siberian.sea.battle.acl.MatchMapper;
import ru.cbr.siberian.sea.battle.model.Match;

import java.util.List;
import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.adhereToPlantUmlDiagram;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchunitTest {

    public static final String IMPORT_PACKAGES = "ru.cbr.siberian.sea.battle";
    public static final String IMPORT_PACKAGES_FEATURE = "ru.cbr.siberian.feature.first.sea.battle";

    @Test
    void archunitRuleTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);

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
        rule.check(javaClasses);
    }

    @Test
    void resideInAPackageTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        ArchRule rule = noClasses()
                .that()
                .haveNameNotMatching(GameMapper.class.getName())
                .should()
                .dependOnClassesThat().resideInAPackage("lombok.experimental..");
        rule.check(javaClasses);
    }

    @Test
    void layeredTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);

        Architectures.LayeredArchitecture layeredArchitecture = layeredArchitecture().consideringAllDependencies()
                .layer(Layer.ACL.name()).definedBy("ru.cbr.siberian.sea.battle.acl..")
                .layer(Layer.CONFIGURATION.name()).definedBy("ru.cbr.siberian.sea.battle.configuration..")
                .layer(Layer.CONTROLLER.name()).definedBy("ru.cbr.siberian.sea.battle.controller..")
                .layer(Layer.DAO.name()).definedBy("ru.cbr.siberian.sea.battle.dao..")
                .layer(Layer.MODEL.name()).definedBy("ru.cbr.siberian.sea.battle.model")
                .layer("model.enumeration").definedBy("ru.cbr.siberian.sea.battle.model.enumeration..")
                .layer("model.game").definedBy("ru.cbr.siberian.sea.battle.model.game..")
                .layer("model.message").definedBy("ru.cbr.siberian.sea.battle.model.message..")
                .layer(Layer.REPOSITORY.name()).definedBy("ru.cbr.siberian.sea.battle.repository..")
                .layer(Layer.SERVICE.name()).definedBy("ru.cbr.siberian.sea.battle.service..")

                .whereLayer(Layer.ACL.name()).mayOnlyBeAccessedByLayers(Layer.SERVICE.name(), "model.enumeration")
                .whereLayer(Layer.CONFIGURATION.name()).mayNotBeAccessedByAnyLayer()
                .whereLayer(Layer.CONTROLLER.name()).mayNotBeAccessedByAnyLayer()
                .whereLayer(Layer.DAO.name()).mayOnlyBeAccessedByLayers(Layer.REPOSITORY.name(), Layer.ACL.name())
                .whereLayer(Layer.MODEL.name()).mayOnlyBeAccessedByLayers(Layer.SERVICE.name(), "model.message", Layer.ACL.name())
                .whereLayer("model.enumeration").mayOnlyBeAccessedByLayers(Layer.DAO.name(), Layer.MODEL.name(), "model.message", Layer.REPOSITORY.name(), Layer.SERVICE.name(), Layer.ACL.name())
                .whereLayer("model.game").mayOnlyBeAccessedByLayers(Layer.SERVICE.name(), Layer.ACL.name())
                .whereLayer("model.message").mayOnlyBeAccessedByLayers(Layer.CONTROLLER.name(), Layer.SERVICE.name())
                .whereLayer(Layer.REPOSITORY.name()).mayOnlyBeAccessedByLayers(Layer.SERVICE.name())
                .whereLayer(Layer.SERVICE.name()).mayOnlyBeAccessedByLayers(Layer.CONTROLLER.name());
        layeredArchitecture.check(javaClasses);
    }

    @Test
    void beFreeOfCyclesTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        SliceRule sliceRule = slices()
                .matching("ru.cbr.siberian.sea.battle.(*)..")
                .should()
                .beFreeOfCycles();
        sliceRule.check(javaClasses);
    }

    @Test
    void lakosMetricsTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);

        Set<JavaPackage> subpackages = javaClasses.getPackage(IMPORT_PACKAGES).getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);

        LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(metricsComponents);
        //https://www.archunit.org/userguide/html/000_Index.html
        System.out.println("CCD - Сумма зависимостей всех компонентов " + metrics.getCumulativeComponentDependency());
        System.out.println("ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());
        System.out.println("RACD - ACD деленное на количество всех компонентов " + metrics.getRelativeAverageComponentDependency());
        System.out.println("CCD системы, деленная на CCD сбалансированного бинарного дерева с тем же количеством компонентов " + metrics.getNormalizedCumulativeComponentDependency());

        assertTrue(metrics.getCumulativeComponentDependency() <= 21, "CCD - Сумма зависимостей всех компонентов " + metrics.getCumulativeComponentDependency());
        assertTrue(metrics.getAverageComponentDependency() <= 3, "ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());
        assertTrue(metrics.getRelativeAverageComponentDependency() < 0.5, "RACD - ACD деленное на количество всех компонентов " + metrics.getRelativeAverageComponentDependency());
        assertTrue(metrics.getNormalizedCumulativeComponentDependency() < 1.24, "CCD системы, деленная на CCD сбалансированного бинарного дерева с тем же количеством компонентов " + metrics.getNormalizedCumulativeComponentDependency());
    }

    @Test
    void lakosMetricsFeatureFirstTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES_FEATURE);

        Set<JavaPackage> subpackages = javaClasses.getPackage(IMPORT_PACKAGES_FEATURE).getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);

        LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(metricsComponents);
        //https://www.archunit.org/userguide/html/000_Index.html

        System.out.println("CCD - Сумма зависимостей всех компонентов " + metrics.getCumulativeComponentDependency());
        System.out.println("ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());
        System.out.println("RACD - ACD деленное на количество всех компонентов " + metrics.getRelativeAverageComponentDependency());
        System.out.println("CCD системы, деленная на CCD сбалансированного бинарного дерева с тем же количеством компонентов " + metrics.getNormalizedCumulativeComponentDependency());

        assertTrue(metrics.getCumulativeComponentDependency() <= 13, "CCD - Сумма зависимостей всех компонентов " + metrics.getCumulativeComponentDependency());
        assertTrue(metrics.getAverageComponentDependency() <= 2.17, "ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());
        assertTrue(metrics.getRelativeAverageComponentDependency() < 0.37, "RACD - ACD деленное на количество всех компонентов " + metrics.getRelativeAverageComponentDependency());
        assertTrue(metrics.getNormalizedCumulativeComponentDependency() < 0.93, "CCD системы, деленная на CCD сбалансированного бинарного дерева с тем же количеством компонентов " + metrics.getNormalizedCumulativeComponentDependency());
    }

    @Test
    void componentDependencyMetricsTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);

        Set<JavaPackage> subpackages = javaClasses.getPackage(IMPORT_PACKAGES).getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
        ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(metricsComponents);
        for (Layer layer : Layer.values()) {
            int efferentCoupling = metrics.getEfferentCoupling(layer.getComponentIdentifier(IMPORT_PACKAGES));

            System.out.println(layer + " Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости) " + efferentCoupling);
            int afferentCoupling = metrics.getAfferentCoupling(layer.getComponentIdentifier(IMPORT_PACKAGES));
            System.out.println(layer + " Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости) " + afferentCoupling);
            double instability = metrics.getInstability(layer.getComponentIdentifier(IMPORT_PACKAGES));
            System.out.println(layer + " I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям " + instability);
            double abstractness = metrics.getAbstractness(layer.getComponentIdentifier(IMPORT_PACKAGES));
            System.out.println(layer + " A - num(abstract_classes) / num(all_classes) в пакете " + abstractness);
            double normalizedDistanceFromMainSequence = metrics.getNormalizedDistanceFromMainSequence(layer.getComponentIdentifier(IMPORT_PACKAGES));
            System.out.println(layer + " D -  | A + I - 1 | нормализованное расстояние от идеальной линии между (A=1, I=0) и (A=0, I=1) " + normalizedDistanceFromMainSequence);

            assertTrue(efferentCoupling <= 3, layer + " Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости) " + efferentCoupling);
            assertTrue(afferentCoupling <= 5, layer + " Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости) " + afferentCoupling);
            assertTrue(instability <= 1, layer + " I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям " + instability);
            assertTrue(abstractness <= 1, layer + " A - num(abstract_classes) / num(all_classes) в пакете " + abstractness);
            assertTrue(normalizedDistanceFromMainSequence <= 0.86, layer + " D -  | A + I - 1 | нормализованное расстояние от идеальной линии между (A=1, I=0) и (A=0, I=1) " + normalizedDistanceFromMainSequence);
        }

    }


    @Test
    void componentDependencyMetricsFeatureFirstTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES_FEATURE);

        Set<JavaPackage> subpackages = javaClasses.getPackage(IMPORT_PACKAGES_FEATURE).getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
        ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(metricsComponents);
        List<String> layers = List.of(".game", ".match", ".player", ".common", ".configuration", ".notification");
        for (String layer : layers) {
            int efferentCoupling = metrics.getEfferentCoupling(IMPORT_PACKAGES_FEATURE + layer);
            System.out.println(layer + " Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости) " + efferentCoupling);
            int afferentCoupling = metrics.getAfferentCoupling(IMPORT_PACKAGES_FEATURE + layer);
            System.out.println(layer + " Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости) " + afferentCoupling);
            double instability = metrics.getInstability(IMPORT_PACKAGES_FEATURE + layer);
            System.out.println(layer + " I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям " + instability);
            double abstractness = metrics.getAbstractness(IMPORT_PACKAGES_FEATURE + layer);
            System.out.println(layer + " A - num(abstract_classes) / num(all_classes) в пакете " + abstractness);
            double normalizedDistanceFromMainSequence = metrics.getNormalizedDistanceFromMainSequence(IMPORT_PACKAGES_FEATURE + layer);
            System.out.println(layer + " D -  | A + I - 1 | нормализованное расстояние от идеальной линии между (A=1, I=0) и (A=0, I=1) " + normalizedDistanceFromMainSequence);

            assertTrue(efferentCoupling <= 4, layer + " Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости) " + efferentCoupling);
            assertTrue(afferentCoupling <= 5, layer + " Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости) " + afferentCoupling);
            assertTrue(instability <= 1, layer + " I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям " + instability);
            assertTrue(abstractness <= 1, layer + " A - num(abstract_classes) / num(all_classes) в пакете " + abstractness);
            assertTrue(normalizedDistanceFromMainSequence <= 0.86, layer + " D -  | A + I - 1 | нормализованное расстояние от идеальной линии между (A=1, I=0) и (A=0, I=1) " + normalizedDistanceFromMainSequence);
            System.out.println();
        }

    }


    @Test
    void shouldNotUseFieldInjectionTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        ArchRule rule = noFields().should()
                .beAnnotatedWith(Autowired.class);

        rule.check(javaClasses);
    }

    @Test
    void shouldFollowNamingConventionTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        ArchRule rule = classes().that().areNotAnonymousClasses().and()
                .resideInAnyPackage(Layer.ACL.getComponentIdentifier(IMPORT_PACKAGES))
                .should()
                .haveSimpleNameEndingWith("Mapper");

        rule.check(javaClasses);
    }

    @Test
    void shouldNotCreateMatchInMatchMapperTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        ArchRule rule = noConstructors().that().areDeclaredInClassesThat()
                .areAssignableTo(Match.class).should().onlyBeCalled().byClassesThat().haveNameMatching(MatchMapper.class.getName());

        rule.check(javaClasses);
    }

    @Test
    void plantUmlTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);
        final var myDiagram = getClass().getClassLoader().getResource("siberian-sea-battle-class-dependency.puml");
        var ss = classes().should(adhereToPlantUmlDiagram(myDiagram, consideringOnlyDependenciesInDiagram()));

        ss.check(javaClasses);
    }

}
