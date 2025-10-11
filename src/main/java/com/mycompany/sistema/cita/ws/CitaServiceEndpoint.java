package com.mycompany.sistema.cita.ws;

import com.mycompany.sistema.cita.ws.dto.*;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jws.WebService;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@WebService(
    serviceName = "CitaService",
    portName = "CitaServicePort",
    endpointInterface = "com.mycompany.sistema.cita.ws.soap.CitaService",
    targetNamespace = "http://ws.cita.sistema.mycompany.com/"
)
public class CitaServiceEndpoint implements CitaService {

    @Inject
    CitaRepository repo;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public CitaList listarCitas() throws BusinessFault {
        try {
            List<WsCitaDto> rows = repo.findAll();
            return new CitaList(rows.stream().map(this::toSoap).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new BusinessFault("Error al listar citas: " + e.getMessage());
        }
    }

    @Override
    public WsCitaDto obtenerCita(long citaId) throws BusinessFault {
        try {
            var dto = repo.findById(citaId);
            if (dto == null) throw new BusinessFault("Cita no encontrada: id=" + citaId);
            return toSoap(dto);
        } catch (BusinessFault bf) {
            throw bf;
        } catch (Exception e) {
            throw new BusinessFault("Error al obtener cita: " + e.getMessage());
        }
    }

    @Override
    public WsCitaDto crearCita(CitaCreateRequest request) throws BusinessFault {
        try {
            long id = repo.create(request);
            var dto = repo.findById(id);
            return toSoap(dto);
        } catch (Exception e) {
            throw new BusinessFault("No se pudo crear la cita: " + e.getMessage());
        }
    }

    @Override
    public WsCitaDto actualizarCita(long citaId, CitaUpdateRequest request) throws BusinessFault {
        try {
            boolean ok = repo.update(citaId, request);
            if (!ok) throw new BusinessFault("Cita no encontrada para actualizar: id=" + citaId);
            var dto = repo.findById(citaId);
            return toSoap(dto);
        } catch (BusinessFault bf) {
            throw bf;
        } catch (SQLException e) {
            throw new BusinessFault("No se pudo actualizar la cita: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarCita(long citaId) throws BusinessFault {
        try {
            boolean ok = repo.delete(citaId);
            if (!ok) throw new BusinessFault("Cita no encontrada para eliminar: id=" + citaId);
            return true;
        } catch (BusinessFault bf) {
            throw bf;
        } catch (Exception e) {
            throw new BusinessFault("No se pudo eliminar la cita: " + e.getMessage());
        }
    }

    // --- Adaptador: del DTO JDBC a DTO SOAP (string para fecha) --- //
    private WsCitaDto toSoap(CitaDto src) {
        
        String creada = src.creadaEn()== null ? null : FMT.format(src.creadaEn());
        return new WsCitaDto(
            src.citaId(),
            src.solicitanteId(),
            src.empresaId(),
            src.motivoId(),
            src.horarioId(),
            src.estadoId(),
            src.prioridad(),
            src.posicionCola(),
            src.notas(),
            creada
        );
        // Si tu repo ya usa com.mycompany.sistema.cita.ws.dto.CitaDto, ajusta las importaciones.
    }
}

