package com.example;

import java.util.concurrent.ThreadLocalRandom;

public class JavaJokes {

    private String[] javaJokes = {
            "Knock Knock 1",
            "Knock Knock 2",
            "Knock Knock 3",
            "Knock Knock 4",
            "Knock Knock 5",
            "Knock Knock 6",
            "Knock Knock 7",
            "Knock Knock 8",
            "Knock Knock 9",
            "Knock Knock 10"
    };

    public String getRandomJoke() {
        int randomInt = ThreadLocalRandom.current().nextInt(0, javaJokes.length);
        String ret = "";
        if (randomInt < javaJokes.length) {
            ret = javaJokes[randomInt];
        }

        return ret;
    }
}
