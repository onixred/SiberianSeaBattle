Regular **Markdown** here.

<div hidden>

```
@startuml AclPackageNoDependencyTest
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
@startuml AclPackageDependencyTest
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
@startuml AclPackageHaveDependencyTest
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
@startuml AclPackageHaveNoDependencyTest
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
![asd](/home/mas/IdeaProjects/SiberianSeaBattle2/target/generated-diagrams/AclPackageNoDependencyTest.svg)
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
![asd](/home/mas/IdeaProjects/SiberianSeaBattle2/target/generated-diagrams/AclPackageDependencyTest.svg)

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
![asd](/home/mas/IdeaProjects/SiberianSeaBattle2/target/generated-diagrams/AclPackageHaveDependencyTest.svg)
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
![asd](/home/mas/IdeaProjects/SiberianSeaBattle2/target/generated-diagrams/AclPackageHaveNoDependencyTest.svg)
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