package main.groovy.util

import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode

class Menu {

     static accionSeleccionMenu={e,menu->
         def seleccion1 = (e?.source?.selection).toString()
         if ((seleccion1.replace('[[', '').replace(']]', '').split(',')).size() == 3) {
             def sele2 = seleccion1.replace('[[', '').replace(']]', '').split(',')[2]
             println sele2
             menu.each { item -> item.value.visible = false }
             menu[sele2.trim()].visible = true
         } else {
             println "Nada"
         }
     }


    static JTree createMenuTree() {
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
        def projectTree = new JTree(topNode)
        return projectTree
    }

    static ButtonPeticionEnviar={

    }

}
