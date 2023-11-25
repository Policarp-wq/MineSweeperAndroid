package com.policarp.cells;

import androidx.annotation.NonNull;

public class Score {
    public int CurrentScore;
    public int MaxScore;

    public Score(int currentScore, int maxScore) {
        CurrentScore = currentScore;
        MaxScore = maxScore;
    }

    @Override
    public String toString() {
        return "" + CurrentScore + "/" + MaxScore;
    }
}
