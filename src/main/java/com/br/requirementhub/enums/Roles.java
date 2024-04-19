package com.br.requirementhub.enums;


import java.util.Arrays;
import java.util.List;
public enum Roles {
    GERENTE_DE_PROJETOS(Arrays.asList(Permissions.APROVAR_REQUISITO, Permissions.EXCLUIR_REQUISITO,
            Permissions.EDITAR_REQUISITO, Permissions.VISUALIZAR_REQUISITO,
            Permissions.APROVAR_DOCUMENTO, Permissions.EXCLUIR_DOCUMENTO,
            Permissions.EDITAR_DOCUMENTO, Permissions.VISUALIZAR_DOCUMENTO,
            Permissions.APROVAR_PROJETO, Permissions.EXCLUIR_PROJETO,
            Permissions.EDITAR_PROJETO, Permissions.VISUALIZAR_PROJETO,
            Permissions.APROVAR_REQUISITO_PROJETO, Permissions.EXCLUIR_REQUISITO_PROJETO,
            Permissions.EDITAR_REQUISITO_PROJETO, Permissions.VISUALIZAR_REQUISITO_PROJETO,
            Permissions.APROVAR_DOCUMENTO_PROJETO, Permissions.EXCLUIR_DOCUMENTO_PROJETO,
            Permissions.EDITAR_DOCUMENTO_PROJETO, Permissions.VISUALIZAR_DOCUMENTO_PROJETO,
            Permissions.VALIDAR_PROJETO, Permissions.COMPLEMENTAR_REQUISITO)),
    ANALISTA_DE_REQUISITOS(Arrays.asList(Permissions.APROVAR_REQUISITO, Permissions.EXCLUIR_REQUISITO,
            Permissions.EDITAR_REQUISITO, Permissions.VISUALIZAR_REQUISITO,
            Permissions.APROVAR_DOCUMENTO, Permissions.EXCLUIR_DOCUMENTO,
            Permissions.EDITAR_DOCUMENTO, Permissions.VISUALIZAR_DOCUMENTO,
            Permissions.APROVAR_PROJETO, Permissions.EXCLUIR_PROJETO,
            Permissions.EDITAR_PROJETO, Permissions.VISUALIZAR_PROJETO,
            Permissions.APROVAR_REQUISITO_PROJETO, Permissions.EXCLUIR_REQUISITO_PROJETO,
            Permissions.EDITAR_REQUISITO_PROJETO, Permissions.VISUALIZAR_REQUISITO_PROJETO,
            Permissions.APROVAR_DOCUMENTO_PROJETO, Permissions.EXCLUIR_DOCUMENTO_PROJETO,
            Permissions.EDITAR_DOCUMENTO_PROJETO, Permissions.VISUALIZAR_DOCUMENTO_PROJETO,
            Permissions.VALIDAR_PROJETO, Permissions.COMPLEMENTAR_REQUISITO)),
    ANALISTA_DE_NEGOCIO(Arrays.asList(Permissions.APROVAR_REQUISITO, Permissions.EXCLUIR_REQUISITO,
            Permissions.EDITAR_REQUISITO, Permissions.VISUALIZAR_REQUISITO,
            Permissions.APROVAR_DOCUMENTO, Permissions.EXCLUIR_DOCUMENTO,
            Permissions.EDITAR_DOCUMENTO, Permissions.VISUALIZAR_DOCUMENTO,
            Permissions.APROVAR_PROJETO, Permissions.EXCLUIR_PROJETO,
            Permissions.EDITAR_PROJETO, Permissions.VISUALIZAR_PROJETO,
            Permissions.APROVAR_REQUISITO_PROJETO, Permissions.EXCLUIR_REQUISITO_PROJETO,
            Permissions.EDITAR_REQUISITO_PROJETO, Permissions.VISUALIZAR_REQUISITO_PROJETO,
            Permissions.APROVAR_DOCUMENTO_PROJETO, Permissions.EXCLUIR_DOCUMENTO_PROJETO,
            Permissions.EDITAR_DOCUMENTO_PROJETO, Permissions.VISUALIZAR_DOCUMENTO_PROJETO,
            Permissions.VALIDAR_PROJETO, Permissions.COMPLEMENTAR_REQUISITO));

    private List<Permissions> permissionsList;

    Roles(List<Permissions> permissionsList) {
        this.permissionsList = permissionsList;
    }


    public List<Permissions> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Permissions> permissionsList) {
        this.permissionsList = permissionsList;
    }
}
