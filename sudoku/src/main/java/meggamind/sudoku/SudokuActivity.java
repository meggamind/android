package meggamind.sudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class SudokuActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Sudoku";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        //set up click listener for all buttons
        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
    }

    public void onClick(View v){
        Log.i(TAG, "v.getId(): " + v.getId());
        switch(v.getId()){
            case R.id.continue_button:
                startGame(Game.DIFFICULTY_CONTINUE);
                break;
            case R.id.about_button:
                Intent aboutIntent = new Intent(this, About.class);
                startActivity(aboutIntent);
                break;
            case R.id.new_button:
                openNewGameDialog();
                break;
            case R.id.exit_button:
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, Prefs.class));
                return true;
        }
        return false;
    }

    private void openNewGameDialog(){
        new AlertDialog.Builder(this).setTitle(R.string.new_game_title).setItems(R.array.difficulty,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startGame(i);
                    }
                }).show();
    }

    public void startGame(int i){
        Log.d(TAG, "Clicked on " + i);
        Intent intent = new Intent(SudokuActivity.this, Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY, i);
        startActivity(intent);
    }
}
