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
import com.tngtech.archunit.library.metrics.LakosMetrics;
import com.tngtech.archunit.library.metrics.MetricsComponents;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 31.01.2025
 */
public class LakosMetricsRuleTest implements ArchUnitRuleTest {

    /**
     * Проверка метрик
     *
     * @param packagePath     путь базового пакета для анализа
     * @param packageName     пакет для анализа метрик
     * @param metric метрики John Lakos
     */
    public static void execute(String packagePath, String packageName, LakosMetric metric) {

        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(packagePath);
        Set<JavaPackage> subpackages = importedClasses.getPackage(packageName).getSubpackages();
        MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
        LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(metricsComponents);

        assertTrue(metrics.getCumulativeComponentDependency() <= metric.cumulativeComponentDependency(),
                String.format("CCD - Сумма зависимостей всех компонентов расчет %s  ожидание %s",
                        metrics.getCumulativeComponentDependency(), metric.cumulativeComponentDependency()));
        assertTrue(metrics.getAverageComponentDependency() <= metric.averageComponentDependency(),
                String.format("ACD - CCD деленная на количество всех компонентов расчет %s ожидание %s" ,
                        metrics.getAverageComponentDependency(), metric.averageComponentDependency()));

    }
}
