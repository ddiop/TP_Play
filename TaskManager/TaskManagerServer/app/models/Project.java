package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;




@Entity
public class Project extends Model
{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Required
	private String label;

	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="project")
	private List<Task> tasks;

	@Temporal( TemporalType.DATE )
	private Date creationDate = new Date();
	
	public Long getId()
	{
		return id;
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
		label = _label;
	}

	public List<Task> getTasks()
	{
		return tasks;
	}

	public Date getCreationDate()
	{
		return creationDate ;
	}

	public static Finder<Long,Project> find = new Finder( Long.class, Project.class );

	public static List<Project> findAll() 
	{
		return find.all();
	}
	
	 public static Map<String,String> options() {
	        List<Project> projects = findAll();
	        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
	        for(Project p: projects) {
	            options.put(p.id.toString(), p.label);
	        }
	        return options;
	    }
	 
	 public static Project findById(Long id) 
		{
			return find.ref(id);
		}
	
	public static void delete(Long id) 
	{
		find.ref(id).delete();
	}
	
	public static void create(Project project) 
	{
		project.save();
	}
}
