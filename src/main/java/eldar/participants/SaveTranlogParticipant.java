package eldar.participants;

import eldar.entities.Tranlog;
import eldar.entities.TranlogDAO;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TxnSupport;
import org.jpos.ee.DB;
import java.io.Serializable;
import java.util.List;

public class SaveTranlogParticipant extends TxnSupport implements Serializable {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        DB db = getDB(ctx);

        ISOMsg isoMsg = ctx.get("RESPONSE");
        TranlogDAO tranlogDAO = new TranlogDAO(db);

            try {
                if (isoMsg.getMTI() != null && isoMsg.hasField(2) && isoMsg.hasField(39)) {

                    List<Tranlog> listTranlogBefore = tranlogDAO.getTranlogListByMtiAndPanNumber(isoMsg.getMTI(), isoMsg.getString(2));
                    info("Cantidad de registros en tabla tranlog con mismo mti y pan number antes de insertar registro: " + listTranlogBefore.size());

                    tranlogDAO.saveTranlog(isoMsg.getMTI(), isoMsg.getString(2), isoMsg.getString(39));
                    List<Tranlog> listTranlog = tranlogDAO.getTranlogListByMtiAndPanNumber(isoMsg.getMTI(), isoMsg.getString(2));

                    info("Cantidad de registros en tabla tranlog con mismo mti y pan number despues de insertar registro: " + listTranlog.size());
                }
                } catch (ISOException e) {
                    throw new RuntimeException(e);
                }

            return PREPARED;
    }

}
