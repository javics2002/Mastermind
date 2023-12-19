package com.example.libengineandroid;

import com.example.aninterface.IFile;

import java.io.FileInputStream;

public class FileAndroid implements IFile {
    private FileInputStream _fileInputStream;

    public FileAndroid(FileInputStream fileInputStream) {
        _fileInputStream = fileInputStream;
    }

    public FileInputStream getInputStream() {
        return _fileInputStream;
    }
}
