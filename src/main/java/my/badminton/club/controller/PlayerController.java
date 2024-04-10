package my.badminton.club.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import my.badminton.club.entity.Player;
import my.badminton.club.repository.PlayerRepository;



@RestController
public class PlayerController {
	
	@Autowired
	private PlayerRepository playerRepo;

	// to retrieve list of players with details
	@GetMapping("/admin/details")
	public List<Player> getPlayers(){  //get player details all
		return playerRepo.findAll();
	}
	
	//create player
	@PostMapping("/admin/createplayer")  //RequestBody used to bind HTTP request with an object in method parameter
	public String createPlayer(@RequestBody Player playerDetails) {
		playerRepo.save(playerDetails);
		return "Player - "+playerDetails.getName()+" is finally  created !!!";
	}
	
	//  to retrieve individual player_details by phone_number
	@GetMapping("/admin/getplayer/{phoneNo}")  
	public Optional<Player> getPlayerById(@PathVariable long phoneNo){
		if(playerRepo.findByPhoneNumber(phoneNo).isPresent()) 
		{
		return playerRepo.findById(phoneNo);
		}
		else {
			return Optional.empty();  //empty array will return
		}
	}

//	@GetMapping("/admin/getplayer/{name}")   //get player details by Name
//	public Optional<Player> getPlayerByName(@PathVariable String name){
//		if(playerRepo.findByName(name).isPresent()) {
//		
//		return playerRepo.findByName(name);
//		}
//		else {
//			return Optional.empty();  //empty array will return
//		}
//	}
}
