
package com.mycompany.sistema.cita.ws;


import com.mycompany.sistema.cita.ws.dto.EmpresaDTO;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmpresaManager {

    @Resource(lookup = "jdbc/SqlServerDS")
    private DataSource ds;

    @Transactional
    public List<EmpresaDTO> listarEmpresas() throws Exception {
        String sql = """
            SELECT empresa_id, nombre, nit, telefono, direccion, activo, creado_en
            FROM   citas.Empresa
            ORDER BY empresa_id
        """;
        List<EmpresaDTO> out = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EmpresaDTO e = new EmpresaDTO();
                e.setEmpresaId(rs.getInt("empresa_id"));
                e.setNombre(rs.getString("nombre"));
                e.setNit(rs.getString("nit"));
                e.setTelefono(rs.getString("telefono"));
                e.setDireccion(rs.getString("direccion"));
                e.setActivo(rs.getBoolean("activo"));
                Timestamp ts = rs.getTimestamp("creado_en");
                e.setCreadoEn(ts != null ? ts.toInstant().atOffset(ZoneOffset.UTC).toString() : null);
                out.add(e);
            }
        }
        return out;
    }

    @Transactional
    public EmpresaDTO buscarEmpresaPorId(int empresaId) throws Exception {
        String sql = """
            SELECT empresa_id, nombre, nit, telefono, direccion, activo, creado_en
            FROM   citas.Empresa
            WHERE  empresa_id = ?
        """;
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, empresaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmpresaDTO e = new EmpresaDTO();
                    e.setEmpresaId(rs.getInt("empresa_id"));
                    e.setNombre(rs.getString("nombre"));
                    e.setNit(rs.getString("nit"));
                    e.setTelefono(rs.getString("telefono"));
                    e.setDireccion(rs.getString("direccion"));
                    e.setActivo(rs.getBoolean("activo"));
                    Timestamp ts = rs.getTimestamp("creado_en");
                    e.setCreadoEn(ts != null ? ts.toInstant().atOffset(ZoneOffset.UTC).toString() : null);
                    return e;
                }
            }
        }
        return null;
    }
}
