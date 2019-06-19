package main.java.app

import main.groovy.app.MainSwing
import main.groovy.util.DbUtilInventario
import main.groovy.util.DbUtilMQM


class Main {

       static exec() {
        def Config = new XmlParser().parse(new File(getClassLoader().getResource("config.xml").getFile()))
        DbUtilMQM.bootStrap(Config)
        DbUtilInventario.bootStrap(Config)
        def swing = MainSwing.getMainSwing()
        assert swing
        swing.frame.visible = true
    }
}