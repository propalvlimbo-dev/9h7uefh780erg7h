package dev.rose.hud;

import java.util.ArrayDeque;
import java.util.Deque;


public final class GameStats {
   private static final long CPS_WINDOW_MS = 1000L;
   private static final Deque<Long> leftClicks = new ArrayDeque<>();
   private static final Deque<Long> rightClicks = new ArrayDeque<>();
   private static long sessionStart = -1L;

   private GameStats() {
   }

   public static void mousePressed(int button) {
      long now = System.currentTimeMillis();
      if (button == 0) {
         leftClicks.addLast(now);
      } else if (button == 1) {
         rightClicks.addLast(now);
      }
   }

   public static int cps(boolean left) {
      Deque<Long> clicks = left ? leftClicks : rightClicks;
      long border = System.currentTimeMillis() - 1000L;

      while (!clicks.isEmpty() && clicks.peekFirst() < border) {
         clicks.pollFirst();
      }

      return clicks.size();
   }

   public static void sessionStarted() {
      sessionStart = System.currentTimeMillis();
   }

   public static void sessionEnded() {
      sessionStart = -1L;
   }

   public static long sessionMillis() {
      return sessionStart < 0L ? 0L : System.currentTimeMillis() - sessionStart;
   }
}
