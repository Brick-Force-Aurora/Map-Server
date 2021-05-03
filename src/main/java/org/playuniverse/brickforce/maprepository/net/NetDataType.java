package org.playuniverse.brickforce.maprepository.net;

import java.util.Arrays;
import java.util.List;

import com.syntaxphoenix.syntaxapi.net.http.NamedType;

public enum NetDataType implements NamedType {

    OCTET_STREAM("application/octet-stream", "bin");

    private final String type;
    private final List<String> extensions;

    private NetDataType(String type, String... extensions) {
        this.type = type;
        this.extensions = Arrays.asList(extensions);
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public boolean has(String extension) {
        return extensions.contains(extension.contains(".") ? extension.substring(1) : extension);
    }

    public static NetDataType parse(String extension) {
    	NetDataType[] types = values();
        for (int index = 0; index < types.length; index++) {
            if (types[index].has(extension)) {
                return types[index];
            }
        }
        return null;
    }

}
