package main.groovy

class Peticion {
    def idpeticion
    def idservicio
    def numpeticion
    def tipopeticion
    def estadopeticion
    def estadoatiempo
    def codigoerror
    def mensajeerror
    def user
    def descripcion
    def fechacreacion
    String toString() { "Peticion[idpeticion=$idpeticion,idservicio=$idservicio,numpeticion=$numpeticion]" }
}


def peticion= new Peticion(idservicio: 1, idpeticion:2)

println peticion

peticion.properties.each { key,value->
    println key
    if (key != 'class') peticion[key]=''
}

println peticion