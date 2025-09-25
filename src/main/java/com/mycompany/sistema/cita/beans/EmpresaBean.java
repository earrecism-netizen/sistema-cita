package com.mycompany.sistema.cita.beans;

import com.mycompany.sistema.cita.ws.EmpresaManager;
import com.mycompany.sistema.cita.ws.dto.EmpresaDTO;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class EmpresaBean implements Serializable {

    @Inject
    private EmpresaManager empresaManager;

    private List<EmpresaDTO> empresas = new ArrayList<>();

    public void listarEmpresas() {
        System.out.println("Entramos al boton");
        try {
            empresas = this.empresaManager.listarEmpresas();
            System.out.println("empresas:");
            for (int i = 0; i < empresas.size(); i++) {
                EmpresaDTO empresa = empresas.get(i);
                System.out.println(empresa);
            }

        } catch (Exception e) {
            System.out.println("error: ");
            e.printStackTrace();
        }

    }

    public List<EmpresaDTO> getEmpresas() {
        return empresas;
    }

}
