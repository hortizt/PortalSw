package main.groovy.app

import main.groovy.util.DbUtilMQM
import main.groovy.util.Peticiones

import java.awt.BorderLayout
import javax.swing.BorderFactory
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import java.awt.GridLayout
import groovy.swing.SwingBuilder
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.*
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.beans.PropertyChangeListener
import groovy.beans.Bindable

def message
def panelPeticiones,panelServicios

def menu = [:]


ObservableList people = [ [name:"Mary", age:18], [name:"Tom", age:25] ]
def numPeticion
Font fontlbl = new Font("Courier", Font.PLAIN, 13)


@Bindable
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

class PeticionDet {
    def PD_IDDETALLE
    def PD_IDPETICION
    def PD_INTERFACE
    def PD_PROCESO
    def PD_SOURCE
    def PD_DESTINO
    def PD_ESTADOENVIO
    def PD_MESSAGEID
    def PD_CORRELATIONID
    def PD_CODIGOERROR
    def PD_MENSAJEERROR
    def PD_USER
    def PD_FECHACREACION
    def PD_FECHAMODIFICACION
    def PD_MENSAJEXML
    String toString() { "PeticionDet[PD_IDDETALLE=$PD_IDDETALLE,PD_IDPETICION=$PD_IDPETICION,PD_INTERFACE=$PD_INTERFACE]" }
}

ObservableList data  = []
/*
 [
        [PD_IDDETALLE:"",
         PD_IDPETICION:"",
         PD_INTERFACE:"",
         PD_PROCESO:"",
         PD_SOURCE:"",
         PD_DESTINO:"",
         PD_ESTADOENVIO:"",
         PD_MESSAGEID:"",
         PD_CORRELATIONID:"",
         PD_CODIGOERROR:"",
         PD_MENSAJEERROR:"",
         PD_USER:"",
         PD_FECHACREACION:"",
         PD_FECHAMODIFICACION:"",
         PD_MENSAJEXML:""],
        [PD_IDDETALLE:"",
         PD_IDPETICION:"",
         PD_INTERFACE:"",
         PD_PROCESO:"",
         PD_SOURCE:"",
         PD_DESTINO:"",
         PD_ESTADOENVIO:"",
         PD_MESSAGEID:"",
         PD_CORRELATIONID:"",
         PD_CODIGOERROR:"",
         PD_MENSAJEERROR:"",
         PD_USER:"",
         PD_FECHACREACION:"",
         PD_FECHAMODIFICACION:"",
         PD_MENSAJEXML:""]
         ]
*/


def peticion= new Peticion()
def peticionDet= new PeticionDet()

DbUtilMQM.bootStrap()
def peticiones = DbUtilMQM.sql.dataSet('PS_PETICION')
def peticionesDet = DbUtilMQM.sql.dataSet('PS_PETICIONDET')


