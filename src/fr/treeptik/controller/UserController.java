package fr.treeptik.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;

import fr.treeptik.model.User;
import fr.treeptik.service.UserEJB;

@SessionScoped
@ManagedBean
public class UserController {

	private User user = new User();
	private List<User> listUsers = new ArrayList<User>();
	private DataModel users;
	@EJB
	private UserEJB userEJB;

	@EJB
	private SendTextMessage gestionmail;
	
	public String generatePassword() {
		return userEJB.generatePassword();
	}
	public String doCreate() {
		user.setPassword(userEJB.generatePassword());
		userEJB.create(user);
		gestionmail.mailCreationUser(user);
		listUsers = userEJB.findAll();
		getUsers();
		return "messageUserCree";
	}

	@SuppressWarnings("rawtypes")
	public String doDelete() {
		User user = (User) users.getRowData();
		userEJB.delete(user);
		users = new ListDataModel();
		users.setWrappedData(userEJB.findAll());
		return "listUsers";
	}

	public String doSelectUpdate() {
		user = (User) users.getRowData();
		return "updateUser";
	}

	@SuppressWarnings("rawtypes")
	public String doUpdate() {
		userEJB.update(user);
		users = new ListDataModel();
		users.setWrappedData(userEJB.findAll());
		return "messageUserUpdate";
	}


	public boolean isUserAdmin() {
		return getRequest().isUserInRole("ADMIN");
	}

	public String logOut() {
		getRequest().getSession().invalidate();
		return "logout";
	}

	private HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<User> getListUsers() {
		return listUsers;
	}
	public void setListUsers(List<User> listUsers) {
		this.listUsers = listUsers;
	}
	public DataModel getUsers() {
		if (users == null) {
			users = new ListDataModel();
			users.setWrappedData(userEJB.findAll());
		}
		return users;
	}
	public void setUsers(DataModel users) {
		this.users = users;
	}
	public UserEJB getUserEJB() {
		return userEJB;
	}
	public void setUserEJB(UserEJB userEJB) {
		this.userEJB = userEJB;
	}
}