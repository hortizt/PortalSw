package main.groovy.app

import main.groovy.util.DbUtilMQM
import main.groovy.util.Peticiones

import java.awt.BorderLayout
import javax.swing.BorderFactory
import java.awt.FlowLayout
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


def numPeticion

@Bindable
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

def peticion= new Peticion()


DbUtilMQM.bootStrap()
def peticiones = DbUtilMQM.sql.dataSet('PS_PETICION')

swing = new SwingBuilder()
frame = swing.frame(title:'Demo',size:[1000,800]) {
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
                                            peticion.properties.each { key,value->
                                                println key
                                                if (key != 'class') peticion[key]=''
                                            }
                                            def result = peticiones.rows().findAll {
                                                it.PT_NUMPETICION == numPeticionField.text.toInteger()
                                            }
                                            if (result.size() == 1){
                                                def aux= result[0]
                                                peticion.idservicio=aux.PT_IDSERVICIO
                                                peticion.idpeticion=aux.PT_IDPETICION
                                                mensajelbl.text='   '
                                            } else
                                            {
                                                mensajelbl.text='No existe petición '

                                            }

                                        }
                                    }
                                    td {
                                        label  text: '                                               '
                                    }
                                }
                                tr {
                                    td {
                                        label id:'mensajelbl', text: '   '
                                    }
                                }
                            }
                        }
                        panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Informacion de la peticion:')])) {
                           tableLayout {
                               tr {
                                   td {
                                       label 'Num Servicio:'
                                   }
                                   td {
                                       textField(text: bind(source: peticion, sourceProperty: 'idservicio'), columns: 20)
                                    }
                                }
                                tr {
                                    td {
                                        label 'Num Peticion:'
                                    }
                                    td {
                                        textField(text: bind(source: peticion, sourceProperty: 'idpeticion'), columns: 20)
                                    }
                                }
                               }
                        }
                        panel(constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Detalle Transaccion:')])) {
                            /*
                            table(id: 'tabla') {
                                tableModel(id: 'model', list: data) {
                                    propertyColumn(header: 'Name', propertyName: 'name')
                                    propertyColumn(header: 'Color', propertyName: 'color',)
                                }
                                data.addPropertyChangeListener({ e -> model.fireTableDataChanged() })
                            }
                                                         */

                        }
                    }
            panelServicios =
                    panel() {
                        label("Panel Servicioes")
                    }
        }
    }

/*    bean peticion,
            idpeticion: bind { idpeticionField.text },
            idservicio: bind { idservicioField.text }
            */


}

//frame.pack()
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