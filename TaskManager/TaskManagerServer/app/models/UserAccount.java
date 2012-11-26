package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class UserAccount extends Model
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Email @Required
	private String mail;

	@Required @Column(unique=true)
	private String nickname;

	@Required
	private String password;
	
	public Long getId()
	{
		return id ;
	}

	public void setId(Long _id)
	{
		id = _id ;
	}

	public String getMail()
	{
		return mail ;
	}	

	public void setMail(String _mail)
	{
		mail = _mail ;
	}

	public String getNickname()
	{
		return nickname ;
	}	

	public void setNickname(String _nickname)
	{
		nickname = _nickname ;
	}

	public String getPassword()
	{
		return password ;
	}	

	public void setPassword(String _password)
	{
		password = _password ;
	}

	public static Finder<Long,UserAccount> find = new Finder( Long.class, UserAccount.class );
	
	public static List<UserAccount> findAll()
	{
		return find.all();
	}

	public static Map<String,String> options() {
        List<UserAccount> users = findAll();
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(UserAccount u: users) {
            options.put(u.id.toString(), u.nickname);
        }
        return options;
    }
	
	public static UserAccount findById(Long id) 
	{
		return find.ref(id);
	}
	
	    /**
	     * Retrieve a User from email.
	     */
	public static UserAccount findByEmail(String email) 
	{
		return find.where().eq("mail", email).findUnique();
	}
    
		/**
	     * Retrieve a User from nickname.
	     */
	public static UserAccount findByNickname(String nickname) 
	{
		return find.where().eq("nickname", nickname).findUnique();
	}

    /**
     * Authenticate a User.
     */
    public static UserAccount authenticateMail(String email, String password) {
        return find.where()
            .eq("mail", email)
            .eq("password", password)
            .findUnique();
    }

/**
     * Authenticate a User.
     */
    public static UserAccount authenticateNickname(String nickname, String password) {
        return find.where()
            .eq("nickname", nickname)
            .eq("password", password)
            .findUnique();
    }

}
