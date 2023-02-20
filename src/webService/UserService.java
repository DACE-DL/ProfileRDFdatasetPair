package webService;



import java.util.ArrayList;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/users")
public class UserService {

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public User getUser( @PathParam("id") int id ) {
		User user = new User(id, "jdoe", 22);
		return user;
	}  


	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ArrayList<User> getUsers() {
		ArrayList<User> listUsers = new ArrayList<User>();
		User user1 = new User(1, "jdoe", 22);
		User user2 = new User(2, "rapha", 52);
		listUsers.add(user1);
		listUsers.add(user2);
		System.out.println(listUsers);
		return listUsers;
	}  

		// Pour tester
		// http://localhost:8085/AobraWebService/api/users/1

	}
