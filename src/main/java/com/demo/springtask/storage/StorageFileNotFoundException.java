package com.demo.springtask.storage;

public class StorageFileNotFoundException extends StorageException {
    public StorageFileNotFoundException() {super();}

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}