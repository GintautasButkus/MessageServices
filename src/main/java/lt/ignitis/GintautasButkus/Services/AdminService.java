package lt.ignitis.GintautasButkus.Services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import lt.ignitis.GintautasButkus.Exceptions.NoUserExistsException;
import lt.ignitis.GintautasButkus.Models.ERole;
import lt.ignitis.GintautasButkus.Models.Message;
import lt.ignitis.GintautasButkus.Models.Role;
import lt.ignitis.GintautasButkus.Models.Statistics;
import lt.ignitis.GintautasButkus.Models.User;
import lt.ignitis.GintautasButkus.PayloadRequest.SignupRequest;
import lt.ignitis.GintautasButkus.PayloadResponse.MessageResponse;
import lt.ignitis.GintautasButkus.Repositories.MessageRepository;
import lt.ignitis.GintautasButkus.Repositories.StatisticsRepository;
import lt.ignitis.GintautasButkus.Repositories.RoleRepository;
import lt.ignitis.GintautasButkus.Repositories.UserRepository;
import lt.ignitis.GintautasButkus.Security.JWT.JwtUtils;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

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

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	StatisticsRepository statisticsRepository;

// ************************* CREATE ADMIN USER WHEN APP STARTS ***********************
	@PostConstruct
	public ResponseEntity<?> init() {
		User admin = new User("admin", "admin@ignitis", encoder.encode("admin"));
		Set<Role> roles = new HashSet<>();
		roles.add(new Role(ERole.ROLE_ADMIN));
		roles.add(new Role(ERole.ROLE_USER));
		admin.setRoles(roles);
		userRepository.save(admin);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

//	******************** REGISTER NEW USER **********************************

	public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;

				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

//	**************** DELETE USER ******************************

	public void deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			Set<Role> emptyRoles = new HashSet<Role>();
			userRepository.findAll().stream().filter(user -> user.getId() == id).findFirst().get().setRoles(emptyRoles);
			userRepository.deleteById(id);
		} else {
			throw new NoUserExistsException("Sorry, there is no such user.");
		}
	}

//	**************** GET FIRST MESSAGE TIME ******************************
	public LocalDateTime getFirstMessageTime(String username) {
		List<Message> userMessages = messageRepository.findAll().stream()
				.filter(message -> message.getTo().equals(username)).collect(Collectors.toList());
		Message firstMessage = Collections.min(userMessages, Comparator.comparing(Message::getSentDate));
		return firstMessage.getSentDate();

	}

//	***************** GET LAST MESSAGE TEXT *****************************
	public String lastMessageText(String username) {
		List<Message> userMessages = messageRepository.findAll().stream()
				.filter(message -> message.getTo().equals(username)).collect(Collectors.toList());
		Message lastMessage = Collections.max(userMessages, Comparator.comparing(Message::getSentDate));

		return lastMessage.getMessageText();

	}

	// **************** GET MESSAGE LENGTH AVERAGE ****************************
	private double getMessageLengthAverage(String username) {
		List<Integer> messagesLengthCollection = messageRepository.findAll().stream()
				.filter(message -> message.getTo().equals(username)).map(message -> message.getMessageText().length())
				.collect(Collectors.toList());
		IntSummaryStatistics messageLengthStats = messagesLengthCollection.stream().mapToInt(Integer::intValue)
				.summaryStatistics();
		return messageLengthStats.getAverage();
	}

//	**************** GET USER MESSAGES AMOUNT ********************************
	private long overalUserReceivedMessages(String username) {
		return messageRepository.findAll().stream().filter(message -> message.getTo().equals(username)).count();
	}

// ****************** LOAD STATISTICS INTO DB *******************************
	public void loadStatistics() {
		for (User user : userRepository.findAll()) {
			if (messageRepository.findAll().stream().filter(message -> message.getTo().equals(user.getUsername()))
					.collect(Collectors.toList()).isEmpty()) {
				Statistics stats = new Statistics(user.getUsername(), 0, null, 0, null);
				statisticsRepository.save(stats);
			} else {
				Statistics stats = new Statistics(user.getUsername(), overalUserReceivedMessages(user.getUsername()),
						getFirstMessageTime(user.getUsername()), getMessageLengthAverage(user.getUsername()),
						lastMessageText(user.getUsername()));
				statisticsRepository.save(stats);
			}
		}
	}

}
