package com.example.tawfiq.run4life;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "run4life.MyService.action.main";

        public static String STARTFOREGROUND_ACTION = "run4life.MyService.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "run4life.MyService.action.stopforeground";


        public static String PREV_ACTION = "run4life.MyService.action.prev";
        public static String PLAY_ACTION = "run4life.MyService.action.play";
        public static String NEXT_ACTION = "run4life.MyService.action.next";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }


}
