package com.junit.constants;

public interface Constants {

	public interface StatusConstants {
		public static final String AVAIALBLE = "Available";
		public static final String OUTOFSTOCK = "Out Of Stock";
	}

	public interface ExceptionConstants {
		public static final String ITEM_NOT_FOUND = "Item details not found";
		public static final String NO_DATA_FOUND = "No Data Found";
		public static final String SOMETHING_WENT_WRONG = "Something went wrong";

	}

	public interface SuccessConstants {
		public static final String DATA_SAVED_SUCCESSFULLY = "Data Save successfully";
		public static final String REGISTRATION_SUCCESS = "User registerd successfully";
		public static final String ITEMADDEDTOCART = "Item added to cart successfully";

	}

	public interface ItemConstants {
		public static final String ITEM_NAME_MANDATORY = "Item Name is mandatory";
		public static final String ITEM_DESCRIPTION_MANDATORY = "Item description is mandatory";
		public static final String ITEM_CATEGORY_MANDATORY = "Item category is mandatory";

	}

	public interface UserConstants {
		public static final String USER_EMAIL_MANDATORY = "User email is mandatory";
		public static final String USER_NAME_MANDATORY = "User name is mandatory";
		public static final String PHONE_NUMBER_MANDATORY = "Phone number is mandatory";
		public static final String PASSWORD_MANDATORY = "Password is mandatory";
		public static final String USER_WITH_EMAIL_ALREADY_PRESENT = "User with same email already present";
		public static final String UNAUTHORIZED_USER = "unAuthorized user";
		public static final String USER_NOT_FOUND = "User Not Found";

	}
}
