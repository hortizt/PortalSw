package main.groovy.app

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

def message
def panelPeticiones,panelServicios

def menu = [:]



def setMessage = { String s -> message.setText(s) }

ObservableList   data = [
        [name: 'Anthony', color: 'mediumBlue'],
        [name: 'Greg', color: 'brightYellow'],
        [name: 'Jeff', color: 'purple'],
        [name: 'Murray', color: 'brightRed']
]
swing = new SwingBuilder()
frame = swing.frame(title:'Demo',size:[1000,1000]) {

    panel(id:'principal', layout: new BorderLayout()){
        panel(constraints:BorderLayout.WEST, border: compoundBorder([emptyBorder(10), titledBorder('Menu')])){
           tree(createMenuTree() ).getSelectionModel().addTreeSelectionListener({e->
                   def seleccion1= (e.source.selection).toString()
                   if ((seleccion1.replace('[[','').replace(']]','').split(',')).size()==3 ){
                       sele2=seleccion1.replace('[[','').replace(']]','').split(',')[2]
                       println sele2
                       menu.each{ item->item.value.visible=false}
                       menu[sele2.trim()].visible=true
                   } else
                        {
                        println "Nada"
                   }
           })
        }
        panel(id:'paneles',border: compoundBorder([emptyBorder(10), titledBorder('Informacion')])) {
//       panel(id:'paneles',layout: new BorderLayout(),constraints:BorderLayout.EAST) {

            panelPeticiones =
                    panel(layout: new BorderLayout()) {
                        panel(constraints:BorderLayout.NORTH, border: compoundBorder([emptyBorder(10), titledBorder('Consulta:')])){
                            label("Numero de peticion                                                                                                                                 ")
                        }
                        panel(constraints:BorderLayout.CENTER,border: compoundBorder([emptyBorder(10), titledBorder('Informacion de la peticion:')])){
                            label("Información de la peticion")
                        }
                        scrollPane(constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Detalle Transaccion:')])) {
                             table(id: 'tabla') {
                                tableModel(id: 'model', list: data) {
                                    propertyColumn(header: 'Name', propertyName: 'name')
                                    propertyColumn(header: 'Color', propertyName: 'color',)
                                }
                                data.addPropertyChangeListener({ e -> model.fireTableDataChanged() })
                            }
                        }
                    }
            panelServicios =
                    panel() {
                        label("Panel Servicioes")
                    }


        }
    }
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