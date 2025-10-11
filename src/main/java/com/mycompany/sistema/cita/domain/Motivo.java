package com.mycompany.sistema.cita.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Motivo", schema = "citas",
       uniqueConstraints = @UniqueConstraint(name = "UQ_Motivo", columnNames = {"empresa_id", "nombre"}))
public class Motivo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "motivo_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    public Integer getId() { return id; }
    public Empresa getEmpresa() { return empresa; }
    public String getNombre() { return nombre; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setId(Integer id) { this.id = id; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
}