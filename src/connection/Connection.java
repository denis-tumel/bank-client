package connection;

import config.Const;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Connection {
    private static Connection instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private Connection() {

    }

    public static Connection getInstance() {
        lock.lock();
        if (!atomicBoolean.get()) {
            instance = new Connection();
            instance.connect();
            atomicBoolean.set(true);
        }
        lock.unlock();
        return instance;
    }

    private void connect() {
        try {
            socket = new Socket(Const.HOST, Const.PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void post(Object obj) {
        try {
            outputStream.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object get() {
        try {
            return inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
