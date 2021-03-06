package pl.kognitywistyka.app.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.kognitywistyka.app.course.Course;
import pl.kognitywistyka.app.security.AuthenticationService;
import pl.kognitywistyka.app.service.CourseService;
import pl.kognitywistyka.app.user.Student;

import java.util.ArrayList;

/**
 * Created by wikto on 20.06.2017.
 */
public class CourseWindow extends ItemWindow {

    //Layouts
    private VerticalLayout middleLayer;
    private CssLayout courseNameLayout;
    private CssLayout facultyLayout;
    private CssLayout syllabusLayout;

    private HorizontalLayout buttonLayout;

    //Labels
    //todo lol, vaadin, seriously, only labels?
    private Label courseNameLabel;
    private Label facultyLabel;
    private TextArea syllabusLabel;

    //Buttons
    private Button previousScreenButton;
    private Button acceptButton;
    private Button rejectButton;

    //Variables
    private Course course;
    private GridWindow previousWindow;
    private Button registerDeleteButton;
    private CourseService courseService = CourseService.getInstance();

    public CourseWindow(Course course, Component previousWindow) {
        setCourse(course);
        setPreviousWindow(previousWindow);
        init();
    }

    private void init() {

        setSizeFull();

        //Initializing layout
        middleLayer = new VerticalLayout();
        middleLayer.setSizeFull();
//        middleLayer.addStyleName("login-panel");

        init(middleLayer);

        middleLayer.setWidth("600px");
        middleLayer.setHeight("600px");

        //Initializing name
        courseNameLayout = new CssLayout();
        courseNameLayout.setSizeFull();

        courseNameLabel = new Label("Course Name: " + course.getCourseName());

        courseNameLayout.addComponent(courseNameLabel);
        middleLayer.addComponent(courseNameLayout);
        middleLayer.setComponentAlignment(courseNameLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(courseNameLayout, 0.1f);

        //Initializing faculty
        facultyLayout = new CssLayout();
        facultyLayout.setSizeFull();

        facultyLabel = new Label("Faculty: " + course.getFaculty());

        facultyLayout.addComponent(facultyLabel);
        middleLayer.addComponent(facultyLayout);
        middleLayer.setComponentAlignment(facultyLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(facultyLayout, 0.1f);

        //Initializing syllabus
        syllabusLayout = new CssLayout();
        syllabusLayout.setSizeFull();

        syllabusLabel = new TextArea("Syllabus: ");
        syllabusLabel.setValue(course.getSyllabus());
        syllabusLabel.setReadOnly(true);
        syllabusLabel.setWidth("600px");
        syllabusLabel.setHeight("300px");
        syllabusLabel.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);

        syllabusLayout.addComponents(syllabusLabel);
        middleLayer.addComponent(syllabusLayout);
        middleLayer.setComponentAlignment(syllabusLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(syllabusLayout, 0.8f);

        //Initializing buttons
        buttonLayout = new HorizontalLayout();

        previousScreenButton = new Button("Return to course list", VaadinIcons.ANGLE_LEFT);
        previousScreenButton.addClickListener(event -> {
            getUI().getCurrent().setContent(previousWindow);
        });

        buttonLayout.addComponent(previousScreenButton);
        middleLayer.addComponent(buttonLayout);
        middleLayer.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);

        if (!AuthenticationService.getInstance().isAdmin()) {
            Student student = (Student) AuthenticationService.getInstance().getCurrentLoginInfo();
            if (!student.isRegisteredTo(course)) {
                registerDeleteButton = new Button("Register");
                registerDeleteButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

                registerDeleteButton.addClickListener(event -> {
                    //Initializing buttons
                    Button cancelButton = new Button("Cancel");
                    cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
                    Button sureButton = new Button("I'm sure");
                    sureButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

                    sureButton.addClickListener(clickEvent -> {
                        boolean registered = courseService.register(course);
                        showNotification(registered);
                    });

                    ArrayList<Button> buttonsList = new ArrayList<>();
                    buttonsList.add(cancelButton);
                    buttonsList.add(sureButton);

                    getUI().getUI().addWindow(showWarning(buttonsList));
                });
            } else {
                registerDeleteButton = new Button("Unregister");
                registerDeleteButton.setStyleName(ValoTheme.BUTTON_DANGER);

                registerDeleteButton.addClickListener(event -> {
                    //Initializing buttons
                    Button cancelButton = new Button("Cancel");
                    cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
                    Button sureButton = new Button("I'm sure");
                    sureButton.setStyleName(ValoTheme.BUTTON_DANGER);

                    sureButton.addClickListener(clickEvent -> {
                        CourseService courseService = CourseService.getInstance();
                        boolean registered = courseService.unregister(course);
                        showNotification(registered);
                    });

                    ArrayList<Button> buttonsList = new ArrayList<>();
                    buttonsList.add(cancelButton);
                    buttonsList.add(sureButton);

                    getUI().getUI().addWindow(showWarning(buttonsList));
                });
            }
        } else {
            registerDeleteButton = new Button("Delete");
            registerDeleteButton.setStyleName(ValoTheme.BUTTON_DANGER);

            registerDeleteButton.addClickListener(event -> {
                //Initializing buttons
                Button cancelButton = new Button("Cancel");
                cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
                Button sureButton = new Button("I'm sure");
                sureButton.setStyleName(ValoTheme.BUTTON_DANGER);

                sureButton.addClickListener(clickEvent -> {
                    CourseService courseService = CourseService.getInstance();
                    boolean deleted = courseService.delete(course);
                    if (deleted) {
                        previousWindow.updateGrid();
                        getUI().getCurrent().setContent(previousWindow);
                    }
                    showNotification(deleted);
                });

                ArrayList<Button> buttonsList = new ArrayList<>();
                buttonsList.add(cancelButton);
                buttonsList.add(sureButton);

                getUI().getUI().addWindow(showWarning(buttonsList));
            });
            if (!course.isAccepted()) {
                acceptButton = new Button("Accept course");
                acceptButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

                acceptButton.addClickListener(clickEvent -> courseService.acceptCourse(course));
                acceptButton.addClickListener(clickEvent -> getUI().getCurrent().setContent(previousWindow));

                rejectButton = new Button("Reject course");

                rejectButton.addClickListener(clickEvent -> courseService.rejectCourse(course));
                acceptButton.addClickListener(clickEvent -> getUI().getCurrent().setContent(previousWindow));

                buttonLayout.addComponents(acceptButton, rejectButton);
            }
        }

        buttonLayout.addComponent(registerDeleteButton);
        middleLayer.addComponent(buttonLayout);
        middleLayer.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);

        initTop();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setPreviousWindow(Component previousWindow) {
        this.previousWindow = (GridWindow) previousWindow;
    }
}
