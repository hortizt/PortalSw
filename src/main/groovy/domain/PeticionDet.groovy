package main.groovy.domain

import main.groovy.util.DbUtilMQM

class PeticionDet {
    def PD_IDDETALLE
    def PD_IDPETICION
    def PD_INTERFACE
    def PD_PROCESO
    def PD_SOURCE
    def PD_DESTINO
    def PD_ESTADOENVIO
    def PD_MESSAGEID
    def PD_CORRELATIONID
    def PD_CODIGOERROR
    def PD_MENSAJEERROR
    def PD_USER
    def PD_FECHACREACION
    def PD_FECHAMODIFICACION
    def PD_MENSAJEXML
    String toString() { "PeticionDet[PD_IDDETALLE=$PD_IDDETALLE,PD_IDPETICION=$PD_IDPETICION,PD_INTERFACE=$PD_INTERFACE]" }

    static List<PeticionDet> getPeticionDet(idPeticion){
        def resultDet = DbUtilMQM.sql.dataSet('PS_PETICIONDET').rows().findAll{
            it.PD_IDPETICION == idPeticion
        }.sort{it.PD_FECHACREACION}
        return resultDet
    }

}