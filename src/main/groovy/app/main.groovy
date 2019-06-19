package main.groovy.app
import main.groovy.app.MainSwing
import main.groovy.util.DbUtilInventario
import main.groovy.util.DbUtilMQM


//def resource =getClass().getClassLoader().getResource("config.xml")
//println getClass().getClassLoader()
//println getClassLoader()
//println "Resource=${resource}"
//new File(getClass().getClassLoader().getResource("config.xml").getFile())
def  Config = new XmlParser().parse(new File("/home/honorio/IdeaProjects/PortalSw/test/config.xml"))
DbUtilMQM.bootStrap(Config)
DbUtilInventario.bootStrap(Config)
def swing = MainSwing.getMainSwing()
assert swing
swing.frame.visible = true
