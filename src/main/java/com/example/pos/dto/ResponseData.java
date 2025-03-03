package com.example.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData<T> {
    private int errCode;
    private String errMessage;
    private T data;
   
}
