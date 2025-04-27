package myhan.clonemantle.guess;

public record GetGuessesItem(
        Long id,
        String word,
        double similarity
) {
}
