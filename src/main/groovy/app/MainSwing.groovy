package main.groovy.app

import main.groovy.domain.PeticionDet
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import groovy.swing.SwingBuilder
import javax.swing.*
import main.groovy.util.DbUtilMQM
import main.groovy.domain.Peticion
import main.groovy.domain.Servicio
import main.groovy.domain.ParametrosDet
import main.groovy.util.Menu

class MainSwing {



    static SwingBuilder getMainSwing() {


        def panelPeticiones, panelServicios
        def menu = [:]
        Font fontlbl = new Font("Courier", Font.PLAIN, 13)

        ObservableList dataPeticionDet = []
        ObservableList dataPetServ = []

        def peticion = new Peticion()
        def servicio = new Servicio()

        DbUtilMQM.bootStrap()
        def peticiones = DbUtilMQM.sql.dataSet('PS_PETICION')
        def peticionesDet = DbUtilMQM.sql.dataSet('PS_PETICIONDET')
        def servicios = DbUtilMQM.sql.dataSet('PS_SERVICIO')
        def parametrosDet = DbUtilMQM.sql.dataSet('PS_PARAMETROSDET')



        def swing = new SwingBuilder()


        def frame = swing.frame(id:'frame', title: 'Demo', size: [1000, 900]) {
            panel(id: 'panelPrincipal', layout: new BorderLayout()) {
                panel(id:'panelMenu',constraints: BorderLayout.WEST, border: compoundBorder([emptyBorder(10), titledBorder('Menu')])) {
                    tree(id:'treeMenu', Menu.createMenuTree()).getSelectionModel().addTreeSelectionListener({ e -> Menu.accionSeleccionMenu(e, menu) })
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
                                            td {
                                                label(id: 'mensajePetlbl', text: '   ').setForeground(Color.RED)
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
                                dataPeticionDet.addPropertyChangeListener({ e -> modelPet.fireTableDataChanged() })
                            }
                    panelServicios =
                            panel(id:'panelPeticiones',layout: new BorderLayout()) {
                                panel(constraints: BorderLayout.NORTH, border: compoundBorder([emptyBorder(10), titledBorder('Consulta:')])) {
                                    tableLayout {
                                        tr {
                                            td {
                                                label 'Numero de Teléfono:'
                                            }
                                            td {
                                                textField id: 'numServicioField', columns: 20
                                            }
                                            td {
                                                button text:'enviar', id:'btnEnviarServicio', actionPerformed: {
                                                    Servicio.clearValores(servicio)
                                                    dataPetServ.clear()
                                                    def auxServicio=Servicio.getServicioNumServicio( numServicioField.text)
                                                    if (auxServicio != null) {
                                                        Servicio.setValores(servicio, auxServicio)
                                                        Servicio.declaredFields.collect {
                                                            if (it.name.substring(0, 2) == 'SE') servicio[it.name] = auxServicio[it.name]
                                                        }
                                                        def estadoServicio = parametrosDet.rows().findAll {
                                                            if (it.PD_IDPARAMETRO == 1 && servicio.SE_ESTADOSERVICIO == it.PD_IDDETALLE) {
                                                                true
                                                            } else {
                                                                false
                                                            }
                                                        }
                                                        servicio.SE_ESTADOSERVICIO = estadoServicio[0].PD_DESCRIPCION
                                                        mensajelbl.text = '   '
                                                        def resultPet = peticiones.rows().findAll {
                                                            it.PT_IDSERVICIO == servicio["SE_IDSERVICIO"]
                                                        }.sort { it.PT_FECHACREACION }

                                                        resultPet.each { itPet ->
                                                            def tipoPeticion = parametrosDet.rows().findAll { itParDet ->
                                                                if (itParDet.PD_IDPARAMETRO == 4 && itPet.PT_TIPOPETICION == itParDet.PD_IDDETALLE) {
                                                                    true
                                                                } else {
                                                                    false
                                                                }
                                                            }
                                                            itPet.PT_TIPOPETICION = tipoPeticion[0]?.PD_DESCRIPCION

                                                            def estadoPeticion = parametrosDet.rows().findAll { itParDet ->
                                                                if (itParDet.PD_IDPARAMETRO == 3 && itPet.PT_ESTADOPETICION == itParDet.PD_IDDETALLE) {
                                                                    true
                                                                } else {
                                                                    false
                                                                }
                                                            }
                                                            itPet.PT_ESTADOPETICION = estadoPeticion[0]?.PD_DESCRIPCION


                                                            def estadoPeticionAtiempo = parametrosDet.rows().findAll { itParDet ->
                                                                if (itParDet.PD_IDPARAMETRO == 5 && itPet.PT_ESTADOATIEMPO == itParDet.PD_IDDETALLE) {
                                                                    true
                                                                } else {
                                                                    false
                                                                }
                                                            }
                                                            itPet.PT_ESTADOATIEMPO = estadoPeticionAtiempo[0]?.PD_DESCRIPCION
                                                            itPet.PT_CODIGOERROR = itPet.PT_CODIGOERROR.padRight(4, '0')
                                                        }
                                                        dataPetServ.addAll(resultPet)
                                                        mensajePetlbl.text = '   '
                                                    } else {
                                                        mensajePetlbl.text = 'No existe el servicio '
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
                                dataPetServ.addPropertyChangeListener({ e -> modelPetServ.fireTableDataChanged() })
                            }
                }
            }
        }

        dataPeticionDet.clear()
        panelPeticiones.visible = false
        panelServicios.visible = false
        menu['Seguimiento Num Peticion'] = panelPeticiones
        menu['Peticiones por servicio'] = panelServicios

        //frame.visible = true
        return swing
    }
}

