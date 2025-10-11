package com.mycompany.sistema.cita.ws;

import com.mycompany.sistema.cita.ws.dto.*;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService(
    name = "CitaService",
    targetNamespace = "http://ws.cita.sistema.mycompany.com/"
)
public interface CitaService {

    @WebMethod(operationName = "ListarCitas")
    @WebResult(name = "CitaList")
    CitaList listarCitas() throws BusinessFault;

    @WebMethod(operationName = "ObtenerCita")
    @WebResult(name = "Cita")
    WsCitaDto obtenerCita(@WebParam(name = "citaId") long citaId) throws BusinessFault;

    @WebMethod(operationName = "CrearCita")
    @WebResult(name = "Cita")
    WsCitaDto crearCita(@WebParam(name = "request") CitaCreateRequest request) throws BusinessFault;

    @WebMethod(operationName = "ActualizarCita")
    @WebResult(name = "Cita")
    WsCitaDto actualizarCita(@WebParam(name = "citaId") long citaId,
                           @WebParam(name = "request") CitaUpdateRequest request) throws BusinessFault;

    @WebMethod(operationName = "EliminarCita")
    @WebResult(name = "eliminado")
    boolean eliminarCita(@WebParam(name = "citaId") long citaId) throws BusinessFault;
}
