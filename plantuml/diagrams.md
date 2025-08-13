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
@startuml shouldFollowNamingConventionTest
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
class AclService
}

note "Неправильное имя у класса" as ServicePackage #crimson
AclService .. ServicePackage


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
@startuml shouldNotUseFieldInjectionTest
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

class GameController <<concrete>> {
{field} -matchService : MatchService
}
note right of GameController::matchService
  поле не помечено аннотацией Autowired
end note

class MatchService <<concrete>> {

}

GameController --> MatchService #green


@enduml
```

```
@startuml shouldNotCreateMatchInMatchMapperTest
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

class MatchMapper <<concrete>> {
}
note "Запрет на вызов конструктора для класса Match" as creteMatch 
MatchMapper .. creteMatch

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
@startuml onlyAllowedDependenciesInServicePackageTest
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
@startuml componentDependencyMetricsTest
skinparam componentStyle uml2
skinparam component {
  BorderColor #grey
  BackgroundColor #white
}
skinparam legend {
  BackgroundColor #lightyellow
}

[Component ru.cbr.siberian.sea.battle.acl \nCe: 2\nCa: 1\nI: 0.66] as acl
[Component ru.cbr.siberian.sea.battle.configuration \nCe: 0\nCa: 0\nI: 1] as configuration
[Component ru.cbr.siberian.sea.battle.controller \nCe: 2\nCa: 0\nI: 1] as controller
[Component ru.cbr.siberian.sea.battle.dao \nCe: 1\nCa: 2\nI: 0.33] as dao
[Component ru.cbr.siberian.sea.battle.model \nCe: 0\nCa: 5\nI: 0] as model
[Component ru.cbr.siberian.sea.battle.repository \nCe: 2\nCa: 1\nI: 0.66] as repository
[Component ru.cbr.siberian.sea.battle.service \nCe: 3\nCa: 1\nI: 0.75] as service


controller --> service
service --> acl
service --> repository
repository --> dao
acl --> dao
acl --> model

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


[Component feature.first.sea.battle.game \nCe: 0\nCa: 1\nI: 0] as game
[Component feature.first.sea.battle.match \nCe: 4\nCa: 0\nI:1] as match
[Component feature.first.sea.battle.player \nCe: 2\nCa: 1\nI: 0.66] as player
[Component feature.first.sea.battle.common \nCe: 0\nCa: 3\nI: 0] as common
[Component feature.first.sea.battle.configuration \nCe: 0\nCa: 0\nI: 1] as configuration
[Component feature.first.sea.battle.notification \nCe: 1\nCa: 2\nI: 0.33] as notification


notification --> common

match --> common
match --> player
match --> game
match --> notification

player --> common
player --> notification

@enduml
```

```
@startuml annotationPreAuthorizeTest
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

class GameController <<concrete>> {
void : createUser() 
}
note right of GameController::createUser()
  метод помечен аннотацией PreAuthorize
end note

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


```
@startuml uml_real
skinparam componentStyle uml2
skinparam component {
BorderColor #grey
BackgroundColor #white
}

[controller] <<..ru.cbr.siberian.sea.battle.controller..>>
[dao] <<..ru.cbr.siberian.sea.battle.dao..>>
[model] <<..ru.cbr.siberian.sea.battle.model..>>
[service] <<..ru.cbr.siberian.sea.battle.service..>>
[acl] <<..ru.cbr.siberian.sea.battle.acl..>>
[repository] <<..cbr.siberian.sea.battle.repository..>>

[controller] --> [model]
[controller] --> [service]

[service] --> [model]
[service] --> [acl]
[service] --> [repository]

[dao] --> [model]

[repository] --> [dao]
[repository] --> [model]

[acl] --> [model]
[acl] --> [dao]

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

 ### Проверка от каких пакетов не зависит ACL
![asd](../../../target/generated-diagrams/aclPackageNoDependencyTest.svg)

```java
JavaClasses javaClasses = new ClassFileImporter()
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

rule.check(javaClasses);
```

### Проверка от каких пакетов зависит ACL
![asd](../../../target/generated-diagrams/aclPackageDependencyTest.svg)

```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().resideInAPackage(Layer.ACL.getPackageName())
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(Layer.MODEL.getPackageName(), Layer.DAO.getPackageName());

rule.check(javaClasses);
```

### Проверка кто зависит от пакета ACL
![asd](../../../target/generated-diagrams/aclPackageHaveDependencyTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().resideInAPackage(Layer.ACL.getPackageName())
        .should()
        .onlyHaveDependentClassesThat()
        .resideInAnyPackage(
                Layer.SERVICE.getPackageName());

rule.check(javaClasses);
```

