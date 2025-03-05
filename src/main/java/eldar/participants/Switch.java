package eldar.participants;

import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;
import org.jpos.transaction.TxnSupport;

import java.io.Serializable;

public class Switch extends TxnSupport implements GroupSelector {

        private String[] keys;

        @Override
        public int prepare(long id, Serializable context) {
            return PREPARED | READONLY | NO_JOIN;
        }

        @Override
        public void commit(long id, Serializable ser) {
        }

        @Override
        public void abort(long id, Serializable ser) {
        }

        @Override
        public String select(long id, Serializable ser) {
            Context ctx = (Context) ser;
            String type = "";
            for (String key : keys) {
                type += ctx.get(key) + ".";
            }
            String groups;
            type = type.substring(0, type.length() - 1);
            groups = cfg.get(type, null);
            if (groups == null) {
                groups = cfg.get("unknown", "");
            }

            // Debug info in cotext
            ctx.put("SWITCH", type + " (" + groups + ")");
            return groups;
        }

        @Override
        public void setConfiguration(Configuration cfg) throws ConfigurationException {
            super.setConfiguration(cfg);
            keys = cfg.getAll("ctx-key");
            if (keys == null) {
                throw new ConfigurationException("Claves de contexto no configuradas");
            }
        }
    }
