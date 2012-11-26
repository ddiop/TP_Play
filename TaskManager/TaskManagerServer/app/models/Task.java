package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Min;
import play.db.ebean.Model;

@Entity
public class Task extends Model 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Required
	private String label;

	@Required
	@Min(0)
	private Integer priority;

	@ManyToOne
	private Project project;

	@Required 
	@ManyToOne
	private UserAccount user;

	public Long getId()
	{
		return id ;
	}

	public void setId(Long _id)
	{
		id = _id ;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String _label)
	{
		label = _label ;
	}

	public Integer getPriority()
	{
		return priority ;
	}

	public void setPriority(Integer _priority)
	{
		priority = _priority ;
	}

	public Project getProject()
	{
		return project ;
	}

	public void setProject(Project _project)
	{
		project = _project ;
	}	

	public UserAccount getUser()
	{
		return user ;
	}

	public void setUser(UserAccount _user)
	{
		user = _user ;
	}

	public static Finder<Long,Task> find = new Finder( Long.class, Task.class );

	public static List<Task> findAll() 
	{
		return find.all();
	}

	public static void create(Task task) 
	{
		task.save();
	}

	public static void delete(Long id) 
	{
		find.ref(id).delete();
	}
}
