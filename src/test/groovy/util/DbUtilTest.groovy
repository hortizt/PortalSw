package test.groovy.util

import main.groovy.util.DbUtilInventario
import main.groovy.util.DbUtilMQM

class DbUtilTest extends GroovyTestCase {

    def getConfig(){
        URL resource = getClass().getClassLoader().getResource("config.xml")
        def configFile = new File(resource.getFile());
        return new XmlParser().parse(configFile)
    }


    void testBootStrapInventario() {
        DbUtilInventario.bootStrap(getConfig())
        assert DbUtilInventario.sql.rows('SELECT * FROM CONSULTA_DATOS_BA').size() ==1
        DbUtilInventario.close()

    }

    void testMQMQuery() {
        DbUtilMQM.bootStrap(getConfig())
        def peticiones = DbUtilMQM.sql.dataSet('PS_PETICION')
        def result= peticiones.rows().findAll{it.PT_NUMPETICION==904100920}
        assert result
        DbUtilMQM.close()
    }

    void testConfig() {

        assert DbUtilMQM.url=='jdbc:hsqldb:mem:Inventario'
        assert DbUtilMQM.driver=='org.hsqldb.jdbcDriver'
        assert DbUtilMQM.user=='sa'
        assert DbUtilMQM.password==''

        assert DbUtilInventario.url=='jdbc:hsqldb:mem:Inventario'
        assert DbUtilInventario.driver=='org.hsqldb.jdbcDriver'
        assert DbUtilInventario.user=='sa'
        assert DbUtilInventario.password==''

    }
}
