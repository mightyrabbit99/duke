package duke;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import duke.parser.*;
import duke.storage.Storage;
import duke.task.*;
import duke.ui.*;

import java.util.Scanner;

/**
 * Main UI method.
 */
public class Duke extends Application {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * If no file path specified, default path is assumed
     */
    public Duke() {
        this(Storage.DEFAULT_PATH);
    }

    /**
     * constructor to specify file path of the last saved data
     *
     * @param filePath = path of last saved data file
     */
    public Duke(String filePath) {
        ui = new Ui(new Scanner(System.in));
        storage = new Storage(filePath);
        if (storage.fileExist()) {
            try {
                tasks = TaskList.fromCSVList(storage.loadCSVList());
            } catch (Exception e) {
                ui.respond(Ui.loadingErrorMsg);
                tasks = new TaskList();
            }
        } else {
            tasks = new TaskList();
        }
    }

    /**
     * Main method.
     */
    public void run() {
        ui.respond(Ui.greetings);
        Command cmd;
        while (ui.hasNextLine()) {
            cmd = Parser.parse(ui.nextLine());
            cmd.execute(this.tasks, this.ui, this.storage);
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            new Duke(args[0]).run();
        } else {
            new Duke().run();
        }
    }

    @Override
    public void start(Stage stage) {
        Label helloWorld = new Label("Hello World!"); // Creating a new Label control
        Scene scene = new Scene(helloWorld); // Setting the scene to be our Label

        stage.setScene(scene); // Setting the stage to show our screen
        stage.show(); // Render the stage.
    }

}
