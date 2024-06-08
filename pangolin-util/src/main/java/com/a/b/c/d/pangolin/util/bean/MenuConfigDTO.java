package com.a.b.c.d.pangolin.util.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MenuConfigDTO implements Serializable {
    private static final long serialVersionUID = -7481218401444861131L;

    private String id;

    private String name;

    private String type;

    private String fxml;

    private List<MenuConfigDTO> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFxml() {
        return fxml;
    }

    public void setFxml(String fxml) {
        this.fxml = fxml;
    }

    public List<MenuConfigDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuConfigDTO> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuConfigDTO that = (MenuConfigDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(fxml, that.fxml) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, fxml, children);
    }
}
