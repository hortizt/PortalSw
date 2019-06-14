package main.groovy.domain

import groovy.beans.Bindable
import main.groovy.util.DbUtilMQM


@Bindable
class Servicio {
    def SE_IDSERVICIO
    def SE_IDCLIENTE
    def SE_NUMSERVICIO
    def SE_NUMTELEFONO
    def SE_CODIGOSERVICIO
    def SE_ZONAATENDIMIENTO
    def SE_CANTIDADIP
    def SE_ESTADOSERVICIO
    def SE_USERNAME
    def SE_USER
    def SE_FECHACREACION
    def SE_FECHAMODIFICACION
    String toString() { "Servicio[SE_IDSERVICIO=$SE_IDSERVICIO,SE_NUMSERVICIO=$SE_NUMSERVICIO,SE_NUMTELEFONO=$SE_NUMTELEFONO]" }
    static Servicio getServicioIdServicio(idServicio){
        def servicio = DbUtilMQM.sql.dataSet('PS_SERVICIO').rows().findAll{
            idServicio=it.SE_IDSERVICIO
        }
        return servicio[0]
    }

    static Servicio getServicioNumServicio(numServicio){

        def servicio = DbUtilMQM.sql.dataSet('PS_SERVICIO').rows().findAll{
            it.SE_NUMSERVICIO == numServicio
        }
        return servicio[0]
    }

    static void setValores(servicio,auxServicio) {
        Servicio.declaredFields.collect {
            if (it.name.substring(0, 2) == 'SE') servicio[it.name] = auxServicio[it.name]
        }
    }

    static void clearValores(servicio){
        Servicio.declaredFields.collect {
            if (it.name.substring(0, 2) == 'SE') servicio[it.name] = null
        }
    }
}