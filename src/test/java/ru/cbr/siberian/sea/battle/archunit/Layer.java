/*
Copyright 2023 Contributors to the Sports-club

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package ru.cbr.siberian.sea.battle.archunit;

import lombok.RequiredArgsConstructor;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 23.10.2024
 */

@RequiredArgsConstructor
public enum Layer {

    ACL,
    CONFIGURATION,
    CONTROLLER,
    DAO,
    MODEL,
    REPOSITORY,
    SERVICE;

   public String getPackageName() {
       return ".." + name().toLowerCase() + "..";
   }

    public String getComponentIdentifier(String packages) {
        return packages + "." + name().toLowerCase();
    }

}
