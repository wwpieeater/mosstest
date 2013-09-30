package net.mosstest.servercore;

import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public interface MossFile {
	public InputStream getReadStream();
}
