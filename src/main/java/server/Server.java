package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transformer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        List<Course> courses = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("cours.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] courseInfo = line.split("\t"); // Utilisez une tabulation comme séparateur
                if (courseInfo.length != 3) {
                    System.err.println("Erreur de format dans le fichier cours.txt: " + line);
                    continue;
                }
                String code = courseInfo[0].trim();
                String name = courseInfo[1].trim();
                String session = courseInfo[2].trim();

                Course course = new Course(name, code, session);
                courses.add(course);
            }
            br.close();

            List<Course> filteredCourses = courses.stream()
                    .filter(course -> course.getSession().equalsIgnoreCase(arg))
                    .collect(Collectors.toList());

            objectOutputStream.writeObject(filteredCourses);
            objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gere les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try {
            RegistrationForm registrationForm = (RegistrationForm) objectInputStream.readObject();

            BufferedWriter bw = new BufferedWriter(new FileWriter("inscription.txt", true));
            String registrationLine = String.format("%s\t%s\t%s\t%s\t%s\t%s%n",
                    registrationForm.getCourse().getSession(),
                    registrationForm.getCourse().getCode(),
                    registrationForm.getMatricule(),
                    registrationForm.getPrenom(),
                    registrationForm.getNom(),
                    registrationForm.getEmail());
                    //The resulting registrationLine string will have the
                    // format:prenom, nom, email, matricule, course name, course code, course session (et un saut de ligne à la fin)

            bw.write(registrationLine);
            bw.close();

            objectOutputStream.writeObject("Inscription réussie!");
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

