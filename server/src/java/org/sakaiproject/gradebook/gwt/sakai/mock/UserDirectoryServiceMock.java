package org.sakaiproject.gradebook.gwt.sakai.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.HttpAccess;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserAlreadyDefinedException;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserEdit;
import org.sakaiproject.user.api.UserIdInvalidException;
import org.sakaiproject.user.api.UserLockedException;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.api.UserPermissionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserDirectoryServiceMock implements UserDirectoryService {

	
	
	public UserEdit addUser(String arg0, String arg1)
			throws UserIdInvalidException, UserAlreadyDefinedException,
			UserPermissionException {
		// TODO Auto-generated method stub
		return null;
	}

	public User addUser(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6, ResourceProperties arg7)
			throws UserIdInvalidException, UserAlreadyDefinedException,
			UserPermissionException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean allowAddUser() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean allowRemoveUser(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean allowUpdateUser(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean allowUpdateUserEmail(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean allowUpdateUserName(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean allowUpdateUserPassword(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean allowUpdateUserType(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public User authenticate(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void cancelEdit(UserEdit arg0) {
		// TODO Auto-generated method stub

	}

	public void commitEdit(UserEdit arg0) throws UserAlreadyDefinedException {
		// TODO Auto-generated method stub

	}

	public int countSearchUsers(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int countUsers() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void destroyAuthentication() {
		// TODO Auto-generated method stub

	}

	public UserEdit editUser(String arg0) throws UserNotDefinedException,
			UserPermissionException, UserLockedException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection findUsersByEmail(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public User getAnonymousUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public User getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(String arg0) throws UserNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUserByEid(String arg0) throws UserNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserEid(String arg0) throws UserNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserId(String arg0) throws UserNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<User> users;
	private static int DEFAULT_NUMBER_TEST_LEARNERS = 200;
	
	public List getUsers(Collection arg0) {
		
		if (users == null) {
			users = new ArrayList<User>(DEFAULT_NUMBER_TEST_LEARNERS);
			for (int i=0;i<DEFAULT_NUMBER_TEST_LEARNERS;i++) {
				users.add(createUserRecord(i));
			}
		}
		
		return users;
	}

	public List getUsers(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserEdit mergeUser(Element arg0) throws UserIdInvalidException,
			UserAlreadyDefinedException, UserPermissionException {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeUser(UserEdit arg0) throws UserPermissionException {
		// TODO Auto-generated method stub

	}

	public List searchUsers(String arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public String userReference(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String archive(String arg0, Document arg1, Stack arg2, String arg3,
			List arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	public Entity getEntity(Reference arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getEntityAuthzGroups(Reference arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEntityDescription(Reference arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceProperties getEntityResourceProperties(Reference arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEntityUrl(Reference arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpAccess getHttpAccess() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String merge(String arg0, Element arg1, String arg2, String arg3,
			Map arg4, Map arg5, Set arg6) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean parseEntityReference(String arg0, Reference arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean willArchiveMerge() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	//
	// TEST DATA
	//
	private final String[] FIRST_NAMES = { "Joel", "John", "Kelly",
		"Freeland", "Bruce", "Rajeev", "Thomas", "Jon", "Mary", "Jane",
		"Susan", "Cindy", "Veronica", "Shana", "Shania", "Olin", "Brenda",
		"Lowell", "Doug", "Yiyun", "Xi-Ming", "Grady", "Martha", "Stewart", 
		"Kennedy", "Joseph", "Iosef", "Sean", "Timothy", "Paula", "Keith",
		"Ignatius", "Iona", "Owen", "Ian", "Ewan", "Rachel", "Wendy", 
		"Quentin", "Nancy", "Mckenna", "Kaylee", "Aaron", "Erin", "Maris", 
		"D.", "Quin", "Tara", "Moira", "Bristol" };

	private  final String[] LAST_NAMES = { "Smith", "Paterson",
		"Haterson", "Raterson", "Johnson", "Sonson", "Paulson", "Li",
		"Yang", "Redford", "Shaner", "Bradley", "Herzog", "O'Neil", "Williams",
		"Simone", "Oppenheimer", "Brown", "Colgan", "Frank", "Grant", "Klein",
		"Miller", "Taylor", "Schwimmer", "Rourer", "Depuis", "Vaugh", "Auerbach", 
		"Shannon", "Stepford", "Banks", "Ashby", "Lynne", "Barclay", "Barton",
		"Cromwell", "Dering", "Dunlevy", "Ethelstan", "Fry", "Gilly",
		"Goodrich", "Granger", "Griffith", "Herbert", "Hurst", "Keigwin", 
		"Paddock", "Pillings", "Landon", "Lawley", "Osborne", "Scarborough",
		"Whiting", "Wibert", "Worth", "Tremaine", "Barnum", "Beal", "Beers", 
		"Bellamy", "Barnwell", "Beckett", "Breck", "Cotesworth", 
		"Coventry", "Elphinstone", "Farnham", "Ely", "Dutton", "Durham",
		"Eberlee", "Eton", "Edgecomb", "Eastcote", "Gloucester", "Lewes", 
		"Leland", "Mansfield", "Lancaster", "Oakham", "Nottingham", "Norfolk",
		"Poole", "Ramsey", "Rawdon", "Rhodes", "Riddell", "Vesey", "Van Wyck",
		"Van Ness", "Twickenham", "Trowbridge", "Ames", "Agnew", "Adlam", 
		"Aston", "Askew", "Alford", "Bedeau", "Beauchamp" };
	
	private final String[] SECTIONS = { "001", "002", "003", "004" };
	
	private Random random = new Random();
	
	private int getRandomInt(int max) {
		return random.nextInt(max);
	}
	
	private String getRandomSection() {
		return SECTIONS[getRandomInt(SECTIONS.length)];
	}
	
	private User createUserRecord(int i) {
		String id = String.valueOf(i);
		String displayId = String.valueOf(100000 + getRandomInt(899999));
		String firstName = FIRST_NAMES[getRandomInt(FIRST_NAMES.length)];
		String lastName = LAST_NAMES[getRandomInt(LAST_NAMES.length)];
		String eid = lastName.toLowerCase();
		String lastNameFirst = lastName + ", " + firstName;
		String sortName = lastName.toUpperCase() + "  " + firstName.toUpperCase();
		String displayName = firstName + " " + lastName;
		String section = getRandomSection();
		String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@nowhere.edu";
	
		User userRecord = new UserMock(id, displayId, eid, firstName, lastName, email);
		//userRecord.setSectionTitle("Section " + section);
		//userRecord.setExportUserId(studentId);
		
		return userRecord;
	}


}
