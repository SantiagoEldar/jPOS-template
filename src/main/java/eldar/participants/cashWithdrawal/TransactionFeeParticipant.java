package eldar.participants.cashWithdrawal;

import org.jpos.transaction.Context;
import org.jpos.transaction.TxnSupport;
import org.jpos.util.LogEvent;
import org.jpos.util.Logger;

import java.io.Serializable;

public class TransactionFeeParticipant extends TxnSupport implements Serializable {
    //Aplica una tarifa por el retiro de efectivo, calculando y añadiendo el monto de la tarifa a la transacción.

    private static final double FEE_PERCENTAGE = 0.02; // Tarifa del 2%

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        LogEvent evt = new LogEvent(this, "transaction-fee");

        try {
            // Obtener el monto del retiro
            double withdrawalAmount = (Double) ctx.get("WITHDRAWAL_AMOUNT");

            // Calcular la tarifa
            double feeAmount = withdrawalAmount * FEE_PERCENTAGE;
            double totalAmount = withdrawalAmount + feeAmount;

            // Guardar el monto total que incluye la tarifa
            ctx.put("TOTAL_AMOUNT_WITH_FEE", totalAmount);
            ctx.put("TRANSACTION_FEE", feeAmount);

            evt.addMessage("Tarifa aplicada: " + feeAmount + ", Monto total: " + totalAmount);
            Logger.log(evt);
            return PREPARED;
        } catch (Exception e) {
            evt.addMessage("Error aplicando la tarifa: " + e.getMessage());
            Logger.log(evt);
            return ABORTED;
        }
    }

}
