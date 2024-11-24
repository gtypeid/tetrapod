package com.kosta.common.core.data.ctx;


import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusContext {
    private String status;
    private String date;

}
