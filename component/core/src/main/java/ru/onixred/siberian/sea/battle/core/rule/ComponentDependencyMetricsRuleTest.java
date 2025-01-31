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

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.metrics.ArchitectureMetrics;
import com.tngtech.archunit.library.metrics.ComponentDependencyMetrics;
import com.tngtech.archunit.library.metrics.MetricsComponents;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 31.01.2025
 */
public class ComponentDependencyMetricsRuleTest implements ArchUnitRuleTest {


    /**
     * Проверка метрик
     *
     * @param packagePath путь базового пакета для анализа
     * @param packageName пакет для анализа метрик
     * @param layers      метрики по слоям
     */
    public static void execute(String packagePath, String packageName, List<ComponentMetricLayer> layers) {

        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(packagePath);
        Set<JavaPackage> subpackages = importedClasses.getPackage(packageName).getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);

        ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(metricsComponents);
        for (ComponentMetricLayer layer : layers) {
            if(layer.getPackageName().contains(".")) {
                continue;
            }
            int efferentCoupling = metrics.getEfferentCoupling(layer.getComponentIdentifier());
            int afferentCoupling = metrics.getAfferentCoupling(layer.getComponentIdentifier());
            double instability = metrics.getInstability(layer.getComponentIdentifier());
            ComponentMetric componentMetric = layer.getComponentMetric();

            assertTrue(efferentCoupling <= componentMetric.efferentCoupling(),
                    String.format("Слой %s Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости) расчет %s  ожидание %s",
                            layer.getPackageName(), efferentCoupling, componentMetric.efferentCoupling()));
            assertTrue(afferentCoupling >= componentMetric.afferentCoupling(),
                    String.format("Слой %s Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости) расчет %s  ожидание %s",
                            layer.getPackageName(), afferentCoupling, componentMetric.afferentCoupling()));
            assertTrue(instability <= componentMetric.instability(),
                    String.format("Слой %s I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям расчет %s  ожидание %s",
                            layer.getPackageName(), instability, componentMetric.instability()));

        }
    }
}
