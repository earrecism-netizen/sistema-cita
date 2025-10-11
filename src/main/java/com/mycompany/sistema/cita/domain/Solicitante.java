package com.mycompany.sistema.cita.domain;
import jakarta.persistence.*;

@Entity
@Table(name = "Solicitante", schema = "citas")
public class Solicitante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitante_id")
    private Integer id;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    public Integer getId() { return id; }
    public String getNombreCompleto() { return (nombres + " " + apellidos).trim(); }
    public void setId(Integer id) { this.id = id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
}