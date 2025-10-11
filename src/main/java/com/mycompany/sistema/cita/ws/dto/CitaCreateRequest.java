package com.mycompany.sistema.cita.ws.dto;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "CitaCreateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CitaCreateRequest {
    private int solicitanteId;
    private int empresaId;
    private int motivoId;
    private int horarioId;
    private Integer prioridad;
    private Integer posicionCola;
    private String notas;

    public CitaCreateRequest() {}

    // getters/setters
    public int getSolicitanteId() { return solicitanteId; }
    public void setSolicitanteId(int solicitanteId) { this.solicitanteId = solicitanteId; }

    public int getEmpresaId() { return empresaId; }
    public void setEmpresaId(int empresaId) { this.empresaId = empresaId; }

    public int getMotivoId() { return motivoId; }
    public void setMotivoId(int motivoId) { this.motivoId = motivoId; }

    public int getHorarioId() { return horarioId; }
    public void setHorarioId(int horarioId) { this.horarioId = horarioId; }

    public Integer getPrioridad() { return prioridad; }
    public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }

    public Integer getPosicionCola() { return posicionCola; }
    public void setPosicionCola(Integer posicionCola) { this.posicionCola = posicionCola; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
