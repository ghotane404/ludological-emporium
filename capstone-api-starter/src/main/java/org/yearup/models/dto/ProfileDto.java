package org.yearup.models.dto;

// holds only the fields that the user is allowed to update from the request body.
public class ProfileDto {
	public String firstName;
	public String lastName;
	public String phone;
	public String email;
	public String address;
	public String city;
	public String state;
	public String zip;
}
