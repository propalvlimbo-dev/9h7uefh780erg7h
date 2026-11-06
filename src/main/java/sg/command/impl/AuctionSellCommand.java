package sg.command.impl;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.command.ClientCommand;
import sg.modules.impl.init.FunTimeItemCatalog;

public final class AuctionSellCommand extends ClientCommand implements MinecraftAccess {
   private final @com.google.gson.annotations.SerializedName("П")  List<AuctionSellCommand.PriceSample> priceSamples = new ArrayList<>();
   private final @com.google.gson.annotations.SerializedName("с0")  EventListener<PacketReceiveEvent> packetReceiveListener = this::handleAuctionContents;
   private final @com.google.gson.annotations.SerializedName("сх")  EventListener<PlayerTickEvent> playerTickListener = this::sendPendingSellCommand;
   private @com.google.gson.annotations.SerializedName("сг")  String pendingSellCommand;
   private @com.google.gson.annotations.SerializedName("В")  AuctionSellCommand.SellRequest pendingSellRequest;
   private static final String COMMAND_NAME = "ah";
   private static final String SELL_LITERAL = "sell";
   private static final String SELL_DISCOUNT_PERCENT_ARGUMENT_NAME = "процент";
   private static final String UNSUPPORTED_ITEM_MESSAGE = "Авто-продажа недоступна для обычных предметов — только для донатных";
   private static final double PERCENT_DIVISOR = 100.0;
   private static final String FORMATTING_CODE_PATTERN = "(?i)§[0-9A-FK-ORX]";
   private static final String FORMATTING_REPLACEMENT = " ";
   private static final String SIZE_TAG_PATTERN = "\\[\\d+x\\d+]";
   private static final String SIZE_TAG_REPLACEMENT = " ";
   private static final String BRACKET_TAG_PATTERN = "\\[[^\\]]*]";
   private static final String BRACKET_TAG_REPLACEMENT = " ";
   private static final String XXX_WORD_PATTERN = "(?i)\\bxxx\\b";
   private static final String XXX_WORD_REPLACEMENT = " ";
   private static final String NAME_PUNCTUATION_PATTERN = "[^\\p{L}\\p{N}\\-\\s]";
   private static final String PUNCTUATION_REPLACEMENT = " ";
   private static final String NAME_WHITESPACE_PATTERN = "\\s+";
   private static final String NAME_WHITESPACE_REPLACEMENT = " ";
   private static final String STALE_LISTING_MESSAGE = "Товар не актуален";
   private static final String PRICE_TOOLTIP_MARKER = "$ Ценa: ";
   private static final String PRICE_TOOLTIP_PREFIX = "$ Ценa: ";
   private static final String PRICE_TOOLTIP_PREFIX_LENGTH_TEXT = "$ Ценa: ";
   private static final String PRICE_GROUP_SEPARATOR = ",";
   private static final String CURRENCY_SYMBOL = "$";
   private static final String PRICE_WHITESPACE_PATTERN = "\\s+";
   private static final long PRICE_SAMPLE_TTL_MILLIS = 15000L;
   private static final double PRICE_OUTLIER_THRESHOLD_RATIO = 0.75;
   private static final String COLLECT_LISTING_TOOLTIP_MARKER = "Нажмите, чтобы забрать";
   private static final String SELL_DISCOUNT_PERCENT_ARGUMENT_KEY = "процент";

   private void sendPendingSellCommand(PlayerTickEvent event) {
      if (this.pendingSellCommand != null && MINECRAFT_CLIENT.player != null) {
         if (MINECRAFT_CLIENT.gui.screen() != null) {
            MINECRAFT_CLIENT.player.closeContainer();
         } else {
            MINECRAFT_CLIENT.player.connection.sendCommand(this.pendingSellCommand);
            this.pendingSellCommand = null;
         }
      }
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      ((LiteralArgumentBuilder)commandBuilder.executes(commandContext -> {
         ItemStack heldStack = MINECRAFT_CLIENT.player.getMainHandItem();
         if (!heldStack.isEmpty()) {
            MINECRAFT_CLIENT.player.connection.sendCommand("ah search " + this.normalizeSearchName(heldStack));
         }

         return 1;
      })).then(this.literal(SELL_LITERAL).executes(commandContext -> {
         this.prepareSellRequest(0.0F);
         return 1;
      }).then(this.argument(SELL_DISCOUNT_PERCENT_ARGUMENT_NAME, FloatArgumentType.floatArg()).executes(commandContext -> {
         this.prepareSellRequest(FloatArgumentType.getFloat(commandContext, SELL_DISCOUNT_PERCENT_ARGUMENT_KEY));
         return 1;
      })));
   }

   private void queueSellCommand(AuctionSellCommand.SellRequest request, int unitPrice) {
      long totalPrice = Math.max(1L, Math.round(unitPrice * (1.0 - request.percent() / PERCENT_DIVISOR) * Math.max(1, request.count())));
      String itemName = request.entry().name();
      ClientChat.З("Выставляю &c" + itemName + " &7за &c" + totalPrice + " &7(-" + itemName + "% от " + (int)request.percent() + ")");
      this.pendingSellCommand = "ah sell " + totalPrice;
   }

