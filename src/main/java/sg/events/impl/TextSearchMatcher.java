package sg.events.impl;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import settings.render.ClientLocalization;

public final class TextSearchMatcher {
   private static final Map<String, String> WORD_TRANSLITERATIONS = Map.ofEntries(
      Map.entry(TextSearchMatcher.ьХ, TextSearchMatcher.ье),
      Map.entry(TextSearchMatcher.ьА, TextSearchMatcher.ь4),
      Map.entry(TextSearchMatcher.ьп, TextSearchMatcher.ьД),
      Map.entry(TextSearchMatcher.ьЪ, TextSearchMatcher.ьт),
      Map.entry(TextSearchMatcher.ьа, TextSearchMatcher.ьЮ),
      Map.entry(TextSearchMatcher.ьЧ, TextSearchMatcher.ьЛ),
      Map.entry(TextSearchMatcher.ь诶, TextSearchMatcher.ь1),
      Map.entry(TextSearchMatcher.ьЬ, TextSearchMatcher.ь衣),
      Map.entry(TextSearchMatcher.ьш, TextSearchMatcher.ьИ),
      Map.entry(TextSearchMatcher.ьь, TextSearchMatcher.ьж),
      Map.entry(TextSearchMatcher.ь弟, TextSearchMatcher.ьб),
      Map.entry(TextSearchMatcher.ьЫ, TextSearchMatcher.ьl),
      Map.entry(TextSearchMatcher.ь8, TextSearchMatcher.ь5),
      Map.entry(TextSearchMatcher.ьв, TextSearchMatcher.ьъ),
      Map.entry(TextSearchMatcher.ьМ, TextSearchMatcher.ьН),
      Map.entry(TextSearchMatcher.ьн, TextSearchMatcher.ьЦ),
      Map.entry(TextSearchMatcher.ьч, TextSearchMatcher.ьЭ),
      Map.entry(TextSearchMatcher.ь必, TextSearchMatcher.ьщ),
      Map.entry(TextSearchMatcher.ьГ, TextSearchMatcher.ьл),
      Map.entry(TextSearchMatcher.ьэ, TextSearchMatcher.ью),
      Map.entry(TextSearchMatcher.ьЩ, TextSearchMatcher.ьу),
      Map.entry(TextSearchMatcher.ь2, TextSearchMatcher.ьф),
      Map.entry(TextSearchMatcher.ьи, TextSearchMatcher.ь9),
      Map.entry(TextSearchMatcher.ьо, TextSearchMatcher.ьO),
      Map.entry(TextSearchMatcher.ь7, TextSearchMatcher.ьР),
      Map.entry(TextSearchMatcher.ь3, TextSearchMatcher.ьр),
      Map.entry(TextSearchMatcher.ь6, TextSearchMatcher.ь_),
      Map.entry(TextSearchMatcher.ьШ, TextSearchMatcher.ьы),
      Map.entry(TextSearchMatcher.ьй, TextSearchMatcher.ьЖ),
      Map.entry(TextSearchMatcher.ьм, TextSearchMatcher.ьК),
      Map.entry(TextSearchMatcher.ьI, TextSearchMatcher.ьТ),
      Map.entry(TextSearchMatcher.ьО, TextSearchMatcher.ьФ),
      Map.entry(TextSearchMatcher.ьЙ, TextSearchMatcher.ьк),
      Map.entry(TextSearchMatcher.ьг, TextSearchMatcher.ь0),
      Map.entry(TextSearchMatcher.ьх, TextSearchMatcher.ьУ),
      Map.entry(TextSearchMatcher.ьЗ, TextSearchMatcher.ьЕ),
      Map.entry(TextSearchMatcher.ьП, TextSearchMatcher.ь西),
      Map.entry(TextSearchMatcher.ьд, TextSearchMatcher.ьБ),
      Map.entry(TextSearchMatcher.ьС, TextSearchMatcher.ьз),
      Map.entry(TextSearchMatcher.ьц, TextSearchMatcher.ья),
      Map.entry(TextSearchMatcher.жс, TextSearchMatcher.жВ),
      Map.entry(TextSearchMatcher.жЯ, TextSearchMatcher.жХ),
      Map.entry(TextSearchMatcher.же, TextSearchMatcher.жА),
      Map.entry(TextSearchMatcher.ж4, TextSearchMatcher.жп),
      Map.entry(TextSearchMatcher.жД, TextSearchMatcher.жЪ),
      Map.entry(TextSearchMatcher.жт, TextSearchMatcher.жа),
      Map.entry(TextSearchMatcher.жЮ, TextSearchMatcher.жЧ),
      Map.entry(TextSearchMatcher.жЛ, TextSearchMatcher.ж诶),
      Map.entry(TextSearchMatcher.ж1, TextSearchMatcher.жЬ),
      Map.entry(TextSearchMatcher.ж衣, TextSearchMatcher.жш),
      Map.entry(TextSearchMatcher.жИ, TextSearchMatcher.жь),
      Map.entry(TextSearchMatcher.жж, TextSearchMatcher.ж弟),
      Map.entry(TextSearchMatcher.жб, TextSearchMatcher.жЫ),
      Map.entry(TextSearchMatcher.жl, TextSearchMatcher.ж8),
      Map.entry(TextSearchMatcher.ж5, TextSearchMatcher.жв),
      Map.entry(TextSearchMatcher.жъ, TextSearchMatcher.жМ),
      Map.entry(TextSearchMatcher.жН, TextSearchMatcher.жн),
      Map.entry(TextSearchMatcher.жЦ, TextSearchMatcher.жч),
      Map.entry(TextSearchMatcher.жЭ, TextSearchMatcher.ж必),
      Map.entry(TextSearchMatcher.жщ, TextSearchMatcher.жГ),
      Map.entry(TextSearchMatcher.жл, TextSearchMatcher.жэ),
      Map.entry(TextSearchMatcher.жю, TextSearchMatcher.жЩ),
      Map.entry(TextSearchMatcher.жу, TextSearchMatcher.ж2),
      Map.entry(TextSearchMatcher.жф, TextSearchMatcher.жи),
      Map.entry(TextSearchMatcher.ж9, TextSearchMatcher.жо),
      Map.entry(TextSearchMatcher.жO, TextSearchMatcher.ж7),
      Map.entry(TextSearchMatcher.жР, TextSearchMatcher.ж3),
      Map.entry(TextSearchMatcher.жр, TextSearchMatcher.ж6),
      Map.entry(TextSearchMatcher.ж_, TextSearchMatcher.жШ),
      Map.entry(TextSearchMatcher.жы, TextSearchMatcher.жй),
      Map.entry(TextSearchMatcher.жЖ, TextSearchMatcher.жм),
      Map.entry(TextSearchMatcher.жК, TextSearchMatcher.жI),
      Map.entry(TextSearchMatcher.жТ, TextSearchMatcher.жО),
      Map.entry(TextSearchMatcher.жФ, TextSearchMatcher.жЙ),
      Map.entry(TextSearchMatcher.жк, TextSearchMatcher.жг),
      Map.entry(TextSearchMatcher.ж0, TextSearchMatcher.жх),
      Map.entry(TextSearchMatcher.жУ, TextSearchMatcher.жЗ),
      Map.entry(TextSearchMatcher.жЕ, TextSearchMatcher.жП),
      Map.entry(TextSearchMatcher.ж西, TextSearchMatcher.жд),
      Map.entry(TextSearchMatcher.жБ, TextSearchMatcher.жС),
      Map.entry(TextSearchMatcher.жз, TextSearchMatcher.жц),
      Map.entry(TextSearchMatcher.жя, TextSearchMatcher.弟с),
      Map.entry(TextSearchMatcher.弟В, TextSearchMatcher.弟Я),
      Map.entry(TextSearchMatcher.弟Х, TextSearchMatcher.弟е),
      Map.entry(TextSearchMatcher.弟А, TextSearchMatcher.弟4),
      Map.entry(TextSearchMatcher.弟п, TextSearchMatcher.弟Д),
      Map.entry(TextSearchMatcher.弟Ъ, TextSearchMatcher.弟т),
      Map.entry(TextSearchMatcher.弟а, TextSearchMatcher.弟Ю),
      Map.entry(TextSearchMatcher.弟Ч, TextSearchMatcher.弟Л),
      Map.entry(TextSearchMatcher.弟诶, TextSearchMatcher.弟1),
      Map.entry(TextSearchMatcher.弟Ь, TextSearchMatcher.弟衣),
      Map.entry(TextSearchMatcher.弟ш, TextSearchMatcher.弟И),
      Map.entry(TextSearchMatcher.弟ь, TextSearchMatcher.弟ж),
      Map.entry(TextSearchMatcher.弟弟, TextSearchMatcher.弟б),
      Map.entry(TextSearchMatcher.弟Ы, TextSearchMatcher.弟l),
      Map.entry(TextSearchMatcher.弟8, TextSearchMatcher.弟5),
      Map.entry(TextSearchMatcher.弟в, TextSearchMatcher.弟ъ),
      Map.entry(TextSearchMatcher.弟М, TextSearchMatcher.弟Н),
      Map.entry(TextSearchMatcher.弟н, TextSearchMatcher.弟Ц),
      Map.entry(TextSearchMatcher.弟ч, TextSearchMatcher.弟Э),
      Map.entry(TextSearchMatcher.弟必, TextSearchMatcher.弟щ),
      Map.entry(TextSearchMatcher.弟Г, TextSearchMatcher.弟л),
      Map.entry(TextSearchMatcher.弟э, TextSearchMatcher.弟ю),
      Map.entry(TextSearchMatcher.弟Щ, TextSearchMatcher.弟у),
      Map.entry(TextSearchMatcher.弟2, TextSearchMatcher.弟ф),
      Map.entry(TextSearchMatcher.弟и, TextSearchMatcher.弟9),
      Map.entry(TextSearchMatcher.弟о, TextSearchMatcher.弟O),
      Map.entry(TextSearchMatcher.弟7, TextSearchMatcher.弟Р),
      Map.entry(TextSearchMatcher.弟3, TextSearchMatcher.弟р),
      Map.entry(TextSearchMatcher.弟6, TextSearchMatcher.弟_),
      Map.entry(TextSearchMatcher.弟Ш, TextSearchMatcher.弟ы),
      Map.entry(TextSearchMatcher.弟й, TextSearchMatcher.弟Ж),
      Map.entry(TextSearchMatcher.弟м, TextSearchMatcher.弟К),
      Map.entry(TextSearchMatcher.弟I, TextSearchMatcher.弟Т),
      Map.entry(TextSearchMatcher.弟О, TextSearchMatcher.弟Ф),
      Map.entry(TextSearchMatcher.弟Й, TextSearchMatcher.弟к),
      Map.entry(TextSearchMatcher.弟г, TextSearchMatcher.弟0),
      Map.entry(TextSearchMatcher.弟х, TextSearchMatcher.弟У),
      Map.entry(TextSearchMatcher.弟З, TextSearchMatcher.弟Е),
      Map.entry(TextSearchMatcher.弟П, TextSearchMatcher.弟西),
      Map.entry(TextSearchMatcher.弟д, TextSearchMatcher.弟Б),
      Map.entry(TextSearchMatcher.弟С, TextSearchMatcher.弟з),
      Map.entry(TextSearchMatcher.弟ц, TextSearchMatcher.弟я),
      Map.entry(TextSearchMatcher.бс, TextSearchMatcher.бВ),
      Map.entry(TextSearchMatcher.бЯ, TextSearchMatcher.бХ),
      Map.entry(TextSearchMatcher.бе, TextSearchMatcher.бА),
      Map.entry(TextSearchMatcher.б4, TextSearchMatcher.бп)
   );
   private static final String Ир = "\\s+";
   private static final String И6 = "([a-z])([A-Z])";
   private static final String И_ = "$1 $2";
   private static final String ИШ = "([A-Z]+)([A-Z][a-z])";
   private static final String Иы = "$1 $2";
   private static final String Ий = "([\\p{L}])(\\d)";
   private static final String ИЖ = "$1 $2";
   private static final String Им = "(\\d)([\\p{L}])";
   private static final String ИК = "$1 $2";
   private static final String ИI = "[^\\p{L}\\p{N}]+";
   private static final String ИТ = " ";
   private static final String ИО = " ";
   private static final String ИФ = "zh";
   private static final String ИЙ = "ts";
   private static final String Ик = "ch";
   private static final String Иг = "sh";
   private static final String И0 = "sch";
   private static final String Их = "yu";
   private static final String ИУ = "ya";
   private static final String ИЗ = "\\s+";
   private static final String ИЕ = "sch";
   private static final String ИП = "tch";
   private static final String И西 = "aut";
   private static final String Ид = "авт";
   private static final String ИБ = "кв";
   private static final String ИС = "джа";
   private static final String Из = "ау";
   private static final String Иц = "оу";
   private static final String Ия = "дж";
   private static final String ьс = "кс";
   private static final String ьВ = "\\s+";
   private static final String ьЯ = " ";
   private static final String ьХ = "accept";
   private static final String ье = "акцепт";
   private static final String ьА = "aim";
   private static final String ь4 = "аим";
   private static final String ьп = "aimbot";
   private static final String ьД = "аимбот";
   private static final String ьЪ = "alert";
   private static final String ьт = "алерт";
   private static final String ьа = "ambience";
   private static final String ьЮ = "амбиенс";
   private static final String ьЧ = "animation";
   private static final String ьЛ = "анимация";
   private static final String ь诶 = "animations";
   private static final String ь1 = "анимации";
   private static final String ьЬ = "anti";
   private static final String ь衣 = "анти";
   private static final String ьш = "armor";
   private static final String ьИ = "армор";
   private static final String ьь = "arrow";
   private static final String ьж = "арроу";
   private static final String ь弟 = "arrows";
   private static final String ьб = "арроус";
   private static final String ьЫ = "atmosphere";
   private static final String ьl = "атмосфера";
   private static final String ь8 = "aura";
   private static final String ь5 = "аура";
   private static final String ьв = "auto";
   private static final String ьъ = "авто";
   private static final String ьМ = "block";
   private static final String ьН = "блок";
   private static final String ьн = "boost";
   private static final String ьЦ = "буст";
   private static final String ьч = "booster";
   private static final String ьЭ = "бустер";
   private static final String ь必 = "box";
   private static final String ьщ = "бокс";
   private static final String ьГ = "break";
   private static final String ьл = "брейк";
   private static final String ьэ = "buy";
   private static final String ью = "бай";
   private static final String ьЩ = "chat";
   private static final String ьу = "чат";
   private static final String ь2 = "china";
   private static final String ьф = "чайна";
   private static final String ьи = "circle";
   private static final String ь9 = "серкл";
   private static final String ьо = "click";
   private static final String ьO = "клик";
   private static final String ь7 = "client";
   private static final String ьР = "клиент";
   private static final String ь3 = "contract";
   private static final String ьр = "контракт";
   private static final String ь6 = "controller";
   private static final String ь_ = "контроллер";
   private static final String ьШ = "criticals";
   private static final String ьы = "критикалс";
   private static final String ьй = "crosshair";
   private static final String ьЖ = "кроссхаир";
   private static final String ьм = "crystal";
   private static final String ьК = "кристалл";
   private static final String ьI = "cube";
   private static final String ьТ = "куб";
   private static final String ьО = "cursor";
   private static final String ьФ = "курсор";
   private static final String ьЙ = "custom";
   private static final String ьк = "кастом";
   private static final String ьг = "down";
   private static final String ь0 = "даун";
   private static final String ьх = "downloader";
   private static final String ьУ = "даунлоадер";
   private static final String ьЗ = "duel";
   private static final String ьЕ = "дуэль";
   private static final String ьП = "durability";
   private static final String ь西 = "дюрабилити";
   private static final String ьд = "effect";
   private static final String ьБ = "эффект";
   private static final String ьС = "effects";
   private static final String ьз = "эффекты";
   private static final String ьц = "elytra";
   private static final String ья = "элитра";
   private static final String жс = "emotes";
   private static final String жВ = "эмоутс";
   private static final String жЯ = "entity";
   private static final String жХ = "энтити";
   private static final String же = "esp";
   private static final String жА = "есп";
   private static final String ж4 = "event";
   private static final String жп = "ивент";
   private static final String жД = "exploit";
   private static final String жЪ = "эксплоит";
   private static final String жт = "fake";
   private static final String жа = "фейк";
   private static final String жЮ = "fast";
   private static final String жЧ = "фаст";
   private static final String жЛ = "field";
   private static final String ж诶 = "филд";
   private static final String ж1 = "fire";
   private static final String жЬ = "фаер";
   private static final String ж衣 = "firework";
   private static final String жш = "фаерворк";
   private static final String жИ = "fish";
   private static final String жь = "фиш";
   private static final String жж = "fly";
   private static final String ж弟 = "флай";
   private static final String жб = "fog";
   private static final String жЫ = "фог";
   private static final String жl = "fun";
   private static final String ж8 = "фан";
   private static final String ж5 = "funtime";
   private static final String жв = "фантайм";
   private static final String жъ = "ghost";
   private static final String жМ = "гост";
   private static final String жН = "ghoul";
   private static final String жн = "гуль";
   private static final String жЦ = "glide";
   private static final String жч = "глайд";
   private static final String жЭ = "gui";
   private static final String ж必 = "гуи";
   private static final String жщ = "hat";
   private static final String жГ = "хэт";
   private static final String жл = "head";
   private static final String жэ = "хед";
   private static final String жю = "helper";
   private static final String жЩ = "хелпер";
   private static final String жу = "hit";
   private static final String ж2 = "хит";
   private static final String жф = "holy";
   private static final String жи = "холи";
   private static final String ж9 = "hud";
   private static final String жо = "худ";
   private static final String жO = "impact";
   private static final String ж7 = "импакт";
   private static final String жР = "invisible";
   private static final String ж3 = "инвизибл";
   private static final String жр = "item";
   private static final String ж6 = "айтем";
   private static final String ж_ = "joiner";
   private static final String жШ = "джойнер";
   private static final String жы = "jump";
   private static final String жй = "джамп";
   private static final String жЖ = "key";
   private static final String жм = "кей";
   private static final String жК = "kill";
   private static final String жI = "килл";
   private static final String жТ = "lag";
   private static final String жО = "лаг";
   private static final String жФ = "leave";
   private static final String жЙ = "лив";
   private static final String жк = "loot";
   private static final String жг = "лут";
   private static final String ж0 = "markers";
   private static final String жх = "маркеры";
   private static final String жУ = "mist";
   private static final String жЗ = "мист";
   private static final String жЕ = "mode";
   private static final String жП = "мод";
   private static final String ж西 = "model";
   private static final String жд = "модель";
   private static final String жБ = "motion";
   private static final String жС = "моушн";
   private static final String жз = "move";
   private static final String жц = "мув";
   private static final String жя = "name";
   private static final String 弟с = "нейм";
   private static final String 弟В = "no";
   private static final String 弟Я = "ноу";
   private static final String 弟Х = "nova";
   private static final String 弟е = "нова";
   private static final String 弟А = "optimizer";
   private static final String 弟4 = "оптимизер";
   private static final String 弟п = "overlay";
   private static final String 弟Д = "оверлей";
   private static final String 弟Ъ = "particle";
   private static final String 弟т = "партикл";
   private static final String 弟а = "particles";
   private static final String 弟Ю = "партиклы";
   private static final String 弟Ч = "pearl";
   private static final String 弟Л = "перл";
   private static final String 弟诶 = "picture";
   private static final String 弟1 = "пикча";
   private static final String 弟Ь = "player";
   private static final String 弟衣 = "плеер";
   private static final String 弟ш = "potion";
   private static final String 弟И = "поушн";
   private static final String 弟ь = "prediction";
   private static final String 弟ж = "предикт";
   private static final String 弟弟 = "protect";
   private static final String 弟б = "протект";
   private static final String 弟Ы = "push";
   private static final String 弟l = "пуш";
   private static final String 弟8 = "saturation";
   private static final String 弟5 = "сатурация";
   private static final String 弟в = "scroller";
   private static final String 弟ъ = "скроллер";
   private static final String 弟М = "see";
   private static final String 弟Н = "си";
   private static final String 弟н = "shader";
   private static final String 弟Ц = "шейдер";
   private static final String 弟ч = "skeleton";
   private static final String 弟Э = "скелетон";
   private static final String 弟必 = "slow";
   private static final String 弟щ = "слоу";
   private static final String 弟Г = "sound";
   private static final String 弟л = "саунд";
   private static final String 弟э = "sounds";
   private static final String 弟ю = "саундс";
   private static final String 弟Щ = "speed";
   private static final String 弟у = "спид";
   private static final String 弟2 = "spider";
   private static final String 弟ф = "спайдер";
   private static final String 弟и = "swap";
   private static final String 弟9 = "свап";
   private static final String 弟о = "swing";
   private static final String 弟O = "свинг";
   private static final String 弟7 = "tag";
   private static final String 弟Р = "тег";
   private static final String 弟3 = "tags";
   private static final String 弟р = "теги";
   private static final String 弟6 = "target";
   private static final String 弟_ = "таргет";
   private static final String 弟Ш = "tester";
   private static final String 弟ы = "тестер";
   private static final String 弟й = "thors";
   private static final String 弟Ж = "торс";
   private static final String 弟м = "time";
   private static final String 弟К = "тайм";
   private static final String 弟I = "timer";
   private static final String 弟Т = "таймер";
   private static final String 弟О = "tool";
   private static final String 弟Ф = "тул";
   private static final String 弟Й = "totem";
   private static final String 弟к = "тотем";
   private static final String 弟г = "tracer";
   private static final String 弟0 = "трейсер";
   private static final String 弟х = "tracers";
   private static final String 弟У = "трейсеры";
   private static final String 弟З = "tracker";
   private static final String 弟Е = "трекер";
   private static final String 弟П = "trail";
   private static final String 弟西 = "трейл";
   private static final String 弟д = "trails";
   private static final String 弟Б = "трейлы";
   private static final String 弟С = "trap";
   private static final String 弟з = "трап";
   private static final String 弟ц = "velocity";
   private static final String 弟я = "велосити";
   private static final String бс = "view";
   private static final String бВ = "вью";
   private static final String бЯ = "voice";
   private static final String бХ = "войс";
   private static final String бе = "web";
   private static final String бА = "веб";
   private static final String б4 = "world";
   private static final String бп = "ворлд";

