package sg.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import net.minecraft.client.player.AbstractClientPlayer;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import sg.render.LuaTransformMath;

final class LuaApiUserdata extends LuaUserdata {
   private final LuaValue е;
   private final LuaTable В = new LuaTable();
   private final Map<LuaValue, LuaValue> с必 = new HashMap<>();
   private static final String с衣Х = "get";
   private static final String с衣е = "get";
   private static final String с衣А = "sin";
   private static final String с衣4 = "cos";
   private static final String с衣п = "floor";
   private static final String с衣Д = "abs";
   private static final String с衣Ъ = "ceil";
   private static final String с衣т = "round";
   private static final String с衣а = "pow";
   private static final String с衣Ю = "clamp";
   private static final String с衣Ч = "lerp";
   private static final String с衣Л = "easeInOutBack";
   private static final String с衣诶 = "easeInSine";
   private static final String с衣1 = "easeOutSine";
   private static final String с衣Ь = "easeInOutSine";
   private static final String с衣衣 = "easeInQuad";
   private static final String с衣ш = "easeOutQuad";
   private static final String с衣И = "easeInOutQuad";
   private static final String с衣ь = "easeInCubic";
   private static final String с衣ж = "easeOutCubic";
   private static final String с衣弟 = "easeInOutCubic";
   private static final String с衣б = "easeInQuart";
   private static final String с衣Ы = "easeOutQuart";
   private static final String с衣l = "easeInOutQuart";
   private static final String с衣8 = "easeInQuint";
   private static final String с衣5 = "easeOutQuint";
   private static final String с衣в = "easeInOutQuint";
   private static final String с衣ъ = "easeInExpo";
   private static final String с衣М = "easeOutExpo";
   private static final String с衣Н = "easeInOutExpo";
   private static final String с衣н = "easeInCirc";
   private static final String с衣Ц = "easeOutCirc";
   private static final String с衣ч = "easeInOutCirc";
   private static final String с衣Э = "easeInBack";
   private static final String с衣必 = "easeOutBack";
   private static final String с衣щ = "easeInElastic";
   private static final String с衣Г = "easeOutElastic";
   private static final String с衣л = "easeInOutElastic";
   private static final String с衣э = "easeOutBounce";
   private static final String с衣ю = "easeInBounce";
   private static final String с衣Щ = "easeInOutBounce";
   private static final String с衣у = "cubicEase";
   private static final String с衣2 = "getHealth";
   private static final String с衣ф = "isSneaking";
   private static final String с衣и = "isOnGround";
   private static final String с衣9 = "isSwimming";
   private static final String с衣о = "isClimbing";
   private static final String с衣O = "isCrawling";
   private static final String с衣7 = "isSubmergedInWater";
   private static final String с衣Р = "isTouchingWater";
   private static final String с衣3 = "isUsingSpyglass";
   private static final String с衣р = "isUsingRiptide";
   private static final String с衣6 = "getX";
   private static final String с衣_ = "getY";
   private static final String с衣Ш = "getZ";
   private static final String с衣ы = "getXSpeed";
   private static final String с衣й = "getYSpeed";
   private static final String с衣Ж = "getZSpeed";
   private static final String с衣м = "getSpeed";
   private static final String с衣К = "isUsingItem";
   private static final String с衣I = "getYaw";
   private static final String с衣Т = "getPitch";
   private static final String с衣О = "getMainItem";
   private static final String с衣Ф = "getOffhandItem";
   private static final String с衣Й = "getActiveHand";
   private static final String с衣к = "getAge";
   private static final String с衣г = "getSwingCount";

   static LuaValue of(Object var0) {
      return new LuaApiUserdata(var0);
   }

