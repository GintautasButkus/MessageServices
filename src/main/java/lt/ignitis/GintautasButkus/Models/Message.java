package lt.ignitis.GintautasButkus.Models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;


@Entity
@Table (name = "message_table")
public class Message  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;
	
	@NotBlank
	@Size(max = 20)
	private String to;
	
	@NotBlank
	@Size(max = 20)
	private String from;
	
	@NotBlank
	@Size(max = 500)
	private String messageText;
	
	@Column(name = "message_status")
	private String status = "Unread";
	
	@NotNull
	private LocalDateTime sentDate;
	
	
	public Message() {}

	public Message(@NotBlank @Size(max = 20) String to, @NotBlank @Size(max = 20) String from,
			@NotBlank @Size(max = 500) String messageText, String status) {
		super();
		this.to = to;
		this.from = from;
		this.messageText = messageText;
		this.status = status;
	}

	public Long getId() {
		return messageId;
	}

	public void setId(Long id) {
		this.messageId = id;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public LocalDateTime getSentDate() {
		return sentDate;
	}

	public void setSentDate(LocalDateTime sentDate) {
		this.sentDate = sentDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
