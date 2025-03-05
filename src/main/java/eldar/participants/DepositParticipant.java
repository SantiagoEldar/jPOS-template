package eldar.participants;

import org.jpos.transaction.Context;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.TxnSupport;
import java.io.Serializable;

public class DepositParticipant extends TxnSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        try {
            ISOMsg isoMsg = (ISOMsg) ctx.get("REQUEST");
            if (isoMsg == null) {
                System.out.println("No se recibio un mensaje ISO.");
                return ABORTED;
            }

            String accountNumber = isoMsg.getString(102); // Cuenta destino
            String amount = isoMsg.getString(4); // Monto a depositar

            if (accountNumber == null || accountNumber.isEmpty()) {
                System.out.println("Numero de cuenta invalido.");
                return ABORTED;
            }

            System.out.println("Deposito recibido: Cuenta " + accountNumber + " - Monto: " + (Integer.parseInt(amount) / 100.0));

            ctx.put("DEPOSIT_SUCCESS", true);
            return PREPARED;
        } catch (Exception e) {
            e.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public void commit(long id, Serializable context) {
        Context ctx = (Context) context;
        Boolean success = (Boolean) ctx.get("DEPOSIT_SUCCESS");
        if (Boolean.TRUE.equals(success)) {
            System.out.println("Deposito confirmado.");
        }
    }

    @Override
    public void abort(long id, Serializable context) {
        System.out.println("Depósito cancelado.");
    }

}
