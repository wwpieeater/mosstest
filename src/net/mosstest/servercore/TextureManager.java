package net.mosstest.servercore;

import java.util.ArrayList;


public class TextureManager {
	static ArrayList<MossFile> texs=new ArrayList<>();
	static MossFile addTexture(MossFile tex){
		for(MossFile thisTex : texs){
			if(tex.sha512==thisTex.sha512) return thisTex;
		}
		return tex;
	}
}
