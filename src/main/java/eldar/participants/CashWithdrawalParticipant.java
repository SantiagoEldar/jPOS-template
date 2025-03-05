package eldar.participants;

import org.jpos.transaction.Context;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.TxnSupport;

import java.io.Serializable;

public class CashWithdrawalParticipant extends TxnSupport implements Serializable{

    private static final long serialVersionUID = 1L; // Versión de serialización

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        try {
            ISOMsg isoMsg = (ISOMsg) ctx.get("REQUEST");
            if (isoMsg == null) {
                System.out.println("No se recibió un mensaje ISO.");
                return ABORTED;
            }

                System.out.println("Procesando RETIRO DE EFECTIVO...");

                // Simulación de validación de saldo
                String amount = isoMsg.getString(4);
                int amountValue = Integer.parseInt(amount);

                if (amountValue > 50000) {  // Límite de prueba: 500.00 USD
                    System.out.println("Saldo insuficiente.");
                    return ABORTED;
                }

                System.out.println("Retiro aprobado: $" + (amountValue / 100.0));
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
