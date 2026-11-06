package sg.render;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.resources.Identifier;

public enum WeaponSkinVariant {
   ABOMINABLE_BLADE("Abominable Blade", "abominableblade"),
   ABOMINABLE_GREATSABER("Abominable Greatsaber", "abominablegreatsaber"),
   ABOMINABLE_SCYTHE("Abominable Scythe", "abominablescythe"),
   ACID_DEMON("Acid Demon", "aciddemon"),
   AMETHYST_SHURIKEN("Amethyst Shuriken", "amethyst_shuriken"),
   ANCIENT_ROYAL_GREATSWORD("Ancient Royal Greatsword", "ancient_royal_great_sword"),
   AQUANTIC_SACRED_BLADE("Aquantic Sacred Blade", "aquantic_sacred_blade"),
   AQUANTIC_TRIDENT("Aquantic Trident", "aquantictrident"),
   ARCANYTHEST("Arcanethyst", "arcanethyst"),
   ASHURA_BLADE("Ashura Blade", "ashura_blade"),
   AWAKENED_LICHBLADE("Awakened Lichblade", "awakened_lichblade"),
   BLOOD_EDGE("Blood Edge", "bloodedge"),
   BLOODY_DEATH("Bloody Death", "bloodydeath"),
   BRAMBLETHORN("Bramblethorn", "bramblethorn"),
   BRIMSTONE_CLAYMORE("Brimstone Claymore", "brimstone_claymore"),
   CARIAN_SWORD("Carian Sword", "cariansword"),
   CHRONO_BLADE("Chrono Blade", "chrono_blade"),
   CORRUPTED_MYTHIC_BLADE("Corrupted Mythic Blade", "corruptedmythicblade"),
   CREATION_SPLITTER("Creation Splitter", "creationsplitter"),
   CRESCENT_ROSE("Crescent Rose", "crescentrose"),
   CYBER_KATANA("Cyber Katana", "cyberkatana"),
   CYBER_MANTIS_BLADE("Cyber Mantis Blade", "cybermantisblade"),
   CYBERNETIC_KATANA("Cybernetic Katana", "cybernetickatana"),
   CYBERNETIC_KNIFE("Cybernetic Knife", "cyberneticknife"),
   CYBERNETIC_SAWBLADE("Cybernetic Sawblade", "cyberneticsawblade"),
   CYBER_SWORD("Cyber Sword", "cybersword"),
   DAINSLEIF("Dainsleif", "dainsleif"),
   DARK_BLADE("Dark Blade", "dark_blade"),
   DARK_CLEAVER("Dark Cleaver", "dark_cleaver"),
   DEATH_KNIGHT_DAGGER("Death Knight Dagger", "death_knight_dagger"),
   DEATH_KNIGHT_SWORD("Death Knight Sword", "death_knight_sword"),
   DEMIGODS_UNHOLY_BLADE("Demigod's Unholy Blade", "demigodsunholyblade"),
   DEMIGODS_UNHOLY_HALBERD("Demigod's Unholy Halberd", "demigodsunholyhalberd"),
   DEMONIC_BLADE("Demonic Blade", "demonicblade"),
   DEMONIC_CLEAVER("Demonic Cleaver", "demoniccleaver"),
   DEMON_LORDS_GREAT_AXE("Demon Lord's Great Axe", "demonlordsgreataxe"),
   DEMON_LORDS_SWORD("Demon Lord's Sword", "demonlordsword"),
   DIVINE_JUSTICE("Divine Justice", "divine_justice"),
   DIVINE_REAPER("Divine Reaper", "divine_reaper"),
   DIVINE_AXE_RHITTA("Divine Axe Rhitta", "divineaxerhitta"),
   DIVINE_PUNISHER("Divine Punisher", "divinepunisher"),
   DRAGON_SLAYING_BLADE("Dragon Slaying Blade", "dragonslayingblade"),
   EDGE_OF_THE_ASTRAL_PLANE("Edge of the Astral Plane", "edgeoftheastralplane"),
   EMBERBLADE("Emberblade", "emberblade"),
   ENIGMA("Enigma", "enigma"),
   EPIC_SWORD("Epic Sword", "epicsword"),
   ESTOC("Estoc", "estoc"),
   EXCALIBUR("Excalibur", "excalibur"),
   FALLEN_GOD_SPEAR("Fallen God Spear", "fallengodspear"),
   FALLEN_GOD_SWORD("Fallen God Sword", "fallengodsword"),
   FLORAL_LONGSWORD("Floral Longsword", "floral_longsword"),
   FLORAL_SABRE("Floral Sabre", "floral_sabre"),
   FOREST_GUARDIAN_GLAIVE("Forest Guardian Glaive", "forest_guardian_glaive"),
   FROST_AXE("Frost Axe", "frostaxe"),
   FROSTBLADE("Frostblade", "frostblade"),
   FROST_SCYTHE("Frost Scythe", "frostscythe"),
   GREEN_SCYTHE("Green Scythe", "greenscythe"),
   HEARTHFLAME("Hearthflame", "hearthflame"),
   HERO_SWORD("Hero Sword", "herosword"),
   HOLY_MOONLIGHT_SWORD("Holy Moonlight Sword", "holymoonlightsword"),
   HORNETS_NEEDLE("Hornet's Needle", "hornetsneedle"),
   ICE_WHISPER("Ice Whisper", "icewhisper"),
   JADE_HALBERD("Jade Halberd", "jadehalberd"),
   KATANA("Katana", "katana"),
   LEGENDARY_SWORD("Legendary Sword", "legendarysword"),
   LONGSWORD("Longsword", "longsword"),
   MAGI_SCYTHE("Magi Scythe", "magiscythe"),
   MASAMUNE("Masamune", "masamune"),
   MJOLNIR("Mjolnir", "mjolnir"),
   MOLTEN_BLADE("Molten Blade", "moltenblade"),
   MOLTEN_SWORD("Molten Sword", "moltensword"),
   MURAMASA("Muramasa", "muramasa"),
   MYSTICAL_SPELLBLADE("Mystical Spellblade", "mysticalspellblade"),
   MYTHIC_BLADE("Mythic Blade", "mythicblade"),
   PARTISAN("Partisan", "partisan"),
   PHARAOHS_TREASURE("Pharaoh's Treasure", "pharaohs_treasure"),
   PHOENIX_GRACE("Phoenix Grace", "pheonixgrace"),
   POWERFUSE_HAMMER("Powerfuse Hammer", "powerfusehammer"),
   POWERFUSE_SWORD("Powerfuse Sword", "powerfusesword"),
   REQUIEM_OF_HELL("Requiem of Hell", "requiem_of_hell"),
   RIBBON_CLEAVER("Ribbon Cleaver", "ribboncleaver"),
   RIGHTEOUS_RELIC("Righteous Relic", "righteous_relic"),
   RIVERS_OF_BLOOD("Rivers of Blood", "riversofblood"),
   ROYAL_CHAKRAM("Royal Chakram", "royalchakram"),
   ROYAL_RAPIER("Royal Rapier", "royalrapier"),
   SABRE("Sabre", "sabre"),
   SCISSOR_BLADE("Scissor Blade", "scissorblade"),
   SCULK_CLEAVER("Sculk Cleaver", "sculkcleaver"),
   SCULK_SCYTHE("Sculk Scythe", "sculkscythe"),
   SCULK_SWORD("Sculk Sword", "sculksword"),
   SENTINELS_WILL("Sentinel's Will", "sentinels_will"),
   SILVERINE_BLADE("Silverine Blade", "silverine_blade"),
   SOUL_CLAWS("Soul Claws", "soulclaws"),
   SOUL_EDGE("Soul Edge", "souledge"),
   SOUL_HARVESTER("Soul Harvester", "soulharvester"),
   SOUL_RENDER("Soul Render", "soulrender"),
   SOUL_STEALER("Soul Stealer", "soulstealer"),
   SOUL_COLLECTOR("Soul Collector", "soul_collector"),
   SOUL_DEVOURER("Soul Devourer", "soul_devourer"),
   STARS_EDGE("Star's Edge", "stars_edge"),
   STEEL_SWORD("Steel Sword", "steelsword"),
   STOP_SIGN("Stop Sign", "stop_sign"),
   STORMBRINGER("Stormbringer", "stormbringer"),
   STORMS_EDGE("Storm's Edge", "storms_edge"),
   SUNBREAK("Sunbreak", "sunbreak"),
   TENGENS_BLADE("Tengen's Blade", "tengensblade"),
   TERRABLADE("Terrablade", "terrablade"),
   THOUSAND_DEMON_DAGGERS("Thousand Demon Daggers", "thousanddemondaggers"),
   THUNDERBRAND("Thunderbrand", "thunderbrand"),
   THUNDERBRINGER("Thunderbringer", "thunderbringer"),
   TOXIC_LONGSWORD("Toxic Longsword", "toxic_longsword"),
   VAMPIRIC_NEEDLE("Vampiric Needle", "vampiricneedle"),
   WAKIZASHI("Wakizashi", "wakizashi"),
   WATCHER_CLAYMORE("Watcher Claymore", "watcher_claymore"),
   WATCHING_WARGLAIVE("Watching Warglaive", "watching_warglaive"),
   WAXWEAVER("Waxweaver", "waxweaver"),
   WHISPERWIND("Whisperwind", "whisperwind"),
   WICKPIERCER("Wickpiercer", "wickpiercer"),
   YORU("Yoru", "yoru");

   private static final Map<String, WeaponSkinVariant> variantsByDisplayName = new HashMap<>();
   private final @com.google.gson.annotations.SerializedName("Ях")  String displayName;
   private final @com.google.gson.annotations.SerializedName("сс")  Identifier itemModelId;

   WeaponSkinVariant(String displayName, String itemModelPath) {
      this.displayName = displayName;
      this.itemModelId = Identifier.fromNamespaceAndPath("minecraft", itemModelPath);
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public Identifier getItemModelId() {
      return this.itemModelId;
   }

   public static WeaponSkinVariant fromDisplayName(String displayName) {
      return displayName != null && !displayName.isEmpty() ? variantsByDisplayName.get(displayName.toLowerCase(Locale.ROOT)) : null;
   }

   static {
      for (WeaponSkinVariant variant : values()) {
         variantsByDisplayName.put(variant.displayName.toLowerCase(Locale.ROOT), variant);
      }
   }
}
