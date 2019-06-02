package test.groovy.util

import main.groovy.util.DbUtilInventario
import main.groovy.util.DbUtilMQM

class DbUtilTest extends GroovyTestCase {
    void testDbUtilinit() {
        //assert DbUtilMQM.init() != null, "error"
        //assert DbUtilInventario.init() != null, "error"
    }
    void testBootStrapMQM() {
        //DbUtilMQM.bootStrap()
        //assert DbUtilMQM.sql.rows('SELECT * FROM PS_PETICION').size() ==2
        //assert DbUtilMQM.sql.rows('SELECT * FROM PS_PETICIONDET').size() ==7
        //assert DbUtilMQM.sql.rows('SELECT * FROM PS_CLIENTE').size() ==1
        //assert DbUtilMQM.sql.rows('SELECT * FROM PS_SERVICIO').size() ==1
        //assert DbUtilMQM.sql.rows('SELECT * FROM PS_DATOSAPROVISIONADO').size() ==1
    }

    void testBootStrapInventario() {
        DbUtilInventario.bootStrap()
        assert DbUtilInventario.sql.rows('SELECT * FROM CONSULTA_DATOS_BA').size() ==1

    }

    void testMQMQuery() {
        DbUtilMQM.bootStrap()
        def peticiones = DbUtilMQM.sql.dataSet('PS_PETICION')
        println peticiones.rows().collect { it.PT_IDPETICION }
        def result= peticiones.rows().findAll{it.PT_NUMPETICION==904100920}
        println result.each {println it.PT_IDPETICION}
    }


}
