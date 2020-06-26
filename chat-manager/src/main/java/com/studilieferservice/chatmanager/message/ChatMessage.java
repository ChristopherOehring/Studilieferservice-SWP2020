package com.studilieferservice.chatmanager.message;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.studilieferservice.chatmanager.group.Group;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JsonBackReference("group") //to avoid recursion in JSON
	private Group group;

	@NotNull
	//name cannot be "user" because this word is reserved in postgres
	private String userName;

	@NotNull
	private String content;

	public ChatMessage() {}

	public ChatMessage(String user, String content) {
		this.userName = userName;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getUser() {
		return userName;
	}

	private void setUser(String user) {
		this.userName = user;
	}

	@Override
	public String toString() {
		return String.format(
				"ChatMessage [id='%s', groupId='%s', groupName='%s', user='%s', content='%s']",
				id, group.getId(), group.getName(), userName, content);
	}
}
