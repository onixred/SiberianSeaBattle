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
package ru.onixred.siberian.sea.battle.layer.archunit.simple;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 23.10.2024
 */

@RequiredArgsConstructor
public enum Layer {

    ACL("acl"),
    CONFIGURATION("configuration"),
    CONTROLLER("controller"),
    DAO("dao"),
    MODEL("model"),
    MODEL_ENUMERATION("model.enumeration"),
    MODEL_GAME("model.game"),
    MODEL_MESSAGE("model.message"),
    REPOSITORY("repository"),
    SERVICE("service");

    @Getter
    private final String name;

    public static final String BASE_PACKAGE = "ru.onixred.siberian.sea.battle.layer.";

    public String getPackageName() {
        return BASE_PACKAGE + name + "..";
    }

    public String getComponentIdentifier() {
        return BASE_PACKAGE + name;
    }

}
