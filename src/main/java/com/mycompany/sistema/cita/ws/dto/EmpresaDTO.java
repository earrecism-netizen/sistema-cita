/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema.cita.ws.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "Empresa")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmpresaDTO {
    private Integer empresaId;
    private String  nombre;
    private String  nit;
    private String  telefono;
    private String  direccion;
    private Boolean activo;
    private String  creadoEn; // ISO-8601 para que sea fácil en SOAP

    // getters/setters
    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public String getCreadoEn() { return creadoEn; }
    public void setCreadoEn(String creadoEn) { this.creadoEn = creadoEn; }

    @Override
    public String toString() {
        return "EmpresaDTO{" + "empresaId=" + empresaId + ", nombre=" + nombre + ", nit=" + nit + ", telefono=" + telefono + ", direccion=" + direccion + ", activo=" + activo + ", creadoEn=" + creadoEn + '}';
    }
    
    
}
