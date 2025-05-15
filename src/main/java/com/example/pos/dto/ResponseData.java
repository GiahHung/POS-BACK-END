package com.example.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class ResponseData<T> {
    private int errCode;
    private String errMessage;
    private T data;

    public ResponseData() {
    }

    public ResponseData(int errCode, String errMessage, T data) {
        this.errCode = errCode;
        this.errMessage = errMessage;
        this.data = data;
    }
    
   
}
