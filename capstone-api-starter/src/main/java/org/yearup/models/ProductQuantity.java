package org.yearup.models;
import jakarta.validation.constraints.NotEmpty;

public class ProductQuantity {
	// ProductQuantity created to represent the request body
	@NotEmpty
	public int quantity;
}
