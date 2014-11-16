package com.github.fritaly.googlecodejam;

import java.io.LineNumberReader;
import java.util.Set;
import java.util.TreeSet;

public class FileFixIt extends AbstractPuzzler {

	@Override
	protected boolean isLogEnabled() {
		return false;
	}

	private final class Directory implements Comparable<Directory> {
		final String name;

		final Directory parent;

		final Set<Directory> children = new TreeSet<>();

		public Directory() {
			this.parent = null;
			this.name = "";
		}

		public Directory(Directory parent, String name) {
			this.parent = parent;
			this.name = name;
		}

		boolean hasChild(String name) {
			return (getChild(name) != null);
		}

		Directory getChild(String name) {
			for (Directory child : this.children) {
				if (child.name.equals(name)) {
					return child;
				}
			}

			return null;
		}

		String getPath() {
			if (parent == null) {
				return "/";
			}

			final StringBuilder buffer = new StringBuilder();
			buffer.append(parent.getPath());

			if (buffer.charAt(buffer.length() - 1) != '/') {
				buffer.append("/");
			}

			return buffer.append(name).toString();
		}

		Directory addChild(String name) {
			if ("".equals(name)) {
				throw new IllegalArgumentException("Can't create a directory with a blank name");
			}

			final Directory dir = new Directory(this, name);

			this.children.add(dir);

			log("Created directory " + dir.getPath());

			return dir;
		}

		@Override
		public int compareTo(Directory other) {
			return this.name.compareTo(other.name);
		}
	}

	public static void main(String[] args) throws Exception {
		new FileFixIt().run();
	}

	@Override
	protected String solve(LineNumberReader reader) throws Exception {
		final String line1 = reader.readLine();
		final int numberOfExistingDirs = Integer.parseInt(line1.split(" ")[0]);
		final int numberOfDirsToCreate = Integer.parseInt(line1.split(" ")[1]);

		// Create an in-memory tree representing the existing directories
		final Directory rootDir = new Directory();

		log("Creating existing directories");

		for (int i = 0; i < numberOfExistingDirs; i++) {
			final String path = reader.readLine();
			final String chunks[] = path.split("/");

			Directory currentDir = rootDir;

			for (int j = 0; j < chunks.length; j++) {
				final String directoryName = chunks[j];

				if (directoryName.equals("")) {
					// That's the root directory which always exists
					continue;
				}

				if (!currentDir.hasChild(directoryName)) {
					currentDir = currentDir.addChild(directoryName);
				} else {
					currentDir = currentDir.getChild(directoryName);
				}
			}
		}

		log("Creating missing directories");

		int count = 0;

		for (int i = 0; i < numberOfDirsToCreate; i++) {
			final String path = reader.readLine();

			final String chunks[] = path.split("/");

			Directory currentDir = rootDir;

			for (int j = 0; j < chunks.length; j++) {
				final String directoryName = chunks[j];

				if (directoryName.equals("")) {
					// That's the root directory which always exists
					continue;
				}

				if (!currentDir.hasChild(directoryName)) {
					currentDir = currentDir.addChild(directoryName);

					count++;
				} else {
					currentDir = currentDir.getChild(directoryName);
				}
			}
		}

		return Integer.toString(count);
	}
}