package com.mycompany.sistema.cita.ws.dto;


import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "CitaList")
@XmlAccessorType(XmlAccessType.FIELD)
public class CitaList {
    @XmlElement(name = "cita")
    private List<WsCitaDto> items;

    public CitaList() {}
    public CitaList(List<WsCitaDto> items) { this.items = items; }

    public List<WsCitaDto> getItems() { return items; }
    public void setItems(List<WsCitaDto> items) { this.items = items; }
}
