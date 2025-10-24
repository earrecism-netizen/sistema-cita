package com.mycompany.sistema.cita.web;

import com.mycompany.sistema.cita.domain.*;
import com.mycompany.sistema.cita.service.CitaService;
import com.mycompany.sistema.cita.ws.CitaManager;
import com.mycompany.sistema.cita.ws.dto.CitaDTO;
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

    @Inject
    CitaManager citaManager; // 🔹 Usado para obtener lista de citas

    // Listas para combos
    private List<Solicitante> solicitantes;
    private List<Empresa> empresas;
    private List<Motivo> motivos;
    private List<Horario> horarios;
    private List<EstadoAtencion> estados;

    // Lista de citas (para mostrar en tabla)
    private List<CitaDTO> citas;

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
    private CitaDTO citaSeleccionada;
    private Long creadoId;
    private String errorMsg;

    @PostConstruct
    public void init() {
        cargarCombos();
        cargarCitas(); // 🔹 Cargar citas al iniciar
    }

    public void cargarCombos() {
        solicitantes = em.createQuery("SELECT s FROM Solicitante s ORDER BY s.id", Solicitante.class).getResultList();
        empresas     = em.createQuery("SELECT e FROM Empresa e ORDER BY e.id", Empresa.class).getResultList();
        motivos      = em.createQuery("SELECT m FROM Motivo m ORDER BY m.id", Motivo.class).getResultList();
        horarios     = em.createQuery("SELECT h FROM Horario h ORDER BY h.id", Horario.class).getResultList();
        estados      = em.createQuery("SELECT e FROM EstadoAtencion e ORDER BY e.id", EstadoAtencion.class).getResultList();
    }

    public void cargarCitas() {
        try {
            citas = citaManager.listarCitas();
            System.out.println("Citas cargadas en bean: " + (citas != null ? citas.size() : "null"));
        } catch (Exception e) {
            errorMsg = "Error al cargar citas: " + e.getMessage();
            e.printStackTrace();
            citas = List.of(); // Lista vacía en caso de error
        }
    }

    public void guardar() {
        try {
            errorMsg = null;
            creadoId = service.crearCita(
                    solicitanteId, empresaId, motivoId, horarioId, estadoId,
                    prioridad, posicionCola, notas
            );
            // Limpiar formulario y actualizar tabla
            notas = null;
            posicionCola = null;
            cargarCitas();
        } catch (Exception ex) {
            errorMsg = ex.getMessage();
            creadoId = null;
        }
    }
    
    public void seleccionarCita(CitaDTO cita) {
        this.citaSeleccionada = cita;
        this.solicitanteId = cita.getSolicitanteId(); 
        this.empresaId     = cita.getEmpresaId();
        this.motivoId      = cita.getMotivoId();
        this.horarioId     = cita.getHorarioId();
        this.estadoId      = cita.getEstadoId();
        this.prioridad     = cita.getPrioridad();
        this.posicionCola  = cita.getPosicionCola();
        this.notas         = cita.getNotas();
    }

    
    public void actualizar() {
        try {
            service.actualizarCita(
            citaSeleccionada.getCitaId(),
            solicitanteId,
            empresaId,
            motivoId,
            horarioId,
            estadoId,
            prioridad,
            posicionCola,
            notas
            );
            limpiarFormulario();
            cargarCitas();
        } catch (Exception e) {
            errorMsg = "Error al actualizar: " + e.getMessage();
        }
    }

    public void limpiarFormulario() {
        citaSeleccionada = null;
        notas = null;
        prioridad = null;
        posicionCola = null;
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

    
    public List<CitaDTO> getCitas() { return citas; }
    public CitaDTO getCitaSeleccionada() { return citaSeleccionada; }
    public void setCitaSeleccionada(CitaDTO c) { this.citaSeleccionada = c; }
    
}
