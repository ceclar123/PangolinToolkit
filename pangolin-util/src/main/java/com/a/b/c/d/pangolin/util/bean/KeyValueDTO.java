package com.a.b.c.d.pangolin.util.bean;

import java.io.Serializable;
import java.util.Objects;

public class KeyValueDTO implements Serializable {
    private static final long serialVersionUID = -7481218401444861131L;

    private String key;

    private String value;

    public KeyValueDTO() {
    }

    public KeyValueDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValueDTO that = (KeyValueDTO) o;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
