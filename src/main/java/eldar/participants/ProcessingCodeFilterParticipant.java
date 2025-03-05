package eldar.participants;

import org.jpos.iso.ISOMsg;
import org.jpos.transaction.TxnSupport;

import org.jpos.transaction.Context;
import java.io.Serializable;

public class ProcessingCodeFilterParticipant extends TxnSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        try {
            ISOMsg isoMsg = (ISOMsg) ctx.get("REQUEST");
            if (isoMsg == null) {
                System.out.println("No se recibió un mensaje ISO.");
                return ABORTED;
            }

            // Obtener el código de procesamiento (campo 3)
            String processingCode = isoMsg.getString(3);

            // Determinar tipo de transacción
            String transactionType;
            switch (processingCode) {
                case "010000":
                    transactionType = "CASH_WITHDRAWAL";
                    break;
                case "420000":
                    transactionType = "SERVICE_PAYMENT";
                    break;
                case "210000":
                    transactionType = "DEPOSIT";
                    break;
                case "310000":
                    transactionType = "BALANCE_INQUIRY";
                    break;
                default:
                    transactionType = "UNKNOWN";
                    break;
            }

            // Guardar tipo de transacción en el contexto
            ctx.put("TRANSACTION_TYPE", transactionType);
            System.out.println("Filtro de código de procesamiento aplicado: " + transactionType);

            return PREPARED;
        } catch (Exception e) {
            e.printStackTrace();
            return ABORTED;
        }
    }

}
