package scintilla;

public class Environment {

    private static final String PREFIX = "scintilla:";

    private static volatile String IPAddress = null;
    private static volatile Integer Port = null;
    private static volatile Boolean IsHeadless = null;
    private static volatile Boolean IsSecure = null;

    public static final synchronized String getIPAddress() {
        return (IPAddress == null && (IPAddress = System.getenv(PREFIX + "ADDRESS")) == null)
            ? IPAddress = "localhost" // default to localhost
            : IPAddress;
    }

    public static final synchronized int getPort() {
        if (Port != null) return Port;
        try {
            return Port = Integer.parseInt(System.getenv(PREFIX + "PORT"));
        } catch (Exception e) {
            return Port = 4568;
        }
    }

    public static final synchronized boolean isHeadless() {
        return IsHeadless != null
            ? IsHeadless.booleanValue()
            : (IsHeadless = (System.getenv(PREFIX + "HEADLESS") != null));
    }

    public static final synchronized boolean isSecure() {
        return IsSecure != null
            ? IsSecure.booleanValue()
            : (IsSecure = (System.getenv(PREFIX + "SECURE") != null));
    }
}
