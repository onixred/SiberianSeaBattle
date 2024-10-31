package ru.cbr.siberian.feature.first.sea.battle.common;

import jakarta.persistence.Column;

import java.time.ZonedDateTime;

public abstract class AbstractDao {

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

}
