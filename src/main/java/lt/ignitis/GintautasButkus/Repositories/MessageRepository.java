package lt.ignitis.GintautasButkus.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ignitis.GintautasButkus.Models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
