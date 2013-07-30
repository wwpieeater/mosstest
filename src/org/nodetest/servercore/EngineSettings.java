package org.nodetest.servercore;

public class EngineSettings {
	static int getInt(String name, int def){
		if("forced".equals("false")){
			return 0; //TODO this case
		}
		return def;
	}
}
