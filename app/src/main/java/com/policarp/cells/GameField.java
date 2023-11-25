package com.policarp.cells;

import android.widget.ImageButton;

public class GameField {
    static String TAG = "GAMEFIELD";
    private final int MINMINESPERC = 10;
    private final int MAXMINESPERC = 40;
    public Size FieldSize;
    public Score Flags;
    public GameCell[][] Cells;
    private int _bombsCnt;

    public interface ChangedStateEvent{
        public void onChangedState();
    }
    private ChangedStateEvent onChangedStateEvent;
    public interface LoseEvent {
        void onLose();
    }
    private LoseEvent loseEvent;
    public interface WinEvent {
        public void onWin();
    }
    private WinEvent winEvent;

    public GameField(Size size, ImageButton[][] buttons, ChangedStateEvent onChangedState, LoseEvent loseEvent, WinEvent winEvent) throws Exception {
        FieldSize = size;
        this.winEvent = winEvent;
        Cells = generateField(size, buttons);
        Flags = new Score(_bombsCnt, _bombsCnt);
        onChangedStateEvent = onChangedState;
        this.loseEvent = loseEvent;
    }

    private GameCell[][] generateField(Size size, ImageButton[][] buttons) throws Exception {
        int n = size.Height;
        int m = size.Width;
        boolean[][] mines = generateMines(size);
        GameCell[][] field = new GameCell[n][m];
        int revealed = 2;
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < m; ++j){
                //buttons[i][j].setEnabled(false);
                field[i][j] = new GameCell(buttons[i][j], mines[i][j], this::onChangedState);
                int cnt = 0;
                for(int di = -1; di <= 1; ++di){
                    for(int dj = -1; dj <= 1; ++dj){
                        if(i + di >= 0 && i + di < n && j + dj >= 0 && j + dj < m && di * di + dj * dj != 0)
                            if(mines[i + di][j + dj])
                                ++cnt;
                    }
                }
                field[i][j].SetSurroundedBombsCnt(cnt);
                if(revealed > 0 && !field[i][j].ContainsBomb){
                    revealed--;
                    field[i][j].reveal();
                }
            }
        }
        return field;
    }

    public void onChangedState(GameCell.CellStates state) {
        if(state == GameCell.CellStates.Flagged){
            Flags.CurrentScore--;
            if(Flags.CurrentScore == 0 && checkVictory() && winEvent != null){
                winEvent.onWin();
                return;
            }
        }
        else if(state == GameCell.CellStates.Unflagged)
            Flags.CurrentScore++;
        else if(state == GameCell.CellStates.Explode){
            onLose();
            return;
        }
        if(onChangedStateEvent != null)
            onChangedStateEvent.onChangedState();
    }

    private boolean checkVictory() {
        int n = FieldSize.Height;
        int m = FieldSize.Width;
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < m; ++j)
                if(Cells[i][j].ContainsBomb && Cells[i][j].CurrentState != GameCell.CellStates.Flagged)
                    return false;
        }
        return true;
    }

    private void onLose() {
        Flags.CurrentScore = 0;
        disableButtons();
        if(loseEvent != null)
            loseEvent.onLose();
    }

    private void disableButtons() {
        int n = FieldSize.Height;
        int m = FieldSize.Width;
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < m; ++j)
                Cells[i][j].CellButton.setEnabled(false);
        }
    }

    private boolean[][] generateMines(Size size) {
        final int maxTries = 1000000;
        int cellsCnt = size.Height * size.Width;
        _bombsCnt = RandomHandler.getRandomIntInBounds( cellsCnt * MINMINESPERC / 100, cellsCnt * MAXMINESPERC / 100);
        int cop = _bombsCnt;
        boolean[][] map = new boolean[size.Height][size.Width];
        int tries = maxTries;
        while(cop > 0 && tries > 0){
            int i = RandomHandler.getRandomIntInBounds(0, size.Height);
            int j = RandomHandler.getRandomIntInBounds(0, size.Width);
            if(!map[i][j]){
                map[i][j] = true;
                cop--;
            }
            else --tries;
        }
        return map;
    }

}
