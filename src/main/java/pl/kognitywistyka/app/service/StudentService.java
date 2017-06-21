package pl.kognitywistyka.app.service;

import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.user.Student;
import pl.kognitywistyka.app.user.User;

import java.util.*;

/**
 * Created by wikto on 21.06.2017.
 */
public class StudentService {

    private static StudentService instance;
    private HashMap<String, User> users;

    public static StudentService getInstance() {
        if (instance == null){
            instance = new StudentService();
            instance.ensureTestData();
        }
        return instance;
    }

    public void ensureTestData() {
        users = new HashMap<>();
        List<User> usersList = Arrays.asList(
                new Student("1", "Radosław", "Jurczak"),
                new Student("2", "Jakub", "Milewski"),
                new Student("3", "Wiktor", "Rorot")
        );
//        Integer id = 0;
        for(User user : usersList) {
            String id = user.getId();
            users.put(id, user);
//            id = id + 1;
        }
    }

    //todo
    public synchronized List<User> findAll() {
        return new ArrayList<>();
    }

    public synchronized List<User> findAll(String value) {
        ArrayList<User> arrayList = new ArrayList<>();
        for (User user : users.values()) {
            boolean passesFilter = (value == null || value.isEmpty())
                    || user.toString().toLowerCase().contains(value.toLowerCase());
            if (passesFilter) {
                //todo we should implement ordering here
                arrayList.add(user);
            }
        }
//        Collections.sort(arrayList, (o1, o2) -> (int) ( - o1.getLocalId()));
        return arrayList;
    }

    public void register(Set<Course> selectedCourses) {
    }

    public boolean delete(User user) {
        users.remove(user.getId());
        return true;
    }

    public boolean delete(Set<User> selectedUsers) {
        for(User user : selectedUsers) {
            users.remove(user.getId());
        }
        return true;
    }
}