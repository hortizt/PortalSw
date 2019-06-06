package main.groovy

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

}


def peticion= new Peticion(PT_IDPETICION: 1, PT_IDSERVICIO:2)

//println peticion

peticion.properties.each { key,value->
    //println key
    if ( !(key in ['class']) ) peticion[key]=''
}


Peticion.declaredFields.collect{
    if( it.name.substring(0,2)=='PT' ) println it.name
}

//println peticion