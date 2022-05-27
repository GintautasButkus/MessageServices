package lt.ignitis.GintautasButkus.Controllers;

import java.util.List;

import javax.persistence.PreRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.ignitis.GintautasButkus.Models.Statistics;
import lt.ignitis.GintautasButkus.Repositories.StatisticsRepository;
import lt.ignitis.GintautasButkus.Services.AdminService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "", tags = { "Admin Board" })
@Tag(name = "Admin Board", description = "Ignitis Admin")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	StatisticsRepository statisticsRepository;
	
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete_user/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreRemove
	public void deleteUser(@PathVariable Long id) {
		adminService.deleteUser(id);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/statistics")
	public List<Statistics> getStatistics(){
		adminService.loadStatistics();
		return statisticsRepository.findAll();
	}

}
