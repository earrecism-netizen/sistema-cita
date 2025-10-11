package com.mycompany.sistema.cita.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Empresa", schema = "citas")
public class Empresa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empresa_id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}