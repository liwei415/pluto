package xyz.inux.pluto.model.pojo.vo.sample;

import javax.validation.constraints.Size;

public class PostInVo {

    @Size(min = 1, max = 8)
    private String words;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
