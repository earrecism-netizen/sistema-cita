package com.mycompany.sistema.cita.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "Horario", schema = "citas",
       uniqueConstraints = @UniqueConstraint(name = "UQ_Horario", columnNames = {"empresa_id","motivo_id","inicio","fin"}))
public class Horario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horario_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "motivo_id", nullable = false)
    private Motivo motivo;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fin", nullable = false)
    private LocalDateTime fin;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "estado_slot", nullable = false, length = 20)
    private String estadoSlot; // DISPONIBLE | OCUPADO | BLOQUEADO

    public Integer getId() { return id; }
    public Empresa getEmpresa() { return empresa; }
    public Motivo getMotivo() { return motivo; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFin() { return fin; }
    public Integer getCapacidad() { return capacidad; }
    public String getEstadoSlot() { return estadoSlot; }
    public void setId(Integer id) { this.id = id; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setMotivo(Motivo motivo) { this.motivo = motivo; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    public void setEstadoSlot(String estadoSlot) { this.estadoSlot = estadoSlot; }
}
