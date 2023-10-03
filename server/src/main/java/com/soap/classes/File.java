package com.soap.classes;

import java.io.Serializable;

public class File implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private byte[] content;
    private long size;

    public File(String name, byte[] content, long size) {
        this.name = name;
        this.content = content;
        this.size = size;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "File [name=" + name + ", size=" + size + "]";
    }
}
