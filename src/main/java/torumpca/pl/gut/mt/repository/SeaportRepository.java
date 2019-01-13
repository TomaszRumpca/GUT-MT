package torumpca.pl.gut.mt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import torumpca.pl.gut.mt.model.Seaport;

@Repository
public interface SeaportRepository extends JpaRepository<Seaport, Long> {

}
