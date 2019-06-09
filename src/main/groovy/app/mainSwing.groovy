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

@Bindable
class Servicio {
    def SE_IDSERVICIO
    def SE_IDCLIENTE
    def SE_NUMSERVICIO
    def SE_NUMTELEFONO
    def SE_CODIGOSERVICIO
    def SE_ZONAATENDIMIENTO
    def SE_CANTIDADIP
    def SE_ESTADOSERVICIO
    def SE_USERNAME
    def SE_USER
    def SE_FECHACREACION
    def SE_FECHAMODIFICACION
    String toString() { "Servicio[SE_IDSERVICIO=$SE_IDSERVICIO,SE_NUMSERVICIO=$SE_NUMSERVICIO,SE_NUMTELEFONO=$SE_NUMTELEFONO]" }
}


ObservableList data  = []
ObservableList dataPetServ =[]



def peticion= new Peticion()
def peticionDet= new PeticionDet()
def servicio= new Servicio()

DbUtilMQM.bootStrap()
def peticiones = DbUtilMQM.sql.dataSet('PS_PETICION')
def peticionesDet = DbUtilMQM.sql.dataSet('PS_PETICIONDET')
def servicios = DbUtilMQM.sql.dataSet('PS_SERVICIO')
def parametrosDet = DbUtilMQM.sql.dataSet('PS_PARAMETROSDET')

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

                                                def numServicio = servicios.rows().findAll{
                                                    peticion.PT_IDSERVICIO=it.SE_IDSERVICIO
                                                }
                                                peticion.PT_IDSERVICIO = numServicio[0].SE_NUMSERVICIO

                                                def tipoPeticion = parametrosDet.rows().findAll{itParDet->
                                                    if (itParDet.PD_IDPARAMETRO==4 && peticion.PT_TIPOPETICION == itParDet.PD_IDDETALLE){true} else {false}
                                                }
                                                peticion.PT_TIPOPETICION=tipoPeticion[0]?.PD_DESCRIPCION

                                                def estadoPeticion = parametrosDet.rows().findAll{itParDet->
                                                    if (itParDet.PD_IDPARAMETRO==3 && peticion.PT_ESTADOPETICION == itParDet.PD_IDDETALLE){true} else {false}
                                                }
                                                peticion.PT_ESTADOPETICION=estadoPeticion[0]?.PD_DESCRIPCION

                                                def estadoPeticionAtiempo = parametrosDet.rows().findAll{itParDet->
                                                    if (itParDet.PD_IDPARAMETRO==5 && peticion.PT_ESTADOATIEMPO == itParDet.PD_IDDETALLE){true} else {false}
                                                }
                                                peticion.PT_ESTADOATIEMPO=estadoPeticionAtiempo[0]?.PD_DESCRIPCION

                                                peticion.PT_CODIGOERROR=peticion.PT_CODIGOERROR.padRight(4, '0')

                                                mensajePetlbl.text = '   '
                                                def resultDet = peticionesDet.rows().findAll{
                                                    it.PD_IDPETICION == peticion["PT_IDPETICION"]
                                                }.sort{it.PD_FECHACREACION}
                                                data.addAll(resultDet)
                                                mensajePetlbl.text = '   '
                                            } else {
                                                mensajePetlbl.text = 'No existe petición '
                                            }
                                        }
                                    }
                                    td {
                                        label text: '                                               '
                                    }
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
                                    label text: bind(source: peticion, sourceProperty: aux), horizontalAlignment: JLabel.LEFT, font: fontlbl
                                }
                            }
                        }
                        scrollPane (constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Detalle Transaccion:')])) {
                            table(id: 'tabla') {
                                tableModel(id: 'modelPet', list: data) {
                                    propertyColumn(header: 'PD_INTERFACE', propertyName: 'PD_INTERFACE', editable: false)
                                    propertyColumn(header: 'PD_PROCESO', propertyName: 'PD_PROCESO', editable: false)
                                    propertyColumn(header: 'PD_CODIGOERROR', propertyName: 'PD_CODIGOERROR', editable: false)
                                    propertyColumn(header: 'PD_MENSAJEERROR', propertyName: 'PD_MENSAJEERROR', editable: false)
                                }
                            }
                        }
                        data.addPropertyChangeListener({ e -> modelPet.fireTableDataChanged() })
                    }
            panelServicios =
                    panel(layout: new BorderLayout()) {
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
                                        button text: 'enviar', actionPerformed: {
                                            Servicio.declaredFields.collect {
                                                if (it.name.substring(0, 2) == 'SE') servicio[it.name] = null
                                            }
                                            dataPetServ.clear()
                                            def result = servicios.rows().findAll {
                                                it.SE_NUMSERVICIO == numServicioField.text
                                            }
                                            if (result.size() == 1) {
                                                aux = result[0]
                                                Servicio.declaredFields.collect {
                                                    if (it.name.substring(0, 2) == 'SE') servicio[it.name] = aux[it.name]
                                                }
                                                def estadoServicio = parametrosDet.rows().findAll{
                                                    if (it.PD_IDPARAMETRO==1 && servicio.SE_ESTADOSERVICIO == it.PD_IDDETALLE){true} else {false}
                                                }
                                                servicio.SE_ESTADOSERVICIO = estadoServicio[0].PD_DESCRIPCION
                                                mensajelbl.text = '   '
                                                def resultPet = peticiones.rows().findAll{
                                                    it.PT_IDSERVICIO == servicio["SE_IDSERVICIO"]
                                                }.sort{it.PT_FECHACREACION}

                                                resultPet.each{ itPet->
                                                    def tipoPeticion = parametrosDet.rows().findAll{itParDet->
                                                        if (itParDet.PD_IDPARAMETRO==4 && itPet.PT_TIPOPETICION == itParDet.PD_IDDETALLE){true} else {false}
                                                    }
                                                    itPet.PT_TIPOPETICION=tipoPeticion[0]?.PD_DESCRIPCION

                                                    def estadoPeticion = parametrosDet.rows().findAll{itParDet->
                                                        if (itParDet.PD_IDPARAMETRO==3 && itPet.PT_ESTADOPETICION == itParDet.PD_IDDETALLE){true} else {false}
                                                    }
                                                    itPet.PT_ESTADOPETICION=estadoPeticion[0]?.PD_DESCRIPCION


                                                    def estadoPeticionAtiempo = parametrosDet.rows().findAll{itParDet->
                                                        if (itParDet.PD_IDPARAMETRO==5 && itPet.PT_ESTADOATIEMPO == itParDet.PD_IDDETALLE){true} else {false}
                                                    }
                                                    itPet.PT_ESTADOATIEMPO=estadoPeticionAtiempo[0]?.PD_DESCRIPCION
                                                    itPet.PT_CODIGOERROR=itPet.PT_CODIGOERROR.padRight(4, '0')
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
                                    label text: bind(source: servicio, sourceProperty: aux), horizontalAlignment: JLabel.LEFT, font: fontlbl
                                }
                            }
                        }
                        scrollPane (constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Detalle Transaccion:')])) {
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