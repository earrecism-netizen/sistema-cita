package com.mycompany.sistema.cita.service;

import com.mycompany.sistema.cita.domain.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CitaService {

    @PersistenceContext(unitName = "citasPU")
    EntityManager em;

    @Transactional
    public Long crearCita(Integer solicitanteId,
                          Integer empresaId,
                          Integer motivoId,
                          Integer horarioId,
                          Integer estadoId,
                          Integer prioridad,
                          Integer posicionCola,
                          String notas) {

        // Cargar entidades
        Solicitante solicitante = em.find(Solicitante.class, solicitanteId);
        Empresa empresa = em.find(Empresa.class, empresaId);
        Motivo motivo = em.find(Motivo.class, motivoId);
        Horario horario = em.find(Horario.class, horarioId);
        EstadoAtencion estado = em.find(EstadoAtencion.class, estadoId);

        if (solicitante == null || empresa == null || motivo == null || horario == null || estado == null) {
            throw new IllegalArgumentException("IDs inválidos para crear cita");
        }

        // Validación simple: horario disponible
        if ("BLOQUEADO".equalsIgnoreCase(horario.getEstadoSlot())) {
            throw new IllegalStateException("El horario está BLOQUEADO");
        }

        // (Opcional) Validar capacidad del horario vs citas ya asignadas
        long yaAsignadas = em.createQuery(
                "SELECT COUNT(c) FROM Cita c WHERE c.horario.id = :hid", Long.class)
                .setParameter("hid", horarioId)
                .getSingleResult();

        if (yaAsignadas >= horario.getCapacidad()) {
            throw new IllegalStateException("El horario ya alcanzó su capacidad");
        }

        Cita c = new Cita();
        c.setSolicitante(solicitante);
        c.setEmpresa(empresa);
        c.setMotivo(motivo);
        c.setHorario(horario);
        c.setEstado(estado);
        c.setPrioridad(prioridad != null ? prioridad : 0);
        c.setPosicionCola(posicionCola);
        c.setNotas(notas);

        em.persist(c);
        em.flush();
        return c.getId();
    }
}