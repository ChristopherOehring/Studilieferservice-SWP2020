package com.studilieferservice.chatmanager.message;

import com.studilieferservice.chatmanager.group.Group;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private Group group;

	@NotNull
	//name cannot be "user" because this word is reserved in postgres
	private String userName;

	@NotNull
	private String content;

	private MessageType type;

	public enum MessageType {
		INFO, MESSAGE
	}

	public ChatMessage() {}

	public ChatMessage(String userName, String content, MessageType type) {
		this.userName = userName;
		this.content = content;
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String user) {
		this.userName = user;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getHtmlClassForType() {
		switch (type) {
			case INFO:
				return "event-message";
			case MESSAGE:
				return "chat-message";
		}
		return "error";
	}

	@Override
	public String toString() {
		return String.format(
				"ChatMessage [id='%s', groupId='%s', groupName='%s', user='%s', type='%s', content='%s']",
				id, group.getId(), group.getName(), userName, type, content);
	}
}
