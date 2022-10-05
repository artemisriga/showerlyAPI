package com.showerly.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@Entity
public class Shower {
    @Id
    private String id;
    private String token;

    @OneToMany
    private Set<DataModel> dataModels;
}
