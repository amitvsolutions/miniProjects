package com.restws.common;

/**
 * The Class BaseException.
 *
 * @author Bhawana_Joshi01
 * @version 1.0
 * @since Jul 17, 2019
 */
public class BaseException extends RuntimeException {

	/** The Constant serialVersionUID. */
	static final long serialVersionUID = -5829545098534135052L;

	/** The error code. */
	private final String errorCode;

	/**
	 * Instantiates a new base exception.
	 *
	 * @param message the message
	 */
	public BaseException(String message) {
		super(message);
		this.errorCode = ErrorCode.S0000.name();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param errorCode the error code
	 * @param message   the message
	 */
	public BaseException(Enum<ErrorCode> errorCode, String message) {
		super(message);
		this.errorCode = errorCode.name();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param cause the cause
	 */
	public BaseException(Throwable cause) {
		super(cause);
		this.errorCode = ErrorCode.S0000.name();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param errorCode the error code
	 * @param cause     the cause
	 */
	public BaseException(Enum<ErrorCode> errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode.name();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = ErrorCode.S0000.name();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param errorCode the error code
	 * @param message   the message
	 * @param cause     the cause
	 */
	public BaseException(Enum<ErrorCode> errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode.name();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param error   the error
	 * @param message the message
	 */
	public BaseException(String error, String message) {
		super(message);
		this.errorCode = error;
	}

	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public String getErrorCode() {
		return errorCode;
	}
}