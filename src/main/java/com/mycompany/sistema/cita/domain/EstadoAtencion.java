package com.mycompany.sistema.cita.domain;
import jakarta.persistence.*;

@Entity
@Table(name = "EstadoAtencion", schema = "citas")
public class EstadoAtencion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estado_id")
    private Integer id;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Integer getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public void setId(Integer id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}