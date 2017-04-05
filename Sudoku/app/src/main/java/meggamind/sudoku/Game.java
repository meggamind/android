package meggamind.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by aniket on 3/30/17.
 */

public class Game extends Activity{
    private static final String TAG = "Sudoku";
    public static final String KEY_DIFFICULTY = "meggamind.sudoku.difficulty";
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;

    // To continue game
    private static final String PREF_PUZZLE = "puzzle";
    protected static final int DIFFICULTY_CONTINUE = -1;

    private final String easyPuzzle =
            "360000000004230800000004200" +
                    "070460003820000014500013020" +
                    "001900000007048300000000045" ;
    private final String mediumPuzzle =
            "650000070000506000014000005" +
                    "007009000002314700000700800" +
                    "500000630000201000030000097" ;
    private final String hardPuzzle =
            "009000000080605020501078000" +
                    "000000700706040102004000000" +
                    "000720903090301080000000600" ;

    private int puzzle[] = new int[9*9];


    private PuzzleView puzzleView;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Log.d(TAG,"onCreate");
        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        puzzle = getPuzzle(diff);
        calculateUsedTiles();

        puzzleView = new PuzzleView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();
        // if the activity is restarted, do a continue next time
        getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
    }


    // To save the state of the application
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
        // save the current puzzle
        getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE, toPuzzleString(puzzle)).commit();
    }

    private int[] getPuzzle(int diff){
        String puz;
        switch(diff){
            case DIFFICULTY_CONTINUE:
                puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE, easyPuzzle);
                break;
            case DIFFICULTY_HARD:
                puz = hardPuzzle;
                break;
            case DIFFICULTY_MEDIUM:
                puz = mediumPuzzle;
                break;
            case DIFFICULTY_EASY: default:
                puz = easyPuzzle;
                break;
        }
        return fromPuzzleString(puz);
    }

    // Converts a puzzle from an array of integers to a string
    static protected String toPuzzleString(int[] puz){
        StringBuilder buf = new StringBuilder();
        for(int element:puz){
            buf.append(element);
        }
        return buf.toString();
    }

    // returns the number currently occcupying that tile.
    private int getTile(int x, int y){
        return puzzle[y*9+x];
    }

    private void setTile(int x, int y, int value){
        puzzle[y*9+x] = value;
    }

    protected String getTileString(int x, int y){
        int v = getTile(x,y);
        if(v==0)
            return "";
        else
            return String.valueOf(v);
    }

    // Converts a puzzle from a string to an array of integers
    static protected int[] fromPuzzleString(String string){
        int[] puz = new int[string.length()];
        for(int i = 0; i < puz.length; i++){
            puz[i] = string.charAt(i) - '0';
        }
        return puz;
    }

    // Calling the keypad here
    protected void showKeypadOrError(int x, int y){
        int tiles[] = getUsedTiles(x, y);
        if (tiles.length == 9){
            Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else{
            Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
            Dialog v = new Keypad(this, tiles, puzzleView);
            v.show();
        }
    }

    // Given the x,y position and the value of a tile, it changes the tile only if the value
    // provided is valid
    protected boolean setTileIfValid(int x, int y, int value){
        int tiles[] = getUsedTiles(x, y);
        if (value!=0){
            for(int tile:tiles){
                if(tile == value)
                    return false;
            }
        }
        setTile(x, y, value);
        calculateUsedTiles();
        return true;
    }

    private final int used[][][] = new int[9][9][];

    protected int[] getUsedTiles(int x, int y){
        return used[x][y];
    }

    // array of used tiles is somewhat expensive so we cache the array
    // recalculating only when necessary by calling calculateUsedTiles()

    private void calculateUsedTiles(){
        for(int x=0; x<9; x++){
            for(int y=0; y<9; y++){
                used[x][y] = calculateUsedTiles(x, y);
            }
        }
    }

    private int[] calculateUsedTiles(int x, int y){
        int c[]  = new int[9];
        // Horizontal
        for(int i=0; i<9; i++){
            if(i==y)
                continue;
            int t = getTile(x,i);
            if(t!=0)
                c[t-1] = t;
        }
        // Vertical
        for(int i=0; i<9; i++){
            if(i==x)
                continue;
            int t = getTile(i,y);
            if(t!=0)
                c[t-1] = t;
        }
        // Same Cell block
        int startX = (x/3)*3;
        int startY= (y/3)*3;
        for(int i = startX; i < startX +3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                if (i == x && j == y)
                    continue;
                int t = getTile(i, j);
                if (t != 0)
                    c[t - 1] = t;
            }
        }
        // compress
        int nused =0;
        for(int t:c){
            if(t!=0)
                nused++;
        }
        int c1[] = new int[nused];
        nused = 0;
        for(int t:c) {
            if (t != 0)
                c1[nused++] = t;
        }
        return c1;
    }
}
