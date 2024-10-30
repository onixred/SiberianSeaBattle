Regular **Markdown** here.

<div hidden>

```
@startuml aclPackageNoDependencyTest
skinparam componentStyle uml2
skinparam component {
BorderColor #grey
BackgroundColor #white
}
[acl] --[#green]right-->  [model]
note left on link #green: доступ разрешен
[acl] --[#green]left-->  [dao]
note left on link #green: доступ разрешен
[acl] --[#crimson]-->  [configuration]
note top on link #crimson: доступ запрещен
[acl] --[#crimson]-->  [controller]
note top on link #crimson: доступ запрещен
[acl] --[#crimson]-->  [repository]
note top on link #crimson: доступ запрещен
[acl] --[#crimson]-->  [service]
note top on link #crimson: доступ запрещен
@enduml
```


```
@startuml aclPackageDependencyTest
skinparam componentStyle uml2
skinparam component {
BorderColor #grey
BackgroundColor #white
}

[acl] --[#green]right-->  [model]
note left on link #green: доступ разрешен
[acl] --[#green]right-->  [dao]
note left on link #green: доступ разрешен
[acl] --[#crimson]left--> [service]
note top on link #crimson: доступ запрещен
@enduml
```

```
@startuml aclPackageHaveDependencyTest
skinparam componentStyle uml2
skinparam component {
BorderColor #grey
BackgroundColor #white
}

[service] --[#green]right-->  [acl]
note left on link #green: доступ разрешен
[model]
@enduml
```

```
@startuml aclPackageHaveNoDependencyTest
skinparam componentStyle uml2
skinparam component {
BorderColor #grey
BackgroundColor #white
}

[service] --[#green]right-->  [acl]
note left on link #green: доступ разрешен
[configuration]
[controller]
[dao]
[model]
[repository]

@enduml
```

```
@startuml matchMapperClassDependencyTest
hide empty members
skinparam componentStyle uml2

skinparam component {
  BorderColor #grey
  BackgroundColor #white
}

skinparam class {
  BorderColor #grey
  BackgroundColor #white
}

class GameService
class MatchMapper

GameService -> MatchMapper
note top on link #crimson: доступ запрещен

MatchService --> MatchMapper #green
note left on link #green: доступ разрешен
@enduml
```

```
@startuml gameMapperClassDependencyTest
hide empty members
set separator none
skinparam componentStyle uml2

skinparam component {
BorderColor #grey
BackgroundColor #white
}

skinparam class {
BorderColor #grey
BackgroundColor #white
}

package ru.cbr.siberian.sea.battle.acl {
class MatchMapper
class GameMapper
}

package ru.cbr.siberian.sea.battle.service {
class MatchService
}

note "находится в пакете 'service'" as ServicePackage #crimson
MatchService .. ServicePackage
@enduml
```

```
@startuml implementBaseRequestMessageTest
hide empty members
skinparam componentStyle uml2

skinparam component {
  BorderColor #grey
  BackgroundColor #white
}

skinparam class {
  BorderColor #grey
  BackgroundColor #white
}

class BaseRequestMessage <<interface>>
class CreateGameRequestMessage <<concrete>>
class CreateUserRequestMessage <<concrete>>
class ChatRequest <<concrete>>

CreateGameRequestMessage ..|> BaseRequestMessage #green
CreateUserRequestMessage ..|> BaseRequestMessage #green
ChatRequest ..|> BaseRequestMessage #crimson

note right on link #crimson: Неправильное имя у класса
@enduml
```

```
@startuml annotationBaseRequestMessageTest
hide empty members
skinparam componentStyle uml2

skinparam component {
  BorderColor #grey
  BackgroundColor #white
}

skinparam class {
  BorderColor #grey
  BackgroundColor #white
}

class BaseRequestMessage <<interface>>
class CreateGameRequestMessage <<concrete>> {
{field} -userId : String 
}
note right of CreateGameRequestMessage::userId
  поле помечено аннотацией NotBlank
end note
  
class CreateUserRequestMessage <<concrete>> {
{field} -username : String
}
note left of CreateUserRequestMessage::username
  поле помечено аннотацией NotBlank
end note

CreateGameRequestMessage ..|> BaseRequestMessage #green
CreateUserRequestMessage ..|> BaseRequestMessage #green

@enduml
```

