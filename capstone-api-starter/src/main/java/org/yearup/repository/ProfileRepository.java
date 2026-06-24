package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yearup.models.CartItem;
import org.yearup.models.Profile;
import org.yearup.models.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {


}
