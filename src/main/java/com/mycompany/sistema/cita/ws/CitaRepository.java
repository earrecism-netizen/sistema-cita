package com.mycompany.sistema.cita.ws;

import com.mycompany.sistema.cita.ws.dto.WsCitaDto;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
public class CitaRepository {

    
    @Resource(lookup = "jdbc/SqlServerDS")
    private DataSource cp;

    // ------------ Helpers ------------ //
    private boolean existePorId(Connection conn, String table, String pk, long id) throws SQLException {
        String sql = "SELECT 1 FROM " + table + " WITH (READPAST) WHERE " + pk + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private boolean horarioPerteneceAMotivoEmpresa(Connection conn, int horarioId, int motivoId, int empresaId) throws SQLException {
        String sql = """
            SELECT 1
            FROM citas.Horario h
            WHERE h.horario_id = ? AND h.motivo_id = ? AND h.empresa_id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, horarioId);
            ps.setInt(2, motivoId);
            ps.setInt(3, empresaId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private int countCitasEnHorario(Connection conn, int horarioId) throws SQLException {
        String sql = "SELECT COUNT(1) FROM citas.Cita WHERE horario_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, horarioId);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        }
    }

    private int capacidadHorario(Connection conn, int horarioId) throws SQLException {
        String sql = "SELECT capacidad FROM citas.Horario WHERE horario_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, horarioId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    private int getEstadoIdPorCodigo(Connection conn, String codigo) throws SQLException {
        String sql = "SELECT estado_id FROM citas.EstadoAtencion WHERE codigo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    private WsCitaDto map(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("creada_en");
        LocalDateTime creada = (ts != null) ? ts.toLocalDateTime() : null;
        
        String creadaEn = (creada != null)
            ? creada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            : null;
        
        return new WsCitaDto(
                rs.getLong("cita_id"),
                rs.getInt("solicitante_id"),
                rs.getInt("empresa_id"),
                rs.getInt("motivo_id"),
                rs.getInt("horario_id"),
                rs.getInt("estado_id"),
                rs.getInt("prioridad"),
                (Integer) rs.getObject("posicion_cola"),
                rs.getString("notas"),
                creadaEn
        );
    }

    // ------------ CRUD ------------ //
    public List<WsCitaDto> findAll() throws SQLException {
        try (Connection conn = cp.getConnection()) {
            String sql = """
                SELECT cita_id, solicitante_id, empresa_id, motivo_id, horario_id, estado_id,
                       prioridad, posicion_cola, notas, CAST(creada_en AS DATETIME2(0)) AS creada_en
                FROM citas.Cita
                ORDER BY cita_id DESC
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                List<WsCitaDto> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                conn.commit();
                return out;
            } catch (Exception e) { conn.rollback(); throw e; }
        }
    }

    public WsCitaDto findById(long id) throws SQLException {
        try (Connection conn = cp.getConnection()) {
            String sql = """
                SELECT cita_id, solicitante_id, empresa_id, motivo_id, horario_id, estado_id,
                       prioridad, posicion_cola, notas, CAST(creada_en AS DATETIME2(0)) AS creada_en
                FROM citas.Cita WHERE cita_id = ?
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    WsCitaDto dto = rs.next() ? map(rs) : null;
                    conn.commit();
                    return dto;
                }
            } catch (Exception e) { conn.rollback(); throw e; }
        }
    }

    public long create(CitaCreateRequest req) throws SQLException {
        try (Connection conn = cp.getConnection()) {

            // Validar FKs
            if (!existePorId(conn, "citas.Solicitante","solicitante_id", req.solicitanteId()))
                throw new SQLException("Solicitante no existe");
            if (!existePorId(conn, "citas.Empresa","empresa_id", req.empresaId()))
                throw new SQLException("Empresa no existe");
            if (!existePorId(conn, "citas.Motivo","motivo_id", req.motivoId()))
                throw new SQLException("Motivo no existe");
            if (!existePorId(conn, "citas.Horario","horario_id", req.horarioId()))
                throw new SQLException("Horario no existe");

            // Validar consistencia
            if (!horarioPerteneceAMotivoEmpresa(conn, req.horarioId(), req.motivoId(), req.empresaId()))
                throw new SQLException("Horario no pertenece al motivo/empresa indicado");

            // Capacidad
            int ocupadas = countCitasEnHorario(conn, req.horarioId());
            int capacidad = capacidadHorario(conn, req.horarioId());
            if (ocupadas >= capacidad) throw new SQLException("Horario sin cupo (capacidad llena)");

            // Estado inicial = PEND
            int estadoPend = getEstadoIdPorCodigo(conn, "PEND");
            if (estadoPend == 0) throw new SQLException("Estado PEND no configurado en citas.EstadoAtencion");

            String sql = """
                INSERT INTO citas.Cita
                  (solicitante_id, empresa_id, motivo_id, horario_id, estado_id, prioridad, posicion_cola, notas)
                VALUES (?,?,?,?,?,?,?,?)
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, req.solicitanteId());
                ps.setInt(2, req.empresaId());
                ps.setInt(3, req.motivoId());
                ps.setInt(4, req.horarioId());
                ps.setInt(5, estadoPend);
                ps.setInt(6, req.prioridad() == null ? 0 : req.prioridad());
                if (req.posicionCola() == null) ps.setNull(7, Types.INTEGER); else ps.setInt(7, req.posicionCola());
                if (req.notas() == null || req.notas().isBlank()) ps.setNull(8, Types.NVARCHAR); else ps.setString(8, req.notas());

                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        long id = keys.getLong(1);
                        conn.commit();
                        return id;
                    }
                    conn.rollback();
                    throw new SQLException("No se generó cita_id");
                }
            } catch (Exception e) { conn.rollback(); throw e; }
        }
    }

    public boolean update(long id, CitaUpdateRequest req) throws SQLException {
        try (Connection conn = cp.getConnection()) {
            if (!existePorId(conn, "citas.Cita", "cita_id", id)) return false;

            // Validar estado si viene
            if (req.estadoId() != null && !existePorId(conn, "citas.EstadoAtencion", "estado_id", req.estadoId()))
                throw new SQLException("Estado indicado no existe");

            String sql = """
                UPDATE citas.Cita
                   SET estado_id = COALESCE(?, estado_id),
                       prioridad = COALESCE(?, prioridad),
                       posicion_cola = ?,
                       notas = ?
                 WHERE cita_id = ?
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (req.estadoId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, req.estadoId());
                if (req.prioridad() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, req.prioridad());
                if (req.posicionCola() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, req.posicionCola());
                if (req.notas() == null) ps.setNull(4, Types.NVARCHAR); else ps.setString(4, req.notas());
                ps.setLong(5, id);

                int rows = ps.executeUpdate();
                conn.commit();
                return rows > 0;
            } catch (Exception e) { conn.rollback(); throw e; }
        }
    }

    public boolean delete(long id) throws SQLException {
        try (Connection conn = cp.getConnection()) {
            String sql = "DELETE FROM citas.Cita WHERE cita_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                int rows = ps.executeUpdate();
                conn.commit();
                return rows > 0;
            } catch (Exception e) { conn.rollback(); throw e; }
        }
    }
}
