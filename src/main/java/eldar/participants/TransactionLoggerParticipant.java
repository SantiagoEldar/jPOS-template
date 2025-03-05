package eldar.participants;

import org.jpos.iso.ISOMsg;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.transaction.Context;
import org.jpos.transaction.TxnSupport;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class TransactionLoggerParticipant extends TxnSupport implements Serializable{
    // Este participante registra la transacción en un log para auditoría.

    private static final long serialVersionUID = 1L;

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        try {
            ISOMsg isoMsg = (ISOMsg) ctx.get("REQUEST");
            if (isoMsg == null) {
                return ABORTED;
            }

            // Extraer información clave
            String processingCode = isoMsg.getString(3);
            String amount = isoMsg.getString(4);
            String terminalId = isoMsg.getString(41);
            String transactionType = (String) ctx.get("TRANSACTION_TYPE");

            // Formatear log
            String logEntry = String.format("Codigo=%s, Monto=%s, Terminal=%s, Tipo=%s%n",
                    processingCode, amount, terminalId, transactionType);

            // Guardar en archivo
            try (FileWriter writer = new FileWriter("transactions.log", true)) {
                writer.write(logEntry);
            }

            System.out.println("Transaccion registrada: "+ logEntry);
            return PREPARED;
        } catch (IOException e) {
            e.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public void commit(long id, Serializable context) {
        System.out.println("Commit exitoso para la transacción ID: " + id);
    }

    @Override
    public void abort(long id, Serializable context) {
        System.out.println("Transacción abortada ID: " + id);
    }
}
