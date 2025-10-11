package com.mycompany.sistema.cita.api.dto;


public class CitaCreateResponse {
    public Long id;
    public String message;

    public CitaCreateResponse() {}
    public CitaCreateResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }
}

