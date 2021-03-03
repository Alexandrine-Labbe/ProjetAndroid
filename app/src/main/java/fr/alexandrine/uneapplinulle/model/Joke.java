package fr.alexandrine.uneapplinulle.model;

public class Joke {
    private final int id;
    private final String category;
    private final String joke;

    public Joke(int id, String category, String joke) {
        this.id = id;
        this.category = category;
        this.joke = joke;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getJoke() {
        return joke;
    }

}
