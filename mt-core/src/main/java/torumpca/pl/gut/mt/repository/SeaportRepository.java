package torumpca.pl.gut.mt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import torumpca.pl.gut.mt.dsm.model.Seaport;

/**
 * Created by Tomasz Rumpca on 2016-08-13.
 */
@Repository
public interface SeaportRepository extends JpaRepository<Seaport, Long> {

}
