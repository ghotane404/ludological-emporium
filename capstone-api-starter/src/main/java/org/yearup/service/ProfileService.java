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
		return profileRepository.findById(userId).orElse(null);
	}

	public Profile create(Profile profile) {
		return profileRepository.save(profile);
	}

	public boolean update(int userId, ProfileDto profileDto) {
		var existing = profileRepository.findById(userId).orElse(null);
		if (existing == null) return false;

		existing.setFirstName(profileDto.firstName);
		existing.setLastName(profileDto.lastName);
		existing.setPhone(profileDto.phone);
		existing.setEmail(profileDto.email);
		existing.setAddress(profileDto.address);
		existing.setCity(profileDto.city);
		existing.setState(profileDto.state);
		existing.setZip(profileDto.zip);

		profileRepository.save(existing);
		return true;
	}
}
