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
package ru.cbr.siberian.sea.battle.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: Сервис проверки данных
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 26.09.2024
 */
@Service
public class ValidatorService {
    private final Validator validator;
    public ValidatorService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Проверка объекта
     * @param object входные данные
     * @return список проблем
     */
    public Set<String> validate(Object object) {
        Set<ConstraintViolation<Object>> errors = validator.validate(object);

        return errors.stream().map(e-> "\nполе " +e.getPropertyPath().toString() +" " + e.getMessage()).collect(Collectors.toSet());
    }
}
