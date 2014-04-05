package net.mosstest.scripting;

import java.util.Arrays;

public class CubeTextureSet {
    // 0 = top
    // 1 = front(toward player when placing, relative to facedir)
    // 2 = right(relative to facedir)
    // 3 = back (away from player when placing, relative to facedir)
    // 4 = left (relative to facedir)
    // 5 = bottom
    private String[] textures = new String[6];

    public CubeTextureSet(String top, String front, String right, String back, String left, String bottom) {
        this.textures = new String[]{top, front, right, back, left, bottom};
    }

    public CubeTextureSet(){
        textures = new String[6];
    }

    public void setAll(String tex){
        for(int i = 0; i < 6; i++){
            textures[i] = tex;
        }
    }

    public void setTopBottom(String tex){
        textures[0] = tex;
        textures[5] = tex;
    }

    public void setCircumference(String tex){
        textures[1] = tex;
        textures[2] = tex;
        textures[3] = tex;
        textures[4] = tex;
    }
    public void setSides(String tex){
        textures[2] = tex;
        textures[4] = tex;
    }
    public void setTop(String tex){
        textures[0] = tex;
    }

    public void setFront(String tex){
        textures[1] = tex;
    }

    public void setRight(String tex){
        textures[2] = tex;
    }

    public void setBack(String tex){
        textures[3] = tex;
    }

    public void setLeft(String tex){
        textures[4] = tex;
    }

    public void setBottom(String tex){
        textures[5] = tex;
    }
    public String getTop(){
        return textures[0];
    }

    public String getFront(){
        return textures[1];
    }

    public String getRight(){
        return textures[2];
    }

    public String getBack(){
        return textures[3];
    }

    public String getLeft(){
        return textures[4];
    }

    public String getBottom(){
        return textures[5];
    }

    public String[] getTextures() {
        return Arrays.copyOf(textures, 6);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CubeTextureSet that = (CubeTextureSet) o;

        if (!Arrays.equals(textures, that.textures)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(textures);
    }

    @Override
    public String toString() {
        return "CubeTextureSet{" +
                "textures=" + Arrays.toString(textures) +
                '}';
    }
}
