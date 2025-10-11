package com.mycompany.sistema.cita.ws;

import com.mycompany.sistema.cita.ws.dto.EmpresaDTO;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;

@WebService(serviceName = "EmpresaService")
public class EmpresaService {

    @Inject
    private EmpresaManager manager;

    @WebMethod(operationName = "listarEmpresas")
    public List<EmpresaDTO> listarEmpresas() throws Exception {
        return manager.listarEmpresas();
    }

    @WebMethod(operationName = "buscarEmpresaPorId")
    public EmpresaDTO buscarEmpresaPorId(@WebParam(name = "empresaId") int empresaId) throws Exception {
        return manager.buscarEmpresaPorId(empresaId);
    }
}