   private void handleAuctionContents(PacketReceiveEvent event) {
      if (this.pendingSellRequest != null && this.pendingSellCommand == null && event.getPacket() != null) {
         if (event.getPacket() instanceof ClientboundContainerSetContentPacket contentsPacket && contentsPacket.containerId() != 0) {
            List<ItemStack> containerItems = contentsPacket.items();
            List<Integer> unitPrices = containerItems.subList(0, Math.max(0, containerItems.size() - 36))
               .stream()
               .filter(
                  candidateStack -> this.pendingSellRequest.entry().matches(candidateStack)
                     && candidateStack.getTooltipLines(TooltipContext.EMPTY, MINECRAFT_CLIENT.player, TooltipFlag.NORMAL).stream().noneMatch(tooltipLine -> tooltipLine.getString().contains(COLLECT_LISTING_TOOLTIP_MARKER))
               )
               .mapToInt(AuctionSellCommand::readListingUnitPrice)
               .filter(unitPrice -> unitPrice > 0)
               .sorted()
               .boxed()
               .toList();
            if (!unitPrices.isEmpty()) {
               int referenceUnitPrice = (Integer)unitPrices.get(Math.min(2, unitPrices.size() - 1));
               int selectedUnitPrice = unitPrices.stream().filter(candidateUnitPrice -> candidateUnitPrice.intValue() >= referenceUnitPrice * PRICE_OUTLIER_THRESHOLD_RATIO).findFirst().orElse(referenceUnitPrice);
               this.priceSamples.removeIf(priceSample -> priceSample.entry() == this.pendingSellRequest.entry());
               this.priceSamples.add(new AuctionSellCommand.PriceSample(this.pendingSellRequest.entry(), selectedUnitPrice, System.currentTimeMillis()));
               this.queueSellCommand(this.pendingSellRequest, selectedUnitPrice);
            }

            this.pendingSellRequest = null;
         }
      }
   }

   public AuctionSellCommand() {
      super(COMMAND_NAME);
   }

   private String normalizeSearchName(ItemStack stack) {
      String hoverName = stack.getHoverName().getString();
      return hoverName.replaceAll(FORMATTING_CODE_PATTERN, FORMATTING_REPLACEMENT).replaceAll(SIZE_TAG_PATTERN, SIZE_TAG_REPLACEMENT).replaceAll(BRACKET_TAG_PATTERN, BRACKET_TAG_REPLACEMENT).replaceAll(XXX_WORD_PATTERN, XXX_WORD_REPLACEMENT).replaceAll(NAME_PUNCTUATION_PATTERN, PUNCTUATION_REPLACEMENT).trim().replaceAll(NAME_WHITESPACE_PATTERN, NAME_WHITESPACE_REPLACEMENT);
   }

   private static int readListingUnitPrice(ItemStack stack) {
      if (stack.isEmpty()) {
         return -1;
      }

      List<String> tooltipLines = stack.getTooltipLines(TooltipContext.EMPTY, MINECRAFT_CLIENT.player, TooltipFlag.NORMAL).stream().skip(1L).map(tooltipLine -> tooltipLine.getString()).toList();
      if (!stack.getItemName().getString().contains(STALE_LISTING_MESSAGE) && stack.getItem() != Items.DYE.gray()) {
         for (String tooltipLine : tooltipLines) {
            if (tooltipLine.contains(PRICE_TOOLTIP_MARKER)) {
               String priceText = tooltipLine.substring(tooltipLine.indexOf(PRICE_TOOLTIP_PREFIX) + PRICE_TOOLTIP_PREFIX_LENGTH_TEXT.length()).trim().replace(PRICE_GROUP_SEPARATOR, "").replace(CURRENCY_SYMBOL, "").replaceAll(PRICE_WHITESPACE_PATTERN, "");
               if (!priceText.isEmpty()) {
                  try {
                     return Integer.parseInt(priceText) / (stack.getCount() > 0 ? stack.getCount() : 1);
                  } catch (NumberFormatException parseError) {
                     return -1;
                  }
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private void prepareSellRequest(float discountPercent) {
      ItemStack heldStack = MINECRAFT_CLIENT.player.getMainHandItem();
      FunTimeItemCatalog.CatalogEntry catalogEntry = FunTimeItemCatalog.Т(heldStack);
      if (catalogEntry == null) {
         ClientChat.З(UNSUPPORTED_ITEM_MESSAGE);
      } else {
         AuctionSellCommand.SellRequest request = new AuctionSellCommand.SellRequest(catalogEntry, heldStack.getCount(), discountPercent);
         AuctionSellCommand.PriceSample cachedPriceSample = this.priceSamples
            .stream()
            .filter(priceSample -> priceSample.entry() == catalogEntry && System.currentTimeMillis() - priceSample.timestamp() < PRICE_SAMPLE_TTL_MILLIS)
            .findFirst()
            .orElse(null);
         if (cachedPriceSample != null) {
            this.queueSellCommand(request, cachedPriceSample.price());
         } else {
            MINECRAFT_CLIENT.player.connection.sendCommand("ah search " + this.normalizeSearchName(heldStack));
            this.pendingSellRequest = request;
         }
      }
   }

   private record PriceSample(FunTimeItemCatalog.CatalogEntry entry, int price, long timestamp) {
   }

   private record SellRequest(FunTimeItemCatalog.CatalogEntry entry, int count, float percent) {
   }
}
