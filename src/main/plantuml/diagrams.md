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
[acl] --[#crimson]-->  [configuration]
note top on link #crimson: forbidden
[acl] --[#crimson]-->  [controller]
note top on link #crimson: forbidden
[acl] --[#crimson]-->  [repository]
note top on link #crimson: forbidden
[acl] --[#crimson]-->  [service]
note top on link #crimson: forbidden
[acl] --[#crimson]-->  [dao]
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
[model] --[#crimson]left--> [acl]
note top on link #crimson: forbidden
@enduml
```

</div>



![](firstDiagram.svg)
![](firstDiagram2.svg)

Some more markdown.