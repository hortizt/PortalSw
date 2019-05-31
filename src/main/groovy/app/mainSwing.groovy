package main.groovy.app

import java.awt.BorderLayout
import javax.swing.BorderFactory
import java.awt.GridLayout
import groovy.swing.SwingBuilder
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.*
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


JTree createMenuTree() {
    def topNode = new DefaultMutableTreeNode('Menu')
    def segServ = new DefaultMutableTreeNode('Seguimiento Servicios')

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

swing = new SwingBuilder()
frame = swing.frame(title:'Demo',size:[1000,1000]) {

    panel(layout: new BorderLayout()){

        scrollPane(constraints: BorderLayout.WEST){

           tree(createMenuTree() ).getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
               @Override
               public void valueChanged(TreeSelectionEvent e) {
                   def seleccion1= (e.source.selection).toString()
                   def sele2=seleccion1.replace('[[','').replace(']]','').split(',')[2]
                   println sele2
               }
           });
        }

        panel(constraints: BorderLayout.EAST){
            scrollPane(constraints: BorderLayout.CENTER){
                textArea(id:'TextArea', lineWrap:true,wrapStyleWord:true, columns:35, rows:4,editable:true)
            }
        }

    }


}
//frame.pack()
frame.visible = true