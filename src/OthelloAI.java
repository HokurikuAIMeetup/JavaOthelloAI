import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by tomoueno on 2016/07/29.
 */



public class OthelloAI {
    public GameUtil.Discs aiPlayer;
    public GameUtil.Discs enemyDiscColor;
    public String aiName;
    private static final int LIMIT = 5;
    private static final int EVALLIMIT = 10;
    private int countNumOfTurn = 0;
    private int boardSize = 8;

    public static int[][] evalArray;
    public static boolean[][] evalArrayBool;
    public GameUtil gameUtilState;

    public OthelloAI(int color,String aiName){
        //this.color = color;
        if(color == 1){
            aiPlayer = GameUtil.Discs.WHITE;
            enemyDiscColor = GameUtil.Discs.BLACK;
        }
        else{
            aiPlayer = GameUtil.Discs.BLACK;
            enemyDiscColor = GameUtil.Discs.WHITE;
        }

        this.aiName = aiName;

        evalArray=new int[8][8];
        evalArrayBool=new boolean[8][8];
        gameUtilState = new GameUtil();
    }

    public void GetCountTurnFromGameUtil(){
        this.countNumOfTurn = GameUtil.countTurn;
    }

    public int[] DecideMove(GameUtil.Discs[][] board,boolean RANDOM_FLAG){
        GetCountTurnFromGameUtil();//何手目かを取得し、思考ルーチンで使う

        int[] place={-1,-1};
        //全部の手の評価値を保存してソートする。
        ArrayList<int[]> moveAndEvalArrayList = new ArrayList<>();

        GameUtil.Discs[][] copyBoard = new GameUtil.Discs[8][8];


        for (int i=0; i < copyBoard.length; i++) {
            copyBoard[i] = new GameUtil.Discs[copyBoard[i].length];
            System.arraycopy(board[i], 0, copyBoard[i], 0, board.length);
        }

        System.out.println(Arrays.deepToString(copyBoard));

        int scoreMax = -999999999,score;
        int alpha=-999999999,beta=999999999;

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                //すでに駒があるときはパス
                if(board[i][j] != GameUtil.Discs.BLANK)
                    continue;
                if(gameUtilState.canReverse(copyBoard,aiPlayer,i, j) == true){
                    if(countNumOfTurn<58) {
                        //序盤・中盤においてAIが思考を行う

                        board = gameUtilState.put(board, aiPlayer, i, j);

                        GameUtil.PrintBoard(board);
                        System.out.println("Thinking by OthelloAI");

                        score = DecideRecursionMove(board, gameUtilState.GetTurn(), LIMIT, alpha, beta);

                        if (score > scoreMax) {
                            place[0] = i;
                            place[1] = j;
                            scoreMax = score;
                        }

                        int[] moveAndEval = {score,place[0],place[1]};
                        moveAndEvalArrayList.add(moveAndEval);

                        for (int k = 0; k < copyBoard.length; k++) {
                            board[k] = new GameUtil.Discs[board[k].length];
                            System.arraycopy(copyBoard[k], 0, board[k], 0, copyBoard[k].length);
                        }
                    }
                    else {
                        //終盤において、AIが思考を行う

                        board = gameUtilState.put(board, aiPlayer, i, j);

                        GameUtil.PrintBoard(board);
                        System.out.println("[終盤読み] Thinking by OthelloAI");
                        score = DecideRecursionEndMove(board, gameUtilState.GetTurn(), 60-countNumOfTurn, alpha, beta);


                        if (score > scoreMax) {
                            place[0] = i;
                            place[1] = j;
                            scoreMax = score;
                        }

                        for (int k = 0; k < copyBoard.length; k++) {
                            board[k] = new GameUtil.Discs[board[k].length];
                            System.arraycopy(copyBoard[k], 0, board[k], 0, copyBoard[k].length);

                        }
                    }
                }
            }
        }

        if(RANDOM_FLAG) {
            //System.out.println(Arrays.deepToString(moveAndEvalArrayList.toArray()));
            int[][] sortedMoveByEval = SortMoveByEval(moveAndEvalArrayList);
            int[] RandomMoveChoise = ChooseRandomMove(sortedMoveByEval);
            int[] randomPlace = {RandomMoveChoise[1], RandomMoveChoise[2]};//x, y座標は添字1,2に格納されている
            return randomPlace;
        }
        else{
            return place;
        }
    }

    public int[][] SortMoveByEval(ArrayList<int []> moveAndEvalArrayList){
        int[][] moveAndEvalList = new int[moveAndEvalArrayList.size()][];//(int[][]) moveAndEvalArrayList.toArray();
        moveAndEvalList = moveAndEvalArrayList.toArray(moveAndEvalList);
        Arrays.sort(moveAndEvalList, Comparator.comparingInt(a -> a[0]));

        System.out.println(Arrays.deepToString(moveAndEvalList));

        return moveAndEvalList;
    }

    public int[] ChooseRandomMove(int[][] moveAndEvalList){
        int lengthArray = moveAndEvalList.length;

        //打てる場所の評価の上位5つまででランダムに選択するか、打てる場所が4つ以下ならその中でランダムに選択
        int top5Index;
        if (lengthArray >= 5){top5Index = 5;}
        else{top5Index = lengthArray;}

        int indexMove = (int)(Math.random()*top5Index);

        return moveAndEvalList[indexMove];
    }

    public int[] EvalMove(GameUtil.Discs[][] board, GameUtil.Discs turn) {
        //aiPlayer=AltulaRiversi.reversiObj.boardState.ChangePlayer(turn);
        aiPlayer=turn;
        int[] place = {-1, -1};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                evalArray[i][j] = 0;
                evalArrayBool[i][j]=false;
            }
        }
        GameUtil.Discs[][] copyBoard = new GameUtil.Discs[8][8];


        for (int i = 0; i < copyBoard.length; i++) {
            copyBoard[i] = new GameUtil.Discs[copyBoard[i].length];
            System.arraycopy(board[i], 0, copyBoard[i], 0, board.length);
        }

        int scoreMax = -100000, score;
        int alpha = -999999999, beta = 999999999;

        GameUtil.PrintBoard(board);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //すでに駒があるときはパス
                if (board[i][j] != GameUtil.Discs.BLANK)
                    continue;
                if (gameUtilState.canReverse(copyBoard, aiPlayer, i, j) == true) {
                    if (countNumOfTurn < 57) {
                        //Board.PrintBoard(board);

                        board = gameUtilState.put(board, aiPlayer, i, j);

                        GameUtil.PrintBoard(board);
                        System.out.println("Thinking by Othellogic");
                        score = DecideRecursionMove(board, gameUtilState.GetTurn(), EVALLIMIT, alpha, beta);
                        evalArray[i][j] = score;
                        evalArrayBool[i][j] = true;
                        System.out.println("There is a move by Othellogic");

                        if (score > scoreMax) {
                            place[0] = i;
                            place[1] = j;
                            scoreMax = score;
                        }

                        for (int k = 0; k < copyBoard.length; k++) {
                            board[k] = new GameUtil.Discs[board[k].length];
                            System.arraycopy(copyBoard[k], 0, board[k], 0, copyBoard[k].length);
                        }
                    } else {
                        //Board.PrintBoard(board);
                        board = gameUtilState.put(board, aiPlayer, i, j);
                        score = DecideRecursionEndMove(board, gameUtilState.GetTurn(), 60 - countNumOfTurn, alpha, beta);

                        evalArray[i][j] = score;
                        evalArrayBool[i][j]=true;
                        if (score > scoreMax) {
                            place[0] = i;
                            place[1] = j;
                            scoreMax = score;
                        }

                        for (int k = 0; k < copyBoard.length; k++) {
                            board[k] = new GameUtil.Discs[board[k].length];
                            System.arraycopy(copyBoard[k], 0, board[k], 0, copyBoard[k].length);

                        }
                    }
                }
            }
        }

        return place;
    }

    int eval_mid(GameUtil.Discs[][] board, GameUtil.Discs player){
        int eval_score = 437;

        GameUtil.Discs[][] edge=new GameUtil.Discs[2][8];

        //TODO: （作ってみよう）全滅の場合は評価を大きくマイナスにする。
        //TODO: （作ってみよう）X打ちは評価を大きくマイナスにする。

        for (int i = 0; i < 2; i++){//上辺のコピー
            for (int j = 0; j < 8; j++){
                edge[i][j] = board[i][j];
            }
        }
        eval_score = eval_score + edge_eval(edge);

        for (int i = 0; i < 8; i++){//下辺のコピー
            for (int j = 0; j < 2; j++){
                edge[j][i] = board[boardSize - i - 1][j];
            }
        }
        eval_score = eval_score + edge_eval(edge);

        for (int i = 0; i < 8; i++){//左辺のコピー
            for (int j = 0; j < 2; j++){
                edge[i][j] = board[j][i];
            }
        }
        eval_score = eval_score + edge_eval(edge);

        for (int i = 0; i < 8; i++){//右辺のコピー
            for (int j = 0; j < 2; j++){
                edge[i - 1][j - 1] = board[8 - j - 1][8 - i - 1];
            }
        }
        eval_score = eval_score + edge_eval(edge);

        return eval_score;
    }

    int edge_eval(GameUtil.Discs[][] edge){
        //edgeは盤面の辺に近い２列分を取り出したもの。四辺あるので一回の評価で４回呼び出される。



        return ev;
    }

    void convert_char_to_pattern(){
        // 数字:評価値　o:自分の石 x:相手の石 .:空白 *:なんでも良い
        
        String edgePattern1 ="+30_"
                +".oooooo."
                +"..oooo..";

        String edgePattern2 ="-100_"
                +"..ooooo."
                +"..oooo..";

        String edgePattern3 ="-100_"
                +".ooooo.."
                +"..oooo..";


    }


    int count_disc(GameUtil.Discs[][] board, GameUtil.Discs selfPlayer){
        //TODO: boardは添字0から開始
        int ai = 0, hu = 0;
        int i, j;
        GameUtil.Discs human = GameUtil.ChangePlayer(selfPlayer);
        for (i = 0; i < 8; i++){
            for (j = 0; j < 8; j++){
                if (board[i][j] == selfPlayer){
                    ai++;
                }
                if (board[i][j] == human){
                    hu++;
                }
            }
        }
        return ai - hu;
    }

    int DecideRecursionMove(GameUtil.Discs[][] board, GameUtil.Discs recursionTurn, int limit, int alpha, int beta){

        int  score, score_max = -999999, score_min = 999999;
        GameUtil.Discs[][] copyBoard=new GameUtil.Discs[8][80];

        for (int i=0; i < copyBoard.length; i++) {
            copyBoard[i] = new GameUtil.Discs[copyBoard[i].length];
            System.arraycopy(board[i], 0, copyBoard[i], 0, board[i].length);
        }

        boolean exist;

        recursionTurn= GameUtil.ChangePlayer(recursionTurn);

        if (limit <= 0 || (gameUtilState.checkEnd() == true)){
            //Board.PrintBoard(board);
            return eval_mid(board,recursionTurn);
            //return count_disc(board,aiPlayer);

        }

        exist = false;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (gameUtilState.canReverse(board,recursionTurn,i, j) == true){//is_legal_move(board, player, i, j)){
                    //set_and_turn_over(board, player, i, j);
                    board = gameUtilState.put(board,aiPlayer,i,j);
                    score = DecideRecursionMove(board, recursionTurn, limit - 1, alpha, beta);
                    //System.out.printf("score:%d i;%d j:%d", score,i,j);

                    for (int k=0; k < copyBoard.length; k++) {
                        board[k] = new GameUtil.Discs[copyBoard[k].length];
                        System.arraycopy(copyBoard[k], 0, board[k], 0, copyBoard[k].length);
                    }

                    if (recursionTurn == aiPlayer){
                        if (score>score_max){//score_max が　alpha
                            score_max = score;
                            alpha = score;
                            if (alpha >= beta){
                                return beta;
                            }
                        }
                    }
                    else{
                        if (score<score_min){
                            score_min = score;
                            beta = score;
                            if (beta <= alpha){
                                return alpha;
                            }
                        }
                    }

                    exist = true;
                }
            }
        }

        if (exist == false){
            return DecideRecursionMove(board, recursionTurn, limit-1, -beta, -alpha);
        }

        if (recursionTurn == aiPlayer){
            return score_max;
        }
        else{
            return score_min;
        }
    }

    int DecideRecursionEndMove(GameUtil.Discs[][] board, GameUtil.Discs recursionTurn, int limit, int alpha, int beta){
        int  score, score_max = -99999999, score_min = 99999999;
        GameUtil.Discs[][] copyBoard=new GameUtil.Discs[8][80];

        for (int i=0; i < copyBoard.length; i++) {
            copyBoard[i] = new GameUtil.Discs[copyBoard[i].length];
            System.arraycopy(board[i], 0, copyBoard[i], 0, board[i].length);
        }

        boolean exist;

        recursionTurn= GameUtil.ChangePlayer(recursionTurn);

        if (limit <= 0 || (gameUtilState.checkEnd() == true)){
            //Board.PrintBoard(board);
            return count_disc(board,recursionTurn);
            //return count_disc(board,aiPlayer);

        }

        exist = false;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (gameUtilState.canReverse(board,recursionTurn,i, j) == true){//is_legal_move(board, player, i, j)){
                    //set_and_turn_over(board, player, i, j);
                    board = gameUtilState.put(board,aiPlayer,i,j);
                    score = DecideRecursionEndMove(board, recursionTurn, limit - 1, alpha, beta);
                    //System.out.printf("score:%d i;%d j:%d", score,i,j);

                    for (int k=0; k < copyBoard.length; k++) {
                        board[k] = new GameUtil.Discs[copyBoard[k].length];
                        System.arraycopy(copyBoard[k], 0, board[k], 0, copyBoard[k].length);
                    }

                    if (recursionTurn == aiPlayer){
                        if (score>score_max){//score_max が　alpha
                            score_max = score;
                            alpha = score;
                            if (alpha >= beta){
                                return beta;
                            }
                        }
                    }
                    else{
                        if (score<score_min){
                            score_min = score;
                            beta = score;
                            if (beta <= alpha){
                                return alpha;
                            }
                        }
                    }

                    exist = true;
                }
            }
        }

        if (exist == false){
            return DecideRecursionEndMove(board, recursionTurn, limit-1, -beta, -alpha);
        }

        if (recursionTurn == aiPlayer){
            return score_max;
        }
        else{
            return score_min;
        }
    }
}