   public static boolean matches(String query, String text) {
      return score(query, text) >= 0;
   }

   private static void addDictionaryPhraseVariants(Set<String> variants, String text) {
      if (!text.isEmpty()) {
         String[] words = text.split(ИЗ);
         StringBuilder phrase = new StringBuilder();
         boolean hadDictionaryReplacement = false;

         for (String word : words) {
            if (!word.isEmpty()) {
               if (!phrase.isEmpty()) {
                  phrase.append(' ');
               }

               String dictionaryWord = WORD_TRANSLITERATIONS.get(word);
               if (dictionaryWord != null) {
                  phrase.append(dictionaryWord);
                  hadDictionaryReplacement = true;
               } else {
                  phrase.append(latinToCyrillic(word, false));
               }
            }
         }

         if (hadDictionaryReplacement) {
            addNormalizedVariant(variants, phrase.toString());
            addNormalizedVariant(variants, compact(phrase.toString()));
         }
      }
   }

   private static boolean matchesAt(String text, int index, String sequence) {
      return index + sequence.length() <= text.length() && text.regionMatches(index, sequence, 0, sequence.length());
   }

   static Set<String> buildVariants(String text, boolean expandPhoneticVariants) {
      LinkedHashSet<String> variants = new LinkedHashSet<>();
      if (text != null && !text.isEmpty()) {
         String splitText = splitIdentifierWords(text);
         addNormalizedVariant(variants, text);
         addNormalizedVariant(variants, splitText);
         addNormalizedVariant(variants, compact(text));
         addNormalizedVariant(variants, compact(splitText));
         if (!expandPhoneticVariants) {
            return variants;
         }

         for (boolean useSoftInitialE : new boolean[]{false, true}) {
            String transliteratedOriginal = latinToCyrillic(normalize(text), useSoftInitialE);
            String transliteratedSplit = latinToCyrillic(normalize(splitText), useSoftInitialE);
            addNormalizedVariant(variants, transliteratedOriginal);
            addNormalizedVariant(variants, transliteratedSplit);
            addNormalizedVariant(variants, compact(transliteratedOriginal));
            addNormalizedVariant(variants, compact(transliteratedSplit));
         }

         addDictionaryPhraseVariants(variants, normalize(splitText));
         addNormalizedVariant(variants, cyrillicToLatin(normalize(text)));
         addNormalizedVariant(variants, cyrillicToLatin(normalize(splitText)));
         return variants;
      } else {
         return variants;
      }
   }

