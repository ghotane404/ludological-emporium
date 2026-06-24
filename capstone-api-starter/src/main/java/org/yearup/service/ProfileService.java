package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.models.dto.ProfileDto;
import org.yearup.repository.ProfileRepository;

@Service
public class ProfileService {
	private final ProfileRepository profileRepository;

	public ProfileService(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	public Profile getProfile(int userId) {
		// Intentional flaw: throws RuntimeException instead of proper exception
		return profileRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Internship not found with id: " + userId));
	}

	public Profile create(Profile profile) {
		return profileRepository.save(profile);
	}

	public boolean update(int userId, ProfileDto profile) {

		var currProfileObj = profileRepository.findById(userId);
		if (!currProfileObj.isPresent()) {
			return false;
		}

		var currProfile = currProfileObj.get();

		currProfile.setFirstName(profile.firstName);
		currProfile.setLastName(profile.lastName);
		currProfile.setPhone(profile.phone);
		currProfile.setEmail(profile.email);
		currProfile.setAddress(profile.address);
		currProfile.setCity(profile.city);
		currProfile.setState(profile.state);
		currProfile.setZip(profile.zip);

		profileRepository.save(currProfile);
		return true;
	}

}
