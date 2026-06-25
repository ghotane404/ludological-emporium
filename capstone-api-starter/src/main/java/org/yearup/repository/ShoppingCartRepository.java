package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yearup.models.CartItem;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<CartItem, Integer> {
	List<CartItem> findByUserId(int userId);

	CartItem findByUserIdAndProductId(int userId, int productId);

	@Modifying
	@Query("""
        DELETE FROM CartItem c
        WHERE c.userId = :userId
        """)
	int deleteByUserId(int userId);


}
