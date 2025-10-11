package com.mycompany.sistema.cita.beans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class CitaBean implements Serializable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final List<Solicitante> solicitantes = new ArrayList<>();
    private final List<Empresa> empresas = new ArrayList<>();
    private final List<Motivo> motivos = new ArrayList<>();
    private final List<Horario> horarios = new ArrayList<>();
    private final List<EstadoAtencion> estados = new ArrayList<>();
    private final List<CitaResumen> citas = new ArrayList<>();

    private CitaForm form = new CitaForm();

    public CitaBean() {
        cargarDatosDemo();
    }

    private void cargarDatosDemo() {
        solicitantes.add(new Solicitante(1, "Juan", "Pérez", "3012345670101", "juan@example.com", "5555-1111"));
        solicitantes.add(new Solicitante(2, "Ana", "García", "3012345670202", "ana@example.com", "5555-2222"));
        solicitantes.add(new Solicitante(3, "Carlos", "Ramírez", null, "carlos@example.com", "5555-3333"));

        empresas.add(new Empresa(1, "Centro Médico Vida", "123456-7", "5555-4444", "Zona 10"));
        empresas.add(new Empresa(2, "Clínica Familiar", "987654-3", "5555-5555", "Zona 14"));

        motivos.add(new Motivo(1, 1, "Consulta General", "Evaluación general", 30));
        motivos.add(new Motivo(2, 1, "Chequeo Cardiológico", "Control de corazón", 60));
        motivos.add(new Motivo(3, 2, "Odontología", "Limpieza dental", 45));
        motivos.add(new Motivo(4, 2, "Pediatría", "Revisión anual", 30));

        horarios.add(new Horario(1, 1, 1, fecha(2024, Month.APRIL, 25, 9, 0), fecha(2024, Month.APRIL, 25, 9, 30), 1, "DISPONIBLE"));
        horarios.add(new Horario(2, 1, 1, fecha(2024, Month.APRIL, 25, 10, 0), fecha(2024, Month.APRIL, 25, 10, 30), 1, "DISPONIBLE"));
        horarios.add(new Horario(3, 1, 2, fecha(2024, Month.APRIL, 26, 11, 0), fecha(2024, Month.APRIL, 26, 12, 0), 1, "DISPONIBLE"));
        horarios.add(new Horario(4, 2, 3, fecha(2024, Month.APRIL, 27, 8, 30), fecha(2024, Month.APRIL, 27, 9, 15), 2, "DISPONIBLE"));
        horarios.add(new Horario(5, 2, 4, fecha(2024, Month.APRIL, 28, 15, 0), fecha(2024, Month.APRIL, 28, 15, 30), 1, "DISPONIBLE"));

        estados.add(new EstadoAtencion(1, "CREADA", "Creada", "Cita creada"));
        estados.add(new EstadoAtencion(2, "CONFIRMADA", "Confirmada", "Cita confirmada"));
        estados.add(new EstadoAtencion(3, "ATENDIDA", "Atendida", "Paciente atendido"));
        estados.add(new EstadoAtencion(4, "CANCELADA", "Cancelada", "Cita cancelada"));

        // Citas de ejemplo
        CitaForm demoForm = new CitaForm();
        demoForm.setSolicitanteId(1);
        demoForm.setEmpresaId(1);
        demoForm.setMotivoId(1);
        demoForm.setHorarioId(1);
        demoForm.setEstadoId(2);
        demoForm.setPrioridad(1);
        demoForm.setPosicionCola(5);
        demoForm.setNotas("Paciente recurrente");
        agregarCitaDesdeFormulario(demoForm);
    }

    private LocalDateTime fecha(int year, Month month, int day, int hour, int minute) {
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public String guardar() {
        if (!validarFormulario()) {
            return null;
        }
        agregarCitaDesdeFormulario(form);
        form = new CitaForm();
        return "cita-list?faces-redirect=true";
    }

    private void agregarCitaDesdeFormulario(CitaForm datos) {
        CitaResumen nueva = new CitaResumen();
        long nextId = citas.stream().mapToLong(CitaResumen::getId).max().orElse(0L) + 1L;
        nueva.setId(nextId);
        nueva.setSolicitanteId(datos.getSolicitanteId());
        nueva.setEmpresaId(datos.getEmpresaId());
        nueva.setMotivoId(datos.getMotivoId());
        nueva.setHorarioId(datos.getHorarioId());
        nueva.setEstadoId(datos.getEstadoId());
        nueva.setPrioridad(Optional.ofNullable(datos.getPrioridad()).orElse(0));
        nueva.setPosicionCola(datos.getPosicionCola());
        nueva.setNotas(datos.getNotas());
        nueva.setCreadaEn(LocalDateTime.now());

        buscarSolicitante(datos.getSolicitanteId()).ifPresent(s -> nueva.setSolicitanteNombre(s.getNombreCompleto()));
        buscarEmpresa(datos.getEmpresaId()).ifPresent(e -> nueva.setEmpresaNombre(e.getNombre()));
        buscarMotivo(datos.getMotivoId()).ifPresent(m -> nueva.setMotivoNombre(m.getNombre()));
        buscarHorario(datos.getHorarioId()).ifPresent(h -> {
            nueva.setHorarioDescripcion(formatearHorario(h));
            nueva.setHorarioInicio(h.getInicio());
            nueva.setHorarioFin(h.getFin());
        });
        buscarEstado(datos.getEstadoId()).ifPresent(e -> nueva.setEstadoNombre(e.getNombre()));

        citas.add(nueva);
    }

    private boolean validarFormulario() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean valido = true;

        if (form.getSolicitanteId() == null) {
            context.addMessage(null, mensajeError("Debe seleccionar un solicitante."));
            valido = false;
        }
        if (form.getEmpresaId() == null) {
            context.addMessage(null, mensajeError("Debe seleccionar una empresa."));
            valido = false;
        }
        if (form.getMotivoId() == null) {
            context.addMessage(null, mensajeError("Debe seleccionar un motivo."));
            valido = false;
        }
        if (form.getHorarioId() == null) {
            context.addMessage(null, mensajeError("Debe seleccionar un horario."));
            valido = false;
        }
        if (form.getEstadoId() == null) {
            context.addMessage(null, mensajeError("Debe seleccionar un estado."));
            valido = false;
        }
        return valido;
    }

    private FacesMessage mensajeError(String mensaje) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
    }

    public void limpiarMotivoYHorario() {
        form.setMotivoId(null);
        form.setHorarioId(null);
    }

    public void limpiarHorario() {
        form.setHorarioId(null);
    }

    public List<Motivo> getMotivosDisponibles() {
        if (form.getEmpresaId() == null) {
            return List.of();
        }
        return motivos.stream()
                .filter(m -> Objects.equals(m.getEmpresaId(), form.getEmpresaId()))
                .sorted(Comparator.comparing(Motivo::getNombre))
                .collect(Collectors.toList());
    }

    public List<Horario> getHorariosDisponibles() {
        if (form.getEmpresaId() == null || form.getMotivoId() == null) {
            return List.of();
        }
        return horarios.stream()
                .filter(h -> Objects.equals(h.getEmpresaId(), form.getEmpresaId()))
                .filter(h -> Objects.equals(h.getMotivoId(), form.getMotivoId()))
                .sorted(Comparator.comparing(Horario::getInicio))
                .collect(Collectors.toList());
    }

    public String getDescripcionHorarioSeleccionado() {
        return buscarHorario(form.getHorarioId())
                .map(this::formatearHorario)
                .orElse("Seleccione un horario disponible");
    }

    public String formatearHorario(Horario horario) {
        return DATE_TIME_FORMATTER.format(horario.getInicio()) +
                " - " + DATE_TIME_FORMATTER.format(horario.getFin());
    }

    public List<Solicitante> getSolicitantes() {
        return solicitantes;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public List<EstadoAtencion> getEstados() {
        return estados;
    }

    public List<CitaResumen> getCitas() {
        return citas;
    }

    public CitaForm getForm() {
        return form;
    }

    public void setForm(CitaForm form) {
        this.form = form;
    }

    public void eliminar(CitaResumen cita) {
        citas.remove(cita);
    }

    private Optional<Solicitante> buscarSolicitante(Integer id) {
        return solicitantes.stream().filter(s -> Objects.equals(s.getId(), id)).findFirst();
    }

    private Optional<Empresa> buscarEmpresa(Integer id) {
        return empresas.stream().filter(e -> Objects.equals(e.getId(), id)).findFirst();
    }

    private Optional<Motivo> buscarMotivo(Integer id) {
        return motivos.stream().filter(m -> Objects.equals(m.getId(), id)).findFirst();
    }

    private Optional<Horario> buscarHorario(Integer id) {
        return horarios.stream().filter(h -> Objects.equals(h.getId(), id)).findFirst();
    }

    private Optional<EstadoAtencion> buscarEstado(Integer id) {
        return estados.stream().filter(e -> Objects.equals(e.getId(), id)).findFirst();
    }

    // Clases auxiliares
    public static class CitaForm implements Serializable {
        private Integer solicitanteId;
        private Integer empresaId;
        private Integer motivoId;
        private Integer horarioId;
        private Integer estadoId;
        private Integer prioridad = 0;
        private Integer posicionCola;
        private String notas;

        public Integer getSolicitanteId() { return solicitanteId; }
        public void setSolicitanteId(Integer solicitanteId) { this.solicitanteId = solicitanteId; }
        public Integer getEmpresaId() { return empresaId; }
        public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }
        public Integer getMotivoId() { return motivoId; }
        public void setMotivoId(Integer motivoId) { this.motivoId = motivoId; }
        public Integer getHorarioId() { return horarioId; }
        public void setHorarioId(Integer horarioId) { this.horarioId = horarioId; }
        public Integer getEstadoId() { return estadoId; }
        public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }
        public Integer getPrioridad() { return prioridad; }
        public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }
        public Integer getPosicionCola() { return posicionCola; }
        public void setPosicionCola(Integer posicionCola) { this.posicionCola = posicionCola; }
        public String getNotas() { return notas; }
        public void setNotas(String notas) { this.notas = notas; }
    }

    public static class CitaResumen implements Serializable {
        private Long id;
        private Integer solicitanteId;
        private Integer empresaId;
        private Integer motivoId;
        private Integer horarioId;
        private Integer estadoId;
        private Integer prioridad;
        private Integer posicionCola;
        private String notas;
        private LocalDateTime creadaEn;

        private String solicitanteNombre;
        private String empresaNombre;
        private String motivoNombre;
        private String estadoNombre;
        private String horarioDescripcion;
        private LocalDateTime horarioInicio;
        private LocalDateTime horarioFin;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Integer getSolicitanteId() { return solicitanteId; }
        public void setSolicitanteId(Integer solicitanteId) { this.solicitanteId = solicitanteId; }
        public Integer getEmpresaId() { return empresaId; }
        public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }
        public Integer getMotivoId() { return motivoId; }
        public void setMotivoId(Integer motivoId) { this.motivoId = motivoId; }
        public Integer getHorarioId() { return horarioId; }
        public void setHorarioId(Integer horarioId) { this.horarioId = horarioId; }
        public Integer getEstadoId() { return estadoId; }
        public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }
        public Integer getPrioridad() { return prioridad; }
        public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }
        public Integer getPosicionCola() { return posicionCola; }
        public void setPosicionCola(Integer posicionCola) { this.posicionCola = posicionCola; }
        public String getNotas() { return notas; }
        public void setNotas(String notas) { this.notas = notas; }
        public LocalDateTime getCreadaEn() { return creadaEn; }
        public void setCreadaEn(LocalDateTime creadaEn) { this.creadaEn = creadaEn; }
        public String getSolicitanteNombre() { return solicitanteNombre; }
        public void setSolicitanteNombre(String solicitanteNombre) { this.solicitanteNombre = solicitanteNombre; }
        public String getEmpresaNombre() { return empresaNombre; }
        public void setEmpresaNombre(String empresaNombre) { this.empresaNombre = empresaNombre; }
        public String getMotivoNombre() { return motivoNombre; }
        public void setMotivoNombre(String motivoNombre) { this.motivoNombre = motivoNombre; }
        public String getEstadoNombre() { return estadoNombre; }
        public void setEstadoNombre(String estadoNombre) { this.estadoNombre = estadoNombre; }
        public String getHorarioDescripcion() { return horarioDescripcion; }
        public void setHorarioDescripcion(String horarioDescripcion) { this.horarioDescripcion = horarioDescripcion; }
        public LocalDateTime getHorarioInicio() { return horarioInicio; }
        public void setHorarioInicio(LocalDateTime horarioInicio) { this.horarioInicio = horarioInicio; }
        public LocalDateTime getHorarioFin() { return horarioFin; }
        public void setHorarioFin(LocalDateTime horarioFin) { this.horarioFin = horarioFin; }
    }

    public static class Solicitante implements Serializable {
        private final Integer id;
        private final String nombres;
        private final String apellidos;
        private final String dpi;
        private final String email;
        private final String telefono;

        public Solicitante(Integer id, String nombres, String apellidos, String dpi, String email, String telefono) {
            this.id = id;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.dpi = dpi;
            this.email = email;
            this.telefono = telefono;
        }

        public Integer getId() { return id; }
        public String getNombres() { return nombres; }
        public String getApellidos() { return apellidos; }
        public String getDpi() { return dpi; }
        public String getEmail() { return email; }
        public String getTelefono() { return telefono; }
        public String getNombreCompleto() { return nombres + " " + apellidos; }
    }

    public static class Empresa implements Serializable {
        private final Integer id;
        private final String nombre;
        private final String nit;
        private final String telefono;
        private final String direccion;

        public Empresa(Integer id, String nombre, String nit, String telefono, String direccion) {
            this.id = id;
            this.nombre = nombre;
            this.nit = nit;
            this.telefono = telefono;
            this.direccion = direccion;
        }

        public Integer getId() { return id; }
        public String getNombre() { return nombre; }
        public String getNit() { return nit; }
        public String getTelefono() { return telefono; }
        public String getDireccion() { return direccion; }
    }

    public static class Motivo implements Serializable {
        private final Integer id;
        private final Integer empresaId;
        private final String nombre;
        private final String descripcion;
        private final Integer duracionMinutos;

        public Motivo(Integer id, Integer empresaId, String nombre, String descripcion, Integer duracionMinutos) {
            this.id = id;
            this.empresaId = empresaId;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.duracionMinutos = duracionMinutos;
        }

        public Integer getId() { return id; }
        public Integer getEmpresaId() { return empresaId; }
        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
        public Integer getDuracionMinutos() { return duracionMinutos; }
    }

    public static class Horario implements Serializable {
        private final Integer id;
        private final Integer empresaId;
        private final Integer motivoId;
        private final LocalDateTime inicio;
        private final LocalDateTime fin;
        private final Integer capacidad;
        private final String estadoSlot;

        public Horario(Integer id, Integer empresaId, Integer motivoId, LocalDateTime inicio, LocalDateTime fin,
                       Integer capacidad, String estadoSlot) {
            this.id = id;
            this.empresaId = empresaId;
            this.motivoId = motivoId;
            this.inicio = inicio;
            this.fin = fin;
            this.capacidad = capacidad;
            this.estadoSlot = estadoSlot;
        }

        public Integer getId() { return id; }
        public Integer getEmpresaId() { return empresaId; }
        public Integer getMotivoId() { return motivoId; }
        public LocalDateTime getInicio() { return inicio; }
        public LocalDateTime getFin() { return fin; }
        public Integer getCapacidad() { return capacidad; }
        public String getEstadoSlot() { return estadoSlot; }
    }

    public static class EstadoAtencion implements Serializable {
        private final Integer id;
        private final String codigo;
        private final String nombre;
        private final String descripcion;

        public EstadoAtencion(Integer id, String codigo, String nombre, String descripcion) {
            this.id = id;
            this.codigo = codigo;
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        public Integer getId() { return id; }
        public String getCodigo() { return codigo; }
        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
    }
}

