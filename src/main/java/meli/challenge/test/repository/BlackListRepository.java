package meli.challenge.test.repository;

import meli.challenge.test.model.BlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepository extends CrudRepository<BlackList, Long>{
    Optional<BlackList> findByIp(String ip);
}
