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
    private static final int LIMIT = 3;
    private static final int EVALLIMIT = 1;
    private int countNumOfTurn = 0;

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

                        score = RecursionMove(board, gameUtilState.GetTurn(), LIMIT, alpha, beta);

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
                        score = RecursionEndMove(board, gameUtilState.GetTurn(), 60-countNumOfTurn, alpha, beta);


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
                        score = RecursionMove(board, gameUtilState.GetTurn(), EVALLIMIT, alpha, beta);
                        evalArray[i][j] = score;
                        evalArrayBool[i][j]=true;
                        System.out.println("There is a move in Othellogic");

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
                        score = RecursionEndMove(board, gameUtilState.GetTurn(), 60 - countNumOfTurn, alpha, beta);

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

    int count_kakutei(GameUtil.Discs[][] board, GameUtil.Discs kakutei_turn){
        GameUtil.Discs[][] kb= new GameUtil.Discs[10][10];
        int i, j, u, l, k;
        int kakutei = 0;

        for (i = 1; i <=8; i++){
            for (j = 1; j <= 8; j++){
                kb[i][j] = board[8 - j + 1][i];
            }
        }

        for (u = 1; board[1][u] == kakutei_turn; u++){
        }
        for (l = 1; board[l][1] == kakutei_turn; l++){
        }

        if (u >= l){
            for (i = 1; i <= l; i++){
                k = 1;
                while (true){
                    if (k > u - i + 1){
                        break;
                    }
                    else if (board[i][k] == kakutei_turn){
                        kakutei++;
                        k++;
                    }
                    else{
                        break;
                    }
                }
            }
        }
        else{
            for (j = 1; j <= u; j++){
                k = 1;
                while (true){
                    if (k > l - j + 1){
                        break;
                    }
                    else if (board[k][j] == kakutei_turn){
                        kakutei++;
                        k++;
                    }
                    else{
                        break;
                    }
                }
            }
        }

        return kakutei;
    }

    int Extinct(GameUtil.Discs[][] board, GameUtil.Discs human){
        //全滅の処理
        boolean break_flag = false;
        int eval_score=0;
        boolean extinct_human = true, extinct_ai = true;
        for (int i = 1; i <=8; i++){
            for (int j = 1; j <= 8; j++){
                if (board[i][j] == human){
                    extinct_human = false;
                    break_flag = true;
                    break;
                }
            }
            if (break_flag == true) break;
        }

        break_flag = false;
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                if (board[i][j] == aiPlayer){
                    extinct_ai = false;
                    break_flag = true;
                    break;
                }
            }
            if (break_flag == true) break;
        }

        if (extinct_human == true&&extinct_ai == false){
            eval_score = eval_score + 10000;
        }
        if (extinct_ai == true&&extinct_human == false){
            eval_score = eval_score - 10000;
        }
        return eval_score;
    }

    int Liberty(GameUtil.Discs[][] board, GameUtil.Discs human){
        int eval=0;

        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                if (board[i][j] == aiPlayer){
                    for (int e = -1; e <= 1; e++){
                        for (int d = -1; d <= 1; d++){
                            if (d == 0 && e == 0) continue;

                            if ( board[i + e][j + d] == GameUtil.Discs.BLANK){
                                eval = eval - 5;
                            }
                        }
                    }
                }
                else if (board[i][j] == human){
                    for (int e = -1; e <= 1; e++){
                        for (int d = -1; d <= 1; d++){
                            if (d == 0 && e == 0) continue;
                            if (board[i + e][j + d] == GameUtil.Discs.BLANK){
                                eval = eval + 5;
                            }
                        }
                    }
                }
            }
        }

        return eval;
    }
    int Danger(GameUtil.Discs[][] board, GameUtil.Discs human){
        int eval=0;
        if (board[1][1] == GameUtil.Discs.BLANK){
            if (board[2][2] == aiPlayer){
                eval -= 500;
            }
        }
        if (board[1][8] == GameUtil.Discs.BLANK){
            if (board[2][7] == aiPlayer){
                eval -= 500;
            }
        }
        if (board[8][1] == GameUtil.Discs.BLANK){
            if (board[7][2] == aiPlayer){
                eval -= 500;
            }
        }
        if (board[8][8] == GameUtil.Discs.BLANK){
            if (board[7][7] == aiPlayer){
                eval -= 500;
            }
        }

        if (board[1][1] == GameUtil.Discs.BLANK){
            if (board[2][2] == human){
                eval += 500;
            }
        }
        if (board[1][8] == GameUtil.Discs.BLANK){
            if (board[2][7] == human){
                eval += 500;
            }
        }
        if (board[8][1] == GameUtil.Discs.BLANK){
            if (board[7][2] == human){
                eval += 500;
            }
        }
        if (board[8][8] == GameUtil.Discs.BLANK){
            if (board[7][7] == human){
                eval += 500;
            }
        }

        return eval;
    }

    int count_disc(int[][] board, int ai_player){
        int ai = 0, hu = 0;
        int i, j;
        int human = 3 - ai_player;
        for (i = 1; i <= 8; i++){
            for (j = 1; j <= 8; j++){
                if (board[i][j] == ai_player){
                    ai++;
                }
                if (board[i][j] == human){
                    hu++;
                }
            }
        }
        return ai - hu;
    }

    int eval_mid(GameUtil.Discs[][] smallBoard, GameUtil.Discs player){
        int eval_score = 437;
        int kakutei = 0;
        GameUtil.Discs[][] edge=new GameUtil.Discs[8][2];
        GameUtil.Discs human;

        GameUtil.Discs[][] board = new GameUtil.Discs[10][10];

        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(0==i||9==i||0==j||9==j){
                    board[i][j]= GameUtil.Discs.WALL;
                }
                else{
                    board[i][j]=smallBoard[i-1][j-1];
                }
            }
        }

        if (aiPlayer == GameUtil.Discs.BLACK) human = GameUtil.Discs.WHITE;
        else human = GameUtil.Discs.BLACK;

        eval_score+=Extinct(board,human);
        eval_score+=Liberty(board,human);

        if (board[1][8] == aiPlayer || board[1][1] == aiPlayer || board[1][8] == aiPlayer || board[8][8] == aiPlayer){
            eval_score +=  500;
            eval_score +=  10 * count_kakutei(board, aiPlayer);
        }
        if (board[1][8] == human || board[1][1] == human || board[8][1] == human || board[8][8] == human){
            eval_score += - 500;
            eval_score += - 10 * count_kakutei(board, human);
        }

        eval_score+=Danger(board,human);

        for (int i = 1; i <= 8; i++){//左辺のコピー
            for (int j = 1; j <= 2; j++){
                edge[i - 1][j - 1] = board[i][j];
            }
        }
        eval_score = eval_score + edge_eval(edge, aiPlayer);
        for (int i = 1; i <= 8; i++){//上辺のコピー
            for (int j = 1; j <= 2; j++){
                edge[i - 1][j - 1] = board[j][i];
            }
        }
        eval_score = eval_score + edge_eval(edge, aiPlayer);
        for (int i = 1; i <= 8; i++){//右辺のコピー
            for (int j = 1; j <= 2; j++){
                edge[i - 1][j - 1] = board[8 - i + 1][8 - j + 1];
            }
        }
        eval_score = eval_score + edge_eval(edge, aiPlayer);
        for (int i = 1; i <= 8; i++){//下辺のコピー
            for (int j = 1; j <= 2; j++){
                edge[i - 1][j - 1] = board[8 - j + 1][i];
            }
        }
        eval_score = eval_score + edge_eval(edge, aiPlayer);

        return eval_score;
    }

    int edge_eval(GameUtil.Discs[][] edge, GameUtil.Discs human){
        //edge[8][2]は添字0から始まっている。
        int x = 0, i, y;
        int ev = 0;
        int j;
        int count_disc = 0;


        if (edge[0][0] == GameUtil.Discs.BLANK&&edge[7][0] == GameUtil.Discs.BLANK){
            //プレイヤーは人間　相手の形を判別
            //余裕手は?２５
            if (edge[2][0] == human && edge[3][0] == human && edge[4][0] == human && edge[5][0] == human){
                //石が４つ辺に並んでいる
                if (edge[1][0] == human&&edge[6][0] == GameUtil.Discs.BLANK){
                    if (edge[5][1] == aiPlayer){
                        //ウイングにならずに余裕手になった
                        ev = ev - 30;
                    }
                    else if (edge[5][1] == human){
                        ev = ev + 10;
                        //ウイングになった
                    }
                }
                else if (edge[1][0] == GameUtil.Discs.BLANK&&edge[6][0] == human){
                    if (edge[2][1] == aiPlayer){
                        //ウイングにならずに余裕手になった
                        ev = ev - 30;
                    }
                    else if (edge[2][1] == human){
                        ev = ev + 10;
                        //ウイングになった
                    }
                }
                else if (edge[1][0] == human&&edge[6][0] == human){
                    //山になった
                    ev = ev - 20;
                }
                else if (edge[1][0] == GameUtil.Discs.BLANK && edge[6][0] == GameUtil.Discs.BLANK){
                    //ブロックか余裕手か
                    if (edge[2][1] == human && edge[5][1] == human){
                        ev = ev + 8;
                    }
                    else{
                        //相手の余裕手になる
                        if (edge[2][1] == aiPlayer){
                            ev = ev - 20;
                        }
                        if (edge[5][1] == aiPlayer){
                            ev = ev - 20;
                        }
                    }
                }
            }
            else if (edge[0][0] == GameUtil.Discs.BLANK&&edge[7][0] == GameUtil.Discs.BLANK){
                //humanの色を探して、石を見つけたら
                //何個連続かを調べて３つ以上なら連続の石の端の一つ上の色を調べて、
                //相手の色なら点数を付ける
                x = 7;
                while (x >= 1){
                    //edge[x][0]がhumanになったらその座標を抽出する
                    if (edge[x][0] == human){
                        break;
                    }
                    //問題点　一個も石がなかったら7まで続けてしまう
                    x--;
                }
                y = x;
                while (true){
                    if (edge[x][0] != human){
                        break;
                    }
                    count_disc++;
                    x--;
                }
                if (count_disc <= 2){//連続の石の個数
                    if (edge[1][0] == human){
                        ev = ev + 20;
                    }
                    if (edge[6][0] == human){
                        ev = ev + 20;
                    }
                }

            }
            //ai_player　始まり

            if (edge[2][0] == aiPlayer && edge[3][0] == aiPlayer && edge[4][0] == aiPlayer && edge[5][0] == aiPlayer){
                //石が４つ辺に並んでいる
                if (edge[1][0] == aiPlayer&&edge[6][0] == GameUtil.Discs.BLANK){
                    if (edge[5][1] == human){
                        //ウイングにならずに余裕手になった
                        ev = ev + 30;
                    }
                    else if (edge[5][1] == aiPlayer){
                        ev = ev - 10;//ウイングになった
                    }
                }
                else if (edge[1][0] == GameUtil.Discs.BLANK&&edge[6][0] == aiPlayer){
                    if (edge[2][1] == human){
                        //ウイングにならずに余裕手になった
                        ev = ev + 30;
                    }
                    else{
                        ev = ev - 10;
                    }
                }
                else if (edge[1][0] == aiPlayer&&edge[6][0] == aiPlayer){
                    //山になった
                    ev = ev + 20;
                }
                else if (edge[1][0] == GameUtil.Discs.BLANK && edge[6][0] == GameUtil.Discs.BLANK){
                    //ブロックか余裕手か
                    if (edge[2][1] == aiPlayer && edge[5][1] == aiPlayer){//ブロック
                        ev = ev - 8;
                    }
                    else{
                        //相手の余裕手になる
                        if (edge[2][1] == human){
                            ev = ev + 20;
                        }
                        if (edge[5][1] == human){
                            ev = ev + 20;
                        }
                    }
                }
            }
            else{
                //humanの色を探して、石を見つけたら
                //何個連続かを調べて３つ以上なら連続の石の端の一つ上の色を調べて、
                //相手の色なら点数を付ける
                x = 7;
                while (x >= 1){
                    //edge[x][0]がhumanになったらその座標を抽出する
                    if (edge[x][0] == aiPlayer){
                        break;
                    }
                    //問題点　一個も石がなかったら7まで続けてしまう
                    x--;
                }
                y = x;
                while (true){
                    if (edge[x][0] != aiPlayer){
                        break;
                    }
                    count_disc++;
                    x--;
                }
                if (count_disc < 3){//連続の石の個数が3以下で単独c打ち
                    if (edge[1][0] == aiPlayer){
                        ev = ev - 20;
                    }
                    if (edge[6][0] == aiPlayer){
                        ev = ev - 20;
                    }
                }

            }
            //ai_player終わり
        }
        return ev;
    }

    int count_disc(GameUtil.Discs[][] board, GameUtil.Discs selfPlayer){
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

    int RecursionMove(GameUtil.Discs[][] board, GameUtil.Discs recursionTurn, int limit, int alpha, int beta){

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
                    score = RecursionMove(board, recursionTurn, limit - 1, alpha, beta);
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
            return RecursionMove(board, recursionTurn, limit-1, -beta, -alpha);
        }

        if (recursionTurn == aiPlayer){
            return score_max;
        }
        else{
            return score_min;
        }
    }

    int RecursionEndMove(GameUtil.Discs[][] board, GameUtil.Discs recursionTurn, int limit, int alpha, int beta){
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
                    score = RecursionEndMove(board, recursionTurn, limit - 1, alpha, beta);
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
            return RecursionEndMove(board, recursionTurn, limit-1, -beta, -alpha);
        }

        if (recursionTurn == aiPlayer){
            return score_max;
        }
        else{
            return score_min;
        }
    }
}