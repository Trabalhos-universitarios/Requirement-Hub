package com.br.requirementhub.enums;

import java.util.Arrays;
import java.util.List;

public enum Role {
    GERENTE_DE_PROJETOS(Arrays.asList(Permission.APROVAR_REQUISITO, Permission.EXCLUIR_REQUISITO,
            Permission.EDITAR_REQUISITO, Permission.VISUALIZAR_REQUISITO,
            Permission.APROVAR_DOCUMENTO, Permission.EXCLUIR_DOCUMENTO,
            Permission.EDITAR_DOCUMENTO, Permission.VISUALIZAR_DOCUMENTO,
            Permission.APROVAR_PROJETO, Permission.EXCLUIR_PROJETO,
            Permission.EDITAR_PROJETO, Permission.VISUALIZAR_PROJETO,
            Permission.APROVAR_REQUISITO_PROJETO, Permission.EXCLUIR_REQUISITO_PROJETO,
            Permission.EDITAR_REQUISITO_PROJETO, Permission.VISUALIZAR_REQUISITO_PROJETO,
            Permission.APROVAR_DOCUMENTO_PROJETO, Permission.EXCLUIR_DOCUMENTO_PROJETO,
            Permission.EDITAR_DOCUMENTO_PROJETO, Permission.VISUALIZAR_DOCUMENTO_PROJETO,
            Permission.VALIDAR_PROJETO, Permission.COMPLEMENTAR_REQUISITO)),
    ANALISTA_DE_REQUISITOS(Arrays.asList(Permission.APROVAR_REQUISITO, Permission.EXCLUIR_REQUISITO,
            Permission.EDITAR_REQUISITO, Permission.VISUALIZAR_REQUISITO,
            Permission.APROVAR_DOCUMENTO, Permission.EXCLUIR_DOCUMENTO,
            Permission.EDITAR_DOCUMENTO, Permission.VISUALIZAR_DOCUMENTO,
            Permission.APROVAR_PROJETO, Permission.EXCLUIR_PROJETO,
            Permission.EDITAR_PROJETO, Permission.VISUALIZAR_PROJETO,
            Permission.APROVAR_REQUISITO_PROJETO, Permission.EXCLUIR_REQUISITO_PROJETO,
            Permission.EDITAR_REQUISITO_PROJETO, Permission.VISUALIZAR_REQUISITO_PROJETO,
            Permission.APROVAR_DOCUMENTO_PROJETO, Permission.EXCLUIR_DOCUMENTO_PROJETO,
            Permission.EDITAR_DOCUMENTO_PROJETO, Permission.VISUALIZAR_DOCUMENTO_PROJETO,
            Permission.VALIDAR_PROJETO, Permission.COMPLEMENTAR_REQUISITO)),
    ANALISTA_DE_NEGOCIO(Arrays.asList(Permission.APROVAR_REQUISITO, Permission.EXCLUIR_REQUISITO,
            Permission.EDITAR_REQUISITO, Permission.VISUALIZAR_REQUISITO,
            Permission.APROVAR_DOCUMENTO, Permission.EXCLUIR_DOCUMENTO,
            Permission.EDITAR_DOCUMENTO, Permission.VISUALIZAR_DOCUMENTO,
            Permission.APROVAR_PROJETO, Permission.EXCLUIR_PROJETO,
            Permission.EDITAR_PROJETO, Permission.VISUALIZAR_PROJETO,
            Permission.APROVAR_REQUISITO_PROJETO, Permission.EXCLUIR_REQUISITO_PROJETO,
            Permission.EDITAR_REQUISITO_PROJETO, Permission.VISUALIZAR_REQUISITO_PROJETO,
            Permission.APROVAR_DOCUMENTO_PROJETO, Permission.EXCLUIR_DOCUMENTO_PROJETO,
            Permission.EDITAR_DOCUMENTO_PROJETO, Permission.VISUALIZAR_DOCUMENTO_PROJETO,
            Permission.VALIDAR_PROJETO, Permission.COMPLEMENTAR_REQUISITO)),
    USUARIO_COMUM(Arrays.asList(Permission.VISUALIZAR_REQUISITO, Permission.VISUALIZAR_DOCUMENTO,
            Permission.VISUALIZAR_PROJETO, Permission.VISUALIZAR_REQUISITO_PROJETO,
            Permission.VISUALIZAR_DOCUMENTO_PROJETO)),
    ADMIN(Arrays.asList(Permission.APROVAR_REQUISITO, Permission.EXCLUIR_REQUISITO,
            Permission.EDITAR_REQUISITO, Permission.VISUALIZAR_REQUISITO,
            Permission.APROVAR_DOCUMENTO, Permission.EXCLUIR_DOCUMENTO,
            Permission.EDITAR_DOCUMENTO, Permission.VISUALIZAR_DOCUMENTO,
            Permission.APROVAR_PROJETO, Permission.EXCLUIR_PROJETO,
            Permission.EDITAR_PROJETO, Permission.VISUALIZAR_PROJETO,
            Permission.APROVAR_REQUISITO_PROJETO, Permission.EXCLUIR_REQUISITO_PROJETO,
            Permission.EDITAR_REQUISITO_PROJETO, Permission.VISUALIZAR_REQUISITO_PROJETO,
            Permission.APROVAR_DOCUMENTO_PROJETO, Permission.EXCLUIR_DOCUMENTO_PROJETO,
            Permission.EDITAR_DOCUMENTO_PROJETO, Permission.VISUALIZAR_DOCUMENTO_PROJETO,
            Permission.VALIDAR_PROJETO, Permission.COMPLEMENTAR_REQUISITO,
            Permission.MANAGE_USERS));

    private List<Permission> permissionList;

    Role(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<Permission> getPermissionsList() {
        return permissionList;
    }

    public void setPermissionsList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }
}
