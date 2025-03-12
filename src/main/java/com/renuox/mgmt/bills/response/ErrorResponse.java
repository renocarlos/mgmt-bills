package com.renuox.mgmt.bills.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private int httpStatus;
    private String code;
    private LocalDateTime timestamp;
    private String detail;
    private String message;
}
