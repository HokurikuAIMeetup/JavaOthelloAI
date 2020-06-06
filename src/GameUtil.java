import java.util.ArrayList;
import java.util.List;

public class GameUtil {
    public static enum Discs{
        BLANK,
        BLACK,
        WHITE,
        WALL
    }

    public static enum GameHistory{
        BLACK,
        WHITE,
        PASS
    }

    public static final int COLS = 8;
    public static final int ROWS = 8;
    private Discs board[][] = new Discs[ROWS][COLS];
    private Discs turn;
    public static int countTurn = 0;
    public static ArrayList<Object[]> gameHistory = new ArrayList<>();

    //Object[] o = new Object[4];
    public GameUtil(){
        InitBoard();
        System.out.println("ボード初期化完了");
        PrintBoard(board);
    }

    public  void InitBoard(){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = Discs.BLANK;
            }
        }

        //初期の配置をセット
        board[ROWS/2 -1][COLS/2 -1]=Discs.WHITE;
        board[ROWS/2 -1][COLS/2]=Discs.BLACK;
        board[ROWS/2][COLS/2 -1]=Discs.BLACK;
        board[ROWS/2][COLS/2]=Discs.WHITE;

        turn = Discs.BLACK;
    }

    /*
    public void InitGameHistory(){
        for(int i = 0; i)
    }
    */

    public Discs[][] GetBoard(){
        return this.board;
    }
    public Discs GetTurn(){
        return this.turn;
    }
    public boolean JudgeBeginningGameOrNot(){
        if(this.countTurn < 20){
            return true;
        }
        else{
            return false;
        }
    }

    public void SetTurn(Discs player){
        this.turn = player;
    }

    public static Discs ChangePlayer(Discs pl){
        if(pl==Discs.BLACK){
            pl = Discs.WHITE;
        }
        else{
            pl = Discs.BLACK;
        }
        return pl;
    }
    public void ChangeTurn(){
        if(turn==Discs.BLACK){
            turn = Discs.WHITE;
        }
        else{
            turn = Discs.BLACK;
        }
        System.out.println("Turn Changed");
    }


    public Discs[][] getCells(){
        return board;
    }

    public void Move(int r, int c, Discs turn) throws Exception{
        //Cell cell = board[r][c];
        if (board[r][c] != Discs.BLANK){
            throw new Exception("Cell is not empty.");
        }

        board[r][c]=turn;
    }

    public Discs[][] put(Discs[][] board,Discs color,int x,int y){
        reverse(board,color,x,y,true);
        board[x][y] = color;
        return board;
    }

    public boolean put(Discs color,int x, int y){
        //すでに駒があるところには置けない
        if(board[x][y] != Discs.BLANK){
            return false;
        }
        //リバースできないところには置けない
        if(reverse(color,x,y,true)==false){
            return false;
        }

        //駒を置く
        System.out.println("put:color:"+color);
        board[x][y] = color;

        return true;
    }
    //boardState(このクラス)のboardを実際に着手して変更する。
    public boolean reverse(Discs color,int x,int y, boolean doReverse ){
        int dir[][] = {
                {-1,-1}, {0,-1}, {1,-1},
                {-1, 0},         {1, 0},
                {-1, 1}, {0, 1}, {1, 1}
        };

        boolean reversed = false;

        for(int i=0; i<8; i++){
            //隣のマス
            int x0 = x+dir[i][0];
            int y0 = y+dir[i][1];
            if(isOut(x0,y0) == true){
                continue;
            }
            Discs nextState =board[x0][y0];
            if(nextState == turn){
                //System.out.println("Next state is player: " +x0 +","+ y0);
                continue;
            }else if(nextState == Discs.BLANK){
                //System.out.println("Next state is null: " +x0 +","+ y0);
                continue;
            }else{
                //System.out.println("Next state is enemy: " +x0 +","+ y0);
            }

            //隣の隣から端まで走査して、自分の色があればリバース
            int j = 2;
            while(true){

                int x1 = x + (dir[i][0]*j);
                int y1 = y + (dir[i][1]*j);
                if(isOut(x1,y1) == true){
                    break;
                }

                //自分の駒があったら、リバース
                if(board[x1][y1]==color){
                    //System.out.println("Player cell!: " +x1 +","+ y1);

                    if(doReverse){
                        for(int k=1; k<j; k++){
                            int x2 = x + (dir[i][0]*k);
                            int y2 = y + (dir[i][1]*k);
                            board[x2][y2] = ChangePlayer(board[x2][y2]);
                            //System.out.println("reverse: " +x2 +","+ y2);
                        }
                    }
                    reversed = true;
                    break;
                }

                //空白があったら、終了
                if(board[x1][y1]==Discs.BLANK){
                    break;
                }

                j++;

            }

        }

        return reversed;
    }


    public boolean reverse(Discs board[][],Discs color,int x,int y, boolean doReverse ){
        int dir[][] = {
                {-1,-1}, {0,-1}, {1,-1},
                {-1, 0},         {1, 0},
                {-1, 1}, {0, 1}, {1, 1}
        };

        boolean reversed = false;

        for(int i=0; i<8; i++){
            //隣のマス
            int x0 = x+dir[i][0];
            int y0 = y+dir[i][1];
            if(isOut(x0,y0) == true){
                continue;
            }
            Discs nextState =board[x0][y0];
            //隣のマスが相手の色かどうか
            if(nextState == color){
                //System.out.println("Next state is player: " +x0 +","+ y0);
                continue;
            }else if(nextState == Discs.BLANK){
                //System.out.println("Next state is null: " +x0 +","+ y0);
                continue;
            }

            //隣の隣から端まで走査して、自分の色があればリバース
            int j = 2;
            while(true){

                int x1 = x + (dir[i][0]*j);
                int y1 = y + (dir[i][1]*j);
                if(isOut(x1,y1) == true){
                    break;
                }

                //自分の駒があったら、リバース
                if(board[x1][y1]==color){
                    //System.out.println("Player cell!: " +x1 +","+ y1);

                    if(doReverse){
                        for(int k=1; k<j; k++){
                            int x2 = x + (dir[i][0]*k);
                            int y2 = y + (dir[i][1]*k);
                            board[x2][y2] = ChangePlayer(board[x2][y2]);
                            //System.out.println("reverse: " +x2 +","+ y2);
                        }
                    }
                    reversed = true;
                    break;
                }

                //空白があったら、終了
                if(board[x1][y1]==Discs.BLANK){
                    break;
                }

                j++;

            }

        }

        return reversed;
    }

    public boolean canReverse(Discs[][] board,Discs color,int x, int y){
        return reverse(board,color,x, y, false);
    }

    public static boolean isOut(int x, int y){
        if(x<0 || y<0 || x>=8 || y>=8){
            return true;
        }
        return false;
    }
    public boolean existMove(Discs[][] board,Discs color){
        for(int y=0; y<8; y++){
            for(int x=0; x<8; x++){
                //すでに駒があるところはチェックしない
                if(board[x][y] != Discs.BLANK){
                    continue;
                }

                //リバースできる（した）とき、元に戻してfalseを返す
                if(canReverse(board,color,x,y) == true){
                    return true;
                }

            }
        }
        return false;
    }
    public boolean checkPass(Discs color){
        ChangeTurn();
        //コピーデータの全升目に対して、リバースできるかチェック
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                //すでに駒があるところはチェックしない
                if(board[i][j] != Discs.BLANK){
                    continue;
                }

                //リバースできる（した）とき、元に戻してfalseを返す
                if(canReverse(this.board,this.turn,i,j) == true){
                    ChangeTurn();
                    return false;
                }
            }
        }
        ChangeTurn();//リバースできなくてもターンの情報を元に戻す
        return true;
    }

    public static void PrintBoard(Discs[][] board){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(board[j][i]==Discs.BLACK){
                    System.out.printf("x");
                }
                else if(board[j][i]==Discs.WHITE){
                    System.out.printf("o");
                }
                else{
                    System.out.printf("+");
                }
            }
            System.out.println();
        }
    }

    public static int countDisc(int player,int[][] board){

        int playerDisc = 0;
        int oppDisc = 0;

        for(int y=0; y<8; y++){
            for(int x=0; x<8; x++){
                if(board[x][y] == player){
                    playerDisc++;
                }else if(board[x][y] == 3-player){
                    oppDisc++;
                }
            }
        }

        return playerDisc - oppDisc;
    }

    public  boolean checkEnd(){
        Discs nextPlayer;
        nextPlayer=ChangePlayer(this.turn);

        //コピーデータの全升目に対して、リバースできるかチェック
        if(existMove(this.board,nextPlayer)==false){
            nextPlayer=ChangePlayer(nextPlayer);
            if(existMove(this.board,nextPlayer)==false){
                return true;//白番でも黒番でも打つ場所が無いのでゲーム終了と判定
            }
            else{
                return false;//色を変えたら打てた、つまりパスは発生するがゲーム終了では無い
            }
        }
        else {
            return false;//一つ目のif文で打つ場所があると判定された、つまりゲーム継続
        }
    }


    public boolean CheckEnd(Discs[][] afterBoard, Discs color) {
        if(existMove(afterBoard,color)==false){
            color=ChangePlayer(color);
            if(existMove(afterBoard,color)==false){
                return true;
            }
            else{
                return false;
            }
        }

        return false;
    }

    public void UpdateGameHistory(int x, int y, GameHistory colorOrPass){
        Object[] logOfThisTurn = {x,y,colorOrPass};
        this.gameHistory.add(logOfThisTurn);
    }

    public void UpdateGameHistory(GameHistory colorOrPass){
        Object[] logOfThisTurn = {null,null,colorOrPass};
        this.gameHistory.add(logOfThisTurn);
    }

    public void WriteGameHistoryToSGF(){
        System.out.println("aaa");
        String historyOneGame = "";
        for (int counter = 0; counter < this.gameHistory.size(); counter++) {
            if(this.gameHistory.get(counter)[2] != GameHistory.PASS) {
                historyOneGame = historyOneGame + this.gameHistory.get(counter)[0].toString();
            }
        }

        System.out.println(historyOneGame);

    }
}