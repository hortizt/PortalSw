package main.groovy.app.MainSwing
import main.groovy.app.MainSwing

def swing=MainSwing.getMainSwing()
assert swing
swing.frame.visible=false

// panelPeticiones
//OK
swing.numPeticionField.text='904100920'
swing.btnEnviarPeticion.doClick() ;
assert swing.PT_IDSERVICIO.text =='44100920'
assert swing.PT_TIPOPETICION.text =='Alta'
assert swing.PT_CODIGOERROR.text =='0000'
assert swing.modelPet.rowsModel.value[0].PD_INTERFACE=="INT.SIGRES.CRM.001"
assert swing.modelPet.rowsModel.value[6].PD_INTERFACE=="INT.SIGRES.CRM.004"
//NOOK
swing.numPeticionField.text='9041009'
swing.btnEnviarPeticion.doClick() ;
assert swing.mensajePetlbl.text == 'No existe petición'

// panelServicios
//OK
swing.numServicioField.text='44100920'
swing.btnEnviarServicio.doClick() ;
assert swing.SE_NUMSERVICIO.text =='44100920'
assert swing.SE_CODIGOSERVICIO.text =='2991'
assert swing.SE_ESTADOSERVICIO.text =='Desactivado'
assert swing.modelPetServ.rowsModel.value[0].PT_ESTADOPETICION=='Finalizado Exitoso'