swing = new SwingBuilder()
frame = swing.frame(title:'Demo',size:[1000,900]) {
    panel(id: 'principal', layout: new BorderLayout()) {
        panel(constraints: BorderLayout.WEST, border: compoundBorder([emptyBorder(10), titledBorder('Menu')])) {
            tree(createMenuTree()).getSelectionModel().addTreeSelectionListener({ e ->
                def seleccion1 = (e.source.selection).toString()
                if ((seleccion1.replace('[[', '').replace(']]', '').split(',')).size() == 3) {
                    sele2 = seleccion1.replace('[[', '').replace(']]', '').split(',')[2]
                    println sele2
                    menu.each { item -> item.value.visible = false }
                    menu[sele2.trim()].visible = true
                } else {
                    println "Nada"
                }
            })
        }
        panel(id: 'paneles', border: compoundBorder([emptyBorder(10), titledBorder('Informacion')])) {
            panelPeticiones =
                    panel(layout: new BorderLayout()) {
                        panel(constraints: BorderLayout.NORTH, border: compoundBorder([emptyBorder(10), titledBorder('Consulta:')])) {
                            tableLayout {
                                tr {
                                    td {
                                        label 'Numero de Peticion:'
                                    }
                                    td {
                                        textField id: 'numPeticionField', columns: 20
                                    }
                                    td {
                                        button text: 'enviar', actionPerformed: {
                                            peticion.properties.each { key, value ->
                                                if (!(key in ['class', 'propertyChangeListeners'])) peticion[key] = null
                                            }
                                            data.clear()
                                            def result = peticiones.rows().findAll {
                                                it.PT_NUMPETICION == numPeticionField.text.toInteger()
                                            }
                                            if (result.size() == 1) {
                                                aux = result[0]
                                                Peticion.declaredFields.collect {
                                                    if (it.name.substring(0, 2) == 'PT') peticion[it.name] = aux[it.name]
                                                }
                                                mensajelbl.text = '   '
                                                def resultDet = peticionesDet.rows().findAll{
                                                    it.PD_IDPETICION == peticion["PT_IDPETICION"]
                                                }.sort{it.PD_FECHACREACION}
                                                data.addAll(resultDet)
                                                mensajelbl.text = '   '
                                            } else {
                                                mensajelbl.text = 'No existe petición '
                                            }
                                        }
                                    }
                                    td {
                                        label text: '                                               '
                                    }
                                }
                                tr {
                                    td {
                                        label(id: 'mensajelbl', text: '   ').setForeground(Color.RED)
                                    }
                                }
                            }
                        }
                        panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Informacion de la peticion:')])) {
                            gridLayout(cols: 2, rows: 12)
                            Peticion.declaredFields.collect {
                                aux = it.name
                                if (it.name.substring(0, 2) == 'PT') {
                                    label aux + '   :  ', horizontalAlignment: JLabel.RIGHT
                                    label text: bind(source: peticion, sourceProperty: aux), horizontalAlignment: JLabel.LEFT, font: fontlbl
                                }
                            }
                        }
                        scrollPane (constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Detalle Transaccion:')])) {
                            table(id: 'tabla') {
                                tableModel(id: 'model', list: data) {
                                    propertyColumn(header: 'PD_INTERFACE', propertyName: 'PD_INTERFACE', editable: false)
                                    propertyColumn(header: 'PD_PROCESO', propertyName: 'PD_PROCESO', editable: false)
                                    propertyColumn(header: 'PD_CODIGOERROR', propertyName: 'PD_CODIGOERROR', editable: false)
                                    propertyColumn(header: 'PD_MENSAJEERROR', propertyName: 'PD_MENSAJEERROR', editable: false)
                                }
                            }
                        }
                        data.addPropertyChangeListener({ e -> model.fireTableDataChanged() })
                    }
            panelServicios =
                    panel() {
                        label("Panel Servicioes")
                    }
        }
    }

}

//frame.pack()
data.clear()
panelPeticiones.visible = false
panelServicios.visible = false
menu['Seguimiento Num Peticion']=panelPeticiones
menu['Peticiones por servicio']=panelServicios



frame.visible = true


JTree createMenuTree() {
    def topNode = new DefaultMutableTreeNode('Menu')
    def segServ = new DefaultMutableTreeNode('Seguimiento Servicios               ')

    segServ.add(new DefaultMutableTreeNode('Seguimiento Num Peticion'))
    segServ.add(new DefaultMutableTreeNode('Peticiones por servicio'))
    segServ.add(new DefaultMutableTreeNode('Servicios por cliente'))
    segServ.add(new DefaultMutableTreeNode('Datos Banda Ancha'))

    def maniobras = new DefaultMutableTreeNode('Maniobras')
    maniobras.add(new DefaultMutableTreeNode('Maniobras 1'))

    topNode.add(segServ)
    topNode.add(maniobras)
    projectTree = new JTree(topNode)
    return projectTree
}