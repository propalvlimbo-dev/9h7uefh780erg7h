package dev.rose.gui;

import com.google.gson.JsonObject;
import dev.rose.config.ConfigManager;
import dev.rose.hud.HudElement;
import dev.rose.hud.HudManager;
import java.util.ArrayDeque;
import java.util.Deque;


final class HudHistory {
   private static final int LIMIT = 80;
   private static final long MERGE_MS = 700L;
   private final Deque<JsonObject> undo = new ArrayDeque<>();
   private final Deque<JsonObject> redo = new ArrayDeque<>();
   private JsonObject pending;
   private String lastKind;
   private long lastTime;

   static JsonObject snapshot() {
      JsonObject root = new JsonObject();

      for (HudElement element : HudManager.elements()) {
         root.add(element.id(), ConfigManager.moduleToJson(element));
      }

      return root;
   }

   private static void restore(JsonObject snapshot) {
      for (HudElement element : HudManager.elements()) {
         JsonObject entry = snapshot.getAsJsonObject(element.id());
         if (entry != null) {
            ConfigManager.applyModule(element, entry);
            element.onConfigLoaded();
         }
      }
   }

   void begin() {
      if (this.pending == null) {
         this.pending = snapshot();
      }
   }

   void end(String kind) {
      if (this.pending != null) {
         JsonObject before = this.pending;
         this.pending = null;
         JsonObject after = snapshot();
         if (!before.equals(after)) {
            long now = System.currentTimeMillis();
            boolean merge = kind != null && kind.equals(this.lastKind) && now - this.lastTime < 700L && !this.undo.isEmpty();
            this.lastKind = kind;
            this.lastTime = now;
            this.redo.clear();
            if (!merge) {
               this.undo.push(before);

               while (this.undo.size() > 80) {
                  this.undo.removeLast();
               }
            }
         }
      }
   }

   void record(JsonObject before) {
      if (!before.equals(snapshot())) {
         this.lastKind = null;
         this.redo.clear();
         this.undo.push(before);

         while (this.undo.size() > 80) {
            this.undo.removeLast();
         }
      }
   }

   boolean canUndo() {
      return !this.undo.isEmpty();
   }

   boolean canRedo() {
      return !this.redo.isEmpty();
   }

   boolean undo() {
      if (this.undo.isEmpty()) {
         return false;
      }

      this.redo.push(snapshot());
      restore(this.undo.pop());
      this.lastKind = null;
      return true;
   }

   boolean redo() {
      if (this.redo.isEmpty()) {
         return false;
      }

      this.undo.push(snapshot());
      restore(this.redo.pop());
      this.lastKind = null;
      return true;
   }
}
