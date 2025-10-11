package com.mycompany.sistema.cita.ws.dto;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "CitaUpdateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CitaUpdateRequest {
    private Integer estadoId;
    private Integer prioridad;
    private Integer posicionCola;
    private String notas;

    public CitaUpdateRequest() {}

    // getters/setters
    public Integer getEstadoId() { return estadoId; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }

    public Integer getPrioridad() { return prioridad; }
    public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }

    public Integer getPosicionCola() { return posicionCola; }
    public void setPosicionCola(Integer posicionCola) { this.posicionCola = posicionCola; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
