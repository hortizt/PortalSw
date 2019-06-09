package main.groovy

import main.groovy.util.DbUtilMQM

DbUtilMQM.bootStrap()
def parametros = DbUtilMQM.sql.dataSet('PS_PARAMETROS').rows()
parametros.each {println it}

def parametrosTrat = DbUtilMQM.sql.dataSet('PS_PARAMETROS_TRAMITES').rows()
parametrosTrat.each {println it }

def parametrosDet = DbUtilMQM.sql.dataSet('PS_PARAMETROSDET').rows()
parametrosDet.each {println it}