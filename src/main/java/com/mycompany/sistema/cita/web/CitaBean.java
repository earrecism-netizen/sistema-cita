package com.mycompany.sistema.cita.web;

import com.mycompany.sistema.cita.domain.*;
import com.mycompany.sistema.cita.service.CitaService;  
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Named("citaBean")
@ViewScoped
public class CitaBean implements Serializable {

    @PersistenceContext(unitName = "citasPU")
    EntityManager em;

    @Inject
    CitaService service;

    // Listas para combos
    private List<Solicitante> solicitantes;
    private List<Empresa> empresas;
    private List<Motivo> motivos;
    private List<Horario> horarios;
    private List<EstadoAtencion> estados;

    // Selecciones (IDs)
    private Integer solicitanteId;
    private Integer empresaId;
    private Integer motivoId;
    private Integer horarioId;
    private Integer estadoId;

    // Otros campos
    private Integer prioridad = 0;
    private Integer posicionCola;
    private String notas;

    private Long creadoId;
    private String errorMsg;

    @PostConstruct
    public void init() {
        cargarCombos();
    }

    public void cargarCombos() {
        solicitantes = em.createQuery("SELECT s FROM Solicitante s ORDER BY s.id", Solicitante.class).getResultList();
        empresas     = em.createQuery("SELECT e FROM Empresa e ORDER BY e.id", Empresa.class).getResultList();
        motivos      = em.createQuery("SELECT m FROM Motivo m ORDER BY m.id", Motivo.class).getResultList();
        horarios     = em.createQuery("SELECT h FROM Horario h ORDER BY h.id", Horario.class).getResultList();
        estados      = em.createQuery("SELECT e FROM EstadoAtencion e ORDER BY e.id", EstadoAtencion.class).getResultList();
    }

    public void guardar() {
        try {
            errorMsg = null;
            creadoId = service.crearCita(
                    solicitanteId, empresaId, motivoId, horarioId, estadoId,
                    prioridad, posicionCola, notas
            );
            // Limpiar formulario básico (deja combos como estaban si prefieres)
            notas = null;
            posicionCola = null;
        } catch (Exception ex) {
            errorMsg = ex.getMessage();
            creadoId = null;
        }
    }

    // Getters/Setters
    public List<Solicitante> getSolicitantes() { return solicitantes; }
    public List<Empresa> getEmpresas() { return empresas; }
    public List<Motivo> getMotivos() { return motivos; }
    public List<Horario> getHorarios() { return horarios; }
    public List<EstadoAtencion> getEstados() { return estados; }

    public Integer getSolicitanteId() { return solicitanteId; }
    public void setSolicitanteId(Integer solicitanteId) { this.solicitanteId = solicitanteId; }
    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }
    public Integer getMotivoId() { return motivoId; }
    public void setMotivoId(Integer motivoId) { this.motivoId = motivoId; }
    public Integer getHorarioId() { return horarioId; }
    public void setHorarioId(Integer horarioId) { this.horarioId = horarioId; }
    public Integer getEstadoId() { return estadoId; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }

    public Integer getPrioridad() { return prioridad; }
    public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }
    public Integer getPosicionCola() { return posicionCola; }
    public void setPosicionCola(Integer posicionCola) { this.posicionCola = posicionCola; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public Long getCreadoId() { return creadoId; }
    public String getErrorMsg() { return errorMsg; }
}