```
@startuml layeredTest
hide empty members
set separator none
skinparam componentStyle uml2

skinparam component {
  BorderColor #grey
  BackgroundColor #white
}

skinparam class {
  BorderColor #grey
  BackgroundColor #white
}

package ru.cbr.siberian.sea.battle.configuration {
    class WebSocketConfiguration
}

package ru.cbr.siberian.sea.battle.acl {
    class MatchMapper

}

package ru.cbr.siberian.sea.battle.controller {
    class GameController
}

package ru.cbr.siberian.sea.battle.repository {
    interface MatchRepository
}

package ru.cbr.siberian.sea.battle.service {
    class MatchService
    class SeaBattleService
}

GameController -down-> SeaBattleService #green

MatchService -down-> MatchMapper #green
MatchService -down-> MatchRepository #green


GameController -up-> MatchRepository #crimson
note right on link #crimson: Доступ в обход слоя запрещен

MatchService -up--> GameController #crimson
note right on link #crimson: Доступ в верхний слой запрещен


@enduml
```

```
@startuml beFreeOfCyclesTest
hide empty members
set separator none
skinparam componentStyle uml2

skinparam component {
  BorderColor #grey
  BackgroundColor #white
}

skinparam class {
  BorderColor #grey
  BackgroundColor #white
}

package ru.cbr.siberian.sea.battle.acl {
    class MatchMapper
}

package ru.cbr.siberian.sea.battle.configuration {
    class WebSocketConfiguration
}

package ru.cbr.siberian.sea.battle.controller {
    class GameController
}

package ru.cbr.siberian.sea.battle.dao {
    class MatchDao
}

package ru.cbr.siberian.sea.battle.model {
    class Match
}

package ru.cbr.siberian.sea.battle.repository {
    interface MatchRepository
}

package ru.cbr.siberian.sea.battle.service {
    class MatchService
    class SeaBattleService
}

GameController -down-> SeaBattleService #green

MatchService -down-> MatchMapper #green
MatchService -down-> MatchRepository #green
MatchRepository -down-> MatchDao #green
MatchMapper -down-> MatchDao #green
MatchMapper -down-> Match #green
MatchMapper --> SeaBattleService #crimson
note right on link #crimson: Комбинация создает цикл

@enduml
```

```
@startuml lakosMetricsTest
skinparam componentStyle uml2
skinparam component {
  BorderColor #grey
  BackgroundColor #white
}
skinparam legend {
  BackgroundColor #lightyellow
}

[Component ru.cbr.siberian.sea.battle.acl\nDependsOn: 3] as acl
[Component ru.cbr.siberian.sea.battle.configuration\nDependsOn: 1] as configuration
[Component ru.cbr.siberian.sea.battle.controller\nDependsOn: 7] as controller
[Component ru.cbr.siberian.sea.battle.dao\nDependsOn: 1] as dao
[Component ru.cbr.siberian.sea.battle.model\nDependsOn: 1] as model
[Component ru.cbr.siberian.sea.battle.repository\nDependsOn: 2] as repository
[Component ru.cbr.siberian.sea.battle.service\nDependsOn: 6] as service


controller --> service
service --> acl
service --> repository
repository --> dao
acl --> dao
acl --> model

legend
| <b>CCD</b>  | 21   |
| <b>ACD</b>  | 3  |
| <b>RACD</b> | 0.42  |
| <b>NCCD</b> | 1.235 |
endlegend

@enduml
```


