package eldar.filters;

import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOFilter;
import org.jpos.iso.ISOMsg;
import org.jpos.util.LogEvent;

public class DemoIncomingFilter implements ISOFilter {

    @Override
    public ISOMsg filter(ISOChannel isoChannel, ISOMsg isoMsg, LogEvent evt) throws VetoException {
        evt.addMessage("--- filtered message (xml_incoming) ---");
        evt.addMessage(isoMsg);

        ISOMsg isoMsgIn = (ISOMsg) isoMsg.clone();

        // Comprobamos si el campo 12 que indica la fecha y hora de la transacción existe en el mensaje
        if (isoMsgIn.hasField(12)) {
            // Si existe, se parsea en fecha y hora separado y se setean en sus respectivos campos
            String datetimeTransaction = isoMsgIn.getString(12);
            String date = datetimeTransaction.substring(0, 6);
            String time = datetimeTransaction.substring(6);

            isoMsgIn.set(12, time);
            isoMsgIn.set(13, date);
        }
        return isoMsgIn;
    }
}
