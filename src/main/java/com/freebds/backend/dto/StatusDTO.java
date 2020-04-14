package com.freebds.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "Status", description = "The status serie list scraped from http://www.bedetheque.com")
public class StatusDTO {

    private List<String> status;
}
