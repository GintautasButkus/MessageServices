package lt.ignitis.GintautasButkus.Exceptions;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoUserExistsException extends NoSuchElementException {
		private static final long serialVersionUID = 1L;
		
		public NoUserExistsException(String message) {
			super(message);
		}
	

}
