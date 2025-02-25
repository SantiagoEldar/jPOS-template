package eldar.entities;

import org.jpos.ee.DB;
import org.jpos.transaction.TxnSupport;

import org.jpos.transaction.Context;
import java.io.Serializable;

public class Open extends TxnSupport {

    int timeout = 0;

    public Open(int timeout) {
    }

    public int prepare(long id, Serializable o) {
        int rc = 0;
        Context ctx = (Context) o;

        try {
            DB db = this.getDB(ctx);
            db.open();
            ctx.put("TX", db.beginTransaction(this.timeout));
            this.checkPoint(ctx);
            rc = 1;
        } catch (Throwable var7) {
            Throwable t = var7;
            this.error(t);
            ctx.remove("DB");
        }

        return rc | 64 | 128;
    }

    public void commit(long id, Serializable o) {
    }

    public void abort(long id, Serializable o) {
    }

}
