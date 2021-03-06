package org.sbrubles.zcontainer.impl.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
	private static final int BUFFER_SIZE = 4096;

	private static void extractFile(ZipInputStream in, File outdir, String name)
			throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(new File(outdir, name)));
		int count = -1;
		while ((count = in.read(buffer)) != -1)
			out.write(buffer, 0, count);
		out.close();
	}

	private static void mkdirs(File outdir, String path) {
		File d = new File(outdir, path);
		if (!d.exists())
			d.mkdirs();
	}

	private static String dirpart(String name) {
		int s = name.lastIndexOf(File.separatorChar);
		return s == -1 ? null : name.substring(0, s);
	}

	/***
	 * Extract zipfile to outdir with complete directory structure
	 * 
	 * @param zipfile
	 *            Input .zip file
	 * @param outdir
	 *            Output directory
	 */
	public static void extract(File zipfile, File outdir) {
		try {
			extract(new FileInputStream(zipfile), outdir);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static void extract(InputStream in, File outdir) {
		try {
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry entry;
			String name, dir;
			while ((entry = zin.getNextEntry()) != null) {
				name = entry.getName();
				if (entry.isDirectory()) {
					mkdirs(outdir, name);
					continue;
				}
				/*
				 * this part is necessary because file entry can come before
				 * directory entry where is file located i.e.: /foo/foo.txt
				 * /foo/
				 */
				dir = dirpart(name);
				if (dir != null)
					mkdirs(outdir, dir);

				extractFile(zin, outdir, name);
			}
			// TODO: was throwing IOException: Pipe closed
			//zin.close();
		} catch (IOException e) {
			throw new RuntimeException("Error unziping InputStream to " + outdir, e);
		}
	}
}