```
@startuml uml

interface ru.cbr.siberian.sea.battle.repository.MatchRepository {
}

ru.cbr.siberian.sea.battle.controller.GameController *--  ru.cbr.siberian.sea.battle.service.SeaBattleService : seaBattleService
ru.cbr.siberian.sea.battle.service.SeaBattleService *--  ru.cbr.siberian.sea.battle.service.MatchService : matchService
ru.cbr.siberian.sea.battle.service.SeaBattleService *--  ru.cbr.siberian.sea.battle.service.GameService : gameService



ru.cbr.siberian.sea.battle.service.GameService *--  ru.cbr.siberian.sea.battle.model.game.WarshipDescription : warshipDescriptionsForMini
ru.cbr.siberian.sea.battle.service.GameService *--  ru.cbr.siberian.sea.battle.model.game.WarshipDescription : warshipDescriptionsForRegular
ru.cbr.siberian.sea.battle.service.GameService *--  ru.cbr.siberian.sea.battle.acl.GameMapper : gameMapper

ru.cbr.siberian.sea.battle.service.MatchService *--  ru.cbr.siberian.sea.battle.repository.MatchRepository : matchRepository
ru.cbr.siberian.sea.battle.service.MatchService *--  ru.cbr.siberian.sea.battle.acl.MatchMapper : matchMapper
ru.cbr.siberian.sea.battle.acl.MatchMapper -- ru.cbr.siberian.sea.battle.dao.MatchDao: link
ru.cbr.siberian.sea.battle.acl.MatchMapper -- ru.cbr.siberian.sea.battle.model.Match: link
ru.cbr.siberian.sea.battle.repository.MatchRepository --  ru.cbr.siberian.sea.battle.dao.MatchDao : link

@enduml
```

</div>


 ### Проверка от каких пакетов не зависит ACL
![asd](../../../target/generated-diagrams/aclPackageNoDependencyTest.svg)

```java
JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = noClasses().that().resideInAPackage(Layer.ACL.getPackageName())
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
                Layer.CONFIGURATION.getPackageName(),
                Layer.CONTROLLER.getPackageName(),
                Layer.REPOSITORY.getPackageName(),
                Layer.SERVICE.getPackageName()
        );

rule.check(importedClasses);
```

### Проверка от каких пакетов зависит ACL
![asd](../../../target/generated-diagrams/aclPackageDependencyTest.svg)

```java
JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().resideInAPackage(Layer.ACL.getPackageName())
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(Layer.MODEL.getPackageName(), Layer.DAO.getPackageName());

rule.check(importedClasses);
```

### Проверка кто зависит от пакета ACL
![asd](../../../target/generated-diagrams/aclPackageHaveDependencyTest.svg)
```java
JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().resideInAPackage(Layer.ACL.getPackageName())
        .should()
        .onlyHaveDependentClassesThat()
        .resideInAnyPackage(
                Layer.SERVICE.getPackageName());

rule.check(importedClasses);
```

### Проверка кто не зависит от пакета ACL
![asd](../../../target/generated-diagrams/aclPackageHaveNoDependencyTest.svg)
```java
JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = noClasses().that().resideInAPackage(Layer.ACL.getPackageName())
        .should()
        .onlyHaveDependentClassesThat()
        .resideInAnyPackage(Layer.CONFIGURATION.getPackageName(),
                Layer.CONTROLLER.getPackageName(),
                Layer.DAO.getPackageName(),
                Layer.MODEL.getPackageName(),
                Layer.REPOSITORY.getPackageName());
        rule.check(importedClasses);
        
        
```

### Проверка кто зависит от класса GameMapper
![asd](../../../target/generated-diagrams/matchMapperClassDependencyTest.svg)
```java
JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().haveNameMatching(MatchMapper.class.getName())
        .should()
        .onlyHaveDependentClassesThat().haveNameMatching(MatchService.class.getName());

rule.check(importedClasses);
```


###  Проверка в пакете ACL все классы с постфиксом Mapper
![asd](../../../target/generated-diagrams/gameMapperClassDependencyTest.svg)
```java
JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");

ArchRule rule = classes().that().haveSimpleNameEndingWith("Mapper")
        .should()
        .resideInAPackage(Layer.ACL.getPackageName());

rule.check(importedClasses);
```

###  Проверка все классы реализующие интерфейс BaseRequestMessage должны иметь постфикс RequestMessage
![asd](../../../target/generated-diagrams/implementBaseRequestMessageTest.svg)
```java
JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().implement(BaseRequestMessage.class)
        .should().haveSimpleNameEndingWith("RequestMessage");

rule.check(importedClasses);
```

