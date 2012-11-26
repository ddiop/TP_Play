package controllers;

import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import models.Project;
import models.Task;
import models.UserAccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class Application extends Controller {

	static Form<Task> taskForm = form(Task.class);
	static Form<Project> projectForm = form(Project.class);

	public static Result index() {
		return redirect(routes.Application.tasks());
	}

	public static Result tasks() {
		System.out.println(request().accept());
		if (request().accepts("text/html")) {

			return ok(views.html.tasks.render(Task.findAll(), taskForm));
		} else {
			ObjectNode result = Json.newObject();
			List<Task> tasks = Task.findAll();
			ArrayNode arrayJson = result.putArray("tasks");
			for (Task task : tasks) {

				arrayJson.add(Json.toJson(task));
			}

			return ok(result);
		}
	}

	public static Result getTask(Long id) {

		System.out.println(request().accept());
		if (request().accepts("text/html")) {

			return ok(views.html.tasks.render(Task.findAll(), taskForm));
		} else {
			ObjectNode result = Json.newObject();
			List<Task> tasks = Task.findAll();
			ArrayNode arrayJson = result.putArray("tasks");
			for (Task task : tasks) {

				arrayJson.add(Json.toJson(task));
			}

			return ok(result);
		}
	}

	public static Result newTask() {
		Form<Task> filledForm = taskForm.bindFromRequest();
		if (filledForm.field("user").value() == null) {
			filledForm.reject("user", "");
		}
		if (filledForm.hasErrors()) {
			return badRequest(tasks.render(Task.findAll(), filledForm));
		} else {
			Task.create(filledForm.get());
			return redirect(routes.Application.tasks());
		}
	}

	public static Result newTaskAJAX() {

		// System.out.println("ACCEPT: " + request().accept());
		JsonNode json = request().body().asJson();
		Task task = new Task();
		task.setLabel(json.get("label").getTextValue());
		task.setPriority(json.get("priority").getIntValue());
		task.setUser(UserAccount.findById(json.get("user_id").asLong()));
		task.setProject(Project.findById(json.get("project_id").asLong()));
		Task.create(task);
		//System.out.println(tasks.id);
		return ok(Json.toJson(task));

	}

	public static Result deleteTask(Long id) {
		Task.delete(id);
		return redirect(routes.Application.tasks());
	}

	public static Result projects() {
		return ok(projects.render(Project.findAll(), projectForm));
	}

	public static Result newProject() {
		Form<Project> filledForm = projectForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(projects.render(Project.findAll(), filledForm));
		} else {
			Project.create(filledForm.get());
			return redirect(routes.Application.projects());
		}
	}

	public static Result deleteProject(Long id) {
		Project.delete(id);
		return redirect(routes.Application.projects());
	}

	// -- Authentication

	public static class Login {

		public String identifier;
		public String password;

		public String validate() {
			if (UserAccount.authenticateMail(identifier, password) == null
					&& UserAccount.authenticateNickname(identifier, password) == null) {
				return "Invalid user or password";
			}
			return null;
		}

	}

	/**
	 * Login page.
	 */
	public static Result login() {
		return ok(login.render(form(Login.class)));
	}

	/**
	 * Handle login form submission.
	 */
	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			String identifier = loginForm.get().identifier;
			UserAccount currentUser;
			if (identifier.contains("@")) {
				currentUser = UserAccount.findByEmail(identifier);
			} else
				currentUser = UserAccount.findByNickname(identifier);
			session("nickname", currentUser.getNickname());
			return redirect(routes.Application.index());
		}
	}

	/**
	 * Logout and clean the session.
	 */
	public static Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.login());
	}

}
