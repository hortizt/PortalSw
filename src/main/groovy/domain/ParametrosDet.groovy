package main.groovy.domain

import main.groovy.util.DbUtilMQM

class ParametrosDet {


    static String getTipoPeticion(id){
        def tipoPeticion = DbUtilMQM.sql.dataSet('PS_PARAMETROSDET').rows().findAll{ itParDet->
            if (itParDet.PD_IDPARAMETRO==4 && id== itParDet.PD_IDDETALLE){true} else {false}
        }
        return tipoPeticion[0]?.PD_DESCRIPCION
    }

    static String getEstadoPeticion(id){
        def estadoPeticion = DbUtilMQM.sql.dataSet('PS_PARAMETROSDET').rows().findAll{itParDet->
            if (itParDet.PD_IDPARAMETRO==3 && id == itParDet.PD_IDDETALLE){true} else {false}
        }
        return estadoPeticion[0]?.PD_DESCRIPCION
    }

    static String getEstadoPeticionAtiempo(id){
        def estadoPeticionAtiempo = DbUtilMQM.sql.dataSet('PS_PARAMETROSDET').rows().findAll{itParDet->
            if (itParDet.PD_IDPARAMETRO==5 && id == itParDet.PD_IDDETALLE){true} else {false}
        }
        return estadoPeticionAtiempo[0]?.PD_DESCRIPCION
    }
}
