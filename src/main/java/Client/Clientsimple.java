package Client;

import server.models.Course;
import server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
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

    public void closeConnection() throws IOException {
        objectInputStream.close();
        objectOutputStream.close();
        socket.close();
    }
    private static String convertSessionChoiceToSessionName(String sessionChoice) {
        switch (sessionChoice) {
            case "1":
                return "Automne";
            case "2":
                return "Hiver";
            case "3":
                return "Ete";
            default:
                return null;
        }
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
            String sessionName = convertSessionChoiceToSessionName(sessionChoice);

            if (sessionName == null) {
                System.out.println("Choix de session invalide.");
                return;
            }

            client.objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + sessionName);
            client.objectOutputStream.flush();

            List<Course> availableCourses = (List<Course>) client.objectInputStream.readObject();
            client.closeConnection();
            System.out.println("Les cours offerts pendant la session choisie sont:");
            int index = 1;
            for (Course course : availableCourses) {
                System.out.println(index + ". " + course.getName() + "\t" + course.getCode());
                index++;
            }

            System.out.print("> Choix: ");
            int courseIndex = Integer.parseInt(scanner.nextLine());

            if (courseIndex < 1 || courseIndex > availableCourses.size()) {
                System.out.println("Le choix du cours saisi est invalide.");
                return;
            }

            Course selectedCourse = availableCourses.get(courseIndex - 1);

            System.out.print("Veuillez saisir votre prénom: ");
            String prenom = scanner.nextLine();
            System.out.print("Veuillez saisir votre nom: ");
            String nom = scanner.nextLine();
            System.out.print("Veuillez saisir votre email: ");
            String email = scanner.nextLine();
            System.out.print("Veuillez saisir votre matricule: ");
            String matricule = scanner.nextLine();

            client = new Clientsimple("localhost", 1337);


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
