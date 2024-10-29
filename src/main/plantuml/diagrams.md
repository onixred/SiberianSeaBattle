Regular **Markdown** here.

<div hidden>

```
@startuml aclPackageNoDependencyTest
skinparam componentStyle uml2
skinparam component {
BorderColor #grey
BackgroundColor #white
}
[acl] --[#green]right-->  [model]:allowed
[acl] --[#green]left-->  [dao]:allowed
[acl] --[#crimson]-->  [configuration]
note top on link #crimson: forbidden
[acl] --[#crimson]-->  [controller]
note top on link #crimson: forbidden
[acl] --[#crimson]-->  [repository]
note top on link #crimson: forbidden
[acl] --[#crimson]-->  [service]
note top on link #crimson: forbidden
@enduml
```



```
@startuml aclPackageDependencyTest
skinparam componentStyle uml2
skinparam component {
BorderColor #grey
BackgroundColor #white
}

[acl] --[#green]right-->  [model]:allowed
[acl] --[#green]right-->  [dao]:allowed
[acl] --[#crimson]left--> [service]
note top on link #crimson: forbidden
@enduml
```

```
@startuml aclPackageHaveDependencyTest
skinparam componentStyle uml2
skinparam component {
BorderColor #grey
BackgroundColor #white
}

[service] --[#green]right-->  [acl]:allowed
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

[service] --[#green]right-->  [acl]:allowed
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
note top on link #crimson: forbidden

MatchService --> MatchMapper #green
note left on link #green: allowed
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

note "resides in service package" as ServicePackage #crimson
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

note right on link #crimson: Has wrong name
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

MatchMapper -down-> Match #green
MatchMapper -down-> MatchDao #green

MatchRepository -down-> MatchDao #green

GameController -up-> MatchRepository #crimson
note right on link #crimson: Доступ в обход слоя запрещен

MatchService -up--> GameController #crimson
note right on link #crimson: Доступ в верхний слой запрещен


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

ArchRule rule = classes().that().haveNameMatching(".*Mapper")
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
        .layer("dao").definedBy("ru.cbr.siberian.sea.battle.dao..")
        .layer("model").definedBy("ru.cbr.siberian.sea.battle.model..")
        .layer("repository").definedBy("ru.cbr.siberian.sea.battle.repository..")
        .layer("service").definedBy("ru.cbr.siberian.sea.battle.service..")

        .whereLayer("acl").mayOnlyBeAccessedByLayers("service")
        .whereLayer("configuration").mayNotBeAccessedByAnyLayer()
        .whereLayer("controller").mayNotBeAccessedByAnyLayer()
        .whereLayer("dao").mayOnlyBeAccessedByLayers("repository", "acl")
        .whereLayer("model").mayOnlyBeAccessedByLayers("service",  "acl",  "controller")
        .whereLayer("repository").mayOnlyBeAccessedByLayers("service")
        .whereLayer("service").mayOnlyBeAccessedByLayers("controller");
layeredArchitecture.check(importPackages);
```


![asd](../../../target/generated-diagrams/uml.svg)