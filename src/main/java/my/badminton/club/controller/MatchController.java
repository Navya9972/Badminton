package my.badminton.club.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import my.badminton.club.entity.Match;
import my.badminton.club.entity.MatchStatus;
import my.badminton.club.entity.Player;
import my.badminton.club.repository.MatchRepository;
import my.badminton.club.repository.PlayerRepository;



@RestController
public class MatchController {

	@Autowired
	private MatchRepository matchRepo;
	
	@Autowired
	private PlayerRepository playerRepo;
	
	@PostMapping("/admin/match/start")
	public String startMatch(@RequestBody Match matchDetails) {   //getting 4 details from postman using 
		if(playerRepo.findById(matchDetails.getPlayer1PhoneNo()).isPresent() && 
				playerRepo.findById(matchDetails.getPlayer2PhoneNo()).isPresent()) {
	    int i = 1;
	    while (i <= 2) {
	        List<Match> matchList = matchRepo.findByCourtNoAndEndDateTimeIsNull(i); //to check court is available or not!!
	        if(matchList.isEmpty())  //it will break the loop and go to 45line
	        	break;
	        i++;
	    }
	    if (i > 2) {
	        return "Sorry!! There is no court available!";
	    } else {
	    	matchDetails.setStartDateTime(LocalDateTime.now());
	    	matchDetails.setCourtNo(i);
	    	matchDetails.setMatchStatus(MatchStatus.STARTED);
	        matchRepo.save(matchDetails);
	        return "Lets Go!! Match Started!";
	    }
		}
		return "Player "+matchDetails.getPlayer1PhoneNo()+" Player "+matchDetails.getPlayer2PhoneNo()+
				" are not available, Please check";
	}
	
	@PutMapping("/admin/match/end{court}{phoneNo}")
	public String endMatch(@RequestParam int court,@RequestParam long phoneNo) {
		List<Match> matchList = matchRepo.findByCourtNoAndEndDateTimeIsNull(court);
		if(matchList.isEmpty())
			return "No matches!";
		Match matchDetails=matchList.get(0);
		matchDetails.setEndDateTime(LocalDateTime.now());
		matchDetails.setLoserPhoneNumber(phoneNo);
		long min=ChronoUnit.MINUTES.between(matchDetails.getStartDateTime(), matchDetails.getEndDateTime());  //defference between startdate time and end date tim in minutes
		matchDetails.setAmount(min*2);
		matchDetails.setMatchStatus(MatchStatus.ENDED);
		Optional<Player> playerDetails = playerRepo.findById(matchDetails.getLoserPhoneNumber());
		if(playerDetails.isPresent()) {			
			playerDetails.get().setAccountBalance(playerDetails.get().getAccountBalance()+(min*2));
			playerRepo.save(playerDetails.get());
			matchRepo.save(matchDetails);
			return "Loser is - "+matchDetails.getLoserPhoneNumber()+" Match Ended!..";
		}
		return "Player " +playerDetails.get()+" not exists!";
	}
	
	@GetMapping("/admin/matchdetails/by{start}{end}{id}")
	public List<Match> getMatchDetails(@RequestParam LocalDateTime start,@RequestParam LocalDateTime end,@RequestParam long id){
		List<Match> matchList= new ArrayList<>();
		matchList.addAll(matchRepo.findByPlayer1PhoneNoAndStartDateTimeBetween(id,start, end));
		matchList.addAll(matchRepo.findByPlayer2PhoneNoAndStartDateTimeBetween(id,start, end));
		return matchList;
	}
	
	@GetMapping("/admin/matchdetails") //get all match details
	public List<Match> getMatches(){
		return matchRepo.findAll();
	}
	
	@GetMapping("/admin/matchdetails{start}{end}")   //getting by start and end date
	public List<Match> getMatchDetails(@RequestParam LocalDateTime start,@RequestParam LocalDateTime end){
		return matchRepo.findByStartDateTimeBetween(start, end);
	}
}
