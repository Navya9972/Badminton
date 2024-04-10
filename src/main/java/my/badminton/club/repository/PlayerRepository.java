package my.badminton.club.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import my.badminton.club.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long>{

	Optional<Player> findByPhoneNumber(long id);
	//Optional<Player> findByName(String name);

}
