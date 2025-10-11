package com.mycompany.sistema.cita.api;


import com.mycompany.sistema.cita.api.dto.CitaCreateRequest;
import com.mycompany.sistema.cita.api.dto.CitaCreateResponse;
import com.mycompany.sistema.cita.service.CitaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/api/citas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CitaResource {

    @Inject
    CitaService service;

    @POST
    public Response create(CitaCreateRequest req, @Context UriInfo uriInfo) {
        if (req == null) throw new BadRequestException("Body requerido");
        Long id = service.crearCita(
                req.solicitanteId, req.empresaId, req.motivoId, req.horarioId,
                req.estadoId, req.prioridad, req.posicionCola, req.notas
        );
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id));
        return Response.created(ub.build())
                .entity(new CitaCreateResponse(id, "Cita creada"))
                .build();
    }
}
