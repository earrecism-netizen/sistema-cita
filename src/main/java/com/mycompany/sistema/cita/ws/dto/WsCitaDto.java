package com.mycompany.sistema.cita.ws.dto;


import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "Cita")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsCitaDto {
    private long citaId;
    private int solicitanteId;
    private int empresaId;
    private int motivoId;
    private int horarioId;
    private int estadoId;
    private int prioridad;
    private Integer posicionCola;
    private String notas;

    // LocalDateTime no es JAXB-friendly por defecto; si lo expones, usa String ISO
    private String creadaEn; // "YYYY-MM-DD HH:MM:SS"

    public WsCitaDto() {}

    public WsCitaDto(long citaId, int solicitanteId, int empresaId, int motivoId, int horarioId,
                   int estadoId, int prioridad, Integer posicionCola, String notas, String creadaEn) {
        this.citaId = citaId;
        this.solicitanteId = solicitanteId;
        this.empresaId = empresaId;
        this.motivoId = motivoId;
        this.horarioId = horarioId;
        this.estadoId = estadoId;
        this.prioridad = prioridad;
        this.posicionCola = posicionCola;
        this.notas = notas;
        this.creadaEn = creadaEn;
    }

    // getters/setters (JAXB los necesita si no usas records)
    public long getCitaId() { return citaId; }
    public void setCitaId(long citaId) { this.citaId = citaId; }

    public int getSolicitanteId() { return solicitanteId; }
    public void setSolicitanteId(int solicitanteId) { this.solicitanteId = solicitanteId; }

    public int getEmpresaId() { return empresaId; }
    public void setEmpresaId(int empresaId) { this.empresaId = empresaId; }

    public int getMotivoId() { return motivoId; }
    public void setMotivoId(int motivoId) { this.motivoId = motivoId; }

    public int getHorarioId() { return horarioId; }
    public void setHorarioId(int horarioId) { this.horarioId = horarioId; }

    public int getEstadoId() { return estadoId; }
    public void setEstadoId(int estadoId) { this.estadoId = estadoId; }

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }

    public Integer getPosicionCola() { return posicionCola; }
    public void setPosicionCola(Integer posicionCola) { this.posicionCola = posicionCola; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getCreadaEn() { return creadaEn; }
    public void setCreadaEn(String creadaEn) { this.creadaEn = creadaEn; }
}
