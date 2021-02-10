package com.auth.JwtAuth.exceptions;

import java.time.LocalDateTime;
import java.util.StringJoiner;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ExceptionResponse {

	private HttpStatus status;

	private int error_code;

	private String message;

	private String detail;

	private LocalDateTime timeStamp;

	public static final class ExceptionResponseBuilder {
		private HttpStatus status;
		private int error_code;
		private String message;
		private String detail;
		private LocalDateTime timeStamp;

		public ExceptionResponseBuilder() {
		}

		public static ExceptionResponseBuilder ExceptionResponse() {
			return new ExceptionResponseBuilder();
		}

		public ExceptionResponseBuilder withStatus(HttpStatus status) {
			this.status = status;
			return this;
		}

		public ExceptionResponseBuilder withError_code(int error_code) {
			this.error_code = error_code;
			return this;
		}

		public ExceptionResponseBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public ExceptionResponseBuilder withDetail(String detail) {
			this.detail = detail;
			return this;
		}

		public ExceptionResponseBuilder atTime(LocalDateTime timeStamp) {
			this.timeStamp = timeStamp;
			return this;
		}

		public ExceptionResponse build() {
			ExceptionResponse exceptionResponse = new ExceptionResponse();
			exceptionResponse.status = this.status;
			exceptionResponse.error_code = this.error_code;
			exceptionResponse.detail = this.detail;
			exceptionResponse.message = this.message;
			exceptionResponse.timeStamp = this.timeStamp;
			return exceptionResponse;
		}
	}

	public String toJson() {
		return new StringJoiner(", ", "{", "}").add("\"status\": " + status).add("\"error\": \"" + error_code + "\"")
				.add("\"message\": \"" + message + "\"").add("\"detail\": \"" + detail + "\"")
				.add("\"timestamp\": \"" + timeStamp + "\"").toString();
	}

}
