package util;

//time class for keeping track of game time and frames
public class Time {
      public static float timeStarted = System.nanoTime();

      //return time in seconds
      public static float getTime(){
            return (float) ((System.nanoTime() - timeStarted)*1E-9);
      }
}
