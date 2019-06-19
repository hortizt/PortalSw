package main.groovy.domain

import groovy.beans.Bindable
import main.groovy.util.DbUtilInventario

@Bindable
class DatosBA {
    def SERVICIO       
    def CLIENTE        
    def ESTADO_SER     
    def TELEFONO       
    def CTA_PADRE      
    def PS             
    def AB             
    def ZA             
    def IP_DSLAM       
    def SHELF          
    def SLOT           
    def NOMBRE_PF      
    def PERFIL_PF      
    def PERFIL_ASSIA_PF
    def ESTADO_PF      
    def POTS           
    def ADSL           
    def IP_LAN         
    def IP_WAN         
    def MASCARA_IP

    static DatosBA getDatosBAByCca(valorBusqueda){
        def result = DbUtilInventario.sql.dataSet('CONSULTA_DATOS_BA').rows().findAll {
            it.CLIENTE == valorBusqueda
        }
        if (result.size() == 1)
            return result[0]
        else    null
    }

    static DatosBA getDatosBAByServ(valorBusqueda){
        def result = DbUtilInventario.sql.dataSet('CONSULTA_DATOS_BA').rows().findAll {
            it.SERVICIO == valorBusqueda
        }
        if (result.size() == 1)
            return result[0]
        else    null
    }

    static void setValores(datosBA,auxDatosBA) {
        DatosBA.declaredFields[0..19].collect { datosBA[it.name] = auxDatosBA[it.name]}
    }

    static void clearValores(datosBA){
        DatosBA.declaredFields[0..19].collect { datosBA[it.name] = null}

    }
}

