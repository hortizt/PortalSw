package test.groovy.util

import main.groovy.app.MainSwing
import main.groovy.domain.DatosBA
import main.groovy.util.DbUtilInventario
import main.groovy.util.DbUtilMQM


class mainUITest extends GroovyTestCase {
    def Config;
    def swing;

    void setUp(){
        Config = new XmlParser().parse(new File(getClass().getClassLoader().getResource("config.xml").getFile()))

        DbUtilMQM.bootStrap(Config)


        swing = MainSwing.getMainSwing()
        assert swing
        swing.frame.visible = false
    }
    void testpanelPeticiones(){
        // panelPeticiones
        //OK
        swing.numPeticionField.text = '904100920'
        swing.btnEnviarPeticion.doClick();
        assert swing.PT_IDSERVICIO.text == '44100920'
        assert swing.PT_TIPOPETICION.text == 'Alta'
        assert swing.PT_CODIGOERROR.text == '0000'
        assert swing.modelPet.rowsModel.value[0].PD_INTERFACE == "INT.SIGRES.CRM.001"
        assert swing.modelPet.rowsModel.value[6].PD_INTERFACE == "INT.SIGRES.CRM.004"
        //NOOK
        swing.numPeticionField.text = '9041009'
        swing.btnEnviarPeticion.doClick();
        assert swing.mensajePetlbl.text == 'No existe petición'
    }

    void testpanelServicios(){
        // panelServicios
        //OK
        swing.numServicioField.text = '44100920'
        swing.btnEnviarServicio.doClick();
        assert swing.SE_NUMSERVICIO.text == '44100920'
        assert swing.SE_CODIGOSERVICIO.text == '2991'
        assert swing.SE_ESTADOSERVICIO.text == 'Desactivado'
        assert swing.modelPetServ.rowsModel.value[0].PT_ESTADOPETICION == 'Finalizado Exitoso'
    }

    void testDatosBA(){
        // Panel Datos BA
        DbUtilInventario.bootStrap(Config)
        swing.numValorBusquedaField.text='CC7163548'
        swing.btnEnviarDatosBA.doClick();
        assert swing.SERVICIO.text =='004392532'
        assert swing.CLIENTE.text =='CC7163548'
        assert swing.ESTADO_SER.text =='EN SERVICIO'
        assert swing.TELEFONO.text=='87432124'
    }

    void testDatosBAMod(){
        // Panel Datos BA
        DbUtilInventario.bootStrap(Config)
        swing.numValorBusquedaField.text='CC7163548'
        swing.btnEnviarDatosBA.doClick();
        assert swing.ESTADO_SER.text =='EN SERVICIO'

        DbUtilInventario.sql.execute '''
                UPDATE CONSULTA_DATOS_BA SET ESTADO_SER='XXXX'
        '''
        swing.btnEnviarDatosBA.doClick();
        assert swing.ESTADO_SER.text =='XXXX'
        DbUtilInventario.sql.execute '''
                UPDATE CONSULTA_DATOS_BA SET ESTADO_SER='EN SERVICIO'
        '''
    }
}