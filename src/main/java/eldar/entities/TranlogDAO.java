package eldar.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.jpos.ee.DB;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Data
@NoArgsConstructor
public class TranlogDAO {

    private DB db;

    public TranlogDAO(DB db) { this.db=db; }

    public void saveTranlog (String mti, String panNumber, String responseCode) {
        Tranlog tranlog=new Tranlog(mti,panNumber, responseCode);
        Session session= db.open();
        session.save(tranlog);
        session.flush();
    }

    public List<Tranlog> getTranlogListByMtiAndPanNumber (String mti, String panNumber) {
        Session session=db.open();
        CriteriaBuilder cb=session.getCriteriaBuilder();
        CriteriaQuery<Tranlog> cq=cb.createQuery(Tranlog.class);
        Root<Tranlog> root=cq.from(Tranlog.class);
        cq.select(root);
        cq.where(cb.equal(root.get("mti"), mti), cb.equal(root.get("panNumber"), panNumber));
        return session.createQuery(cq).list();
    }

}