   private void ternary(String var1, final LuaApiUserdata.TernaryDoubleOperator var2) {
      final LuaValue var3 = this.е.get(var1);
      this.В
         .set(
            var1,
            new VarArgFunction() {
               @Override
               public Varargs invoke(Varargs var1) {
                  if (var1.arg1().touserdata() == LuaApiUserdata.this.m_instance
                     && var1.narg() == 4
                     && var1.arg(2).isnumber()
                     && var1.arg(3).isnumber()
                     && var1.arg(4).isnumber()) {
                     try {
                        return valueOf(var2.ч(var1.arg(2).todouble(), var1.arg(3).todouble(), var1.arg(4).todouble()));
                     } catch (RuntimeException var3x) {
                        throw new LuaError(var3x);
                     }
                  } else {
                     return var3.invoke(var1);
                  }
               }
            }
         );
   }

   @Override
   public void set(LuaValue var1, LuaValue var2) {
      this.е.set(var1, var2);
      this.с必.remove(var1);
   }

   @Override
   public LuaValue get(LuaValue var1) {
      LuaValue var2 = this.В.rawget(var1);
      if (!var2.isnil()) {
         return var2;
      }

      var2 = this.с必.get(var1);
      if (var2 != null) {
         return var2;
      }

      LuaValue var3 = this.е.get(var1);
      if (var3.isfunction()) {
         if (this.с必.size() >= 256) {
            this.с必.clear();
         }

         this.с必.put(var1, var3);
      }

      return var3;
   }

   private void player(String var1, final Function<AbstractClientPlayer, Object> var2) {
      final LuaValue var3 = this.е.get(var1);
      this.В.set(var1, new TwoArgFunction() {
         @Override
         public LuaValue call(LuaValue var1, LuaValue var2x) {
            if (var1.touserdata() == LuaApiUserdata.this.m_instance && var2x.isuserdata(AbstractClientPlayer.class)) {
               try {
                  return CoerceJavaToLua.coerce(var2.apply((AbstractClientPlayer)var2x.touserdata()));
               } catch (RuntimeException var4) {
                  throw new LuaError(var4);
               }
            } else {
               return var3.call(var1, var2x);
            }
         }
      });
   }

   private void binary(String var1, final DoubleBinaryOperator var2) {
      final LuaValue var3 = this.е.get(var1);
      this.В
         .set(
            var1,
            new ThreeArgFunction() {
               @Override
               public LuaValue call(LuaValue var1, LuaValue var2x, LuaValue var3x) {
                  return var1.touserdata() == LuaApiUserdata.this.m_instance && var2x.isnumber() && var3x.isnumber()
                     ? valueOf(var2.applyAsDouble(var2x.todouble(), var3x.todouble()))
                     : var3.call(var1, var2x, var3x);
               }
            }
         );
   }

   private void unary(String var1, final DoubleUnaryOperator var2) {
      final LuaValue var3 = this.е.get(var1);
      this.В.set(var1, new TwoArgFunction() {
         @Override
         public LuaValue call(LuaValue var1, LuaValue var2x) {
            return var1.touserdata() == LuaApiUserdata.this.m_instance && var2x.isnumber() ? valueOf(var2.applyAsDouble(var2x.todouble())) : var3.call(var1, var2x);
         }
      });
   }

