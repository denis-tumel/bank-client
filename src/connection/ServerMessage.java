package connection;

public class ServerMessage {
    public static Object get() {
        return Connection.getInstance().get();
    }
}
