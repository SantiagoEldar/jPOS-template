package eldar.entities;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOFilter;
import org.jpos.iso.ISOMsg;
import org.jpos.util.LogEvent;

public class DemoOutgoingFilter implements ISOFilter, Configurable {

    private String codigoAprobado; // 2 usages

    @Override
    public void setConfiguration(Configuration cfg) throws ConfigurationException {
        codigoAprobado = cfg.get("codigo-aprobado");
    }

    @Override
    public ISOMsg filter(ISOChannel isoChannel, ISOMsg isoMsg, LogEvent evt) throws VetoException {
        evt.addMessage("--- filtered message (xml_outgoing) ---");
        evt.addMessage(isoMsg);

        ISOMsg isoMsgOut = (ISOMsg) isoMsg.clone();

        // A modo de prueba, si la transaccion aprobada quitamos el campo 22 y 40 de la respuesta al cliente tcp
        if (isoMsgOut.hasField(39) && isoMsgOut.getString(39).equals(codigoAprobado)) {
            isoMsgOut.unset(22);
            isoMsgOut.unset(40);
        }
        // De otra manera lo devuelve con los respectivos campos
        return isoMsgOut;
    }
}
