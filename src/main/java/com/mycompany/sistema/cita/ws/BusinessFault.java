package com.mycompany.sistema.cita.ws;

import jakarta.xml.ws.WebFault;

@WebFault(name = "BusinessFault")
public class BusinessFault extends Exception {
    private static final long serialVersionUID = 1L;

    public BusinessFault(String message) {
        super(message);
    }
}