   static String normalize(String text) {
      return text != null && !text.isEmpty() ? text.toLowerCase(Locale.ROOT).replace('ё', 'е').trim() : "";
   }

   static String compact(String text) {
      return normalize(text).replace(ИО, "");
   }

   private static boolean matchesVariant(String candidate, String queryVariant) {
      if (!candidate.equals(queryVariant) && !candidate.startsWith(queryVariant)) {
         for (String word : candidate.split(ьВ)) {
            if (word.startsWith(queryVariant)) {
               return true;
            }

            if (queryVariant.length() >= 3 && word.contains(queryVariant)) {
               return true;
            }
         }

         String compactCandidate = candidate.replace(ьЯ, "");
         return compactCandidate.startsWith(queryVariant) ? true : queryVariant.length() >= 3 && compactCandidate.contains(queryVariant);
      } else {
         return true;
      }
   }

   private static boolean matchesToken(Set<String> candidates, String queryToken) {
      for (String queryVariant : buildTokenVariants(queryToken)) {
         for (String candidate : candidates) {
            if (matchesVariant(candidate, queryVariant)) {
               return true;
            }
         }
      }

      return false;
   }

   private TextSearchMatcher() {
   }

   private static boolean isAsciiLatinLowercase(char character) {
      return character >= 'a' && character <= 'z';
   }

