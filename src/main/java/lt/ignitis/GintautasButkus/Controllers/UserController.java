package lt.ignitis.GintautasButkus.Controllers;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.ignitis.GintautasButkus.Models.Message;
import lt.ignitis.GintautasButkus.Services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "", tags = { "User Message Board" })
@Tag(name = "User Message Board", description = "Ignitis Message Service")
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/{usernameTo}/{messageText}")
	@ResponseStatus(HttpStatus.CREATED)
	public void createMessage(@PathVariable String usernameTo, @PathVariable String messageText, @RequestBody @Valid Message messageDetails) {
		userService.createMessage(usernameTo, messageDetails, messageText);
	}
	
	@GetMapping("/unread_messages")
	public List<Message> getUnreadMessages(){
		return userService.getUnreadMessages();
	}
	
	@GetMapping("/all_messages")
	public List<Message> getAllMessages(){
		return userService.getAllMessages();
	}
}
