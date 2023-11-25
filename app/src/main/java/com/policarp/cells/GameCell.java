package com.policarp.cells;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;

public class GameCell {
    public static HashMap<CellStates, Integer> ImageByState;
    static {
        ImageByState  = new HashMap<CellStates, Integer>();
        ImageByState.put(CellStates.Disguised, R.drawable.disguised);
        ImageByState.put(CellStates.Flagged, R.drawable.flag);
        ImageByState.put(CellStates.Unflagged, R.drawable.disguised);
        ImageByState.put(CellStates.Explode, R.drawable.explotion);
    }

    public static Integer[] BombsCntImage = new Integer[]
            {
                    R.drawable.empty,
                    R.drawable.c1,
                    R.drawable.c2,
                    R.drawable.c3,
                    R.drawable.c4,
                    R.drawable.c5,
                    R.drawable.c6,
                    R.drawable.c7,
                    R.drawable.c8,
            };
    public ImageButton CellButton;
    public enum CellStates{
        Disguised, Revealed ,Flagged, Unflagged, Explode
    }
    private int _surroundedBombsCnt;
    public void SetSurroundedBombsCnt(int cnt) throws Exception {
        if(cnt < 0 || cnt > 8)
            throw new Exception("Bombs count can be only between 0 and 8");
        _surroundedBombsCnt = cnt;
    }
    public CellStates CurrentState = CellStates.Disguised;
    public boolean ContainsBomb;
    public interface BombStateChanged{
        public void onStateChanged(CellStates state);
    }
    public BombStateChanged OnBombStateChanged;
    public GameCell(ImageButton button, boolean containsBomb, BombStateChanged onChanged){
        CellButton = button;
        OnBombStateChanged = onChanged;
        ContainsBomb = containsBomb;
        changeState(CellStates.Disguised);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reveal();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                flag();
                return true;
            }
        });
    }
    private void flag(){
        if(CurrentState == CellStates.Flagged)
            changeState(CellStates.Unflagged);
       else if(CurrentState == CellStates.Disguised)
           changeState(CellStates.Flagged);
    }
    public void reveal(){
        if(ContainsBomb)
            changeState(CellStates.Explode);
        else changeState(CellStates.Revealed);
    }
    private void changeState(CellStates state){
        CurrentState = state;

        if(state == CellStates.Revealed)
            CellButton.setImageResource(BombsCntImage[_surroundedBombsCnt]);
        else if(ImageByState.containsKey(state))
            CellButton.setImageResource(ImageByState.get(state));

        if(OnBombStateChanged != null)
            OnBombStateChanged.onStateChanged(state);
    }

}