###  Проверка все поля классов реализующие интерфейс BaseRequestMessage используют аннотации проверки NotBlank или NotNull
![asd](../../../target/generated-diagrams/annotationBaseRequestMessageTest.svg)
```java
JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().areAssignableTo(BaseRequestMessage.class)
        .should().onlyAccessFieldsThat(are(annotatedWith(NotBlank.class)).or(annotatedWith(NotNull.class)));

rule.check(importedClasses);
```
###  Проверка слоев
![asd](../../../target/generated-diagrams/layeredTest.svg)
```java

JavaClasses importPackages = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");

Architectures.LayeredArchitecture layeredArchitecture = layeredArchitecture().consideringAllDependencies()
        .layer("acl").definedBy("ru.cbr.siberian.sea.battle.acl..")
        .layer("configuration").definedBy("ru.cbr.siberian.sea.battle.configuration..")
        .layer("controller").definedBy("ru.cbr.siberian.sea.battle.controller..")
        .layer("repository").definedBy("ru.cbr.siberian.sea.battle.repository..")
        .layer("service").definedBy("ru.cbr.siberian.sea.battle.service..")

        .whereLayer("acl").mayOnlyBeAccessedByLayers("service")
        .whereLayer("configuration").mayNotBeAccessedByAnyLayer()
        .whereLayer("controller").mayNotBeAccessedByAnyLayer()
        .whereLayer("repository").mayOnlyBeAccessedByLayers("service")
        .whereLayer("service").mayOnlyBeAccessedByLayers("controller");
layeredArchitecture.check(importPackages);
JavaClasses importPackages = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");

Architectures.LayeredArchitecture layeredArchitecture = layeredArchitecture().consideringAllDependencies()
        .layer(Layer.ACL.name()).definedBy("ru.cbr.siberian.sea.battle.acl..")
        .layer(Layer.CONFIGURATION.name()).definedBy("ru.cbr.siberian.sea.battle.configuration..")
        .layer(Layer.CONTROLLER.name()).definedBy("ru.cbr.siberian.sea.battle.controller..")
        .layer(Layer.REPOSITORY.name()).definedBy("ru.cbr.siberian.sea.battle.repository..")
        .layer(Layer.SERVICE.name()).definedBy("ru.cbr.siberian.sea.battle.service..")

        .whereLayer(Layer.ACL.name()).mayOnlyBeAccessedByLayers(Layer.SERVICE.name())
        .whereLayer(Layer.CONFIGURATION.name()).mayNotBeAccessedByAnyLayer()
        .whereLayer(Layer.CONTROLLER.name()).mayNotBeAccessedByAnyLayer()
        .whereLayer(Layer.REPOSITORY.name()).mayOnlyBeAccessedByLayers(Layer.SERVICE.name())
        .whereLayer(Layer.SERVICE.name()).mayOnlyBeAccessedByLayers(Layer.CONTROLLER.name());
layeredArchitecture.check(importPackages);
```

###  Проверка циклов
![asd](../../../target/generated-diagrams/beFreeOfCyclesTest.svg)
```java
JavaClasses importPackages = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
SliceRule sliceRule = slices()
        .matching("ru.cbr.siberian.sea.battle.(*)..")
        .should()
        .beFreeOfCycles();
sliceRule.check(importPackages);
```

###  Проверка метрик 
![asd](../../../target/generated-diagrams/lakosMetricsTest.svg)
```java
 JavaClasses importPackages = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");

Set<JavaPackage> subpackages = importPackages.getPackage("ru.cbr.siberian.sea.battle").getSubpackages();
MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(metricsComponents);

assertTrue(metrics.getCumulativeComponentDependency() <= 21, "CCD - Сумма зависимомтей всех компонентов " + metrics.getCumulativeComponentDependency());
assertTrue(metrics.getAverageComponentDependency() <= 3, "ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());
```



![asd](../../../target/generated-diagrams/uml.svg)