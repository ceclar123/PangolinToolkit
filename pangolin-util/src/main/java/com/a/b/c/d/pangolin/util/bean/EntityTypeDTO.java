package com.a.b.c.d.pangolin.util.bean;

import java.io.Serializable;
import java.util.Objects;

public class EntityTypeDTO implements Serializable {
    private static final long serialVersionUID = -7481218401444861131L;

    private String type;
    private String name;

    public EntityTypeDTO() {
    }

    public EntityTypeDTO(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityTypeDTO that = (EntityTypeDTO) o;
        return Objects.equals(type, that.type) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }
}
