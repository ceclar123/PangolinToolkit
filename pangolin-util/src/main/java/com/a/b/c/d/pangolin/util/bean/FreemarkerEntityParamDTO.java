package com.a.b.c.d.pangolin.util.bean;

import java.io.Serializable;
import java.util.List;

public class FreemarkerEntityParamDTO implements Serializable {
    private static final long serialVersionUID = -7481218401444861131L;

    private List<EntityTypeDTO> paramList;

    public List<EntityTypeDTO> getParamList() {
        return paramList;
    }

    public void setParamList(List<EntityTypeDTO> paramList) {
        this.paramList = paramList;
    }
}
