package ru.sumenkov.SiberianSeaBattle.dao;

import jakarta.persistence.Column;

import java.time.ZonedDateTime;

public abstract class AbstractDao {

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

}
