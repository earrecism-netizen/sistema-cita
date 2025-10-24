package com.mycompany.sistema.cita.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "Cita", schema = "citas")
public class Cita {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cita_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Solicitante solicitante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "motivo_id", nullable = false)
    private Motivo motivo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoAtencion estado;

    @Column(name = "prioridad", nullable = false)
    private Integer prioridad = 0;

    @Column(name = "posicion_cola")
    private Integer posicionCola;

    @Column(name = "notas", length = 500)
    private String notas;

    @Column(name = "creada_en", nullable = false)
    private LocalDateTime creadaEn;

    @PrePersist
    void prePersist() { if (creadaEn == null) creadaEn = LocalDateTime.now(); }
    
    private LocalDateTime actualizadaEn;
    private Integer solicitanteId;
    private Integer empresaId;
    private Integer motivoId;
    private Integer horarioId;
    private Integer estadoId;
    
    public Long getId() { return id; }
    public Solicitante getSolicitante() { return solicitante; }
    public Empresa getEmpresa() { return empresa; }
    public Motivo getMotivo() { return motivo; }
    public Horario getHorario() { return horario; }
    public EstadoAtencion getEstado() { return estado; }
    public Integer getPrioridad() { return prioridad; }
    public Integer getPosicionCola() { return posicionCola; }
    public String getNotas() { return notas; }
    public LocalDateTime getCreadaEn() { return creadaEn; }
    public Integer getSolicitanteId() { return solicitanteId; }
    public Integer getEmpresaId() { return empresaId; }
    public Integer getMotivoId() { return motivoId; }
    public Integer getHorarioId() { return horarioId; }
    public Integer getEstadoId() { return estadoId; }
    public LocalDateTime getActualizadaEn() { return actualizadaEn; }
    public void setId(Long id) { this.id = id; }
    public void setSolicitante(Solicitante solicitante) { this.solicitante = solicitante; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setMotivo(Motivo motivo) { this.motivo = motivo; }
    public void setHorario(Horario horario) { this.horario = horario; }
    public void setEstado(EstadoAtencion estado) { this.estado = estado; }
    public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }
    public void setPosicionCola(Integer posicionCola) { this.posicionCola = posicionCola; }
    public void setNotas(String notas) { this.notas = notas; }
    public void setCreadaEn(LocalDateTime creadaEn) { this.creadaEn = creadaEn; }
    public void setId(long id) { this.id = id; }
    public void setSolicitanteId(Integer solicitanteId) { this.solicitanteId = solicitanteId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }
    public void setMotivoId(Integer motivoId) { this.motivoId = motivoId; }
    public void setHorarioId(Integer horarioId) { this.horarioId = horarioId; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }
    public void setActualizadaEn(LocalDateTime actualizadaEn) { this.actualizadaEn = actualizadaEn; }

}
