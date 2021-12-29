package meli.challenge.test.repository;

import meli.challenge.test.model.Statistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, String>{

    @Query(nativeQuery = true, value = "SELECT AVG(distance_to_villavo) AS average, MIN(distance_to_villavo) AS min, MAX(distance_to_villavo) AS max, count(*) AS quantity FROM statistics")
    List<Map<String, ?>> averageDistanceToVillavo();

}
