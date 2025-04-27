package myhan.clonemantle.guess;

public record PostGuessResponse(
        Long id,
        String word,
        double similarity
) {
    public static PostGuessResponse of(Guess guess) {
        return new PostGuessResponse(guess.getId(), guess.getWord(), guess.getSimilarity());
    }
}
