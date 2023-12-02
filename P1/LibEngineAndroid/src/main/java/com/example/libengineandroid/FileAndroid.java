package com.example.libengineandroid;

import com.example.aninterface.IFile;

public class FileAndroid implements IFile {
    String _content;
    public FileAndroid(String content) {
        _content = content;
    }

    @Override
    public String getContent(){
        return _content;
    }
}