   private LuaApiUserdata(Object var1) {
      super(var1);
      this.е = CoerceJavaToLua.coerce(var1);
      if (var1 instanceof LuaItemRegistry var2) {
         final LuaValue var3 = this.е.get(с衣Х);
         final HashMap var4 = new HashMap();
         this.В.set(с衣е, new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue var1, LuaValue var2x) {
               if (var1.touserdata() == LuaApiUserdata.this.m_instance && var2x.type() == 4) {
                  LuaValue var3x = (LuaValue)var4.get(var2x);
                  if (var3x != null) {
                     return var3x;
                  }

                  try {
                     var3x = CoerceJavaToLua.coerce(var2.get(var2x.tojstring()));
                  } catch (RuntimeException var5) {
                     throw new LuaError(var5);
                  }

                  if (var4.size() >= 2048) {
                     var4.clear();
                  }

                  var4.put(var2x, var3x);
                  return var3x;
               } else {
                  return var3.call(var1, var2x);
               }
            }
         });
      }

      if (var1 instanceof LuaTransformMath var5) {
         this.unary(с衣А, var5::sin);
         this.unary(с衣4, var5::cos);
         this.unary(с衣п, var5::дн);
         this.unary(с衣Д, var5::abs);
         this.unary(с衣Ъ, var5::дl);
         this.unary(с衣т, var5::дФ);
         this.binary(с衣а, var5::pow);
         this.ternary(с衣Ю, var5::clamp);
         this.ternary(с衣Ч, var5::lerp);
      }

      if (var1 instanceof LuaEasings var6) {
         this.unary(с衣Л, var6::easeInOutBack);
         this.unary(с衣诶, var6::easeInSine);
         this.unary(с衣1, var6::西);
         this.unary(с衣Ь, var6::easeInOutSine);
         this.unary(с衣衣, var6::д);
         this.unary(с衣ш, var6::к);
         this.unary(с衣И, var6::б);
         this.unary(с衣ь, var6::Ж);
         this.unary(с衣ж, var6::Д);
         this.unary(с衣弟, var6::й);
         this.unary(с衣б, var6::easeInQuart);
         this.unary(с衣Ы, var6::easeOutQuart);
         this.unary(с衣l, var6::ж);
         this.unary(с衣8, var6::М);
         this.unary(с衣5, var6::ц);
         this.unary(с衣в, var6::method_5);
         this.unary(с衣ъ, var6::Н);
         this.unary(с衣М, var6::У);
         this.unary(с衣Н, var6::easeInOutExpo);
         this.unary(с衣н, var6::в);
         this.unary(с衣Ц, var6::э);
         this.unary(с衣ч, var6::ш);
         this.unary(с衣Э, var6::easeInBack);
         this.unary(с衣必, var6::easeOutBack);
         this.unary(с衣щ, var6::р);
         this.unary(с衣Г, var6::ю);
         this.unary(с衣л, var6::н);
         this.unary(с衣э, var6::诶);
         this.unary(с衣ю, var6::ъ);
         this.unary(с衣Щ, var6::弟);
         this.unary(с衣у, var6::cubicEase);
      }

      if (var1 instanceof LuaPlayerAccess var7) {
         this.player(с衣2, var7::Г_3f39d5c3);
         this.player(с衣ф, var7::isSneaking);
         this.player(с衣и, var7::isOnGround);
         this.player(с衣9, var7::isSwimming);
         this.player(с衣о, var7::isClimbing);
         this.player(с衣O, var7::isCrawling);
         this.player(с衣7, var7::Я_bbeebd87);
         this.player(с衣Р, var7::isTouchingWater);
         this.player(с衣3, var7::У_bbeebd87);
         this.player(с衣р, var7::isUsingRiptide);
         this.player(с衣6, var7::Ъ_3f39d5c3);
         this.player(с衣_, var7::method_8_3f39d5c3);
         this.player(с衣Ш, var7::Ч_3f39d5c3);
         this.player(с衣ы, var7::И_3f39d5c3);
         this.player(с衣й, var7::getYSpeed);
         this.player(с衣Ж, var7::э_3f39d5c3);
         this.player(с衣м, var7::getSpeed);
         this.player(с衣К, var7::isUsingItem);
         this.player(с衣I, var7::getYaw);
         this.player(с衣Т, var7::getPitch);
         this.player(с衣О, var7::getMainItem);
         this.player(с衣Ф, var7::getOffhandItem);
         this.player(с衣Й, var7::getActiveHand);
         this.player(с衣к, var7::getAge);
         this.player(с衣г, var7::getSwingCount);
      }
   }

   private interface TernaryDoubleOperator {
      double ч(double var1, double var3, double var5);
   }
}
