package org.yearup.controllers;

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
	private final ProfileService profileService;
	private final UserService userService;

	public ProfileController(ProfileService profileService, UserService userService) {
		this.profileService = profileService;
		this.userService = userService;
	}

	@GetMapping("")
	public ResponseEntity<Profile> getProfile(Principal principal){
		int userId = getUserId(principal);
		Profile profile = profileService.getProfile(userId);

		return ResponseEntity.ok(profile);
	}

	@PutMapping("")
	public ResponseEntity<Void> updateProfile(@RequestBody ProfileDto profileDto, Principal principal) {
		int userId = getUserId(principal);
		boolean updated = profileService.update(userId, profileDto);

		if (!updated) return ResponseEntity.notFound().build();

		return ResponseEntity.noContent().build();
	}

	private int getUserId(Principal principal) {
		String userName = principal.getName();
		User user = userService.getByUserName(userName);

		return user.getId();
	}
}
