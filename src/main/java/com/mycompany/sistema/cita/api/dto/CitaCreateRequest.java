package com.mycompany.sistema.cita.api.dto;

public class CitaCreateRequest {
    public Integer solicitanteId;
    public Integer empresaId;
    public Integer motivoId;
    public Integer horarioId;
    public Integer estadoId;
    public Integer prioridad;     // opcional
    public Integer posicionCola;  // opcional
    public String  notas;         // opcional
}
