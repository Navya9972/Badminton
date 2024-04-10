package my.badminton.club.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import my.badminton.club.entity.PaymentDetails;
import my.badminton.club.entity.Player;
import my.badminton.club.repository.PaymentRepository;
import my.badminton.club.repository.PlayerRepository;


@RestController
public class PaymentController {
	
	@Autowired
	private PlayerRepository playerRepo;
	
	@Autowired
	private PaymentRepository paymentRepo;

	//to retrieve payment details of a player
	@GetMapping("/admin/paymentdetails")
	public List<PaymentDetails> getPaymentDetails(){
		return paymentRepo.findAll();
	}
	
	//to retrieve payment details of a player by id
	@GetMapping("/admin/paymentdetails/{id}")
	public Optional<PaymentDetails> getdetails(@PathVariable long id){
		return paymentRepo.findByPhoneNumber(id);
	}
	
	//create payment details of player
	@PostMapping("/admin/payment")
	public String createPayment(@RequestParam long id,@RequestParam long amount) {
		Optional<Player> playerDetails = playerRepo.findByPhoneNumber(id);
		if(playerDetails.isPresent())
		{
			PaymentDetails paymentDetails = new PaymentDetails();
			playerDetails.get().setAccountBalance(playerDetails.get().getAccountBalance()-amount);
			paymentDetails.setAmountPaid(amount);
			paymentDetails.setDateOfPayment(LocalDateTime.now());
			paymentDetails.setPhoneNumber(id);
			paymentRepo.save(paymentDetails);
			return "Payment generated successfull !!! for "+paymentDetails.getPhoneNumber();
		}
		return "Player "+playerDetails.get()+" not present ! please enter correct player!!";
	}
}
