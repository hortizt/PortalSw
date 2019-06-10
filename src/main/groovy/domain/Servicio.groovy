package main.groovy.domain

import groovy.beans.Bindable


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
}