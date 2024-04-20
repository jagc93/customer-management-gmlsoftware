package com.gmlsoftware.exception;

import static com.gmlsoftware.constant.MessagesConstant.NOT_FOUND;

import java.util.Objects;

import jakarta.ws.rs.NotFoundException;

public class GenericException {

	public static final String NOT_NULL_OR_EMPTY_MESSAGE = "'%s' is required";
    public static final String LENGTH_EXCEED_MESSAGE = "The '%s' field cannot be longer than %d characters.";
    public static final String LOG_NOT_EXIST_MESSAGE = "No '%s' with id: %s exists";

    /**
     * @param value
     * @param fieldName (is required)
     */
	public static <T> void requireNotNull(T value, String fieldName) {
        Objects.requireNonNull(value, String.format(NOT_NULL_OR_EMPTY_MESSAGE, fieldName));
    }

	/**
	 * @param value
	 * @param fieldName (is required)
	 */
	public static void requireNotEmpty(String value, String fieldName) {
		requireNotNull(value, fieldName);

		if (value.isBlank()) {
			throw new IllegalArgumentException(String.format(NOT_NULL_OR_EMPTY_MESSAGE, fieldName));
		}
	}

	/**
	 * @param length
	 * @param maxLength
	 * @param fieldName (is required)
	 */
	public static void requiresThatNotExceedLength(int length, int maxLength, String fieldName) {
		if (fieldName.isBlank()) {
			throw new IllegalArgumentException("Field name cannot be determined.");
		}

		if (length > maxLength) {
			throw new IllegalArgumentException(String.format(LENGTH_EXCEED_MESSAGE, fieldName, maxLength));
		}
	}

	/**
	 * @param fieldValue
	 * @param maxLength
	 * @param fieldName (is required)
	 */
	public static void validateFieldLength(String fieldValue, int maxLength, String fieldName) {
		if (fieldValue != null) {
			requiresThatNotExceedLength(fieldValue.length(), maxLength, fieldName);
		}
	}

	/**
	 * @param <T>
	 * @param id
	 * @param fileName
	 * @return
	 */
	public static <T> NotFoundException getNotFound(T id, String fileName) {
		return new NotFoundException(String.format(NOT_FOUND, fileName, id));
	}
}
