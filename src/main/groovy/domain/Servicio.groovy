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
    static Servicio getServicio(idServicio){
        def servicio = DbUtilMQM.sql.dataSet('PS_SERVICIO').rows().findAll{
            idServicio=it.SE_IDSERVICIO
        }
        return servicio[0]
    }
}