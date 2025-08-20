Regular **Markdown** here.

<div hidden>


```
@startuml lakosMetricsFeatureFirstTest
skinparam componentStyle uml2
skinparam component {
  BorderColor #grey
  BackgroundColor #white
}
skinparam legend {
  BackgroundColor #lightyellow
}

[Component feature.first.sea.battle.game \nDependsOn: 1] as game
[Component feature.first.sea.battle.match \nDependsOn: 5] as match
[Component feature.first.sea.battle.player \nDependsOn: 3] as player
[Component feature.first.sea.battle.common \nDependsOn: 1] as common
[Component feature.first.sea.battle.configuration \nDependsOn: 1] as configuration
[Component feature.first.sea.battle.notification \nDependsOn: 2] as notification


notification --> common

match --> common
match --> player
match --> game
match --> notification

player --> common
player --> notification


legend
| <b>CCD</b>  | 13   |
| <b>ACD</b>  | 2.166  |
| <b>RACD</b> | 0.37  |
| <b>NCCD</b> | 0.93 |
endlegend

@enduml
```


```
@startuml componentDependencyMetricsFeatureFirstTest
skinparam componentStyle uml2
skinparam component {
  BorderColor #grey
  BackgroundColor #white
}
skinparam legend {
  BackgroundColor #lightyellow
}


[Component ru.onixred.siberian.sea.battle.feature.game \nCe: 0\nCa: 1\nI: 0] as game
[Component ru.onixred.siberian.sea.battle.feature.match \nCe: 4\nCa: 0\nI:1] as match
[Component ru.onixred.siberian.sea.battle.feature.player \nCe: 2\nCa: 1\nI: 0.66] as player
[Component ru.onixred.siberian.sea.battle.feature.common \nCe: 0\nCa: 3\nI: 0] as common
[Component ru.onixred.siberian.sea.battle.feature.configuration \nCe: 0\nCa: 0\nI: 1] as configuration
[Component ru.onixred.siberian.sea.battle.feature.notification \nCe: 1\nCa: 2\nI: 0.33] as notification


notification --> common

match --> common
match --> player
match --> game
match --> notification

player --> common
player --> notification

@enduml
```
</div>



### Какие есть аналоги

1. JQAssistant - Статический анализатор кода с основанном на Neo4J ()
2. JDepend - Анализатор кода создает отчет качества кода (устарел, прародитель ArchUnit)
3. Classycle - Инструмент анализа классов и зависимостей (устарел)
4. Degraph - Инструмент для визуализации и тестирования зависимостей классов и пакетов в приложениях (устарел)


### plantuml - это универсальный инструмент, позволяющий быстро и просто создавать широкий спектр диаграмм
Создаем диаграмму для наглядности



###  Проверка метрик Джона Лакоса на примере feature-first
![asd](../../../target/generated-diagrams/lakosMetricsFeatureFirstTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.onixred.siberian.sea.battle.feature");

Set<JavaPackage> subpackages = javaClasses.getPackage("ru.onixred.siberian.sea.battle.feature")
        .getSubpackages();
MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(subpackages);
LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(components);

assertTrue(metrics.getCumulativeComponentDependency() <= 15, 
        "CCD - Сумма зависимостей всех компонентов " + metrics.getCumulativeComponentDependency());
assertTrue(metrics.getAverageComponentDependency() <= 2.17, 
        "ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());
```


https://habr.com/ru/articles/772802/
###  Проверка метрик Роберта Мартина «Чистая архитектура» на примере feature-first
![asd](../../../target/generated-diagrams/componentDependencyMetricsFeatureFirstTest.svg)

В структуре компонентов нестабильные компоненты должны располагаться сверху, а более стабильные — снизу,
Чем больше I тем не стабильнее компонент.
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.onixred.siberian.sea.battle.feature");

Set<JavaPackage> subpackages = javaClasses.getPackage("ru.onixred.siberian.sea.battle.feature")
        .getSubpackages();
MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(subpackages);
ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(components);

int efferentCoupling = metrics.getEfferentCoupling(Feature.Game.getComponentIdentifier());
assertTrue(efferentCoupling <= 0, 
           "Ce - показывает зависимости пакета от внешних пакетов" + efferentCoupling);

int afferentCoupling = metrics.getAfferentCoupling(Feature.Game.getComponentIdentifier());
assertTrue(afferentCoupling <= 1, 
           "Ca - показывает зависимости внешних пакетов от указанного пакета" + afferentCoupling);

double instability = metrics.getInstability(Feature.Game.getComponentIdentifier());
assertTrue(instability <= 0, 
           "I - Ce / (Ca + Ce), (нестабильность)" + instability);
```
