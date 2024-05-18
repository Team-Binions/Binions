package com.beanions.common.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentsDTO {

    private Integer commentCode;
    private Integer postCode;
    private Integer memberCode;
    private String commentContext;
    private Date commentDate;
}
