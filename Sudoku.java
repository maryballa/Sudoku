import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Sudoku {
  class Tile extends JButton{
    int r; //row number
    int c; //column number
    Tile(int r, int c){
      this.r = r; 
      this.c = c;
    }
  }

  int boardWidth = 600;
  int boardHeight = 650;

  int solution[][] = new int[9][9];
  int puzzle[][] = new int[9][9];

  JFrame frame = new JFrame("Sudoku");
  JLabel textLabel = new JLabel();
  JPanel textPanel = new JPanel();

  JPanel boardPanel = new JPanel();

  JPanel buttonsPanel = new JPanel();
  JButton numSelected = null;
  int numSelectedInt;

  int errors = 0;
  int numCounter[] = {0,0,0,0,0,0,0,0,0};

  Sudoku(){
    
    sudokuGenerator();
    numberCounter(puzzle);

    frame.setSize(boardWidth, boardHeight);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setLayout(new BorderLayout());

    textLabel.setFont(new Font("Arial", Font.BOLD, 30));
    textLabel.setHorizontalAlignment(JLabel.CENTER);
    textLabel.setText("Sudoku  errors: ");

    textPanel.add(textLabel);
    frame.add(textPanel, BorderLayout.NORTH);

    boardPanel.setLayout(new GridLayout(9,9));
    setupTiles();    
    frame.add(boardPanel, BorderLayout.CENTER);

    buttonsPanel.setLayout(new GridLayout(1,9));
    setupButtons();
    frame.add(buttonsPanel, BorderLayout.SOUTH);

    frame.setVisible(true);
  }

  void setupTiles(){
    for (int r = 0; r < 9; r++){
      for (int c = 0; c < 9; c++){
        Tile tile = new Tile(r, c);
        int tileVal = puzzle[r][c];
        if (tileVal!= 0){
          tile.setFont(new Font("Ariel", Font.BOLD, 20));
          tile.setBackground(Color.lightGray);
          tile.setText(String.valueOf(tileVal));
        }
        else {
          tile.setFont(new Font("Ariel", Font.PLAIN, 20));
          tile.setBackground(Color.white);
        }
        
        if ((r == 2 && c == 2) || (r == 2 && c == 5) || (r == 5 && c == 2) || (r == 5 && c == 5)){
          tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.black));

        }
        else if (r == 2 || r == 5){
          tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.black));
        }
        else if (c == 2 || c == 5){
          tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.black));
        }
        else {
          tile.setBorder(BorderFactory.createLineBorder(Color.black));
        }

        tile.setFocusable(false);
        boardPanel.add(tile);

        tile.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e){
            Tile tile = (Tile) e.getSource();
            int r = tile.r;
            int c = tile.c;

            if (numSelected == null) return;
            if (numSelected != null){
              if(tile.getText() != ""){
                return;
              }
              String numSelectedText = numSelected.getText();
              numSelectedInt = Integer.parseInt(numSelectedText);
              if (solution[r][c] == numSelectedInt){
                tile.setText(numSelectedText);
                if (numCounter[numSelectedInt-1] < 9){
                  numCounter[numSelectedInt-1]++;
                }

                if (numCounter[numSelectedInt - 1] == 9) {
                  numSelected.setBackground(Color.green);
                }
              }
              else{
                errors += 1;
                textLabel.setText("Sudoku  errors: " + String.valueOf(errors));
              }
            }
          }
        });
      }
    }
  }

  void setupButtons(){
    for (int i = 1; i < 10; i++){
      JButton button = new JButton();
      button.setFont(new Font("Arial", Font.BOLD, 20));
      button.setText(String.valueOf(i));
      button.setFocusable(false);
      button.setBackground(Color.white);
      buttonsPanel.add(button);

      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
          JButton button = (JButton) e.getSource();
          if (numSelected != null){
            if (numCounter[numSelectedInt - 1] != 9){
              numSelected.setBackground(Color.white);
            } else{
              numSelected.setBackground(Color.green);
            }
          }
          numSelected = button;
          numSelectedInt = Integer.parseInt(numSelected.getText());
          numSelected.setBackground(Color.lightGray);
          if (numCounter[numSelectedInt - 1] == 9){
            numSelected.setBackground(Color.green);
          }else{
            numSelected.setBackground(Color.lightGray);
          }
        }
      });
    }
  }

  void numberCounter(int array[][]){ //count how many times each number appears
    for (int p = 0; p < 9; p++){
      for (int i = 0; i < 9; i++){
        for (int j = 0; j < 9; j++){  
          if((array[i][j]) == p+1){
            numCounter[p]++;
          }
        }
      }
    }
  }

/********SUDOKU GENERATOR*************/

  static boolean unUsedInBox(int[][] grid, int rowStart, int colStart, int num) {
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 3; j++){
        if (grid[rowStart + i][colStart + j] == num){
          return false;
        }
      }
    }
    return true;
  }

  static void fillBox(int[][] grid, int row, int col) {
    Random rand = new Random();
    int num;
    
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        
        do {
          num = rand.nextInt(9) + 1;
        } while (!unUsedInBox(grid, row, col, num));
        
        grid[row + i][col + j] = num;
      }
    }
  }

  static boolean unUsedInRow(int[][] grid, int i, int num) {
    for (int j = 0; j < 9; j++) {
      if (grid[i][j] == num) {
        return false;
      }
    }
    return true;
  }

  static boolean unUsedInCol(int[][] grid, int j, int num) {
    for (int i = 0; i < 9; i++) {
      if (grid[i][j] == num) {
        return false;
      }
    }
    return true;
  }

  static boolean checkIfSafe(int[][] grid, int i, int j, int num) {
    return (unUsedInRow(grid, i, num) && unUsedInCol(grid, j, num) && unUsedInBox(grid, i - i % 3, j - j % 3, num));
  }

  static void fillDiagonal(int[][] grid) {
    for (int i = 0; i < 9; i = i + 3) {
      fillBox(grid, i, i);
    }
  }

  static boolean fillRemaining(int[][] grid, int i, int j) {
        
    if (i == 9) {
      return true;
    }

    if (j == 9) {
      return fillRemaining(grid, i + 1, 0);
    }

    if (grid[i][j] != 0) {
      return fillRemaining(grid, i, j + 1);
    }

    for (int num = 1; num <= 9; num++) {
      if (checkIfSafe(grid, i, j, num)) {
        grid[i][j] = num;
          if (fillRemaining(grid, i, j + 1)) {
            return true;
          }
          grid[i][j] = 0;
        }
      }
      return false;
  }

  static void removeKDigits(int[][] grid, int k) {
    Random rand = new Random();
    while (k > 0) {
      int cellId = rand.nextInt(81);
      int i = cellId / 9;
      int j = cellId % 9;
      if (grid[i][j] != 0) {
        grid[i][j] = 0;
        k--;
      }
    }
  }

  void sudokuGenerator() {
    int k = 25; //number of empty cells 

    fillDiagonal(solution);
    fillRemaining(solution, 0, 0);

    for(int i = 0; i < 9; i++)
      puzzle[i] = solution[i].clone();    
    removeKDigits(puzzle, k);
  }
  /********SUDOKU GENERATOR*************/
}
