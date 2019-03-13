package torumpca.pl.gut.mt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
    User findByUsername(String username);
    List<User> findAll();
//    User update(User user);
    User deleteById(long id);

}
