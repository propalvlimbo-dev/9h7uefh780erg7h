package sg.handler;

import com.google.gson.annotations.SerializedName;

public final class PartyOperationState {
   @SerializedName("В")
   private PartyOperationState.PendingOperation В_278bf792;
   @SerializedName("В")
   private PartyOperationState.Credentials В_eaf667c1;
   private boolean с3;
   private static final String Ч7 = "[A-Za-z0-9_.+\\-]{1,128}";
   private static final String ЧР = "party.created";
   private static final String Ч3 = "party.joined";
   private static final String Чр = "party.already";
   private static final String Ч6 = "party.joined";
   private static final String Ч_ = "party.leaved";
   private static final String ЧШ = "party.disband";
   private static final String Чы = "party.disbanded";
   private static final String Чй = "party.deleted";
   private static final String ЧЖ = "party.null";
   private static final String Чм = "party.kicked";
   private static final String ЧК = "party.not.owner";
   private static final String ЧI = "unknown.data";
   private static final String ЧТ = "party.not.owner";
   private static final String ЧО = "party.not.found";
   private static final String ЧФ = "party.exist";
   private static final String ЧЙ = "party.password.incorrect";
   private static final String Чк = "party.already";
   private static final long Чг = 5000000000L;

   public void О(PartySession var1, String var2, long var3) {
      PartyOperationState.PendingOperation var5 = this.В_278bf792;
      boolean var6 = var2.equals(ЧР) || var2.equals(Ч3);
      if (var5 != null && var5.restoring && var2.equals(Чр) && this.В_eaf667c1 != null && this.В_eaf667c1.name.equalsIgnoreCase(var5.credentials.name)) {
         var2 = Ч6;
         var6 = true;
      }

      var1.З_de5a6f78(var2, var3);

      boolean var7 = switch (var2) {
         case Ч_, ЧШ, Чы, Чй, ЧЖ, Чм -> true;
         default -> false;
      };
      if (var7) {
         this.В_278bf792 = null;
         this.В_eaf667c1 = null;
         this.с3 = false;
         var1.З_();
      } else if (var5 != null && var5.joining() && var6 && var1.Зй()) {
         this.В_eaf667c1 = var5.credentials;
         this.с3 = false;
         this.В_278bf792 = null;
      } else if (var5 != null && this.Ы(var2)) {
         PartyOperationState.Credentials var10 = this.В_eaf667c1;
         this.О(var1, var5);
         if (!var5.joining() && var2.equals(ЧК) && var1.Зй()) {
            this.В_eaf667c1 = var10;
         }
      }
   }

   public PartyOperationState.PendingOperation О(
      PartySession var1, PartyOperationState.Action var2, String var3, String var4, boolean var5, long var6
   ) {
      if (this.В_278bf792 != null) {
         return null;
      }

      boolean var8 = var2 == PartyOperationState.Action.JOIN || var2 == PartyOperationState.Action.CREATE;
      if (!var8 || Щ(var3) && Я(var4) && var1.method_6(var3)) {
         this.В_278bf792 = new PartyOperationState.PendingOperation(var2, var8 ? new PartyOperationState.Credentials(var3, var4) : null, var6, var5);
         if (!var8) {
            this.с3 = false;
         }

         return this.В_278bf792;
      } else {
         return null;
      }
   }

   public static boolean Я(String var0) {
      return var0 != null && (var0.isEmpty() || Щ(var0));
   }

   public boolean О(PartySession var1, PartyOperationState.PendingOperation var2) {
      if (var2 != null && this.В_278bf792 == var2) {
         this.В_278bf792 = null;
         var1.З_();
         if (var2.restoring || !var2.joining()) {
            this.В_eaf667c1 = null;
            this.с3 = false;
         }

         return true;
      } else {
         return false;
      }
   }

   public void жь() {
      this.В_eaf667c1 = null;
      this.с3 = false;
   }

   public PartyOperationState.PendingOperation О_278bf792() {
      return this.В_278bf792;
   }

   public void О(PartySession var1) {
      if (this.В_278bf792 != null && !this.В_278bf792.joining()) {
         this.В_eaf667c1 = null;
      }

      this.В_278bf792 = null;
      this.с3 = this.В_eaf667c1 != null;
      var1.ЗЕ();
   }

   public static boolean Щ(String var0) {
      return var0 != null && var0.matches(Ч7);
   }

   private boolean Ы(String var1) {
      return var1.equals(ЧI) || var1.equals(ЧТ) || var1.equals(ЧО) || var1.equals(ЧФ) || var1.equals(ЧЙ) || var1.equals(Чк);
   }

   public PartyOperationState.Credentials О_eaf667c1() {
      return this.с3 ? this.В_eaf667c1 : null;
   }

   public boolean Ш(long var1) {
      return this.В_278bf792 != null && var1 - this.В_278bf792.started >= Чг;
   }

   public record Credentials(String name, String password) {
   }

   public enum Action {
      JOIN,
      CREATE,
      LEAVE,
      DISBAND;
   }

   public record PendingOperation(
      PartyOperationState.Action action, PartyOperationState.Credentials credentials, long started, boolean restoring
   ) {
      public boolean joining() {
         return this.action == PartyOperationState.Action.JOIN || this.action == PartyOperationState.Action.CREATE;
      }
   }
}
