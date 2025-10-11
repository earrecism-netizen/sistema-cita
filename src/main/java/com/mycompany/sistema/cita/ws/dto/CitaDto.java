package com.mycompany.sistema.cita.ws.dto;

import java.time.LocalDateTime;

public record CitaDto(
        long citaId,
        int solicitanteId,
        int empresaId,
        int motivoId,
        int horarioId,
        int estadoId,
        int prioridad,
        Integer posicionCola,
        String notas,
        LocalDateTime creadaEn
) {}
