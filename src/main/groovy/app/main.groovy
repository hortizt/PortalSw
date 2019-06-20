package main.groovy.app
import main.groovy.app.MainSwing
import main.groovy.util.DbUtilInventario
import main.groovy.util.DbUtilMQM

def currentDir = new File(".").getAbsolutePath()
def Config = new XmlParser().parse(new File("${currentDir}/config.xml"))
//def Config= new XmlParser().parse(new File(getClass().getClassLoader().getResource("config.xml").getFile()))
DbUtilMQM.bootStrap(Config)
DbUtilInventario.bootStrap(Config)
def swing = MainSwing.getMainSwing()
assert swing
swing.frame.visible = true