### Проверка кто не зависит от пакета ACL
![asd](../../../target/generated-diagrams/aclPackageHaveNoDependencyTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
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
        rule.check(javaClasses);
        
        
```

### Проверка кто зависит от класса GameMapper
![asd](../../../target/generated-diagrams/matchMapperClassDependencyTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().haveNameMatching(MatchMapper.class.getName())
        .should()
        .onlyHaveDependentClassesThat().haveNameMatching(MatchService.class.getName());

rule.check(javaClasses);
```


###  Проверка все классы с постфиксом Mapper находятся в пакете ACL
![asd](../../../target/generated-diagrams/gameMapperClassDependencyTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");

ArchRule rule = classes().that().haveSimpleNameEndingWith("Mapper")
        .should()
        .resideInAPackage(Layer.ACL.getPackageName());

rule.check(javaClasses);
```

###  Проверка в пакете ACL все классы с постфиксом Mapper
![asd](../../../target/generated-diagrams/shouldFollowNamingConventionTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().resideInAnyPackage(Layer.ACL.getComponentIdentifier("ru.cbr.siberian.sea.battle"))
        .should()
        .haveSimpleNameEndingWith("Mapper");

rule.check(javaClasses);
```


###  Проверка все классы реализующие интерфейс BaseRequestMessage должны иметь постфикс RequestMessage
![asd](../../../target/generated-diagrams/implementBaseRequestMessageTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().implement(BaseRequestMessage.class)
        .should().haveSimpleNameEndingWith("RequestMessage");

rule.check(javaClasses);
```

###  Проверка все поля классов реализующие интерфейс BaseRequestMessage используют аннотации проверки NotBlank или NotNull
![asd](../../../target/generated-diagrams/annotationBaseRequestMessageTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = classes().that().areAssignableTo(BaseRequestMessage.class)
        .should().onlyAccessFieldsThat(are(annotatedWith(NotBlank.class)).or(annotatedWith(NotNull.class)));

rule.check(javaClasses);
```


###  Проверка на наличие аннотаций Autowired у полей класса
![asd](../../../target/generated-diagrams/shouldNotUseFieldInjectionTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = noFields().should()
        .beAnnotatedWith(Autowired.class);

rule.check(javaClasses);
```

###  Проверка на отсутствие конструктора Match в MatchMapper
![asd](../../../target/generated-diagrams/shouldNotCreateMatchInMatchMapperTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
ArchRule rule = noConstructors().that().areDeclaredInClassesThat()
        .areAssignableTo(Match.class).should().onlyBeCalled().byClassesThat().haveNameMatching(MatchMapper.class.getName());

rule.check(javaClasses);
```

###  Проверка слоев
![asd](../../../target/generated-diagrams/layeredTest.svg)
```java

JavaClasses javaClasses = new ClassFileImporter()
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
layeredArchitecture.check(javaClasses);
```

###  Проверка циклов
![asd](../../../target/generated-diagrams/beFreeOfCyclesTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");
SliceRule sliceRule = slices()
        .matching("ru.cbr.siberian.sea.battle.(*)..")
        .should()
        .beFreeOfCycles();
sliceRule.check(javaClasses);
```

### Проверка внешних библиотек
![asd](../../../target/onlyAllowedDependenciesInServicePackageTest.svg)
```java
    @Test
    @DisplayName("Классы в пакете DAO не должны напрямую зависеть от 'других' внешних библиотек")
    void onlyAllowedDependenciesInServicePackageTest() {

        JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(IMPORT_PACKAGES);

        ArchRule rule = classes().that()
                .resideInAPackage("ru.onixred.siberian.sea.battle.layer.dao..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("java..",
                        "ru.onixred.siberian..",
                        "lombok..",
                        "jakarta.persistence..",
                        "org.hibernate.proxy.."
                ) // Allowed dependency
                .as("Classes in the DAO package should not depend on external 'other' libraries directly");

        rule.check(javaClasses);
    }
}
```




###  Проверка метрик Джона Лакоса на примере layer-first
![asd](../../../target/generated-diagrams/lakosMetricsTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");

Set<JavaPackage> subpackages = javaClasses.getPackage("ru.cbr.siberian.sea.battle").getSubpackages();
MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(metricsComponents);

assertTrue(metrics.getCumulativeComponentDependency() <= 21, "CCD - Сумма зависимостей всех компонентов " + metrics.getCumulativeComponentDependency());
assertTrue(metrics.getAverageComponentDependency() <= 3, "ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());
```

###  Проверка метрик Джона Лакоса на примере feature-first
![asd](../../../target/generated-diagrams/lakosMetricsFeatureFirstTest.svg)
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.feature.first.sea.battle");

Set<JavaPackage> subpackages = javaClasses.getPackage("ru.cbr.siberian.feature.first.sea.battle").getSubpackages();
MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(metricsComponents);

assertTrue(metrics.getCumulativeComponentDependency() <= 15, "CCD - Сумма зависимостей всех компонентов " + metrics.getCumulativeComponentDependency());
assertTrue(metrics.getAverageComponentDependency() <= 2.17, "ACD - CCD деленная на количество всех компонентов " + metrics.getAverageComponentDependency());
```


https://habr.com/ru/articles/772802/
###  Проверка метрик Роберта Мартина «Чистая архитектура» на примере layer-first
![asd](../../../target/generated-diagrams/componentDependencyMetricsTest.svg)

В структуре компонентов нестабильные компоненты должны располагаться сверху, а более стабильные — снизу,
Чем больше I тем не стабильнее компонент.
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.sea.battle");

Set<JavaPackage> subpackages = javaClasses.getPackage("ru.cbr.siberian.sea.battle").getSubpackages();
MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(metricsComponents);

int efferentCoupling = metrics.getEfferentCoupling(Layer.ACL.getComponentIdentifier());
assertTrue(efferentCoupling <= 2, "Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости)" + efferentCoupling);

int afferentCoupling = metrics.getAfferentCoupling(Layer.ACL.getComponentIdentifier());
assertTrue(afferentCoupling <= 1, "Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости)" + afferentCoupling);

double instability = metrics.getInstability(Layer.ACL.getComponentIdentifier());
assertTrue(instability <= 0.7, "I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям (нестабильность)" + instability);
```

###  Проверка метрик Роберта Мартина «Чистая архитектура» на примере feature-first
![asd](../../../target/generated-diagrams/componentDependencyMetricsFeatureFirstTest.svg)

В структуре компонентов нестабильные компоненты должны располагаться сверху, а более стабильные — снизу,
Чем больше I тем не стабильнее компонент.
```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ru.cbr.siberian.feature.first.sea.battle");

Set<JavaPackage> subpackages = javaClasses.getPackage("ru.cbr.siberian.feature.first.sea.battle").getSubpackages();
MetricsComponents<JavaClass> metricsComponents = MetricsComponents.fromPackages(subpackages);
ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(metricsComponents);

int efferentCoupling = metrics.getEfferentCoupling(Feature.Game.getComponentIdentifier());
assertTrue(efferentCoupling <= 0, "Ce - показывает зависимости пакета от внешних пакетов (исходящие зависимости)" + efferentCoupling);

int afferentCoupling = metrics.getAfferentCoupling(Feature.Game.getComponentIdentifier());
assertTrue(afferentCoupling <= 1, "Ca - показывает зависимости внешних пакетов от указанного пакета (входящие зависимости)" + afferentCoupling);

double instability = metrics.getInstability(Feature.Game.getComponentIdentifier());
assertTrue(instability <= 0, "I - Ce / (Ca + Ce), т.е. отношение исходящих зависимостей ко всем зависимостям (нестабильность)" + instability);
```



###  Проверка во всех классах в пакете controller на методах должна стоять аннотация PreAuthorize
![asd](../../../target/generated-diagrams/annotationPreAuthorizeTest.svg)

В структуре компонентов нестабильные компоненты должны располагаться сверху, а более стабильные — снизу,
Чем больше I тем не стабильнее компонент.
```java
@Test
@DisplayName("Проверка во всех классах в пакете controller на методах должна стоять аннотация PreAuthorize")
void annotationPreAuthorizeTest() {
    String importPackages = "ru.cbr.siberian.sea.battle";
    JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(importPackages);

    ArchRule rule = methods().that().arePublic()
            .and().areDeclaredInClassesThat().resideInAPackage("..controller..")
            .should()
            .beAnnotatedWith(PreAuthorize.class);

    rule.check(importedClasses);
}
```

![asd](../../../target/generated-diagrams/uml.svg)
реальные

```java
JavaClasses javaClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages(IMPORT_PACKAGES);
final var diagram = getClass().getClassLoader().getResource("siberian-sea-battle.puml");
ClassesShouldConjunction conjunction = classes()
        .should(adhereToPlantUmlDiagram(diagram, consideringOnlyDependenciesInDiagram()));

conjunction.check(javaClasses);
```

![asd](../../../target/generated-diagrams/uml_real.svg)