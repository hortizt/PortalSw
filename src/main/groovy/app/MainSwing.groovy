package main.groovy.app

import javax.swing.event.TreeSelectionListener
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import groovy.swing.SwingBuilder
import javax.swing.*
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import javax.swing.event.TreeSelectionEvent
import main.groovy.domain.Peticion
import main.groovy.domain.Servicio
import main.groovy.domain.DatosBA
import main.groovy.util.Menu

import java.beans.PropertyChangeEvent

class MainSwing {

    static SwingBuilder getMainSwing() {

        def panelPeticiones, panelServicios, panelDatosBA, panelVacio

        def menu = [:]
        Font fontlbl = new Font("Courier", Font.PLAIN, 13)
        ObservableList dataPeticionDet = []
        ObservableList dataPetServ = []

        def peticion = new Peticion()
        def servicio = new Servicio()
        def datosBA = new DatosBA()



        def swing = new SwingBuilder()
        def frame = swing.frame(id:'frame', title: 'Demo', size: [1000, 900]) {
            panel(id: 'panelPrincipal', layout: new BorderLayout()) {
                panel(id:'panelMenu',constraints: BorderLayout.WEST, border: compoundBorder([emptyBorder(10), titledBorder('Menu')])) {
                    tree(id:'treeMenu', Menu.createMenuTree()).getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
                        public void valueChanged(TreeSelectionEvent e) {
                            def seleccion1 = (e?.source?.selection).toString()
                            if ((seleccion1.replace('[[', '').replace(']]', '').split(',')).size() == 3) {
                                def sele2 = seleccion1.replace('[[', '').replace(']]', '').split(',')[2]
                                //println sele2
                                menu.each { item -> item.value.visible = false }
                                menu[sele2.trim()].visible = true
                            } else {
                                menu.each { item -> item.value.visible = false }
                                menu['Panel Vacio'].visible = true
                            }
                        }

                    })
                }
                panel(id: 'paneles', border: compoundBorder([emptyBorder(10), titledBorder('Informacion')])) {
                    panelPeticiones =
                            panel(id:'panelPeticiones',layout: new BorderLayout()) {
                                panel(constraints: BorderLayout.NORTH, border: compoundBorder([emptyBorder(10), titledBorder('Consulta:')])) {
                                    tableLayout {
                                        tr {
                                            td { label 'Numero de Peticion:' }
                                            td { textField id: 'numPeticionField', columns: 20 }
                                            td { button id:'btnEnviarPeticion', text: 'enviar', actionPerformed:{
                                                    if (Menu.btnEnviarPeticionAccion(numPeticionField.text.toInteger(), peticion,dataPeticionDet)=="OK"){
                                                        mensajePetlbl.text = '      '
                                                    } else {
                                                        mensajePetlbl.text = 'No existe petición'
                                                    }
                                                }
                                                }
                                            td { label text: '                                               ' }
                                        }
                                        tr {
                                            td { label(id: 'mensajePetlbl', text: '   ').setForeground(Color.RED)   }
                                        }
                                    }
                                }
                                panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Informacion de la peticion:')])) {
                                    gridLayout(cols: 2, rows: 12)
                                    Peticion.declaredFields.collect {
                                        aux = it.name
                                        if (it.name.substring(0, 2) == 'PT') {
                                            label aux + '   :  ', horizontalAlignment: JLabel.RIGHT
                                            label id:aux, text: bind(source: peticion, sourceProperty: aux), horizontalAlignment: JLabel.LEFT, font: fontlbl
                                        }
                                    }
                                }
                                scrollPane(constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Detalle Transaccion:')])) {
                                    table(id: 'tabla') {
                                        tableModel(id: 'modelPet', list: dataPeticionDet) {
                                            propertyColumn(header: 'PD_INTERFACE', propertyName: 'PD_INTERFACE', editable: false)
                                            propertyColumn(header: 'PD_PROCESO', propertyName: 'PD_PROCESO', editable: false)
                                            propertyColumn(header: 'PD_CODIGOERROR', propertyName: 'PD_CODIGOERROR', editable: false)
                                            propertyColumn(header: 'PD_MENSAJEERROR', propertyName: 'PD_MENSAJEERROR', editable: false)
                                        }
                                    }
                                }
                                def auxModelPet=modelPet
                                dataPeticionDet.addPropertyChangeListener(
                                        //{ e -> modelPet.fireTableDataChanged() }
                                        new PropertyChangeListener(){
                                            public void propertyChange(PropertyChangeEvent e)
                                            {
                                                auxModelPet.fireTableDataChanged()
                                            }
                                        }
                                )
                            }
                    panelServicios =
                            panel(id:'panelPeticiones',layout: new BorderLayout()) {
                                panel(constraints: BorderLayout.NORTH, border: compoundBorder([emptyBorder(10), titledBorder('Consulta:')])) {
                                    tableLayout {
                                        tr {
                                            td { label 'Numero de Teléfono:' }
                                            td { textField id: 'numServicioField', columns: 20 }
                                            td { button text:'enviar', id:'btnEnviarServicio', actionPerformed: {
                                                    if(Menu.btnEnviarServicioAccion(numServicioField.text, servicio, dataPetServ)=="OK"){
                                                        mensajePetlbl.text = '   '
                                                    } else {
                                                        mensajePetlbl.text = 'No existe el servicio '
                                                    }
                                                }
                                            }
                                            td { label text: '                                               ' }
                                        }
                                        tr {
                                            td { label(id: 'mensajelbl', text: '   ').setForeground(Color.RED)  }
                                        }
                                    }
                                }
                                panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Informacion del servicio:')])) {
                                    gridLayout(cols: 2, rows: 12)
                                    Servicio.declaredFields.collect {
                                        aux = it.name
                                        if (it.name.substring(0, 2) == 'SE') {
                                            label aux + '   :  ', horizontalAlignment: JLabel.RIGHT
                                            label id: aux, text: bind(source: servicio, sourceProperty: aux), horizontalAlignment: JLabel.LEFT, font: fontlbl
                                        }
                                    }
                                }
                                scrollPane(constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Detalle Transaccion:')])) {
                                    table(id: 'tabla') {
                                        tableModel(id: 'modelPetServ', list: dataPetServ) {
                                            propertyColumn(header: 'Petición', propertyName: 'PT_NUMPETICION', editable: false)
                                            propertyColumn(header: 'Tipo Pet', propertyName: 'PT_TIPOPETICION', editable: false)
                                            propertyColumn(header: 'Estado', propertyName: 'PT_ESTADOPETICION', editable: false)
                                            propertyColumn(header: 'Est@Tiempo', propertyName: 'PT_ESTADOATIEMPO', editable: false)
                                            propertyColumn(header: 'Error', propertyName: 'PT_CODIGOERROR', editable: false)
                                            propertyColumn(header: 'Mensaje', propertyName: 'PT_MENSAJEERROR', editable: false)
                                            propertyColumn(header: 'Creación', propertyName: 'PT_FECHACREACION', editable: false)
                                            propertyColumn(header: 'Modificación', propertyName: 'PT_FECHAMODIFICACION', editable: false)
                                        }
                                    }
                                }
                                //dataPetServ.addPropertyChangeListener({ e -> modelPetServ.fireTableDataChanged() })
                                def auxmodelPetServ=modelPetServ
                                dataPetServ.addPropertyChangeListener(
                                        //{ e -> modelPet.fireTableDataChanged() }
                                        new PropertyChangeListener(){
                                            public void propertyChange(PropertyChangeEvent e)
                                            {
                                                auxmodelPetServ.fireTableDataChanged()
                                            }
                                        }
                                )
                            }
                    panelDatosBA =
                            panel(id:'panelDatosBA',layout: new BorderLayout()) {
                                panel(constraints: BorderLayout.NORTH, border: compoundBorder([emptyBorder(10), titledBorder('Consulta:')])) {
                                    tableLayout {
                                        tr {
                                            td { label 'Valor Busqueda:' }
                                            td { textField id: 'numValorBusquedaField', columns: 20 }
                                            td { button text:'enviar', id:'btnEnviarDatosBA', actionPerformed: {
                                                if(Menu.btnEnviarDatosBAAccion(numValorBusquedaField.text, datosBA,cca.selected)=="OK"){
                                                    mensajeDatosBAlbl.text = '   '
                                                } else {
                                                    mensajeDatosBAlbl.text = "No existe el ${(cca.selected)?'Cliente':'Servicio'}"
                                                }
                                            }
                                            }
                                            td {
                                                panel ( /*border: compoundBorder([emptyBorder(10), titledBorder('TipoConsulta:')])*/) {
                                                    buttonGroup(id:'cmd');
                                                    radioButton(id:'cca', text:'cca', buttonGroup:cmd, actionPerformed:{}, selected:true)
                                                    radioButton(id:'serv', text:'serv', buttonGroup:cmd, actionPerformed:{})
                                                }
                                            }
                                        }
                                        tr {
                                            td { label(id: 'mensajeDatosBAlbl', text: '   ').setForeground(Color.RED)  }
                                        }
                                    }
                                }
                                panel(constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Informacion del servicio:')])) {
                                    gridLayout(cols: 2, rows: 20)
                                    DatosBA.declaredFields[0..19].collect {
                                        aux = it.name
                                        label aux + '   :  ', horizontalAlignment: JLabel.RIGHT
                                        label id: aux, text: bind(source: datosBA, sourceProperty: aux), horizontalAlignment: JLabel.LEFT, font: fontlbl
                                    }
                                }
                            }

                    panelVacio =   panel(id:'panelDatosBA',layout: new BorderLayout()) {}



                }
            }
        }

        dataPeticionDet.clear()
        dataPetServ.clear()
        panelPeticiones.visible = false
        panelServicios.visible = false
        panelDatosBA.visible = false
        panelVacio.visible = true
        menu['Seguimiento Num Peticion'] = panelPeticiones
        menu['Peticiones por servicio'] = panelServicios
        menu['Datos Banda Ancha'] = panelDatosBA
        menu['Panel Vacio'] = panelVacio
        return swing
    }
}

