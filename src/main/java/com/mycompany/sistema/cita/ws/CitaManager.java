package com.mycompany.sistema.cita.ws;

import com.mycompany.sistema.cita.ws.dto.CitaDTO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class CitaManager {

    @PersistenceContext(unitName = "citasPU")
    private EntityManager em;

    public List<CitaDTO> listarCitas() {
        try {
            // Consulta JPQL mejorada con ORDER BY
            List<Object[]> resultados = em.createQuery(
                "SELECT c.id, s.nombres, s.apellidos, e.nombre, m.nombre, h.inicio, h.fin, ea.nombre, c.prioridad, c.posicionCola, c.notas, c.creadaEn " +
                "FROM Cita c " +
                "JOIN c.solicitante s " +
                "JOIN c.empresa e " +
                "JOIN c.motivo m " +
                "JOIN c.horario h " +
                "JOIN c.estado ea " +
                "ORDER BY c.creadaEn DESC",  // Ordenar por fecha de creación
                Object[].class
            ).getResultList();

            System.out.println("Citas encontradas: " + resultados.size()); // Debug

            // Mapear a lista de CitaDTO
            return resultados.stream().map(r -> {
                CitaDTO dto = new CitaDTO();
                dto.setCitaId((Long) r[0]);
                
                // Combinar nombre y apellido del solicitante
                String nombres = r[1] != null ? r[1].toString() : "";
                String apellidos = r[2] != null ? r[2].toString() : "";
                dto.setSolicitante((nombres + " " + apellidos).trim());
                
                dto.setEmpresa(r[3] != null ? r[3].toString() : "N/A");
                dto.setMotivo(r[4] != null ? r[4].toString() : "N/A");
                dto.setInicio((java.time.LocalDateTime) r[5]);
                dto.setFin((java.time.LocalDateTime) r[6]);
                dto.setEstado(r[7] != null ? r[7].toString() : "N/A");
                dto.setPrioridad(r[8] != null ? (Integer) r[8] : 0);
                dto.setPosicionCola(r[9] != null ? (Integer) r[9] : null);
                dto.setNotas(r[10] != null ? r[10].toString() : "");
                dto.setCreadaEn((java.time.LocalDateTime) r[11]);
                return dto;
            }).collect(Collectors.toList());
            
        } catch (Exception e) {
            System.err.println("Error en listarCitas: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzar la excepción para ver el error en el bean
        }
    }
}