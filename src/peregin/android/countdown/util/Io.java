package peregin.android.countdown.util;

import java.io.*;
import java.net.Socket;

/**
 * Provides I/O utilities.
 */
public class Io {

    /**
     * Size of the copy buffer.
     */
    private static final int BUFFER_SIZE = 2048;

    /**
     * Copy text read from the input stream to the output stream.
     * 
     * @param in specifies the data source to read.
     * @param out specifies the data target to write.
     * @throws java.io.IOException if any IO operation fails.
     */
    public static void copy(Reader in, Writer out) throws IOException {
        char[] buffer = new char[BUFFER_SIZE];

        for (;;) {
            int read = in.read(buffer, 0, BUFFER_SIZE);
            if (read == -1) {
                break;
            }

            out.write(buffer, 0, read);
            out.flush();
        }
        out.flush();
    }

    /**
     * Copy bytes read from the input stream to the output stream.
     *
     * @param in specifies the data source to read.
     * @param out specifies the data target to write.
     * @throws java.io.IOException if any IO operation fails.
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        for (;;) {
            int read = in.read(buffer, 0, BUFFER_SIZE);
            if (read == -1) {
                break;
            }

            out.write(buffer, 0, read);
            out.flush();
        }
        out.flush();
    }

    /**
     * Closes the given input stream without throwing exception.
     *
     * @param in the input stream to close.
     */
    public static void close(InputStream in) {
        if (in == null) {
            return;
        }

        try {
            in.close();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
    }

    /**
     * Closes the given reader without throwing exception.
     *
     * @param in the reader to close.
     */
    public static void close(Reader in) {
        if (in == null) {
            return;
        }

        try {
            in.close();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
    }

    /**
     * Closes the given output stream without throwing exception.
     *
     * @param out the output stream to close.
     */
    public static void close(OutputStream out) {
        if (out == null) {
            return;
        }

        try {
            out.close();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
    }

    /**
     * Closes the given writer without throwing exception.
     *
     * @param out the writer to close.
     */
    public static void close(Writer out) {
        if (out == null) {
            return;
        }

        try {
            out.close();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
    }

    /**
     * Closes the given socket without throwing exception.
     *
     * @param s the socket to close.
     */
    public static void close(Socket s) {
        if (s == null) {
            return;
        }

        try {
            s.close();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
    }

    /**
     * Retreives the length of the stream measured in characters.
     *
     * @param in the stream to read.
     * @return the size of the content measured in characters.
     * @throws java.io.IOException if any IO operation fails.
     */
    public static long length(Reader in) throws IOException {
        long contentLength = 0;
        char[] buffer = new char[BUFFER_SIZE];

        for (;;) {
            int read = in.read(buffer, 0, BUFFER_SIZE);
            if (read == -1) {
                break;
            }
            contentLength += read;
        }

        return contentLength;
    }

    /**
     * Reads the the content of the stream in a buffer.
     *
     * @param in input stream to read.
     * @return a string buffer with the content of the stream.
     * @throws java.io.IOException if any I/O failure occures.
     */
    public static StringBuffer readString(InputStream in) throws IOException {
        StringWriter to = new StringWriter();
        InputStreamReader from = new InputStreamReader(in);
        char[] buffer = new char[BUFFER_SIZE];

        for (;;) {
            int read = from.read(buffer, 0, BUFFER_SIZE);
            if (read == -1) {
                break;
            }
            to.write(buffer, 0, read);
        }
        from.close();
        to.flush();
        to.close();

        return to.getBuffer();
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        return readBytes(in, -1);
    }

    /**
     * Read the bytes from the stream. When the read limit is reached I/O exception is thrown.
     *
     * @param in the input stream to read.
     * @param limit the maximum number of bytes to read.
     * @return a byte array with the content of the stream.
     * @throws java.io.IOException if any I/O error occurs.
     */
    public static byte[] readBytes(InputStream in, int limit) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int count = 0;

        for (;;) {
            int read = in.read(buffer, 0, BUFFER_SIZE);
            if (read == -1) {
                break;
            }

            bos.write(buffer, 0, read);
            count += read;
            if (limit > 0 && count > limit) {
                throw new IOException("Too large content: " + count);
            }
        }
        return bos.toByteArray();
    }

    /**
     * Read the bytes of the given file.
     *
     * @param name the name of the file.
     * @return the byte array.
     * @throws java.io.IOException if any I/O operation fails.
     */
    public static byte[] readBytes(String name) throws IOException {
        InputStream in = new FileInputStream(name);
        byte[] barr = readBytes(in, -1);
        close(in);
        return barr;
    }

    /**
     * Writes the object into a byte array.
     *
     * @param obj the object to write.
     * @return the byte array.
     * @throws java.io.IOException when any I/O error occures.
     */
    public static byte[] writeObject(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        bos.flush();
        byte[] barr = bos.toByteArray();
        oos.close();
        bos.close();
        return barr;
    }

    /**
     * Transforms the given object from the byte array.
     *
     * @param arr the byte array to transform.
     * @return an object from the byte array.
     * @throws java.io.IOException when any I/O error occures.
     * @throws ClassNotFoundException when the class does not exist.
     */
    public static Object readObject(byte[] arr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        ObjectInputStream oin = new ObjectInputStream(bis);
        Object obj = oin.readObject();
        oin.close();
        bis.close();
        return obj;
    }
}
