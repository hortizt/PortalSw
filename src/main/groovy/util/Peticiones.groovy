package main.groovy.util

import groovy.swing.SwingBuilder
import javax.swing.JTable

class Peticiones {

    static JTable  getPeticionesTable(){

        def data = [
                [name: 'Anthony', color: 'mediumBlue'],
                [name: 'Greg', color: 'brightYellow'],
                [name: 'Jeff', color: 'purple'],
                [name: 'Murray', color: 'brightRed']
        ]
        def swing = new SwingBuilder()
        def table = swing.table() {
                    tableModel(list: data) {
                        propertyColumn(header: 'Name', propertyName: 'name')
                        propertyColumn(header: 'Color', propertyName: 'color',  )
                    }
        }
        return table
    }
}
