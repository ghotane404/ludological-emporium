package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.dto.ProfileDto;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController {
	private ProfileService profileService;
	private UserService userService;

	@Autowired
	public ProfileController(ProfileService profileService, UserService userService) {
		this.profileService = profileService;
		this.userService = userService;
	}

	@GetMapping("")
	public ResponseEntity<Profile> getUserInfo(Principal principal){
		String userName = principal.getName();
		User user = userService.getByUserName(userName);
		int userId = user.getId();

		Profile profile = profileService.getProfile(userId);

		return ResponseEntity.ok(profile);
	}


	@PutMapping("")
	public void updateProfile(@RequestBody ProfileDto profile, Principal principal) {
		String userName = principal.getName();

		User user = userService.getByUserName(userName);
		int userId = user.getId();

		profileService.update(userId, profile);
	}

}
