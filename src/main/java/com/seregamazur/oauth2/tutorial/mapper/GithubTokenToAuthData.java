package com.seregamazur.oauth2.tutorial.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring"
)
public interface GithubTokenToAuthData {

//    public interface DiagnosisMapper extends EntityMapper<DiagnosisDTO, Diagnosis> {
//        @Named("id")
//        @Mapping(target = "longitudinalPatientRecord", source = "longitudinalPatientRecord", qualifiedByName = "id")
//        @Mapping(target = "code", source = "code", qualifiedByName = "id")
//        @Mapping(target = "hccCode", source = "hccCode", qualifiedByName = "id")
//        @Mapping(target = "organization", source = "organization", qualifiedByName = "id")
//        DiagnosisDTO toDtoId(Diagnosis s);
//
//        DiagnosisDTO toDto(Diagnosis s);
//
//        @Named("idSet")
//        @BeanMapping(ignoreByDefault = true)
//        @Mapping(target = "id", source = "id")
//        Set<DiagnosisDTO> toDtoIdSet(Set<Diagnosis> diagnosis);
//    }
}
