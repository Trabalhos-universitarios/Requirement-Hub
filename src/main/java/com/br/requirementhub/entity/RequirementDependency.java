//package com.br.requirementhub.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import jakarta.persistence.EmbeddedId;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
//@Entity
//@Table(name = "requirement_dependency")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class RequirementDependency {
//
//    @EmbeddedId
//    private RequirementDependencyId id;
//
//    @Embeddable
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class RequirementDependencyId implements Serializable {
//
//        @Column(name = "requirement_id")
//        private Long requirementId;
//
//        @Column(name = "dependency_id")
//        private Long dependencyId;
//    }
//}