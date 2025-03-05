package eldar.participants.cashWithdrawal;

import org.jpos.transaction.Context;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.TxnSupport;

import java.io.Serializable;

public class CashWithdrawalParticipant extends TxnSupport implements Serializable {

    private static final long serialVersionUID = 1L; // Versión de serialización

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        try {
            // Obtener el mensaje ISO desde el contexto
            ISOMsg isoMsg = (ISOMsg) ctx.get("REQUEST");
            if (isoMsg == null) {
                System.out.println("No se recibió un mensaje ISO.");
                return ABORTED;
            }

            // Obtener el monto de la transacción desde el campo 4 del mensaje ISO
            String amount = isoMsg.getString(4);  // El monto de la transacción está en el campo 4
            double withdrawalAmount = Integer.parseInt(amount) / 100.0;  // Convertir a formato estándar (dividir entre 100)

            // Guardar el monto de retiro en el contexto
            ctx.put("WITHDRAWAL_AMOUNT", withdrawalAmount);

            System.out.println("Monto de retiro guardado: $" + withdrawalAmount);
            return PREPARED;

        } catch (Exception e) {
            System.out.println("Error en CashWithdrawalParticipant: " + e.getMessage());
            e.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public void commit(long id, Serializable ctx) {
        System.out.println("Retiro confirmado.");
    }

    @Override
    public void abort(long id, Serializable ctx) {
        System.out.println("Retiro cancelado.");
    }
}
