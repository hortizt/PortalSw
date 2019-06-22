package main.groovy.util

import main.groovy.domain.ParametrosDet
import main.groovy.domain.Peticion
import main.groovy.domain.PeticionDet
import main.groovy.domain.Servicio
import main.groovy.domain.DatosBA

import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode

class Menu {
/*
     static accionSeleccionMenu={e,menu->
         def seleccion1 = (e?.source?.selection).toString()
         if ((seleccion1.replace('[[', '').replace(']]', '').split(',')).size() == 3) {
             def sele2 = seleccion1.replace('[[', '').replace(']]', '').split(',')[2]
             println sele2
             menu.each { item -> item.value.visible = false }
             menu[sele2.trim()].visible = true
         } else {
             menu.each { item -> item.value.visible = false }
             menu['Panel Vacio'].visible=true
         }
     }
*/
    static JTree createMenuTree() {
        def topNode = new DefaultMutableTreeNode('Menu')
        def segServ = new DefaultMutableTreeNode('Seguimiento Servicios               ')

        segServ.add(new DefaultMutableTreeNode('Seguimiento Num Peticion'))
        segServ.add(new DefaultMutableTreeNode('Peticiones por servicio'))
        segServ.add(new DefaultMutableTreeNode('Datos Banda Ancha'))

        //def maniobras = new DefaultMutableTreeNode('Maniobras')
        //maniobras.add(new DefaultMutableTreeNode('Maniobras 1'))

        topNode.add(segServ)
        //topNode.add(maniobras)
        def projectTree = new JTree(topNode)
        return projectTree
    }

    static String btnEnviarPeticionAccion(Integer numPeticion, Peticion peticion, ObservableList dataPeticionDet)
    {
        Peticion.clearValores(peticion)
        dataPeticionDet.clear()
        def auxPeticion = Peticion.getPeticionbyNumPeticion(numPeticion)
        if (auxPeticion != null) {
            Peticion.setValores(peticion, auxPeticion)
            peticion.PT_IDSERVICIO = Servicio.getServicioIdServicio(peticion.PT_IDSERVICIO).SE_NUMSERVICIO
            peticion.PT_TIPOPETICION = ParametrosDet.getTipoPeticion(peticion.PT_TIPOPETICION)
            peticion.PT_ESTADOPETICION = ParametrosDet.getEstadoPeticion(peticion.PT_ESTADOPETICION)
            peticion.PT_ESTADOATIEMPO = ParametrosDet.getEstadoPeticionAtiempo(peticion.PT_ESTADOATIEMPO)
            peticion.PT_CODIGOERROR = peticion.PT_CODIGOERROR.padRight(4, '0')
            dataPeticionDet.addAll(PeticionDet.getPeticionDet(peticion.PT_IDPETICION))
            return "OK"
        } else {
            return "NOOK"
        }
    }

    static String btnEnviarServicioAccion(String numServicio, Servicio servicio, ObservableList dataPetServ) {
        Servicio.clearValores(servicio)
        dataPetServ.clear()
        def auxServicio = Servicio.getServicioNumServicio(numServicio)
        if (auxServicio != null) {
            Servicio.setValores(servicio, auxServicio)
            servicio.SE_ESTADOSERVICIO = ParametrosDet.getEstadoServicio(servicio.SE_ESTADOSERVICIO)
            def resultPet = Peticion.getPeticionbyNumServicio(servicio["SE_IDSERVICIO"])
            resultPet.each { itPet ->
                itPet.PT_TIPOPETICION = ParametrosDet.getTipoPeticion(itPet.PT_TIPOPETICION)
                itPet.PT_ESTADOPETICION = ParametrosDet.getEstadoPeticion(itPet.PT_ESTADOPETICION)
                itPet.PT_ESTADOATIEMPO = ParametrosDet.getEstadoPeticionAtiempo(itPet.PT_ESTADOATIEMPO)
                itPet.PT_CODIGOERROR = itPet.PT_CODIGOERROR.padRight(4, '0')
            }
            dataPetServ.addAll(resultPet)
            return "OK"
        } else {
            return "NOOK"
        }
    }

    static String btnEnviarDatosBAAccion(String valorBusqueda, DatosBA datosBA, boolean ccaSelected) {
        def auxDatosBA
        DatosBA.clearValores(datosBA)

        if (ccaSelected) {
            auxDatosBA = DatosBA.getDatosBAByCca(valorBusqueda)
        } else {
            auxDatosBA = DatosBA.getDatosBAByServ(valorBusqueda)
        }
        if (auxDatosBA) {
            DatosBA.setValores(datosBA, auxDatosBA)
            return "OK"
        } else {
            return "NOOK"
        }
    }
}
