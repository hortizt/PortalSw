package main.groovy.domain

import groovy.beans.Bindable
import main.groovy.util.DbUtilMQM


@Bindable
class Peticion {
    def PT_IDPETICION
    def PT_IDSERVICIO
    def PT_NUMPETICION
    def PT_TIPOPETICION
    def PT_ESTADOPETICION
    def PT_ESTADOATIEMPO
    def PT_CODIGOERROR
    def PT_MENSAJEERROR
    def PT_USER
    def PT_DESCRIPCION
    def PT_FECHACREACION
    def PT_FECHAMODIFICACION
    String toString() { "Peticion[idpeticion=$PT_IDPETICION,idservicio=$PT_IDSERVICIO,numpeticion=$PT_NUMPETICION]" }

    static Peticion getPeticion(numPeticion){
        def result = DbUtilMQM.sql.dataSet('PS_PETICION').rows().findAll {
            it.PT_NUMPETICION == numPeticion
        }
        if (result.size() == 1)
            return result[0]
        else    null

    }

    static void setValores(peticion,auxPeticion) {
        Peticion.declaredFields.collect {
            if (it.name.substring(0, 2) == 'PT') peticion[it.name] = auxPeticion[it.name]
        }
    }

    static void clearValores(peticion){
        Peticion.declaredFields.collect {
            if (it.name.substring(0, 2) == 'PT') peticion[it.name] = null
        }
    }

}