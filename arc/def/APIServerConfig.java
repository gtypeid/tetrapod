package com.kosta.hcr.arc.def;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class APIServerConfig {
    private String endPoint;
}
