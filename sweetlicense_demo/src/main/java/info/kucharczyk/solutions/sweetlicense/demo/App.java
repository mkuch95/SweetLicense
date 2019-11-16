package info.kucharczyk.solutions.sweetlicense.demo;

import info.kucharczyk.solutions.sweetlicense.demo.authority.AuthorityApp;
import info.kucharczyk.solutions.sweetlicense.demo.client.ClientApp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String args[]) throws Exception {
        App app = new App();
        app.main();
    }

    private void clean() throws IOException {
        Files.deleteIfExists(Paths.get(ClientApp.LICENSE_PATH));
        Files.deleteIfExists(Paths.get(ClientApp.LICENSE_REQUEST_PATH));
        Files.deleteIfExists(Paths.get(AuthorityApp.LICENSE_PATH));
        Files.deleteIfExists(Paths.get(AuthorityApp.LICENSE_REQUEST_PATH));
    }

    public void main() throws Exception {
        clean();
        System.out.println("------ Client: ------");
        ClientApp.main(new String[]{});

        System.out.println(">>>>>> Move the license request file: Client -> Authority server");
        Files.copy(Paths.get(ClientApp.LICENSE_REQUEST_PATH), Paths.get(AuthorityApp.LICENSE_REQUEST_PATH));

        System.out.println("------ Authority server: ------");
        AuthorityApp.main(new String[]{});

        System.out.println(">>>>>> Move the license file: Authority server -> Client");
        Files.copy(Paths.get(AuthorityApp.LICENSE_PATH), Paths.get(ClientApp.LICENSE_PATH));

        System.out.println("------ Client: ------");
        ClientApp.main(new String[]{});
    }
}
