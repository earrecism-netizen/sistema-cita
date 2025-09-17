package com.mycompany.sistema.cita.beans;


import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class CitaBean implements Serializable {

    private final List<Cita> citas;
    private  Cita citaActual;

    public CitaBean() {
        citas = new ArrayList<>();
        // Cargar datos de prueba (luego reemplaza por llamada a servicio/JPA)
        citas.add(new Cita(1L, "Juan Pérez", "Dr. López", "2025-09-20 09:00"));
        citas.add(new Cita(2L, "Ana García", "Dra. Ramírez", "2025-09-21 14:00"));
    }

    // Getters & Setters
    public List<Cita> getCitas() { return citas; }
    public Cita getCitaActual() { return citaActual; }
    public void setCitaActual(Cita citaActual) { this.citaActual = citaActual; }

    // Métodos de negocio
    public void guardar() {
        if (citaActual != null) {
            citas.add(citaActual);
            citaActual = null;
        }
    }

    public void editar(Cita c) {
        this.citaActual = c;
    }

    public void eliminar(Cita c) {
        citas.remove(c);
    }
}

