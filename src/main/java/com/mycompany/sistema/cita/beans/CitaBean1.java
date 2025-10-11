package com.mycompany.sistema.cita.beans;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class CitaBean1 implements Serializable {
    private List<Cita> citas = new ArrayList<>();
    private Cita citaActual = new Cita();

    public CitaBean1() {
        // demo
        citas.add(new Cita(1L, "Juan Pérez", "Dra. López", LocalDateTime.now().plusDays(1)));
        citas.add(new Cita(2L, "Ana García", "Dr. Ruiz", LocalDateTime.now().plusDays(2)));
    }

    public String guardar() {
        if (citaActual.getId() == null) {
            long nextId = citas.stream().mapToLong(c -> c.getId()).max().orElse(0L) + 1L;
            citaActual.setId(nextId);
            citas.add(citaActual);
        }
        citaActual = new Cita();
        return "cita-list?faces-redirect=true";
    }

    public String editar(Cita c) {
        this.citaActual = c;
        return "cita-form?faces-redirect=true";
    }

    public void eliminar(Cita c) { citas.remove(c); }

    // getters/setters
    public List<Cita> getCitas() { return citas; }
    public Cita getCitaActual() { return citaActual; }
    public void setCitaActual(Cita c) { this.citaActual = c; }

    // Clase simple interna o en su propio archivo
    public static class Cita implements Serializable {
        private Long id;
        private String paciente;
        private String doctor;
        private LocalDateTime fechaHora;

        public Cita() {}
        public Cita(Long id, String paciente, String doctor, LocalDateTime fechaHora) {
            this.id = id; this.paciente = paciente; this.doctor = doctor; this.fechaHora = fechaHora;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getPaciente() { return paciente; }
        public void setPaciente(String paciente) { this.paciente = paciente; }
        public String getDoctor() { return doctor; }
        public void setDoctor(String doctor) { this.doctor = doctor; }
        public LocalDateTime getFechaHora() { return fechaHora; }
        public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    }
}

