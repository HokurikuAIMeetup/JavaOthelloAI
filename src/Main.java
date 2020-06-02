import java.util.Arrays;

class Main{
    //public static PlayGame playgame;
    public static int BOARD_SIZE  = 8;

    public static void PrintArray(int[] anyArray) {
        System.out.println(Arrays.toString(anyArray));
    }

    public static void main(String args[]) {
        System.out.println("Hello!");
        int playMode = 0;

        GameUtil gameUtil = new GameUtil();
        OthelloAI othelloAI_1 = new OthelloAI(0);//AIプレイヤーは黒の場合、0を指定 AIが白の場合1を指定;
        OthelloAI othelloAI_2 = new OthelloAI(1);//AIプレイヤーは黒の場合、0を指定 AIが白の場合1を指定;

        OthelloAI[] ai_agent_array = {othelloAI_1,othelloAI_2};

        //ここから対戦開始
        //int[] cpu_move_position = new int[2];

        boolean GAME_END_FLAG = false;
        while(GAME_END_FLAG==false){
            for(int ai_player=0;ai_player<2;ai_player++) {
                int[] cpu_move_position = ai_agent_array[ai_player].DecideMove(gameUtil.GetBoard());
                gameUtil.put(gameUtil.GetBoard(), gameUtil.GetTurn(), cpu_move_position[0], cpu_move_position[1]);
                gameUtil.PrintBoard(gameUtil.GetBoard());
                gameUtil.ChangeTurn();
                //GAME_END_FLAG = true;
            }
        }

    }
}

/*TODO
*  配列の中身を可視化する関数を作る*/