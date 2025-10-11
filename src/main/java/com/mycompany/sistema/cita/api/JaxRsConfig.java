package com.mycompany.sistema.cita.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class JaxRsConfig extends Application {
    // vacía; Payara detecta los recursos por classpath
}
