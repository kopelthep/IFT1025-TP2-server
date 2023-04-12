package Client;


import server.models.Course;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Clientsimple {
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

    public Clientsimple(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    public static void main(String[] args) {
        try {
            Clientsimple client = new Clientsimple("localhost", 1337);

            Scanner scanner = new Scanner(System.in);
            System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
            System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
            System.out.println("1. Automne");
            System.out.println("2. Hiver");
            System.out.println("3. Ete");
            System.out.print("> Choix: ");
            String sessionChoice = scanner.nextLine();

            client.objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + sessionChoice);
            client.objectOutputStream.flush();

            List<Course> courses = (List<Course>) client.objectInputStream.readObject();
            System.out.println("Les cours offerts pendant la session choisie sont:");
            for (Course course : courses) {
                System.out.println(course.getCode() + "\t" + course.getName());
            }

            System.out.print("> Choix: ");
            String courseCode = scanner.nextLine();

            System.out.print("Veuillez saisir votre prénom: ");
            String prenom = scanner.nextLine();
            System.out.print("Veuillez saisir votre nom: ");
            String nom = scanner.nextLine();
            System.out.print("Veuillez saisir votre email: ");
            String email = scanner.nextLine();
            System.out.print("Veuillez saisir votre matricule: ");
            String matricule = scanner.nextLine();

            Course selectedCourse = courses.stream().filter(course -> course.getCode().equalsIgnoreCase(courseCode)).findFirst().orElse(null);
            if (selectedCourse == null) {
                System.out.println("Le code du cours saisi est invalide.");
                return;
            }

            server.models.RegistrationForm registrationForm = new server.models.RegistrationForm(prenom, nom, email, matricule, selectedCourse);
            client.objectOutputStream.writeObject(Server.REGISTER_COMMAND);
            client.objectOutputStream.writeObject(registrationForm);
            client.objectOutputStream.flush();

            String message = (String) client.objectInputStream.readObject();
            System.out.println(message);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

