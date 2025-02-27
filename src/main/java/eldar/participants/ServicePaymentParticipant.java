package eldar.participants;

import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TxnSupport;

import java.io.Serializable;

public class ServicePaymentParticipant extends TxnSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int prepare(long id, Serializable context) {
        try {
            Context ctx = (Context) context;
            ISOMsg isoMsg = (ISOMsg) ctx.get("REQUEST");
            if (isoMsg == null) {
                System.out.println("No se recibió un mensaje ISO.");
                return ABORTED;
            }

            String processingCode = isoMsg.getString(3);
            if ("420000".equals(processingCode)) { // Código de procesamiento para pagos de servicios
                System.out.println("Procesando PAGO DE SERVICIOS...");

                // Simulación de validación de saldo
                String amount = isoMsg.getString(4);
                double paymentAmount = Double.parseDouble(amount) / 100.0;

                if (paymentAmount > 1000.00) { // Simulando un límite
                    System.out.println("Monto excede el límite permitido. Pago rechazado.");
                    return ABORTED;
                }

                System.out.println("Pago aprobado: $" + paymentAmount);
                return PREPARED;
            }

            return ABORTED;
        } catch (Exception e) {
            e.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public void commit(long id, Serializable ctx) {
        System.out.println("Pago confirmado.");
    }

    @Override
    public void abort(long id, Serializable ctx) {
        System.out.println("Pago cancelado.");
    }

}
