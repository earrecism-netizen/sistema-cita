package com.mycompany.sistema.cita.ws.dto;
import java.time.LocalDateTime;

public class CitaDTO {
    private Long citaId;
    private String solicitante;
    private String empresa;
    private String motivo;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String estado;
    private int prioridad;
    private Integer posicionCola;
    private String notas;
    private LocalDateTime creadaEn;
    private Integer solicitanteId;
    private Integer empresaId;
    private Integer motivoId;
    private Integer horarioId;
    private Integer estadoId;

    // getters y setters
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }
    public Integer getPosicionCola() { return posicionCola; }
    public void setPosicionCola(Integer posicionCola) { this.posicionCola = posicionCola; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public LocalDateTime getCreadaEn() { return creadaEn; }
    public void setCreadaEn(LocalDateTime creadaEn) { this.creadaEn = creadaEn; }
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
}
