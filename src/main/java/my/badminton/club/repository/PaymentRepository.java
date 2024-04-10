package my.badminton.club.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import my.badminton.club.entity.*;

public interface PaymentRepository extends JpaRepository<PaymentDetails, Integer>{

	public Optional<PaymentDetails> findByPhoneNumber(long id);
}
