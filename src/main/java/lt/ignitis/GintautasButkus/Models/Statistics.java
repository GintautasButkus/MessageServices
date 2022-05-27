package lt.ignitis.GintautasButkus.Models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "statistics")
public class Statistics {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@Column(name = "username")
		private String username;
		
		@Column(name = "messages_received_amount")
		private long receivedMessagesAmount;
		
		@Column(name = "first_message_time")
		private LocalDateTime firstMessageTime;
		
		@Column(name = "message_average_length")
		private double messageAverageLength;
		
		@Column(name = "last_message_text")
		private String lastMessageText;
		
		public Statistics() {}

		public Statistics(String username, long messagesAmount, 
				LocalDateTime firstMessageTime,
				double messageAverageLength,
				String lastMessageText
				) {
			this.username = username;
			this.receivedMessagesAmount = messagesAmount;
			this.firstMessageTime = firstMessageTime;
			this.messageAverageLength = messageAverageLength;
			this.lastMessageText = lastMessageText;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public long getMessagesAmount() {
			return receivedMessagesAmount;
		}

		public void setMessagesAmount(long messagesAmount) {
			this.receivedMessagesAmount = messagesAmount;
		}

		public LocalDateTime getFirstMessageTime() {
			return firstMessageTime;
		}

		public void setFirstMessageTime(LocalDateTime firstMessageTime) {
			this.firstMessageTime = firstMessageTime;
		}

		public double getMessageAverageLength() {
			return messageAverageLength;
		}

		public void setMessageAverageLength(double messageAverageLength) {
			this.messageAverageLength = messageAverageLength;
		}

		public String getLastMessageText() {
			return lastMessageText;
		}

		public void setLastMessageText(String lastMessageText) {
			this.lastMessageText = lastMessageText;
		}
		
		
		
		

	
	


}
