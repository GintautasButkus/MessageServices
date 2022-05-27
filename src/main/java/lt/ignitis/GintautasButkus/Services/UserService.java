package lt.ignitis.GintautasButkus.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lt.ignitis.GintautasButkus.Exceptions.NoMessagesException;
import lt.ignitis.GintautasButkus.Exceptions.NoUserExistsException;
import lt.ignitis.GintautasButkus.Models.Message;
import lt.ignitis.GintautasButkus.PayloadRequest.LoginRequest;
import lt.ignitis.GintautasButkus.PayloadResponse.MessageResponse;
import lt.ignitis.GintautasButkus.PayloadResponse.UserInfoResponse;
import lt.ignitis.GintautasButkus.Repositories.MessageRepository;
import lt.ignitis.GintautasButkus.Repositories.RoleRepository;
import lt.ignitis.GintautasButkus.Repositories.UserRepository;
import lt.ignitis.GintautasButkus.Security.JWT.JwtUtils;
import lt.ignitis.GintautasButkus.Security.Services.UserDetailsImpl;

@Service
public class UserService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

//	@Autowired(required = false)

	@Autowired
	MessageRepository messageRepository;

	public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(
				new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("You've been signed out!"));
	}

	public void createMessage(String username, Message messageDetails, String messageText) {
		Message message = userRepository.findById(userRepository.findAll().stream()
				.filter(user -> user.getUsername().equals(username)).findFirst().get().getId()).map(user -> {
//			messageDetails.setUser(user);
					messageDetails.setSentDate(LocalDateTime.now());
					messageDetails.setFrom(getUsername());
					messageDetails.setTo(username);
					messageDetails.setStatus("Unread");
					messageDetails.setMessageText(messageText);
					return messageRepository.save(messageDetails);

				}).orElseThrow(() -> new NoUserExistsException("No user exists with username  " + username));
		messageRepository.save(message);
	}

	public static String getUsername() {
		String username = null;
		Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (authentication instanceof UserDetails) {
			username = ((UserDetails) authentication).getUsername();
		} else {
			username = authentication.toString();
		}
		return username;
	}

	public List<Message> getUnreadMessages() {
		List<Message> unreadMessages = messageRepository.findAll().stream()
				.filter(message -> message.getStatus().equals("Unread"))
				.filter(unread -> unread.getTo().equals(getUsername())).collect(Collectors.toList());
		for (Message message : unreadMessages) {
			if (!unreadMessages.isEmpty()) {
				message.setStatus("Read");
				messageRepository.save(message);
			} else {
				throw new NoMessagesException("No unread messages received.");
			}
		}

		return unreadMessages;
	}

	public List<Message> getAllMessages() {
		List<Message> unreadMessages = messageRepository.findAll().stream()
				.filter(unread -> unread.getTo().equals(getUsername())).collect(Collectors.toList());
		for (Message message : unreadMessages) {
			if (!unreadMessages.isEmpty()) {
				message.setStatus("Read");
				messageRepository.save(message);
			} else {
				throw new NoMessagesException("No messages received.");
			}
		}
		return unreadMessages;
	}

}
