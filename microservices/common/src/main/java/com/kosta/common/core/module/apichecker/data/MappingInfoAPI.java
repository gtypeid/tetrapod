package com.kosta.common.core.module.apichecker.data;


import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class MappingInfoAPI {
    public MappingInfoAPI(){

    }

    private String method;
    private String path;
    private String name;
    private String returnType;
    private List<String> params;

}
