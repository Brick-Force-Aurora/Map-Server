package org.playuniverse.brickforce.maprepository.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.syntaxphoenix.syntaxapi.utils.java.Streams;

public final class Geometry {

	public Geometry(File file) throws FileNotFoundException, IOException {
		this(new FileInputStream(file));
	}

	public Geometry(InputStream stream) throws IOException {
		this(Streams.toByteArray(stream));
	}

	public Geometry(byte[] data) {
		
	}



}
