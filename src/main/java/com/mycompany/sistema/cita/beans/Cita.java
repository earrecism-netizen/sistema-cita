package com.mycompany.sistema.cita.beans;

public class Cita {
    private Long id;
    private String paciente;
    private String doctor;
    private String fechaHora;

    public Cita(Long id, String paciente, String doctor, String fechaHora) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fechaHora = fechaHora;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPaciente() { return paciente; }
    public void setPaciente(String paciente) { this.paciente = paciente; }
    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}
