package net.mosstest.launcher;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class MossDebugUtils {

	private static final String[] propertiesToGet = { "awt.toolkit",
			"file.encoding", "file.separator", "java.class.version",
			"java.home", "java.runtime.name", "java.runtime.version",
			"java.specification.name", "java.specification.vendor",
			"java.specification.version", "java.vendor", "java.vm.info",
			"java.vm.name", "java.vm.specification.name", "java.vm.version",
			"os.arch", "os.name", "path.separator", "sun.arch.data.model",
			"sun.cpu.endian", "sun.desktop", "user.language"

	};

	public static String getDebugInformation(Exception e) {
		StringBuilder s = new StringBuilder(
				MossDebugUtils.getGitConfig("git.commit.id") + " on "+ MossDebugUtils.getGitConfig("git.branch")+ "\r\n");
		s.append("Built on: "+ MossDebugUtils.getGitConfig("git.build.time")+"\r\n\r\n");
		s.append("<<<Exception caught>>> \r\n");
		for (StackTraceElement ste : e.getStackTrace()) {
			s.append(ste.toString()).append("\r\n");
		}
		s.append("\r\n");
		s.append(getOsDetails());
		return s.toString();
	}

	public static String getOsDetails() {
		StringBuilder s = new StringBuilder();

		s.append("<<<System properties>>>\r\n");
		s.append("Available cores: "
				+ Runtime.getRuntime().availableProcessors() + "\r\n");
		s.append("Free memory (bytes): " + Runtime.getRuntime().freeMemory()
				+ "\r\n");
		long maxMemory = Runtime.getRuntime().maxMemory();
		s.append("Maximum memory (bytes): "
				+ (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory)
				+ "\r\n");
		s.append("Total memory (bytes): " + Runtime.getRuntime().totalMemory()
				+ "\r\n");
		File[] roots = File.listRoots();

		for (File root : roots) {
			s.append("File system root: " + root.getAbsolutePath() + "\r\n");
			s.append("Total space (bytes): " + root.getTotalSpace() + "\r\n");

			s.append("Free space (bytes): " + root.getFreeSpace() + "\r\n");
			s.append("Usable space (bytes): " + root.getUsableSpace() + "\r\n");
		}
		s.append("\r\n");
		s.append("<<<Java properties>>>\r\n");
		for (String key : propertiesToGet) {
			s.append(key + ":" + System.getProperty(key, "") + "\r\n");
		}
		return s.toString();
	}

	private static String getGitConfig(String cfgKey) {

		Properties properties = new Properties();
		try {
			properties.load(MossDebugUtils.class.getClassLoader()
					.getResourceAsStream("git.properties"));
		} catch (IOException e) {
			return "IOException loading, perhaps non-existent?";
		}
		return properties.getProperty(cfgKey);

	}

	public static void main(String[] args) {
		System.out.println(getDebugInformation(new Exception()));
	}
}
