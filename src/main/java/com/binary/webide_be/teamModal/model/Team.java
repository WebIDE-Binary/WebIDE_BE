package com.binary.webide_be.teamModal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Team { //ERD대로 넣어준다
    @Getter
    @Setter
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int participant;

    public void setName(Object name) {
    }

    private enum role {

    }

}