   private static boolean isWordEnd(String text, int index) {
      int nextIndex = index + 1;
      return nextIndex >= text.length() || !Character.isLetter(text.charAt(nextIndex));
   }

   static String latinToCyrillic(String text, boolean useSoftInitialE) {
      if (text != null && !text.isEmpty()) {
         StringBuilder result = new StringBuilder(text.length());
         boolean wordStart = true;
         int index = 0;

         while (index < text.length()) {
            char character = text.charAt(index);
            if (!isAsciiLatinLowercase(character)) {
               if (character >= 1072 && character <= 1103) {
                  result.append(character);
                  wordStart = false;
               } else if (character == 1105) {
                  result.append('е');
                  wordStart = false;
               } else {
                  result.append(character);
                  wordStart = Character.isWhitespace(character);
               }

               index++;
            } else {
               int consumedChars = appendLatinChunk(result, text, index, wordStart, useSoftInitialE);
               index += consumedChars;
               wordStart = false;
            }
         }

         return result.toString();
      } else {
         return "";
      }
   }

   private static void addNormalizedVariant(Set<String> variants, String text) {
      String normalizedVariant = normalize(text);
      if (!normalizedVariant.isEmpty()) {
         variants.add(normalizedVariant);
      }
   }

   static String cyrillicToLatin(String text) {
      if (text != null && !text.isEmpty()) {
         StringBuilder result = new StringBuilder(text.length());

         for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            switch (character) {
               case 'а':
                  result.append('a');
                  break;
               case 'б':
                  result.append('b');
                  break;
               case 'в':
                  result.append('v');
                  break;
               case 'г':
                  result.append('g');
                  break;
               case 'д':
                  result.append('d');
                  break;
               case 'е':
               case 'э':
               case 'ё':
                  result.append('e');
                  break;
               case 'ж':
                  result.append(ИФ);
                  break;
               case 'з':
                  result.append('z');
                  break;
               case 'и':
               case 'й':
               case 'ы':
                  result.append('i');
                  break;
               case 'к':
                  result.append('k');
                  break;
               case 'л':
                  result.append('l');
                  break;
               case 'м':
                  result.append('m');
                  break;
               case 'н':
                  result.append('n');
                  break;
               case 'о':
                  result.append('o');
                  break;
               case 'п':
                  result.append('p');
                  break;
               case 'р':
                  result.append('r');
                  break;
               case 'с':
                  result.append('s');
                  break;
               case 'т':
                  result.append('t');
                  break;
               case 'у':
                  result.append('u');
                  break;
               case 'ф':
                  result.append('f');
                  break;
               case 'х':
                  result.append('h');
                  break;
               case 'ц':
                  result.append(ИЙ);
                  break;
               case 'ч':
                  result.append(Ик);
                  break;
               case 'ш':
                  result.append(Иг);
                  break;
               case 'щ':
                  result.append(И0);
               case 'ъ':
               case 'ь':
                  break;
               case 'ю':
                  result.append(Их);
                  break;
               case 'я':
                  result.append(ИУ);
                  break;
               case 'ѐ':
               default:
                  result.append(character);
            }
         }

         return result.toString();
      } else {
         return "";
      }
   }

   private static int scoreVariant(String query, String variant) {
      if (variant.equals(query)) {
         return 1000;
      }

      if (variant.startsWith(query)) {
         return 800 - Math.min(variant.length(), 80);
      }

      int matchIndex = variant.indexOf(query);
      return matchIndex >= 0 ? 600 - matchIndex : 0;
   }

   private static int appendLatinChunk(StringBuilder result, String text, int index, boolean wordStart, boolean useSoftInitialE) {
      int remainingLength = text.length() - index;
      char character = text.charAt(index);
      char nextCharacter = remainingLength > 1 ? text.charAt(index + 1) : '\u0000';
      if (matchesAt(text, index, ИЕ)) {
         result.append('щ');
         return 3;
      }

      if (matchesAt(text, index, ИП)) {
         result.append('ч');
         return 3;
      }

      if (matchesAt(text, index, И西)) {
         result.append(Ид);
         return 3;
      }

      if (character == 'p' && nextCharacter == 'h') {
         result.append('ф');
         return 2;
      }

      if (character == 't' && nextCharacter == 'h') {
         result.append('т');
         return 2;
      }

      if (character == 'c' && nextCharacter == 'h') {
         result.append('ч');
         return 2;
      }

      if (character == 's' && nextCharacter == 'h') {
         result.append('ш');
         return 2;
      }

      if (character == 'c' && nextCharacter == 'k') {
         result.append('к');
         return 2;
      }

      if (character == 'q' && nextCharacter == 'u') {
         result.append(ИБ);
         return 2;
      }

      if (character == 'w' && nextCharacter == 'h') {
         result.append('в');
         return 2;
      }

      if (character == 'k' && nextCharacter == 'h') {
         result.append('х');
         return 2;
      }

      if (character == 'z' && nextCharacter == 'h') {
         result.append('ж');
         return 2;
      }

      if (character == 'g' && nextCharacter == 'h') {
         result.append('г');
         return 2;
      }

      if (character == 'j' && nextCharacter == 'u') {
         result.append(ИС);
         return 2;
      }

      if (character == 'e' && nextCharacter == 'e') {
         result.append('и');
         return 2;
      }

      if (character == 'o' && nextCharacter == 'o') {
         result.append('у');
         return 2;
      }

      if (character == 'o' && nextCharacter == 'u') {
         result.append(Из);
         return 2;
      }

      if (character == 'e' && nextCharacter == 'a') {
         result.append('е');
         return 2;
      }

      if (character == 'o' && nextCharacter == 'w') {
         result.append(Иц);
         return 2;
      }

      if (character == 'y' && nextCharacter == 'o') {
         result.append('е');
         return 2;
      }

      if (character == 'y' && nextCharacter == 'u') {
         result.append('ю');
         return 2;
      }

      if (character == 'y' && nextCharacter == 'a') {
         result.append('я');
         return 2;
      }

      if (character == 'y' && nextCharacter == 'e') {
         result.append('е');
         return 2;
      }

      switch (character) {
         case 'a':
            result.append('а');
            break;
         case 'b':
            result.append('б');
            break;
         case 'c':
            result.append((char)(nextCharacter != 'e' && nextCharacter != 'i' && nextCharacter != 'y' ? 'к' : 'с'));
            break;
         case 'd':
            result.append('д');
            break;
         case 'e':
            result.append((char)(wordStart && !useSoftInitialE ? 'э' : 'е'));
            break;
         case 'f':
            result.append('ф');
            break;
         case 'g':
            result.append('г');
            break;
         case 'h':
            result.append('х');
            break;
         case 'i':
            result.append('и');
            break;
         case 'j':
            result.append(Ия);
            break;
         case 'k':
            result.append('к');
            break;
         case 'l':
            result.append('л');
            break;
         case 'm':
            result.append('м');
            break;
         case 'n':
            result.append('н');
            break;
         case 'o':
            result.append('о');
            break;
         case 'p':
            result.append('п');
            break;
         case 'q':
            result.append('к');
            break;
         case 'r':
            result.append('р');
            break;
         case 's':
            result.append('с');
            break;
         case 't':
            result.append('т');
            break;
         case 'u':
            result.append('у');
            break;
         case 'v':
            result.append('в');
            break;
         case 'w':
            result.append((char)(isWordEnd(text, index) ? 'у' : 'в'));
            break;
         case 'x':
            result.append(ьс);
            break;
         case 'y':
            result.append('и');
            break;
         case 'z':
            result.append('з');
            break;
         default:
            result.append(character);
      }

      return 1;
   }

   static String splitIdentifierWords(String text) {
      return text != null && !text.isEmpty() ? text.replaceAll(И6, И_).replaceAll(ИШ, Иы).replaceAll(Ий, ИЖ).replaceAll(Им, ИК).replaceAll(ИI, ИТ).trim() : "";
   }

   private static Set<String> buildTokenVariants(String token) {
      LinkedHashSet<String> variants = new LinkedHashSet<>();
      addNormalizedVariant(variants, token);
      if (token.length() < 2) {
         return variants;
      }

      addNormalizedVariant(variants, latinToCyrillic(token, false));
      addNormalizedVariant(variants, latinToCyrillic(token, true));
      addNormalizedVariant(variants, cyrillicToLatin(token));
      String dictionaryWord = WORD_TRANSLITERATIONS.get(token);
      if (dictionaryWord != null) {
         addNormalizedVariant(variants, dictionaryWord);
      }

      return variants;
   }

   static Set<String> buildSearchVariants(String text) {
      return buildVariants(text, true);
   }

   public static int score(String query, String... textCandidates) {
      if (query != null && !query.isEmpty() && textCandidates != null && textCandidates.length != 0) {
         String normalizedQuery = normalize(query);
         if (normalizedQuery.isEmpty()) {
            return -1;
         }

         int compactQueryLength = compact(normalizedQuery).length();
         boolean expandPhonetics = compactQueryLength >= 2;
         boolean includeDescriptions = compactQueryLength >= 3;
         LinkedHashSet<String> allVariants = new LinkedHashSet<>();
         LinkedHashSet<String> nameVariants = new LinkedHashSet<>();
         boolean hasCandidate = false;

         for (int candidateIndex = 0; candidateIndex < textCandidates.length; candidateIndex++) {
            if (candidateIndex <= 0 || includeDescriptions) {
               String candidate = textCandidates[candidateIndex];
               if (candidate != null && !candidate.isEmpty()) {
                  hasCandidate = true;
                  Set<String> candidateVariants = buildVariants(candidate, expandPhonetics);
                  allVariants.addAll(candidateVariants);
                  if (candidateIndex == 0) {
                     nameVariants.addAll(candidateVariants);
                  }

                  String localizedCandidate = ClientLocalization.translate(candidate);
                  if (localizedCandidate != null && !localizedCandidate.isEmpty() && !localizedCandidate.equals(candidate)) {
                     Set<String> localizedVariants = buildVariants(localizedCandidate, expandPhonetics);
                     allVariants.addAll(localizedVariants);
                     if (candidateIndex == 0) {
                        nameVariants.addAll(localizedVariants);
                     }
                  }
               }
            }
         }

         if (!hasCandidate) {
            return -1;
         }

         for (String queryToken : normalizedQuery.split(Ир)) {
            if (!queryToken.isEmpty() && !matchesToken(allVariants, queryToken)) {
               return -1;
            }
         }

         int bestScore = 0;

         for (String nameVariant : nameVariants) {
            bestScore = Math.max(bestScore, scoreVariant(normalizedQuery, nameVariant));
         }

         return bestScore > 0 ? bestScore : 300;
      } else {
         return -1;
      }
   }
}
