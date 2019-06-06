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
                                                //println key
                                                if( !(key in ['class','propertyChangeListeners']) ) peticion[key]=null
                                            }
                                            def result = peticiones.rows().findAll {
                                                it.PT_NUMPETICION == numPeticionField.text.toInteger()
                                            }
                                            if (result.size() == 1){
                                                aux=result[0]
                                                /*
                                                peticion.properties.each { key,value->
                                                    if( !(key in ['class','propertyChangeListeners']) ) peticion[key]=aux[key]
                                                }
                                                 */
                                                Peticion.declaredFields.collect{
                                                    if( it.name.substring(0,2)=='PT' ) peticion[it.name]=aux[it.name]
                                                }
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
                               Peticion.declaredFields.collect{
                                   aux=it.name
                                   if( it.name.substring(0,2)=='PT' ) {
                                       tr {
                                           td {
                                               label aux                                           }
                                           td {
                                               textField(text: bind(source: peticion, sourceProperty: aux), columns: 20)
                                           }
                                       }
                                   }
                               }

                                /*
                               tr {
                                   td {
                                       label 'Num Servicio:'
                                   }
                                   td {
                                       textField(text: bind(source: peticion, sourceProperty: 'PT_IDSERVICIO'), columns: 20)
                                    }
                                }
                                tr {
                                    td {
                                        label 'Id Peticion:'
                                    }
                                    td {
                                        textField(text: bind(source: peticion, sourceProperty: 'PT_IDPETICION'), columns: 20)
                                    }
                                }
                               tr {
                                   td {
                                       label 'Num Peticion:'
                                   }
                                   td {
                                       textField(text: bind(source: peticion, sourceProperty: 'PT_NUMPETICION'), columns: 20)
                                   }
                               }
                                */

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