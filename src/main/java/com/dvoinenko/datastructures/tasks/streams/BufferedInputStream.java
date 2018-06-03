package com.dvoinenko.datastructures.tasks.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends FilterInputStream {
    private static final int INITIAL_CAPACITY = 5;

    private InputStream inputStream;
    private byte[] buffer;
    private int index;
    private int count;

    public BufferedInputStream(InputStream inputStream) {
        this(inputStream, INITIAL_CAPACITY);
    }

    public BufferedInputStream(InputStream inputStream, int size) {
        super(inputStream);
        if (size <= 0) {
            throw new IllegalArgumentException("buffer size should be over 0 but is: " + size);
        }
        buffer = new byte[size];
        count = size;
    }

    @Override
    public int read() throws IOException {
        if (index == count) {
            count = inputStream.read(buffer);
            index = 0;
        } else if (count == -1) {
            return -1;
        }
        return buffer[index++];
    }

    @Override
    public int read(byte[] array, int off, int len) throws IOException {
        if (off < 0 || len < 0 || (array.length - off < len)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        } else if (index == count) {
            count = inputStream.read(buffer);
            index = 0;
        } else if (count == -1) {
            return -1;
        }

        int totalBytesCount = Math.min(count - index, len);
        System.arraycopy(buffer, index, array, off, totalBytesCount);
        index += totalBytesCount;
        off += totalBytesCount;
        len -= totalBytesCount;

        while (len > 0) {
            int remainBytesCount = Math.min(count - index, len);
            System.arraycopy(buffer, index, array, off, remainBytesCount);
            index += remainBytesCount;
            off += remainBytesCount;
            len -= remainBytesCount;
            totalBytesCount += remainBytesCount;
        }
        return totalBytesCount;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

}
