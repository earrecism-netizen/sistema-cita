package com.mycompany.sistema.cita.api;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/ping-db")
public class PingResource {

    @PersistenceContext
    private EntityManager em;

    @GET
    @Transactional
    public Response ping() {
        try {
            // Ejecuta una consulta mínima
            Object result = em.createNativeQuery("SELECT 1").getSingleResult();
            return Response.ok("Conexión exitosa a la base de datos. Resultado: " + result).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("Error al conectar a la base de datos: " + e.getMessage())
                    .build();
        }
    }
}
