package com.policarp.cells;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int WIDTH = 6;
    final int HEIGHT = 10;
    public GameField MineField;
    public TextView Score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Score = findViewById(R.id.Score);
        try {
            generate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generate() throws Exception {
        GridLayout layout = findViewById(R.id.Grid);
        layout.removeAllViews();
        layout.setRowCount(HEIGHT);
        layout.setColumnCount(WIDTH);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ImageButton[][] buttons = new ImageButton[HEIGHT][WIDTH];
        for(int i = 0; i < HEIGHT; ++i){
            for(int j = 0; j < WIDTH; ++j){
                buttons[i][j] = (ImageButton) inflater.inflate(R.layout.cell, layout, false);

            }
        }
        MineField = new GameField(new Size(HEIGHT, WIDTH), buttons,
                () -> Score.setText(MineField.Flags.toString()),
                () -> {
                    Toast.makeText(MainActivity.this, "Вы проиграли!", Toast.LENGTH_LONG);
                    Score.setText("Проиграл ;(");
                },
                () -> {
                    Toast.makeText(MainActivity.this, "Вы ВЫИГРАЛИ!!!11!!!!", Toast.LENGTH_LONG);
                    Score.setText("ВЫИГРАЛ !;0");
                });
        Score.setText(MineField.Flags.toString());
        for(int i = 0; i < HEIGHT; ++i){
            for(int j = 0; j < WIDTH; ++j){
                layout.addView(MineField.Cells[i][j].CellButton);
            }
        }

    }
}