package scintilla;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Scintilla {
    // block initialisation of multiple WebServers
    private Scintilla() {
    }

    private static final WebServer INSTANCE = new WebServer();

    public static void initialize() {
        INSTANCE.initialize();
    }

    private static void downloadAndUnZip(String urlPath, String where) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();

        ZipInputStream zis = new ZipInputStream(in);
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            newFile(new File(where), zipEntry, zis);
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    public static void newFile(File destinationDir, ZipEntry zipEntry, ZipInputStream zis) throws IOException {
        if (zipEntry.isDirectory())
            return;

        byte[] buffer = new byte[1024];

        File destFile;
        try {
            destFile = new File(Path.of(Scintilla.class.getResource(destinationDir.toPath().toString()).toURI()).toFile(), zipEntry.getName());
        } catch (URISyntaxException e) {
            return;
        }
        destFile.createNewFile();

        // write file content
        FileOutputStream fos = new FileOutputStream(destFile);
        int len;
        while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
    }

    public static void start() {
        // try to update frontend
        // (bit of a hard coded url but who cares)
        try {
            downloadAndUnZip("https://project21t3comp2511.blob.core.windows.net/frontend/frontend.zip", "/app/");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(
                    "ERROR: Failed to download and/or unzip (possibly updated) frontend, just using current cached version... this is probably okay, since it's probably just because you aren't connected to the internet.");
        }

        INSTANCE.finalizeWebServer();
        PlatformUtils.openBrowserAtPath(INSTANCE.getHostUrl() + "/app/");
    }
}
