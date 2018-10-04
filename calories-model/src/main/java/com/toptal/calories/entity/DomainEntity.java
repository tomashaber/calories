package com.toptal.calories.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Tomas Haber on 6.7.2015
 */
@MappedSuperclass
public abstract class DomainEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @Version
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
