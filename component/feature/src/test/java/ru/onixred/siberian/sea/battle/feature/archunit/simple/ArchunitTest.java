package ru.onixred.siberian.sea.battle.feature.archunit.simple;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.metrics.ArchitectureMetrics;
import com.tngtech.archunit.library.metrics.ComponentDependencyMetrics;
import com.tngtech.archunit.library.metrics.LakosMetrics;
import com.tngtech.archunit.library.metrics.MetricsComponents;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.onixred.siberian.sea.battle.feature.game.GameMapper;
import ru.onixred.siberian.sea.battle.feature.match.Match;
import ru.onixred.siberian.sea.battle.feature.match.MatchMapper;

import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noConstructors;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.adhereToPlantUmlDiagram;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchunitTest {


    public static final String IMPORT_PACKAGES_FEATURE = "ru.onixred.siberian.sea.battle.feature";

    @Test
    void archunitRuleTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES_FEATURE);

        ArchRule rule = classes()
                .that()
                .resideInAnyPackage(IMPORT_PACKAGES_FEATURE + "..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage(
                        IMPORT_PACKAGES_FEATURE + "..",
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
                .importPackages(IMPORT_PACKAGES_FEATURE);
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
                .importPackages(IMPORT_PACKAGES_FEATURE);

        Architectures.LayeredArchitecture layeredArchitecture = layeredArchitecture().consideringAllDependencies()
                .layer(Layer.COMMON.name()).definedBy(Layer.COMMON.getPackageName())
                .layer(Layer.CONFIGURATION.name()).definedBy(Layer.CONFIGURATION.getPackageName())
                .layer(Layer.GAME.name()).definedBy(Layer.GAME.getPackageName())
                .layer(Layer.MATCH.name()).definedBy(Layer.MATCH.getPackageName())
                .layer(Layer.NOTIFICATION.name()).definedBy(Layer.NOTIFICATION.getPackageName())
                .layer(Layer.PLAYER.name()).definedBy(Layer.PLAYER.getPackageName())

                .whereLayer(Layer.COMMON.name()).mayOnlyBeAccessedByLayers(Layer.MATCH.name(), Layer.PLAYER.name(), Layer.NOTIFICATION.name())
                .whereLayer(Layer.CONFIGURATION.name()).mayNotBeAccessedByAnyLayer()
                .whereLayer(Layer.GAME.name()).mayOnlyBeAccessedByLayers(Layer.MATCH.name())
                .whereLayer(Layer.MATCH.name()).mayNotBeAccessedByAnyLayer()
                .whereLayer(Layer.NOTIFICATION.name()).mayOnlyBeAccessedByLayers(Layer.MATCH.name(), Layer.PLAYER.name())
                .whereLayer(Layer.PLAYER.name()).mayOnlyBeAccessedByLayers(Layer.MATCH.name());

        layeredArchitecture.check(javaClasses);
    }

    @Test
    void beFreeOfCyclesTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES_FEATURE);
        SliceRule sliceRule = slices()
                .matching(IMPORT_PACKAGES_FEATURE + ".(*)..")
                .should()
                .beFreeOfCycles();
        sliceRule.check(javaClasses);
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
    void componentDependencyMetricsFeatureFirstTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES_FEATURE);

        Set<JavaPackage> subpackages = javaClasses.getPackage(IMPORT_PACKAGES_FEATURE).getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
        ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(metricsComponents);

        for (Layer layer : Layer.values()) {
            int efferentCoupling = metrics.getEfferentCoupling(layer.getComponentIdentifier());
            System.out.println(layer + " Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости) " + efferentCoupling);
            int afferentCoupling = metrics.getAfferentCoupling(layer.getComponentIdentifier());
            System.out.println(layer + " Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости) " + afferentCoupling);
            double instability = metrics.getInstability(layer.getComponentIdentifier());
            System.out.println(layer + " I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям " + instability);
            double abstractness = metrics.getAbstractness(layer.getComponentIdentifier());
            System.out.println(layer + " A - num(abstract_classes) / num(all_classes) в пакете " + abstractness);
            double normalizedDistanceFromMainSequence = metrics.getNormalizedDistanceFromMainSequence(layer.getComponentIdentifier());
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
                .importPackages(IMPORT_PACKAGES_FEATURE);
        ArchRule rule = noFields().should()
                .beAnnotatedWith(Autowired.class);

        rule.check(javaClasses);
    }

    @Test
    void shouldFollowNamingConventionTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES_FEATURE);
        ArchRule rule = classes().that().areNotAnonymousClasses().and()
                .resideInAnyPackage(Layer.NOTIFICATION.getComponentIdentifier())
                .should()
                .haveNameMatching(".*notification.*");

        rule.check(javaClasses);
    }

    @Test
    void shouldNotCreateMatchInMatchMapperTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES_FEATURE);
        ArchRule rule = noConstructors().that().areDeclaredInClassesThat()
                .areAssignableTo(Match.class).should().onlyBeCalled().byClassesThat().haveNameMatching(MatchMapper.class.getName());

        rule.check(javaClasses);
    }

    @Test
    void plantUmlTest() {
        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES_FEATURE);
        final var myDiagram = getClass().getClassLoader().getResource("siberian-sea-battle-class-dependency.puml");
        ClassesShouldConjunction conjunction = classes().should(adhereToPlantUmlDiagram(myDiagram, consideringOnlyDependenciesInDiagram()));

        conjunction.check(javaClasses);
    }

}
