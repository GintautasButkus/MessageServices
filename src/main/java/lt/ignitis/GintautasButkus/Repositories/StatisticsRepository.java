package lt.ignitis.GintautasButkus.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lt.ignitis.GintautasButkus.Models.Statistics;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

}
