package eldar.participants;

import org.jpos.transaction.TxnSupport;
import org.jpos.transaction.Context;
import java.io.Serializable;
import java.util.Random;

public class BalanceInquiryHandlerParticipant extends TxnSupport implements Serializable {
    //Esta transacción no mueve dinero, solo consulta información.

    private static final long serialVersionUID = 1L;

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        try {
            // Obtener el tipo de transacción
            String transactionType = (String) ctx.get("TRANSACTION_TYPE");

            if (!"BALANCE_INQUIRY".equals(transactionType)) {
                return PREPARED; // No es una consulta de saldo, continuar con la transacción normal.
            }

            // Simular la recuperación de saldo de una cuenta
            double simulatedBalance = new Random().nextDouble() * 5000; // Genera un saldo aleatorio

            // Guardar saldo en el contexto
            ctx.put("BALANCE_AMOUNT", simulatedBalance);
            System.out.println("Consulta de saldo realizada. Saldo disponible: $" + simulatedBalance);

            return PREPARED;
        } catch (Exception e) {
            e.printStackTrace();
            return ABORTED;
        }
    }

}
