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
package ru.onixred.siberian.sea.battle.layer.archunit.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import ru.onixred.siberian.sea.battle.core.rule.AnnotationsForAllClassesAssignableToClassRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.AnnotationsForAllPublicMethodsToClassRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.ComponentDependencyMetricsRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.ComponentMetric;
import ru.onixred.siberian.sea.battle.core.rule.ComponentMetricLayer;
import ru.onixred.siberian.sea.battle.core.rule.LakosMetric;
import ru.onixred.siberian.sea.battle.core.rule.LakosMetricsRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.LayeredRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.NamingConventionInPackageRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.OnlyDependOnClassesRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.OnlyHaveNameNotMatchingRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.PackageContainmentRuleTest;
import ru.onixred.siberian.sea.battle.core.rule.ParamLayer;
import ru.onixred.siberian.sea.battle.layer.acl.GameMapper;
import ru.onixred.siberian.sea.battle.layer.archunit.simple.Layer;
import ru.onixred.siberian.sea.battle.layer.model.message.BaseRequestMessage;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 31.01.2025
 */
public class ArchUnitWithCoreTest {
    public static final String IMPORT_PACKAGES = "ru.onixred.siberian.sea.battle.layer";


    @Test
    @DisplayName("Проверка все поля классов реализующие интерфейс BaseRequestMessage используют аннотации проверки NotBlank или NotNull")
    void annotationBaseRequestMessageTest() {
        AnnotationsForAllClassesAssignableToClassRuleTest.execute(IMPORT_PACKAGES, BaseRequestMessage.class, List.of(NotBlank.class, NotNull.class));
    }

    @Test
    @DisplayName("Проверка во всех классах в пакете controller на методах должна стоять аннотация MessageMapping")
    void annotationMessageMappingTest() {
        AnnotationsForAllPublicMethodsToClassRuleTest.execute(IMPORT_PACKAGES, Layer.CONTROLLER.getPackageName(), MessageMapping.class);
    }
    @Test
    @DisplayName("Проверка метрик связности компонентов")
    void componentDependencyMetricsTest() {
        List<ComponentMetricLayer> layers = List.of(ComponentMetricLayers.values());
        ComponentDependencyMetricsRuleTest.execute(IMPORT_PACKAGES, IMPORT_PACKAGES, layers);
    }


    @Test
    @DisplayName("Проверка метрик John Lakos")
    void lakosMetricsTest() {
        LakosMetricsRuleTest.execute(IMPORT_PACKAGES, IMPORT_PACKAGES, new LakosMetric(21,3.0));
    }

    @Test
    @DisplayName("Проверка слоев")
    void layeredTest() {
        Map<ParamLayer, List<ParamLayer>> layerBeAccessedByLayers = Map.of(
                Layer.ACL, List.of(Layer.SERVICE, Layer.MODEL_ENUMERATION),
                Layer.CONFIGURATION, List.of(),
                Layer.CONTROLLER, List.of(),
                Layer.DAO, List.of(Layer.REPOSITORY, Layer.ACL),
                Layer.MODEL, List.of(Layer.SERVICE, Layer.ACL,Layer.DAO, Layer.CONTROLLER, Layer.REPOSITORY),
                Layer.MODEL_ENUMERATION, List.of(Layer.DAO, Layer.MODEL, Layer.MODEL_MESSAGE, Layer.REPOSITORY, Layer.SERVICE, Layer.ACL),
                Layer.MODEL_GAME, List.of(Layer.SERVICE, Layer.ACL),
                Layer.MODEL_MESSAGE, List.of(Layer.CONTROLLER, Layer.SERVICE),
                Layer.REPOSITORY, List.of(Layer.SERVICE),
                Layer.SERVICE, List.of(Layer.CONTROLLER));
         LayeredRuleTest.execute(IMPORT_PACKAGES,layerBeAccessedByLayers);
    }

    @ParameterizedTest
    @DisplayName("Проверка именований классов в пакетах")
    @EnumSource(value = Layer.class)
    void shouldFollowNamingConventionTest(Layer layer) {
            NamingConventionInPackageRuleTest.execute(IMPORT_PACKAGES, layer);
    }

    @Test
    @DisplayName("Проверка зависимостей")
    void onlyDependOnClassesRuleTestTest() {
        List<String> dependencies = List.of(IMPORT_PACKAGES + "..",
                "org.springframework..",
                "jakarta.persistence..",
                "org.modelmapper..",
                "jakarta.validation..",
                "org.slf4j..",
                "org.hibernate.proxy..",
                "lombok..",
                "java..");
        OnlyDependOnClassesRuleTest.execute(IMPORT_PACKAGES, IMPORT_PACKAGES + "..", dependencies);
    }

    @Test
    @DisplayName("Проверка что только класс GameMapper может иметь зависимость lombok.experimental..")
    void resideInAPackageTest() {
        OnlyHaveNameNotMatchingRuleTest.execute(IMPORT_PACKAGES, GameMapper.class.getName(), List.of("lombok.experimental.."));
    }

    @ParameterizedTest
    @DisplayName("Проверка именований классов в пакетах")
    @EnumSource(value = Layer.class)
    void resideInAPackageTest(Layer layer) {
        if(layer.getName().contains("model")) {
            return;
        }
        PackageContainmentRuleTest.execute(IMPORT_PACKAGES, layer);
    }

    @Getter
    @RequiredArgsConstructor
    enum ComponentMetricLayers implements ComponentMetricLayer {

        ACL("acl", new ComponentMetric(2,1,0.67)),
        CONFIGURATION("configuration", new ComponentMetric(0,0,1)),
        CONTROLLER("controller", new ComponentMetric(2,0,1)),
        DAO("dao", new ComponentMetric(1,2,0.34)),
        MODEL("model", new ComponentMetric(0,5,0)),
        MODEL_ENUMERATION("model.enumeration", new ComponentMetric(0,0,0)),
        MODEL_GAME("model.game", new ComponentMetric(0,0,0)),
        MODEL_MESSAGE("model.message", new ComponentMetric(0,0,0)),
        REPOSITORY("repository", new ComponentMetric(2,1,0.67)),
        SERVICE("service", new ComponentMetric(3,1,0.75));

        private final String packageName;

        private final ComponentMetric componentMetric;

        public String getComponentIdentifier() {
            return IMPORT_PACKAGES + "." + packageName;
        }


    }



}
